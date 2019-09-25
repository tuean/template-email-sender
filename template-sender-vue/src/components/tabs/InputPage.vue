<template>
  <div>
    <section>
      <el-input
        type="textarea"
        :rows="4"
        placeholder="请输入模板..."
        v-model="textarea">
      </el-input>
    </section>

    <section>

    </section>

  </div>
</template>


<script>
  export default {
      data() {
          return {
              textarea: ''
          }
      }
  }
</script>
