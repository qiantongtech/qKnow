![banner.png](.gitee/banner.png)
<p align="center">
 <img src="https://img.shields.io/badge/JDK-1.8+-brightgreen.svg" alt="">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.5.15-blue.svg" alt="">
 <img src="https://img.shields.io/badge/Vue-3.4.31-blue.svg" alt="">
 <img src="https://img.shields.io/badge/license-Apache--2.0-green" alt=""/>
 <img src="https://img.shields.io/badge/qKnow-v1.0.0-blue.svg" alt=""/>
 <img src="https://gitee.com/qiantongtech/qKnow/badge/star.svg" alt=""/>
 <img src="https://img.shields.io/github/stars/qiantongtech/qKnow?label=Github%20Stars" alt=""/>
</p>

<p align="center">
  📖简体中文 | <a href="README.en.md">📖English</a>
</p>


## 🌈平台简介
千知平台（qKnow） 是一款以**知识图谱**为核心的开源企业级知识平台，聚焦知识图谱的构建与应用，集成**知识抽取**、**知识融合**、**知识推理**等关键能力，支持从**结构化**与**非结构化**数据中高效获取和整合知识，帮助企业构建语义明确、动态演化的知识图谱体系。

平台为**智能问答**、**语义检索**和**智能体构建**提供坚实基础，是企业迈向智能化知识管理与AI融合应用的重要支撑平台。

✨✨✨**在线文档**✨✨✨ <a href="https://qknow.qiantong.tech" target="_blank">https://qknow.qiantong.tech</a>

✨✨✨**开源版演示地址**✨✨✨ <a href="https://qknow-demo.qiantong.tech" target="_blank">点击跳转</a> ，账号：qKnow 密码：qKnow123

✨✨✨**商业版演示地址**✨✨✨ <a href="https://qknow-pro.qiantong.tech" target="_blank">点击跳转</a> ，演示账号请联系客服获取

## 🍱 使用场景
### 1、智慧水利场景

> 🌊 **实时监测与智能决策**

#### 场景描述
在水资源管理和防洪抗旱工作中，利用知识图谱整合来自多个传感器的数据（如水位、水质、气象信息），并通过大模型进行数据分析与预测，帮助决策者快速做出响应。

##### 实现方式
- **知识图谱**：构建涵盖河流、水库、泵站及其相互关系的完整网络。
- **大模型应用**：使用深度学习模型预测洪水风险，优化水资源调度策略。

##### 效果
- 提高灾害预警准确性，减少损失。
- 实现水资源高效利用，保障供水安全。

---

### 2、企业内部管理

> 🏢 **提升效率与促进协作**

#### 场景描述
大型企业拥有海量的内部文档和资料，员工查找特定信息往往耗时费力。通过建立企业级知识图谱，可以有效地组织这些资源，并允许员工通过自然语言查询获取所需信息。

##### 实现方式
- **知识图谱**：对企业内所有文档、项目报告、会议纪要等进行分类整理。
- **大模型应用**：开发定制化的搜索工具，能够理解用户的查询意图并返回相关度最高的结果。

##### 效果
- 提高工作效率，促进知识共享。
- 激发创新思维，增强团队协作能力。

---

### 3、智能制造与供应链优化

> 🛠️ **精益生产与敏捷供应链**

#### 场景描述
制造企业在生产过程中积累了大量设备运行数据和供应链信息。利用知识图谱可以将这些分散的数据点连接起来，形成完整的生产流程视图；而大模型则可用于预测维护需求或优化库存水平。

##### 实现方式
- **知识图谱**：描绘出生产设备、原材料供应商、成品流向之间的复杂关系。
- **大模型应用**：运用机器学习算法预测设备故障时间，或者根据市场需求动态调整库存策略。

##### 效果
- 降低成本，提高生产效率。
- 增强供应链韧性，提升整体运营水平。

---

### 4、智能客服与问答系统

> 💬 **即时响应与精准服务**

#### 场景描述
在一个电商平台上，客户经常询问关于产品规格、退货政策等问题。通过集成知识图谱与大模型技术，平台能够自动解析客户的自然语言问题，并从知识库中提取准确答案，提供即时响应。

