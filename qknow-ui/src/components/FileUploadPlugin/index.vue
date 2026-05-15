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
  <div class="upload-file">
    <el-upload
      multiple
      :action="uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :file-list="fileList"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-success="handleUploadSuccess"
      :show-file-list="false"
      :headers="headers"
      class="upload-file-uploader"
      ref="fileUpload"
      :data="uploadData"
      :drag="dragFlag"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text" style="width: 100%">
        将文件拖到此处，或<em>点击上传</em>
      </div>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip">
      请上传
      <template v-if="fileSize">
        大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
      </template>
      <template v-if="fileType">
        格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b>
      </template>
      的文件
    </div>
    <!-- 文件列表 -->
    <transition-group
      class="upload-file-list el-upload-list el-upload-list--text"
      name="el-fade-in-linear"
      tag="ul"
    >
      <!-- <li :key="file.uid" class="el-upload-list__item ele-upload-list__item-content" v-for="(file, index) in fileList">
  <el-link :href="`${baseUrl}${file.url}`" :underline="false" target="_blank">
    <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
  </el-link>
  <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
  <div class="ele-upload-list__item-content-action">
    <el-link :underline="false" @click="handleDelete(index)" type="danger">删除</el-link>
  </div>
</li> -->
      <li
        :key="file.uid"
        class="filelistcont"
        v-for="(file, index) in fileList"
      >
        <div class="filelistcont-name">
          <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
        </div>
        <div class="ele-upload-list__item-content-action">
          <el-link :underline="false" @click="handleDelete(index)" type="danger"
            >删除</el-link
          >
        </div>
      </li>
    </transition-group>
  </div>
</template>

<script setup>
import { getToken } from "@/utils/auth";

const props = defineProps({
  modelValue: [String, Object, Array],
  // 数量限制
  limit: {
    type: Number,
    default: 100,
  },
  fileName: {
    type: String,
    default: "",
  },
  // 大小限制(MB)
  fileSize: {
    type: Number,
    default: 5,
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array,
    default: () => ["txt", "pdf", "xlsx", "xls", "docx", "csv"],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true,
  },
  // platform参数
  platForm: {
    type: String,
    default: "",
  },
  // 是否支持拖拽上传
  dragFlag: {
    type: Boolean,
    default: true,
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const number = ref(0);
const uploadList = ref([]);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const env = import.meta.env.VITE_APP_ENV;
const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + "/upload-plugin"); // 上传文件服务器地址
const headers = ref({ Authorization: "Bearer " + getToken() });
const fileList = ref([]);
const uploadData = ref({
  platForm: "local",
});
const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize)
);

watch(
  () => props.modelValue,
  (val) => {
    if (val) {
      let temp = 1;
      // 首先将值转为数组
      const list = Array.isArray(val) ? val : props.modelValue.split(",");
      //props.fileName逗号分割转成数组
      const fileName =
        typeof props.fileName === "string" ? [props.fileName] : props.fileName; // 然后将数组转为对象数组
      fileList.value = list.map((item, index) => {
        if (typeof item === "string") {
          item = { name: fileName[index], url: item };
        }
        item.uid = item.uid || new Date().getTime() + temp++;
        return item;
      });
    } else {
      fileList.value = [];
      return [];
    }
  },
  { deep: true, immediate: true }
);

// 上传前校检格式和大小
function handleBeforeUpload(file) {
  // 校检文件类型
  if (props.fileType.length) {
    const fileName = file.name.split(".");
    const fileExt = fileName[fileName.length - 1];
    const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
    if (!isTypeOk) {
      proxy.$modal.msgError(
        `文件格式不正确, 请上传${props.fileType.join("/")}格式文件!`
      );
      return false;
    }
  }
  // 校检文件大小
  // if (props.fileSize) {
  //   const isLt = file.size / 1024 / 1024 < props.fileSize;
  //   if (!isLt) {
  //     proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`);
  //     return false;
  //   }
  // }
  proxy.$modal.loading("正在上传文件，请稍候...");
  number.value++;
  return true;
}

// 文件个数超出
function handleExceed() {
  proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
}

// 上传失败
function handleUploadError(err) {
  proxy.$modal.msgError("上传文件失败");
}

// 上传成功回调
function handleUploadSuccess(res, file) {
  if (res.url) {
    const fileName = getFileName(file.name);
    uploadList.value.push({
      name: "/profile/" + res.path + res.filename,
      url: res.url,
    });
    emit("update:fileName", fileName);
    if (res.size) {
      emit("update:fileSize", res.size); // 更新文件大小
    }
    if (res.ext) {
      emit("update:fileExt", res.ext); // 更新文件后缀名
    }
    uploadedSuccessfully();
  } else {
    number.value--;
    proxy.$modal.closeLoading();
    proxy.$modal.msgError(res.msg);
    proxy.$refs.fileUpload.handleRemove(file);
    uploadedSuccessfully();
  }
}

// 删除文件
function handleDelete(index) {
  // 删除前获取文件名用于父组件同步更新
  const deletedFileName = fileList.value[index]?.name;
  fileList.value.splice(index, 1);
  emit("update:modelValue", listToString(fileList.value));
  emit("update:fileExt", null); // 更新文件后缀名
  emit("update:fileSize", null); // 更新文件大小
  emit("delete:index", index);
  // 如果需要更精确的匹配，可以传递文件名
  emit("delete:file", deletedFileName);
}

// 上传结束处理
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value
      .filter((f) => f.url !== undefined)
      .concat(uploadList.value);
    uploadList.value = [];
    number.value = 0;
    emit("update:modelValue", listToString(fileList.value));
    proxy.$modal.closeLoading();
  }
}

// 获取文件名称
function getFileName(name) {
  // 如果是url那么取最后的名字 如果不是直接返回
  if (name.lastIndexOf("/") > -1) {
    return name.slice(name.lastIndexOf("/") + 1);
  } else {
    return name;
  }
}

// 对象转成指定字符串分隔
function listToString(list, separator) {
  let strs = "";
  separator = separator || ",";
  for (let i in list) {
    if (list[i].url) {
      strs += list[i].url + separator;
    }
  }
  return strs != "" ? strs.substr(0, strs.length - 1) : "";
}
</script>

<style scoped lang="scss">
.upload-file {
  width: 100%;
}
.upload-file-uploader {
  margin-bottom: 5px;
}
// .upload-file-list .el-upload-list__item {
//   border: 1px solid #e4e7ed;
//   line-height: 2;
//   margin-bottom: 10px;
//   position: relative;
// }
// .upload-file-list .ele-upload-list__item-content {
//   display: flex;
//   justify-content: space-between;
//   align-items: center;
//   color: inherit;
// }
// .ele-upload-list__item-content-action .el-link {
//   margin-right: 10px;
// }
.filelistcont {
  display: flex;
  align-items: center;
  .filelistcont-name {
    margin-right: 10px;
  }
}
</style>
