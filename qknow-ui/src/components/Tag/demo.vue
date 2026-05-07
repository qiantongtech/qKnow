<template>
    <div class="demo-container">
        <div class="demo-header">
            <div>
                <h2>TagPill 标签组件演示</h2>
                <p class="demo-subtitle">
                    默认显示直角标签，需显式传递 styleType="pill" 才显示圆角标签
                </p>
            </div>
            <el-button type="primary" plain @click="goToQtTag" class="nav-button">
                返回QtTag样式
            </el-button>
        </div>

        <!-- 样式类型选择 -->
        <div class="demo-section">
            <h3>1. 样式类型选择</h3>
            <div class="style-toggle">
                <el-radio-group v-model="currentStyle" @change="handleStyleChange">
                    <el-radio label="pill">圆角标签 (styleType="pill")</el-radio>
                    <el-radio label="rect">直角标签 (styleType="rect")</el-radio>
                </el-radio-group>
            </div>
        </div>

        <!-- 六种类型演示 -->
        <div class="demo-section">
            <h3>2. 六种类型标签 - {{ currentStyle === 'pill' ? '圆角样式' : '直角样式' }}</h3>
            <TagPill
                :type="type"
                :name="name"
                :styleType="currentStyle"
                v-for="(name, type) in tagTypes"
                :key="type"
            />
        </div>

        <!-- 大小控制 -->
        <div class="demo-section">
            <h3>3. 大小控制</h3>
            <div class="size-demo">
                <div class="size-item">
                    <p>大标签：</p>
                    <TagPill type="primary" name="大标签" size="large" :styleType="currentStyle" />
                </div>
                <div class="size-item">
                    <p>默认标签：</p>
                    <TagPill
                        type="primary"
                        name="默认标签"
                        size="default"
                        :styleType="currentStyle"
                    />
                </div>
                <div class="size-item">
                    <p>小标签：</p>
                    <TagPill type="primary" name="小标签" size="small" :styleType="currentStyle" />
                </div>
            </div>
        </div>

        <!-- 组合使用 -->
        <div class="demo-section">
            <h3>4. 组合使用示例</h3>
            <div class="combo-demo">
                <TagPill type="success" name="已完成" size="small" :styleType="currentStyle" />
                <TagPill type="warning" name="进行中" size="default" :styleType="currentStyle" />
                <TagPill type="danger" name="已取消" size="large" :styleType="currentStyle" />
            </div>
        </div>

        <!-- 实际应用场景 -->
        <div class="demo-section">
            <h3>5. 实际应用场景</h3>
            <div class="scenario">
                <p>任务状态：</p>
                <TagPill type="success" name="已完成" :styleType="currentStyle" />
                <TagPill type="warning" name="待处理" :styleType="currentStyle" />
                <TagPill type="danger" name="已过期" :styleType="currentStyle" />
            </div>
            <div class="scenario">
                <p>优先级：</p>
                <TagPill type="danger" name="紧急" :styleType="currentStyle" />
                <TagPill type="warning" name="高" :styleType="currentStyle" />
                <TagPill type="primary" name="中" :styleType="currentStyle" />
                <TagPill type="info" name="低" :styleType="currentStyle" />
            </div>
        </div>

        <!-- 样式对比 -->
        <div class="demo-section">
            <h3>6. 两种样式对比</h3>
            <div class="comparison">
                <div class="comparison-item">
                    <h4>圆角标签 (styleType="pill")</h4>
                    <TagPill type="primary" name="主要标签" styleType="pill" />
                    <TagPill type="success" name="成功标签" styleType="pill" />
                    <TagPill type="warning" name="警告标签" styleType="pill" />
                </div>
                <div class="comparison-item">
                    <h4>直角标签 (styleType="rect")</h4>
                    <TagPill type="primary" name="主要标签" />
                    <TagPill type="success" name="成功标签" />
                    <TagPill type="warning" name="警告标签" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { useRouter } from 'vue-router';
    import { ref } from 'vue';
    import TagPill from './index.vue';

    const router = useRouter();

    // 当前选中的样式类型
    const currentStyle = ref('rect');

    // 标签类型映射
    const tagTypes = {
        default: '默认标签',
        primary: '主要标签',
        success: '成功标签',
        warning: '警告标签',
        danger: '危险标签',
        info: '信息标签'
    };

    // 跳转到QtTag页面
    const goToQtTag = () => {
        router.push('/QtTag');
    };

    // 样式切换处理
    const handleStyleChange = (value) => {
        console.log('切换到样式:', value);
    };
</script>

<style scoped>
    .demo-container {
        padding: 20px;
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        background: #f5f7fa;
        min-height: 100vh;
    }

    .demo-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
        padding-bottom: 20px;
        border-bottom: 2px solid #409eff;
    }

    .demo-header h2 {
        color: #303133;
        margin: 0;
        text-align: left;
    }

    .demo-subtitle {
        color: #909399;
        margin: 5px 0 0 0;
        font-size: 14px;
        font-weight: normal;
    }

    .nav-button {
        margin-left: auto;
    }

    .demo-section {
        margin-bottom: 30px;
        padding: 20px;
        background: #f8f9fa;
        border-radius: 8px;
        border: 1px solid #e4e7ed;
    }

    h3 {
        color: #606266;
        margin-bottom: 15px;
        font-size: 16px;
    }

    h4 {
        color: #909399;
        margin-bottom: 10px;
        font-size: 14px;
    }

    .style-toggle {
        margin-bottom: 15px;
    }

    .tag {
        margin-right: 10px;
        margin-bottom: 10px;
    }

    .size-demo {
        display: flex;
        gap: 20px;
        align-items: center;
    }

    .size-item {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .size-item p {
        margin: 0;
        color: #606266;
        font-weight: 500;
        min-width: 80px;
    }

    .combo-demo {
        display: flex;
        gap: 15px;
        align-items: center;
    }

    .scenario {
        margin-bottom: 15px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .scenario p {
        margin: 0;
        color: #606266;
        font-weight: 500;
        min-width: 80px;
    }

    .comparison {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 30px;
    }

    .comparison-item {
        padding: 15px;
        background: #ffffff;
        border-radius: 6px;
        border: 1px solid #dcdfe6;
    }

    /* 响应式设计 */
    @media (max-width: 768px) {
        .demo-container {
            padding: 10px;
        }

        .size-demo {
            flex-direction: column;
            align-items: flex-start;
            gap: 10px;
        }

        .combo-demo {
            flex-direction: column;
            align-items: flex-start;
            gap: 10px;
        }

        .scenario {
            flex-direction: column;
            align-items: flex-start;
        }

        .scenario p {
            margin-bottom: 5px;
        }

        .comparison {
            grid-template-columns: 1fr;
            gap: 20px;
        }
    }
</style>
