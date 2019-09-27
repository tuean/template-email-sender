<template>
  <el-dialog title="参数设置" :visible.sync="paramFillVisible" v-if="paramFillVisible" :show="show"  @close="closeModal()" :append-to-body="true">
    <el-form ref="form" :model="data" label-width="80px">

      <el-form-column
        v-for="(index, val) in paramList"
        :label="val.label"
      >
        <el-input v-model="data[val.prop]" />
      </el-form-column>



      <el-form-item>
        <el-button type="primary" @click="onSubmit()">保存</el-button>
        <el-button type="primary" @click="onRemove()">删除</el-button>
        <el-button @click="cancel()">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>



<script>
  export default {
      data() {
          return {
              paramFillVisible: this.show,
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
          },
          paramList: {
              type: Array,
              default: function () {
                  return []
              }
          },
          photoList: {
              type: Array,
              default: function () {
                  return []
              }
          }
      },
      created() {
         console.log(this.paramList)
         console.log(this.photoList)
      },
      watch: {
          show() {
              this.paramFillVisible = this.show
          }
      },
      methods: {
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
