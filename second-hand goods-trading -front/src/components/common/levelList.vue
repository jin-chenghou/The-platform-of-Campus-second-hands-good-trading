<template>

    <div class="main-border">
        <el-table
                :data="Level"
                stripe
                style="width: 100%;color: #5a5c61;">
            <el-table-column
                    prop="id"
                    label="ID"
                    show-overflow-tooltip
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="level"
                    label="等级"
                    show-overflow-tooltip
                    >
            </el-table-column>
            <el-table-column
                    prop="rate"
                    label="费率"
                    show-overflow-tooltip
                    min-width="100"
                   >
            </el-table-column>
            <el-table-column
                prop="remark"
                label="备注"
                show-overflow-tooltip
                min-width="100"
                >
            </el-table-column>

        </el-table>
        <div class="block">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="nowPage"
                    :page-size="8"
                    background
                    layout="prev, pager, next,jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "levelList",
        created() {
            this.getLevel();
        },
        methods:{
            getLevel(){
                this.$api.getLevelList({
                    page: this.nowPage,
                    nums:8
                }).then(res => {
                    if(res.status_code==1){
                        this.Level = res.data;
                    }else {
                        this.$message.error(res.msg)
                    }

                }).catch(e => {
                    console.log(e)
                })
            },
            deleteOrder(index){
                this.$api.deleteOrder({
                    id:this.Order[index].id
                }).then(res=>{
                    if(res.status_code==1){
                        this.getOrder();
                    }else {
                        this.$message.error(res.msg)
                    }

                }).catch(e => {
                    console.log(e)
                })
            },
            handleCurrentChange(val) {
                this.nowPage = val;
                this.getOrder();
            },
        },
        data(){
            return {
                mode:1,
                nowPage: 1,
                total: 0,
                Order: [],
                Level: []
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