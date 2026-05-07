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
  <div class="app-container">
    <div class="pagecont-top">
      <div class="warn-container">
        <!-- 提示文字区域：单独div控制换行 -->
        <div class="warn-tip">
          <div class="circle">
            <img class="circle-img" src="@/assets/kg/danger.svg" alt="" />
          </div>
          此操作无法恢复，删除知识库将会连同相关的所有数据一起删除，请慎重选择！
        </div>
        <!-- 按钮区域：单独放置，避免和文字重叠 -->
        <div class="btn-group">
                  <el-button
                    type="danger"
                    @click.stop="handleDelete()"
                  >删除知识库
                  </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="RoleDel">
const { proxy } = getCurrentInstance();
const route = useRoute();
const graphId = route.params.kbId;
const kmcName = ref('');
import { delKnowledgeBase,getKnowledgeBase} from "@/api/kmc/knowledgeBase/knowledgeBase.js";

// 返回
function back() {
  const obj = { path: '/kmc/knowledgeBase' };
  proxy.$tab.closeOpenPage(obj);
}

function getKmcName(){
  getKnowledgeBase(graphId).then((res)=>{
    kmcName.value = res.data.name
  })
}
/** 删除按钮操作 */
function handleDelete() {
  const _ids = graphId;
  proxy.$modal
      // .confirm('是否确认删除知识图谱编号为"' + _ids + '"的数据项？')
      .confirm('是否确认删除名称为"' + kmcName.value + '"的知识库？')
      .then(function () {
        return delKnowledgeBase(_ids);
      })
      .then(() => {
        proxy.$modal.msgSuccess("删除成功");
        back();
      })
      .catch(() => {});
}

getKmcName();
</script>


<style scoped lang="scss">
.app-container {
  display: flex;
  .pagecont-top {
    padding: 15px;
    flex: 1;
    .warn-container {
      width: calc(100% - 64px - 64px);;
      margin-left: 64px;
      margin-top: 33px;
    }

    /* 提示文字div：核心是设置自动换行 */
    .warn-tip {
      display: flex;
      align-items: center;
      border: 1px solid #FFA39E;
      background-color: #FFF1F0  ;
      color: #FE4F4F;
      padding: 15px 38px 12px 19px; /* 改用上下内边距，替代固定height，适配换行 */
      font-size: 14px;
      border-radius: 2px;
      width: 100%;
      height: 48px;
      /* 关键：自动换行 + 防止文字溢出 */
      word-wrap: break-word; /* 强制换行（英文/长数字也会换行） */
      white-space: normal;   /* 允许换行（默认是nowrap的话会不换行） */
      .circle{
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 100px;
        background-color: #FE4F4F ;
        width: 17.96px;
        height: 17.96px;
        padding: 4px;
        margin-right: 8px;
        .circle-img{
          width: 12.62px;
          height: 13.68px;
        }
      }
    }

    /* 按钮区域：和文字区域分开，避免重叠 */
    .btn-group {
      margin-top: 10px; /* 和文字区域保持间距 */
    }
  }
}

:deep(.btn-group .el-button){
  background-color: #FE4F4F;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
  color: #FFFFFF ;
}

</style>
