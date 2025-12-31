<!--
  Copyright © 2026 Qiantong Technology Co., Ltd.
  qKnow Knowledge Platform
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qknow.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2026 江苏千桐科技有限公司
  qKnow 知识平台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qknow.qiantong.tech/business.html
-->

<template>
    <div class="app-container" ref="app-container" v-loading="appLoading">
        <el-container>
            <!-- 左侧可调整的部分 -->
            <!-- 左侧可调整的部分 -->
            <el-aside :style="{ width: `${leftWidth}px` }" class="left-pane">
                <div class="left-tree">
                    <div class="head-container">
                        <el-input :suffix-icon="Search" v-model="filterText" clearable
                                  style="width: 100%; margin-bottom: 20px" placeholder="输入查找名称"/>
                    </div>
                    <div class="head-container control-tree">
                        <el-tree
                                node-key="id"
                                :props="props"
                                show-checkbox
                                highlight-current
                                @check="handleCheck"
                                @node-click="handleNodeClick"
                                ref="treeRef"
                                class="filter-tree"
                                :data="treeData"
                                :default-checked-keys="checkedKeys"
                                default-expand-all
                                :filter-node-method="filterNode"
                        />
                    </div>
                </div>
            </el-aside>
            <!-- 拖拽条 -->
            <div class="resize-bar" @mousedown="startResize">
                <div class="resize-handle-sx">
                    <span class="zjsx"></span>
                </div>
            </div>
            <!-- 右侧部分 -->
            <el-main>
                <div class="head-title">
                    <div class="name">{{ taskInfo.name }}</div>
                    <div class="btns">
                        <el-button icon="Back" @click="handleBack">返回</el-button>
                        <el-button v-if="!releaseStatus" icon="Upload" type="primary" @click="handleRelease">发布
                        </el-button>
                        <el-button v-if="releaseStatus" icon="Upload" type="primary" @click="handleCancelRelease">取消发布
                        </el-button>
                    </div>
                </div>
                <div class="gragh-wrap">
                    <div class="gragh-container" id="gragh-container">
                        <div class="toolbar" ref="toolbarRef">
                            <template v-for="item in toolbar" :key="item.id">
                                <el-tooltip class="box-item" effect="light" :content="item.tip" placement="right">
                                    <div class="toolbar-item" @click="toolbarClick(item)" :title="item.tip">
                                        <el-icon>
                                            <component :is="item.icon"></component>
                                        </el-icon>
                                    </div>
                                </el-tooltip>
                            </template>
                        </div>
                    </div>
                    <!--  ----TODO 图例 --------------  -->
                    <!-- <div class="tool" @click="toolShow = !toolShow">
                                <div class="tool-mask" v-if="toolShow">
                                  <div class="mask-item" v-for="item in toolData" :key="item.label">
                                    <div class="mask-icon" :style="{ background: 'rgba(' + item.icon + ', 1)', border: '2px solid ' + 'rgba(0,0,0, 0.3)' }"></div>
                                    <div class="mask-label">{{ item.label }}</div>
                                  </div>
                                </div>
                              </div> -->
                    <transition name="el-zoom-in-right">
                        <div class="details-dialog" v-if="detailShow">
                            <div class="details-title">
                                <div class="title-label">
                                    <el-icon class="icon" @click="detailClose">
                                        <Close/>
                                    </el-icon>
                                    <span class="label">{{ currentNodeData.name }}</span>
                                </div>
                                <div class="title-slot">
                                    <el-button size="small" type="danger" icon="Delete" @click="handleDel">删除
                                    </el-button>
                                </div>
                            </div>
                            <div class="details-con">
                                <el-collapse v-model="collapseAct" @change="collapseChange">
                                    <el-collapse-item title="属性信息" name="1">
                                        <template #title>
                                            <div class="collapse-title">属性信息</div>
                                        </template>
                                        <div class="collapse-con">
                                            <el-table stripe height="100px" v-loading="attrLoading" :data="attrData">
                                                <el-table-column label="属性名称" key="a" prop="a"/>
                                                <el-table-column label="数据类型" key="b" prop="b" show-overflow-tooltip/>
                                                <el-table-column label="属性值" key="c" prop="c" show-overflow-tooltip/>
                                                <el-table-column label="操作" class-name="small-padding fixed-width"
                                                                 fixed="right" width="150">
                                                    <template #default="scope">
                                                        <el-button link type="primary" icon="Edit"
                                                                   @click="attrUpdate(scope.row)">修改
                                                        </el-button>
                                                        <el-divider direction="vertical"/>
                                                        <el-button link type="danger" icon="Delete"
                                                                   @click="attrDelete(scope.row)">删除
                                                        </el-button>
                                                    </template>
                                                </el-table-column>
                                            </el-table>
                                        </div>
                                    </el-collapse-item>
                                    <el-collapse-item title="关联三元组" name="2">
                                        <template #title>
                                            <div class="collapse-title">关联三元组</div>
                                        </template>
                                        <div class="collapse-con">
                                            <el-table stripe height="200px" v-loading="tripletLoading"
                                                      :data="tripletData">
                                                <el-table-column label="起点" key="startName" prop="startName"
                                                                 show-overflow-tooltip/>
                                                <el-table-column label="关系" key="name" prop="name"
                                                                 show-overflow-tooltip/>
                                                <el-table-column label="终点" key="endName" prop="endName"
                                                                 show-overflow-tooltip/>
                                                <el-table-column label="操作" class-name="small-padding fixed-width"
                                                                 fixed="right" width="150">
                                                    <template #default="scope">
                                                        <el-button link type="primary" icon="Edit"
                                                                   @click="tripletUpdate(scope.row)">修改
                                                        </el-button>
                                                        <el-divider direction="vertical"/>
                                                        <el-button link type="danger" icon="Delete"
                                                                   @click="tripletDelete(scope.row)">删除
                                                        </el-button>
                                                    </template>
                                                </el-table-column>
                                            </el-table>
                                        </div>
                                    </el-collapse-item>
                                    <el-collapse-item title="关联分段" name="3">
                                        <template #title>
                                            <div class="collapse-title">关联分段</div>
                                        </template>
                                        <div class="collapse-con1">
                                            <div v-for="(item, index) in textList" :key="index">
                                              <span v-if="currentNodeData.textIds && currentNodeData.textIds.split(',').includes(item.id.toString())">{{ item.text }}</span>
                                            </div>
                                        </div>
                                    </el-collapse-item>
                                    <el-collapse-item title="关联文件" name="4">
                                        <template #title>
                                            <div class="collapse-title">关联文件</div>
                                        </template>
                                        <div class="collapse-con1">
                                            <div v-for="(doc, index) in docList" :key="index">
                                                <div v-if="currentNodeData.docId == doc.id"
                                                     style="display: flex; justify-content: space-between">
                                                    <td style="text-align: left; width: 80%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; cursor: pointer"
                                                        :title="doc.name" @click="previewRefactoring(doc)">
                                                        {{ doc.name }}
                                                    </td>
                                                    <td style="text-align: right">{{ parseTime(doc.createTime,
                                                        "{y}-{m}-{d}") }}
                                                    </td>
                                                    <!--                          <td style="text-align: right">-->
                                                    <!--                            <el-button link type="primary" icon="view" @click="previewRefactoring(doc)">预览</el-button>-->
                                                    <!--                            <el-button link type="primary" icon="download" @click="handleDownload(doc)">下载</el-button>-->
                                                    <!--                          </td>-->
                                                </div>
                                            </div>
                                        </div>
                                    </el-collapse-item>
                                </el-collapse>
                            </div>
                        </div>
                    </transition>
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script setup name="KEresult">
    // 初始化画布
    import {Graph} from "@antv/g6";
    import {Search} from "@element-plus/icons-vue";
    import {getExtExtraction,
        getTaskTextList,
        deleteNode,
        strutReleaseByTaskId,
        strutCancelReleaseByTaskId,
    } from "@/api/ext/extUnstructTask/unstructTask";
    import {
        updateRelationship,
        deleteRelationship
    } from "@/api/ext/extStructTask/extStruct";
    import {getPdfPreview, updatePreviewCount, updateDownloadCount} from "@/api/kmc/kmcDocument/kmcDocument.js";

    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const taskInfo = ref({name: ""});
    watch(
        () => router.currentRoute.value.query.id,
        (val) => {
            if (val) {
                taskInfo.value = router.currentRoute.value.query;
            }
        },
        {immediate: true}
    );

    const releaseStatus = ref(false)//true已发布 false未发布
    const textList = ref(null);
    const docList = ref(null);
    let graph = null;
    let graphData = ref({
        nodes: [],
        edges: [],
    });
    const appLoading = ref(false);
    onMounted(() => {
        //查询抽取结果
        let id = taskInfo.value.id;
        getExtExtractionData({taskId: id});
        //根据taskId获取段落数据和文档
        getTextListAndDocList({taskId: id});
    });

    //根据taskId获取段落数据和文档
    function getTextListAndDocList(val) {
        textList.value = null;
        docList.value = null;
        getTaskTextList({
            taskId: val.taskId,
        }).then((response) => {
            if (response && response.code === 200) {
                //文本段落数据
                textList.value = response.data.textListByTaskId;
                //文档数据
                docList.value = response.data.docListByTaskId;
            }
        });
    }

    //查询抽取结果
    function getExtExtractionData(val) {
        appLoading.value = true;
        getExtExtraction({
            taskId: val.taskId,
        }).then((response) => {
            appLoading.value = false;
            let data = response.data;
            console.log("-----data----", data);
            releaseStatus.value = response.data.releaseStatus == 1 ? true : false

            let entityList = data.entities.map((item) => {
                return {
                    ...item,
                    id: item.id + "",
                    size: 40,
                    halo: true,
                    style: {
                        fill: "#7dbffa",
                    },
                };
            });
            let tripletList = data.relationships.map((item) => {
                return {
                    ...item,
                    id: "edge" + item.id,
                    name: item.relationType, //关系
                    source: item.startId + "", //头部实体
                    target: item.endId + "", //尾部实体
                };
            });
            console.log("-----数据1----", entityList);
            console.log("-----数据2----", tripletList);
            graphData.value.nodes = entityList;
            graphData.value.edges = tripletList;
            console.log("-----数据----", graphData.value);
            //设置画布
            setGraph(graphData.value);
            if (data.length == 0) return;
            // 初始化树结构
            initTree();
        });
    }

    function setGraph(data) {
        // 画布逻辑
        graph = new Graph({
            container: "gragh-container",
            animation: data.nodes.length < 100,
            data: data,
            autoFit: "view",
            autoResize: true,
            node: {
                type: "circle",
                style: {
                    halo: (d) => d.halo || false,
                    labelWordWrap: true,
                    labelMaxWidth: "300",
                    labelFill: "#DAE6FD",
                    labelPlacement: "bottom",
                    labelText: (d) => d.name,
                    labelFontSize: 16,
                    size: (d) => d.size,
                    ports: [],
                },
                state: {
                    // 高亮
                    highlight: {
                        fill: "#0076ff",
                        halo: true,
                        lineWidth: 0,
                    },
                    click: {
                        halo: true,
                        haloLineWidth: 20,
                        stroke: "#0036ff",
                        lineWidth: 4,
                    },
                    dim: {
                        fill: "#99ADD1",
                    },
                },
                palette: {
                    type: "group",
                    field: "cluster",
                },
            },
            edge: {
                state: {
                    // 高亮
                    highlight: {
                        stroke: "#0076ff",
                    },
                },
                style: {
                    lineWidth: 5,
                    labelText: (d) => d.name,
                    labelFill: "#7DBFFA",
                    labelFontSize: 12,
                    labelOffsetY: 16,
                    endArrow: true,
                },
                palette: {
                    type: "group",
                    field: "cluster",
                },
            },
            layout: {
                type: "d3-force",
                link: {
                    distance: (d) => {
                        return 100;
                    },
                    strength: (d) => {
                        return 0.2;
                    },
                },
                manyBody: {
                    strength: (d) => {
                        return -180;
                    },
                },
            },
            plugins: [
                {
                    type: "fullscreen",
                    key: "fullscreen",
                },
            ],
            behaviors: [
                "zoom-canvas",
                "drag-canvas",
                "drag-element-force",
                {
                    type: "hover-activate",
                    enable: (event) => event.targetType === "node",
                    degree: 1, // 👈🏻 Activate relations.
                    state: "highlight",
                    inactiveState: "dim",
                    onHover: (event) => {
                        event.view.setCursor("pointer");
                    },
                    onHoverEnd: (event) => {
                        event.view.setCursor("default");
                    },
                },
                {
                    type: "click-select",
                    key: "clickControl",
                    state: "click",
                    onClick: (d) => {
                        console.log(d, "===dd");
                        if (d.target.nodeName === "document") {
                            detailShow.value = false;
                        }
                        if (d.target.type == "node") {
                            currentNodeData.value = graphData.value.nodes.find((e) => e.id == d.target.id);
                            console.log("---------currentNodeData.value-------", currentNodeData.value);

                            //关联三元组
                            let tripletList = [];
                            graphData.value.edges.forEach((e) => {
                                if (e.startId == d.target.id || e.endId == d.target.id) {
                                    tripletList.push(e);
                                }
                            });
                            tripletData.value = tripletList;
                            detailShow.value = true;
                        }
                    },
                },
            ],
        });
        graph.render();
    }

    //发布
    const handleRelease = () => {
        proxy.$modal
            .confirm(`是否确认发布？`)
            .then(() => {
                strutReleaseByTaskId({taskId: taskInfo.value.id}).then(res => {
                    if (res && res.code == 200) {
                        releaseStatus.value = true
                        proxy.$modal.msgSuccess("发布成功")
                    }
                })
            })
    }

    //取消发布
    const handleCancelRelease = () => {
        proxy.$modal
            .confirm(`是否确认取消发布？`)
            .then(() => {
                strutCancelReleaseByTaskId({taskId: taskInfo.value.id}).then(res => {
                    if (res && res.code == 200) {
                        releaseStatus.value = false
                        proxy.$modal.msgSuccess("取消发布成功")
                    }
                })
            })
    }

    const toolbarRef = ref();
    const toolbar = ref([
        {
            id: "full-screen",
            icon: "FullScreen",
            isFull: false,
            tip: "全屏",
        },
        {
            id: "zoom-in",
            icon: "ZoomIn",
            tip: "放大",
        },
        {
            id: "zoom-out",
            icon: "ZoomOut",
            tip: "缩小",
        },
        //   {
        //     id: "undo",
        //     icon: "RefreshLeft",
        //     tip: "撤销",
        //   },
        //   {
        //     id: "redo",
        //     icon: "RefreshRight",
        //     tip: "重做",
        //   },
        {
            id: "auto-fit",
            icon: "Aim",
            tip: "恢复视角",
        },
        {
            id: "export",
            icon: "Download",
            tip: "导出",
        },
        {
            id: "reset",
            icon: "Refresh",
            tip: "重置",
        },
    ]);
    const toolbarClick = async (item) => {
        const animation = {
            duration: 500,
            easing: "linear",
        };
        const history = graph.getPluginInstance("history");
        switch (item.id) {
            case "full-screen": {
                const fullscreenPlugin = graph.getPluginInstance("fullscreen");
                if (!item.isFull) {
                    fullscreenPlugin.request();
                } else {
                    fullscreenPlugin.exit();
                }
                item.isFull = !item.isFull;
                break;
            }
            case "zoom-in":
                graph.zoomBy(1.2, animation);
                break;
            case "zoom-out":
                graph.zoomBy(0.8, animation);
                break;
            case "redo":
                if (history.canRedo()) history.redo();
                break;
            case "undo":
                if (history.canUndo()) history.undo();
                break;
            case "auto-fit":
                graph.fitView(
                    {
                        when: "always", // 始终进行适配
                        direction: "both", // 在两个方向上适配
                    },
                    animation
                );
                break;
            case "export": {
                const dataURL = await graph.toDataURL();
                const [head, content] = dataURL.split(",");
                const contentType = head.match(/:(.*?);/)[1];

                const bstr = atob(content);
                let length = bstr.length;
                const u8arr = new Uint8Array(length);
                while (length--) {
                    u8arr[length] = bstr.charCodeAt(length);
                }
                const blob = new Blob([u8arr], {type: contentType});
                let link = document.createElement("a");
                let url = URL.createObjectURL(blob); // 创建 Blob URL
                link.href = url;
                link.setAttribute("download", taskInfo.value.name + "抽取结果"); // 设置文件名
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                URL.revokeObjectURL(url); // 释放 Blob URL
                break;
            }
            case "reset": {
                graph.destroy();
                graph = null;
                let id = taskInfo.value.id;
                getExtExtractionData({taskId: id});
                //根据taskId获取段落数据和文档
                getTextListAndDocList({taskId: id});
                break;
            }
        }
    };

    // 侧边栏拖拽
    const leftWidth = ref(278); // 初始左侧宽度
    const isResizing = ref(false); // 判断是否正在拖拽
    let startX = 0; // 鼠标按下时的初始位置
    const startResize = (event) => {
        isResizing.value = true;
        startX = event.clientX;
        // 使用 requestAnimationFrame 减少重绘频率
        document.addEventListener("mousemove", updateResize);
        document.addEventListener("mouseup", stopResize);
    };
    const updateResize = (event) => {
        if (isResizing.value) {
            const delta = event.clientX - startX; // 计算鼠标移动距离
            leftWidth.value += delta; // 修改左侧宽度
            startX = event.clientX; // 更新起始位置
            // 使用 requestAnimationFrame 来减少页面重绘频率
            requestAnimationFrame(() => {
            });
        }
    };
    const stopResize = () => {
        isResizing.value = false;
        document.removeEventListener("mousemove", updateResize);
        document.removeEventListener("mouseup", stopResize);
    };

    // 过滤树
    const filterText = ref("");
    const treeRef = ref();
    watch(filterText, (val) => {
        treeRef.value && treeRef.value.filter(val);
    });

    // 树结构
    const props = {
        label: "label",
        children: "children",
    };
    let treeData = ref([]);
    let checkedKeys = ref([]);
    const initTree = () => {
        treeData.value = [
            {
                id: "parent1",
                label: "实体",
                children: graphData.value.nodes.map((item) => {
                    return {
                        id: item.id,
                        label: item.name,
                    };
                }),
            },
        ];
        const getAllIds = (treeData) => {
            let ids = [];
            for (const node of treeData) {
                ids.push(node.id); // 将当前节点的id加入数组
                if (node.children && node.children.length > 0) {
                    ids = ids.concat(getAllIds(node.children)); // 递归处理子节点
                }
            }
            return ids;
        };
        checkedKeys.value = getAllIds(treeData.value);
        // console.log(checkedKeys.value);
    };

    //预览
    function previewRefactoring(row) {
        console.log(row, "1111111111");
        getPdfPreview({url: row.path}).then((res) => {
            console.log(res);
            if (res.data.fileBase64 == "") {
                this.$modal.msgError("文件无内容");
            } else {
                viewPdf(res.data.fileBase64);
                //修改浏览次数
                updatePreviewCount(row.id).then((response) => {
                });
            }
        });
    }

    // content是base64格式
    function viewPdf(content) {
        console.log("content", content);
        const blob = base64ToBlob(content);
        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(blob);
        } else {
            // 获取屏幕尺寸
            const screenWidth = window.screen.width;
            const screenHeight = window.screen.height;

            // 设置窗口尺寸为屏幕尺寸的一部分，例如60%
            const width = screenWidth * 0.7;
            const height = screenHeight * 0.7;

            // 计算窗口居中时的左上角位置
            const left = (screenWidth - width) / 2;
            const top = (screenHeight - height) / 2;
            const fileURL = URL.createObjectURL(blob);
            window.open(fileURL, "预览", `scrollbars=yes, width=${width}, height=${height}, top=${top}, left=${left}`); //打开ppf文件
        }
    }

    function base64ToBlob(data) {
        let code = data.replace(/[\n\r]/g, ""); // 检查base64字符串是否符合base64编码
        // atob() 方法用于解码使用 base-64 编码的字符串。
        const raw = window.atob(code);
        const rawLength = raw.length;
        const uInt8Array = new Uint8Array(rawLength);
        for (let i = 0; i < rawLength; ++i) {
            // 将解码后的逐个字符转换成Unicode序号，放入Unit8Array数组
            uInt8Array[i] = raw.charCodeAt(i);
        }
        // 通过Blob将Uint8Array数组转换成pdf类型的文件对象
        return new Blob([uInt8Array], {type: "application/pdf"});
    }

    //下载
    function handleDownload(row) {
        if (row.path === "") {
            this.$modal.msgError("没有文件");
        } else {
            fetch(row.path) // 使用 fetch 获取文件数据
                .then((response) => response.blob()) // 将文件数据转换为 Blob
                .then((blob) => {
                    updateDownloadCount(row.id).then((response) => {
                        const link = document.createElement("a");
                        const url = URL.createObjectURL(blob); // 创建 Blob URL
                        link.href = url;
                        link.setAttribute("download", row.name || "下载文件"); // 设置文件名
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);
                        URL.revokeObjectURL(url); // 释放 Blob URL
                    });
                })
                .catch(() => {
                    this.$modal.msgError("文件下载失败");
                });
        }
    }

    const handleCheck = (data, checked) => {
        const id = data.id;
        // console.log(data, checked.checkedKeys);
        if (id !== "parent1") {
            // 隐藏数据
            let hideData = graphData.value.edges.filter((e) => e.source === id || e.target === id).map((e) => e.id);
            hideData.push(id);
            // 显示数据
            let showData = graphData.value.edges
                .filter((e) => e.source === id || e.target === id)
                .filter((e) => checked.checkedKeys.includes(e.source) && checked.checkedKeys.includes(e.target))
                .map((e) => e.id);
            showData.push(id);
            if (checked.checkedKeys.includes(id)) {
                showData.forEach((e) => graph.showElement(e));
            } else {
                hideData.forEach((e) => graph.hideElement(e));
            }
        } else {
            let data = [...graphData.value.nodes, ...graphData.value.edges];
            if (checked.checkedKeys.length !== 0) {
                data.map((e) => e.id).forEach((e) => graph.showElement(e));
            } else {
                data.map((e) => e.id).forEach((e) => graph.hideElement(e));
            }
        }
    };
    const oldId = ref(null);
    const handleNodeClick = (data) => {
        console.log(data.id);
        if (oldId.value) {
            graph.setElementState(oldId.value, "");
        }
        oldId.value = data.id;
        if (data.id == "parent1" || data.id == "parent2") return;
        graph.setElementState(data.id, "click").then(() =>
            graph.focusElement(data.id).then(() => {
                graph.zoomTo(2);
            })
        );
        // 弹框已展示，调整内容
        if (detailShow.value) {
            currentNodeData.value = graphData.value.nodes.find((e) => e.id == data.id);
            //关联三元组
            let tripletList = [];
            graphData.value.edges.forEach((e) => {
                if (e.startId == data.id || e.endId == data.id) {
                    tripletList.push(e);
                }
            });
            tripletData.value = tripletList;
        }
    };
    const filterNode = (value, data) => {
        if (!value) return true;
        return data.label.includes(value);
    };

    // 点击事件
    const handleBack = () => {
        router.push({
            path: "/ext/unstructTask",
            query: {
                pageNum: taskInfo.value.pageNum,
                pageSize: taskInfo.value.pageSize,
            },
        });
    };

    /** tool图例
     const toolShow = ref(false);
     const toolData = ref([
     {
    label: "泵站",
    icon: "125, 191, 249",
  },
     {
    label: "设备",
    icon: "169, 114, 41",
  },
     {
    label: "故障现象",
    icon: "236, 90, 172",
  },
     {
    label: "故障原因",
    icon: "180, 140, 244",
  },
     {
    label: "处理措施",
    icon: "117, 221, 202",
  },
     {
    label: "测点告警",
    icon: "147, 156, 248",
  },
     ]);
     */

