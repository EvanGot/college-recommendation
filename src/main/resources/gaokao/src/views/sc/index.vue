<template>
    <div class="app-container">

        <el-row :gutter="10" class="mb8">

            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDelete"
                >取消收藏</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" stripe :data="tbuserList" @selection-change="handleSelectionChange" style="margin-top: 20px">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="学校" align="center" prop="school" />
            <el-table-column label="名称" align="center" prop="name" />
            <el-table-column label="地址" align="center" prop="address" />
            <el-table-column label="公办/民办" align="center" prop="type" />
            <el-table-column label="本科/专科" align="center" prop="sctype" />
            <el-table-column label="优秀专业" align="center" prop="zytype" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)">取消收藏</el-button>
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
                url:'http://localhost:8088',
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
                },
                // 表单参数
                form: {
                    head:''
                },
                // 表单校验
                rules: {

                    title: [
                        { required: true, message: "标题不能为空", trigger: "blur" }
                    ],

                },

            }
        },
        components:{
            pagination
        },

        created(){
            this.user = JSON.parse(sessionStorage.user);
            this.getList();
        },

        methods:{


            /** 查询列表 */
            getList() {
                this.loading = true;
                this.$axios.get('/sc/pagegetall?page='+this.queryParams.pageNum+'&limit='+this.queryParams.pageSize+'&uid='+this.user.id)
                    .then(res => {
                        console.log(res);
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
                    pic: null,
                    count: null,
                };
            },

            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.queryParams.nickname = '';
                this.handleQuery();
            },
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.id)
                this.single = selection.length!==1
                this.multiple = !selection.length
            },


            /** 删除按钮操作 */
            handleDelete(row) {
                console.log(row)
                const userIds = row.id || this.ids;
                this.$confirm("确认要取消收藏该数据吗, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        // 删除逻辑
                        this.$axios.get('/sc/del?id='+userIds)
                            .then(res => {
                                if (res.data.code ==='200'){
                                    this.$message.success('取消成功');
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
