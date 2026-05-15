function isPlainObject(value) {
  return Object.prototype.toString.call(value) === "[object Object]";
}

function normalizeList(value) {
  return Array.isArray(value) ? value.filter(Boolean) : [];
}

export function cloneNodeData(data = {}) {
  return JSON.parse(JSON.stringify(data));
}

export function normalizeTemplateReferenceSegment(value = "", fallback = "node") {
  const normalized = `${value || ""}`
    .trim()
    .replace(/[^A-Za-z0-9_]+/g, "_")
    .replace(/^_+|_+$/g, "");
  const fallbackValue = `${fallback || ""}`
    .trim()
    .replace(/[^A-Za-z0-9_]+/g, "_")
    .replace(/^_+|_+$/g, "");
  const nextValue = normalized || fallbackValue || "node";

  return /^[0-9]/.test(nextValue) ? `node_${nextValue}` : nextValue;
}

function buildUniqueNodeId(baseValue = "", usedValues = new Set()) {
  const normalizedBase = normalizeTemplateReferenceSegment(baseValue, "node");
  let candidate = normalizedBase;
  let suffix = 1;

  while (!candidate || usedValues.has(candidate)) {
    candidate = `${normalizedBase || "node"}_${suffix}`;
    suffix += 1;
  }

  usedValues.add(candidate);
  return candidate;
}

function mergeMaps(maps = []) {
  const merged = new Map();

  maps
    .filter((item) => item instanceof Map)
    .forEach((map) => {
      map.forEach((value, key) => {
        if (key && value) {
          merged.set(`${key}`, `${value}`);
        }
      });
    });

  return merged;
}

function resolveNodeId(value = "", nodeIdMaps = []) {
  const normalizedValue = `${value || ""}`.trim();

  if (!normalizedValue) {
    return "";
  }

  for (let index = nodeIdMaps.length - 1; index >= 0; index -= 1) {
    const map = nodeIdMaps[index];

    if (map instanceof Map && map.has(normalizedValue)) {
      return map.get(normalizedValue);
    }
  }

  return normalizeTemplateReferenceSegment(normalizedValue, normalizedValue);
}

function normalizeContextSourceId(value = "", nodeIdMaps = []) {
  const normalizedValue = `${value || ""}`.trim();

  if (!normalizedValue) {
    return "";
  }

  return normalizedValue
    .split("::")
    .filter(Boolean)
    .map((segment) => resolveNodeId(segment, nodeIdMaps))
    .join("::");
}

function replaceTemplateReferenceSources(value = "", segmentMaps = []) {
  const currentValue = `${value || ""}`;

  if (!currentValue.includes("{{")) {
    return currentValue;
  }

  const segmentMap = mergeMaps(segmentMaps);

  if (!segmentMap.size) {
    return currentValue;
  }

  return currentValue.replace(
    /\{\{\s*([A-Za-z0-9_:-]+)\.([A-Za-z0-9_.]+)\s*\}\}/g,
    (matchedValue, sourceSegment, variablePath) => {
      const normalizedSourceSegment = `${sourceSegment || ""}`.trim();

      if (!normalizedSourceSegment) {
        return matchedValue;
      }

      const nextSourceSegment =
        segmentMap.get(normalizedSourceSegment) ||
        segmentMap.get(
          normalizeTemplateReferenceSegment(
            normalizedSourceSegment,
            normalizedSourceSegment
          )
        );

      return nextSourceSegment
        ? `{{ ${nextSourceSegment}.${variablePath} }}`
        : matchedValue;
    }
  );
}

function normalizeWorkflowValue(value, context = {}, currentKey = "") {
  if (Array.isArray(value)) {
    return value.map((item) =>
      normalizeWorkflowValue(item, context, currentKey)
    );
  }

  if (typeof value === "string") {
    if (currentKey === "sourceNodeId" || currentKey === "nodeId") {
      return normalizeContextSourceId(value, context.nodeIdMaps || []);
    }

    return replaceTemplateReferenceSources(value, context.segmentMaps || []);
  }

  if (!isPlainObject(value)) {
    return value;
  }

  const nextValue = {};
  const hasLoopGraph =
    Array.isArray(value.loopNodes) || Array.isArray(value.loopEdges);

  if (hasLoopGraph) {
    const normalizedLoopGraph = normalizeWorkflowGraphIds(
      {
        nodes: value.loopNodes,
        edges: value.loopEdges,
      },
      context
    );

    nextValue.loopNodes = normalizedLoopGraph.nodes;
    nextValue.loopEdges = normalizedLoopGraph.edges;
  }

  Object.keys(value).forEach((key) => {
    if (key === "loopNodes" || key === "loopEdges") {
      return;
    }

    nextValue[key] = normalizeWorkflowValue(value[key], context, key);
  });

  return nextValue;
}

