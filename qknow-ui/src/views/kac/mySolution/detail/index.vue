<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qKnow Knowledge Platform (Business Edition)
  Software Copyright Registration No. 15980140
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <div class="app-container" ref="app-container" :key="pageKey">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          <el-tag size="medium">
            {{ solutionDetail.id }}
          </el-tag>
          <span style="margin-left: 8px">
            {{ solutionDetail.name || "-" }}
          </span>
          <!--          <el-row :gutter="15" class="btn-style" style="margin-left: auto">-->
          <!--            <el-col :span="1.5">-->
          <!--              <el-button-->
          <!--                  type="primary"-->
          <!--                  plain-->
          <!--                  @click="handlePublish(solutionDetail)"-->
          <!--                  @mousedown="(e) => e.preventDefault()"-->
          <!--                  class="custom-btn-padding"-->
          <!--              >-->
          <!--                {{ solutionDetail.status === 1 ? '取消发布' : '发布' }}-->
          <!--              </el-button>-->
          <!--            </el-col>-->
          <!--          </el-row>-->
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">标签</div>
              <div class="infotop-row-value">
                <div class="multiline-ellipsis">
                  <el-tag
                    v-for="(tag, tagIndex) in getTags(solutionDetail)"
                    :key="tagIndex"
                    class="card-tag"
                  >
                    {{ tag.name }}
                  </el-tag>
                  <span v-if="getTags(solutionDetail).length <= 0">-</span>
                </div>
              </div>
            </div>
          </el-col>
          <!--          <el-col :span="8">-->
          <!--            <div class="infotop-row border-top">-->
          <!--              <div class="infotop-row-lable">发布状态</div>-->
          <!--              <div class="infotop-row-value">-->
          <!--                <dict-tag-->
          <!--                    :options="publish_status"-->
          <!--                    :value="solutionDetail.status"-->
          <!--                />-->
          <!--              </div>-->
          <!--            </div>-->
          <!--          </el-col>-->
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建人</div>
              <div class="infotop-row-value">
                {{ solutionDetail.createBy || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">
                {{
                  parseTime(solutionDetail.createTime, "{y}-{m}-{d} {h}:{i}")
                }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">最后更新时间</div>
              <div class="infotop-row-value">
                {{
                  parseTime(solutionDetail.updateTime, "{y}-{m}-{d} {h}:{i}")
                }}
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                <el-tooltip
                  :content="solutionDetail.description"
                  placement="top"
                >
                  <span class="text-ellipsis">
                    {{ solutionDetail.description || "-" }}
                  </span>
                </el-tooltip>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">备注</div>
              <div class="infotop-row-value">
                {{ solutionDetail.remark || "-" }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="关联应用" name="1">
          <MyAppList
            v-if="activeName === '1'"
            :solutionId="solutionDetail.id"
            :key="activeName"
            :myAppList="myAppList"
          ></MyAppList>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="SolutionDetail">
import { useRoute } from "vue-router";
import { ref, reactive, toRefs, watch } from "vue";
import MyAppList from "./myAppList.vue";
import { getSolution } from "@/api/kac/solution/solution";
import { listApply } from "@/api/kac/apply/apply.js";
import { updateSolution } from "@/api/kac/solution/solution.js";

const activeName = ref("1");
const pageKey = ref(0); // 用于强制刷新页面

const handleClick = (tab, event) => {
  console.log(tab, event);
};

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;

const data = reactive({
  solutionDetail: {},
});

const { proxy } = getCurrentInstance();
const { publish_status } = proxy.useDict("publish_status");
const { solutionDetail } = toRefs(data);

// 应用列表
const myAppList = ref([]);

watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;
    getSolutionDetailById();
  },
  { immediate: true }
);

function getTags(row) {
  if (!row.tags) {
    return [];
  }
  return JSON.parse(row.tags);
}

/** 获取解决方案详情 */
function getSolutionDetailById() {
  const _id = id;
  getSolution(_id).then((response) => {
    solutionDetail.value = response.data;
  });
}

// 加载下拉框数据
function loadDropdownData() {
  // 获取应用列表
  listApply({ pageSize: 1000, pageNum: 1 }).then((response) => {
    myAppList.value = response.data.rows;
  });
}

/** 发布/取消发布操作 */
const handlePublish = (row) => {
  const targetStatus = 1 - row.status;
  const actionText = targetStatus === 1 ? "发布" : "取消发布";
  const confirmMsg = `是否确认${actionText}编号为 "${row.id}" 的数据项？`;
  proxy.$modal
    .confirm(confirmMsg)
    .then(function () {
      row.status = targetStatus;
      return updateSolution(row);
    })
    .then(() => {
      proxy.$modal.msgSuccess(`${actionText}成功`);
      // 强制刷新整个页面（类似重新进入页面）
      pageKey.value++;
    })
    .catch(() => {});
};

// 加载所有下拉框数据
loadDropdownData();
</script>
<style scoped lang="scss">
.card-tag {
  margin: 2px;
}
.multiline-ellipsis {
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 限制为2行 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
.text-ellipsis {
  display: inline-block;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>

