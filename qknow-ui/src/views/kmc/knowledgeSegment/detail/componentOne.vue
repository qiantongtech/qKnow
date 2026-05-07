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
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['knowledgeSegment:knowledgeSegment:add']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['knowledgeSegment:knowledgeSegment:export']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="374px" v-loading="loading" :data="knowledgeSegmentList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
            <el-table-column v-if="columns[7].visible" label="分段内容文本" align="center" prop="content">
              <template #default="scope">
                {{ scope.row.content || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[9].visible" label="答案内容(如果有)" align="center" prop="answer">
              <template #default="scope">
                {{ scope.row.answer || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[10].visible" label="内容长度" align="center" prop="wordCount">
              <template #default="scope">
                {{ scope.row.wordCount || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[12].visible" label="关键词" align="center" prop="keywords">
              <template #default="scope">
                {{ scope.row.keywords || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[15].visible" label="访问次数" align="center" prop="hitCount">
              <template #default="scope">
                {{ scope.row.hitCount || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[21].visible" label="分段添加dify状态" align="center" prop="syncStatus">
              <template #default="scope">
                    <dict-tag :options="sync_status" :value="scope.row.syncStatus"/>
              </template>
            </el-table-column>
            <el-table-column v-if="columns[28].visible" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                   v-hasPermi="['knowledgeSegment:knowledgeSegment:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                   v-hasPermi="['knowledgeSegment:knowledgeSegment:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                   v-hasPermi="['knowledgeSegment:knowledgeSegment:edit']">详情</el-button>
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/system/images/no_data/noData.png" alt="" />
        <p>暂无记录</p>
      </div>
    </template>
  </el-table>

  <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
  />

  <!-- 添加或修改文件分段对话框 -->
  <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
          <span role="heading" aria-level="2" class="el-dialog__title">
            {{ title }}
            <el-icon size="20" style="color: #909399; font-size: 16px">
              <InfoFilled />
            </el-icon>
          </span>
      <button aria-label="el.dialog.close" class="el-dialog__headerbtn" type="button">
        <i class="el-icon el-dialog__close"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
          <path fill="currentColor"
                d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z">
          </path>
        </svg></i>
      </button>
    </template>
    <el-form ref="knowledgeSegmentRef" :model="form" :rules="rules" label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-form-item label="分段内容文本">
                          <editor v-model="form.content" :min-height="192"/>
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="答案内容(如果有)" prop="answer">
                          <el-input v-model="form.answer" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-form-item label="关键词" prop="keywords">
                          <el-input v-model="form.keywords" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                      </el-col>
                    </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 文件分段详情对话框 -->
  <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
          <el-icon size="20" style="color: #909399; font-size: 16px">
            <InfoFilled />
          </el-icon>
        </span>
      <button aria-label="el.dialog.close" class="el-dialog__headerbtn" type="button">
        <i class="el-icon el-dialog__close"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
          <path fill="currentColor"
                d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z">
          </path>
        </svg></i>
      </button>
    </template>
    <el-form ref="knowledgeSegmentRef" :model="form"  label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="分段内容文本">
                          <div v-html="form.content" ></div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="答案内容(如果有)" prop="answer">
                          <div>
                            {{ form.answer }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-form-item label="关键词" prop="keywords">
                          <div>
                            {{ form.keywords }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                          <div>
                            {{ form.remark }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">关 闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
  import { listKnowledgeSegment, getKnowledgeSegment, delKnowledgeSegment, addKnowledgeSegment, updateKnowledgeSegment } from "@/api/kmc/knowledgeSegment/knowledgeSegment";

  const { proxy } = getCurrentInstance();
      const { sync_status, segment_type } = proxy.useDict('sync_status', 'segment_type');


  const knowledgeSegmentList = ref([]);

  // 列显隐信息
  const columns = ref([
        { key: 0, label: "ID", visible: true },
        { key: 1, label: "工作空间id", visible: true },
        { key: 2, label: "文件名称", visible: true },
        { key: 3, label: "文件id", visible: true },
        { key: 4, label: "dify段落id", visible: true },
        { key: 5, label: "位置", visible: true },
        { key: 6, label: "dify所属文档ID", visible: true },
        { key: 7, label: "分段内容文本", visible: true },
        { key: 8, label: "签名内容文本", visible: true },
        { key: 9, label: "答案内容(如果有)", visible: true },
        { key: 10, label: "内容长度", visible: true },
        { key: 11, label: "token数量", visible: true },
        { key: 12, label: "关键词", visible: true },
        { key: 13, label: "索引节点ID", visible: true },
        { key: 14, label: "索引节点哈希值", visible: true },
        { key: 15, label: "访问次数", visible: true },
        { key: 16, label: "启用状态", visible: true },
        { key: 17, label: "状态", visible: true },
        { key: 18, label: "完成时间戳", visible: true },
        { key: 19, label: "错误信息", visible: true },
        { key: 20, label: "子模块", visible: true },
        { key: 21, label: "分段添加dify状态", visible: true },
        { key: 22, label: "分段类型", visible: true },
        { key: 23, label: "父级id", visible: true },
        { key: 24, label: "是否有效", visible: true },
        { key: 25, label: "删除标志", visible: true },
        { key: 26, label: "创建人", visible: true },
        { key: 27, label: "创建人id", visible: true },
        { key: 28, label: "创建时间", visible: true },
        { key: 29, label: "更新人", visible: true },
        { key: 30, label: "更新人id", visible: true },
        { key: 31, label: "更新时间", visible: true },
        { key: 32, label: "备注", visible: true }
  ]);

  const open = ref(false);
  const openDetail = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  const title = ref("");
  const defaultSort = ref({ prop: "createTime", order: "desc" });

  const data = reactive({
          knowledgeSegmentDetail: {
    },
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    content: null,
                    syncStatus: null,
    },
    rules: {
                    workspaceId: [{ required: true, message: "工作空间id不能为空", trigger: "blur" }],
                    documentName: [{ required: true, message: "文件名称不能为空", trigger: "blur" }],
                    documentId: [{ required: true, message: "文件id不能为空", trigger: "blur" }],
                    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
                    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
                    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
                    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    }
  });

  const { queryParams, form, knowledgeSegmentDetail, rules } = toRefs(data);

  /** 查询文件分段列表 */
  function getList() {
    loading.value = true;
    listKnowledgeSegment(queryParams.value).then(response => {
            knowledgeSegmentList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  }

  // 取消按钮
  function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
  }

  // 表单重置
  function reset() {
    form.value = {
                    id: null,
                    workspaceId: null,
                    documentName: null,
                    documentId: null,
                    qmSegmentId: null,
                    position: null,
                    qmDocumentId: null,
                    content: null,
                    signContent: null,
                    answer: null,
                    wordCount: null,
                    tokens: null,
                    keywords: null,
                    indexNodeId: null,
                    indexNodeHash: null,
                    hitCount: null,
                    enabled: null,
                    status: null,
                    completedAt: null,
                    error: null,
                    childChunks: null,
                    syncStatus: null,
                    segmentType: null,
                    parentId: null,
                    validFlag: null,
                    delFlag: null,
                    createBy: null,
                    creatorId: null,
                    createTime: null,
                    updateBy: null,
                    updatorId: null,
                    updateTime: null,
                    remark: null
    };
    proxy.resetForm("knowledgeSegmentRef");
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
  }

  // 多选框选中数据
  function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }


  /** 排序触发事件 */
  function handleSortChange(column, prop, order) {
    queryParams.value.orderByColumn = column.prop;
    queryParams.value.isAsc = column.order;
    getList();
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加文件分段";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getKnowledgeSegment(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改文件分段";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getKnowledgeSegment(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "文件分段详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["knowledgeSegmentRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateKnowledgeSegment(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addKnowledgeSegment(form.value).then(response => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        }
      }
    });
  }

  /** 删除按钮操作 */
  function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal.confirm('是否确认删除文件分段编号为"' + _ids + '"的数据项？').then(function() {
      return delKnowledgeSegment(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('kmc/knowledgeSegment/export', {
      ...queryParams.value
    }, `knowledgeSegment_${new Date().getTime()}.xlsx`)
  }



  getList();

</script>
