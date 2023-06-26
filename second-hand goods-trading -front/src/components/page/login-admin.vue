<template>
    <div class="login-container">
        <el-card class="box-card">
            <div class="login-body">
                <div class="login-title">管理员登陆界面</div>
                <el-form ref="form" :model="userForm">
                    <el-input placeholder="请输入管理员账号" v-model="userForm.accountNumber" class="login-input">
                        <template slot="prepend">
                            <div class="el-icon-user-solid"></div>
                        </template>
                    </el-input>
                    <el-input placeholder="请输入管理员密码" v-model="userForm.adminPassword" class="login-input"
                              @keyup.enter.native="login"  show-password>
                        <template slot="prepend">
                            <div class="el-icon-lock"></div>
                        </template>
                    </el-input>
                    <el-input placeholder="请输入验证码..." v-model="userForm.verifyCode" class="login-input">
                        <template slot="prepend">
                            <img :src="verifySrc" alt="图片无法加载" @click="changeVerify()" style='height: 30px;width: 90px'>
                        </template>
                    </el-input>
                    <div class="login-submit" style="margin-top: 20px"  >
                        <el-button type="primary" @click="login">登录</el-button>
                        <el-button type="warning" autocomplete="off" @click="$router.push('/login')" style="margin-left: 20px">返回</el-button>
                    </div>
                </el-form>
            </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "login",
        data() {
            return {
                userForm: {
                    accountNumber: '11',
                    adminPassword: '123123',
                    verifyCode: '123123'
                },
                verifySrc: "http://localhost:8080/code/verify"
            };
        },
        methods: {
            //验证码
            changeVerify(){
                this.verifySrc="http://localhost:8080/code/verify?"+new Date().getTime()
            },
            login() {
                this.$api.adminLogin({
                    accountNumber: this.userForm.accountNumber,
                    adminPassword: this.userForm.adminPassword,
                    verifyCode: this.userForm.verifyCode
                }).then(res => {
                    console.log(res);
                    if (res.status_code === 1) {
                        console.log(res);
                        this.$sta.isLogin = true;
                        this.$sta.adminName=res.data.adminName;
                        this.$router.replace({path:'/platform-admin'});
                    } else {
                        this.$message.error(res.msg);
                    }
                });

            }
        }
    }
</script>

<style scoped>
    .login-container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        width: 100%;
		background-size: 100%;
		background-image: url('/image/back.png');
    }
    .login-body {
        padding: 30px;
        width: 300px;
        height: 100%;
    }
    .login-title {
        padding-bottom: 30px;
        text-align: center;
        font-weight: 600;
        font-size: 20px;
        color: #409EFF;
        cursor: pointer;
    }
    .login-input {
        margin-bottom: 20px;
    }
    .login-submit {
        display: flex;
        justify-content: center;
    }
    .sign-in-text {
        color: #409EFF;
        font-size: 16px;
        text-decoration: none;
        line-height:28px;
    }
    .other-submit{
        display:flex;
        justify-content: space-between;
        margin-top: 10px;
    }
</style>
