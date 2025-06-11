# 🚀 千知平台（qKnow） 快速开始指南

---

## 一、准备工作 <small>（必备环境）</small>

在运行系统之前，请确保您已安装以下环境：

|                                                         软件/工具                                                         |   版本要求   |  推荐版本  |
|:---------------------------------------------------------------------------------------------------------------------:|:--------:|:------:|
|              <img src="https://img.icons8.com/color/48/000000/java-coffee-cup-logo.png" width="24"/> JDK              |  >= 1.8  |  1.8   |
|                  <img src="https://img.icons8.com/color/48/000000/mysql-logo.png" width="24"/> MySQL                  | >= 5.7.0 |  5.7   |
| <img src="https://dist.neo4j.com/wp-content/uploads/20230926084108/Logo_FullColor_RGB_TransBG.svg" width="24"/> Neo4j |    4.x   | 4.4.40 |
|                    <img src="https://img.icons8.com/color/48/000000/redis.png" width="24"/> Redis                     |  >= 5.0  |  5.0   |
|               <img src="https://img.icons8.com/?size=100&id=jfjmkTUFX5Vf&format=png" width="24"/> Maven               |  >= 3.6  |  3.6   |
|                   <img src="https://img.icons8.com/color/48/000000/nodejs.png" width="24"/> Node.js                   |  >= 16   |   16   |

> **提示**: 前端安装完 `Node.js` 后，建议设置淘宝镜像源以提升依赖包下载速度。不推荐使用 `cnpm`，可能会引入一些不可预知的问题。
>
> ```bash
> npm config set registry https://registry.npmmirror.com
> ```

---

## 二、获取项目代码 <small>（下载与解压）</small>

