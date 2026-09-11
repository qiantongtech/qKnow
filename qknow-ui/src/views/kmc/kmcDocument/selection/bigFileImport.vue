<!--
 Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

 This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).

 qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 See the LICENSE file in the project root for full license information.
-->

<template>

  <div>
    <el-upload
        ref="uploadRef"
        class="my-big-json-upload"
        :limit="5"
        :disabled="upload.isUploading"
        :http-request="handleUpload"
        :file-list="fileList"
        :auto-upload="true"
        :show-file-list="false"
        :on-change="handleChange"
        accept=".json,.jsonl"
        drag
        multiple
        show-file-list
    >
      <el-icon class="el-icon--upload">
        <upload-filled/>
      </el-icon>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip">
          请上传格式为 <b style="color: #f56c6c">json/jsonl</b> 的文件
        </div>
      </template>
    </el-upload>

    <!-- 文件列表 -->
    <transition-group
        class="upload-file-list el-upload-list el-upload-list--text"
        name="el-fade-in-linear"
        tag="ul"
    >
      <li :key="file.uid" class="filelistcont" v-for="(file, index) in fileList">
        <div class="filelistcont-name">
          <span class="el-icon-document"> {{ file.name }} </span>
        </div>
        <div class="ele-upload-list__item-content-action">
          <el-link :underline="false" @click="handleDelete(index)" type="danger">删除</el-link>
        </div>
        <div style="margin-left:10px ">
          <el-text class="mx-1" :type="uploadProgressMap[file.uid]?.status">
            {{ uploadProgressMap[file.uid]?.msg }}
          </el-text>
        </div>
      </li>
    </transition-group>
  </div>

</template>

<script setup name="bigFileImport">

import {getToken} from "@/utils/auth.js";
import SparkMD5 from 'spark-md5'
import axios from 'axios'
import {uploadCheck, uploadMerge} from '@/api/kmc/knowledgeSegment/knowledgeSegment';

const {proxy} = getCurrentInstance();
const emit = defineEmits(['update:modelValue', 'update:fileName'])
const uploadRef = ref(null)
const fileList = ref([])// 绑定文件列表
const uploadProgressMap = ref({})// uid：{"status":"","msg":""}

const props = defineProps({
  modelValue: [String, Object, Array],
  fileName: {
    type: [String, Array],
    default: ''
  }
});

/**
 * 编辑半结构化文件时，根据父表单已有的名称和路径初始化文件列表。
 * 本地已有待上传文件时不覆盖，避免多文件并行上传过程中丢失状态。
 */
watch(
  () => [props.modelValue, props.fileName],
  ([modelValue, fileName]) => {
    if (fileList.value.some(item => !item.existingFile)) {
      return;
    }

    const pathList = Array.isArray(modelValue)
      ? modelValue
      : String(modelValue || '').split(',').filter(Boolean);
    const nameList = Array.isArray(fileName)
      ? fileName
      : String(fileName || '').split(',').filter(Boolean);

    fileList.value = nameList.map((name, index) => ({
      name,
      url: pathList[index] || pathList[0] || '',
      uid: `existing-${index}-${name}`,
      status: 'success',
      existingFile: true
    }));
  },
  { immediate: true }
);

/*** 用户导入参数 */
const upload = {
  isUploading: false,// 是否禁用上传
  chunkSize: 5 * 1024 * 1024, // 单分片5MB
  headers: {// 上传的请求头
    "Content-Type": "multipart/form-data",
    "Authorization": "Bearer " + getToken()
  },
  url: import.meta.env.VITE_APP_BASE_API + '/kmc/knowledgeSegment/upload/chunk'// 上传的地址
}

/**
 * 计算文件MD5（用于断点续传）
 */
const calculateFileMD5 = async (file) => {
  return new Promise((resolve) => {
    const spark = new SparkMD5.ArrayBuffer()
    const reader = new FileReader()
    const chunkSize = 20 * 1024 * 1024
    let currentChunk = 0
    const chunks = Math.ceil(file.size / chunkSize)

    const loadNext = () => {
      const start = currentChunk * chunkSize
      const end = Math.min(start + chunkSize, file.size)
      reader.readAsArrayBuffer(file.slice(start, end))
    }

    reader.onload = (e) => {
      spark.append(e.target.result)
      currentChunk++
      if (currentChunk < chunks) {
        loadNext()
      } else {
        resolve(spark.end())
      }
    }
    loadNext()
  })
}

/**
 * 自定义分片上传逻辑
 */
