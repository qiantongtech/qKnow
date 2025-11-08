FROM --platform=linux/amd64 ubuntu:18.04

# 设置 UTF-8 环境（关键！）
ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8

RUN apt-get update && apt-get install -y --no-install-recommends \
         build-essential \
         cmake \
         pkg-config \
         wget \
         git \
         curl \
         ca-certificates \
         openjdk-8-jdk \
    && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# 安装 Miniconda
ENV CONDA_HOME=/root/miniconda3
RUN curl -o ~/miniconda.sh https://repo.anaconda.com/miniconda/Miniconda3-4.7.12-Linux-x86_64.sh && \
    chmod +x ~/miniconda.sh && \
    ~/miniconda.sh -b && \
    rm ~/miniconda.sh

ENV PYTHON_VERSION=3.8.0
RUN $CONDA_HOME/bin/conda create -y --name deepke python=$PYTHON_VERSION

# 将 deepke 环境加入 PATH，使其成为默认 Python
ENV CONDA_ENV_PATH=$CONDA_HOME/envs/deepke
ENV PATH=$CONDA_ENV_PATH/bin:$CONDA_HOME/bin:$PATH

# 升级 pip 并安装基础构建工具
RUN pip install --upgrade "pip==24.0" "setuptools>=65" "wheel" "setuptools_scm" -i https://mirrors.aliyun.com/pypi/simple/

# 提前解决 urllib3 兼容性问题
RUN pip install "urllib3>=1.25.4,<1.27" -i https://mirrors.aliyun.com/pypi/simple/

RUN pip install torch==1.10 -i https://mirrors.aliyun.com/pypi/simple/ && \
    pip install transformers==3.4.0 -i https://mirrors.aliyun.com/pypi/simple/ && \
    pip install pytorch-transformers -i https://mirrors.aliyun.com/pypi/simple/ && \
    pip install "protobuf==3.20.1" -i https://mirrors.aliyun.com/pypi/simple/ && \
    pip install six -i https://mirrors.aliyun.com/pypi/simple/ && \
    pip install hydra-core==1.0.6 pyld==2.0.3 -i https://mirrors.aliyun.com/pypi/simple/

# 设置时区和应用目录
ENV TZ=Asia/Shanghai \
    APP_HOME=/usr/app/jar
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && mkdir -p $APP_HOME

WORKDIR $APP_HOME
COPY ./*.jar ./qknow.jar
COPY ./bin ./bin
COPY ./data ./data

EXPOSE 8090
ENTRYPOINT ["java", "-Xms512m", "-Xmx2g", "-Dspring.profiles.active=prod", "-jar", "qknow.jar"]
CMD ["--server.port=8090"]