前往 【[qKnow Gitee 下载页面](https://gitee.com/qiantongtech/qKnow)】 下载项目并解压到您的工作目录。

---

## 三、目录结构
### 2.1 项目结构;

```
├─qknow-framework           # 公共配置模块
├─   ├─qknow-auth           # oauth2模块
├─   ├─qknow-common         # 共通模块
├─   ├─qknow-config         # 配置模块
├─   ├─qknow-es             # es模块
├─   ├─qknow-file           # 文件管理模块
├─   ├─qknow-generator      # 代码生成器
├─   ├─qknow-mybatis        # mybatis配置
├─   ├─qknow-neo4j          # neo4j数据库模块
├─   ├─qknow-pay            # 支付模块
├─   ├─qknow-quartz         # 定时任务模块
├─   ├─qknow-redis          # redis模块
├─   ├─qknow-security       # security模块
├─   ├─qknow-websocket      # websocket模块
├─qknow-module-app          # 知识应用模块
├─qknow-module-dm           # 数据管理模块
├─qknow-module-ext          # 知识抽取模块
├─qknow-module-kmc          # 知识中心模块
├─qknow-module-system       # 系统管理模块
├─qknow-server              # 启动项目
├─qknow-ui                  # 前端模块
├─sql                       # sql脚本
├─README.md                 # 相关介绍
├─QUICKSTART.md                 # 快速启动
```

### 2.2 前端结构;

```
├─qknow-ui                  # 前端模块
├─   ├─public                   # 静态资源目录
├─   ├─src
├─   |  ├─api                   # 接口
├─   |  ├─assets                # 图片、样式等资源
├─   |  ├─components            # 通用组件
├─   |  ├─layout                # 布局
├─   |  ├─plugins               # 插件
├─   |  ├─router                # 路由
├─   |  ├─store                 # 状态管理
├─   |  ├─utils                 # 工具类
├─   |  ├─views                     # 页面视图
├─   |  |   ├─app                   # 知识应用模块
├─   |  |   ├─dm                    # 数据管理模块
├─   |  |   ├─ext                   # 知识抽取模块
├─   |  |   ├─kmc                   # 知识中心模块
├─   |  |   ├─system                # 系统管理模块
├─   ├─.env.development         # 开发环境配置
├─   ├─.env.production          # 生产环境配置
├─   ├─vite.config.js           # Vite配置文件
```

---

## 四、必要配置 <small>（数据库与服务配置）</small>

### 1. 创建MySQL数据库并导入数据

- 创建 MySQL 数据库：`qknow_dev`
- 导入数据脚本：`qknow_20250522.sql`、`quartz.sql`

### 2. 修改MySQL数据库连接配置

路径：`qKnow/qknow-server/src/main/resources/application-dev.yml`

```yaml
datasource:
  type: mysql

mysql:
  driver-class-name: com.mysql.cj.jdbc.Driver
  url: 数据库地址
  username: 数据库账号
  password: 数据库密码
```

### 3. 创建Neo4j数据库并导入数据

#### 步骤 1: 关闭Neo4j服务
- 进入Neo4j安装目录，在bin目录下，执行`./neo4j stop`将服务关掉。

#### 步骤 2: 导入数据库
- 进入Neo4j安装目录，在bin目录下，执行以下导入命令。

```bash
# 命令说明: --force 表示强制覆盖，database如果不存在，则创建新数据库。
# dump文件: sql/neo4j/neo4j.dump。      注：以下命令行中的路径请填写您的绝对路径。
./neo4j-admin load --from=dump文件路径 --database=您的数据库名称   --force 
```

- 进入Neo4j安装目录，在conf目录下，找到`neo4j.conf`文件，修改数据库名称:`dbms.default_database=您的数据库名称`

#### 步骤 3: 启动Neo4j服务
- 进入Neo4j安装目录，在bin目录下，执行`./neo4j start`启动服务

### 4. 修改Neo4j数据库连接配置

路径：`qKnow/qknow-framework/qknow-neo4j/src/main/resources/application-neo4j-dev.yml`

```yaml
neo4j:
  uri: 数据库地址
  user: 数据库账号
  password: 数据库密码
```

### 5. 配置服务端口和访问路径

路径：`qKnow/qknow-server/src/main/resources/application.yml`

```yaml
server:
  port: 端口            # 默认为8090
  servlet:
    context-path: /应用路径  # 应用访问路径，默认为空
```

---

## 五、知识抽取工具配置（DeepKE）<small>（深度学习模型配置）</small>

当前开源版本采用 [**开源中文知识图谱抽取框架开箱即用特别版DeepKE-cnSchema**](https://github.com/zjunlp/DeepKE/blob/main/README_CNSCHEMA_CN.md) 作为知识抽取工具。请参考官方文档进行安装，推荐使用 Docker 安装。

#### <span style="color:#4CAF50">1、根据需要修改抽取实体类型配置（需进入docker容器内修改） </span>
配置文件路径：`DeepKE/example/ner/standard/conf/train.yaml`

```yaml
adam_epsilon: 1e-8
data_dir: "data"
do_eval: True
do_train: True
eval_batch_size: 8
eval_on: "dev"
gpu_id: 0
gradient_accumulation_steps: 1
learning_rate: 1e-3            # tips：set 2e-5 for BERT with recommended datasets
num_train_epochs: 3            # the number of training epochs
output_dir: "checkpoints"
seed: 42
train_batch_size: 128
use_gpu: True                # use gpu or not
warmup_proportion: 0.1
weight_decay: 0.01

# For StepLR Optimizer
lr_step : 5
lr_gamma : 0.8
beta1: 0.9
beta2: 0.999

# 此处可修改抽取的实体类型
labels: ['LOC','ORG','PER']
# labels: ['YAS','TOJ', 'NGS', 'QCV', 'OKB', 'BQF', 'CAR', 'ZFM', 'EMT', 'UER', 'QEE', 'UFT', 'GJS', 'SVA', 'ANO', 'KEJ', 'ZDI', 'CAT', 'GCK', 'FQK', 'BAK', 'RET', 'QZP', 'QAQ', 'ZRE', 'TDZ', 'CVC', 'PMN']

use_multi_gpu: False
```

> **注意**: 实体类型配置请参考以下DeepKE官网文档提供的类型，若需抽取其他类型，需重新训练模型。训练方式请参考 [DeepKE 官方文档](https://github.com/zjunlp/DeepKE/blob/main/README_CNSCHEMA_CN.md#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E5%9E%8B)。

![extEntityType.png](.gitee/extEntityType.png)

#### <span style="color:#4CAF50">2、修改 DeepKE 执行脚本中的 **docker容器id** </span>

路径：`qKnow/bin/DeepKE/start.sh`

```bash
#!/bin/bash
# DeepKE.sh

if [ $# -lt 1 ]; then
    echo "Usage: $0 <text>"
    exit 1
fi

TEXT="$1"

# 注意：此处请换成你的docker容器id
# 注意：建议在本地命令行中运行以下测试命令，保证能够成功执行predict.py文件。
# 测试命令：docker exec dabea2a436b6 python -u predict.py "text='歌手周杰伦创作了多首经典歌曲，如《稻香》，这首歌深受中国各地听众的喜爱。'"
docker exec dabea2a436b6 python -u predict.py "text='$TEXT'"
```

---
#### <span style="color:#4CAF50">友情提示：</span>
> DeepKE抽取依赖CPU、GPU性能，建议抽取的文件不宜过大，否则抽取时间会很长。
---

## 六、后端运行 <small>（启动服务器）</small>

1. 使用 IntelliJ IDEA 或 Eclipse 导入项目。
2. IDE 将自动加载 Maven 依赖，初次加载可能较慢。
3. 运行 `tech.qiantong.qknow.server.QKnowApplication.java`。启动成功后，可通过 `http://localhost:8090` 访问后端服务。
4. 出现以下提示，表示后端服务启动成功：

```
(♥◠‿◠)ﾉﾞ  千知平台启动成功   ლ(´ڡ`ლ)ﾞ  
        _  __                    
   __ _| |/ /_ __   _____      __
  / _` | ' /| '_ \ / _ \ \ /\ / /
 | (_| | . \| | | | (_) \ V  V / 
  \__, |_|\_\_| |_|\___/ \_/\_/  
     |_|                         
```

> **注意**: 仅启动后端服务不会显示静态页面，请继续部署前端服务。

> **Tips**: 如果提示缺少依赖包aspose-words-java、aspose-cells-java时，需要将项目根目录下lib文件夹中的两个jar包引入，在项目根目录下运行如下两行代码：
 
```
mvn install:install-file -Dfile=lib/aspose-cells-21.8.cracked.jar  -DgroupId=com.aspose-cells  -DartifactId=aspose-cells-java -Dversion=21.8 -Dpackaging=jar
mvn install:install-file -Dfile=lib/aspose-words-15.8.0-jdk16.jar  -DgroupId=com.aspose-words  -DartifactId=aspose-words-java -Dversion=15.8.0 -Dpackaging=jar
```

---

## 七、前端运行 <small>（启动前端服务）</small>

### 1. 根据需要修改后台服务端口配置

```javascript
// vite 相关配置
server: {
  port: 80,
  host: true,
  open: true,
  proxy: {
    "/dev-api": {
      target: "http://localhost:8090",
      changeOrigin: true,
      rewrite: (p) => p.replace(/^\/dev-api/, ""),
    },
  }
}
```

### 2. 启动前端服务

#### 步骤 1: 进入项目目录

首先，导航到 `qknow-ui` 目录下：

```bash
# 进入项目目录
cd qknow-ui
```

#### 步骤 2: 安装依赖

接下来，安装项目的依赖包。这里我们使用淘宝镜像源以加速下载速度：

```bash
# 安装依赖（推荐使用淘宝镜像）
npm install --registry=https://registry.npmmirror.com
```

#### 步骤 3: 本地开发并启动项目

最后，运行以下命令来启动前端开发服务器：

```bash
# 本地开发 启动项目
npm run dev
```

### 3. 测试前端服务

打开浏览器，输入 `http://localhost`。默认账户/密码为 `qKnow/qKnow123`。若能正确展示登录页面，并能成功登录，菜单及页面展示正常，则表明环境搭建成功。

> **提示**: 因为本项目是前后端完全分离的，所以需要前后端都单独启动好，才能进行访问。

---

希望这个文档能够帮助您更好地理解和操作 **千知平台（qKnow）** 项目！

---

<p align="center">
  <img src="https://img.icons8.com/fluent/48/000000/checkmark.png" width="36"/>
  <strong>完成！</strong>
</p>
