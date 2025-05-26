<template>
  <div class="app-container stagingIndex">
    <el-row :gutter="15">
      <el-col :xs="24" :sm="24" :md="18" :lg="18" class="home-gutter">
        <div class="userInfo module-1">
          <div class="info-main">
            <img :src="userStore.avatar" alt="" class="avatar" />
            <div class="info-con">
              <div class="info-con-name">上午好，{{ userStore.nickName }} ，祝你开心每一天！</div>
              <div class="info-con-desc">
                <span style="color: var(--el-color-primary)"> 系统管理员 </span>
                <el-divider direction="vertical" />
                {{ xljtcont }}
                <!-- 不要再纠缠于“长期”和“短期”，很多时候长期已经成为了现在不做事情的借口 -->
              </div>
            </div>
            <div class="info-btns">
              <!-- <a href="/user/profile" class=""> -->
              <el-button type="primary" class="info-btn-dft" plain size="large" @click="goprofile"> 个人中心 </el-button>
              <!-- </a> -->

              <!--                            <el-button type="primary" class="info-btn-dft" plain size="large">
                                退出登录
                            </el-button>-->
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="6" :lg="6" class="home-gutter">
        <!-- 模块2 -->
        <!-- <div class="module-2">
                    <img src="@/assets/system/images/index/weather.png" alt="" srcset="" />
                </div> -->
        <div class="module-2">
          <iframe
            width="100%"
            scrolling="no"
            height="150"
            frameborder="0"
            allowtransparency="true"
            src="https://i.tianqi.com?c=code&id=21&icon=1&site=12"
            ></iframe>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :md="18" :lg="18" class="home-gutter">
        <!-- 模块3 -->
        <div class="module-3">
          <div class="module-item" v-for="(item, index) in module1" :key="index">
            <!-- <router-link to="/sy/index"> -->
            <div class="module-item-t">
              <div class="module-item-t-l">
                <div class="name">{{ item.name }}</div>
                <span>{{ item.value }}</span>
              </div>
              <img :src="item.img" class="module-item-t-r" />
            </div>
            <!-- </router-link> -->

            <div class="module-item-border"></div>
            <div class="module-item-data">
              <span>周同比：</span>
              <span :class="[item.up ? 'data-up' : 'data-down']">{{ item.speed }}% </span>
            </div>
          </div>
        </div>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :md="12" :lg="12">
            <!-- 模块4 饼图 -->
            <div class="module-4 border-item">
              <div class="border-item-head">
                <span class="head-title">文件类型统计 </span>
              </div>
              <div class="border-item-body">
                <div class="chart-container" ref="module4ChartRef"></div>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12">
            <!-- 模块3 柱状图 -->
            <div class="module-5 border-item">
              <div class="border-item-head">
                <span class="head-title">近7日抽取数量统计</span>
              </div>
              <div class="border-item-body">
                <div class="chart-container" ref="module5ChartRef"></div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-col>

      <el-col :xs="24" :sm="24" :md="6" :lg="6" class="home-gutter">
