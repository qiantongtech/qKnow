<template>
  <!-- 结构化抽取任务 数据映射 -->
  <el-dialog
    :title="title"
    v-model="visible"
    width="1300px"
    top="5vh"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
    @close="handleClose"
    class="custom-dialog"
  >
    <div class="pagecont-top">
      <el-form
        ref="mappingRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="概念" prop="concept">
              <el-select
                @change="conceptChange"
                v-model="form.concept"
                placeholder="请选择概念"
              >
                <el-option
                  v-for="item in conceptList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="实体名称字段" prop="entityNameField">
              <el-select
                v-model="form.entityNameField"
                placeholder="请选择实体名称字段"
              >
                <el-option
                  v-for="item in attributeMappingList"
                  :key="item.field"
                  :label="item.field"
                  :value="item.field"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主键" prop="primaryKey">
              <el-select v-model="form.primaryKey" placeholder="请选择主键">
                <el-option
                  v-for="item in attributeMappingList"
                  :key="item.field"
                  :label="item.field"
                  :value="item.field"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="timeFieldShow">
          <el-col :span="12">
            <el-form-item label="增量依据字段" prop="entityTimeField">
              <el-select
                v-model="form.entityTimeField"
                placeholder="请选择增量依据字段"
              >
                <el-option
                  v-for="item in timeMappingList"
                  :key="item.field"
                  :label="item.field"
                  :value="item.field"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="属性映射" prop="attributeMapping">
              <el-table :data="attributeMappingList" style="height: 330px">
                <el-table-column
                  prop="field"
                  label="数据表字段"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  prop="fieldDescription"
                  label="字段描述"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column prop="conceptId" label="概念属性">
                  <template #default="scope">
                    <el-select
                      @change="rowConceptClick(scope.row)"
                      v-model="scope.row.conceptId"
                      placeholder="请选择概念属性"
                      clearable
                    >
                      <el-option
                        v-for="item in conceptualAttributeList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
  
  <script setup>
// import {getTableData} from "@/api/ext/extDatasource/datasource";
import { getColumnsList } from "@/api/da/datasource/daDatasource";
import { getExtSchemaAllList } from "@/api/ext/extSchema/schema";
import { listAttribute } from "@/api/ext/extSchemaAttribute/attribute";
import { listRelation } from "@/api/ext/extSchemaRelation/relation";

const title = ref("数据映射");
const total = ref(0);
const visible = ref(false);
const tables = ref([]);
const dbTableList = ref([]);
const conceptList = ref([]);
const attributeMappingList = ref([]);
const timeMappingList = ref([]);
const conceptualAttributeList = ref([]);
const associationTables = ref([]);
const relationshipMappingList = ref([]);
const customMappingList = ref([]);
const queryParams = ref(null);
const { proxy } = getCurrentInstance();
const loading = ref(false);
const route = useRoute();

const timeFieldShow = ref(false);

const form = reactive({
  dataSourceId: null,
  concept: null,
  conceptName: null,
  tableName: null,
  entityNameField: null,
  primaryKey: null,
  entityTimeField: null,
});

const rules = reactive({
  concept: [{ required: true, message: "概念不能为空", trigger: "blur" }],
  entityNameField: [
    { required: true, message: "实体名称不能为空", trigger: "blur" },
  ],
  primaryKey: [{ required: true, message: "主键不能为空", trigger: "blur" }],
  entityTimeField: [
    { required: true, message: "增量依据字段不能为空", trigger: "blur" },
  ],
});

const emit = defineEmits(["ok", "close-dialog"]);

const handleClose = () => {
  emit("close-dialog");
};

/** 查询参数列表 */
async function show(val) {
  console.log("---------val------", val);
  timeFieldShow.value = val.timeFieldShow; // 时间字段是否显示
  queryParams.value = val;
  form.dataSourceId = val.dataSourceId;
  form.tableName = val.tableName;
  form.concept = null;
  form.conceptName = null;
  form.entityNameField = null;
  form.primaryKey = null;
  associationTables.value = val.tables;
  attributeMappingList.value = [];
  timeMappingList.value = [];
  relationshipMappingList.value = [];
  customMappingList.value = [];
  conceptList.value = [];
  conceptualAttributeList.value = [];
  //获取概念列表
  await getSchemaList();
  await getTableDetail(); //TODO

  title.value = val.title;
  visible.value = true;

  //如果添加过映射
  if (val.status == 1) {
    form.concept = val.mappingData.concept;
    form.conceptName = val.mappingData.conceptName;
    form.entityNameField = val.mappingData.entityNameField;
    form.primaryKey = val.mappingData.primaryKey;
    //查询对应概念的属性
    await conceptChange();
    console.log("-------val.mappingData-----", val.mappingData);
    // console.log('-------attributeMappingList.value-----', attributeMappingList.value)

    //属性映射
    attributeMappingList.value.forEach((e) => {
      val.mappingData.attributeList.forEach((v) => {
        if (e.field == v.field) {
          e.conceptId = v.conceptId;
          e.conceptName = v.conceptName;
        }
      });
    });

    //时间
    timeMappingList.value.forEach((e) => {
      val.mappingData.attributeList.forEach((v) => {
        if (e.field == v.field) {
          e.conceptId = v.conceptId;
          e.conceptName = v.conceptName;
        }
      });
    });

    relationshipMappingList.value.forEach((e) => {
      val.mappingData.relationshipList.forEach((v) => {
        if (e.relation == v.relation && e.field == null) {
          e.field = v.field;
          e.associationTable = v.relationTable;
          e.associationTableField = v.relationField;
          e.associationTableEntityField = v.relationNameField;
          changeAssociationTable(e);
        }
      });
    });

    customMappingList.value = val.mappingData.customList;
  }

  // title.value = val.title
  // visible.value = true;
}