##### 实现方式
- **知识图谱**：存储产品信息、用户反馈、退货流程等结构化数据。
- **大模型应用**：利用NLP模型理解客户提问意图，结合图谱进行精确匹配和推理，给出最合适的回答。

##### 效果
- 提升客户服务效率，减少人工客服工作量。
- 提高客户满意度和服务质量。

## 🚀 优势
- 知识图谱为骨，大模型为脑。
- 企业级架构，轻量级上手。
- 功能模块自由组合，扩展性强如拼乐高。
- 初生即开源，成长由社区共塑。
- 技术有温度，知识有脉络。

## ✨ 核心功能
| 功能模块   | 描述                                     | 状态     |
|--------|----------------------------------------|--------|
| 知识中心   | 提供文件分类与文件管理功能，帮助用户快速准确地找到所需资源。         | ✅ 已完成  |
| 概念配置   | 自定义知识抽取概念规则，提升信息提取的灵活性与准确性。            | ✅ 已完成  |
| 关系配置   | 灵活配置知识间关系抽取规则，强化知识关联建模能力。              | ✅ 已完成  |
| 非结构化抽取 | 基于DeepKE工具提取非结构化数据中的关键知识，提升信息利用率。      | ✅ 已完成  |
| 结构化抽取  | 从结构化数据源中系统化抽取、转换并加载结构化知识元素。            | ✅ 已完成  |
| 图谱探索   | 可视化、交互式知识图谱浏览与分析界面，助力深度洞察复杂关系网络。       | ✅ 已完成  |
| 系统管理   | 用户、角色、部门、菜单及日志等核心系统信息的统一管理和权限控制。       | ✅ 已完成  |
| 知识融合   | 融合多源异构知识，通过策略优化提升知识的一致性、准确性和可用性。       | ❌ 未来计划 |
| 知识推理   | 利用逻辑推理技术挖掘知识间的潜在关联，增强系统的语义理解和智能决策能力。   | ❌ 未来计划 |
| 知识问答   | 构建基于大语言模型（LLM）与知识图谱的智能问答系统，支持自然语言交互查询。 | ❌ 未来计划 |
| 知识检索   | 融合全文检索与语义理解技术，实现对知识库内容的高效精准检索。         | ❌ 未来计划 |
| 服务管理   | 服务注册、监控、维护与优化于一体的后台服务治理解决方案。           | ❌ 未来计划 |
| 服务日志   | 全面记录服务调用过程，支撑故障追踪与性能分析。                | ❌ 未来计划 |

## 🧩 架构图

![framework.png](.gitee/framework.jpg)

## 🛠️ 技术栈
千知平台（qKnow）采用前后端分离架构，后端基于 Spring Boot，前端基于 Vue 3，整合常用中间件与数据工具，构建一站式数据中台解决方案。

<table>
  <tr>
    <th>技术栈</th><th>技术框架</th><th>描述</th>
  </tr>
  <tr>
    <td rowspan="6">后端技术栈</td><td>Spring Boot</td><td>主体框架，简化配置与开发</td>
  </tr>
  <tr>
    <td>MyBatis-Plus</td><td>ORM 框架，简化数据库操作</td>
  </tr>
  <tr>
    <td>Spring Framework</td><td>基础架构支持，包括依赖注入、面向切面编程等功能</td>
  </tr>
  <tr>
    <td>Quartz</td><td>定时任务调度</td>
  </tr>
  <tr>
    <td>Spring Security</td><td>安全框架，提供认证、授权等安全功能</td>
  </tr>
  <tr>
    <td>Alibaba Druid</td><td>数据库连接池，优化数据库访问性能</td>
  </tr>


  <tr>
    <td rowspan="8">前端技术栈</td><td>Vue 3</td><td>渐进式前端框架</td>
  </tr>
  <tr>
    <td>Vite</td><td>快速构建工具，替代 Vue CLI</td>
  </tr>
  <tr>
    <td>Element Plus</td><td>UI 组件库</td>
  </tr>
  <tr>
    <td>Axios</td><td>HTTP 请求库</td>
  </tr>
  <tr>
    <td>Pinia</td><td>状态管理（替代 Vuex）</td>
  </tr>
  <tr>
    <td>Vue Router</td><td>前端路由控制</td>
  </tr>
  <tr>
    <td>Vis</td><td>知识图谱展示，创建动态、交互式的可视化图表和网络图</td>
  </tr>
  <tr>
    <td>Echarts</td><td>数据可视化库，支持多种类型的图表</td>
  </tr>
  

  <tr>
    <td rowspan="6">第三方依赖</td><td>DeepKE</td><td>知识抽取工具，利用深度学习技术从文本中提取实体关系</td>
  </tr>
  <tr>
    <td>MySQL</td><td>核心关系型数据库</td>
  </tr>
  <tr>
    <td>Neo4j</td><td>图数据库</td>
  </tr>
  <tr>
    <td>Redis</td><td>数据缓存与高性能数据读取</td>
  </tr>
  <tr>
    <td>Swagger</td><td>接口文档生成工具</td>
  </tr>
  <tr>
    <td>Docker（可选）</td><td>容器化部署支持</td>
  </tr>