<!--        <div class="border-item module-report">-->
<!--          &lt;!&ndash;                    <el-button class="button" type="primary" @click="changeReport">&ndash;&gt;-->
<!--          &lt;!&ndash;                        报工&ndash;&gt;-->
<!--          &lt;!&ndash;                    </el-button>&ndash;&gt;-->
<!--          <el-button class="report-button" type="primary" icon="Tickets" plain @click="changeReport" @mousedown="(e) => e.preventDefault()">-->
<!--            <p>&nbsp;报&nbsp;工</p>-->
<!--            &lt;!&ndash;                        <img src="@/assets/icons/svg/报工统计.svg" style="width: 22px;" alt="" />&nbsp;报工&ndash;&gt;-->
<!--          </el-button>-->
<!--        </div>-->
        <div class="border-item module-6 home-gutter">
          <div class="border-item-head">
            <span class="head-title">新闻公告 </span>
            <el-link type="primary" :underline="false" @click="goxinwen">查看更多 </el-link>
          </div>
          <div class="border-item-body">
            <div class="module-item" v-for="(item, index) in module6" :key="index" @click="goxinwen">
              <dict-tag :options="sys_notice_type" :value="item.noticeType" />
              <div class="value" :title="item.noticeTitle">{{ item.noticeTitle }}</div>
              <div class="time">{{ parseTime(item.createTime, "{y}-{m}-{d}") }}</div>
            </div>
          </div>
        </div>
        <div class="border-item module-7">
          <div class="border-item-head">
            <span class="head-title">千知平台 </span>
            <el-link type="primary" :underline="false">查看更多 </el-link>
          </div>
          <div class="border-item-body">
            <!-- <el-carousel
                            trigger="click"
                            ref="carousel"
                            arrow="always"
                            style="height: 100%"
                        >
                            <el-carousel-item v-for="item in 4" :key="item">
                                <div class="module-item">
                                    <img
                                        src="@/assets/system/images/login/login-background.jpg"
                                        style="object-fit: cover; width: 100%; height: 100%"
                                    />
                                </div>
                            </el-carousel-item>
                        </el-carousel> -->
            <button class="qhbut" @click="prevSlide">{{ "<" }}</button>
            <div class="carouselcont">
              <el-carousel ref="carousel" arrow="always" style="height: 100%" indicator-position="none">
                <el-carousel-item v-for="item in 4" :key="item">
                  <img src="@/assets/system/images/login/logo.png" style="object-fit: contain; width: 100%; height: 100%" />
                </el-carousel-item>
              </el-carousel>
            </div>

            <button class="qhbut" @click="nextSlide">{{ ">" }}</button>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :md="9" :lg="9">
        <div class="module-8 border-item">
          <div class="border-item-head">
            <span class="head-title">近半年实体新增趋势 </span>
            <el-link type="primary" :underline="false">查看更多 </el-link>
          </div>
          <div class="border-item-body">
            <div class="chart-container" ref="module8ChartRef"></div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :md="15" :lg="15">
        <div class="module-9 border-item">
          <div class="border-item-head">
            <span class="head-title">抽取任务 </span>
            <el-link type="primary" :underline="false">查看更多 </el-link>
          </div>
          <div class="border-item-body">
            <el-table :data="module9" style="width: 100%" height="100%">
              <el-table-column fixed prop="id" label="id" align="center" width="80">
                <template #default="scope">
                  <div class="table-column-code">{{ scope.row.id }}</div>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="任务名称" width="200"/>
              <el-table-column prop="status" align="center" label="任务状态" />
              <el-table-column prop="publishStatus" align="center" label="发布状态" />
              <el-table-column prop="publishBy" align="center" label="发布人" />
              <el-table-column prop="createTime" label="创建时间" width="180"/>
            </el-table>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
<!--  <ReportAddOrEdit v-show="open" v-model:open="open" :title="reportTitle" :itemId="reportItemId" @close-dialog="handleChildClose"></ReportAddOrEdit>-->
</template>

<script setup name="Index">
import useUserStore from "@/store/system/user";
import { listNotice } from "@/api/system/system/notice.js";
import useAppStore from "@/store/system/app";
import * as echarts from "echarts";
// eslint-disable-next-line no-unused-vars
import { onBeforeUnmount, onMounted, ref, watch, getCurrentInstance } from "vue";

let { proxy } = getCurrentInstance();
const { sys_notice_type } = proxy.useDict("sys_notice_type");
const getAssetsFile = (url) => {
  return new URL(`../../assets/system/images/index/${url}`, import.meta.url).href;
};

const carousel = ref(null);

const prevSlide = () => {
  carousel.value.prev();
};

const nextSlide = () => {
  carousel.value.next();
};

//------------报工---------------------------
const open = ref(false);
const reportTitle = ref("报工");
const reportItemId = ref(null);

// 点击报工
function changeReport() {
  open.value = true;
  console.log("打开或者关闭弹窗状态", open.value);
}

// 父组件接收子组件发出的事件
const handleChildClose = () => {
  open.value = false;
  console.log("子组件的弹窗关闭了");
  // 在这里执行关闭弹窗后需要进行的逻辑
};
//------------报工---------------------------

