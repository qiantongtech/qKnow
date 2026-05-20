<template>
    <div class="weather-wrap">
        <div class="weather-head">
            <!-- <div class="desc">当前天气</div>
            <div class="spac"></div> -->
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
                    <div class="unit">℃</div>
                    <div class="label">{{ store.data.now?.text }}</div>
                </div>
            </div>

            <div class="right">
                <div class="info-item">
                    <div class="iconbox">
                        <svg
                            class="tqicon"
                            width="16px"
                            height="16.00px"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                d="M518.826667 85.504c-14.08-2.432-29.482667 3.413333-39.936 16-205.226667 246.186667-265.386667 372.906667-265.386667 538.666667a298.666667 298.666667 0 1 0 597.333333 0c0-60.842667-13.568-107.392-43.946666-155.989334-13.056-20.906667-78.08-106.453333-96-133.376C626.773333 284.672 588.8 211.2 552.234667 113.493333a44.245333 44.245333 0 0 0-33.365334-27.989333z m-107.946667 343.978667c10.410667-3.328 21.546667-4.053333 32 1.365333a42.581333 42.581333 0 0 1 18.645333 57.344c-27.989333 54.144-37.162667 106.666667-33.322666 150.613333a42.965333 42.965333 0 0 1-38.656 46.677334 42.965333 42.965333 0 0 1-46.677334-38.656c-5.205333-59.562667 6.997333-128.341333 42.666667-197.333334 5.418667-10.453333 14.933333-16.682667 25.344-20.053333z"
                                fill="#4f8efc"
                            />
                        </svg>
                    </div>

                    <div class="label">湿度：</div>
                    <div class="value valueunit">{{ store.data.daily?.humidity }}%</div>
                </div>
                <div class="info-item border">
                    <div class="iconbox">
                        <svg
                            class="tqicon"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                d="M174.5 394.1h331.2c91 0 166-73.2 166.3-164.2 0.3-91-73.7-165.1-164.7-165.1-43.2 0-84 16.6-114.9 46.7-15.5 15.1-27.7 32.9-36.2 52.2-12.7 29.1 8.4 61.7 40.1 62.6 18.5 0.5 35.2-10.4 42.6-27.3 11.6-26.2 37.7-44.2 68.3-44.2 41.4 0 75.1 33.9 74.7 75.4-0.4 41.1-34.5 73.9-75.6 73.9H174.5c-24.9 0-45 20.1-45 45s20.2 45 45 45zM189.3 634.2l0.6 45-0.6-45zM682 626.9c-0.6 0-0.8 0-427.3 6.3-7.3 0.1-14.2 0.2-20.5 0.3-24.8 0.4-44.7 20.8-44.3 45.6 0.3 24.9 20.8 44.8 45.7 44.4 6.3-0.1 13.2-0.2 20.5-0.3 124.9-1.9 415.3-6.2 426.2-6.3 42.8 0.2 77.2 36.5 74.2 80-2.8 39.8-35.9 70-75.7 69.3-30.2-0.5-55.9-18.5-67.2-44.5-7.3-16.8-24.1-27.5-42.5-27-31.7 0.9-52.8 33.5-40.1 62.6 8.4 19.4 20.6 37.1 36.2 52.2 30.9 30.1 71.8 46.7 115 46.7 91.8-0.1 166.8-77 164.5-168.8-2.3-88.9-75.3-160.5-164.7-160.5z"
                                fill="#4f8efc"
                            />
                            <path
                                d="M856.6 240.3c-29-11.5-60.5 10.1-60.5 41.4v1.3c0 18 10.9 34.4 27.6 41.1 27.5 11 47 38 47 69.4 0 41.2-33.5 74.7-74.7 74.7H112.6c-24.9 0-45 20.1-45 45s20.1 45 45 45h683.5c90.8 0 164.7-73.9 164.7-164.7-0.1-69.5-43.3-129-104.2-153.2z"
                                fill="#4f8efc"
                            />
                        </svg>
                    </div>
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
        return new URL(`../../assets/system/images/index/weather/${img}.png`, import.meta.url).href;
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
    .iconbox {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 25px;
        height: 25px;
        background-color: #f3f9ff;
        border-radius: 50%;
        margin-right: 10px;
    }
    // .item-unit {
    //     margin-top: 15px;
    // }
    .weather-head {
        line-height: 32px;
        gap: 10px;
        color: #333;
        padding: 0px 20px;
    }

    .weather-body {
        flex-wrap: wrap;
        justify-content: space-between;
        padding: 12px 20px;
        gap: 16px;
    }

    .spac {
        width: 2px;
        height: 16px;
        background-color: #c0c0c0;
    }

    .left {
        gap: 10px;
        flex: 0 0 188px;
        border-right: 1px solid #d1e1fa;
    }

    .icon {
        width: 52px;
        height: 52px;
    }
    .tqicon {
        width: 15px;
        height: 15px;
    }

    .temperature {
        font-size: 48px;
        line-height: 55px;
    }

    .explain {
        color: #555;
        .label {
            font-size: 12px;
            line-height: 30px;
        }

        .unit {
            font-size: 18px;
            line-height: 23px;
        }
    }

    .right {
        flex: 1;
        min-width: 100px;
        display: flex;
        flex-wrap: wrap;
        margin-left: 15px;
    }

    .info-item {
        font-size: 12px;
        .label {
            font-size: 12px;
            color: #868d98;
        }
        .value {
            width: 75px;
            color: #222;
            text-align: right;
        }
        .valueunit {
            font-size: 18px;
        }
    }
    .border {
        position: relative;
        margin-top: 10px;
        padding-top: 10px;

        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 35px;
            right: 0;
            height: 1px;
            background-color: #d1e1fa;
        }
    }
    .tqimg {
        margin-right: 10px;
    }
    .el-icon {
        width: 1.2em;
        height: 1.2em;
        margin-right: 5px;
    }
</style>
