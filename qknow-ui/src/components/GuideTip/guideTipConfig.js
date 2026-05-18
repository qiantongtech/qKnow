/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
 */

/**
 * guideTipConfig 使用说明
 *
 * guideTipConfig 是一个统一管理页面提示信息的配置对象，用于在前端展示各种提示框。
 * 通过配置不同的提示 ID，可以灵活控制提示内容、样式和交互行为。
 *
 * 配置对象结构：
 *
 export const guideTipConfig = {
 '提示ID': {
 title: '提示框标题，可包含 HTML',       // 提示框标题，显示在头部
 content: '提示内容，可包含 HTML 或自定义标签，不设置则不显示相应内容', // 支持 <span>、<a> 标签及自定义属性（如 data-func、data-link）
 type: '提示类型',         // 提示类型，用于样式区分，可选值：'remind.svg'（提醒）、'warning'（警告）、'danger'（危险），其中'danger'类型的不可设置“不再展示”
 version: '配置版本号'     // 当前配置的版本号，用于管理和更新
 }
 }
 *
 * 字段说明：
 *
 * title      string  提示框标题，显示在头部
 * content    string  提示内容，可包含纯文本或 HTML 标签，如 <span>、<a>
 * type       string  提示类型，用于样式区分，可选值：'remind.svg'、'warning'、'danger'
 * version    string  当前配置版本号，用于管理和更新
 *
 * 示例：
 export const guideTipConfig = {
 'cat/AttQualityCat.list': {
 title: '温馨提示！',
 content: `
 这是数据资产下
 <span class="clickable" data-func="routeTo" data-link="/da/dataQuality/dataQualityTasks">数据质量任务</span>
 的目录，访问
 <a href="http://example.com" target="_blank">帮助中心</a> 了解更多
 `,
 type: 'warning',
 version: '1.0'
 },
 'cat/AttAssetCat.detail': {
 title: '注意事项！',
 content: '数据资产下数据质量任务的目录',
 type: 'remind.svg',
 version: '1.0'
 }
 }
 *
 * 注意事项：
 * 1. 提示 ID 唯一：每个提示的 key 必须唯一。
 * 2. HTML 标签安全：仅使用安全标签（如 <span>、<a>），并绑定相应事件逻辑。
 * 3. 版本管理：更新内容时应增加 version，便于追踪和管理。
 * 4. 样式区分：type 为 'remind.svg' 时为提醒样式，'warning' 时为警告样式。
 * 5. 内容长度：内容过长时可分段或换行，保证显示效果。
 */

// 定义文档基础路径
const DOCS_BASE_URL = 'http://114.66.57.2:8084/docs';