const chartIntances = [];
// eslint-disable-next-line no-unused-vars
const appStore = useAppStore();
const userStore = useUserStore();
const module1 = ref([
  {
    name: "实体总数",
    value: 126,
    up: true,
    speed: 12,
    img: getAssetsFile("1.png"),
  },
  {
    name: "关系总数",
    value: 72,
    up: true,
    speed: 2,
    img: getAssetsFile("2.png"),
  },
  {
    name: "三元组总数",
    value: 164,
    up: true,
    speed: 9,
    img: getAssetsFile("3.png"),
  },
  {
    name: "文件总数",
    value: 76,
    up: true,
    speed: 10,
    img: getAssetsFile("4.png"),
  },
  {
    name: "抽取任务总数",
    value: 18,
    up: false,
    speed: 10,
    img: getAssetsFile("5.png"),
  },
]);

//新闻跳转
function goxinwen() {
  proxy.$router.push("/system/notice"); // 内部页面路径
}

function goprofile() {
  proxy.$router.push("/user/profile"); // 内部页面路径
}
//获取心灵鸡汤内容
const xljtcont = ref("");
function getxljtcont() {
  let num = Math.floor(Math.random() * 9);
  let xljtlist = [
    { value: "起风的日子，学会依风起舞，下雨的是时候，学会为自己撑伞。" },
    { value: "别让鸡零狗碎的破事，耗尽你对美好生活的所有向往。" },
    { value: "管好身体，照顾好父母和老婆孩子，要自己感受生活的美好。" },
    { value: "我希望你过普通的生活，有稳定的收入平凡的爱人。" },
    { value: "一定要努力赚钱，好好经营自己。" },
    { value: "我们穷极一生追求的幸福，眼中景，盘中餐，身边人。" },
    { value: "日出有盼，日落有思，平平安安，所遇皆甜。" },
    { value: "不要慌，太阳下山有月光。" },
    { value: "几经波折见风雪，再见是我也非我。" },
  ];
  xljtcont.value = xljtlist[num].value;
}

const module4ChartRef = ref(null);
function initModule4() {
  const intance = echarts.init(module4ChartRef.value, "macarons");
  let m2R2Data = [
    // {
    //     value: 335,
    //     legendname: '种类06',
    //     name: '种类06  335'
    //     // itemStyle: { color: '#fca4bb' }
    // },
    // {
    //     value: 335,
    //     legendname: '种类07',
    //     name: '种类07  335'
    //     // itemStyle: { color: '#f59a8f' }
    // },
    {
      value: 130,
      legendname: "种类08",
      name: "种类08",
      // itemStyle: { color: '#fdb301' }
    },
    {
      value: 150,
      legendname: "种类09",
      name: "种类09",
      // itemStyle: { color: '#57e7ec' }
    },
    {
      value: 100,
      legendname: "种类10",
      name: "种类10",
      // itemStyle: { color: '#cf9ef1' }
    },
    {
      value: 190,
      legendname: "种类11",
      name: "种类11",
      // itemStyle: { color: '#57e7ec' }
    },
    {
      value: 200,
      legendname: "种类12",
      name: "种类12",
      // itemStyle: { color: '#cf9ef1' }
    },
  ];

  let option = {
    title: [
      // {
      //     text: '标题',
      //     textStyle: {
      //         fontSize: 16,
      //         color: 'black'
      //     },
      //     left: '2%'
      // },
      {
        text: "100%",
        // subtext: 12312 + '个',
        textStyle: {
          fontSize: 30,
          color: "rgba(0,0,0,0.65)",
          fontFamily: "Sharp",
        },
        // subtextStyle: {
        //     fontSize: 20,
        //     color: 'black'
        // },
        textAlign: "center",
        x: "34.5%",
        y: "43%",
      },
    ],
    tooltip: {
      trigger: "item",
      formatter: function (parms) {
        let str =
          parms.seriesName +
          "</br>" +
          parms.marker +
          "" +
          parms.data.legendname +
          "</br>" +
          "数量：" +
          parms.data.value +
          "</br>" +
          "占比：" +
          parms.percent +
          "%";
        return str;
      },
    },
    legend: {
      type: "scroll",
      orient: "vertical",
      left: "75%",
      align: "left",
      top: "10%",
      // top: 'middle',
      itemWidth: 8, // 设置图例项的宽度
      itemHeight: 8, // 设置图例项的高度
      icon: "circle", // 设置图例图标为圆形
      // textStyle: {
      //     color: '#8C8C8C',
      //     fontSize: 14,
      //     lineHeight: 20 // 设置行高，以确保文本垂直居中
      // },

      formatter: function (name) {
        // 找到对应的项并计算百分比
        console.log(name, "============name");
        let total = 770;
        let item = m2R2Data.find((item) => item.name === name);
        // eslint-disable-next-line no-unused-vars
        let percent = item ? ((item.value / total) * 100).toFixed(2) + "%" : "0%";
        // return `${name} |  ${percent}  ${item.value}`; // 自定义图例显示
        // 使用 HTML 语法自定义颜色
        let arr = [
          "{a|" + name + "}",
          // '{b|' + '|   ' + percent + '}',
          // '{c|' + item.value + '}'
        ];
        return arr.join("  ");
        // return `${name}: <span style="color: red;">${percent}</span> (<span style="color: blue;">${percent}</span>)`;
      },
      textStyle: {
        lineHeight: 25, // 设置行高，以确保文本垂直居中

        // 添加
        padding: [0, 0, 0, 0],
        rich: {
          a: {
            fontSize: 14,
            width: 46,
          },
          b: {
            fontSize: 14,
            width: 70,
            color: "#888888",
          },
          c: {
            fontSize: 14,
            color: "rgba(0,0,0,0.65)",
          },
        },
      },
      // height:250
    },
    series: [
      {
        name: "标题",
        type: "pie",
        center: ["35%", "50%"],
        radius: ["35%", "55%"],
        clockwise: false, //饼图的扇区是否是顺时针排布
        avoidLabelOverlap: false,
        // label: {
        //     normal: {
        //         // show: true,
        //         // position: 'outter',
        //         formatter: function (parms) {
        //             return parms.data.value;
        //         }
        //     }
        // },
        label: {
          formatter: "{d}%", // 显示每个部分的名称和占比
          position: "outside", // 标签位置
        },
        itemStyle: {
          normal: {
            color: function (params) {
              let colorList = [
                {
                  c1: "#1E60FB",
                  c2: "#5D8EFE",
                },
                {
                  c1: "#6CD8D0",
                  c2: "#1DC7B5",
                },
                {
                  c1: "#F9D370",
                  c2: "#F7BD26",
                },
                {
                  c1: "#B28AE9",
                  c2: "#9358E3",
                },
                {
                  c1: "#EA7283",
                  c2: "#F53D57",
                },
              ];
              return new echarts.graphic.LinearGradient(1, 0, 0, 0, [
                {
                  //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                  offset: 0,
                  color: colorList[params.dataIndex].c1,
                },
                {
                  offset: 1,
                  color: colorList[params.dataIndex].c2,
                },
              ]);
            },
          },
        },
        data: m2R2Data,
      },
    ],
  };
  intance.setOption(option);
  chartIntances.push(intance);
  //  // 窗口大小变化时，自动更新图表大小
  // window.addEventListener('resize', function() {
  //     intance.resize();
  // });
}