const handleUpload = async (uploadFileObj) => {
  const file = uploadFileObj.file
  uploadProgressMap.value[file.uid] = {status: 'info', msg: '正在预处理文件...'}
  const fileMd5 = await calculateFileMD5(file)// 计算文件MD5
  const totalChunks = Math.ceil(file.size / upload.chunkSize)
  try {
    const res = await uploadCheck(fileMd5)
    const uploadedChunkList = res.data || []// 查询已上传分片

    // 分片上传
    for (let chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
      if (uploadedChunkList.includes(chunkIndex)) {// 跳过已上传分片
        const uploadProgress = Math.round(((chunkIndex + 1) / totalChunks) * 100)
        uploadProgressMap.value[file.uid] = {status: 'primary', msg: "上传中：" + uploadProgress + "%"}
        continue
      }

      const start = chunkIndex * upload.chunkSize
      const end = Math.min(start + upload.chunkSize, file.size)
      const chunkBlob = file.slice(start, end)
      await uploadChunk(chunkBlob, fileMd5, file.name, chunkIndex, totalChunks, file.size)

      // 更新进度
      const uploadProgress = Math.round(((chunkIndex + 1) / totalChunks) * 100)
      uploadProgressMap.value[file.uid] = {status: 'primary', msg: "上传中：" + uploadProgress + "%"}
    }
    await mergeChunk(fileMd5, file, totalChunks);
    uploadProgressMap.value[file.uid] = {status: 'success', msg: "上传完成"}
    uploadRef.value.clearFiles()
  } catch (err) {
    uploadProgressMap.value[file.uid] = {status: 'danger', msg: "上传失败"}
  }
}

/**
 * 上传分片
 */
async function uploadChunk(chunkBlob, fileMd5, fileName, chunkIndex, totalChunks, fileSize) {
  // 构造formData
  const formData = new FormData()
  formData.append('chunk', chunkBlob)
  formData.append('fileMd5', fileMd5)
  formData.append('fileName', fileName)
  formData.append('chunkIndex', chunkIndex)
  formData.append('totalChunks', totalChunks)
  formData.append('fileSize', fileSize)

  // 单分片上传接口
  await axios.post(upload.url, formData, {
    // 关闭axios默认超时，大分片延长超时
    timeout: 300000,
    headers: upload.headers,
  })
}

/**
 * 合并分片
 */
async function mergeChunk(fileMd5, file, totalChunks) {
  const mergeData = {
    "fileMd5": fileMd5,
    "fileName": file.name,
    "chunkTotal": totalChunks,
    "fileSize": file.size
  }
  const mergeRes = await uploadMerge(mergeData);
  const item = fileList.value.find(row => row.uid === file.uid)
  item.url = mergeRes.data;
  const fileInfo = getFileString();
  emit('update:modelValue', fileInfo.pathList);
  emit('update:fileName', fileInfo.nameList);
}

/**
 * 监听文件上传
 */
function handleChange(uploadFile, uploadFiles) {
  const fileType = ["json","jsonl"]
  const fileName = uploadFile.name.split('.');
  const fileExt = fileName[fileName.length - 1];
  const isTypeOk = fileType.indexOf(fileExt) >= 0;
  if (!isTypeOk) {
    proxy.$modal.msgError(`文件格式不正确, 请上传${fileType.join('/')}格式文件!`);
    return ;
  }
  const exist = fileList.value.some(item => item.uid === uploadFile.uid);
  if (!exist) {
    fileList.value.push(uploadFile)
  }
}

// 删除文件
function handleDelete(index) {
  // 删除前获取文件名用于父组件同步更新
  const deletedFileName = fileList.value[index]?.name;
  fileList.value.splice(index, 1);
  const fileInfo = getFileString();
  // todo： 还应该停止上传操作
  emit('update:modelValue', fileInfo.pathList);
  emit('update:fileName', fileInfo.nameList);
}

// 对象转成指定字符串分隔
function getFileString() {
  let pathList = [];
  let nameList = [];
  for (let i in fileList.value) {
    let file = fileList.value[i];
    if (file.name && file.url) {
      pathList.push(file.url)
      nameList.push(file.name)
    }
  }
  return {"pathList": pathList.join(','), "nameList": nameList.join(',')};
}

/**
 * 打开选择框
 * @param {Array} val 选中的对象数组
 */
function uploadStatus() {
  let status = "success";
  fileList.value.forEach(item => {
    if (!item.url || item.url.length === 0) {
      status = "update";
      return status
    }
  })
  return status
}

defineExpose({uploadStatus});
</script>

<style scoped lang="scss">
.upload-file {
  width: 100%;
}

.upload-file-uploader {
  margin-bottom: 5px;
}

.filelistcont {
  display: flex;
  align-items: center;

  .filelistcont-name {
    margin-right: 10px;
  }
}
</style>
