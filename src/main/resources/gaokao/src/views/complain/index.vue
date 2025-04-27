<template>
    <div class="app-container">

        <el-table v-loading="loading" stripe :data="tbuserList" @selection-change="handleSelectionChange" style="margin-top: 20px">
            <el-table-column label="反馈内容" align="center" prop="content" />
            <el-table-column label="反馈时间" align="center" prop="time" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)">删除</el-button>

                </template>
            </el-table-column>
        </el-table>
        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList"
        />

    </div>
</template>

<script>
    import pagination from '../../components/Pagination';
    export default {
        name: "index",
        data(){
            return{
                user:[],
                fileList: [],
                url:'',
                uploadhead:"/public/",
                uploadimg:'/upload/image',
                imageUrl: '',
                baseUrl:'',

                // 遮罩层
                loading: false,
                // 选中数组
                ids: [],
                // 非单个禁用
                single: true,
                // 非多个禁用
                multiple: true,
                // 显示搜索条件
                showSearch: true,
                // 总条数
                total: 0,
                // 用户管理表格数据
                tbuserList: [],
                // 弹出层标题
                title: "",
                // 是否显示弹出层
                open: false,
                // 查询参数
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                    nickname: '',
                    address:'',
                    type:'',
                    zytype:'',
                    sctype:''
                },
                // 表单参数
                form: {
                    head:''
                },
                // 表单校验
                rules: {

                    content: [
                        { required: true, message: "反馈内容不能为空", trigger: "blur" }
                    ],

                },

            }
        },
        components:{
            pagination
        },

        created(){
            this.getList();
        },

        methods:{
            /** 查询管理列表 */
            getList() {
                this.loading = true;
                this.$axios.get('/complain/pagegetall?page='+this.queryParams.pageNum+'&limit='+this.queryParams.pageSize)
                    .then(res => {
                        this.loading = false;
                        this.tbuserList = res.data.data;
                        this.total = res.data.count;
                        this.loading = false;

                    })
                    .catch(() => {
                        this.$message.error("服务器连接失败，请稍后重试......");
                    });

            },
            // 取消按钮
            cancel() {
                this.open = false;
                this.reset();
            },
            // 表单重置
            reset() {
                this.form = {
                    id: null,
                    name: null,
                    address: null,

                };
            },

            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.handleQuery();
            },
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.id)
                this.single = selection.length!==1
                this.multiple = !selection.length
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
                this.title = "添加";
            },
            //跳转到专业详情页面，传递专业 ID
            handlezy(row){
                const ids = row.id || this.ids;
                this.$router.push('/specialized/see?tid='+ids);
            },

            /** 提交按钮 */
            submitForm() {
                this.$refs.form.validate(valid => {
                    if (valid){
                        console.log('form',this.form)
                        if (this.form.id !== null&&this.form.id !=='') {
                            this.$axios.get('/school/update?id='+this.form.id+'&name='+this.form.name+'&address='+this.form.address)
                                .then(res => {
                                    console.log(res);
                                    console.log(res.data.code ==='200')
                                    if (res.data.code ==='200'){
                                        console.log("成功")
                                        this.$message.success('修改成功');
                                        this.open=false
                                        this.getList();
                                    }else {
                                        this.$message.error(res.data.msg);
                                    }

                                })
                                .catch(() => {
                                    this.$message.error("服务器连接失败，请稍后重试......");
                                });
                        }else {
                            this.$axios.get('/complain/add?content='+this.form.content+'&uid='+this.user.id)
                                .then(res => {
                                    console.log(res);
                                    console.log(res.data.code ==='200')
                                    if (res.data.code ==='200'){
                                        this.$message.success('反馈成功');
                                        this.open=false
                                        this.getList();

                                    }else {
                                        this.$message.error(res.data.msg);

                                    }

                                })
                                .catch(() => {
                                    this.$message.error("服务器连接失败，请稍后重试......");
                                });
                        }



                    }
                    else {
                        return false
                    }
                })

            },

            /** 删除按钮操作 */
            handleDelete(row) {
                console.log(row)
                const userIds = row.id || this.ids;
                this.$confirm("确认要删除吗, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        // 删除逻辑
                        this.$axios.get('/complain/del?id='+userIds)
                            .then(res => {
                                if (res.data.code ==='200'){
                                    this.$message.success('删除成功');
                                    this.getList();

                                }else {
                                    this.$message.error('失败');

                                }

                            })
                            .catch(() => {
                                this.$message.error("服务器连接失败，请稍后重试......");
                            });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "已取消删除",
                        });
                    });

            },




        },

    }
</script>

<style scoped>
    .app-container {
        padding: 20px;
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
        border: 1px dashed  #8c939d;
        border-radius: 20px;
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

</style>