const module5ChartRef = ref(null);
function initModule5() {
  const intance = echarts.init(module5ChartRef.value, "macarons");
  intance.setOption({
    grid: {
      top: "15%",
      bottom: "10%",
      right: "5%",
    },
    legend: {
      show: true,
      itemGap: 50,
      data: ["抽取实体数量"],
      icon: "circle",
      itemWidth: 8, // 设置图例项的宽度
      itemHeight: 8, // 设置图例项的高度
      right: "5%",
      top: "1%",
      textStyle: {
        color: "rgba(0,0,0,0.65)",
        fontSize: 15,
        lineHeight: 30, // 设置行高，以确保文本垂直居中
      },
    },
    xAxis: {
      type: "category",
      data: ["05-01", "05-02", "05-03", "05-04", "05-05", "05-06", "05-07"],
    },
    yAxis: {
      type: "value",
      max: 1000, // 设置 Y 轴最大值为 1000
    },
    series: [
      {
        type: "bar",
        name: "抽取实体数量",
        barWidth: 30,
        itemStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1,
              [
                {
                  offset: 0,
                  color: "#5b8dfe",
                },
                {
                  offset: 1,
                  color: "#2263fc",
                },
              ],
              false
            ),
          },
        },
        data: [800, 550, 740, 450, 800, 730, 600],
      },
    ],
  });
  chartIntances.push(intance);
}