// 右侧弹框
    const currentNodeData = ref();
    const detailShow = ref(false);
    const detailClose = () => {
        detailShow.value = false;
    };
    const handleDel = () => {
        if (releaseStatus.value) {
            proxy.$modal.msgWarning("已发布状态禁止删除和修改！")
        } else {
            proxy.$modal
                .confirm(`是否确认删除实体为【${currentNodeData.value.name}】的数据项？`)
                .then(() => {
                    // 接口
                    const id = currentNodeData.value.id;
                    deleteNode(id).then((response) => {
                        console.log("-------删除成功------", response);
                        // 隐藏弹框，过滤树，暂隐node节点
                        detailClose();
                        treeData.value[0].children = treeData.value[0].children.filter((e) => e.id !== id);
                        let hideData = graphData.value.edges.filter((e) => e.source === id || e.target === id).map((e) => e.id);
                        hideData.push(id);
                        hideData.forEach((e) => graph.hideElement(e, false));
                        // 过滤数据
                        graphData.value.nodes = graphData.value.nodes.filter((e) => e.id !== id);
                        graphData.value.edges = graphData.value.edges.filter((e) => e.source !== id && e.target !== id);

                        proxy.$modal.msgSuccess("删除成功");
                    });
                })
                .catch(() => {
                });
        }

    };

    // 右侧详情-折叠面板
    const collapseAct = ref(["1", "2", "3", "4"]);
    const collapseChange = (val) => {
        console.log(val);
    };

    // 表格1
    const attrLoading = ref(false);
    const attrData = ref([]);
    const attrUpdate = () => {
    };
    const attrDelete = () => {
    };

    // 表格2
    const tripletLoading = ref(false);
    const tripletData = ref([]);

    // 修改关系
    const tripletUpdate = (val) => {
        if (releaseStatus.value) {
            proxy.$modal.msgWarning("已发布状态禁止删除和修改！")
        } else {
            proxy.$modal
                .prompt(`请输入起点【${val.startName}】终点【${val.endName}】的关系`)
                .then((res) => {
                    console.log('---修改关系----', res)
                    if (!res.value) {
                        proxy.$modal.msgWarning("关系不能为空")
                    }else if(/^\d+$/.test(res.value)){
                        proxy.$modal.msgWarning("关系不能为纯数字")
                    }else {
                        // let startNode = graphData.value.nodes.find(e => e.id == val.startId)
                        // let endNode = graphData.value.nodes.find(e => e.id == val.endId)
                        updateRelationship({
                            relationshipId: Number(val.id.replace('edge', '')),
                            relationship: res.value,
                            startId: val.startId,
                            endId: val.endId,
                            taskId: Number(taskInfo.value.id),
                            extractType: 2
                        }).then((response) => {
                            if (response && response.code == 200) {
                                graphData.value.edges.forEach(e => {
                                    if (e.id == val.id) {
                                        e.relationType = res.value
                                    }
                                })
                                tripletData.value.forEach(e => {
                                    if (e.id == val.id) {
                                        e.name = res.value
                                    }
                                })
                            }
                        })
                    }
                })
        }
    };

    // 删除关系
    const tripletDelete = (val) => {
        if (releaseStatus.value) {
            proxy.$modal.msgWarning("已发布状态禁止删除和修改！")
        } else {
            proxy.$modal
                .confirm(`是否确认删除起点【${val.startName}】终点【${val.endName}】的数据项？`)
                .then(() => {
                    deleteRelationship({
                        relationshipId: Number(val.id.replace('edge', ''))
                    }).then(res => {
                        if (res && res.code == 200) {
                            graphData.value.edges = graphData.value.edges.filter(e => e.id != val.id)
                            tripletData.value = tripletData.value.filter(e => e.id != val.id)
                        }
                    })
                })
        }
    };

    // 表格3
    // const sourceLoading = ref(false);
    // const sourceData = ref([]);