export function normalizeWorkflowGraphIds(graph = {}, parentContext = {}) {
  const rawNodes = Array.isArray(graph?.nodes) ? cloneNodeData(graph.nodes) : [];
  const rawEdges = Array.isArray(graph?.edges) ? cloneNodeData(graph.edges) : [];
  const usedNodeIds = new Set();
  const localNodeIdMap = new Map();
  const localSegmentMap = new Map();

  const normalizedNodes = rawNodes.map((node, index) => {
    const nextNode = isPlainObject(node) ? { ...node } : {};
    const rawNodeId = `${nextNode.id || ""}`.trim();
    const fallbackId = `${nextNode.type || "node"}_${index + 1}`;
    const normalizedNodeId = buildUniqueNodeId(
      rawNodeId || fallbackId,
      usedNodeIds
    );

    if (rawNodeId) {
      localNodeIdMap.set(rawNodeId, normalizedNodeId);
      localSegmentMap.set(rawNodeId, normalizedNodeId);
      localSegmentMap.set(
        normalizeTemplateReferenceSegment(rawNodeId, fallbackId),
        normalizedNodeId
      );
    }

    localNodeIdMap.set(normalizedNodeId, normalizedNodeId);
    localSegmentMap.set(normalizedNodeId, normalizedNodeId);

    return {
      ...nextNode,
      id: normalizedNodeId,
    };
  });

  const nextContext = {
    nodeIdMaps: [...(parentContext.nodeIdMaps || []), localNodeIdMap],
    segmentMaps: [...(parentContext.segmentMaps || []), localSegmentMap],
  };

  const normalizedEdgeList = rawEdges.map((edge) => {
    if (!isPlainObject(edge)) {
      return edge;
    }

    const rawSource = `${edge.source || ""}`.trim();
    const rawTarget = `${edge.target || ""}`.trim();

    return {
      ...edge,
      source: rawSource ? resolveNodeId(rawSource, [localNodeIdMap]) : rawSource,
      target: rawTarget ? resolveNodeId(rawTarget, [localNodeIdMap]) : rawTarget,
    };
  });

  return {
    ...graph,
    nodes: normalizedNodes.map((node) => ({
      ...node,
      data: normalizeWorkflowValue(node.data, nextContext),
    })),
    edges: normalizedEdgeList,
  };
}

export function omitObjectKeys(source = {}, keys = []) {
  if (!isPlainObject(source)) {
    return {};
  }

  const keySet = new Set(keys);

  return Object.keys(source).reduce((result, key) => {
    if (!keySet.has(key)) {
      result[key] = source[key];
    }

    return result;
  }, {});
}

export function getNodeConfig(data = {}) {
  return isPlainObject(data?.config) ? data.config : {};
}

export function getNodeInput(data = {}) {
  return normalizeList(data?.input);
}

export function getNodeOutput(data = {}) {
  return normalizeList(data?.output);
}

export function createStructuredNodeData({
  input = [],
  config = {},
  output = [],
} = {}) {
  return {
    input: normalizeList(input),
    config: isPlainObject(config) ? { ...config } : {},
    output: normalizeList(output),
  };
}

export function getNodeConfigValue(data = {}, field = "", fallback = undefined) {
  if (!field) {
    return fallback;
  }

  const config = getNodeConfig(data);

  if (config[field] !== undefined) {
    return config[field];
  }

  if (data && data[field] !== undefined) {
    return data[field];
  }

  return fallback;
}

export function getNodeLabel(data = {}, fallback = "") {
  return `${getNodeConfigValue(data, "label", fallback) || ""}`.trim() || fallback;
}

export function getNodeDescription(data = {}, fallback = "") {
  return `${getNodeConfigValue(data, "description", fallback) || ""}`.trim();
}

export function setNodeConfigValue(data = {}, field = "", value) {
  return createStructuredNodeData({
    input: getNodeInput(data),
    config: {
      ...getNodeConfig(data),
      [field]: value,
    },
    output: getNodeOutput(data),
  });
}

export function mergeNodeConfig(data = {}, patch = {}) {
  return createStructuredNodeData({
    input: getNodeInput(data),
    config: {
      ...getNodeConfig(data),
      ...(isPlainObject(patch) ? patch : {}),
    },
    output: getNodeOutput(data),
  });
}

export function setNodeInput(data = {}, input = []) {
  return createStructuredNodeData({
    input,
    config: getNodeConfig(data),
    output: getNodeOutput(data),
  });
}

export function setNodeOutput(data = {}, output = []) {
  return createStructuredNodeData({
    input: getNodeInput(data),
    config: getNodeConfig(data),
    output,
  });
}