const module6 = ref([
  {
    date: "2016-05-05",
    title: "置顶",
    value: "XXXXXxxx科技有限公司",
    type: "danger",
  },
  {
    date: "2016-05-03",
    title: "新闻",
    value: "XXXXXxxx科技有限公司",
    type: "success",
  },
  {
    date: "2016-05-02",
    title: "其他",
    value: "XXXXXxxx科技有限公司",
    type: "primary",
  },
  {
    date: "2016-05-04",
    title: "其他",
    value: "XXXXXxxx科技有限公司",
    type: "info",
  },
  {
    date: "2016-05-06",
    title: "公告",
    value: "XXXXXxxx科技有限公司",
    type: "warning",
  },
]);

function initModule6() {
  let query = {
    pageNum: 1,
    pageSize: 5,
  };
  listNotice(query).then((response) => {
    module6.value = response.rows;
  });
}

const module8ChartRef = ref(null);
function initModule8() {
  const intance = echarts.init(module8ChartRef.value, "macarons");
  intance.setOption({
    // title: [
    //     {
    //         text: '告警趋势',
    //         textStyle: {
    //             fontSize: 14,
    //             color: 'rgba(0,0,0,0.65)'
    //         },
    //         right: '2%'
    //     }
    // ],
    legend: {
      show: true,
      itemGap: 50,
      data: ["实体新增趋势"],
      icon: "circle",
      itemWidth: 6, // 设置图例项的宽度
      itemHeight: 6, // 设置图例项的高度
      right: "5%",
      top: "1%",
      textStyle: {
        color: "rgba(0,0,0,0.65)",
        fontSize: 15,
        lineHeight: 30, // 设置行高，以确保文本垂直居中
      },
    },
    grid: {
      top: "18%",
      bottom: "10%",
      right: "5%",
    },
    xAxis: {
      type: "category",
      data: ["25年1月", "25年2月", "25年3月", "25年4月", "25年5月", "25年6月"],
    },
    yAxis: {
      type: "value",
      max: 1000, // 设置 Y 轴最大值为 1000
    },
    series: [
      {
        name: "告警趋势",
        lineStyle: {
          normal: {
            color: "#5285fd",
          },
        },
        // itemStyle: {
        //     color: '#3571FC',
        //     borderColor: '#3571FC'
        // },

        // symbol: 'circle', // 数据点形状
        symbolSize: 8, // 数据点大小
        itemStyle: {
          color: "#427afd",
          borderColor: "#427afd", // 边框颜色
          borderWidth: 1, // 边框宽度
        },

        areaStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1,
              [
                {
                  offset: 0,
                  color: "rgba(204, 220, 254,1)",
                },
                {
                  offset: 1,
                  color: "rgba(204, 220, 254,0)",
                },
              ],
              false
            ),
            // shadowColor: 'rgba(204, 220, 254,0.1)',
            // shadowBlur: 20
          },
        },
        data: [250, 100, 780, 60, 760, 200],
        type: "line",
      },
    ],
  });
  chartIntances.push(intance);
}

const module9 = ref([
  {
    id: "1",
    name: "抽取任务名称一",
    status: "进行中",
    publishStatus: "已发布",
    publishBy: "千知",
    createTime: "2025-05-01 11:23:43",
  },
  {
    id: "2",
    name: "抽取任务名称二",
    status: "已完成",
    publishStatus: "已发布",
    publishBy: "千知",
    createTime: "2025-05-01 11:23:43",
  },
  {
    id: "3",
    name: "抽取任务名称三",
    status: "进行中",
    publishStatus: "已发布",
    publishBy: "千知",
    createTime: "2025-05-01 11:23:43",
  },
  {
    id: "4",
    name: "抽取任务名称四",
    status: "已完成",
    publishStatus: "未发布",
    publishBy: "千知",
    createTime: "2025-05-01 11:23:43",
  },
  {
    id: "5",
    name: "抽取任务名称五",
    status: "进行中",
    publishStatus: "已发布",
    publishBy: "千知",
    createTime: "2025-05-01 11:23:43",
  },
]);

function chartIntancesResize() {
  console.log(chartIntances);
  chartIntances.forEach((intance) => {
    intance.resize();
  });
}

window.addEventListener("resize", chartIntancesResize);

onBeforeUnmount(() => {
  window.removeEventListener("resize", chartIntancesResize);
});

// 获取当前实例
const instance = getCurrentInstance();

// 在组件销毁时移除事件监听
onBeforeUnmount(() => {
  instance.appContext.config.globalProperties.$bus.off("getsidebarStatus", callback);
});

