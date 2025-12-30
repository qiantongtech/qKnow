<!-- index.vue -->
<template>
  <div
    class="app-container"
    ref="app-container"
    v-loading="loadingInstance"
    style="background-color: #f0f2f5"
  >
    <div class="custom-card">
      <div class="steps-inner">
        <ul class="zl-step">
          <li
            v-for="(item, index) in stepsList"
            :key="index"
            :class="{
              statusEnd: activeReult === index,
              prevStep: index < activeReult,
              cur: index > activeReult,
            }"
          >
            <div
              class="step-circle"
              :class="{
                active: activeReult === index,
                prev: index < activeReult,
              }"
            >
              <span>{{ index + 1 }}</span>
            </div>

            <!-- 步骤名称 -->
            <span class="step-name">{{ item.name }}</span>
          </li>
        </ul>
      </div>
    </div>

    <div class="pagecont-top" v-show="showSearch">
      <div class="infotop">
        <div class="main">
          <!-- 第一步：基础信息 -->
          <BasicInfoForm
            v-show="activeReult == 0"
            ref="extStructRef"
            :form-data="form"
            :rules="rules"
            :data-source-list="dataSourceList"
            :update-type-options="ext_update_type"
            :disabled="!!route.query.info"
            :connection-success="connectionSuccess"
            :connection-error="connectionError"
            @data-source-change="changeDataSource"
            @test-connection="testConnection"
            @show-cron-dialog="handleShowCron"
            @update:form-data="(value) => (form = value)"
          />

          <!-- 第二步：表映射 -->
          <div v-show="activeReult == 1">
            <TableMapping
              :table-data="tableData"
              :mapping-status-options="ext_mapping_status"
              @import-table="openImportTable"
              @mapping-click="mappingClick"
              @delete-click="tableDataDeleteClick"
            />
          </div>

          <!-- 第三步：关系映射 -->
          <div v-show="activeReult == 2">
            <RelationMapping
              ref="relationFormRef"
              :form-data="relationForm"
              :rules="relationRules"
              :table-data="tableData"
              :db-table-list="dbTableList"
              :relation-type-options="relationTypeOptions"
              :loading="loading"
              :should-show-form-labels="shouldShowFormLabels"
              @add-relation="addRelationItem"
              @delete-relation="deleteItem"
              @relation-type-change="handleRelationTypeChange"
              @table-change="handleTableChange"
              @add-intermediate="addIntermediateTable"
              @remove-intermediate="removeIntermediateTable"
              @intermediate-table-change="handleIntermediateTableChange"
              @update:form-data="(value) => (relationForm = value)"
            />
          </div>
        </div>
        <div class="button-style">
          <el-button type="primary" @click="handleSuccess">返回列表</el-button>
          <el-button v-if="activeReult != 0" @click="handleLastStep"
            >上一步</el-button
          >
          <el-button
            type="primary"
            v-if="activeReult === 2 && !route.query.info"
            @click="submitForm"
            :loading="loadingOptions.loading"
          >
            确定并退出
          </el-button>
          <el-button v-if="activeReult !== 2" @click="handleNextStep"
            >下一步</el-button
          >
        </div>
      </div>
    </div>
    <el-dialog
      title="Cron表达式生成器"
      v-model="openCron"
      :append-to="$refs['app-container']"
      destroy-on-close
    >
      <Crontab
        ref="crontabRef"
        @hide="openCron = false"
        @fill="crontabFill"
        :expression="updateFrequency"
      >
      </Crontab>
    </el-dialog>

    <ImportTable
      class="custom-import"
      ref="importRef"
      @importDataTable="importDataTable"
    />
    <Mapping class="custom-mapping" ref="MappingRef" @ok="mappingOk" />
  </div>
</template>

