<!--
 Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 
 This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 
 qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 You may use qKnow for commercial purposes, but you may not remove, hide,
 modify, or replace the qKnow logo, copyright notices, license notices,
 or attribution information without a separate commercial license.
 
 White-label use, OEM distribution, rebranding, or presenting qKnow as
 another product requires separate commercial authorization from
 Jiangsu Qiantong Technology Co., Ltd.
 
 Business License: https://community.qknow.ai/business/policy.html
 See the LICENSE file in the project root for full license information.
-->

<template>
  <el-form
    ref="formRef"
    :model="form"
    :rules="rules"
    label-width="170px"
    class="storage-connection-step"
    @submit.prevent
  >
    <div class="h2-titles">数据连接类型</div>

    <el-form-item prop="datasourceType" label-width="0">
      <div class="storage-type-grid">
        <button
          v-for="item in storageTypes"
          :key="item.value"
          type="button"
          class="storage-type-card"
          :class="{ active: selectedType === item.value }"
          :aria-pressed="selectedType === item.value"
          @click="selectType(item.value)"
        >
          <span
            v-if="selectedType === item.value"
            class="storage-type-card__selected"
            aria-hidden="true"
          />
          <img
            :src="item.icon"
            :alt="item.label"
            class="storage-type-card__icon"
          />
          <div class="storage-type-card__content">
            <div class="storage-type-card__title">{{ item.label }}</div>
            <div class="storage-type-card__desc">{{ item.desc }}</div>
          </div>
        </button>
      </div>
    </el-form-item>

    <div class="h2-titles">数据连接配置</div>

    <el-row :gutter="20">
      <el-col
        v-for="field in currentFields"
        :key="`${selectedType}-${field.key}`"
        :span="field.span || 12"
      >
        <el-form-item :label="field.label" :prop="field.key">
          <el-input
            v-if="field.component === 'input'"
            v-model="form[field.key]"
            :placeholder="field.placeholder"
          />
          <el-input
            v-else-if="field.component === 'password'"
            v-model="form[field.key]"
            type="password"
            :placeholder="field.placeholder"
            show-password
          />
          <el-input
            v-else-if="field.component === 'textarea'"
            v-model="form[field.key]"
            type="textarea"
            :rows="field.rows || 3"
            :maxlength="field.maxlength + '个字符' || 500 + '个字符'"
            :placeholder="field.placeholder"
            resize="vertical"
            show-word-limit
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup name="StorageConnectionStep">
import { computed, reactive, ref, watch } from "vue";
import hdfsIcon from "@/assets/kmc/storage/hdfs.svg";
import ossIcon from "@/assets/kmc/storage/oss.svg";
import ftpIcon from "@/assets/kmc/storage/ftp.svg";

const storageTypes = [
  {
    value: "HDFS",
    label: "HDFS",
    desc: "分布式文件系统，适用于大数据文件存储接入。",
    icon: hdfsIcon,
  },
  {
    value: "FTP",
    label: "FTP",
    desc: "文件传输服务，适用于批量文件读取与写入。",
    icon: ftpIcon,
  },
  {
    value: "OSS-ALIYUN",
    label: "OSS（阿里云）",
    desc: "阿里云对象存储，适用于云端文件数据接入。",
    icon: ossIcon,
  },
];

const emit = defineEmits(["change"]);

const ipPortFields = [
  {
    key: "ip",
    label: "IP",
    component: "input",
    placeholder: "请输入服务器 IP 地址",
  },
  {
    key: "port",
    label: "端口号",
    component: "input",
    placeholder: "请输入端口号",
  },
];

const authFields = [
  {
    key: "username",
    label: "账号",
    component: "input",
    placeholder: "请输入账号",
  },
  {
    key: "password",
    label: "密码",
    component: "password",
    placeholder: "请输入密码",
  },
];

const fieldsByType = {
  HDFS: [
    ...ipPortFields,
    {
      key: "config",
      label: "配置参数",
      component: "textarea",
      placeholder: '例如：{"kerberosKeytabFilePath":"/path/to/keytab/file"}',
      maxlength: 1000,
      rows: 2,
      span: 24,
    },
  ],
  "OSS-ALIYUN": [
    {
      key: "keyId",
      label: "KeyId",
      component: "input",
      placeholder: "请输入阿里云 AccessKey ID",
    },
    {
      key: "keySecret",
      label: "KeySecret",
      component: "password",
      placeholder: "请输入阿里云 AccessKey Secret",
    },
    {
      key: "bucket",
      label: "Bucket",
      component: "input",
      placeholder: "请输入 Bucket，例如：test",
    },
    {
      key: "endpoint",
      label: "Endpoint",
      component: "input",
      placeholder: "例如：oss-cn-beijing.aliyuncs.com",
    },
    {
      key: "domain",
      label: "域名",
      component: "input",
      placeholder: "请输入域名，例如：test.oss-cn-beijing.aliyuncs.com",
      span: 24,
    },
  ],
  FTP: [...ipPortFields, ...authFields],
};

