<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          <span style="margin-left: 8px">
          {{ modelDetail.name }}
          </span>
          <el-row :gutter="15" class="btn-style" style="margin-left: auto">
            <el-col :span="1.5">
              <el-button
                  type="primary"
                  size="small"
                  class="fhbtn"
                  plain
                  @click="handleReturn"
                  @mousedown="(e) => e.preventDefault()"
              >
                <svg-icon style="width: 1em;height: 1em; margin-right: 3px;" :iconClass="'fhs'" /> 返回
              </el-button>
            </el-col>
          </el-row>
        </div>
        <el-row :gutter="3" style="margin-bottom: 3px;">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">模型名称</div>
              <div class="infotop-row-value">
                {{ modelDetail.name || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">支持语言</div>
              <div class="infotop-row-value">
                {{ "中/英文" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">模型提供方</div>
              <div class="infotop-row-value">
                <dict-tag :options="ai_model_platform" :value="modelDetail.platform"/>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" >
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ modelDetail.description || '-' }}
              </div>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-change="tabChange">
        <el-tab-pane v-for="item in platformKeyList" :key=item.id :label="item.name" :name=item.id></el-tab-pane>
      </el-tabs>
      <ModelTable :keyId=keyId />
    </div>

  </div>
</template>

<script setup name="ModelMarketDetail">
import {listByPlatform, getByPlatform} from "@/api/ai/modelMarket/key.js";
import {useRoute} from 'vue-router';
import ModelTable from './modelTable.vue'

const {proxy} = getCurrentInstance();
const {
  ai_apikey_status,
  ai_model_platform,
  ai_deploy_type
} = proxy.useDict('ai_apikey_status', 'ai_model_platform', 'ai_deploy_type');

const activeName = ref('1')
const platformKeyList = ref([]);

let keyId = 0;
const showSearch = ref(true);
const route = useRoute();
let platform = route.query.platform;
// 监听 id 变化
watch(
    () => route.query.platform,
    (newPlatform) => {
      platform = newPlatform;  // 如果 id 为空，使用默认值 1
      queryByPlatform(newPlatform);
      queryInfoByPlatform(newPlatform)
    },
    {immediate: true}
);
const data = reactive({
  modelDetail: {},
  form: {},
});

const {modelDetail, rules} = toRefs(data);

function queryByPlatform(platform) {
  if (!platform){
    return
  }
  listByPlatform(platform).then((response) => {
    platformKeyList.value = response.data;
    keyId = response.data[0].id
    activeName.value = response.data[0].id
  });
}

function queryInfoByPlatform(platform) {
  if (!platform){
    return
  }
  getByPlatform(platform).then((response) => {
    modelDetail.value = response.data;
  });
}

function tabChange() {
  keyId = activeName.value
}

function handleReturn() {
  const path = route.path;
  if (path.indexOf('/system/ai/myModel/detail') > -1) {
    proxy.$router.push({
      path: '/system/ai/myModel',
    });
  }else {
    proxy.$router.push({
      path: '/system/ai/modelMarket',
    });
  }

}

queryByPlatform(platform);
queryInfoByPlatform(platform)

</script>
<style lang="scss" scoped>
.fhbtn {
  .svg-icon {
    font-size: 12px;
    margin-right: 3px;
    vertical-align: middle;
    margin-top: -3px;
  }
  &:hover {
    .svg-icon {
      filter: brightness(0) invert(1) !important;
    }
  }
}
</style>
