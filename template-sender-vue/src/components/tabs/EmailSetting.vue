<template>
  <div class="email">
    <el-form class="form" ref="form" label-width="180px">
      <el-form-item label="当前邮箱">
        <el-select v-model="instantEmail" placeholder="请选择邮箱" clearable @change="changeEmail">
          <el-option
            v-for="item in emailList"
            :key="item.account"
            :label="item.account"
            :value="item.id">
          </el-option>
        </el-select>
        <el-button type="primary" @click="createNew">新建</el-button>
        <el-button type="primary" @click="modify" v-if="instantEmail != null">修改</el-button>
        <el-button type="primary" @click="deleteIt" v-if="instantEmail != null">删除</el-button>
      </el-form-item>
    </el-form>

    <email-config :show.sync="emailConfigVisible" v-if="emailConfigVisible" v-bind:data="configData" @callFather="deleteIt"/>

  </div>


</template>



<script>
  import EmailConfig from "../dialogs/EmailConfig";
  import * as storage from '../../util/storage'

  export default {
      components: {
          EmailConfig
      },
      data() {
          return {
              instantEmail: {},
              emailList: [],
              emailConfigVisible: false,
              configData: {},
          }
      },
      created() {
          this.getList()
      },
      methods: {
          getList() {
            this.emailList = storage.getEmailList()
            this.instantEmail = storage.getSelect()
          },
          changeEmail(item) {
              console.log(item)
          },
          createNew() {
              this.configData = {}
              this.emailConfigVisible = true
          },
          modify() {
              this.configData = storage.getById(this.instantEmail)
              this.emailConfigVisible = true
          },
          deleteIt(id) {
              if (this.instantEmail != null && this.instantEmail != '') {
                id = this.instantEmail
              }
              storage.deleteItem(id)
              storage.deleteSelected()
              this.getList()
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
