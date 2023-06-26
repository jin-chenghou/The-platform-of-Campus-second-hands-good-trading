<template>
    <div class="sign-in-container">
        <el-card class="box-card">
            <div class="sign-in-body">
                <div class="sign-in-title">注册</div>
                <el-input placeholder="请输入昵称..." maxlength="30"  v-model="userInfo.nickname" class="sign-in-input" clearable>
                    <template slot="prepend">
                        <div class="el-icon-user-solid"></div>
                    </template>
                </el-input>
                <el-input placeholder="请输入帐号..." maxlength="11" v-model="userInfo.accountNumber" class="sign-in-input" clearable>
                    <template slot="prepend">
                        <div class="el-icon-phone"></div>
                    </template>
                </el-input>
                <el-radio-group v-model="userInfo.sex" >
                    <template>
                        <el-radio label="男">男</el-radio>
                        <el-radio label="女">女</el-radio>
                    </template>
                </el-radio-group>

                <el-input placeholder="请输入密码..." show-password maxlength="16" v-model="userInfo.userPassword" class="sign-in-input" clearable>
                    <template slot="prepend">
                        <div class="el-icon-lock"></div>
                    </template>
                </el-input>
                <el-input placeholder="请再次输入密码..." show-password maxlength="16" v-model="userPassword2" @keyup.enter.native="signIn" class="sign-in-input" clearable>
                    <template slot="prepend">
                        <div class="el-icon-lock"></div>
                    </template>
                </el-input>
                <el-input placeholder="请输入手机号..." maxlength="50" v-model="userInfo.phone" class="sign-in-input" clearable>
                    <template slot="prepend">
                        <div class="el-icon-message"></div>
                    </template>
                </el-input>
                <el-input placeholder="请输入邮箱..." maxlength="50" v-model="userInfo.email" class="sign-in-input" clearable>
                    <template slot="prepend">
                        <div class="el-icon-message"></div>
                    </template>
                </el-input>
                <el-input placeholder="请输入城市..." maxlength="500" v-model="userInfo.city" class="sign-in-input" clearable>
                    <template slot="prepend">
                        <div class="el-icon-place"></div>
                    </template>
                </el-input>

                <!-- 商户-->

                <el-input placeholder="请输入银行账户..." maxlength="50" v-model="userInfo.bankAccount" class="sign-in-input" clearable v-show='businessLicense'>
                    <template slot="prepend">
                        <div class="el-icon-s-custom"></div>
                    </template>
                </el-input>
                <div class="release-idle-container-picture" v-show='businessLicense'>
                    <div class="edit-tip">上传营业执照</div>
                    <el-upload
                        action="http://localhost:8080/file/"
                        :on-success="fileHandleSuccess"
                        :file-list="imgList"
                        accept="image/*"
                    >
                        <el-image
                            style="width: 120px; height: 120px;border-radius: 10px;"
                            :src="userInfo.businessLicense"
                            fit="contain"></el-image>
                    </el-upload>
                </div>

                <div class="release-idle-container-picture" v-show='idCard'>
                    <div class="edit-tip">上传身份证</div>
                    <el-upload
                        action="http://localhost:8080/file/"
                        :on-success="idCardFileHandleSuccess"
                        :file-list="imgList"
                        accept="image/*"
                    >
                        <el-image
                            style="width: 120px; height: 120px;border-radius: 10px;"
                            :src="userInfo.idCard"
                            fit="contain"></el-image>
                    </el-upload>
                </div>

                <template>
                    <el-radio-group v-model="userInfo.type" @change="getright">
                        <!-- 单选按钮A -->
                        <el-radio  label="1">用户</el-radio>
                        <el-radio  label="2">商户</el-radio>
                    </el-radio-group>
                </template>
                <br>
                <div class="sign-in-submit">
                    <el-button type="primary" @click="signIn">提交</el-button>
                    <el-button type="primary" @click="toLogin" style="margin-left: 20px" >返回登录</el-button>
                </div>

            </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "sign-in",
        data(){
            return{
                idCard:false,
                businessLicense:false,
                showFileList:true,
                imgDialogVisible:false,
                imgList:[],
                userPassword2:'',
                img:"",
                userInfo:{
                    bankAccount:"",
                    phone:"",
                    type:"1",
                    sex:"男",
                    accountNumber:'',
                    userPassword:'',
                    email:'',
                    city:'',
                    nickname:'',
                    businessLicense:'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
                    idCard:'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
                }
            };
        },
        methods:{
            toLogin(){
                this.$router.replace({path: '/login'});
            },
            signIn(){
                console.log(this.userInfo.nickname);
                if(this.userInfo.accountNumber&&this.userInfo.userPassword&&this.userInfo.nickname){
                    if(this.userInfo.userPassword!==this.userPassword2){
                        this.$message.error('两次输入的密码不相同！');
                    }else {
                        this.$api.signIn(this.userInfo).then(res=>{
                            if(res.status_code===1){
                                this.$message({
                                    message: '注册成功！',
                                    type: 'success'
                                });
                                this.$router.replace({path: '/login'});
                            }else {
                                this.$message.error('注册失败，用户已存！');
                            }
                        }).catch(e=>{
                            console.log(e);
                            this.$message.error('注册失败，网络异常！');
                        })
                    }
                }else{
                    this.$message.error('注册信息未填写完整！');
                }
            },


            fileHandleRemove(file, fileList) {
                console.log(file, fileList);
                for(let i=0;i<this.imgList.length;i++){
                    if(this.imgList[i]===file.response.data){
                        this.imgList.splice(i,1);
                    }
                }
            },
            fileHandlePreview(file) {
                console.log(file);
                this.imgDialogVisible=true;

            },
            fileHandleSuccess(response, file, fileList){
                console.log("file:",response,file,fileList);
                this.userInfo.businessLicense=response.data;
                this.imgList.push(response.data);
            },
            idCardFileHandleSuccess(response, file, fileList){
                console.log("file:",response,file,fileList);
                this.userInfo.idCard=response.data;
                this.imgList.push(response.data);
            },
            handleExceed(files, fileList) {
                this.$message.warning(`限制1张图片，本次选择了 ${files.length} 张图，共选择了 ${files.length + fileList.length} 张图`);
            },
            getright(){
                console.log("当前选中" + this.userInfo.type);
                if (this.userInfo.type === '1'){
                    this.businessLicense = false;
                    this.idCard = false;
                }else {
                    this.businessLicense = true;
                    this.idCard = true;
                }
            },
        },

    }
</script>

<style scoped>
    .sign-in-container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        width: 100%;
		background-size: 100%;
		background-image: url('/image/back.png');
    }

    .sign-in-body {
        padding: 30px;
        width: 300px;
        height: 100%;
    }

    .sign-in-title {
        padding-bottom: 30px;
        text-align: center;
        font-weight: 600;
        font-size: 20px;
        color: #409EFF;
    }

    .sign-in-input {
        margin-bottom: 20px;
    }
    .sign-in-submit{
        margin-top: 20px;
        display: flex;
        justify-content: center;
    }
    .login-container{
        padding: 0 10px;
    }
    .login-text{
        color: #409EFF;
        font-size: 16px;
        cursor:pointer;
    }
    /deep/ .el-upload-dragger{
        width: 300px;
        height: 100px;

    }
</style>
