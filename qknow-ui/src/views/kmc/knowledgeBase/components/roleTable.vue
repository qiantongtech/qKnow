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
  <div class="app-container" ref="app-container">
    <div class="pagecont-bottom">
      <div class="header-left">
        <div class="blue-bar"></div>
        权限设置
      </div>
      <!-- 表格数据 -->
      <el-table stripe   v-loading="loading" :data="roleList">
        <!--        <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column label="角色编码" prop="roleId" align="center" />
        <el-table-column
          label="角色名称"
          prop="roleName"
          align="center"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="权限字符"
          prop="roleKey"
          align="center"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="状态" align="center">
          <template #default="scope">
            <el-switch
              :model-value="
                selectedRoleIds.includes(scope.row.roleId) ? '0' : '1'
              "
              @update:model-value="handleSwitchUpdate(scope.row.roleId, $event)"
              active-value="0"
              inactive-value="1"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="显示顺序" prop="roleSort" align="center" />
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="160"
        >
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup name="RoleTable">
import {
  addKnowledgeBase,
  getRerank,
  getTextEmbedding,
  updateKnowledgeBase,
  delKnowledgeBase,
  getKnowledgeBase,
  getRole,
  updateKnowledgeBaseRole,
  changeKnowledgeValid,
} from "@/api/kmc/knowledgeBase/knowledgeBase.js";
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const roleList = ref([]);
const loading = ref(true);
const total = ref(0);
const dateRange = ref([]);
const kmcId = route.params.kbId;
const selectedRoleIds = ref([]);
const isInitializing = ref(true); // 新增：初始化标志位

const handleSwitchUpdate = (roleId, value) => {
  if (isInitializing.value) return;

  console.log("switch 更新:", roleId, value);

  if (value === "0") {
    // 开启
    if (!selectedRoleIds.value.includes(roleId)) {
      selectedRoleIds.value.push(roleId);
    }
  } else {
    // 关闭
    selectedRoleIds.value = selectedRoleIds.value.filter((id) => id !== roleId);
  }
  handleStatusChange();
};

/** 查询图谱角色列表 */
function getList() {
  loading.value = true;
  getRole(kmcId).then((response) => {
    loading.value = false;
    roleList.value = response.data.roleList;
    // 开始初始化
    isInitializing.value = true;
    // 直接初始化 selectedRoleIds
    selectedRoleIds.value = response.data.knowledgeRoleList.map(
      (item) => item.roleId
    );
    nextTick(() => {
      isInitializing.value = false;
    });
  });
}

/** 角色状态修改 */
function handleStatusChange() {
  updateKnowledgeBaseRole({
    knowledgeId: kmcId,
    roleIds: selectedRoleIds.value.join(","),
  });
}
getList();
</script>
<style scoped>
.app-container {
  .pagecont-bottom {
    .header-left {
      display: flex;
      align-items: center;
      margin-bottom: 15px;
      .blue-bar {
        background-color: #2666fb;
        width: 6px;
        height: 16px;
        margin-right: 10px;
        border-radius: 3px;
      }
    }
    height: 100%;
  }
}
</style>
