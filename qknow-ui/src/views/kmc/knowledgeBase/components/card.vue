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
    <div class="card-container">
        <div v-for="(item, index) in data" :key="index" class="card" @click="handleDetail(item)">
            <!-- 标题栏：图标 + 名称 + 启用标签 -->
            <div class="card-top">
                <div class="card-title">
                    <div class="card-title-text">
                        <span class="icon">
                            <img src="@/assets/icons/svg/kmc/kmc.svg" alt="知识库图标" />
                        </span>
                        <span class="name">{{ item.name }}</span>
                    </div>
                    <div class="card-title-status">
                        <el-tag :type="item.validFlag == 1 ? 'primary' : 'warning'" size="small">
                            {{ item.validFlag == 1 ? '启用' : '禁用' }}
                        </el-tag>
                    </div>
                </div>

                <el-divider style="margin: 7px 0px; width: 100%" />

                <!-- 统计项 + 标签 + 封面图 -->
                <div class="card-bottom">
                    <div class="card-bottom-right">
                        <div class="card-description">{{ item.description }}</div>
                        <div class="card-bottom-text-group">
                            <div class="card-bottom-text">
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    content="文件总数"
                                    placement="top"
                                >
                                    <img
                                        src="@/assets/icons/svg/kmc/FileNum.svg"
                                        alt="文件数"
                                        class="stat-icon"
                                    />
                                </el-tooltip>
                                <span class="card-bottom-text-name">{{ item.fileCount }} 个</span>
                            </div>
                            <div class="card-bottom-text">
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    content="索引方式"
                                    placement="top"
                                >
                                    <img
                                        src="@/assets/icons/svg/kmc/IndexWay.svg"
                                        alt="索引方式"
                                        class="stat-icon"
                                    />
                                </el-tooltip>
                                <span class="card-bottom-text-name">
                                    {{ getDictLabel(item.indexingTechnique) }}
                                </span>
                            </div>
                            <div class="card-bottom-text">
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    content="创建时间"
                                    placement="top"
                                >
                                    <img
                                        src="@/assets/icons/svg/kmc/CreateTime.svg"
                                        alt="时间"
                                        class="stat-icon"
                                    />
                                </el-tooltip>
                                <span class="card-bottom-text-name">{{
                                    parseTime(item.createTime, '{y}-{m}-{d}')
                                }}</span>
                            </div>
                            <div class="card-bottom-text">
                                <el-tooltip
                                    class="item"
                                    effect="dark"
                                    content="创建人"
                                    placement="top"
                                >
                                    <img
                                        src="@/assets/icons/svg/kmc/CreateBy.svg"
                                        alt="创建人"
                                        class="stat-icon"
                                    />
                                </el-tooltip>
                                <span class="card-bottom-text-name">{{ item.createBy }}</span>
                            </div>
                        </div>
                        <div class="card-title-tags">
                            <el-tag
                                v-for="(tag, index) in parseTags(item.tags)"
                                :key="index"
                                size="medium"
                                class="card-tag"
                            >
                                {{ tag.name }}
                            </el-tag>
                        </div>
                    </div>
                    <div class="card-bottom-left">
                        <img :src="getImage(item)" alt="知识库封面" class="cover" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import KnowledgeCover from '@/assets/kmc/knowledgeCover.png';

    const { proxy } = getCurrentInstance();
    const router = useRouter();

    defineEmits(['handleDataScope']);
    const { kmc_know_index } = proxy.useDict('kmc_know_index');

    const getDictLabel = (dictValue) => {
        // 安全地获取字典数组
        const dictArray = kmc_know_index.value;

        if (Array.isArray(dictArray)) {
            const found = dictArray.find((item) => item.value === dictValue);
            if (found) {
                return found.label;
            }
        }

        // 如果找不到匹配项，返回原值或默认显示
        return dictValue || '--';
    };
    defineProps({
        data: {
            type: Array,
            required: true
        }
    });

    // 解析标签
    const parseTags = (tagsStr) => {
        try {
            return JSON.parse(tagsStr);
        } catch {
            return [];
        }
    };

    function getImage(row) {
        if (!row.coverImage) {
            return KnowledgeCover;
        }
        return import.meta.env.VITE_APP_BASE_API + '/profile' + row.coverImage;
    }

    // 查看详情
    function handleDetail(row) {
        router.push({
            path: '/kmc/' + row.id + '/kmcDocument'
        });
    }
</script>