onMounted(() => {
  initModule4();
  initModule5();
  initModule6();
  initModule8();
  getxljtcont();
  instance.appContext.config.globalProperties.$bus.on("getsidebarStatus", () => {
    window.addEventListener("resize", chartIntancesResize);
  });
});
</script>

<style scoped lang="scss">
.home-gutter {
  position: relative;
}

.work-time-div {
  width: 85%;
  height: 99%;
  position: absolute;
  /* top: 20px; */
  right: 20px;
  background-color: #ffffff;
  /* color: white; */
  /* font-size: 18px; */
  /* font-weight: bold; */
  /* padding: 30px 30px; */
  border-radius: 2px;
  text-align: center;
  /* cursor: pointer; */
  /* box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); */
  /* transition: transform 0.3s, box-shadow 0.3s;
        }

        .work-time-div:hover {
            /*transform: scale(1.05);*/
  /*box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);*/
}

.work-time-div:active {
  transform: scale(1);
}

.stagingIndex {
  min-width: 1290px;
  max-width: 100%;
  // width: 100%;
  height: 100%;
  // padding: 24px;
  background: #f0f2f5;

  ::v-deep .el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
    .el-carousel__arrow--left {
      display: none;
    }
    .el-carousel__arrow--right {
      display: none;
    }
  }
}

.module-4,
.module-5,
.module-8,
.module-9 {
  height: 350px !important;
//   box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
}

.module-6,
.module-7 {
  height: 250px !important;
//   box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
}
.module-report {
  margin-bottom: 15px;
  height: 60px !important;
}
.module-report .report-button {
  width: 100%;
  height: 60px;
  font-size: 22px;
  border-radius: 2px;
}

.home-gutter {
  margin-bottom: 15px;
}

