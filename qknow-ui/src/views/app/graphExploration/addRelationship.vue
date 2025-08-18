<template>
  <el-dialog v-model="open" width="800px" :append-to="$refs['app-container']" draggable @close="handleClose" destroy-on-close>
    <template #header>
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="handleRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="实体关系" prop="tableData">
            <template #label> 实体关系 </template>
            <el-button type="primary" style="margin-bottom: 15px" @click="addItem" plain>新增实体关系</el-button>
            <el-table :data="form.tableData" style="width: 100%">
              <el-table-column label="实体名称1" min-width="180">
                <template #default="scope">
                  <el-input v-model="scope.row.entityName1" readonly @click="openEntity(scope.row, 'pre')" type="text" placeholder="请选择实体名称" />
                </template>
              </el-table-column>
              <el-table-column label="关系名称" min-width="180">
                <template #default="scope">
                  <el-input v-model="scope.row.relationshipType" type="text" placeholder="请填写名称" />
                </template>
              </el-table-column>
              <el-table-column label="实体名称2" min-width="180">
                <template #default="scope">
                  <el-input v-model="scope.row.entityName2" readonly @click="openEntity(scope.row, 'post')" type="text" placeholder="请选择实体名称" />
                </template>
              </el-table-column>
              <!-- 操作列 -->
              <el-table-column label="操作" min-width="70">
                <template #default="scope">
                  <el-button size="mini" type="danger" @click="deleteItem(scope.$index, scope.row)" plain>删除 </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" size="mini" @click="submitForm">提交</el-button>
        <el-button size="mini" @click="handleClose">取 消</el-button>
      </div>
    </template>
    <EntitySingle ref="entitySingle" :graphId="graphId" @confirm="confirm"></EntitySingle>
  </el-dialog>
</template>

<script setup name="addEntity">
import EntitySingle from "./selection/entitySingle.vue";
import { addTripletRel, deleteRelationshipsByIds } from "@/api/app/graph";

const { proxy } = getCurrentInstance();

// eslint-disable-next-line vue/valid-define-emits
const emit = defineEmits();

const props = defineProps({
  title: {
    type: String,
    default: "实体",
  },
  graphId: {
    default: {},
  },
});
const open = ref(false);
const title = computed(() => props.title);
const graphId = computed(() => props.graphId);
const itemData = () => {
  return { entityId1: null, entityName1: null, relationshipType: null, entityId2: null, entityName2: null };
};
const openDialog = (row) => {
  let data = row.map((item) => {
    return {
      id: item.id.replace(/edge/g, ""),
      entityId1: item.startId,
      entityName1: item.startName,
      relationshipType: item.relationType,
      entityId2: item.endId,
      entityName2: item.endName,
    };
  });
  reset();
  open.value = true;
  if (data.length > 0) {
    form.value = {
      tableData: [...data],
    };
  } else {
    form.value = {
      tableData: [itemData()],
    };
  }
};
// 表单重置
function reset() {
  ids.value = [];
  form.value = {
    tableData: [],
  };
  proxy.resetForm("handleRef");
}

function handleClose() {
  reset();
  open.value = false;
}

const data = reactive({
  form: {
    tableData: [],
  },
  rules: {
    tableData: [{ required: true, message: "实体不能为空", trigger: "blur" }],
  },
});

const { form, rules } = toRefs(data);

/** 提交按钮 */
function submitForm() {
  proxy.$refs["handleRef"].validate(async (valid) => {
    if (valid) {
      if (form.value.tableData[0].entityId1 == null || form.value.tableData[0].entityId1 == undefined) {
        form.value.tableData = [];
      }
      if (ids.value.length > 0) {
        await deleteRelationshipsByIds(ids.value);
      }
      const addRes = await addTripletRel(form.value.tableData);
      if (addRes.code == 200) {
        proxy.$modal.msgSuccess("提交成功");
        emit("handleRefresh");
        handleClose();
      }
    }
  });
}

const ids = ref([]);
// 删除操作
const deleteItem = (index, row) => {
  if (row.id) {
    // 使用 splice 方法根据索引删除数据
    proxy.$modal
      .confirm(`是否确认删除当前三元组？`)
      .then(() => {
        form.value.tableData.splice(index, 1);
        row.id && ids.value.push(row.id);
        // 最后一条清空不删掉
        form.value.tableData.length == 0 && form.value.tableData.push(itemData());
      })
      .catch(() => {});
  } else {
    // 使用 splice 方法根据索引删除数据
    form.value.tableData.splice(index, 1);
    // 最后一条清空不删掉
    form.value.tableData.length == 0 && form.value.tableData.push(itemData());
  }
};

const addItem = () => {
  form.value.tableData.push(itemData());
};

let selectData = null;
let selectType = null;
const openEntity = (data, type) => {
  proxy.$refs["entitySingle"].open();
  selectData = data;
  selectType = type;
};

const confirm = (data) => {
  if (selectType === "pre") {
    selectData.entityId1 = data.id;
    selectData.entityName1 = data.name;
  } else {
    selectData.entityId2 = data.id;
    selectData.entityName2 = data.name;
  }
};
defineExpose({ openDialog });
</script>
