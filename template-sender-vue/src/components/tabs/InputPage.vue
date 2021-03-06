<template>
  <div>
    <section class="editor-container">
      <el-row type="flex" class="row-bg">
        <el-col :span="1">
          <span>模板：</span>

          <!-- 图片上传组件辅助-->
          <el-upload
            class="avatar-uploader"
            :action="serverUrl"
            name="img"
            :headers="header"
            :show-file-list="false"
            :on-success="uploadSuccess"
            :on-error="uploadError"
            :before-upload="beforeUpload">
          </el-upload>
        </el-col>

        <el-col :span="20">
          <section class="editor">
            <quill-editor
              v-model="content"
              ref="myQuillEditor"
              :options="editorOption"
              @blur="onEditorBlur($event)" @focus="onEditorFocus($event)"
              @change="onEditorChange($event)">
            </quill-editor>
          </section>
        </el-col>
        <el-col :span="1"/>
        <el-col :span="3">
          <el-button type="primary" @click="doSend()">发送</el-button>
        </el-col>
      </el-row>
    </section>

    <section>
      <el-row type="flex" class="row-bg">
        <el-col :span="1">参数：</el-col>
        <el-col :span="20">
          <el-table
            :data="tableData"
            border
            highlight-current-row
            style="width:100%">

            <el-table-column
              v-for="(val, i) in paramList"
              :key="i"
              :prop='val.type'
              :label='val.name'
              align="center">
            </el-table-column>

            <el-table-column
              v-if="tableData.size > 0"
              fixed="right"
              label="操作"
              >
              <template slot-scope="scope">
                <el-button @click="reCheck(scope.row)" type="text" size="small">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="1"/>
        <el-col :span="3">
          <div>
            <el-button type="primary" @click="add()">新增字段</el-button>
          </div>
          &nbsp;
          <div>
            <el-button type="primary" @click="addData()">新增属性</el-button>
          </div>
        </el-col>
      </el-row>
    </section>

    <params-fill :show.sync="paramFillVisible"
                 v-if="paramFillVisible"
    />
  </div>
</template>


<script>

  import ParamsFill from "../dialogs/ParamsFill";
  import {checkTemplate} from '../../axios/api'
  import {contentStore, getTempContent, getKeys} from '../../util/storage'

  const toolbarOptions = [
    ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线
    ["blockquote", "code-block"], // 引用  代码块
    [{ header: 1 }, { header: 2 }], // 1、2 级标题
    [{ list: "ordered" }, { list: "bullet" }], // 有序、无序列表
    [{ script: "sub" }, { script: "super" }], // 上标/下标
    [{ indent: "-1" }, { indent: "+1" }], // 缩进
    [{'direction': 'rtl'}],                         // 文本方向
    [{ size: ["small", false, "large", "huge"] }], // 字体大小
    [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
    [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色
    [{ font: [] }], // 字体种类
    [{ align: [] }], // 对齐方式
    ["clean"], // 清除文本格式
    ["link", "image", "video"] // 链接、图片、视频
  ];

  export default {
      data() {
        return {
          content: this.value,
          quillUpdateImg: false, // 根据图片上传状态来确定是否显示loading动画，刚开始是false,不显示
          editorOption: {
            placeholder: "",
            theme: "snow", // or 'bubble'
            placeholder: "邮件模板~",
            modules: {
              toolbar: {
                container: toolbarOptions,
                // container: "#toolbar",
                handlers: {
                  image: function(value) {
                    if (value) {
                      // 触发input框选择图片文件
                      document.querySelector(".avatar-uploader input").click();
                    } else {
                      this.quill.format("image", false);
                    }
                  },
                }
              }
            }
          },
          serverUrl: "/v1/blog/imgUpload", // 这里写你要上传的图片服务器地址
          header: {
            // token: sessionStorage.token
              // 有的图片服务器要求请求头需要有token
          },
          tableData: [],
          paramList: [],
          paramFillVisible: false,
        };
      },
      computed: {
        editor() {
          return this.$refs.myQuillEditor.quill;
        },
      },
      methods: {
        onEditorReady(editor) {
          // 准备编辑器
        },
        onEditorBlur(){

        },
        // 失去焦点事件
        onEditorFocus(){

        },
        // 获得焦点事件
        onEditorChange(){
            contentStore(this.content)
        },
        beforeUpload() {

        },
        // 内容改变事件
        saveHtml: function(event){

        },
        doSend() {

        },
        uploadSuccess(res, file) {
          // res为图片服务器返回的数据
          // 获取富文本组件实例
          let quill = this.$refs.myQuillEditor.quill;
          // 如果上传成功
          if (res.code == 200) {
            // 获取光标所在位置
            let length = quill.getSelection().index;
            // 插入图片  res.url为服务器返回的图片地址
            quill.insertEmbed(length, "image", res.url);
            // 调整光标到最后
            quill.setSelection(length + 1);
          } else {
            this.$message.error("图片插入失败");
          }
          // loading动画消失
          this.quillUpdateImg = false;
        },
        uploadError() {
          this.$message.error("图片插入失败");
        },
        add() {
            this.paramFillVisible = true
        },
          reCheck() {

          },
        refreshKeys() {
          this.paramList = getKeys();
        },
        addData() {

        }

    },
      components: {
          ParamsFill
      },
      created() {
          let temp = getTempContent();
          if (temp != null && temp.length > 0) {
              this.content = temp
          }
          this.refreshKeys()
      }
  }
</script>

<style>
  .editor-container {
    height: auto;
  }
  .editor {
    line-height: normal !important;
    height: auto;
  }
  .ql-snow .ql-tooltip[data-mode=link]::before {
    content: "请输入链接地址:";
  }
  .ql-snow .ql-tooltip.ql-editing a.ql-action::after {
    border-right: 0px;
    content: '保存';
    padding-right: 0px;
  }

  .ql-snow .ql-tooltip[data-mode=video]::before {
    content: "请输入视频地址:";
  }

  .ql-snow .ql-picker.ql-size .ql-picker-label::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item::before {
    content: '14px';
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value=small]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value=small]::before {
    content: '10px';
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value=large]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value=large]::before {
    content: '18px';
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value=huge]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value=huge]::before {
    content: '32px';
  }

  .ql-snow .ql-picker.ql-header .ql-picker-label::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item::before {
    content: '文本';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
    content: '标题1';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
    content: '标题2';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
    content: '标题3';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
    content: '标题4';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
    content: '标题5';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
    content: '标题6';
  }

  .ql-snow .ql-picker.ql-font .ql-picker-label::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item::before {
    content: '标准字体';
  }
  .ql-snow .ql-picker.ql-font .ql-picker-label[data-value=serif]::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item[data-value=serif]::before {
    content: '衬线字体';
  }
  .ql-snow .ql-picker.ql-font .ql-picker-label[data-value=monospace]::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item[data-value=monospace]::before {
    content: '等宽字体';
  }
</style>
