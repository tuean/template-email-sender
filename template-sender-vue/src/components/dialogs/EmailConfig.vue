<template>
  <el-dialog title="邮箱设置" :visible.sync="emailConfigVisible" v-if="emailConfigVisible" :show="show"  @close="closeModal()" :append-to-body="true">
    <el-form ref="form" :model="data" label-width="80px">

      <el-form-item label="邮箱">
        <el-input v-model="data.account"/>
      </el-form-item>

      <el-form-item label="密码">
        <el-input v-model="data.pwd" show-password/>
      </el-form-item>

      <el-form-item label="smtpAuth">
        <el-select v-model="data.smtpAuth" placeholder="" clearable>
          <el-option
            v-for="item in ifList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="smtpHost">
        <el-input v-model="data.smtpHost"/>
      </el-form-item>

      <el-form-item label="smtpPort">
        <el-input v-model="data.smtpPort"/>
      </el-form-item>

      <el-form-item label="smtpPlain">
        <el-select v-model="data.smtpPlain" placeholder="" clearable>
          <el-option
            v-for="item in ifList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="smtpSsl">
        <el-select v-model="data.smtpSsl" placeholder="" clearable>
          <el-option
            v-for="item in ifList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit()">保存</el-button>
        <el-button type="primary" @click="onRemove()">删除</el-button>
        <el-button @click="cancel()">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>


<script>
  import {addItem, deleteItem, updateItem, deleteSelected} from '../../util/storage'
  import {randomString} from "../../util/common";

  export default {
      data() {
          return {
              emailConfigVisible: this.show,
              ifList: [
                  {
                      label: 'true',
                      value: 'true'
                  },{
                      label: 'false',
                      value: 'false'
                  }
              ],
          }
      },
      props: {
          show: {
              type: Boolean,
              default: false
          },
          data: {
              type: Object,
              default: function () {
                return {}
              }
          }
      },
      watch: {
          show() {
              this.emailConfigVisible = this.show
          }
      },
      methods: {
          onSubmit() {
              if (this.data['id'] != null) {
                updateItem(this.data)
              } else {
                let id = randomString(32)
                this.data['id'] = id
                addItem(this.data)
              }
              this.closeModal()
          },
          onRemove() {
            let _this = this
            this.$confirm('确认删除?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.closeModal()
              _this.$emit("callFather", _this.data['id'])
              this.$message({
                type: 'success',
                message: '删除成功!'
              });

            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除'
              });
            });
          },
          closeModal() {
              this.$emit('update:show', false)
          },
          cancel() {
              this.closeModal()
          }
      }
  }
</script>



<style>


</style>
