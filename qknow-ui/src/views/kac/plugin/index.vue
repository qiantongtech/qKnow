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
        <div class="pagecont-top" v-show="showSearch">
            <el-form
                class="btn-style"
                :model="queryParams"
                ref="queryRef"
                :inline="true"
                v-show="showSearch"
                @submit.prevent
            >
                <el-form-item label="名称" prop="name">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.name"
                        placeholder="请输入名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button
                        plain
                        type="primary"
                        @click="handleQuery"
                        @mousedown="(e) => e.preventDefault()"
                    >
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                    </el-button>
                    <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                    </el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            @click="handleAdd"
                            v-hasPermi="['kac:plugin:plugin:add']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="danger"
                            plain
                            :disabled="multiple"
                            @click="handleDelete"
                            v-hasPermi="['kac:plugin:plugin:remove']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <div class="search-desc">
                            <svg-icon iconClass="remind" class="desc-icon" />
                            上传插件后，插件自启动，系统会短暂加载，期间无法正常使用，请谨慎操作！
                        </div>
                    </el-col>
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar
                        v-model:showSearch="showSearch"
                        @queryTable="getList"
                        :columns="columns"
                    ></right-toolbar>
                </div>
            </div>
            <el-table
                stripe
                height="65vh"
                v-loading="loading"
                :data="pluginList"
                @selection-change="handleSelectionChange"
                :default-sort="defaultSort"
                @sort-change="handleSortChange"
            >
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column
                    v-if="getColumnVisibility(1)"
                    label="编号"
                    align="center"
                    width="80"
                    prop="id"
                    sortable="custom"
                >
                    <template #default="scope">
                        {{ scope.row.id || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(2)"
                    label="名称"
                    align="left"
                    prop="name"
                    width="200px"
                    :show-overflow-tooltip="{ effect: 'light' }"
                >
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(3)"
                    label="描述"
                    align="left"
                    prop="description"
                    width="280px"
                    :show-overflow-tooltip="{ effect: 'light' }"
                >
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(2)"
                    label="作者"
                    align="left"
                    prop="name"
                    width="60px"
                    :show-overflow-tooltip="{ effect: 'light' }"
                >
                    <template #default="scope"> 小桐 </template>
                </el-table-column>
                <el-table-column label="文件名称" align="center" prop="path" width="250">
                    <template #default="scope">
                        {{ scope.row.path.replaceAll('/', '') || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="文件大小" align="center" prop="fileSize" width="100">
                    <template #default="scope"> {{ getFileSize(scope.row) }} </template>
                </el-table-column>
                <el-table-column label="图标" align="center" prop="coverImage" width="80">
                    <template #default="scope">
                        <img :src="getImage(scope.row)" alt="应用图标" :width="50" :height="50" />
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(4)"
                    label="状态"
                    align="center"
                    prop="status"
                    width="80"
                >
                    <template #default="scope">
                        <el-switch
                            v-model="scope.row.status"
                            :active-value="1"
                            :inactive-value="0"
                            @change="handleStatusChange(scope.row)"
                        ></el-switch>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(5)"
                    label="创建人"
                    align="center"
                    prop="createBy"
                >
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(6)"
                    label="创建时间"
                    align="center"
                    prop="createTime"
                    sortable="custom"
                    width="180"
                    :sort-orders="['descending', 'ascending']"
                >
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}')
                        }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                    class-name="small-padding fixed-width"
                    fixed="right"
                    width="`40"
                >
                    <template #default="scope">
                        <!--            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
                        <!--                       v-hasPermi="['kac:plugin:plugin:edit']">修改</el-button>-->
                        <el-button
                            link
                            type="danger"
                            icon="Delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['kac:plugin:plugin:remove']"
                            >删除</el-button
                        >
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
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
            />
        </div>

        <!-- 添加或修改插件管理对话框 -->
        <el-dialog
            :title="title"
            v-model="open"
            width="800px"
            :append-to="$refs['app-container']"
            draggable
        >
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form
                ref="pluginRef"
                :model="form"
                :rules="rules"
                @submit.prevent
                label-width="87px"
            >
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="插件路径" prop="path">
                            <FileUpload
                                v-model="form.path"
                                :fileName="form.fileName"
                                :fileSize="10"
                                :limit="1"
                                :fileType="['jar']"
                                :platForm="platForm"
                                @update:fileName="updateFormFileName"
                            ></FileUpload>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="图标" prop="coverImage" class="image-form-item">
                            <image-upload
                                v-model="form.coverImage"
                                :limit="1"
                                :fileSize="300"
                                :isShowTip="true"
                                :platForm="platImageForm"
                            />
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

        <!-- 用户导入对话框 -->
        <el-dialog
            :title="upload.title"
            v-model="upload.open"
            width="800px"
            :append-to="$refs['app-container']"
            draggable
            destroy-on-close
        >
            <el-upload
                ref="uploadRef"
                :limit="1"
                accept=".xlsx, .xls"
                :headers="upload.headers"
                :action="upload.url + '?updateSupport=' + upload.updateSupport"
                :disabled="upload.isUploading"
                :on-progress="handleFileUploadProgress"
                :on-success="handleFileSuccess"
                :auto-upload="false"
                drag
            >
                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                <div class="el-upload__tip">将文件拖到此处，或<em>点击上传</em></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <div class="el-upload__tip">
                            <el-checkbox
                                v-model="upload.updateSupport"
                            />是否更新已经存在的插件管理数据
                        </div>
                        <span>仅允许导入xls、xlsx格式文件。</span>
                        <el-link
                            type="primary"
                            :underline="false"
                            style="font-size: 12px; vertical-align: baseline"
                            @click="importTemplate"
                            >下载模板</el-link
                        >
                    </div>
                </template>
            </el-upload>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="upload.open = false">取 消</el-button>
                    <el-button type="primary" @click="submitFileForm">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="Plugin">
    // import {
    //   listPlugin,
    //   getPlugin,
    //   delPlugin,
    //   addPlugin,
    //   updatePlugin,
    // } from "@/api/kac/plugin/plugin";
    import { getToken } from '@/utils/auth.js';
    import FileUpload from '@/components/FileUploadPlugin/index.vue';
    import GraphCover from '@/assets/kac/wzbx.png';
    import { WarningFilled } from '@element-plus/icons-vue'; // 修正导入

    const { proxy } = getCurrentInstance();
    const platForm = ref('aliyun-oss-qt');
    const platImageForm = ref('local');

    function createIcon({ bgStart, bgEnd, accent, symbol }) {
        const symbols = {
            wrench: `
      <path d="M42 18a13 13 0 0 0 15 18L36 57a8 8 0 0 1-11-11l21-21a13 13 0 0 0-4-7Z" fill="white" opacity=".96"/>
      <path d="M25 51l7 7" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
    `,
            health: `
      <rect x="18" y="24" width="44" height="32" rx="8" fill="white" opacity=".95"/>
      <path d="M40 31v18M31 40h18" stroke="${accent}" stroke-width="5" stroke-linecap="round"/>
      <path d="M22 20c8-9 28-9 36 0" stroke="white" stroke-width="5" stroke-linecap="round" opacity=".75"/>
    `,
            rain: `
      <path d="M25 35a12 12 0 0 1 23-5 10 10 0 1 1 4 19H25a7 7 0 0 1 0-14Z" fill="white" opacity=".95"/>
      <path d="M27 58l-3 6M39 58l-3 6M51 58l-3 6" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
    `,
            checklist: `
      <rect x="18" y="17" width="44" height="46" rx="8" fill="white" opacity=".95"/>
      <path d="m27 31 4 4 8-9M27 47l4 4 8-9" stroke="${accent}" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M45 33h8M45 49h8" stroke="${accent}" stroke-width="4" stroke-linecap="round" opacity=".75"/>
    `,
            medicine: `
      <rect x="18" y="34" width="44" height="18" rx="9" fill="white" opacity=".95"/>
      <path d="M40 34v18" stroke="${accent}" stroke-width="4"/>
      <path d="M40 17v13M33 24h14" stroke="white" stroke-width="5" stroke-linecap="round"/>
    `,
            folder: `
      <path d="M16 25a7 7 0 0 1 7-7h12l6 7h16a7 7 0 0 1 7 7v24a7 7 0 0 1-7 7H23a7 7 0 0 1-7-7V25Z" fill="white" opacity=".95"/>
      <path d="M28 42h24M28 52h17" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
    `,
            water: `
      <path d="M40 15c10 13 18 24 18 34a18 18 0 1 1-36 0c0-10 8-21 18-34Z" fill="white" opacity=".95"/>
      <path d="M31 50c5 5 13 5 18 0" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
    `,
            image: `
      <rect x="17" y="19" width="46" height="42" rx="8" fill="white" opacity=".95"/>
      <circle cx="30" cy="32" r="5" fill="${accent}"/>
      <path d="m22 55 13-14 8 8 7-8 10 14" fill="${accent}" opacity=".82"/>
    `
        };
        const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 80 80">
      <defs>
        <linearGradient id="bg" x1="12" y1="10" x2="68" y2="70" gradientUnits="userSpaceOnUse">
          <stop stop-color="${bgStart}"/>
          <stop offset="1" stop-color="${bgEnd}"/>
        </linearGradient>
      </defs>
      <rect x="0" y="0" width="80" height="80" rx="16" fill="url(#bg)"/>
      <circle cx="66" cy="14" r="9" fill="white" opacity=".24"/>
      <circle cx="15" cy="66" r="12" fill="white" opacity=".14"/>
      ${symbols[symbol]}
    </svg>
  `;
        return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
    }

    const mockPluginList = ref([
        {
            id: 4,
            workspaceId: 1001,
            pluginKey: 'writing-plugin',
            name: '文章编写',
            description:
                '文章编写插件是一类旨在辅助用户更高效、更高质量地完成文本创作任务的软件工具或扩展程序。',
            path: '/69ea0859e4b077552f280c95.jar',
            category: '0',
            fileName: 'qknow-writing-2.7.1.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01a88de4b0d389f4f52e8e.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-23 19:54:04',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-23 19:54:04',
            remark: null
        },
        {
            id: 5,
            workspaceId: 1001,
            pluginKey: 'batch-search-plugin',
            name: '批量检索',
            description:
                '支持一次性上传多个查询条件并行处理，汇总输出结果，大幅提升效率，适用于多项目数据对比或大规模文献调研场景。',
            path: '/8f2c4a9b7d6e1f305c92b8a4.jar',
            category: '0',
            fileName: 'qknow-batch-search-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01a9a0e4b0d389f4f52e90.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-24 09:18:32',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-24 09:18:32',
            remark: null
        },
        {
            id: 6,
            workspaceId: 1001,
            pluginKey: 'accurate-search-plugin',
            name: '精确检索',
            description:
                '严格字符匹配，精准查找代码、条款或参数，无模糊干扰，适合高准确度定位与核验。',
            path: '/4b71d9e6c8a20f35d19a7c62.jar',
            category: '0',
            fileName: 'qknow-accurate-search-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01a9d8e4b0d389f4f52e91.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-24 10:06:15',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-24 10:06:15',
            remark: null
        },
        {
            id: 7,
            workspaceId: 1001,
            pluginKey: 'entity-relation-search-plugin',
            name: '实体关系检索',
            description: '智能识别实体与深层关系，辅助知识图谱与情报分析，快速发现关键关联线索。',
            path: '/a06d3b8e9f41275c60d9a134.jar',
            category: '0',
            fileName: 'qknow-entity-relation-search-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01a9e9e4b0d389f4f52e92.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-24 11:22:48',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-24 11:22:48',
            remark: null
        },
        {
            id: 8,
            workspaceId: 1001,
            pluginKey: 'semantic-search-plugin',
            name: '语义检索',
            description:
                '利用深度学习理解查询意图与上下文，突破关键词匹配限制，即使词汇不完全一致，也能通过语义关联精准定位内容。',
            path: '/c52a91f4e8b7036d2a4f9c18.jar',
            category: '0',
            fileName: 'qknow-semantic-search-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01a9f9e4b0d389f4f52e93.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-24 14:35:20',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-24 14:35:20',
            remark: null
        },
        {
            id: 9,
            workspaceId: 1001,
            pluginKey: 'knowledge-qa-plugin',
            name: '知识问答',
            description:
                '基于海量数据理解并回答各类事实性或解释性问题，提供准确简洁的答案，满足即时信息获取需求。',
            path: '/d9e14c73a5f826b0c48a2f91.jar',
            category: '0',
            fileName: 'qknow-knowledge-qa-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01aa0ae4b0d389f4f52e94.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-24 15:12:09',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-24 15:12:09',
            remark: null
        },
        {
            id: 10,
            workspaceId: 1001,
            pluginKey: 'report-template-plugin',
            name: '模板报告生成',
            description: '提供多场景标准模板，引导填充并自动排版，确保企业级文档专业规范。',
            path: '/e63f8a10c4b72d59a91e5f03.jar',
            category: '0',
            fileName: 'qknow-report-template-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01aa17e4b0d389f4f52e95.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-24 16:28:44',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-24 16:28:44',
            remark: null
        },
        {
            id: 11,
            workspaceId: 1001,
            pluginKey: 'periodic-article-plugin',
            name: '日报/周报/月报文章编写',
            description:
                '简化周期性工作汇报撰写，输入关键事项后自动扩展为结构完整、语气专业的报告，并智能识别成果与计划。',
            path: '/f47b2c90d6a15e83c29a7b04.jar',
            category: '0',
            fileName: 'qknow-periodic-article-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: '/2026/05/11/6a01a8f6e4b0d389f4f52e8f.png',
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-04-24 17:40:16',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-04-24 17:40:16',
            remark: null
        },
        {
            id: 12,
            workspaceId: 1001,
            pluginKey: 'fault-maintenance-guide-plugin',
            name: '故障快速排查与维修指导',
            description:
                '面向设备运维场景，结合故障现象、报警代码和历史维修记录，快速定位可能原因并生成维修步骤建议。',
            path: '/b15e8d4a7c9032f6a1d9e240.jar',
            category: '1',
            fileName: 'qknow-fault-maintenance-guide-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#ff9f43',
                bgEnd: '#f45b69',
                accent: '#f97316',
                symbol: 'wrench'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        },
        {
            id: 13,
            workspaceId: 1001,
            pluginKey: 'device-health-report-plugin',
            name: '设备健康诊断与报告编写',
            description:
                '汇总巡检、运行、告警和检修数据，自动评估设备健康状态，输出结构化诊断结论和专业分析报告。',
            path: '/c20a9f5e1d8746b3a8f2d091.jar',
            category: '1',
            fileName: 'qknow-device-health-report-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#35d0ba',
                bgEnd: '#0ea5e9',
                accent: '#0ea5e9',
                symbol: 'health'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        },
        {
            id: 14,
            workspaceId: 1001,
            pluginKey: 'rainfall-brief-report-plugin',
            name: '水雨情数据统计与简报编写',
            description:
                '自动统计降雨、水位、流量等水雨情数据，生成趋势分析、异常提示和可直接发布的业务简报。',
            path: '/d34b6e8f9012a75c4e0b9f13.jar',
            category: '1',
            fileName: 'qknow-rainfall-brief-report-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#60a5fa',
                bgEnd: '#2563eb',
                accent: '#2563eb',
                symbol: 'rain'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        },
        {
            id: 15,
            workspaceId: 1001,
            pluginKey: 'inspection-ledger-plugin',
            name: '巡查规范答疑与隐患台账生成',
            description:
                '基于巡查规范和隐患分类标准，解答现场检查问题，并将发现项自动整理为隐患台账。',
            path: '/e48c7a1d0f9352b6c4a8d017.jar',
            category: '1',
            fileName: 'qknow-inspection-ledger-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#a78bfa',
                bgEnd: '#7c3aed',
                accent: '#7c3aed',
                symbol: 'checklist'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        },
        {
            id: 16,
            workspaceId: 1001,
            pluginKey: 'medicine-consult-plugin',
            name: '智能用药咨询',
            description:
                '围绕药品适应症、用法用量、禁忌和相互作用提供智能咨询，辅助形成安全、规范的用药建议。',
            path: '/f59d2b8a6e1047c3b0a9d524.jar',
            category: '1',
            fileName: 'qknow-medicine-consult-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#34d399',
                bgEnd: '#059669',
                accent: '#059669',
                symbol: 'medicine'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        },
        {
            id: 17,
            workspaceId: 1001,
            pluginKey: 'chronic-health-followup-plugin',
            name: '慢病健康档案与随访报告编写',
            description:
                '整合慢病患者基础信息、检查结果和随访记录，自动生成健康档案摘要与随访报告。',
            path: '/a62e9c5b7d0134f8b2c6e905.jar',
            category: '1',
            fileName: 'qknow-chronic-health-followup-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#fbbf24',
                bgEnd: '#f97316',
                accent: '#f97316',
                symbol: 'folder'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        },
        {
            id: 18,
            workspaceId: 1001,
            pluginKey: 'water-resource-permit-qa-plugin',
            name: '水资源调度与取水许可问答',
            description:
                '围绕水资源调度规则、取水许可流程和政策条款提供问答服务，辅助业务人员快速研判办理要求。',
            path: '/b73f0d6a9c8125e4f1a8b390.jar',
            category: '1',
            fileName: 'qknow-water-resource-permit-qa-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#22d3ee',
                bgEnd: '#0284c7',
                accent: '#0284c7',
                symbol: 'water'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        },
        {
            id: 19,
            workspaceId: 1001,
            pluginKey: 'medical-image-report-plugin',
            name: '影像报告解读',
            description:
                '结合医学影像报告文本，提取关键检查结论、异常描述和随访建议，生成通俗易懂的解读内容。',
            path: '/c84a1e7f0b9236d5a9e2f418.jar',
            category: '1',
            fileName: 'qknow-medical-image-report-1.0.0.jar',
            version: 'v1.0.0',
            status: 1,
            coverImage: createIcon({
                bgStart: '#fb7185',
                bgEnd: '#be123c',
                accent: '#e11d48',
                symbol: 'image'
            }),
            validFlag: true,
            delFlag: false,
            createBy: '吴同',
            creatorId: 2,
            createTime: '2026-05-12 09:20:00',
            updateBy: '吴同',
            updaterId: null,
            updateTime: '2026-05-12 09:20:00',
            remark: null
        }
    ]);
    const pluginList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 1, label: '编号', visible: true },
        { key: 2, label: '名称', visible: true },
        { key: 3, label: '描述', visible: true },
        { key: 4, label: '状态', visible: true },
        { key: 5, label: '创建人', visible: true },
        { key: 6, label: '创建时间', visible: true }
    ]);

    const getColumnVisibility = (key) => {
        const column = columns.value.find((col) => col.key === key);
        // 如果没有找到对应列配置，默认显示
        if (!column) return true;
        // 如果找到对应列配置，根据visible属性来控制显示
        return column.visible;
    };

    const mockFileSizeMap = {
        4: '5.6MB',
        5: '7.2MB',
        6: '4.8MB',
        7: '8.5MB',
        8: '6.1MB',
        9: '9.4MB',
        10: '3.9MB',
        11: '6.8MB',
        12: '12.3MB',
        13: '10.7MB',
        14: '5.2MB',
        15: '4.6MB',
        16: '7.9MB',
        17: '11.5MB',
        18: '6.4MB',
        19: '8.1MB'
    };

    function getFileSize(row) {
        return row.fileSize || mockFileSizeMap[row.id] || '-';
    }

    const open = ref(false);
    const openDetail = ref(false);
    const loading = ref(false);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref('');
    const defaultSort = ref({ prop: 'createTime', order: 'desc' });
    const router = useRouter();

    const updateFormFileName = (newValue) => {
        form.value.fileName = newValue;
    };

    /*** 用户导入参数 */
    const upload = reactive({
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: import.meta.env.VITE_APP_BASE_API + '/kac/plugin/importData'
    });

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            pluginKey: null,
            name: null,
            description: null,
            fileName: null,
            path: null,
            version: null,
            status: null,
            createTime: null,
            orderByColumn: null,
            isAsc: null
        },
        rules: {
            name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
            pluginKey: [{ required: true, message: '插件标识不能为空', trigger: 'blur' }],
            version: [{ required: true, message: '版本不能为空', trigger: 'blur' }],
            status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
            path: [{ required: true, message: '插件路径不能为空', trigger: 'blur' }]
            // coverImage: [{ required: true, message: "图标不能为空", trigger: "blur" }],
        }
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询插件管理列表：当前使用前端假数据，后续接接口时再恢复 listPlugin */
    function getList() {
        loading.value = true;
        const keyword = queryParams.value.name?.trim().toLowerCase();
        let list = mockPluginList.value.filter((item) => {
            if (!keyword) return true;
            return [item.name, item.description, item.pluginKey, item.fileName]
                .filter(Boolean)
                .some((value) => String(value).toLowerCase().includes(keyword));
        });

        if (queryParams.value.orderByColumn) {
            const orderByColumn = queryParams.value.orderByColumn;
            const isAsc = queryParams.value.isAsc;
            list = [...list].sort((a, b) => {
                const aValue = a[orderByColumn] ?? '';
                const bValue = b[orderByColumn] ?? '';
                const result = String(aValue).localeCompare(String(bValue), 'zh-CN', {
                    numeric: true
                });
                return isAsc === 'ascending' || isAsc === 'asc' ? result : -result;
            });
        }

        total.value = list.length;
        const pageNum = Number(queryParams.value.pageNum) || 1;
        const pageSize = Number(queryParams.value.pageSize) || 10;
        const start = (pageNum - 1) * pageSize;
        pluginList.value = list.slice(start, start + pageSize);
        loading.value = false;
        // listPlugin(queryParams.value).then((response) => {
        //     pluginList.value = response.data.rows;
        //     total.value = response.data.total;
        //     loading.value = false;
        // });
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
            pluginKey: null,
            name: null,
            description: null,
            fileName: null,
            path: null,
            version: null,
            status: null,
            validFlag: null,
            delFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null
        };
        proxy.resetForm('pluginRef');
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm('queryRef');
        handleQuery();
    }

    // 多选框选中数据
    function handleSelectionChange(selection) {
        ids.value = selection.map((item) => item.id);
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
        title.value = '新增插件';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        const current = mockPluginList.value.find((item) => item.id === _id);
        if (current) {
            form.value = { ...current };
        }
        open.value = true;
        title.value = '修改插件管理';
        // getPlugin(_id).then((response) => {
        //     form.value = response.data;
        //     open.value = true;
        //     title.value = '修改插件管理';
        // });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        const current = mockPluginList.value.find((item) => item.id === _id);
        if (current) {
            form.value = { ...current };
        }
        openDetail.value = true;
        title.value = '插件管理详情';
        // getPlugin(_id).then((response) => {
        //     form.value = response.data;
        //     openDetail.value = true;
        //     title.value = '插件管理详情';
        // });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['pluginRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    const index = mockPluginList.value.findIndex(
                        (item) => item.id === form.value.id
                    );
                    if (index > -1) {
                        mockPluginList.value[index] = {
                            ...mockPluginList.value[index],
                            ...form.value,
                            updateTime: parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}')
                        };
                    }
                    proxy.$modal.msgSuccess('修改成功');
                    open.value = false;
                    getList();
                    // updatePlugin(form.value).then(() => {
                    //     proxy.$modal.msgSuccess('修改成功');
                    //     open.value = false;
                    //     getList();
                    // });
                } else {
                    if (!form.value.path) {
                        proxy.$modal.msgWarning('请先上传 jar 文件');
                        return;
                    }
                    // 默认启用
                    form.value.status = 1;
                    // 默认横向应用
                    form.value.category = 0;
                    mockPluginList.value.unshift({
                        ...form.value,
                        id: Math.max(...mockPluginList.value.map((item) => item.id), 0) + 1,
                        workspaceId: 1001,
                        validFlag: true,
                        delFlag: false,
                        createBy: '吴同',
                        creatorId: 2,
                        createTime: parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}'),
                        updateBy: '吴同',
                        updaterId: null,
                        updateTime: parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}'),
                        remark: null
                    });
                    proxy.$modal.msgSuccess('新增成功');
                    open.value = false;
                    getList();
                    // addPlugin(form.value).then(() => {
                    //     proxy.$modal.msgSuccess('新增成功');
                    //     open.value = false;
                    //     getList();
                    // });
                }
            }
        });
    }

    function getImage(row) {
        if (!row.coverImage) {
            return GraphCover;
        }
        if (/^(data:image|https?:\/\/)/.test(row.coverImage)) {
            return row.coverImage;
        }
        return `${import.meta.env.VITE_APP_BASE_API}/profile${row.coverImage}`;
    }

    /** 状态修改  */
    function handleStatusChange(row) {
        let text = row.status === 0 ? '停用' : '启用';
        proxy.$modal
            .confirm('确认要"' + text + '""' + row.name + '"插件吗?')
            .then(function () {
                const item = mockPluginList.value.find((plugin) => plugin.id === row.id);
                if (item) {
                    item.status = row.status;
                }
                // return updatePlugin(row);
            })
            .then(() => {
                proxy.$modal.msgSuccess(text + '成功');
                handleQuery();
            })
            .catch(function () {
                row.status = row.status === 0 ? 1 : 0;
            });
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const _ids = row.id || ids.value;
        proxy.$modal
            .confirm('是否确认删除插件管理编号为"' + _ids + '"的数据项？')
            .then(function () {
                const deleteIds = Array.isArray(_ids) ? _ids : [_ids];
                mockPluginList.value = mockPluginList.value.filter(
                    (item) => !deleteIds.includes(item.id)
                );
                ids.value = [];
                single.value = true;
                multiple.value = true;
                // return delPlugin(_ids);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    /** 导出按钮操作 */
    function handleExport() {
        proxy.download(
            'kac/plugin/export',
            {
                ...queryParams.value
            },
            `plugin_${new Date().getTime()}.xlsx`
        );
    }

    /** ---------------- 导入相关操作 -----------------**/
    /** 导入按钮操作 */
    function handleImport() {
        upload.title = '插件管理导入';
        upload.open = true;
    }

    /** 下载模板操作 */
    function importTemplate() {
        proxy.download(
            'system/user/importTemplate',
            {},
            `plugin_template_${new Date().getTime()}.xlsx`
        );
    }

    /** 提交上传文件 */
    function submitFileForm() {
        proxy.$refs['uploadRef'].submit();
    }

    /**文件上传中处理 */
    const handleFileUploadProgress = (event, file, fileList) => {
        upload.isUploading = true;
    };

    /** 文件上传成功处理 */
    const handleFileSuccess = (response, file, fileList) => {
        upload.open = false;
        upload.isUploading = false;
        proxy.$refs['uploadRef'].handleRemove(file);
        proxy.$alert(
            "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
                response.msg +
                '</div>',
            '导入结果',
            { dangerouslyUseHTMLString: true }
        );
        getList();
    };

    getList();
</script>

<style scoped lang="scss">
    // 检索方式描述（复用提示文本样式）
    .search-desc {
        display: flex;
        align-items: center;
        font-size: 14px;
        line-height: 22px;
        font-family: Microsoft YaHei-Regular;
        color: #e6a23c;

        .desc-icon {
            margin-right: 3px;
        }
        margin-top: 3px;
    }
</style>
