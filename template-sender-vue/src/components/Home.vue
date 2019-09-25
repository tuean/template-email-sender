<template>
  <el-container>
    <el-header class="header">
      <Title/>
    </el-header>

    <el-main>
      <el-tabs v-model="activeName" :tab-position="tabPosition" @tab-click="handleClick">
        <el-tab-pane label="邮箱设置" name="emailSetting">
          <EmailSetting/>
        </el-tab-pane>
        <el-tab-pane label="页面输入" name="inputPage">
          <input-page/>
        </el-tab-pane>
        <el-tab-pane label="excel导入" name="inputExcel">
          敬请期待
        </el-tab-pane>
      </el-tabs>
    </el-main>


  </el-container>
</template>



<script>
  import Title  from "./Title";
  import InputPage from "./tabs/InputPage";
  import EmailSetting from "./tabs/EmailSetting";

  export default {
      components: {
          Title, InputPage, EmailSetting
      },
      data() {
          return {
              activeName: 'emailSetting',
              tabPosition: 'left'
          };
      },
      methods: {
          handleClick(tab, event) {
              console.log(tab, event);
          }
      }
  }
</script>


<style>
  .header {
    height: 30px;
    vertical-align: center;
    justify-content: center;
    text-justify: auto;
  }
  .main {
    display: flex;
  }
</style>