const formRef = ref();
const selectedType = ref("HDFS");
const form = reactive(createInitialForm());

const currentFields = computed(() => fieldsByType[selectedType.value] || []);

const rules = {
  datasourceType: [
    { required: true, message: "请选择数据连接类型", trigger: "change" },
  ],
  ip: [{ required: true, message: "请输入IP", trigger: "blur" }],
  port: [{ required: true, message: "请输入端口号", trigger: "blur" }],
  username: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  keyId: [{ required: true, message: "请输入KeyId", trigger: "blur" }],
  keySecret: [{ required: true, message: "请输入KeySecret", trigger: "blur" }],
  bucket: [{ required: true, message: "请输入Bucket", trigger: "blur" }],
  endpoint: [{ required: true, message: "请输入Endpoint", trigger: "blur" }],
};
watch(
  form,
  () => emit("change", { ...form, datasourceType: selectedType.value }),
  { deep: true }
);

function createInitialForm() {
  return {
    datasourceType: "HDFS",
    ip: "",
    port: "",
    username: "",
    password: "",
    config: "",
    keyId: "",
    keySecret: "",
    bucket: "",
    endpoint: "",
    domain: "",
  };
}

function selectType(type) {
  if (selectedType.value === type) return;
  selectedType.value = type;
  const nextForm = createInitialForm();
  nextForm.datasourceType = type;
  Object.assign(form, nextForm);
  formRef.value?.clearValidate();
}

function reset() {
  selectedType.value = "HDFS";
  Object.assign(form, createInitialForm());
  formRef.value?.clearValidate();
}

defineExpose({
  validate: () => formRef.value?.validate(),
  getFormData: () => ({ ...form, datasourceType: selectedType.value }),
  reset,
});
</script>

<style scoped lang="scss">
.storage-connection-step {
  height: 100%;
  padding: 15px 90px 0 15px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;

  :deep(.el-form-item) {
    margin-bottom: 16px;
  }
}

.h2-titles {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  font-family: "PingFang SC", sans-serif;
  font-weight: 500;
  margin: 8px 0;
}

.h2-titles::before {
  display: inline-block;
  content: "";
  width: 6px;
  height: 16px;
  border-radius: 3px;
  background: var(--el-color-primary);
  margin-right: 8px;
}

.storage-type-grid {
  display: grid;
  width: calc(100% - 170px);
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
  margin-left: 170px;
}

.storage-type-card {
  position: relative;
  display: flex;
  min-height: 86px;
  align-items: center;
  padding: 13px 15px 12px;
  color: #303133;
  text-align: left;
  background: #fff;
  border: 1px solid rgba(214, 218, 225, 0.45);
  border-radius: 2px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    border-color: rgba(38, 102, 251, 1);
    box-shadow: 0 2px 8px rgba(38, 102, 251, 0.15);
  }

  &.active {
    border-color: rgba(232, 232, 232, 0.45);
    background-image: linear-gradient(
      180deg,
      rgba(255, 255, 255, 0) 0,
      rgba(38, 102, 251, 0.05) 100%
    );

    &:hover {
      border-color: rgba(38, 102, 251, 1);
      box-shadow: 0 2px 8px rgba(38, 102, 251, 0.15);
    }
  }

  &__selected {
    position: absolute;
    top: 12px;
    right: 11px;
    width: 14px;
    height: 14px;
    border: 4px solid rgba(38, 102, 251, 1);
    background-color: #ffffff;
    border-radius: 50%;
  }

  &__icon {
    width: 36px;
    height: 36px;
    flex-shrink: 0;
    margin-right: 14px;
    object-fit: contain;
  }

  &__content {
    min-width: 0;
  }

  &__title {
    margin-bottom: 5px;
    color: #303133;
    font-size: 14px;
    font-weight: 500;
    line-height: 20px;
  }

  &__desc {
    color: #777;
    font-size: 13px;
    line-height: 19px;
  }
}

@media (max-width: 900px) {
  .storage-type-grid {
    width: 100%;
    grid-template-columns: 1fr;
    margin-left: 0;
  }
}
</style>
