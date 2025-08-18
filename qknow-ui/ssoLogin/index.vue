<template>
  <div style="height: 100%" class="app-container"  ref="app-container">
    <sys-info-two
      ref="sysInfoTwo"
    >
    </sys-info-two>
  </div>
</template>

<script>
import SysInfoTwo from "./loginPages/loginPage.vue";

export default {
  components: {
    SysInfoTwo,
  },
  name: "Login",
};
</script>
<style lang="scss" scoped>
.app-container {
    padding: 0px;
    margin: 0px;
    background-color: #FFFFFF;
    min-height: 100%;
}
</style>