.userInfo {
  height: 150px;
  padding: 35px 60px 0 32px;
  background-image: -webkit-gradient(linear, left top, right top, from(#fff), to(#f3f7fe));
  background-image: linear-gradient(90deg, #fff, #f3f7fe);
  border-radius: 2px;
  justify-content: space-between;
  align-items: center;
//   box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
}

.userInfo .menu {
  margin-bottom: 16px;
}

.userInfo .menu .el-breadcrumb__inner.is-link {
  font-family: PingFang SC;
  font-size: 14px;
  color: #888888;
  line-height: 22px;
  font-weight: 400;
}

.userInfo .menu .el-breadcrumb__inner {
  font-family: PingFang SC;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.65);
  text-align: left;
  line-height: 22px;
  font-weight: 400;
}

.userInfo .menu .el-breadcrumb__separator {
  font-size: 14px;
  color: #888888;
}

.userInfo .info-main {
  min-width: 100%;
  display: flex;
  position: relative;
  flex-shrink: 0;
}

.userInfo .info-main .avatar {
  width: 72px;
  height: 72px;
  margin-right: 24px;
  border-radius: 50%;
}

.userInfo .info-main .info-con {
  height: 72px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.userInfo .info-main .info-con .info-con-name {
  font-family: PingFang SC;
  font-size: 20px;
  color: rgba(0, 0, 0, 0.85);
  line-height: 28px;
  font-weight: 600;
}

.userInfo .info-main .info-con .info-con-desc {
  font-family: PingFang SC;
  font-size: 14px;
  color: #888888;
  line-height: 22px;
  font-weight: 400;
}

.userInfo .info-main .info-btns {
  position: absolute;
  top: 18px;
  right: 0;
  font-family: PingFang SC;
  font-size: 14px;
  font-weight: 500;
}

.userInfo .info-main .info-btns .info-btn {
  width: 100px;
  height: 40px;
  border-radius: 4px !important;
  border: none !important;
}

.userInfo .info-main .info-btns .info-btn-dft {
  width: 100px;
  height: 40px;
  border-radius: 4px !important;
  margin-left: 20px;
}

.module-2 {
  height: 150px;
  // background-color: #fff;
  border-radius: 2px;
  text-align: center;
//   box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);

  img {
    height: 100%;
  }
}

.module-2 #mobile12 .wtleft {
  width: 30%;
}

.module-3 {
  display: flex;

  justify-content: space-between;
  height: 150px;
  margin-bottom: 15px;
}

.module-3 .module-item {
  // width: 18.3%;
  // min-width: 185px;
  flex: 1;
  height: 100%;
  border-radius: 2px;
  padding: 20px;
  background: #fff;
  margin-right: 15px;
//   box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
}
.module-3 .module-item:last-of-type {
  margin-right: 0px;
}

.module-3 .module-item .module-item-t {
  display: flex;

  align-items: center;

  justify-content: space-between;
}

.module-3 .module-item .module-item-t .module-item-t-l {
  font-family: PingFang SC;
  font-size: 14px;
  color: #888888;
  line-height: 22px;
  font-weight: 400;
}

.module-3 .module-item .module-item-t .module-item-t-l .name {
  margin-bottom: 12px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.module-3 .module-item .module-item-t .module-item-t-l span {
  font-family: Sharp;
  // font-size: 34px;
  font-size: 30px;
  color: #000;
}

.module-3 .module-item .module-item-t .module-item-t-r {
  width: 48px;
  height: 48px;
}

.module-3 .module-item .module-item-border {
  height: 1px;
  background: #e2e2e2;
  margin: 20px 0 12px;
}

.module-3 .module-item .module-item-data {
  display: flex;

  align-items: center;
  // color: #666;
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
}

.module-3 .module-item .module-item-data .data-up {
  font-size: 14px;
  margin: 0 10px;
  // color: #000;
  color: rgba(0, 0, 0, 0.85);
  position: relative;
}

.module-3 .module-item .module-item-data .data-up:after {
  content: "";
  position: absolute;
  top: -2px;
  right: -16px;
  width: 0;
  height: 12px;
  border: 6px solid #f5222d;
  border-color: transparent transparent #f5222d transparent;
}

.module-3 .module-item .module-item-data .data-down {
  font-size: 14px;
  margin: 0 10px;
  color: #000;
  position: relative;
}

.module-3 .module-item .module-item-data .data-down:after {
  content: "";
  position: absolute;
  top: 4px;
  right: -16px;
  width: 0;
  height: 12px;
  border: 6px solid #f5222d;
  border-color: #60f522 transparent transparent transparent;
}

.module-3 .module-item .module-item-data .data-equal {
  font-size: 14px;
  margin: 0 10px;
  color: #000;
  position: relative;
}

.border-item {
  width: 100%;
  height: 100%;
  background: #fff;
  border-radius: 2px;
}

.border-item .border-item-head {
  height: 50px;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e8e8e8;
}

.border-item .border-item-head .head-title {
  font-size: 16px;
  color: var(--el-color-primary);
  font-weight: 700;
  display: flex;
  align-items: center;
}
.border-item .border-item-head .head-title-seach {
  cursor: pointer;
  font-size: 14px;
  color: #0f62ff;
  line-height: 0px;
  font-style: normal;
}

.border-item .border-item-head .head-title:before {
  display: inline-block;
  content: "";
  width: 6px;
  height: 16px;
  border-radius: 2px 2px 2px 2px;
  background: var(--el-color-primary);
  margin-right: 10px;
}

.border-item .border-item-body {
  height: calc(100% - 50px);
  position: relative;
}

.border-item .border-item-body .chart-container {
  height: 100%;
}

.border-item .border-item-body .border-item-order {
  display: flex;
  justify-content: space-between;
  padding: 12px 20px 0 20px;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.65);
  line-height: 22px;
  font-weight: 400;
}

.border-item .border-item-body .border-item-order .name-span {
  width: 225px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
}

.border-item .border-item-body .border-item-order .name-span:hover {
  color: #409eff;
}

.border-item .border-item-body .border-item-order .time-span {
  margin-left: 20px;
  width: 60px;
  min-width: 60px;
  height: 22px;
  font-size: 14px;
  font-family: HelveticaNeue;
  color: #888888;
  line-height: 22px;
}

.border-item .border-item-body .border-item-order .top {
  width: 50px;
  height: 22px;
  background: rgba(255, 0, 25, 0.06);
  border-radius: 2px;
  color: #ff0019;
}

.border-item .border-item-body .border-item-order .hui-span {
  background: #f0f2f5 !important;
  color: rgba(0, 0, 0, 0.65) !important;
}

.module3-item {
  background-color: #f7f7f7;
}

.module-6 {
  .border-item-body {
    overflow-y: auto;
    overflow-x: hidden;
    padding: 12px 0px;
  }
  .module-item {
    display: flex;
    align-items: center;
    padding: 6px 20px;
    cursor: pointer;
  }
  // .value,
  // .title {
  //     margin: 0 10px;
  // }

  .value {
    margin: 0 10px;
    font-size: 14px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    flex: 1;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.65);
  }

  .time {
    width: 90px;
    text-align: right;
    font-size: 14px;
    color: #888888;
  }
}

.module-7 {
  .border-item-body {
    padding: 21px 0px;
    display: flex;
    align-items: center;
    justify-content: center;
    .carouselcont {
      height: 100%;
      width: calc(100% - 84px);
      margin-left: 8px;
      margin-right: 8px;
    }
    .qhbut {
      width: 21px;
      height: 21px;
      background: var(--el-color-primary);
      border-radius: 50%;
      border: 0px;
      color: #ffffff;
      text-align: center;
    }
  }
  .module-item {
    height: 100%;
    // background-color: #f6f6f6;
    // width: 100%;
    // height: 100%;
    // display: flex;
    // justify-content: center;
  }
  // ::v-deep {
  //     // .el-carousel__arrow {
  //     //     width: 21px;
  //     //     height: 21px;
  //     // }
  //     .el-carousel__arrow--left {
  //         display: none;
  //     }
  //     .el-carousel__arrow--right {
  //         display: none;
  //     }
  // }
}

.module-9 {
  // ::v-deep .el-table {
  //     thead .el-table__cell.is-leaf {
  //         background-color: #fff !important;
  //     }
  // }
  ::v-deep {
    // 列表表头
    .el-table thead {
      height: 53px;
      .el-table__cell.is-leaf {
        background: rgba(19, 90, 251, 0.04) !important;
        // border-radius: 4px 4px 0px 0px;
      }
    }
  }
}
.module-9 {
  .border-item-body {
    padding: 15px 34px 15px 34px;
  }
  .table-column-code {
    font-size: 14px;
    color: #135afb;
  }
}
</style>
<style lang="scss" scoped>
@media screen and (max-width: 1280px) {
}
@media screen and (max-width: 992px) {
  .stagingIndex {
    min-width: 100%;
    padding: 12px;
    .userInfo {
      height: 100px;
      padding: 15px 15px;
      .info-main {
        align-items: center;
        .avatar {
          width: 48px;
          height: 48px;
          margin-right: 12px;
        }
        .info-con {
          .info-con-name {
            font-size: 14px;
            line-height: 20px;
          }
          .info-con-desc {
            font-size: 10px;
            line-height: 20px;
          }
        }
      }
      .info-btns {
        display: none;
      }
    }
    .module-3 {
      flex-direction: column;
      height: auto;
    }
    .module-3 .module-item {
      width: 100%;
      margin-bottom: 15px;
    }
  }
}
@media screen and (max-width: 768px) {
  .stagingIndex {
    min-width: 100%;
    padding: 12px;
    .userInfo {
      height: 100px;
      padding: 15px 15px;
      .info-main {
        align-items: center;
        .avatar {
          width: 48px;
          height: 48px;
          margin-right: 12px;
        }
        .info-con {
          .info-con-name {
            font-size: 14px;
            line-height: 20px;
          }
          .info-con-desc {
            font-size: 10px;
            line-height: 20px;
          }
        }
      }
      .info-btns {
        display: none;
      }
    }
    .module-3 {
      flex-direction: column;
      height: auto;
    }
    .module-3 .module-item {
      width: 100%;
      margin-bottom: 15px;
    }
  }
}
@media screen and (max-width: 576px) {
  .stagingIndex {
    min-width: 100%;
    padding: 12px;
    .userInfo {
      height: 100px;
      padding: 15px 15px;
      .info-main {
        align-items: center;
        .avatar {
          width: 48px;
          height: 48px;
          margin-right: 12px;
        }
        .info-con {
          .info-con-name {
            font-size: 14px;
            line-height: 20px;
          }
          .info-con-desc {
            font-size: 10px;
            line-height: 20px;
          }
        }
      }
      .info-btns {
        display: none;
      }
    }
    .module-3 {
      flex-direction: column;
      height: auto;
    }
    .module-3 .module-item {
      width: 100%;
      margin-bottom: 15px;
    }
  }
}
</style>
