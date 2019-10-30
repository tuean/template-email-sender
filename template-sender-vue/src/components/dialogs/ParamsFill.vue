<template>
  <el-dialog title="参数设置" :visible.sync="paramFillVisible" v-if="paramFillVisible" :show="show"  @close="closeModal()" :append-to-body="true">
    <el-form ref="form" :model="data" label-width="80px">

      <el-form-item label="字段名">
        <el-input v-model="data.name" placeholder="多个请用英文逗号分隔"></el-input>
      </el-form-item>

      <el-form-item label="字段类型">
        <el-select v-model="data.type" placeholder="" clearable>
          <el-option
            v-for="item in typeList"
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

  import {updateKey, deleteKey} from '../../util/storage'

  export default {
      data() {
          return {
              paramFillVisible: this.show,
              typeList: [
                  {
                      label: '字符串',
                      value: 'string'
                  },{
                      label: '图片',
                      value: 'photo'
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
      created() {

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
          },
          onSubmit() {
              updateKey(this.data)
              this.closeModal()
          },
          onRemove() {
              deleteKey(this.data)
              this.closeModal()
          }
      }
  }
</script>


<style>

</style>
