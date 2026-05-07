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
    <div class="pagecont-bottom">
      <div class="infotop-title mb15">
        <div class="title-left">
          <span class="titleIcon"></span> {{ title }}
        </div>
      </div>
      <el-form
        ref="documentRef"
        :model="form"
        :rules="rules"
        label-width="96px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="所属知识库" prop="knowledgeBaseId">
              <el-select
                disabled
                v-model="form.knowledgeBaseId"
                placeholder="请选择知识库"
                style="width: 755px"
              >
                <el-option
                  v-for="item in knowledgeBaseList"
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
          <el-col :span="24">
            <el-form-item label="所属分类" prop="categoryId">
              <el-tree-select
                style="width: 755px"
                v-model="form.categoryId"
                :data="KcOptions"
                :props="{ value: 'id', label: 'label', children: 'children' }"
                value-key="id"
                placeholder="请选择所属分类"
                check-strictly
                @change="handleTypeChange"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件描述" prop="description">
              <el-input
                v-model="form.description"
                placeholder="请输入文件描述"
                style="width: 755px"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件路径" prop="path">
              <div style="width: 755px">
                <FileUpload
                  v-model="form.path"
                  :fileName="form.name"
                  :fileSize="15"
                  :platForm="platForm"
                  :fileType="[
                    'txt',
                    'pdf',
                    'html',
                    'xlsx',
                    'xls',
                    'docx',
                    'csv',
                    'md',
                    'mdx',
                    'htm',
                    'markdown',
                  ]"
                  @update:fileName="updateFormFileName"
                  @delete:index="handleDeleteFile"
                ></FileUpload>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="underline"></div>
      <div
        v-if="settingBase == null || settingBase.mode === 'custom'"
        :class="form.mode == 'custom' ? 'segmentation act' : 'segmentation'"
        @click="handleMode('custom')"
      >
        <div :class="form.mode == 'custom' ? 'titleBox xz' : 'titleBox'">
          <div class="segmentation-title">
            <img src="../../../../assets/app/‌Universal.png" alt="" />
            <div>
              <div :class="form.mode == 'custom' ? 'hbt txz' : 'hbt'">通用</div>
              <div class="text">通用文本分块模式，检索和召回的块相同的</div>
            </div>
          </div>
        </div>
        <div class="item-box" v-if="form.mode == 'custom'">
          <div class="itema-two" style="border: 0">
            <div class="two-item">
              <div class="icontitle">
                <span>分段标识符</span>
                <el-tooltip
                  content="分隔符是用于分隔文本的字符。\n\n和 \n 是常用于分隔段落和行的分隔符。用逗号连接分隔符(\n\n,\n)，当段落超过最大块长度时，会按行进行分割。你也可以使用自定义的特殊分隔符(例如 ***)。"
                >
                  <!-- <QuestionFilled -->
                  <img
                    class="imgicon"
                    src="../../../../assets/kmc/QuestionFilled.png"
                    alt=""
                  />
                </el-tooltip>
              </div>
              <div>
                <el-input
                  v-model="form.separator"
                  placeholder="请输入内容"
                  style="width: 250px"
                ></el-input>
              </div>
            </div>
            <div class="two-item">
              <div class="icontitle">
                <span>分段最大长度</span>
              </div>
              <div>
                <el-input-number
                  v-model="form.maxTokens"
                  controls-position="right"
                  style="width: 250px"
                ></el-input-number>
              </div>
            </div>
            <div class="two-item">
              <div class="icontitle">
                <span>分段重叠长度</span>
                <el-tooltip
                  content="设置分段之间的重叠长度可以保留分段之间的语义关系，提升召回效果。建议设置为最大分段长度的10%-25%"
                >
                  <!-- <QuestionFilled -->
                  <img
                    class="imgicon"
                    src="../../../../assets/kmc/QuestionFilled.png"
                    alt=""
                  />
                </el-tooltip>
              </div>
              <div>
                <el-input-number
                  v-model="form.chunkOverlap"
                  controls-position="right"
                  @change="handleChange"
                  style="width: 250px"
                ></el-input-number>
              </div>
            </div>
          </div>
          <div class="item-a">
            <img
              src="../../../../assets/app/kmcDocumentAdd/icontitle.png"
              alt=""
            />
            <span>文本预处理规则</span>
          </div>
          <el-checkbox v-model="form.removeExtraSpaces"
            >替换掉连续的空格、换行符和制表符</el-checkbox
          >
          <el-checkbox v-model="form.removeUrlsEmails"
            >删除所有 URL 和电子邮件地址</el-checkbox
          >
          <div class="bottomCheck">
            <el-checkbox
              v-model="checkedyy"
              @change="eckboxChange"
              :disabled="settingBase"
            >
              <span>使用Q&A分段,语言</span>
              <el-select
                v-model="form.docLanguage"
                placeholder="请选择"
                style="width: 160px; border: none"
                :disabled="!checkedyy"
              >
                <el-option
                  v-for="dict in kmc_language_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
              <span style="margin-left: 8px">模型</span>
              <el-select
                v-model="form.chatModel"
                placeholder="请选择"
                style="width: 160px; border: none"
                :disabled="!checkedyy"
              >
                <el-option-group
                  v-for="group in chatModel"
                  :key="group.label.zh_Hans"
                  :label="group.label.zh_Hans"
                >
                  <el-option
                    v-for="item in group.models"
                    :key="item.model"
                    :label="item.model"
                    :value="item.model"
                  />
                </el-option-group>
              </el-select>
            </el-checkbox>
            <!--                      <span style="font-size: 14px;color: #333;margin-left: 8px;">模型</span>-->
            <el-tooltip content="开启后将会消耗额外的 token">
              <img
                class="imgicon"
                src="../../../../assets/kmc/QuestionFilled.png"
                alt=""
              />
            </el-tooltip>
          </div>
        </div>
      </div>
      <div class="underline3"></div>
      <div class="btnBox">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import FileUpload from "@/components/FileUpload2/index.vue";
