/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

/**
 * 存放一些节点操作的公共方法
 */
import { Shape, DataUri } from '@antv/x6';
import { History } from '@antv/x6-plugin-history';
import { Export } from '@antv/x6-plugin-export';
import { Selection } from '@antv/x6-plugin-selection';
import '@/assets/system/styles/global.scss';
import { baseConfig, cuPort } from '@/utils/graph';
/**
 * 插件使用
 */
export const usePlugins = (graph) => {
    graph
        .use(
            new History({
                enabled: true
            })
        )
        .use(
            new Selection({
                enabled: true,
                rubberband: true,
                showNodeSelectionBox: true
            })
        )
        .use(new Export());
};
/**
 * 画布缩放比例
 * @param {*} graph
 * @returns
 */
export const getCanvasScale = (graph) => {
    const scaleValue = graph.zoom();
    let result = parseFloat(scaleValue * 100).toFixed(0);
    return result;
};

/**
 * 自定义html节点
 */
export const useHtmlNode = () => {
    Shape.HTML.register({
        shape: 'cu-data-node',
        width: 180,
        height: 60,
        html(cell) {
            const { name: nodeName, createPerson, icon, length } = cell.getData();
            const htmlContainer = document.createElement('div');
            htmlContainer.setAttribute('class', 'cu_html_container');
            // 左侧的图片
            const htmlTop = document.createElement('img');
            htmlTop.setAttribute('class', 'cu_html_top');
            // const imgUrl = 'https://lf3-cdn-tos.bytescm.com/obj/static/xitu_juejin_web/24127194d5b158d7eaf8f09a256c5d01.svg';
            htmlTop.setAttribute('src', icon);
            DataUri.imageToDataUri(icon, function (nu, url) {
                htmlTop.src = url;
            });

            // 右侧的文本区域
            const htmlText = document.createElement('div');
            htmlText.setAttribute('class', 'cu_html_text');

            // 标题
            const htmlTitle = document.createElement('div');
            htmlTitle.setAttribute('class', 'cu_html_title');
            htmlTitle.innerText = nodeName;

            // 添加 zIndex 到文本区域
            const htmlZIndex = document.createElement('div');
            htmlZIndex.setAttribute('class', 'cu_html_zIndex');
            htmlZIndex.innerText = `${String(length).padStart(3, '0')}`;

            // 组合文字内容
            htmlText.appendChild(htmlTitle);
            htmlText.appendChild(htmlZIndex); // 将 zIndex 添加到文本区域

            // 组合整个节点
            htmlContainer.appendChild(htmlTop);
            htmlContainer.appendChild(htmlText);

            return htmlContainer;
        }
    });
};

/**
 * 显示节点上的连接桩
 * @param {*} ports
 * @param {*} show
 */
export const showPorts = (ports, show) => {
    for (let i = 0, len = ports.length; i < len; i = i + 1) {
        ports[i].style.visibility = show ? 'visible' : 'hidden';
    }
};

/**
 * 画布清空
 */
export const handleRmNodes = (graph) => {
    graph.clearCells();
};

/**
 * 转换画布节点数据为所需格式
 */
export const transNodeData = (graph) => {
    let allNodes = JSON.parse(JSON.stringify(graph.getNodes())); // 深拷贝节点数据
    let allEdges = JSON.parse(JSON.stringify(graph.getEdges())); // 获取边数据

    console.log(allNodes);
    console.log(allEdges);

    const tailNodes = {};
    allEdges.forEach((item) => {
        const targetId = item.target.cell;
        tailNodes[targetId] = true;
    });

    const isHeadNode = (code) => !tailNodes[code];

    let locations = [];
    let tasksMap = {};
    let taskDefinitionList = []; // 初始化 taskDefinitionList

    // 处理节点数据
    allNodes.forEach((item) => {
        if (item.shape === 'cu-data-node') {
            const code = item.id;
            locations.push({
                taskCode: item.id,
                x: item.position.x,
                y: item.position.y,
                ports: item.ports?.items?.map((port) => port.id) || []
            });
            tasksMap[code] = item.data;

            // 确保每个节点的 data 中包含 code
            taskDefinitionList.push({
                ...item.data,
                code: code // 添加 code 字段
            });
        }
    });

    let taskRelationJson = allNodes
        .filter((node) => isHeadNode(node.id))
        .map((node) => {
            const task = tasksMap[node.id];
            console.log('🚀 ~ .map ~ task:', task);

            return {
                name: '',
                preTaskCode: 0,
                preTaskVersion: 0,
                postTaskCode: task.code,
                postTaskVersion: task.version || 0,
                conditionType: 'NONE',
                conditionParams: {}
            };
        });

    // 处理边数据
    allEdges.forEach((item) => {
        if (item.shape === 'edge') {
            console.log('🚀 ~ transNodeData ~ item:', item);
            const sourceId = item.source.cell;
            const targetId = item.target.cell;
            const prevTask = tasksMap[sourceId];
            const task = tasksMap[targetId];
            const source = item.source;
            const target = item.target;
            console.log('🚀 ~ transNodeData ~ item:', item);

            // 保存源端口信息和目标端口信息以便回显
            taskRelationJson.push({
                name: '',
                id: item.id,
                preTaskCode: prevTask.code,
                preTaskVersion: prevTask.version || 0,
                postTaskCode: task.code,
                postTaskVersion: task.version || 0,
                conditionType: 'NONE',
                conditionParams: {},
                source: sourceId, // 只保存 ID
                target: targetId, // 只保存 ID
                targetport: target,
                sourcetport: source
            });
        }
    });

    return {
        locations,
        processTaskRelationList: taskRelationJson,
        taskDefinitionList // 返回 taskDefinitionList
    };
};

/**
 * 使用 graph.fromJSON 还原transNodeData处理过的数据流程图画布
 */
export const renderGraph = (graph, savedData) => {
    // 在渲染新节点和边之前，先清空画布
    graph.clearCells();

    // 还原节点
    savedData.locations.forEach((location) => {
        const nodeData = savedData.taskDefinitionList.find(
            (item) => item.code === location.taskCode
        );
        if (nodeData) {
            const node = graph.addNode({
                id: location.taskCode, // 使用保存的 ID
                shape: 'cu-data-node',
                x: location.x,
                y: location.y,
                width: 150,
                height: 50,
                data: nodeData,
                ports: {
                    ...cuPort,
                    items: [
                        { group: 'top', id: 'port-top' },
                        { group: 'left', id: 'port-left' },
                        { group: 'right', id: 'port-right' },
                        { group: 'bottom', id: 'port-bottom' }
                    ]
                }
            });
        } else {
            console.log(`未找到 taskCode 为 ${location.taskCode} 的节点数据`);
        }
    });

    // 还原边
    savedData.processTaskRelationList.forEach((relation) => {
        const sourceNode = graph.getCellById(relation.source);
        const targetNode = graph.getCellById(relation.target);
        if (sourceNode && targetNode) {
            graph.addEdge({
                id: relation.id,
                source: relation.sourcetport,
                target: relation.targetport,
                data: relation,
                attrs: {
                    line: {
                        stroke: '#409eff', // 边的颜色
                        strokeWidth: 1,
                        targetMarker: { name: 'block', width: 12, height: 8 }
                    }
                }
            });
        } else {
            console.log(`未找到源节点或目标节点`);
        }
    });
};