</table>

## 🏗️ 部署要求
在部署 qKnow 之前，请确保以下环境和工具已正确安装：

<table>
  <tr>
    <th>环境</th><th>项目</th><th>推荐版本</th><th>说明</th>
  </tr>
  <tr>
    <td rowspan="6">后端</td><td>JDK</td><td>1.8 或以上</td><td>建议使用 OpenJDK 8 或 11</td>
  </tr>
  <tr>
    <td>Maven</td><td>3.6+</td><td>项目构建与依赖管理</td>
  </tr>
  <tr>
    <td>MySQL</td><td>5.7 或 8.0</td><td>关系型数据库</td>
  </tr>
  <tr>
    <td>Neo4j</td><td>4.4.40</td><td>图数据库</td>
  </tr>
  <tr>
    <td>Redis</td><td>5.0+</td><td>缓存与消息功能支持</td>
  </tr>
  <tr>
    <td>操作系统</td><td>Windows / Linux / Mac</td><td>通用环境均可运行</td>
  </tr>

  <tr>
    <td rowspan="3">前端</td><td>Node.js</td><td>16+</td><td>构建工具依赖</td>
  </tr>
  <tr>
    <td>npm / pnpm / yarn</td><td>任一即可</td><td>包管理器</td>
  </tr>
  <tr>
    <td>Vue CLI / Vite</td><td>最新版</td><td>脚手架工具</td>
  </tr>
</table>

## 🚨 商用授权

👉 如需在商业场景中使用千知平台，请点击下方按钮查看商用授权详情：

[💼 了解授权详情](https://qknow.qiantong.tech/business.html)

## 🚀 快速开始
👉 点击下方按钮，查看完整的安装与部署指南：

[🧭 查看快速开始文档](QUICKSTART.md)

## 👥 QQ交流群
欢迎加入 千知平台（qKnow） 官方 QQ 交流群，获取最新动态、技术支持与使用交流。

[![加入QQ群](https://img.shields.io/badge/QQ群-277328475-blue.svg)](https://qm.qq.com/q/pr4xkk3dXq)

## 🖼️ 系统配图
<table>
    <tr>
        <td><img alt="" src=".gitee/system/login.png"/></td>
        <td><img alt="" src=".gitee/system/workbench.png"/></td>
    </tr>
    <tr>
        <td><img alt="" src=".gitee/system/kmcCategory.png"/></td>
        <td><img alt="" src=".gitee/system/kmcDocument.png"/></td>
    </tr>
    <tr>
        <td><img alt="" src=".gitee/system/extSchema.png"/></td>
        <td><img alt="" src=".gitee/system/extRelation.png"/></td>
    </tr>
    <tr>
        <td><img alt="" src=".gitee/system/extTask.png"/></td>
        <td><img alt="" src=".gitee/system/extResult.png"/></td>
    </tr>
    <tr>
        <td><img alt="" src=".gitee/system/extSetting.png"/></td>
        <td><img alt="" src=".gitee/system/graph.png"/></td>
    </tr>
</table>