import { kmcCategoryTree } from "@/api/kmc/kmcCategory/kmcCategory.js";
import { getKmcKnowledgeBaseList } from "@/api/kmc/knowledgeBase/knowledgeBase.js";
import {
  addDocument,
  updateDocument,
  getDocument,
  getOne,
} from "@/api/kmc/kmcDocument/kmcDocument.js";
import { getChatModelDict } from "@/api/ai/myModel/myModel.js";
import moment from "moment/moment.js";
const platForm = ref("aliyun-oss-qt");
const { proxy } = getCurrentInstance();
const title = ref("新增知识文件");
const { kmc_language_type } = proxy.useDict("kmc_language_type");
const data = reactive({
  form: {},
  rules: {
    knowledgeBaseId: [
      { required: true, message: "所属知识库不能为空", trigger: "blur" },
    ],
    categoryId: [
      { required: true, message: "所属分类不能为空", trigger: "blur" },
    ],
    // name: [{ required: true, message: '文件名称不能为空', trigger: 'blur' }],
    path: [{ required: true, message: "文件路径不能为空", trigger: "blur" }],
  },
});
const checkedyy = ref(false);
const knowledgeBaseList = ref([]);
const KcOptions = ref(undefined);
const checkList = ref(["a", "b"]);
const settingBase = ref(null);
const route = useRoute();
const { form, rules } = toRefs(data);
const chatModel = ref([]);

function handleMode(model) {
  form.value.mode = model;
  form.value.docForm = "text_model";
  if (model === "hierarchical") {
    form.value.docForm = "hierarchical_model";
  }
}

function getKmcCategoryTree() {
  kmcCategoryTree({ knowledgeBaseId: form.value.knowledgeBaseId }).then(
    (response) => {
      KcOptions.value = response.data;
      if (!proxy.$route.query.id) {
        handleTypeChange(form.value.query.categoryId);
      }
    }
  );
}
/** 处理选择变化的方法 */
const handleTypeChange = (value) => {
  const selectedNode = findNodeById(KcOptions.value, value);
  if (selectedNode) {
    form.value.categoryName = selectedNode.label;
  }
};
const eckboxChange = () => {
  if (checkedyy) {
    form.value.docForm = "qa_model";
  } else {
    form.value.docForm = "text_model";
  }
};
/** 递归查找选中的节点 */
const findNodeById = (nodes, id) => {
  for (let node of nodes) {
    if (node.id === id) {
      return node;
    }
    if (node.children) {
      const found = findNodeById(node.children, id);
      if (found) return found;
    }
  }
  return null;
};
const cancel = () => {
  const obj = {
    path: `/kmc/${route.params.kbId}/kmcDocument`,
  };
  proxy.$tab.closeOpenPage(obj);
};