<script setup name="qualityTask">
import { ref, reactive, toRefs, onMounted, watch, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import Crontab from "@/components/Crontab/index.vue";
import {
  listDaDatasource,
  clientsTest,
} from "@/api/da/datasource/daDatasource";
import { getDaDatasourceTableList } from "@/api/da/datasource/daDatasource";
import {
  addExtStructDataMapping,
  updateExtStructDataMapping,
  getExtStruct,
} from "@/api/ext/extStructTask/extStruct";
import { getColumnsList } from "@/api/da/datasource/daDatasource";
import { listRelation } from "@/api/ext/extSchemaRelation/relation";

// 导入子组件
import BasicInfoForm from "./basicInfoForm.vue";
import TableMapping from "./tableMapping.vue";
import RelationMapping from "./relationMapping.vue";

//导入表页面
import ImportTable from "../importTable.vue";
//数据映射页面
import Mapping from "../mapping.vue";

const { proxy } = getCurrentInstance();
const { ext_mapping_status, ext_update_type } = proxy.useDict(
  "ext_mapping_status",
  "ext_update_type"
);
const route = useRoute();
const loading = ref(false);
const showSearch = ref(true);
const router = useRouter();
let loadingInstance = ref(null); // 全局 loading 实例

const extStructRef = ref();
const relationFormRef = ref();
const dataSourceList = ref(null); //数据源列表
const updateFrequency = ref("");
const connectionSuccess = ref(false);
const connectionError = ref(false);
const open = ref(false);
const openCron = ref(false);
const tableData = ref([]);
const dbTableList = ref([]);
const structTaskStatus = ref(null);
const queryParams = ref(null);
const emit = defineEmits(["ok", "close-dialog"]);

const data = reactive({
  //基本信息
  form: {
    dataSourceId: null,
    updateType: 0,
  },
  //关系映射
  relationForm: {
    dataList: [],
  },
  stepsList: [
    { name: "基础信息", id: 0 },
    { name: "表映射", id: 1 },
    { name: "关系映射", id: 2 },
  ],
  relationTypeOptions: [
    { label: "一对一/一对多", value: 1 },
    { label: "多对多", value: 2 },
  ],
  rules: {
    name: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
    dataSourceId: [
      { required: true, message: "数据源不能为空", trigger: "blur" },
    ],
    updateType: [
      { required: true, message: "更新类型不能为空", trigger: "blur" },
    ],
    updateFrequency: [
      { required: true, message: "更新频率不能为空", trigger: "blur" },
    ],
  },
  // 关系映射的验证规则
  relationRules: {
    type: [{ required: true, message: "请选择关系类型", trigger: "change" }],
    "source.tableName": [
      { required: true, message: "请选择源表", trigger: "change" },
    ],
    "source.fieldName": [
      { required: true, message: "请选择源表关联字段", trigger: "change" },
    ],
    "target.tableName": [
      { required: true, message: "请选择目标表", trigger: "change" },
    ],
    "target.fieldName": [
      { required: true, message: "请选择目标表关联字段", trigger: "change" },
    ],
    relation: [
      { required: true, message: "请选择关系描述", trigger: "change" },
    ],
    "intermediate.tableName": [
      { required: true, message: "请选择中间表", trigger: "change" },
    ],
    "intermediate.tableField": [
      { required: true, message: "请选择连接源表的字段", trigger: "change" },
    ],
    "intermediate.relationField": [
      { required: true, message: "请选择连接目标表的字段", trigger: "change" },
    ],
  },

  activeReult: 0,
  active: 0,
  loadingOptions: { loading: false },
});
const {
  form,
  stepsList,
  activeReult,
  loadingOptions,
  relationForm,
  relationTypeOptions,
  rules,
  relationRules,
} = toRefs(data);

onMounted(() => {
  getDataSourceList(); //获取数据源
});

// 监听 id 变化
watch(
  () => route.query.taskId,
  (newId) => {
    if (newId) {
      getOpenPage(newId);
    }
  },
  { immediate: true } //
);

// 计算是否显示表单标签
const shouldShowFormLabels = computed(() => {
  if (
    !relationForm.value.dataList ||
    relationForm.value.dataList.length === 0
  ) {
    return false;
  }

  // 检查所有数据项
  for (const data of relationForm.value.dataList) {
    // 检查是否有带 label 的表单项
    const hasRelationType = data.type !== undefined;
    const hasSourceTable = data.source?.tableName;
    const hasTargetTable = data.target?.tableName;
    const hasRelationDesc = data.relation;
    const hasIntermediateTable =
      data.type === 2 && data.relationMappingMiddle?.length > 0;

    // 只要任意一个有值，就需要显示 label
    if (
      hasRelationType ||
      hasSourceTable ||
      hasTargetTable ||
      hasRelationDesc ||
      hasIntermediateTable
    ) {
      return true;
    }
  }

  return false;
});

/**
 * 进入页面
 */
function getOpenPage(id) {
  proxy.resetForm("extStructRef");
  structTaskStatus.value = null;
  form.value = {
    updateType: 0,
  };
  connectionSuccess.value = null;
  connectionError.value = null;
  tableData.value = [];
  if (id) {
    getExtStructInfo(id);
  } else {
    form.value.dataSourceId =
      dataSourceList.value.length > 0 ? dataSourceList.value[0].id : null;
  }
  // open.value = true;
}

/**
 * 查询任务信息
 */
async function getExtStructInfo(taskId) {
  form.value = {
    dataSourceId: null,
    updateType: 0,
  };

  await getExtStruct(taskId).then(async (res) => {
    let structTask = res.data.structTask;
    structTaskStatus.value = structTask.status;

    await getTableName(structTask.datasourceId);

    let formData = {
      id: taskId,
      name: structTask.name,
      dataSourceId: structTask.datasourceId,
      remark: structTask.remark,
      updateType: structTask.updateType,
      updateFrequency: structTask.updateFrequency,
    };
    form.value = formData;

    //导入过的表
    let tableMappingList = res.data.tableMappingList;

    let schemaMappingList = res.data.schemaMappingList;
    let attributeMappingList = res.data.attributeMappingList;
    let relationMappingList = res.data.relationMappingList;

    tableMappingList.forEach((e) => {
      e.operate = e.schemaName;
      if (!e.tableComment) {
        dbTableList.value.forEach((db) => {
          if (e.tableName == db.tableName) {
            e.tableComment = db.tableComment;
          }
        });
      }
      e.status = e.status == true ? 1 : 0;

      let mappingData = {};
      // 对应概念
      schemaMappingList.forEach((sch) => {
        if (e.tableName == sch.tableName) {
          mappingData.concept = sch.schemaId;
          mappingData.conceptName = sch.schemaName;
          mappingData.entityNameField = sch.entityNameField;
          mappingData.primaryKey = sch.primaryKey;
          return;
        }
      });

      // 属性映射
      let attList = [];
      let attributeMapping = attributeMappingList.filter(
        (item) => e.tableName == item.tableName && item.attributeId
      );
      attributeMapping.forEach((e) => {
        attList.push({
          field: e.fieldName,
          fieldDescription: e.fieldComment,
          conceptId: e.attributeId,
          conceptName: e.attributeName,
        });
      });
      mappingData.attributeList = attList;

      //关系映射
      let relationMapping = relationMappingList.filter(
        (item) => e.tableName == item.tableName
      );
      mappingData.relationshipList = relationMapping;
      e.mappingData = mappingData;
    });
    tableData.value = tableMappingList;

    console.log("--回显任务数据---", tableData.value);
  });
}

/**
 * 查询数据源的所有数据表，用于中间表使用
 */
const getTableName = async (datasourceId) => {
  try {
    const res = await getDaDatasourceTableList(datasourceId);
    const data = res.data;

    // 从 tableData 中提取出已有的表名
    const existingTableNames = new Set(tableData.value.map((e) => e.tableName));

    // 过滤出不在 existingTableNames 中的表
    const filteredTables = data.filter(
      (item) => !existingTableNames.has(item.tableName)
    );

    // 更新 dbTableList 只包含未被剔除的表
    dbTableList.value = filteredTables;

    console.log("--数据源表---dbTableList---", dbTableList.value);
  } catch (error) {
    console.error("Error fetching table names:", error);
  }
};

/**
 * 获取数据源
 */
function getDataSourceList() {
  listDaDatasource().then((res) => {
    dataSourceList.value = res.data.list;
    // 默认选中第一个选项
    if (res.data.list.length > 0) {
      form.value.dataSourceId = res.data.list[0].id;
    }
  });
}
/**
 * 测试连接
 */
function testConnection() {
  if (!form.value.dataSourceId) {
    proxy.$modal.msgWarning("测试连接不能为空");
  } else {
    clientsTest(form.value.dataSourceId).then((res) => {
      if (res && res.code == 200) {
        connectionSuccess.value = true;
      } else {
        connectionError.value = true;
      }
    });
  }
}
/**
 * 切换数据源
 */
function changeDataSource() {
  connectionSuccess.value = false;
  connectionError.value = false;
}

/** cron表达式按钮操作 */
function handleShowCron() {
  updateFrequency.value = form.value.updateFrequency;
  openCron.value = true;
}

//-----------------------------------------------------表映射页面方法-------------------------------------------------------------
/** 确定后回传值 */
async function crontabFill(value) {
  form.value.updateFrequency = value;
  await nextTick();
  extStructRef.value?.validateField("updateFrequency");
}

/** 打开导入表弹窗 */
function openImportTable() {
  proxy.$refs["importRef"].show({ dataSourceId: form.value.dataSourceId });
}

/**
 * 导入表
 * @param val
 */
function importDataTable(val) {
  console.log("-------dataTableList---111------", val);
  val.forEach((e) => {
    // 检查 tableData 中是否已经包含该 tableName
    const exists = tableData.value.some(
      (item) => item.tableName === e.tableName
    );
    if (!exists) {
      // 如果没有重复的表名，则添加新的表格数据
      tableData.value.push({
        tableName: e.tableName,
        tableComment: e.tableComment,
        operate: null,
        status: 0,
        mappingData: null,
      });
    }
  });
}

/**
 * 映射
 * @param row
 */
function mappingClick(row) {
  console.log("--点击映射--mappingClick----row---", row);
  let tables = [];
  tableData.value.forEach((e) => {
    tables.push({
      tableName: e.tableName,
      tableComment: e.tableName + "(" + e.tableComment + ")",
    });
  });
  proxy.$refs["MappingRef"].show({
    title: `数据映射 - ${row.tableName} (${row.tableComment})`,
    dataSourceId: form.value.dataSourceId,
    tableName: row.tableName,
    tableComment: row.tableComment,
    status: row.status,
    tables: tables,
    mappingData: row.mappingData,
    timeFieldShow: form.value.updateType == 1 ? true : false,
    primaryKey: row.primaryKey,
  });
}

/**
 * 删除导入的表
 * @param row
 */
function tableDataDeleteClick(row) {
  tableData.value = tableData.value.filter((e) => e.tableName != row.tableName);
}

/**
 * 数据映射确定
 *
 * @param row
 */
const mappingOk = (val) => {
  console.log("-----mappingData----", val);
  tableData.value.forEach((tableItem) => {
    if (tableItem.tableName == val.tableName) {
      tableItem.operate = val.conceptName;
      tableItem.status = 1;
      tableItem.mappingData = val;
      tableItem.entityTimeField = val.entityTimeField;
      tableItem.entityNameField = val.entityNameField;
      tableItem.primaryKey = val.primaryKey;
    }
  });
};

//-----------------------------------------------------关系映射页面方法-------------------------------------------------------------

// 获取关系映射列表数据
async function getRealtionMappingList() {
  loading.value = true; // 建议在这里开启 loading，逻辑更清晰
  try {
    const relationList = [];
    const tableFieldRequests = []; // 存储所有需要查询的表

    // 第一步：收集所有需要查询的表
    for (const tableItem of tableData.value) {
      if (!tableItem.mappingData.concept) {
        continue;
      }

      // 关系数据判断，如果 relationshipList 为 null 或 undefined，说明是新增需要查询数据。
      let relationshipList = tableItem.mappingData.relationshipList || [];
      if (relationshipList.length === 0) {
        relationshipList = await getConceptList(tableItem.mappingData.concept);
        // 若仍为空，跳过
        if (relationshipList.length === 0) continue;
      }

      for (const relationItem of relationshipList) {
        tableFieldRequests.push({
          tableName: tableItem.tableName,
          relationTable: relationItem.relationTable,
          relationItem: relationItem,
          tableItem: tableItem,
          concept: tableItem.mappingData.concept,
        });
      }
    }

    if (tableFieldRequests.length === 0) {
      relationForm.value.dataList = [];
      return;
    }

    // 第二步：去重表名，避免重复查询
    const uniqueTables = new Set();
    tableFieldRequests.forEach((req) => {
      if (req.tableName) {
        uniqueTables.add(req.tableName);
      }
      if (req.relationTable) {
        uniqueTables.add(req.relationTable);
      }
    });

    // 第三步：批量查询所有表的字段信息
    const tableFieldsMap = new Map();
    const batchSize = 5;
    const tableArray = Array.from(uniqueTables);

    for (let i = 0; i < tableArray.length; i += batchSize) {
      const batch = tableArray.slice(i, i + batchSize);

      const batchPromises = batch.map((tableName) =>
        getColumnsDataList({
          id: form.value.dataSourceId,
          tableName: tableName,
          // type: "MYSQL",
        }).then((fields) => ({ tableName, fields }))
      );

      const results = await Promise.all(batchPromises);

      results.forEach(({ tableName, fields }) => {
        tableFieldsMap.set(tableName, fields);
      });

      // 批量之间添加短暂延迟（避免接口限流）
      if (i + batchSize < tableArray.length) {
        await sleep(200);
      }
    }

    // 第四步：构建结果
    for (const request of tableFieldRequests) {
      const sourceFields = tableFieldsMap.get(request.tableName) || [];
      const targetFields = tableFieldsMap.get(request.relationTable) || [];

      const relationshipList = await getConceptList(request.concept);

      relationList.push({
        type:
          request.relationItem.relationMappingMiddle &&
          request.relationItem.relationMappingMiddle.length > 0
            ? 2
            : 1,
        source: {
          tableName: request.relationItem.tableName
            ? request.relationItem.tableName
            : request.tableName || "",
          fieldName: request.relationItem.fieldName || "",
          fields: sourceFields,
          relations: [...(relationshipList || [])],
        },
        target: {
          tableName: request.relationItem.relationTable || "",
          fieldName: request.relationItem.relationField || "",
          fieldEntityName: request.tableItem.entityNameField || "",
          fields: targetFields,
          relations: [...(relationshipList || [])],
        },
        relationMappingMiddle: request.relationItem.relationMappingMiddle || [],
        relation: request.relationItem.relation || "",
      });
    }

    relationForm.value.dataList = relationList;
  } catch (error) {
    console.error("获取关系映射列表失败:", error);
  } finally {
    loading.value = false; // ✅ 保证一定执行
  }

  try {
    await getTableName(form.value.dataSourceId);
  } catch (err) {
    console.warn("获取表名列表失败（非阻塞）:", err);
  }
}

//根据表映射查询的概念查询所有关系数据列表
async function getConceptList(startSchemaId) {
  const dataList = [];
  //根据概念获取对应关系
  const result = await listRelation({ startSchemaId: startSchemaId });
  if (result && result.code == 200) {
    result.data.list.forEach((e) => {
      dataList.push({
        id: e.id,
        field: null,
        relation: e.relation,
        associationTable: null,
        associationTableField: null,
        associationTableEntityField: null,
        associationTableDetail: null,
      });
    });
  }
  return dataList;
}

//获取表字段列表方法
async function getColumnsDataList(data) {
  const dataList = [];
  const res = await getColumnsList(data);
  res.data.forEach((e) => {
    dataList.push({
      field: e.engName,
      fieldDescription: e.cnName,
      conceptId: null,
      conceptName: null,
    });
  });
  return dataList;
}

// 新增关系映射项（修复版）
function addRelationItem() {
  const newItem = {
    type: 1,
    source: {
      tableName: "",
      fieldName: "",
      fields: [], // 确保是空数组
      relations: [], // 确保是空数组
    },
    target: {
      tableName: "",
      fieldName: "",
      fieldEntityName: "",
      fields: [],
      relations: [],
    },
    relationMappingMiddle: [],
    relation: "",
  };
  relationForm.value.dataList.push(newItem);
}
// 统一处理表选择变化（优化版）
async function handleTableChange(index, type, tableName) {
  const currentItem = relationForm.value.dataList[index];

  if (!tableName) {
    // 如果清空选择，重置相关字段
    resetTableFields(currentItem, type, true);
    return;
  }

  const tableDataItem = tableData.value.find((e) => e.tableName === tableName);

  if (!tableDataItem || !tableDataItem.mappingData) {
    resetTableFields(currentItem, type, true);
    return;
  }

  const mappingData = tableDataItem.mappingData;

  //根据数据表查询对应的字段列数据
  const clumnsDataList = await getColumnsDataList({
    id: form.value.dataSourceId,
    tableName: tableName,
    // type: "MYSQL",
  });
  //根据数据表对应的概念查询他的关系数据
  const relationshipList = await getConceptList(mappingData.concept);

  if (type === "source") {
    // 源表切换：完全重置
    currentItem.source.fields = [...(clumnsDataList || [])];
    currentItem.source.relations = [...(relationshipList || [])];
    currentItem.source.fieldName = "";
    currentItem.relation = "";

    // 重置其他相关字段
    resetRelatedFields(currentItem, "source");
  } else if (type === "target") {
    // 目标表切换：只重置目标表字段
    currentItem.target.fields = [...(clumnsDataList || [])];
    currentItem.target.relations = [...(relationshipList || [])];
    currentItem.target.fieldName = "";
    currentItem.target.fieldEntityName = tableDataItem.entityNameField;
  }
}

// 辅助函数：重置相关字段
function resetRelatedFields(item, changedType) {
  if (changedType === "source") {
    // 清空目标表选择
    item.target.tableName = "";
    item.target.fieldName = "";
    item.target.fieldEntityName = "";
    item.target.fields = [];
    item.target.relations = [];

    // 重置中间表字段选择
    if (item.type === 2) {
      item.relationMappingMiddle.forEach((intermediate) => {
        intermediate.tableField = "";
        intermediate.relationField = "";
      });
    }
  }
}

// 辅助函数：重置表字段
function resetTableFields(item, type, clearAll = false) {
  if (type === "source") {
    item.source.fields = [];
    item.source.relations = [];
    item.source.fieldName = "";
    item.relation = "";

    if (clearAll) {
      item.source.tableName = "";
      resetRelatedFields(item, "source");
    }
  } else if (type === "target") {
    item.target.fields = [];
    item.target.relations = [];
    item.target.fieldName = "";
    item.target.fieldEntityName = "";

    if (clearAll) {
      item.target.tableName = "";
    }
  }
}

// 处理中间表选择变化
async function handleIntermediateTableChange(index, iIndex, tableName) {
  const currentItem = relationForm.value.dataList[index];
  const intermediateTable = currentItem.relationMappingMiddle[iIndex];
  const tableDataItem = dbTableList.value.find(
    (e) => e.tableName === tableName
  );
  //获取数据表的所有字段
  const dataList = await getColumnsDataList({
    id: form.value.dataSourceId,
    tableName: tableDataItem.tableName,
    // type: "MYSQL",
  });
  if (tableDataItem) {
    intermediateTable.fields = dataList || [];
    intermediateTable.tableComment = tableDataItem.tableComment;
    intermediateTable.tableField = "";
    intermediateTable.relationField = "";
  }
}

// 添加中间表
function addIntermediateTable(index) {
  const currentItem = relationForm.value.dataList[index];
  currentItem.relationMappingMiddle.push({
    tableName: "",
    tableField: "",
    relationField: "",
    fields: [],
    tableComment: "",
  });
}

// 删除中间表
function removeIntermediateTable(index, iIndex) {
  const currentItem = relationForm.value.dataList[index];
  currentItem.relationMappingMiddle.splice(iIndex, 1);
}

// 处理关系类型变化
function handleRelationTypeChange(index, type) {
  const currentItem = relationForm.value.dataList[index];

  // 如果是多对多关系，确保至少有一个中间表
  if (type === 2 && currentItem.relationMappingMiddle.length === 0) {
    addIntermediateTable(index);
  }

  // 如果不是多对多关系，清空中间表
  if (type !== 2) {
    currentItem.relationMappingMiddle = [];
  }
}

// 删除关系项
function deleteItem(index, data) {
  relationForm.value.dataList.splice(index, 1);
}

// 提交表单前的数据处理
function formatRelationData() {
  const formattedList = [];

  relationForm.value.dataList.forEach((item) => {
    const formattedItem = {
      type: item.type,
      tableName: item.source.tableName,
      fieldName: item.source.fieldName,
      relationTable: item.target.tableName,
      relationField: item.target.fieldName,
      relationNameField: item.target.fieldEntityName,
      relation: item.relation,
    };

    // 如果是多对多关系，添加中间表信息
    if (item.type === 2 && item.relationMappingMiddle.length > 0) {
      formattedItem.relationMappingMiddle = item.relationMappingMiddle.map(
        (intermediate) => ({
          tableName: intermediate.tableName,
          tableField: intermediate.tableField,
          relationField: intermediate.relationField,
        })
      );
    }

    formattedList.push(formattedItem);
  });

  return formattedList;
}

/** 上一步 */
function handleLastStep() {
  if (activeReult.value == 2) {
    proxy.$modal
      .confirm("返回上一步会重置当前页面数据，是否确认返回？")
      .then(function () {
        activeReult.value--;
      })
      .catch(() => {});
  } else {
    activeReult.value--;
  }
}
/** 下一步 */
async function handleNextStep() {
  try {
    await extStructRef.value?.validate();
  } catch (err) {
    console.warn("表单校验未通过：", err);
    ElMessage.warning("校验未通过，请检查必填项");
    loadingInstance.value = false;
    return;
  }
  if (connectionError.value) {
    ElMessage.warning("数据库测试连接失败，请检查配置信息");
    loadingInstance.value = false;
    return;
  }
  //进入关系映射页面执行
  if (activeReult.value == 1) {
    if (tableData.value.length == 0) {
      ElMessage.warning("请导入数据表");
      loadingInstance.value = false;
      return;
    }
    // 创建一个数组来存储所有 primaryKey 为 null 的表名
    const nullPrimaryKeys = [];
    // 遍历 tableData，检查每个表的 primaryKey
    tableData.value.forEach((tableItem) => {
      if (!tableItem.primaryKey) {
        nullPrimaryKeys.push(tableItem.tableName);
      }
    });
    // 将数组转换为逗号分隔的字符串
    const resultString = nullPrimaryKeys.join(", ");
    // 如果需要显示结果，例如通过 ElMessage
    if (resultString) {
      ElMessage.warning(`以下数据表未映射: ${resultString}`);
      loadingInstance.value = false;
      return;
    }

    //给关系映射页面赋值
    getRealtionMappingList();

    console.log("关系映射页面数据---", relationForm.value.dataList);
  }

  activeReult.value++;
}
/** 返回列表 */
const handleSuccess = () => {
  // 然后跳转
  setTimeout(() => {
    router.push("/da/quality/qualityTask");
    const obj = { path: "/ext/extStructTask" };
    proxy.$tab.closeOpenPage(obj);
  }, 800);
};
// 提交表单方法
async function submitForm() {
  try {
    // 验证基本表单
    await extStructRef.value?.validate();

    // 验证关系表单
    if (relationForm.value.dataList.length > 0) {
      const relationFormRef = proxy.$refs.relationFormRef;
      if (relationFormRef) {
        await relationFormRef.validate();
      }

      // 额外验证
      const isValid = await validateRelationForm();
      if (!isValid) {
        return;
      }
    }

    //关系映射数据
    const relationData = formatRelationData();
    console.log("关系映射数据：", relationData);

    //基础信息数据
    console.log("基础信息数据：", form.value);

    //表映射数据
    console.log("表映射数据：", tableData.value);
    //执行数据保存逻辑
    //提交之前测试数据库连通性
    clientsTest(form.value.dataSourceId).then((res) => {
      if (res && res.code == 200) {
        if (form.value.name.length >= 256) {
          return;
        } else if (form.value.remark && form.value.remark.length >= 512) {
          return;
        }

        //定义保存数据格式
        const dataList = {
          taskId: form.value.id,
          taskName: form.value.name,
          remark: form.value.remark,
          dataSourceId: form.value.dataSourceId,
          updateType: form.value.updateType,
          updateFrequency: form.value.updateFrequency,
          //关系映射数据
          relationData: relationData,
        };

        console.log("最终保存数据", dataList);

        if (form.value.id) {
          let data = tableData.value.filter((e) => e.status == 1);
          //表映射数据
          dataList.tableData = data;
          if (relationData.length > 0) {
            dataList.tableData.forEach((item) => {
              item.mappingData.relationshipList = relationData.filter(
                (e) => item.tableName == e.tableName
              );
            });
          }
          dataList.taskId = form.value.id;
          updateExtStructDataMapping(dataList).then((res) => {
            if (res && res.code == 200) {
              // 显示消息
              ElMessage.success("编辑成功");
              handleSuccess();
            }
          });
        } else {
          //表映射数据
          dataList.tableData = tableData.value;
          if (relationData.length > 0) {
            dataList.tableData.forEach((item) => {
              item.mappingData.relationshipList = relationData.filter(
                (e) => item.tableName == e.tableName
              );
            });
          }
          console.log("新增保存数据", dataList);
          addExtStructDataMapping(dataList).then((res) => {
            if (res && res.code == 200) {
              // 显示消息
              ElMessage.success("新增成功");
              handleSuccess();
            }
          });
        }
      }
    });
  } catch (err) {
    console.warn("表单校验未通过：", err);
    ElMessage.warning("请检查必填项");
    loadingInstance.value = false;
  }
}

// 验证关系表单
async function validateRelationForm() {
  try {
    // 原来的验证逻辑
    for (let i = 0; i < relationForm.value.dataList.length; i++) {
      const item = relationForm.value.dataList[i];

      // 验证源表和目标表不能相同
      if (item.source.tableName === item.target.tableName) {
        ElMessage.error(`第 ${i + 1} 项：源表和目标表不能相同`);
        return false;
      }

      // 多对多关系验证
      if (item.type === 2) {
        if (item.relationMappingMiddle.length === 0) {
          ElMessage.error(`第 ${i + 1} 项：多对多关系至少需要一个中间表`);
          return false;
        }
      }
    }

    return true;
  } catch (error) {
    ElMessage.error(error.message);
    return false;
  }
}

// 工具函数
function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
</script>

<style lang="scss" scoped>
.el-card ::v-deep .el-card__body {
  overflow-y: auto;
}

.pagecont-top {
  height: auto;
  min-height: 74vh;
  position: relative;
  padding-bottom: 60px;
}

.button-style {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 0px 35px 25px 0px;
  background: #fff;
  text-align: right;
  z-index: 10;
}

.main {
  flex: 1;
  background-color: white;
  padding: 0px 25px 0;
}

.custom-card {
  width: 100%;
  height: 100px;
  padding: 34px 177px 26px 189px;
  background: #fff;
  box-sizing: border-box;
  margin-bottom: 15px;

  .steps-inner {
    padding: 0 10px;
    padding-left: 20px;
    display: flex;
    width: auto;
    color: #303133;
    transition: 0.3s;
    transform: translateZ(0);

    &::-webkit-scrollbar {
      height: 5px;
    }

    .zl-step {
      list-style: none;
      width: 100%;
      height: 20px;
      padding: 0;
      margin: 20px auto;
      cursor: pointer;
      display: flex;
      align-items: flex-end;

      li {
        position: relative;
        flex: 1;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #d7d8da;
        color: #666;
        font-weight: 500;
        transition: background 0.3s;

        &:first-child {
          z-index: 2;
          clip-path: polygon(
            0 0,
            calc(100% - 20px) 0,
            100% 50%,
            calc(100% - 20px) 100%,
            0 100%
          );
        }

        &:not(:first-child):not(:last-child) {
          margin-left: -10px;
          clip-path: polygon(
            0 0,
            calc(100% - 20px) 0,
            100% 50%,
            calc(100% - 20px) 100%,
            0 100%
          );
          z-index: 1;

          &::before {
            content: "";
            position: absolute;
            left: 0;
            top: 0;
            width: 20px;
            height: 100%;
            background: #fff;
            clip-path: polygon(0 0, 100% 50%, 0 100%);
            z-index: 2;
          }
        }

        &:last-child {
          margin-left: -10px;
          z-index: 0;
          clip-path: polygon(0 0, 100% 0, 100% 100%, 0 100%);

          &::before {
            content: "";
            position: absolute;
            left: 0;
            top: 0;
            width: 20px;
            height: 100%;
            background: #fff;
            clip-path: polygon(0 0, 100% 50%, 0 100%);
            z-index: 2;
          }
        }

        &.statusEnd {
          background: linear-gradient(270deg, #e9effe 0%, #5589fa 100%);
          color: #2666fb !important;
        }

        &.prevStep {
          background: #e9effe !important;
          font-weight: normal;
          font-size: 16px !important;
          color: #2666fb !important;
        }

        &.cur {
          background: #f1f1f5;
          color: #404040;
          font-weight: 500;
        }
      }
    }

    .step-circle {
      width: 26px;
      height: 26px;
      border-radius: 50%;
      background: #f1f1f5;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      font-weight: bold;
      margin-right: 11px;
      border: 1px solid #b2b2b2;
      flex-shrink: 0;
      transition: all 0.3s;

      &.active {
        background: #2666fb;
        color: #fff;
        border: 1px solid #fff;
      }

      &.prev {
        background: #f1f1f5 !important;
        border: 1px solid #2666fb !important;
        color: #2666fb !important;
      }
    }

    .step-name {
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 16px;
    }
  }
}

:deep(.custom-import .el-dialog__body) {
  height: 630px !important;
}

:deep(.custom-mapping .el-dialog__body) {
  height: 700px !important;
}
</style>