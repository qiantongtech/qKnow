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
  源代码的"可访问性"均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
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
      <!-- 标题 -->
      <div class="panel-header">
        <div class="blue-bar"></div>
        删除设置
      </div>
      <!-- 警告提示 -->
      <div class="warn-container">
        <div class="warn-content">
          <div class="warn-icon">
            <img class="circle-img" src="@/assets/kg/danger.svg" alt="" />
          </div>
          <div class="warn-text">此操作无法恢复！</div>
        </div>

        <div class="warn-detail">
          <span>危险操作：</span
          >删除知识库将会连同相关的知识文件、切片、检索配置、召回测试记录等所有数据一起彻底删除。如有应用正在调用，将导致关联应用直接报错，请极其慎重地选择!
        </div>
      </div>
    </div>

    <div class="pagecont-content">
      <!-- 第一行：删除范围概览 + 删除前检查 -->
      <div class="row-wrapper">
        <!-- 左侧：删除范围概览 -->
        <div class="left-panel">
          <div class="panel-header">
            <div class="blue-bar"></div>
            删除范围概览
          </div>
          <div class="stats-row">
            <div class="stat-card-h" style="animation-delay: 0s">
              <div class="stat-icon-h doc-icon">
                <img
                  src="@/assets/system/images/index/icon (7).png"
                  class="module-item-t-r"
                />
              </div>
              <div class="stat-info-h">
                <div class="stat-label-h">知识文件</div>
                <div class="stat-value-h">
                  {{ animatedStats.documentCount.toLocaleString() }}
                </div>
                <div class="stat-desc-h">将一并删除</div>
              </div>
            </div>

            <div class="stat-card-h" style="animation-delay: 0.1s">
              <div class="stat-icon-h category-icon">
                <img
                  src="@/assets/system/images/index/icon (10).png"
                  class="module-item-t-r"
                />
              </div>
              <div class="stat-info-h">
                <div class="stat-label-h">知识分类</div>
                <div class="stat-value-h">
                  {{ animatedStats.categoryCount }}
                </div>
                <div class="stat-desc-h">将一并删除</div>
              </div>
            </div>

            <div class="stat-card-h" style="animation-delay: 0.2s">
              <div class="stat-icon-h test-icon">
                <img
                  src="@/assets/system/images/index/icon (3).png"
                  class="module-item-t-r"
                />
              </div>
              <div class="stat-info-h">
                <div class="stat-label-h">召回测试记录</div>
                <div class="stat-value-h">{{ animatedStats.testCount }}</div>
                <div class="stat-desc-h">将一并删除</div>
              </div>
            </div>

            <div class="stat-card-h" style="animation-delay: 0.3s">
              <div class="stat-icon-h index-icon">
                <img
                  src="@/assets/system/images/index/gzs (3).png"
                  class="module-item-t-r"
                />
              </div>
              <div class="stat-info-h">
                <div class="stat-label-h">知识文件分段</div>
                <div class="stat-value-h">
                  {{ animatedStats.indexCount.toLocaleString() }}
                </div>
                <div class="stat-desc-h">将一并删除</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：删除前检查 -->
        <div class="right-panel">
          <div class="panel-header">
            <div class="blue-bar"></div>
            删除前检查
          </div>
          <div class="precheck-list">
            <div
              v-for="(item, index) in precheckItems"
              :key="index"
              class="precheck-item"
            >
              <el-icon class="precheck-icon"><CircleCheckFilled /></el-icon>
              <span class="precheck-text">{{ item }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 第二行：关联数据详情 + 安全校验 -->
      <div class="row-wrapper">
        <!-- 左侧：关联数据详情 -->
        <div class="left-panel">
          <div class="panel-header">
            <div class="blue-bar"></div>
            关联数据详情
          </div>
          <div class="data-table-wrapper">
            <el-table stripe :data="relatedData">
              <el-table-column label="数据类型" min-width="160">
                <template #default="{ row }">
                  <div class="type-cell">
                    <span class="type-icon" :class="row.type">
                      <el-icon><component :is="row.icon" /></el-icon>
                    </span>
                    <span>{{ row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="数量" prop="count" width="140" />
              <el-table-column
                label="最近更新时间"
                prop="updatedAt"
                min-width="180"
                align="center"
              />
              <el-table-column label="删除影响" prop="impact" min-width="280" />
            </el-table>
            <pagination :total="relatedData.length" />
          </div>
        </div>

        <!-- 右侧：安全校验 -->
        <div class="right-panel">
          <div class="panel-header">
            <div class="blue-bar"></div>
            安全校验
          </div>
          <div class="security-check">
            <div class="check-item-sc">
              <div class="check-label-sc">
                <span class="required">*</span>
                请输入知识库名称以确认删除
              </div>
              <el-input
                v-model="confirmName"
                placeholder="请输入：基础医学知识库"
                class="check-input"
              />
            </div>

            <div class="check-item-sc">
              <div class="check-label-sc">
                <span class="required">*</span>
                删除原因
              </div>
              <el-select
                v-model="deleteReason"
                placeholder="请选择"
                class="check-input"
              >
                <el-option label="业务下线" value="business_offline" />
                <el-option label="数据迁移" value="data_migration" />
                <el-option label="重复创建" value="duplicate" />
                <el-option label="其他" value="other" />
              </el-select>
            </div>

            <div class="check-item-sc">
              <div class="check-label-sc">备注说明（选填）</div>
              <el-input
                v-model="remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注说明..."
                maxlength="200"
                show-word-limit
                class="check-input"
              />
            </div>

            <div class="confirm-checkbox">
              <el-checkbox v-model="confirmed">
                我已知晓该操作不可恢复，并确认继续删除
              </el-checkbox>
            </div>
          </div>
        </div>
      </div>

      <!-- 底部按钮 -->
      <div class="btn-group">
        <el-button class="btn-cancel" @click="back">取消</el-button>
        <!-- <el-button
          type="primary"
          class="btn-export"
          icon="Download"
          :disabled="!canDelete"
          @click="handleExport"
          >导出备份</el-button
        > -->
        <el-button
          type="danger"
          class="btn-delete"
          icon="Delete"
          @click.stop="handleDelete()"
        >
          删除知识库
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup name="RoleDel">
const { proxy } = getCurrentInstance();
const route = useRoute();
const graphId = route.params.kbId;
const kmcName = ref("");
import {
  delKnowledgeBase,
  getKnowledgeBase,
} from "@/api/kmc/knowledgeBase/knowledgeBase.js";
import {
  CircleCheckFilled,
  Document,
  Collection,
  Tickets,
  Coin,
  Lock,
} from "@element-plus/icons-vue";

// 统计数据
const stats = ref({
  documentCount: 12,
  categoryCount: 15,
  testCount: 128,
  indexCount: 158,
});

// 数字动画相关
const animatedStats = ref({
  documentCount: 0,
  categoryCount: 0,
  testCount: 0,
  indexCount: 0,
});

const isAnimating = ref(false);

// 安全校验表单
const confirmName = ref("");
const deleteReason = ref("");
const remark = ref("");
const confirmed = ref(false);

// 删除前检查项
const precheckItems = ref([
  "已完成数据备份",
  "已通知相关负责人",
  "已确认该知识库不再被业务依赖",
]);

// 关联数据详情
const relatedData = ref([
  {
    name: "知识文件",
    count: stats.value.documentCount.toLocaleString(),
    updatedAt: "2024-05-20 14:32:18",
    impact: "所有文件及元数据将被永久删除",
    type: "doc",
    icon: Document,
  },
  {
    name: "知识分类",
    count: stats.value.categoryCount.toLocaleString(),
    updatedAt: "2024-05-18 09:15:05",
    impact: "分类结构及关联关系将被删除",
    type: "category",
    icon: Collection,
  },
  {
    name: "知识文件分段",
    count: stats.value.indexCount.toLocaleString(),
    updatedAt: "2024-05-20 14:35:42",
    impact: "知识文件分段数据将被删除，影响检索与召回",
    type: "index",
    icon: Coin,
  },
  {
    name: "召回测试记录",
    count: stats.value.testCount.toLocaleString(),
    updatedAt: "2024-05-19 17:22:33",
    impact: "测试记录及评估结果将被删除",
    type: "test",
    icon: Tickets,
  },
  {
    name: "权限配置",
    count: "18",
    updatedAt: "2024-05-18 11:08:47",
    impact: "与该知识库相关的权限配置将被删除",
    type: "permission",
    icon: Lock,
  },
]);

// 计算属性：是否可以删除
const canDelete = computed(() => {
  return (
    confirmName.value === kmcName.value && deleteReason.value && confirmed.value
  );
});

// 数字递增动画函数
function animateNumber(target, key, duration = 1500) {
  const start = 0;
  const startTime = Date.now();

  function update() {
    const elapsed = Date.now() - startTime;
    const progress = Math.min(elapsed / duration, 1);

    // 使用easeOutExpo缓动函数
    const easeProgress = 1 - Math.pow(2, -10 * progress);
    animatedStats.value[key] = Math.floor(
      start + (target - start) * easeProgress
    );

    if (progress < 1) {
      requestAnimationFrame(update);
    } else {
      animatedStats.value[key] = target;
    }
  }

  requestAnimationFrame(update);
}

// 启动所有数字动画
function startAnimation() {
  if (isAnimating.value) return;
  isAnimating.value = true;

  setTimeout(() => {
    animateNumber(stats.value.documentCount, "documentCount");
    animateNumber(stats.value.categoryCount, "categoryCount");
    animateNumber(stats.value.testCount, "testCount");
    animateNumber(stats.value.indexCount, "indexCount", 2000);
  }, 300);
}

// 返回
function back() {
  const obj = { path: "/kmc/knowledgeBase" };
  proxy.$tab.closeOpenPage(obj);
}

function getKmcName() {
  getKnowledgeBase(graphId).then((res) => {
    kmcName.value = res.data.name;
  });
}

/** 导出备份 */
function handleExport() {
  proxy.$modal.msgSuccess("备份导出成功");
}

/** 删除按钮操作 */
function handleDelete() {
  const _ids = graphId;
  proxy.$modal
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

// 组件挂载后启动动画
onMounted(() => {
  startAnimation();
});
</script>


<style scoped lang="scss">
.app-container {
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .pagecont-top {
    padding: 24px;
    width: 100%;
    margin: 0 auto;
  }

  // .pagecont-content {
  //   padding: 24px 24px 24px;
  // }

  .panel-header {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 16px;
    .blue-bar {
      background-color: #2666fb;
      width: 6px;
      height: 16px;
      margin-right: 10px;
      border-radius: 3px;
    }
  }

  /* 警告提示 */
  .warn-container {
    background: linear-gradient(135deg, #fff1f0 0%, #fff2f0 100%);
    border: 1px solid #ffa39e;
    border-radius: 12px;
    padding: 20px 28px;
    margin: 24px 0px;
    position: relative;
    overflow: hidden;

    &::before {
      content: "";
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 4px;
    }

    .warn-content {
      display: flex;
      align-items: center;
    }

    .warn-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 100px;
      background-color: #fe4f4f;
      width: 19px;
      height: 19px;
      padding: 4px;
      margin-right: 8px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .warn-text {
      display: inline-block;
      vertical-align: middle;
      font-size: 15px;
      color: #fe4f4f;
      font-weight: 600;
      letter-spacing: 0.3px;
    }

    .warn-detail {
      margin-top: 14px;
      font-size: 13px;
      color: #fe4f4f;
      line-height: 1.7;
      margin-left: 24px;
      span {
        font-weight: 700;
        color: #fe4f4f;
      }
    }
  }

  /* 每一行布局：左右面板 + 背景色间距 */
  .row-wrapper {
    display: flex;
    gap: 15px;
    margin: 15px 0px;

    .left-panel {
      flex: 1;
      padding: 13px 15px;
      background-color: #ffffff;
      border-radius: 2px;
    }

    .right-panel {
      width: 360px;
      padding: 13px 15px;
      background-color: #ffffff;
      border-radius: 2px;
    }
  }

  /* 横向统计卡片 */
  .stats-row {
    display: flex;
    gap: 16px;

    .stat-card-h {
      flex: 1;
      background: #ffffff;
      border-radius: 12px;
      padding: 20px 16px;
      display: flex;
      align-items: flex-start;
      gap: 12px;
      border: 1px solid #f0f0f0;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      position: relative;
      overflow: hidden;
      opacity: 0;
      transform: translateY(20px);
      animation: cardFadeIn 0.6s ease forwards;

      @keyframes cardFadeIn {
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      &::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 3px;
        background: linear-gradient(90deg, #2666fb 0%, transparent 100%);
        opacity: 0;
        transition: opacity 0.3s ease;
      }

      // &:hover {
      //   transform: translateY(-4px);
      //   border-color: #ffa39e;
      //   box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);

      //   &::before {
      //     opacity: 1;
      //   }
      // }

      .stat-icon-h {
        width: 44px;
        height: 44px;
        border-radius: 10px;
        .module-item-t-r {
          width: 100%;
          height: 100%;
        }
      }

      .stat-info-h {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 4px;
        min-width: 0;

        .stat-label-h {
          font-size: 12px;
          color: #909399;
          font-weight: 500;
        }

        .stat-value-h {
          font-size: 22px;
          font-weight: 700;
          color: #303133;
          line-height: 1.2;
          letter-spacing: -0.5px;
        }

        .stat-desc-h {
          font-size: 11px;
          color: #fa8c16;
          font-weight: 500;
          display: inline-flex;
          align-items: center;
          gap: 4px;

          &::before {
            content: "⚠";
            font-size: 10px;
          }
        }
      }
    }
  }

  /* 删除前检查 */
  .precheck-list {
    background: #fff;
    border-radius: 12px;
    padding: 0px 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .precheck-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .precheck-icon {
        color: #67c23a;
        font-size: 16px;
        flex-shrink: 0;
      }

      .precheck-text {
        font-size: 13px;
        color: #606266;
      }
    }
  }

  /* 关联数据详情表格 */
  .data-table-wrapper {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .data-table {
      :deep(.el-table__header-wrapper) {
        th {
          background-color: #f5f7fa;
          color: #606266;
          font-weight: 600;
          font-size: 13px;
          padding: 12px 0;
        }
      }

      :deep(.el-table__row) {
        td {
          padding: 14px 0;
          font-size: 13px;
        }
      }
    }

    .type-icon {
      width: 24px;
      height: 24px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      font-size: 14px;

      &.doc,
      &.index,
      &.permission {
        color: #2666fb;
        background: #eaf1ff;
      }

      &.category {
        color: #10b981;
        background: #e8f8f1;
      }

      &.test {
        color: #7c5cff;
        background: #f0edff;
      }
    }

    .data-type-cell {
      display: flex;
      align-items: center;
      gap: 8px;

      .data-type-icon {
        width: 20px;
        height: 20px;
      }
    }

    .data-count {
      font-weight: 600;
      color: #303133;
    }

    .data-time {
      color: #909399;
      font-size: 12px;
    }

    .data-impact {
      color: #606266;
      font-size: 12px;
    }

    .table-footer {
      text-align: center;
      padding: 12px 0 0;
      font-size: 12px;
      color: #909399;
      border-top: 1px solid #ebeef5;
      margin-top: 8px;
    }
  }

  /* 安全校验 */
  .security-check {
    background: #fff;
    border-radius: 12px;
    padding: 20px 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .check-item-sc {
      margin-bottom: 20px;

      &:last-of-type {
        margin-bottom: 0;
      }

      .check-label-sc {
        font-size: 13px;
        color: #606266;
        margin-bottom: 8px;
        font-weight: 500;

        .required {
          color: #fe4f4f;
          margin-right: 4px;
        }
      }

      .check-input {
        width: 100%;
      }
    }

    .confirm-checkbox {
      margin-top: 20px;
      padding-top: 16px;
      border-top: 1px solid #ebeef5;

      :deep(.el-checkbox__label) {
        font-size: 13px;
        color: #606266;
      }
    }
  }

  /* 按钮区域 */
  .btn-group {
    display: flex;
    padding: 13px 15px;
    border-top: 1px solid #ebeef5;
    justify-content: flex-end;
    gap: 12px;
    background-color: #ffffff;

    .btn-cancel {
      min-width: 88px;
    }

    .btn-export {
      min-width: 100px;
    }

    .btn-delete {
      min-width: 120px;
    }
  }
}
</style>