function submitForm() {
  form.value.docForm = checkedyy.value ? "qa_model" : "text_model";
  proxy.$refs["documentRef"].validate((valid) => {
    if (valid) {
      proxy.$modal.loading("正在保存，请稍候...");
      if (form.value.createTime != null) {
        form.value.createTime = moment(form.value.createTime).format(
          "YYYY-MM-DD HH:mm:ss"
        );
      }
      if (form.value.updateTime != null) {
        form.value.updateTime = moment(form.value.updateTime).format(
          "YYYY-MM-DD HH:mm:ss"
        );
      }
      form.value.separator = form.value.separator.replace(/\\n/g, "\n");
      form.value.subchunkSeparator = form.value.subchunkSeparator.replace(
        /\\n/g,
        "\n"
      );
      if (form.value.id != null) {
        updateDocument(form.value)
          .then((response) => {
            proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("修改成功");
            cancel();
          })
          .catch((error) => {
            proxy.$modal.closeLoading();
          });
      } else {
        //将form.value.name由数组转为字符串用逗号分割
        if (Array.isArray(form.value.name)) {
          form.value.name = form.value.name.join(",");
        }
        addDocument(form.value)
          .then((response) => {
            proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("新增成功");
            cancel();
          })
          .catch((error) => {
            proxy.$modal.closeLoading();
          });
      }
    }
  });
}

function getKnowledge() {
  getKmcKnowledgeBaseList().then((res) => {
    knowledgeBaseList.value = res.data;
    getKmcCategoryTree();
  });
}
function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    categoryId: proxy.$route.query.categoryId
      ? Number(proxy.$route.query.categoryId)
      : null,
    categoryName: null,
    name: [],
    path: null,
    description: "",
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
    previewCount: null,
    downloadCount: null,

    knowledgeBaseId: Number(proxy.$route.params.kbId),
    knowledgeBaseName: null,
    mode: "custom",
    parentMode: "paragraph",
    removeExtraSpaces: true,
    removeUrlsEmails: false,
    chunkOverlap: 50,
    maxTokens: 1024,
    separator: "\n\n",
    docForm: checkedyy.value ? "qa_model" : "text_model",
    docLanguage: "Chinese",
    subchunkMaxTokens: 512,
    subchunkSeparator: "\n",
    chatModel: null,
    chatModelProvider: null,
  };
  form.value.separator = form.value.separator.replace(/\n/g, "\\n");
  form.value.subchunkSeparator = form.value.subchunkSeparator.replace(
    /\n/g,
    "\\n"
  );

  if (KcOptions.value) {
    handleTypeChange(form.value.categoryId);
  }

  proxy.resetForm("documentRef");
}
function handleUpdate(id) {
  getDocument(id).then((response) => {
    form.value = response.data;
    if (form.value.separator) {
      form.value.separator = form.value.separator.replace(/\n/g, "\\n");
    }
    getFirstConfig(response.data.knowledgeBaseId);
    if (response.data.knowledgeBaseId !== Number(proxy.$route.params.kbId)) {
      proxy.$modal.msgSuccess("文档与知识库不匹配，当前页面进行关闭。");
      cancel();
    }
  });
}
const updateFormFileName = (newValue) => {
  if (Array.isArray(form.value.name)) {
    form.value.name.push(newValue);
  }
};

// 处理文件删除
const handleDeleteFile = (index) => {
  // 从form.name数组中删除对应索引的文件名
  if (Array.isArray(form.value.name)) {
    form.value.name.splice(index, 1);
  }
};

// 获取文件下的第一个配置
function getFirstConfig(id) {
  getOne(id).then((res) => {
    settingBase.value = res.data;
    checkedyy.value = res.data.docForm === "qa_model";
    form.value.mode = res.data.mode;
    form.value.docForm = res.data.docForm;
  });
}
onMounted(async () => {
  getKnowledge();
});

