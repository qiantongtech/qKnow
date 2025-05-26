<template>
  <div class="navbar" ref="navbar">
    <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container"
               @toggleClick="toggleSideBar"/>
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!settingsStore.topNav"/>
    <top-nav id="topmenu-container" class="topmenu-container" v-if="settingsStore.topNav"/>

    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">
        <!-- ---------------------------- 报工 --------------------------------- -->
        <el-popover
            trigger="hover"
            popper-style="
                        width: 336px;
                        height: 360px;
                        background: #FFFFFF;
                        box-shadow: 0px 2px 8px 0px rgba(0,0,0,0.15);
                        padding:0;
                    "
        >
          <template #reference>
            <el-badge
                :value="msgCount"
                :max="99"
                class="badge"
                :class="msgCount > 0 ? 'flash' : ''"
                :offset="[0,0]"
                :hidden="msgCount == 0"
            >
              <!--                            <i-->
              <!--                                class="iconfont right-menu-item hover-effect"-->
              <!--                                style="font-size: 22px"-->
              <!--                            >&#xebe7;</i>-->
              <i
                  class="iconfont icon-a-dingbulingdangxianxing right-menu-item hover-effect"
                  style="font-size: 20px"
              ></i>

            </el-badge>
          </template>
          <template #default>
            <el-tabs v-model="activeMsg" stretch class="mag-tabs" @tab-click="handleClick">
              <el-tab-pane label="消息提醒" name="first">
                <div class="message-list">
                  <div class="msg-item" v-for="(msg, index) in messages" :key="index" v-show="messages.length > 0" @click="clickViewMessage(msg)">
                    <img class="icon" src="@/assets/system/images/layout/msg/icon1.png" alt=""/>
                    <div class="content">
                      <div class="title">{{ msg.title }}</div>
                      <div class="time">{{ msg.time }}</div>
                    </div>
                  </div>
                  <el-empty
                      v-show="messages.length == 0 || messages == null || messages == 'undefined'"
                      :image-size="100"
                      description="暂无消息"
                      class="empty-block"
                  />
                </div>
              </el-tab-pane>
              <el-tab-pane label="通知" name="second">
                <!--                <message-list :msg-category="'first'"></message-list>-->
                <div class="message-list">
                  <div class="msg-item" v-for="(msg, index) in noticeList" :key="index" v-show="msg.entityType == 1" @click="clickViewMessage(msg)">
                    <img class="icon" src="@/assets/system/images/layout/msg/icon1.png" alt=""/>
                    <div class="content">
                      <div class="title">{{ msg.title }}</div>
                      <div class="time">{{ msg.time }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="公告" name="third">
                <!--                <message-list :msg-category="'second'"></message-list>-->
                <div class="message-list">
                  <div class="msg-item" v-for="(msg, index) in noticeList" :key="index"  v-show="msg.entityType == 2" @click="clickViewMessage(msg)">
                    <img class="icon" src="@/assets/system/images/layout/msg/icon1.png" alt=""/>
                    <div class="content">
                      <div class="title">{{ msg.title }}</div>
                      <div class="time">{{ msg.time }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>

            </el-tabs>
            <div class="msg-btns">
              <div class="btn-item" @click="clearNotification">全部已读</div>
              <div class="btn-item" @click="messageDetail">查看更多</div>
            </div>
          </template>
        </el-popover>

        <div @click="handleRefreshClick" class="right-menu-item hover-effect">
          <!--                    <el-icon size="22">-->
          <!--                        <Refresh/>-->
          <!--                    </el-icon>-->
          <i
              class="iconfont icon-a-shuaxinxianxing"
              style="font-size: 20px"
          ></i>
        </div>



        <header-search id="header-search" class="right-menu-item"/>

        <screenfull id="screenfull" class="right-menu-item hover-effect"/>


        <!-- <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip> -->
      </template>
      <div class="avatar-container">
        <el-dropdown @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar"/>
            <span class="nickName">{{ userStore.nickName }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link to="/user/profile">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <el-dropdown-item command="setLayout" v-if="settingsStore.showSettings">
                <span>布局设置</span>
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!-- <MessageList></MessageList> -->

    <!-- ----------------------------------  报工 ---------------------------- -->
    <el-dialog  v-model="open" width="800px" height="600px" :append-to="$refs['navbar']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <div style="margin: -26px 0 0 0px;">
                    <span role="heading" aria-level="2"
                          class="el-dialog__title"
                          style="text-align: left;
                            background: rgb(248, 248, 248);
                            margin: 0px -52px 0px -20px;
                            font-size: 16px;
                            padding: 10px 690px 10px 25px !important;
                            color: rgb(63, 63, 63);
                            line-height: 1.5;
                            font-family: Microsoft YaHei, Microsoft YaHei;
                            font-size: 16px;
                            font-weight: 600;"
                    >
                  {{ title }}
                </span>
        </div>

      </template>
      <el-form ref="reportRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20" style="margin-left: 15px;margin-right: -10px;">
          <el-col :span="24">
            <el-form-item prop="reportExperience" label="工作心得">
              <template #label>
                <span style="color: red; font-weight: 100 !important; font-size: 14px;">*&nbsp;</span> 工作心得
              </template>
              <el-input v-model="form.reportExperience" type="textarea" :rows="4" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="报工项目" prop="reportContent">
              <template #label>
                <span style="color: red;font-weight: 100;">*&nbsp;</span> 报工项目
              </template>
              <el-button type="primary" style="margin-bottom: 15px;" @click="addItem" plain>新增报工</el-button>
              <el-table :data="tableData" style="width: 100%;height: 320px">
                <!-- 报工项目列 -->
                <el-table-column label="报工项目" min-width="180">
                  <template v-slot="scope">
                    <el-select
                        v-model="scope.row.projectId"
                        @change="projectIdChange(scope.row, scope.row.projectId)"
                        filterable
                        placeholder="请选择项目"
                    >
                      <el-option
                          v-for="item in projectReportList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                          :disabled="isProjectDisabled(item.id, scope.row)"
                      />
                    </el-select>
                  </template>
                </el-table-column>

                <!-- 时长列 -->
                <el-table-column label="工作时长" min-width="180">
                  <template v-slot="scope">
                    <el-input v-model="scope.row.duration" :disabled="scope.row.status == 1 || scope.row.status == 2" type="number" placeholder="请填写工作时长">
                      <template #append><span>小时</span></template>
                    </el-input>
                  </template>
                </el-table-column>

                <el-table-column label="审核状态" v-if="title != '新增报工'" min-width="60">
                  <template v-slot="scope">
                    <dict-tag :options="report_status" :value="scope.row.status"/>
                  </template>
                </el-table-column>

                <!-- 操作列 -->
                <el-table-column label="操作" min-width="60">
                  <template v-slot="scope">
                    <el-button size="mini" v-if="tableData.length > 1
                                        && scope.row.status != 1 && scope.row.status != 2" type="danger"
                               @click="deleteItem(scope.$index, scope.row)" plain>删除
                    </el-button>
                    <!--                                        <el-button type="danger" icon="el-icon-delete" @click="deleteItem(scope.$index, scope.row)" circle></el-button>-->
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini"
                     class="rounded-button"
                     type="warning" @click="offFromWork">我请假了
          </el-button>
          <el-button class="rounded-button"
                     type="primary" size="mini" @click="submitForm">提交</el-button>
          <el-button class="rounded-button"
                     size="mini" @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- ----------------------------------  报工 ---------------------------- -->

    <el-dialog
        title="消息详情"
        v-model="openView"
        width="800px"
        draggable
        destroy-on-close
        class="msg-dialog"
    >
      <el-form label-width="100px">
        <el-row :gutter="20">
          <el-col :offset="4">
            <el-form-item label="创建时间：">
              <div class="form-value-ifon">
                {{ viewData.createTime }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :offset="4">
            <el-form-item label="消息标题：">
              <div class="form-value-ifon">
                {{ viewData.title }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :offset="4">
            <el-form-item label="消息内容：">
              <div v-html="viewData.content"></div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openView = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {ElMessageBox} from 'element-plus'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import HeaderSearch from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import useAppStore from '@/store/system/app'
import useUserStore from '@/store/system/user'
import useSettingsStore from '@/store/system/settings'
import useTagsViewStore from '@/store/system/tagsView'
import {getNum, listMessage, readAll} from "@/api/system/system/message/message"
import {loginOut} from "@/api/system/sso-auth.js"
// import MessageList from "@/views/system/system/message/components/messageList.vue";
import {onMounted, ref, watch} from "vue";
import moment from 'moment';
import {listNotice} from "@/api/system/system/notice";

// 认证模式
const authType = import.meta.env.VITE_APP_AUTH_TYPE;

const route = useRoute()
const router = useRouter();
const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const {proxy} = getCurrentInstance();
const visitedViews = computed(() => useTagsViewStore().visitedViews);

// 默认选择的消息类型
const activeMsg = ref('first')


//-----------------------以下报工内容-------------------------
const data = reactive({
  form: {
    reportExperience: null
  },
  rules: {
    reportExperience: [{required: true, message: "工作心得不能为空", trigger: "blur"}]
  }
});

const open = ref(false)
const title = ref(null)
const form = ref({})
const openView = ref(false)
const viewData = ref({
  title: '',
  content: '',
  createTime: ''
})
const tableData = ref([
  {projectId: null, duration: null}
])

onMounted(() => {
  // getListProject();
})

const projectList = ref([])
const projectReportList = ref([])
//项目列表
const getListProject = async () => {
  const projectRes = await listProject()
  if (projectRes && projectRes.code == 200) {
    projectList.value = projectRes.data.rows
    projectReportList.value = projectRes.data.rows
  }
}

function resetFromWork() {
  tableData.value = [
    {projectId: null, duration: null}
  ]
  form.value.reportExperience = null
}
function clickViewMessage(msg) {
  console.log('消息 ' + JSON.stringify(msg))
  openView.value = true
  viewData.value.title = msg.title;
  viewData.value.content = msg.noticeContent;
  viewData.value.createTime = msg.time;
}
//请假了
function offFromWork() {
  proxy.$modal.confirm('确认请假了？').then(function() {

  }).then(() => {
    const itemList = tableData.value
    const req = {
      reportExperience: '我请假了',
      status: 1,
      reportTime: new Date(),
      detailRespVOList: tableData.value
    }
    console.log('---------提交-请假----req-------', req)
    addReport(req).then(response => {
      proxy.$modal.msgSuccess("提交成功");
      open.value = false;
      getList();
    }).catch(error => {
    });
  }).catch(() => {});
  // form.value.reportExperience = '我请假了'
}

const handleDemoClick = ()=>{
  router.push({path: '/flyflow/task/demo'})
}

/** 提交按钮 */
function submitForm() {
  if(form.value.reportExperience == null){
    proxy.$modal.msgWarning("工作心得为空");
    return;
  }
  proxy.$refs["reportRef"].validate(valid => {
    console.log('---------校验----',valid)
    if (valid) {
      const tempList = tableData.value
      if(tempList.length == 0){
        proxy.$modal.msgError("报工项目为空");
        return
      }
      let idStatus = false
      let timeStatus = false
      tempList.forEach(e=>{
        if(e.projectId == null){
          idStatus = true
        }
        if(e.duration == null){
          timeStatus = true
        }
      })
      if(idStatus){
        proxy.$modal.msgWarning("报工项目为空");
        return
      }
      if(timeStatus){
        proxy.$modal.msgWarning("报工项目工作时长为空");
        return
      }
      // 提取所有非空的 projectId 并用逗号连接
      form.value.reportContent = tempList
          .map(item => item.projectId)
          .filter(id => id != null)  // 过滤掉 null 或 undefined 的值
          .join(',');

      if (form.value.id != null) {
        const tempList = tableData.value.map(e => {
          const date = new Date(e.reportTime);
          return {
            ...e,
            reportTime: isNaN(date.getTime()) ? null : date // 如果无效，设置为 null
          };
        });
        const req = {
          ...form.value,
          createTime: new Date(form.value.createTime),
          reportTime: new Date(form.value.reportTime),
          updateTime: new Date(),
          detailRespVOList: tempList
        }
        updateReport(req).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        const itemList = tableData.value
        const req = {
          ...form.value,
          status: 0,
          reportTime: new Date(),
          detailRespVOList: tableData.value
        }
        console.log('---------提交-----req-------', req)
        addReport(req).then(response => {
          proxy.$modal.msgSuccess("提交成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      }
    }
  });
}

// 删除操作
const deleteItem = (index) => {
  // 使用 splice 方法根据索引删除数据
  tableData.value.splice(index, 1)
  console.log('删除了索引为', index, '的项')
}

const addItem = () => {
  tableData.value.push({name: 'aa'})
}

const popoverVisible= ref(false)

const handleFocus = () => {
  popoverVisible.value = true
}
const handleBlur = () => {
  popoverVisible.value = false
}
const handleSelectChange = (value) => {
  console.log('选中的选项:', value);
}

const handlePopoverClick = (value) => {
  // 如果不想关闭 Popover，可以在这里处理额外的逻辑
}

//打开报工页面
function openForWork() {
  tableData.value = [
    {projectId: null, duration: null}
  ]
  form.value.reportExperience = null
  title.value = '新增报工'
  open.value = true
}

function cancel() {
  open.value = false
}

//报工管理
function reportingForWork() {
  router.push({path: '/project/report'})
}

const projectIdChange = (row, newValue) => {
  const oldValue = row.projectId;

  // 如果用户选择了新项目，且与之前的不同
  if (oldValue !== newValue) {
    // 更新已选的项目列表
    row.projectId = newValue;
  }
};

// 判断项目是否被禁用
const isProjectDisabled = (projectId, currentRow) => {
  // 判断当前项目是否已被选中，并且不是当前行
  return tableData.value.some(row => row.projectId === projectId && row !== currentRow);
};
//-----------------------以上报工内容-------------------------


// 消息通知数量
const msgCount = ref(0);
const messages = ref([])
const noticeList = ref([])
const sessionValue = ref(null)
getMessageNum(); // 第一次主要获取消息

const wsUri = import.meta.env.VITE_APP_WEBSOCKET_API + '/websocket/message/' + userStore.userId
// 建立socket连接
const ws = new WebSocket(wsUri);

const initWebSocket = () => {
  //查询通知公告
  listNotice().then(response => {
    console.log('---------- response.rows-------------', response)
    response.rows.forEach(item => {
      item.title = item.noticeTitle
      item.entityType = item.noticeType
      item.time = item.updateTime != undefined && item.updateTime != null
          ? formatTimestamp(item.updateTime) : formatTimestamp(item.createTime)
    })
    noticeList.value = response.rows
    console.log('----------noticeList-------------', noticeList.value)
    // messages.value.push(messageData)

  })

  //查询未读消息通知
  listMessage({
    receiverId: userStore.userId,
    hasRead: 0,
    pageNum: 1,
    pageSize: 1000,
  }).then((response) => {
    response.data.rows.forEach(item => {
      item.time = item.updateTime
      item.entityType = item.category
      // item.title = item.title
    })
    messages.value = [...response.data.rows, ...messages.value]
    msgCount.value = messages.value ? messages.value.length : 0;
    console.log('------messages.value----', messages.value)
  });
  ws.onmessage = (event) => { // 服务端推送数据
    // console.log('===服务端推送数据=========>',event.data)
    const messageData = JSON.parse(event.data);
    console.log('===监测数据 messageData=========>', messageData)
    if (messageData) {
      messageData.time = messageData.updateTime != undefined && messageData.updateTime != null
          ? formatTimestamp(messageData.updateTime) : formatTimestamp(messageData.createTime)
      // messages.value.push(messageData)
      messages.value = [messageData, ...messages.value]
    }
    console.log('===存储的数据 messages=========>', messages.value)
    // 消息数量更新
    msgCount.value = messages.value ? messages.value.length : 0;
  };
}

onMounted(() => {
  initWebSocket();
})
// 页面注销
onBeforeUnmount(() => {
  console.log('------页面注销----')
  ws.close();// 关闭socket
})

// 格式化时间戳为 YYYY-MM-DD HH:mm:ss 格式
function formatTimestamp(timestamp) {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 消息查询
function getMessageNum() {
  getNum()
}


// tab-click 事件处理函数
const handleClick = (tab) => {
  console.log('当前选中的 tab:', tab.props);  // tab 是一个对象，包含当前被点击的 tab 的信息
  const label = tab.props.label
  activeMsg.value = tab.props.name
};

function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout();
      break;
    case "logout":
      logout();
      break;
    default:
      break;
  }
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logOut().then(() => {
      if (authType === 'sso') {
        // 退出统一认证中心的登录状态
        loginOut(userStore.userId).then(() => {
          location.href = '/index';
        });
      } else {
        location.href = '/index';
      }
    });
  }).catch(() => {
  });
}

const emits = defineEmits(['setLayout'])

function setLayout() {
  emits('setLayout');
}

function handleRefreshClick() {
  const activeView = visitedViews.value.find(view => view.path === route.path);
  proxy.$tab.refreshPage(activeView);
  if (route.meta.link) {
    useTagsViewStore().delIframeView(route);
  }
}

function messageDetail() {
  if(activeMsg.value == 'first') {
    router.push({path: '/bases/message'})
  }else{
    router.push({path: '/system/notice'})
  }
}

function clearNotification() {
  readAll().then(() => {
    messages.value = []
    msgCount.value = 0
    ElMessage.success("已全部已读！");
  })

}
</script>

<style lang='scss' scoped>
.message-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
}

.msg-item {
  cursor: pointer;
  display: flex;
  align-items: center;
  width: 100%;
  padding: 10px 16px;
  margin-bottom: 10px;
  background: #f9f9f9;
  border-radius: 4px;
}

.icon {
  width: 34px;
  height: 34px;
  margin-right: 12px;
}

.content {
  .title {
    font-size: 14px;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);
    margin-bottom: 6px;
  }

  .time {
    font-size: 12px;
    color: rgba(0, 0, 0, 0.45);
  }
}

.rwgl-item{
  display: flex !important;
  align-items: center;
  img{
    height: 18px;
    display: block;
  }
}

.navbar {
  height: 60px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  text-align: center;
  line-height: 60px;

  ::v-deep .size-icon--style {
    line-height: 60px;
  }

  .hamburger-container {
    line-height: 60px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 60px;
    display: flex;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin: 0 15px 0 0;

      .avatar-wrapper {
        display: flex;
        align-items: center;
        margin-top: 10px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 20px;
        }

        .nickName {
          font-size: 15px;
          /*font-weight: bold;*/
          // color: rgba(0, 0, 0, 0.65);
          color: var(--themeColor);
          display: inline-block;
          margin-left: 10px;
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }

  .flash ::v-deep .el-badge__content.is-fixed {
    animation: twinkle 1s infinite;
    /*margin-top: 16px;*/
    margin-right: 6px;
  }

  /* 定义闪烁的动画 */
  @keyframes twinkle {
    0% {
      opacity: 1; /* 完全可见 */
    }
    50% {
      opacity: 0.3; /* 半透明 */
    }
    100% {
      opacity: 1; /* 完全可见 */
    }
  }

  .item {
    height: 60px;
    line-height: 60px;
    display: inline-block;
    cursor: pointer;
  }

  .badge :deep(.el-badge__content.is-fixed) {
    top: 20px;
    transform: translateY(-50%) translateX(64%);
  }
}

.mag-tabs {
  height: calc(100% - 50px);

  ::v-deep .el-tabs__item {
    height: 50px;
    line-height: 50px;
  }

  ::v-deep .el-tabs__header {
    margin-bottom: 6px;
  }

  ::v-deep .el-tabs__content {
    height: calc(100% - 56px);

    .el-tab-pane {
      height: 100%;
    }
  }
}

.msg-btns {
  display: flex;
  height: 50px;
  line-height: 50px;
  border-top: 1px solid #e6e6e6;

  .btn-item {
    width: 50%;
    text-align: center;
    cursor: pointer;
    color: rgba(0, 0, 0, 0.85);

    &:last-child {
      border-left: 1px solid #e6e6e6;
    }
  }
}

#custom-header {
  background-color: rgb(248, 248, 248);
}


.el-dialog__header.show-close{
  text-align: left !important;
  padding: 9px 620px 9px 20px !important;
  background: rgb(248, 248, 248) !important;
}

.el-dialog__body{
  height: 500px;
}

/* 确保样式生效，增加选择器的优先级 */
.rounded-button, .rounded-button .el-button {
  border-radius: 2px !important;
}
</style>
