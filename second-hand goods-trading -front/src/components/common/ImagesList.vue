<template>
    <div class="main-border" style="width: auto;height: 10000px">

      <el-dialog
          title="添加轮播图"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="form.name"></el-input>
          </el-form-item>

          <el-form-item label="图片">

            <el-upload
                action="http://localhost:8080/file/"
                :on-success="fileHandleSuccess"
                accept="image/*"
            >
              <el-image
                  style="width: 120px; height: 120px;border-radius: 10px;"
                  :src="originUrl"
                  fit="contain"></el-image>
            </el-upload>

          </el-form-item>
        </el-form>


        <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="saveImages">确 定</el-button>
    </span>
      </el-dialog>

      <el-button type="primary" @click="dialogVisible = true">添加</el-button>


        <el-table
                :data="images"
                stripe
                style="width: 100%;color: #5a5c61;">
            <el-table-column
                    prop="id"
                    label="ID"
                    show-overflow-tooltip
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="名称"
                    width="150"
                    show-overflow-tooltip
                    >
            </el-table-column>
            <el-table-column show-overflow-tooltip>
              <template slot-scope="scope">
                <el-avatar shape="square" :src="scope.row.url" :size="150" :fit='fill'></el-avatar>
              </template>
            </el-table-column>
            <el-table-column
                prop="createTime"
                label="创建时间"
                show-overflow-tooltip
                min-width="100"
                >
            </el-table-column>

          <el-table-column label="操作" width='150px'>
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  type="danger"
                  @click="deleteImages(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
<!--        <div class="block">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="nowPage"
                    :page-size="8"
                    background
                    layout="prev, pager, next,jumper"
                    :total="total">
            </el-pagination>
        </div>-->
    </div>
</template>

<script>
    export default {
        name: "imagesList",
        created() {
            this.getLevel();
        },
        methods:{
          saveImages(){
            this.dialogVisible = false
            console.log(this.form)
            this.$api.saveImages({
              name:this.form.name,
              url:this.form.url
            }).then(res => {
              if(res.status_code==1){
                this.images = res.data;
              }else {
                this.$message.error(res.msg)
              }
            }).catch(e => {
              console.log(e)
            })
          },
          fileHandleSuccess(response, file) {
            console.log("file:", response, file);
            let imgUrl = response.data;
            this.form.url = imgUrl;
          },
          handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                  done();
                })
                .catch(_ => {});
          },
            getLevel(){
                this.$api.getImages().then(res => {
                    if(res.status_code==1){
                        this.images = res.data;
                    }else {
                        this.$message.error(res.msg)
                    }

                }).catch(e => {
                    console.log(e)
                })
            },
            deleteImages(index){
              this.$confirm('确定删除', '删除', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.$api.deleteImages({
                  id:this.images[index].id
                }).then(res=>{
                  if(res.status_code==1){
                    this.$message({
                      message: '删除成功！',
                      type: 'success'
                    });
                    location.reload();
                  }else {
                    this.$message.error(res.msg)
                  }

                }).catch(e => {
                  console.log(e)
                })
              })

            },
            updateImages(){

            },
            handleCurrentChange(val) {
              this.nowPage = val;
              if(this.mode == 1){
                this.getUserData();
              }
              if(this.mode == 2){
                this.getBadUserData();
              }
              if(this.mode == 3){
                this.getUserManage();
              }
            },

        },
        data(){
            return {
              originUrl:'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
              form:{},
              dialogVisible:false,
                fill:'fill',
                mode:1,
                nowPage: 1,
                total: 0,
                Order: [],
                images: []
            }
        },
    }
</script>

<style scoped>
    .main-border{
        background-color: #FFF;
        padding: 10px 30px;
        box-shadow: 0 1px 15px -6px rgba(0,0,0,.5);
        border-radius: 5px;
    }
    .block {
        display: flex;
        justify-content:center;
        padding-top: 15px;
        padding-bottom: 10px;
        width: 100%;
    }
</style>