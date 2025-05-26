# 🚀 千知平台（qKnow） 快速开始指南

---

## 一、准备工作 <small>（必备环境）</small>

在运行系统之前，请确保您已安装以下环境：

|                                                         软件/工具                                                         |   版本要求   | 推荐版本  |
|:---------------------------------------------------------------------------------------------------------------------:|:--------:|:-----:|
|              <img src="https://img.icons8.com/color/48/000000/java-coffee-cup-logo.png" width="24"/> JDK              |  >= 1.8  |  1.8  |
|                  <img src="https://img.icons8.com/color/48/000000/mysql-logo.png" width="24"/> MySQL                  | >= 5.7.0 |  5.7  |
| <img src="https://dist.neo4j.com/wp-content/uploads/20230926084108/Logo_FullColor_RGB_TransBG.svg" width="24"/> Neo4j | >= 3.4.5 | 3.4.5 |
|                    <img src="https://img.icons8.com/color/48/000000/redis.png" width="24"/> Redis                     |  >= 3.0  |  3.0  |
|               <img src="https://img.icons8.com/?size=100&id=jfjmkTUFX5Vf&format=png" width="24"/> Maven               |  >= 3.0  |  3.0  |
|                   <img src="https://img.icons8.com/color/48/000000/nodejs.png" width="24"/> Node.js                   |  >= 12   |  12   |

> **提示**: 前端安装完 `Node.js` 后，建议设置淘宝镜像源以提升依赖包下载速度。不推荐使用 `cnpm`，可能会引入一些不可预知的问题。
>
> ```bash
> npm config set registry https://registry.npmmirror.com
> ```

---

## 二、获取项目代码 <small>（下载与解压）</small>

前往 【[qKnow Gitee 下载页面](https://gitee.com/qiantongtech/qKnow)】 下载项目并解压到您的工作目录。

---

## 三、必要配置 <small>（数据库与服务配置）</small>

### 1. 创建数据库并导入数据

- 创建 MySQL 数据库：`qknow_dev`
- 导入数据脚本：`qknow_20250522.sql`、`quartz.sql`

### 2. 修改数据库连接配置

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

### 3. 修改Neo4j数据库连接配置

路径：`qKnow/qknow-framework/qknow-neo4j/src/main/resources/application-neo4j-dev.yml`

```yaml
neo4j:
  uri: 数据库地址
  user: 数据库账号
  password: 数据库密码
```

### 4. 配置服务端口和访问路径

路径：`qKnow/qknow-server/src/main/resources/application.yml`

```yaml
server:
  port: 端口            # 默认为8090
  servlet:
    context-path: /应用路径  # 应用访问路径，默认为空
```

### 5. 配置文件上传路径

路径一：`qKnow/qknow-server/src/main/resources/application.yml`

```yaml
# 项目相关配置
qknow:
  # 名称
  name: qKnow
  # 版本
  version: 3.8.8
  # 版权年份
  copyrightYear: 2025
  # 文件路径 示例（ Windows配置D:/qknow/uploadPath，Linux配置 /home/qknow/uploadPath） -- 需要和 file 模块保持一致
  profile: 文件路径
```

路径二：`qKnow/qknow-framework/qknow-file/src/main/resources/application-file-dev.yml`

```yaml
# 文件上传配置
dromara:
  x-file-storage: #文件存储配置
    default-platform: local #默认使用的存储平台
    thumbnail-suffix: ".min.jpg" #缩略图后缀，例如【.min.jpg】【.png】
    #对应平台的配置写在这里，注意缩进要对齐
    local-plus:
      - platform: local # 存储平台标识
        enable-storage: true  #启用存储
        enable-access: false #启用访问（线上请使用 Nginx 配置，效率更高）
        domain: "" # 访问域名，例如：“http://127.0.0.1:8081/file/”，注意后面要和 path-patterns 保持一致，“/”结尾，本地存储建议使用相对路径，方便后期更换域名
        path-patterns: /** # 访问路径
        base-path: / # 基础路径
        storage-path: 文件路径 # 存储路径
```

> **注意**: 以上两个配置文件中的文件路径务必保持一致，否则可能会导致文件无法上传和访问。

---

## 四、知识抽取工具配置（DeepKe）<small>（深度学习模型配置）</small>

当前开源版本采用 [**DeepKe**](https://github.com/zjunlp/DeepKE) 作为知识抽取工具。请参考官方文档进行安装，推荐使用 Docker 安装。

#### <span style="color:#4CAF50">修改 DeepKe 执行脚本</span>

路径：`qKnow/bin/deepke/start.sh`

```bash
#!/bin/bash                    
# deepke.sh
 
if [ $# -lt 1 ]; then
    echo "Usage: $0 <text>"
    exit 1
fi

TEXT="$1"
# 此处请换成你的docker容器id
docker exec afd0ee75f9e1 python -u predict.py text='\"$TEXT\"'
```

#### <span style="color:#4CAF50">修改配置文件</span>

路径：`qKnow/qknow-module-ext/qknow-module-ext-biz/src/main/resources/application-ext-dev.yml`

```yaml
# 非结构化抽取方式选择：deepke或者model，当前开源版本请选择deepke
unstruct:
  type: deepke

# 如果是本地化部署DeepKE，请将deepke.baseUrl修改为本地的路径
deepke:
  baseUrl: D:\qiantong
  predictYaml: ${deepke.baseUrl}\cnschema\conf\predict.yaml
  relatinCsv: ${deepke.baseUrl}\cnschema\data\test.csv
  conceptYaml: ${deepke.baseUrl}\DeepKE\example\ner\standard\conf\train.yaml
```

---

## 五、后端运行 <small>（启动服务器）</small>

1. 使用 IntelliJ IDEA 或 Eclipse 导入项目。
2. IDE 将自动加载 Maven 依赖，初次加载可能较慢。
3. 运行 `tech.qiantong.qknow.server.AniviaApplication.java`。启动成功后，可通过 `http://localhost:8090` 访问后端服务。

> **注意**: 仅启动后端服务不会显示静态页面，请继续部署前端服务。

---

## 六、前端运行 <small>（启动前端服务）</small>

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

打开浏览器，输入 `http://localhost`。默认账户/密码为 `admin/qKnow@123`。若能正确展示登录页面，并能成功登录，菜单及页面展示正常，则表明环境搭建成功。

> **提示**: 因为本项目是前后端完全分离的，所以需要前后端都单独启动好，才能进行访问。

---

希望这个文档能够帮助您更好地理解和操作 **千知平台（qKnow）** 项目！

---

<p align="center">
  <img src="https://img.icons8.com/fluent/48/000000/checkmark.png" width="36"/>
  <strong>完成！</strong>
</p>
