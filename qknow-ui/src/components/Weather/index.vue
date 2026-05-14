<template>
    <div class="weather-wrap">
        <div class="weather-head">
            <div class="desc">当前天气</div>
            <div class="spac"></div>
            <div class="address">
                <el-icon><Location /></el-icon>
                {{ store.data.location?.name }}
            </div>
        </div>

        <div class="weather-body">
            <div class="left">
                <img class="icon" :src="getWeatherIcon(store.data.now?.text)" alt="" />
                <div class="temperature">{{ store.data.now?.temperature }}</div>
                <div class="explain">
                    <div class="label">{{ store.data.now?.text }}</div>
                    <div class="unit">℃</div>
                </div>
            </div>

            <div class="right">
                <div class="info-item">
                    <div class="label">湿度：</div>
                    <div class="value">{{ store.data.daily?.humidity }}%</div>
                </div>
                <div class="info-item">
                    <div class="label">风速：</div>
                    <div class="value">
                        {{ store.data.daily?.wind_direction }}风{{ store.data.daily?.wind_scale }}级
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup name="Weather">
    import { reactive } from 'vue';

    const store = reactive({
        data: {}
    });

    function getWeatherIcon(name) {
        let img = null;
        if (['大暴雨'].includes(name)) {
            img = '特大暴雨';
        }
        if (['小雨', '中雨', '大雨'].includes(name)) {
            img = '下雨';
        }
        if (['小雪', '中雪', '大雪'].includes(name)) {
            img = '雪';
        }
        if (['大风', '飓风', '热带风暴', '龙卷风'].includes(name)) {
            img = '风';
        }
        if (
            [
                '晴',
                '暴雨',
                '雷阵雨',
                '雷阵雨伴有冰雹',
                '特大暴雨',
                '雾',
                '阵雪',
                '暴雪',
                '雨夹雪'
            ].includes(name)
        ) {
            img = name;
        }
        img = img || '晴';
        return new URL(`../../assets/system/images/index/weather/${img}.png`, import.meta.url)
            .href;
    }

    function getData() {
        const baseUrl = 'https://api.seniverse.com/v3/weather';
        const baseParams = 'key=SjyiLD_odjCGOsHoF&location=ip&language=zh-Hans&unit=c';
        fetch(`${baseUrl}/now.json?${baseParams}`)
            .then((res) => res.json())
            .then((res) => {
                console.log(res);
                store.data.now = res.results[0].now;
                console.log(store.data);
            });
        fetch(`${baseUrl}/daily.json?${baseParams}&start=0&days=1`)
            .then((res) => res.json())
            .then((res) => {
                store.data.location = res.results[0].location;
                store.data.daily = res.results[0].daily[0];
                console.log(store.data);
            });
    }

    getData();
</script>

<style scoped lang="scss">
    .weather-wrap {
        width: 100%;
        height: 150px;
        background: url('@/assets/system/images/index/weather-bg.png') no-repeat center center;
        background-size: 100% 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    .info-item,
    .address,
    .left,
    .weather-body,
    .weather-head {
        display: flex;
        align-items: center;
    }

    .weather-head {
        line-height: 32px;
        gap: 6px;
        color: #333;
        padding: 0px 26px;
    }

    .weather-body {
        flex-wrap: wrap;
        justify-content: space-between;
        padding: 0px 26px;
        gap: 16px;
    }

    .spac {
        width: 2px;
        height: 16px;
        background-color: #c0c0c0;
    }

    .left {
        gap: 10px;
        flex: 0 0 200px;
    }

    .icon {
        width: 45px;
        height: 45px;
    }

    .temperature {
        font-size: 42px;
        line-height: 45px;
    }

    .explain {
        color: #555;
        .label {
            font-size: 20px;
            line-height: 30px;
        }

        .unit {
            font-size: 14px;
            line-height: 15px;
        }
    }

    .right {
        flex: 1;
        min-width: 100px;
        display: flex;
        flex-wrap: wrap;
    }

    .info-item {
        .label {
            font-size: 16px;
            color: #555;
        }

        .value {
            font-weight: 600;
            color: #222;
        }
    }
</style>