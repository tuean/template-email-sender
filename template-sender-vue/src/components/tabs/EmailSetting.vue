<template>
  <div class="email">
    <el-form class="form" ref="form" label-width="180px">
      <el-form-item label="当前邮箱">
        <el-select v-model="instantEmail" placeholder="请选择邮箱" clearable @change="changeEmail">
          <el-option
            v-for="item in emailList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <el-button type="primary" @click="createNew">新建</el-button>
        <el-button type="primary" @click="modify" v-if="instantEmail.length > 0">修改</el-button>
        <el-button type="primary" @click="deleteIt" v-if="instantEmail.length > 0">删除</el-button>
      </el-form-item>
    </el-form>

    <email-config :show.sync="emailConfigVisible" v-if="emailConfigVisible" v-bind:data="configData"/>

  </div>


</template>



<script>
  import EmailConfig from "../dialogs/EmailConfig";

  export default {
      components: {
          EmailConfig
      },
      data() {
          return {
              instantEmail: '',
              emailList: [
                  {
                      label: 'tuean_z@163.com',
                      value: 'tuean_z@163.com'
                  },{
                      label: 'tuean_x@163.com',
                      value: 'tuean_x@163.com'
                  }
              ],
              emailConfigVisible: false,
              configData: {},
          }
      },
      methods: {
          changeEmail(item) {
              console.log(item)
          },
          createNew() {
              this.configData = {}
              this.emailConfigVisible = true
          },
          modify() {
              this.configData = this.instantEmail
              this.emailConfigVisible = true
          },
          deleteIt() {
              this.instantEmail = ''
              // todo
          }
      }
  }
</script>


<style>
  .email {
    display:flex;
    justify-content:center;
    width: 90%;
    align-items: center;
    /* 注意这里需要设置高度来查看垂直居中效果 */
    /*height: 200px;*/
  }
  .form {

  }
</style>
