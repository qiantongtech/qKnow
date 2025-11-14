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

**qKnow** 是一个以 **知识图谱（Knowledge Graph）** 为核心的开源知识平台，提供知识抽取、知识融合、图谱构建与可视化等能力，帮助企业与组织构建结构化、可推理、可查询的知识体系。
平台基于结构化与非结构化数据自动构建知识图谱，并为智能问答、语义检索与智能体（Agent）提供坚实的数据基础。

✨✨✨**在线文档**✨✨✨ <a href="https://qknow.qiantong.tech" target="_blank">https://qknow.qiantong.tech</a>

✨✨✨**演示地址**✨✨✨ <a href="https://qknow-demo.qiantong.tech" target="_blank">https://qknow-demo.qiantong.tech</a> （账号：`qKnow`，密码：`qKnow123`）

## 🍱 典型应用场景

| 场景             | 说明 |
|------------------|------|
| **多源知识整合**   | 统一管理来自文档、数据库、报告等异构来源的知识数据 |
| **知识标准化治理** | 解决知识冗余、命名不一致等问题，提升数据质量 |
| **跨系统知识联通** | 打通部门或系统间的信息孤岛，实现知识共享 |
| **辅助决策分析**   | 基于**知识图谱**的关系网络，支持业务洞察与策略制定 |
| **数字化知识底座** | 为智能化应用（如语义检索、智能问答）提供结构化知识支撑 |

## 🚀 核心优势

- 以知识图谱为核心，构建可解释、可追溯的知识体系  
- 企业级架构设计，轻量部署，快速上手  
- 模块化设计，功能灵活组合，易于扩展  
- 初生即开源，社区共建，持续演进  
- 技术有温度，知识有脉络

## ✨ 核心功能
| 功能模块   | 描述                                     |
|--------|----------------------------------------|
| 知识中心   | 提供文件分类与文件管理功能，帮助用户快速准确地找到所需资源。         |
| 概念配置   | 自定义知识抽取概念规则，提升信息提取的灵活性与准确性。            |
| 关系配置   | 灵活配置知识间关系抽取规则，强化知识关联建模能力。              |
| 非结构化抽取 | 基于 DeepKE 工具提取非结构化数据中的关键知识，提升信息利用率。      |
| 结构化抽取  | 从结构化数据源中系统化抽取、转换并加载结构化知识元素。            |
| 图谱探索   | 可视化、交互式知识图谱浏览与分析界面，助力深度洞察复杂关系网络。       |
| 系统管理   | 用户、角色、部门、菜单及日志等核心系统信息的统一管理和权限控制。       |

> 注：知识融合、语义问答、智能检索等高级功能正在规划中，欢迎社区贡献。

## 🛠️ 技术栈

qKnow 采用前后端分离架构，后端基于 Spring Boot，前端基于 Vue 3，并整合了部分主流中间件与数据工具。

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

qKnow 提供 **商业版** 与 **开源版** 两种形态，满足不同规模与场景下的用户需求。两者既各具特色，又形成互补：开源版更像启蒙老师，帮助低成本起步；商业版更像专家顾问，提供深度与保障。无论选择哪种版本，qKnow 都将成为可靠的伙伴，帮助企业迈向智能化知识管理与 AI 融合应用。

👉 如需 **开源版品牌定制授权** 或 **咨询商业版**，请点击按钮查看详情：[💼 了解授权详情](https://qknow.qiantong.tech/business.html)

## 🚀 快速开始

| 部署方式                                                                     | 说明                                                                           | 适用场景                   |
|--------------------------------------------------------------------------|------------------------------------------------------------------------------|------------------------|
| [Docker Compose 部署](https://qknow.qiantong.tech/docs/deploy/docker-compose-deployment.html) | 所有组件（DeepKE、Neo4j、Mysql、Nginx、Redis 等）以及 qKnow 知识平台源码都通过 Docker Compose 一键启动 | **初学者快速上手**、功能演示、测试环境  |
| [自主部署（纯手工安装）](https://qknow.qiantong.tech/docs/deploy/manual-deployment/)       | 所有依赖组件及 qKnow 知识平台服务均需手工安装和配置                                                | **生产环境**、大规模部署、个性化定制场景 |

## 👥 QQ交流群

欢迎加入 qKnow 官方 QQ 交流群，以便获取平台最新动态及进行用户间的交流。

👉 <a href="https://qknow.qiantong.tech/discuss.html">点击加入 QQ 交流群</a>

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