</script>

<style scoped lang="scss">
    .el-zoom-in-right-enter-active,
    .el-zoom-in-right-leave-active {
        opacity: 1;
        transform: translateX(0);
        transform-origin: center right;
        transition: var(--el-transition-md-fade);
    }

    .el-zoom-in-right-enter-from,
    .el-zoom-in-right-leave-active {
        opacity: 0;
        transform: translateX(100%);
    }

    .app-container {
        height: calc(100% - 30px);
        display: flex;
        justify-content: space-between;

        .el-main {
            padding: 0px 0px;
        }

        .el-aside {
            padding: 0px;
            margin-bottom: 0px;
            background-color: #f0f2f5;
        }

        .left-tree {
            padding: 15px 15px 5px 15px;
        }

        .control-tree {
            height: calc(100% - 52px);
            background: #fff;
            overflow: hidden auto;

            :deep(.el-icon) {
                color: #7e7e7e;
            }

            :deep(.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content) {
                background-color: #e3f4fc !important;
                border-right: 2px solid var(--el-color-primary);
            }
        }

        .resize-bar {
            cursor: ew-resize;
            background-color: #f0f2f5;
            height: 86vh;
            display: flex;
            align-items: center;
            justify-content: center;

            .resize-handle-sx {
                width: 15px;
                text-align: center;

                .zjsx {
                    display: inline-block;
                    width: 5px;
                    height: 50px;
                    border-left: 1px solid #ccc;
                    border-right: 1px solid #ccc;
                }
            }
        }

        .control {
            width: 100%;
            height: 100%;
        }

        .head-title {
            height: 50px;
            background: #fff;
            padding: 10px;
            margin-bottom: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .name {
                font-family: PingFangSC;
                font-weight: 500;
                font-size: 16px;
            }

            .btns {
                float: right;

                .el-button {
                    height: 28px;
                }
            }
        }

        .gragh-wrap {
            position: relative;
            height: calc(100% - 60px);
            overflow: hidden auto;

            .gragh-container {
                width: 100%;
                height: 100%;
                background: #fff url("@/assets/ke/images/bg.png") no-repeat;
                background-size: 100% 100%;

                :deep(.g6-toolbar) {
                    .g6-toolbar-item {
                        fill: #7dbffa;

                        &:hover {
                            background-color: rgba(255, 255, 255, 0.2);
                        }
                    }
                }
            }

            .toolbar {
                position: absolute;
                top: 8px;
                right: unset;
                bottom: unset;
                left: 8px;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                border-radius: 2px;
                box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.4);
                // opacity: 0.65;
                z-index: 100;

                .toolbar-item {
                    display: inline-block;
                    width: 20px;
                    height: 20px;
                    padding: 4px;
                    cursor: pointer;
                    box-sizing: content-box;

                    &:hover {
                        background-color: rgba(255, 255, 255, 0.2);
                    }

                    i {
                        color: #7dbffa;
                        font-size: 20px;
                        pointer-events: none;
                    }
                }
            }

            .tool {
                cursor: pointer;
                position: absolute;
                left: 20px;
                bottom: 20px;
                width: 50px;
                height: 50px;
                background: url("@/assets/ke/images/tool.png") no-repeat;
                background-size: 100% 100%;

                .tool-mask {
                    width: 151px;
                    height: 214px;
                    background: #1c376a;
                    border-radius: 4px 4px 4px 4px;
                    border: 1px solid #448fff;
                    position: absolute;
                    bottom: calc(100% + 8px);
                    left: 0;
                    box-sizing: border-box;
                    padding: 20px;

                    .mask-item {
                        display: flex;
                        align-items: center;
                        margin-bottom: 12px;

                        .mask-icon {
                            width: 18px;
                            height: 18px;
                            border-radius: 50%;
                            margin-right: 8px;
                        }

                        .mask-label {
                            line-height: 1;
                            font-family: PingFangSCs;
                            font-size: 13px;
                            color: #d7e3fa;
                        }
                    }
                }
            }

            .details-dialog {
                position: absolute;
                top: 0;
                right: 0;
                width: 500px;
                height: 100%;
                background: #fff;
                box-shadow: 0px 0px 4px 1px rgba(0, 0, 0, 0.2);

                .details-title {
                    height: 42px;
                    padding: 0 12px;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    border-bottom: 1px solid #e8e8e8;

                    .title-label {
                        display: flex;
                        align-items: center;

                        .icon {
                            cursor: pointer;
                        }

                        .label {
                            margin-left: 12px;
                            color: var(--el-color-primary);
                        }
                    }

                    //.title-slot {}
                }

                .details-con {
                    height: calc(100% - 42px);
                    overflow: hidden auto;
                    padding: 10px 10px;
                    --el-border-color-lighter: #ddd;

                    &::-webkit-scrollbar {
                        width: 2px;
                    }

                    :deep(.el-collapse) {
                        border: none;

                        .el-collapse-item__wrap {
                            border: none;
                        }

                        .el-collapse-item {
                            margin-bottom: 10px;
                        }
                    }

                    :deep(.el-collapse-item__header) {
                        border: none;
                        height: 30px;
                    }

                    :deep(.el-collapse-item__content) {
                        padding-bottom: 0px;
                    }

                    .collapse-title {
                        font-family: Pingfang Sc;
                        font-size: 16px;
                        font-weight: 600;
                        // padding: 0 10px;
                        display: flex;
                        align-items: center;

                        &::before {
                            display: inline-block;
                            content: "";
                            width: 6px;
                            height: 16px;
                            border-radius: 2px 2px 2px 2px;
                            background: var(--el-color-primary);
                            margin-right: 10px;
                        }
                    }

                    .collapse-con {
                        padding: 5px 0;

                        :deep(.el-descriptions__label.el-descriptions__cell.is-bordered-label) {
                            width: 100px;
                        }
                    }

                    .collapse-con1 {
                        padding: 5px 5px;

                        :deep(.el-descriptions__label.el-descriptions__cell.is-bordered-label) {
                            width: 100px;
                        }

                        ::v-deep {
                            border-color: #f5ecec !important;
                            border: 0.0001rem solid;
                        }
                    }
                }
            }
        }
    }
</style>
