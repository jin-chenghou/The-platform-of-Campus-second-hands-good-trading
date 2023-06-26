<template>
    <div class="main-border">
        <el-menu default-active="1" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="1">正常用户</el-menu-item>
            <el-menu-item index="2">违规用户</el-menu-item>
            <el-menu-item index="3">管理员</el-menu-item>

            <div v-show="this.mode ==3" class="addAdminButton">
                <el-button size="mini" type="success" @click="adminRegVisible = true">添加管理员</el-button>
                <el-dialog
                        title="添加管理员"
                        :visible.sync="adminRegVisible"
                        width="25%"
                       >
                    <span style="margin-left: 10px">新增管理员名称</span>
                    <el-input v-model="adminName"  maxlength="8" placeholder="请输入管理员名称" style="padding: 10px 0" clearable required></el-input>
                    <span style="margin-left: 10px">新增管理员账户</span>
                    <el-input v-model="adminAccount" minlength="8" maxlength="10" placeholder="请输入管理员账户" style="padding: 10px 0"
                               clearable required></el-input>
                    <span style="margin-left: 10px">新增管理员密码</span>
                    <el-input v-model="adminPassword" minlength="8" placeholder="请输入管理员密码" style="padding: 10px 0" show-password required></el-input>
                    <span style="margin-left: 10px">确认管理员密码</span>
                    <el-input v-model="adminRePassword" minlength="10" placeholder="请再次输入管理员密码" style="padding: 10px 0" show-password required></el-input>
                    <span slot="footer" class="dialog-footer">
                        <el-button type="primary" @click="regAdmin">添加</el-button>
                    </span>
                </el-dialog>

            </div>
        </el-menu>
        <el-table v-show="this.mode == 1"
                  :data="userData"
                  @cell-dblclick="doubleClick"
                  :key="key"
                  stripe
                  style="width: 100%;color: #5a5c61;">
            <el-table-column label="头像" >
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="60" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column
                    prop="accountNumber"
                    label="用户账号"
                    show-overflow-tooltip
                    min-width="150"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="nickname"
                    label="用户昵称"
                    show-overflow-tooltip
                    min-width="150"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="signInTime"
                    label="注册时间"
                    show-overflow-tooltip
                    width="200">
            </el-table-column>
            <el-table-column
                prop="phone"
                label="手机号"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="email"
                label="邮箱"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="city"
                label="城市"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="sex"
                label="性别"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="bankAccount"
                label="银行账户"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column prop="level" label="钱包金额(双击编辑)" width="150">
                <template slot-scope="scope">
<!--                    <el-input v-if="scope.row.isSelected"  v-model="scope.row.wallet"-->
<!--                              @focus="focusEvent(scope.row, scope.column)"-->
<!--                              v-focus-->
<!--                    ></el-input>-->
                    <el-input v-if="scope.row.index + ',' + scope.column.index == currentCell"
                              v-model="scope.row.wallet"
                              v-focus
                              @focus="focusEvent(scope.row, scope.column)"
                              :ref="scope.row.index + ',' + scope.column.index"
                    ></el-input>
                    <span v-else>{{ scope.row.wallet }}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="integral"
                label="积分"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column label="营业执照">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="60" :src="scope.row.businessLicense" :fit='fill'></el-avatar>
                </template>
            </el-table-column>
            <el-table-column label="身份证">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="60" :src="scope.row.idCard" :fit='fill'></el-avatar>
                </template>
            </el-table-column>

            <el-table-column prop="level" label="商家等级(双击编辑)" width="200">
                <template slot-scope="scope">