<style scoped lang="scss">
    .card-container {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(555px, 1fr));
        gap: 20px;
        padding: 16px 0px;
        box-sizing: border-box;
        width: 100%;
        // 平滑过渡效果
        transition: all 0.3s ease;
        @media (min-width: 555px) {
            grid-template-columns: repeat(1, minmax(555px, 1fr));
        }
        @media (min-width: 1160px) {
            grid-template-columns: repeat(2, minmax(555px, 1fr));
        }
        @media (min-width: 1735px) {
            grid-template-columns: repeat(3, minmax(555px, 1fr));
        }

        .card {
            background-color: #fff;
            width: 100%;
            min-width: 555px;
            min-height: 240px;
            display: flex;
            flex-direction: column;
            border-radius: 2px;
            cursor: pointer;
            transition:
                transform 0.2s ease,
                box-shadow 0.2s ease;

            .card-top {
                display: flex;
                flex-direction: column;
                padding: 21px;

                .card-title {
                    height: 28px;
                    display: flex;
                    align-items: center;
                    justify-content: space-between;

                    .card-title-text {
                        height: 28px;
                        font-family: PingFang SC;
                        font-weight: 800;
                        font-size: 20px;
                        color: #333333;
                        line-height: 28px;
                        text-align: left;
                        display: flex;
                        align-items: center;
                        min-width: 0; // 允许文本收缩

                        .icon {
                            width: 25px;
                            height: 25px;
                            background: #e6f0fd;
                            border-radius: 4px;
                            margin-right: 8px;
                            display: flex;
                            align-items: center;
                            justify-content: center;

                            img {
                                width: 16px;
                                height: 16px;
                            }
                        }
                    }

                    .card-title-status {
                        ::v-deep .el-tag {
                            border-radius: 12px !important;
                            font-family: PingFang SC;
                            font-weight: bold;
                            text-align: left;
                        }
                    }
                }

                .card-bottom {
                    display: flex;
                    justify-content: space-between;
                    padding: 6px 0px;
                    flex: 1;

                    .card-bottom-left {
                        flex-shrink: 0;
                        img {
                            width: 100%;
                            max-width: 205px;
                            height: 149px;
                            background: #ffffff;
                            border-radius: 8px;
                            object-fit: cover;

                            @media (max-width: 767px) {
                                max-width: 160px;
                                height: 120px;
                            }
                        }
                    }

                    .card-bottom-right {
                        display: flex;
                        flex-direction: column;
                        flex: 1;
                        margin-right: 16px;
                        min-width: 0; // 允许收缩

                        @media (max-width: 767px) {
                            margin-right: 12px;
                        }

                        .card-description {
                            width: 100%;
                            flex: 1;
                            min-height: 42px; /* 2行高度 */
                            font-family: PingFang SC;
                            font-weight: bold;
                            font-size: 14px;
                            color: rgba(0, 0, 0, 0.85);
                            line-height: 21px;
                            text-align: left;
                            display: -webkit-box;
                            -webkit-line-clamp: 2; /* 固定显示2行 */
                            -webkit-box-orient: vertical;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            word-break: break-all;

                            @media (min-width: 768px) {
                                min-height: 64px; /* 桌面端3行高度 */
                                -webkit-line-clamp: 3; /* 桌面端显示3行 */
                            }
                        }

                        .card-bottom-text-group {
                            min-height: 52px;
                            width: 100%;
                            margin-top: 3px;
                            display: grid;
                            gap: 8px;
                            align-items: center;
                            grid-template-columns: repeat(2, 1fr);

                            .card-bottom-text {
                                flex: 1;
                                min-width: 140px;
                                height: 24px;
                                background: #f5f7fa;
                                padding: 2px 13px;
                                display: flex;
                                align-items: center;
                                border-radius: 4px;
                                img {
                                    width: 17px;
                                    height: 17px;
                                    flex-shrink: 0;
                                }

                                .card-bottom-text-name {
                                    flex: 1;
                                    padding: 0 7px;
                                    font-family: PingFang SC;
                                    font-weight: bold;
                                    font-size: 14px;
                                    color: rgba(0, 0, 0, 0.85);
                                    line-height: 26px;
                                    text-align: left;
                                    display: -webkit-box;
                                    -webkit-line-clamp: 1;
                                    -webkit-box-orient: vertical;
                                    overflow: hidden;
                                    text-overflow: ellipsis;
                                    word-break: break-all;

                                    @media (max-width: 767px) {
                                        font-size: 13px;
                                    }
                                }
                            }
                        }

                        .card-title-tags {
                            width: 100%;
                            margin-top: 7px;
                            display: flex;
                            flex-wrap: wrap;
                            gap: 8px;
                            overflow: hidden;
                            min-height: 24px;
                            .card-tag {
                                flex-shrink: 0;
                                background: #e6f7ff !important;
                                font-family: PingFang SC;
                                font-weight: bold;
                                font-size: 12px;
                                color: #2b70f4;
                                text-align: left;
                                max-width: 120px;

                                @media (max-width: 767px) {
                                    font-size: 11px;
                                    max-width: 100px;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
</style>