/**
 * 获取概念列表
 */
const getSchemaList = async () => {
  getExtSchemaAllList().then((res) => {
    if (res && res.code === 200) {
      conceptList.value = res.data;
    }
  });
};

/**
 * 获取数据库信息
 */
const getTableDetail = async () => {
  if (loading.value) {
    return;
  }
  loading.value = true;

  attributeMappingList.value = [];
  await getColumnsList({
    id: queryParams.value.dataSourceId,
    tableName: queryParams.value.tableName,
    // type: "MYSQL",
  }).then((res) => {
    if (res && res.code == 200) {
      res.data.forEach((e) => {
        attributeMappingList.value.push({
          field: e.engName,
          fieldDescription: e.cnName,
          conceptId: null,
          conceptName: null,
        });
        // 判断字段类型是否是日期类型
        if (
          e.columnType === "DATE" ||
          e.columnType === "DATETIME" ||
          e.columnType === "TIMESTAMP"
        ) {
          // 将日期类型字段加入 timeMappingList
          timeMappingList.value.push({
            field: e.engName,
            fieldDescription: e.cnName,
            conceptId: null,
            conceptName: null,
          });
        }
      });
    }
  });

  loading.value = false;
};

//获取关联表的字段
const changeAssociationTable = async (row) => {
  console.log("=======", row);
  if (loading.value) {
    return;
  }
  loading.value = true;

  await getColumnsList({
    id: queryParams.value.dataSourceId,
    tableName: row.associationTable,
    // type: "MYSQL",
  }).then((res) => {
    if (res && res.code == 200) {
      let temp = [];
      res.data.forEach((e) => {
        temp.push(e);
      });
      relationshipMappingList.value.forEach((e) => {
        if (e.id == row.id) {
          e.associationTableDetail = temp;
        }
      });
    }
  });

  loading.value = false;
};
// 添加一行数据
const addRow = () => {
  customMappingList.value.push({
    field: null,
    customSQL: null,
  });
};

// 删除某一行
const removeRow = (index) => {
  customMappingList.value.splice(index, 1);
};

//切换概念  根据概念id获取概念属性
const conceptChange = async () => {
  conceptList.value.find((e) => {
    if (form.concept == e.id) {
      form.conceptName = e.name;
    }
  });
  attributeMappingList.value.forEach((e) => {
    e.conceptId = null;
    e.conceptName = null;
  });
  //根据概念获取对应属性
  const res = await listAttribute({ schemaId: form.concept });
  if (res && res.code == 200) {
    conceptualAttributeList.value = res.data.list;
  }
  //根据概念获取对应关系
  const result = await listRelation({ startSchemaId: form.concept });
  if (result && result.code == 200) {
    let temp = [];
    result.data.list.forEach((e) => {
      temp.push({
        id: e.id,
        field: null,
        relation: e.relation,
        associationTable: null,
        associationTableField: null,
        associationTableEntityField: null,
        associationTableDetail: null,
      });
    });
    relationshipMappingList.value = temp;
  }
};

// 选择概念属性
const rowConceptClick = (row) => {
  if (row.conceptId != null) {
    attributeMappingList.value.forEach((e) => {
      if (e.field == row.field) {
        let temp = conceptualAttributeList.value.find(
          (c) => c.id == row.conceptId
        );
        // 确保找到了匹配项，并且 temp 不为 undefined
        if (temp) {
          e.conceptName = temp.name;
        } else {
          // 如果没有找到对应项，可以选择赋一个默认值
          e.conceptName = "未找到概念";
        }
      }
    });
  }
};

//确定
const handleAdd = () => {
  proxy.$refs["mappingRef"].validate((valid) => {
    if (valid) {
      //属性映射
      let attributeList = attributeMappingList.value;
      // //关系映射
      // let relationshipList = relationshipMappingList.value;
      //自定义映射
      let customList = customMappingList.value;
      let mappingData = {
        ...form,
        attributeList,
        // relationshipList,
        customList,
      };
      emit("ok", mappingData);
      visible.value = false;
    }
  });
};

defineExpose({
  show,
});
</script>
  <style scoped lang="scss">
/*:deep(.custom-dialog) {*/
/*    .el-dialog__body {*/
/*        height: 700px !important;*/
/*    }*/
/*}*/
</style>
  