watch(
  () => proxy.$route.query.id,
  (newId) => {
    title.value = "新增知识文件";
    if (newId) {
      title.value = "修改知识文件";
      handleUpdate(newId);
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => [proxy.$route.params.kbId, proxy.$route.query.categoryId],
  () => {
    if (!proxy.$route.query.id) {
      reset();
    }
    if (proxy.$route.params.kbId) {
      form.value.knowledgeBaseId = Number(proxy.$route.params.kbId);
      getKmcCategoryTree();
      getFirstConfig(proxy.$route.params.kbId);
    }
  },
  { deep: true, immediate: true }
);
// 模型联动逻辑
watchEffect(() => {
  const selectedModel = form.value.chatModel;
  if (!selectedModel) return;
  for (const group of chatModel.value) {
    const found = group.models.find((item) => item.model === selectedModel);
    if (found) {
      form.value.chatModelProvider = group.provider;
      return;
    }
  }
  form.value.chatModelProvider = "";
});
// 初始化模型数据
function init() {
  getChatModelDict().then((response) => {
    chatModel.value = response.data;
  });
}
// 初始化
init();
</script>

<style scoped lang="scss">
.infotop-title {
  display: flex;
  align-items: center;
  //color: #4b7eed;
  border-bottom: 2px solid #f4f4f4;
  height: 40px;
  .title-left {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .titleIcon {
    display: inline-block;
    width: 5px;
    height: 20px;
    border-radius: 2px;
    background: #4b7eed;
    margin: 0 10px 0 0;
  }
}
.pagecont-bottom {
  height: calc(100vh - 125px);
  width: 98.3%;
  position: absolute;
  margin-top: 0;
  .formFlex {
    display: flex;
  }
  .underline {
    border-bottom: 2px solid #f4f4f4;
    margin: -10px 0 22px 0;
  }
  .underline2 {
    border-bottom: 2px solid #f4f4f4;
    margin: 5px 0 22px 0;
  }
  .underline3 {
    border-bottom: 2px solid #f4f4f4;
    margin: 158px 0px 15px 0px;
  }
  .btnBox {
    position: absolute;
    right: 15px;
    bottom: 15px;
  }
  .segmentation {
    width: 852px;

    border: 1px solid #d9d9d9;
    border-radius: 2px;
    &.act {
      border: 1px solid #92b3fc;
    }
    .titleBox {
      width: 100%;
      // height: 50px;
      padding: 10px 20px;
      &.xz {
        background: #f6f9ff;
      }
      .segmentation-title {
        height: 100%;
        display: flex;
        align-items: center;

        img {
          width: 30px;
          height: 30px;
          margin-right: 10px;
        }
        .hbt {
          color: #747474;
          &.txz {
            color: #135afb;
          }
        }
        .text {
          font-size: 14px;
          color: #b4b4b4;
        }
      }
    }
    .item-box {
      padding: 10px 20px;
      .item-a {
        display: flex;
        align-items: center;
        img {
          width: 13px;
          height: 13px;
          margin-right: 10px;
        }
        span {
          color: #135afb;
          font-size: 14px;
        }
      }
      .bottomCheck {
        margin-top: 10px;
        display: flex;
        align-items: center;
        :deep(.el-select__wrapper) {
          min-height: 20px;
          margin-left: 10px;
        }

        .imgicon {
          width: 15px;
          height: 15px;
          margin-left: 10px;
        }
      }
      .itema-one {
        width: 100%;
        border: 1px solid #f4f4f4;
        margin-top: 10px;
        padding: 5px 10px;
        .itema-onetitle {
          display: flex;
          align-items: center;
          justify-content: space-between;
          .leftone {
            display: flex;
            align-items: center;
            img {
              width: 25px;
              height: 25px;
              margin-right: 10px;
            }
            .hbt {
              font-size: 14px;
              color: #747474;
              &.txz {
                color: #135afb;
              }
            }
            .text {
              font-size: 14px;
              color: #b4b4b4;
            }
          }
        }
      }
      .itema-two {
        width: 100%;
        padding: 10px 10px;
        border: 1px solid #f4f4f4;
        border-top: none; /* 上边框不设置 */
        display: flex;
        align-items: center;
        justify-content: space-between;
        .two-item {
          .icontitle {
            display: flex;
            align-items: center;
            font-size: 14px;
            color: #747474;
            margin-bottom: 5px;
            img {
              width: 15px;
              height: 15px;
              margin-left: 5px;
            }
          }
        }
      }
      .itema-two2 {
        width: 100%;
        padding: 10px 10px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .two-item {
          .icontitle {
            display: flex;
            align-items: center;
            font-size: 14px;
            color: #747474;
            margin-bottom: 5px;
            img {
              width: 15px;
              height: 15px;
              margin-left: 5px;
            }
          }
        }
      }
    }
  }
  .segmentation2 {
    width: 852px;
    padding: 6px 20px;
    border: 1px solid #92b3fc;
    border-radius: 2px;
    // height: 606px;
    .segmentation-title {
      height: 50px;
      display: flex;
      align-items: center;
      img {
        width: 30px;
        height: 30px;
        margin-right: 10px;
      }
      .hbt {
        color: #747474;
      }
      .text {
        font-size: 14px;
        color: #b4b4b4;
      }
    }
  }
}

:deep(.el-upload-dragger) {
  height: 110px;
}
:deep(.el-icon--upload) {
  font-size: 42px;
  margin-bottom: 0;
  margin-top: -50px;
}
:deep(.el-upload__text) {
  margin-top: -10px;
  font-size: 14px;
}
:deep(.el-radio__label) {
  display: none;
}
:deep(.checkbox__label) {
  width: 375px;
}
:deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: #333333;
}
</style>