<!--                    <el-input v-if="scope.row.isSelected"  v-model="scope.row.level"-->
<!--                              @focus="focusEvent(scope.row, scope.column)"-->
<!--                              v-focus-->
<!--                             ></el-input>-->
                    <el-input v-if="scope.row.index + ',' + scope.column.index == currentCell"
                              v-model="scope.row.level"
                              :ref="scope.row.index + ',' + scope.column.index"
                              @focus="focusEvent(scope.row, scope.column)"
                              v-focus
                    ></el-input>
                    <span v-else>{{ scope.row.level }}</span>
                </template>
            </el-table-column>


            <el-table-column
                :formatter='typeFomat'
                prop="type"
                label="类型"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>


            <el-table-column label="操作" width='150px'>
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="danger"
                            @click="sealUser(scope.$index)">封号</el-button>
                    <el-button size="mini" type="success" @click="onBlur(scope.row, scope.column)">更新</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-table v-show="this.mode == 2"
                  :data="badUserData"
                  @cell-dblclick="doubleClick"
                  stripe
                  style="width: 100%;color: #5a5c61;">
            <el-table-column
                    label="头像"
                    width="50">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="23" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column label="头像" >
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="60" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column
                prop="accountNumber"
                label="用户账号"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="nickname"
                label="用户昵称"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="signInTime"
                label="注册时间"
                show-overflow-tooltip
                width="200">
            </el-table-column>
            <el-table-column
                prop="phone"
                label="手机号"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="email"
                label="邮箱"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="city"
                label="城市"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="sex"
                label="性别"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column
                prop="bankAccount"
                label="银行账户"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column prop="level" label="钱包金额(双击编辑)" width="150">
                <template slot-scope="scope">
                    <el-input v-if="scope.row.index + ',' + scope.column.index == currentCell"
                              v-model="scope.row.wallet"
                              v-focus
                              @focus="focusEvent(scope.row, scope.column)"
                              :ref="scope.row.index + ',' + scope.column.index"
                    ></el-input>
                    <span v-else>{{ scope.row.wallet }}</span>
                </template>
            </el-table-column>
            <el-table-column
                prop="integral"
                label="积分"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>
            <el-table-column label="营业执照">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="60" :src="scope.row.businessLicense" :fit='fill'></el-avatar>
                </template>
            </el-table-column>
            <el-table-column label="身份证">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="60" :src="scope.row.idCard" :fit='fill'></el-avatar>
                </template>
            </el-table-column>
            <el-table-column prop="level" label="商家等级(双击编辑)" width="200">
                <template slot-scope="scope">
                    <el-input v-if="scope.row.index + ',' + scope.column.index == currentCell"
                              v-model="scope.row.level"
                              :ref="scope.row.index + ',' + scope.column.index"
                              @focus="focusEvent(scope.row, scope.column)"
                              v-focus
                    ></el-input>
                    <span v-else>{{ scope.row.level }}</span>
                </template>
            </el-table-column>
            <el-table-column
                :formatter='typeFomat'
                prop="type"
                label="类型"
                show-overflow-tooltip
                min-width="150"
                width="150">
            </el-table-column>

            <el-table-column label="操作" width='150px'>
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="success"
                            @click="unsealUser(scope.$index)">解封</el-button>
                    <el-button size="mini" type="success" @click="onBlur(scope.row, scope.column)">更新</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-table  v-show="this.mode == 3"
                :data="userManage"
                stripe
                style="width: 100%;color: #5a5c61;">
            <el-table-column
                    prop="accountNumber"
                    label="管理员账号"
                    show-overflow-tooltip
                    width="200">
            </el-table-column>
            <el-table-column
                    prop="adminName"
                    label="管理名称"
                    >
            </el-table-column>
        </el-table>
        <div class="block">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="nowPage"
                    :page-size="7"
                    background
                    layout="prev, pager, next,jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "userList",
        directives: {
            focus: {
                inserted: function (el) {
                    el.querySelector('input').focus()
                }
            }
        },
        created() {
            this.getUserData();
        },
        methods: {
            focusEvent (row, column) {
                // row.oldLevel = row.level
                // row.oldWallet = row.wallet
                console.log("focusEvent")
                this.currentCell = row.index + ',' + column.index;

                // 这里必须要setTimeout，因为在点击的时候，input才刚被v-if显示出来，不然拿不到dom
                setTimeout(() => {
                    // 双击后自动获得焦点
                    this.$refs[row.index + ',' + column.index].focus();
                })
            },
            // 双击单元格触发事件
            doubleClick(row, column) {
                // row.isSelected = !row.isSelected
                row.index=row.id;
                column.index=column.property;
                this.currentCell = row.index +','+column.index
                console.log(this.currentCell)
            },

            //输入框失焦事件
            onBlur(row, column) {
                console.log("输入框失焦事件")
                this.updateTable(row)
                this.currentCell = ""
                // row.isSelected = !row.isSelected
                // if (row.level !== row.oldLevel || row.wallet !== row.oldWallet) {
                //     // 。。。此处代码省略（调用修改名称接口）
                //     this.updateTable(row)
                // }

            },
            //更新表格
            updateTable(row) {
                this.$api.updateUser(row).then(res => {
                    if(res.status_code==1){
                        this.$message({
                            message: '修改成功',
                            type: 'success',
                            duration: 1000
                        })
                    }else {
                        this.$message.error(res.msg)
                    }
                }).catch(e => {
                    console.log(e)
                })
            },
            typeFomat(row, column){
                if (row.type === 1) {
                    return "用户";
                } else if (row.type === 2) {
                    return "商户";
                } else {
                    return "-";
                }
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
            handleSelect(val){
                if(this.mode !== val){
                    this.mode = val
                    if(val == 1){
                        this.nowPage = 1;
                        this.getUserData();
                    }
                    if(val == 2){
                        this.nowPage = 1;
                        this.getBadUserData();
                    }
                    if(val == 3){
                        this.nowPage = 1;
                        this.getUserManage();
                    }
                }
            },
            getUserData(){
                //正常普通用户
                this.$api.getUserData({
                    page: this.nowPage,
                    nums:8,
                    status:0
                }).then(res => {
                    if(res.status_code==1){
                        this.userData = res.data.list;
                        // 拿到后台数据后给数组对象每一项中添加了同一个属性isSelected,即：
                        this.userData = this.userData.map(item => {
                            return {...item, isSelected: false}
                        })
                        this.total = res.data.count;
                    }else {
                        this.$message.error(res.msg)
                    }
                }).catch(e => {
                    console.log(e)
                })
            },
            getBadUserData(){
                //违规用户
                this.$api.getUserData({
                    page: this.nowPage,
                    nums:8,
                    status:1
                }).then(res => {
                    if(res.status_code==1){
                        this.badUserData = res.data.list;
                        // 拿到后台数据后给数组对象每一项中添加了同一个属性isSelected,即：
                        this.badUserData = this.badUserData.map(item => {
                            return {...item, isSelected: false}
                        })
                        this.total = res.data.count;
                    }else {
                        this.$message.error(res.msg)
                    }
                }).catch(e => {
                    console.log(e)
                });
            },
            getUserManage(){
                //管理员
                this.$api.getUserManage({
                    page: this.nowPage,
                    nums:8,
                }).then(res => {
                    if(res.status_code==1){
                        this.userManage = res.data.list;
                        this.total = res.data.count;
                    }else {
                        this.$message.error(res.msg)
                    }
                }).catch(e => {
                    console.log(e)
                })
            },
            sealUser(i){
                console.log( this.userData[i].id);
                this.$api.updateUserStatus({
                    id: this.userData[i].id,
                    status:1
                }).then(res => {
                    if(res.status_code==1){
                        this.getUserData();
                    }else {
                        this.$message.error(res.msg)
                    }
                }).catch(e => {
                    console.log(e)
                })
            },
            unsealUser(i){
                this.$api.updateUserStatus({
                    id: this.badUserData[i].id,
                    status:0
                }).then(res => {
                    if(res.status_code==1){
                        this.getBadUserData();
                    }else {
                        this.$message.error(res.msg)
                    }

                }).catch(e => {
                    console.log(e)
                })
            },
            regAdmin(){
                if(this.adminPassword == this.adminRePassword){
                    this.$api.regAdministrator({
                        adminName: this.adminName,
                        accountNumber: this.adminAccount,
                        adminPassword: this.adminPassword,
                    }).then(res => {
                        if(res.status_code==1){
                            this.total = this.total+1;
                            this.nowPage= Math.ceil(this.total/8);
                            console.log(this.nowPage);
                            this.getUserManage();
                        }else {
                            this.$message.error(res.msg)
                        }
                    }).catch(e => {
                        console.log(e);
                        this.$message.error("添加失败，账号重复或网络异常")
                    });
                    this.adminRegVisible = false
                }
                else {
                    this.$message.error("两次输入的密码不一致");
                }
            },
        },
        data(){
            return {
                // 用一个字符串来保存当前双击的是哪一个单元格
                currentCell: "",
                levelShow:false,
                key: Math.random(),
                fill:"fill",
                mode:1,
                nowPage: 1,
                total: 63,
                adminRegVisible: false,
                adminAccount:'',
                adminPassword:'',
                adminRePassword:'',
                adminName:'',
                userData: [],
                badUserData:[],
                userManage:[],
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
    .addAdminButton{
        display:flex;
        justify-content: flex-end;
        align-items: center;
        height: 60px;
        outline: none;
    }
</style>