export const guideTipConfig = {
    // 首页
    'index': {
        title: '重要提醒：本系统为演示站点，<u>每日凌晨 02:30 清除所有用户数据</u>，以保障系统整洁与稳定运行。如需体验完整功能，建议申请试用专属测试环境。',
        content: `建议先看视频再试用！40分钟完整演示 qKnow 商业版全流程操作，助您高效理解系统能力。 👉 观看视频 <strong><a href="https://www.bilibili.com/video/BV1ojkmBNEm3/" target="_blank">《qKnow知识平台商业版全流程实操演示(2025年11月版)》</a></strong>`,
        // content: `建议先看视频再试用！60 分钟 qKnow 商业版全流程操作演示，助你高效 get 系统核心能力。实操视频正在精心制作中，上线后将第一时间同步，敬请期待～`,
        type: 'danger', version: '1.0'
    },
    // 知识图谱
    'kg/graph.list': {
        title: '知识图谱-构建智能知识中枢',
        content: "基于高扩展架构实现跨场景、跨系统的知识融合与统一治理，通过可视化界面与开放API，打造可追溯、可管控、可扩展的智能知识中枢；支持图谱的新增与查询。",
        type: 'remind', version: '1.0'
    },
     // 图谱概览
     'kg/graphOverview': {
        title: '图谱概览-全景洞察知识资产底座',
        content: "一站式呈现企业级知识图谱的核心资产与运行状态，聚合图谱规模、结构分布、数据来源、更新趋势等关键雄维度，构建从“掌握全貌”到“驱动决策”的智能知识管理视图中枢。",
        type: 'remind', version: '1.0'
    },
    // 图谱探索
    'kg/graphExploration': {
        title: '图谱探索-多图谱智能知识中枢',
        content: "提供企业级知识图谱的可视化交互与智能分析能力，支持多图谱环境下的动态浏览、精准检索、实时编辑与成果发布，打造从“看见知识”到“用好知识”的一站式智能中枢操作入口。",
        type: 'remind', version: '1.0'
    },
    // 知识中心 - 知识分类
    'kg/knowledge/category.list': {
        title: '知识分类-统一归类与高效管理各类数据',
        content: "支持分类的新增、删除、修改。构建多层级分类体系，实现非结构化内容的归集，提升知识结构化效率与下游应用准确性。",
        type: 'remind', version: '1.0'
    },
    // 知识中心 - 知识文件
    'kg/knowledge/document.list': {
        title: '知识文件-文档智能管理中枢',
        content: "支持知识文件的新增、删除、修改、预览、下载操作。面向企业级知识治理需求，实现非结构化文档的集中化管理与智能化组织，构建标准化、可复用的知识输入中枢。",
        type: 'remind', version: '1.0'
    },
    // 知识抽取 - 非结构化数据抽取
    "kg/ext/unstructTask.list": {
        title: '非结构化抽取-多格式文本转知识中枢',
        content: "基于大模型技术，从文档、文本、扫描件等内容中自动识别实体与关系，精准生成三元组结构知识，支持PDF、Word、TXT等多种格式，实现“文本到知识”的高效转化。",
        type: 'remind', version: '1.0'
    },
    // 知识抽取 - 结构化数据抽取
    "kg/ext/structTask.list": {
        title: '结构化抽取-结构化数据转知识中枢',
        content: "支持对接结构化数据库，通过字段映射与规则配置，将结构化数据自动转化为标准三元组形式，实现系统数据向知识图谱的无缝迁移与融合。",
        type: 'remind', version: '1.0'
    },
    // 知识推理
    "kg/inf/infTask.list": {
        title: '知识推理-AI 驱动知识图谱演进中枢',
        content: "融合大模型语义理解与图结构分析技术，主动挖掘知识图谱中潜在的未知实体与隐含关系，发现业务盲点与关联规律，通过“AI建议 + 人工验证”机制，持续拓展知识边界，实现知识图谱的智能演进与价值延伸。",
        type: 'remind', version: '1.0'
    },
    // 知识融合
    "kg/fus/task.list": {
        title: '知识融合-AI 协同知识去重中枢',
        content: "利用大模型语义理解优势，智能识别多源知识中的重复或近似实体与关系，精准标记疑似冗余项，辅助人工高效判断与确认，实现“AI识别 + 人工把关”的协同治理模式，确保知识图谱的准确性与简洁性。",
        type: 'remind', version: '1.0'
    },
    // 数据管理
    "kg/dm/dmDatasource.list": {
        title: '数据管理-异构数据接入治理中枢',
        content: "支持多种结构化数据系统的无缝接入，提供统一、安全、可配置的数据连接能力，为知识抽取与图谱构建提供稳定、高效的数据输入通道，实现企业异构数据库的集中治理与价值释放。",
        type: 'remind', version: '1.0'
    },
    "kg/extModel.list": {
        title: '图谱模型管理-提炼异构知识，构建企业知识网',
        content: "通过灵活定义知识表达模式，实现三元组（主体-关系-客体）的精准识别与结构化输出，构建企业专属的知识网络，为智能问答、图谱构建等上层应用提供高质量知识供给。",
        type: 'remind', version: '1.0'
    },
    "kg/schema.list": {
        title: '概念配置-高效管理各类概念数据',
        content: "通过展示各类图谱的概念数据，便于用户快速查找和维护",
        type: 'remind', version: '1.0'
    },
    "kg/relation.list": {
        title: '关系配置-高效管理各类关系数据',
        content: "通过展示各类图谱的关系数据，便于用户快速查找和维护",
        type: 'remind', version: '1.0'
    },

    // 知识库
    "kmc/knowledgeBase.list": {
        title: '知识库-多库全生命周期管理中枢',
        content: "支持企业级多知识库并行管理，覆盖构建、编辑、更新、查询全生命周期；通过可视化界面与开放API，打造可追溯、可管控、可扩展的智能知识中枢。",
        type: 'remind', version: '1.0'
    },
    "kmc/kmcCategory.list": {
        title: '知识分类-归类与高效管理各类数据',
        content: "构建多层级分类体系，实现非结构化内容的归集，提升知识结构化效率与下游应用准确性。",
        type: 'remind', version: '1.0'
    },
    "kmc/kmcDocument.list": {
        title: '知识文件-知识资产的集中化、可视化与可治理化管理',
        content: "支持 Word、PDF、TXT、Xlsx、Xls 等主流文档格式的统一纳管，提供在线预览、下载、查看、编辑、删除等全维度操作能力，兼容技术文档多种业务场景，实现知识资产的集中化、可视化与可治理化管理。",
        type: 'remind', version: '1.0'
    },
    "kmc/recall": {
        title: '召回测试-精准评估检索，优化召回准率',
        content: "帮助用户精准评估知识库的检索有效性，通过模拟真实查询场景验证段落召回质量，支持动态调优检索参数，持续提升知识检索的准确率与召回率，确保知识应用“查得全、找得准”。",
        type: 'remind', version: '1.0'
    },
    "kmc/kmcDocument.setting": {
        title: '知识库设置—构建高效可扩展智能知识底座',
        content: "构建高效、精准、可扩展的智能知识底座，支持知识库的参数配置、权限管理与检索优化，并融合自定义 Embedding 模型与混合检索策略。",
        type: 'remind', version: '1.0'
    },

    // 知识应用
    // 资源配置
    "app/chatflow/resources": {
        title: '资源配置-结构化索引，资源智配复用',
        content: "支持多类型非结构化资源的统一管理，为知识问答中的“相关资源推荐”提供高质量输入。通过结构化描述与语义索引，大模型可精准匹配用户问题与关联资源，实现图纸、图像、文档等资产的智能关联与高效复用。",
        type: 'remind', version: '1.0'
    },
    // 知识问答
    "app/chatflow/conversation": {
        title: '知识问答-融合多源知识，按需精准作答',
        content: "深度融合大模型与企业多源知识资产，支持在会话前按需选择一个或多个知识库与知识图谱作为回答依据，实现“按需调用、精准作答”。无论是部门级文档库还是跨系统的专业知识图谱，均可灵活组合，确保每一次问答都基于最相关、最权威的知识背景。",
        type: 'remind', version: '1.0'
    },
    // 知识检索-语义检索
    "app/search/semantic": {
        title: '语义检索-自然语言驱动的智能图谱查询',
        content: "以自然语言输入为核心交互方式，依托大模型的语义理解能力，自动识别用户查询意图，精准匹配知识图谱中的关联实体、关系链路及相关属性信息。同步输出意图解析结果与个性化智能推荐内容，支持检索结果以图谱可视化、结构化列表等形式灵活呈现。",
        type: 'remind', version: '1.0'
    },
    // 知识检索-实体/关系检索
    "app/search/entityRelation": {
        title: '实体关系检索-要素输入驱动的图谱关联查询',
        content: "以实体、关系、标签等核心要素为检索入口，支持模糊匹配目标内容，实现知识图谱中关联实体及关系链路的高效定位。通过要素关联规则的智能解析，深度挖掘图谱中隐藏的实体关联，清晰呈现“实体-关系-实体”的完整链路及属性信息，支持结果以图谱可视化或结构化列表形式展示。",
        type: 'remind', version: '1.0'
    },
    // 知识检索-精确检索
    "app/search/precise": {
        title: '精确检索-字段配置驱动的知识图谱精准查询',
        content: "以数据表字段配置为核心入口，支持自定义字段对应条件与目标内容，实现知识图谱及关联实体信息的定向检索。通过字段匹配规则的灵活设定，精准过滤无效数据，直接定位符合业务需求的图谱节点、实体属性及关联关系，支持检索结果以结构化表格或图谱可视化形式呈现。",
        type: 'remind', version: '1.0'
    },
    // 知识检索-批量检索
    "app/search/batch": {
        title: '批量检索-实体ID驱动的图谱批量查询',
        content: "以实体ID为核心检索标识，支持单批多ID直接输入或批量文件导入两种操作模式，实现知识图谱中批量实体及关联信息的一次性检索。通过高效的ID匹配引擎，快速关联图谱节点，同步返回各实体的完整属性、关联关系及所属图谱结构。",
        type: 'remind', version: '1.0'
    },
    // 知识推荐
    "app/recommend": {
        title: '知识推荐-大模型智能推荐，知识找人',
        content: "基于大模型技术，支持通过自然语言对话方式，智能识别用户意图，主动推荐相关的热门知识、关注知识和最新知识。无需关键词检索，实现“说即所得”的智能推荐体验，让知识主动找人，提升信息触达效率。",
        type: 'remind', version: '1.0'
    },
    // 方案模板管理
    "app/writing/plan.list": {
        title: '方案模板管理-模板化配置，数据化生成',
        content: "支持企业级写作模板的集中配置与动态数据集成。通过上传标准文档模板、绑定业务系统接口、配置智能提示词，实现模板内容的自动化填充与个性化生成，真正达成“一次配置，多次复用，数据驱动”的智能写作闭环。",
        type: 'remind', version: '1.0'
    },
    // 接口管理
    "app/writing/request.list": {
        title: '接口管理-绑定业务 API，实时取数填充',
        content: "可绑定企业内部业务系统（如ERP、CRM、项目管理系统）的API接口，用于在生成时自动获取动态数据（如客户信息、项目进度、预算金额），确保内容实时准确。",
        type: 'remind', version: '1.0'
    },
    // 智能写作
    "app/writing/dataAssets": {
        title: '智能写作-快速产出高质量的内容',
        content: "融合大模型生成能力与企业知识资产，支持从灵感激发到成文输出的全流程辅助，提供通用写作、模板定制、大纲生成、智能润色与多格式导出能力，帮助用户快速产出高质量、合规化、场景化的内容。",
        type: 'remind', version: '1.0'
    },
    // 规则库
    "app/check/rule.list": {
        title: '规则库-统一校验规则，管控内容质量',
        content: "支持企业统一管理语法、术语、风格、合规等多类文本校验规则。通过灵活配置与持续迭代，确保文本检查能力贴合组织规范、行业标准与业务场景，实现内容质量的标准化与自动化管控。",
        type: 'remind', version: '1.0'
    },
    "app/check/ruleCategory.list": {
        title: '规则分类-归类管理各项分类',
        content: "支持对规则进行分类管理（如“语法类”“术语类”“合规类”），便于差异化呈现检查结果。",
        type: 'remind', version: '1.0'
    },
    // AI应用
    "app/agent/workflow": {
        title: 'AI应用-打通内外智能 多端协同调用',
        content: "致力于打通外部智能与内部知识生态的高效连接。它允许用户将自行在 Dify 等低代码 AI 工作流平台构建的智能化流程，无缝接入 qKnow，实现“一次构建，多端调用，动态协同”的智能应用新模式",
        type: 'remind', version: '1.0'
    },
    // AI应用配置
    "app/agent/type.list": {
        title: 'AI应用配置-集中管控 API 密钥 保障跨平台安全',
        content: "集中管理和安全存储与第三方平台对接所需的url、API-Key，支持密钥的分级权限控制、启用/禁用及更新操作，确保智能体调用的安全性与可控性，实现跨平台能力集成的统一治理。",
        type: 'remind', version: '1.0'
    },

    // 模型市场
    'ai/key.list': {
        title: '模型市场-一站式多模态大模型集成中枢',
        content: "聚合主流大语言模型，提供统一接入、参数配置等一站式管理界面。支持按业务场景灵活切换模型，助力用户快速构建高适配、可评估、易迭代的AI应用底座，释放大模型在专业领域知识应用中的最大潜能。",
        type: 'remind', version: '1.0'
    },

}
