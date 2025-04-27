<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

            <el-form-item label="名称" prop="nickname">
                <el-input
                        v-model="queryParams.nickname"
                        placeholder="请输入名称"
                        clearable
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        plain
                        icon="el-icon-plus"
                        size="mini"
                        @click="handleAdd"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="el-icon-edit"
                        size="mini"
                        :disabled="single"
                        @click="handleUpdate"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDelete"
                >删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" stripe :data="tbuserList" @selection-change="handleSelectionChange" style="margin-top: 20px">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="账号" align="center" prop="username" />
            <el-table-column label="名称" align="center" prop="nickname" />
            <el-table-column label="电话" align="center" prop="tel" />
            <el-table-column label="密码" align="center" prop="pass" />
            <el-table-column label="头像" align="center" prop="avatar" width="100">
                <template slot-scope="scope">
                    <div>
                        <el-popover placement="right" trigger="hover">
                            <el-image
                                    style="height: 300px"
                                    :src="`${url}${uploadhead}${scope.row.head}`"
                                    fit="contain"
                            />
                            <el-image
                                    slot="reference"
                                    style="height: 100px"
                                    :src="`${url}${uploadhead}${scope.row.head}`"
                                    fit="contain"
                            />
                        </el-popover>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="语文" align="center" prop="chinese" />
            <el-table-column label="数学" align="center" prop="math" />
            <el-table-column label="英语" align="center" prop="english" />
            <el-table-column label="选科1" align="center" prop="subject1">
                <template slot-scope="scope">
                    <span>{{scope.row.subject1}}:{{scope.row.subject1score}}</span>
                </template>

            </el-table-column>
            <el-table-column label="选科2" align="center" prop="subject1">
                <template slot-scope="scope">
                    <span>{{scope.row.subject2}}:{{scope.row.subject2score}}</span>
                </template>

            </el-table-column>
            <el-table-column label="选科3" align="center" prop="subject1">
                <template slot-scope="scope">
                    <span>{{scope.row.subject3}}:{{scope.row.subject3score}}</span>
                </template>

            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleUpdate(scope.row)">修改</el-button>
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


        <!-- 添加或修改用户管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="账号" v-if="form.id === null" prop="username" >
                            <el-input v-model="form.username" placeholder="请输入账号" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码" v-if="form.id === null" prop="pass">
                            <el-input v-model="form.pass" value="123456" placeholder="请输入密码" type="password" maxlength="20" show-password />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="名称" prop="nickname">
                            <el-input v-model="form.nickname" placeholder="请输入名称" maxlength="30"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="电话" prop="tel">
                            <el-input v-model="form.tel" placeholder="请输入电话" maxlength="11"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="头像">
                    <el-upload class="avatar-uploader"
                               action="/upload/uploadImage"
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <el-image v-if="form.head !==null&&form.head !==''" :src="url+uploadhead+form.head" class="avatar"></el-image>
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import pagination from '../../components/Pagination';
    export default {
        name: "index",
        data(){
            return{
                fileList: [],
                url:'http://localhost:8088',
                uploadhead:"/public/",
                uploadimg:'/upload/image',
                imageUrl: '',
                baseUrl:'',
                // 默认密码
                initPassword: 123456,
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
                },
                // 表单参数
                form: {
                    head:''
                },
                // 表单校验
                rules: {
                    username: [
                        { required: true, message: "用户名称不能为空", trigger: "blur" },
                        { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
                    ],
                    nickname: [
                        { required: true, message: "用户昵称不能为空", trigger: "blur" }
                    ],
                    tel: [
                        { required: true, message: "用户电话不能为空", trigger: "blur" },
                        {  min: 11, max: 11, message: '用户电话长度必须等于11', trigger: 'blur' }
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

            handleAvatarSuccess(res, file) {
                console.log('file',file)
                //获取到file下面的响应数据
                this.form.head= res;
                console.log('图片路径：',this.form)
            },
            //判断用户上传的图片格式和大小
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';

                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isJPG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!');
                    return false;
                }
                else {
                    this.form.head= file.name
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                    return false;
                }
            },
            /** 查询用户管理列表 */
            getList() {
                this.loading = true;
                this.$axios.get('/user/pagelist?page='+this.queryParams.pageNum+'&limit='+this.queryParams.pageSize+'&nickname='+this.queryParams.nickname)
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
                    head: null,
                    nickname: null,
                    username: null,
                    tel: null,
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
            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
                this.title = "添加用户";
                this.form.pass = this.initPassword;


            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                const ids = row.id || this.ids;
                this.$axios.get('/user/get?id='+ids)
                    .then(res => {
                        console.log(res)
                        console.log(res.data.code ==='200')
                        if (res.data.code ==='200'){
                            this.form = res.data.data
                            this.open=true
                            this.title = "修改";

                            console.log('this.form',this.form)
                        }


                    })
                    .catch(() => {
                        this.$message.error("服务器连接失败，请稍后重试......");
                    });
            },
            /** 提交按钮 */
            submitForm() {
                this.$refs.form.validate(valid => {
                    if (valid){
                        console.log('form',this.form)
                        if (this.form.id !== null&&this.form.id !=='') {
                            this.$axios.get('/user/update?id='+this.form.id+'&head='+this.form.head+'&nickname='+this.form.nickname+'&tel='+this.form.tel)
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
                            this.$axios.get('/user/add?username='+this.form.username+'&pass='+this.form.pass+'&head='+this.form.head+'&nickname='+this.form.nickname+'&tel='+this.form.tel)
                                .then(res => {
                                    console.log(res);
                                    console.log(res.data.code ==='200')
                                    if (res.data.code ==='200'){
                                        console.log("成功")
                                        this.$message.success('添加成功');
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
                this.$confirm("确认要删除该用户吗, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        // 删除逻辑
                        this.$axios.get('/user/del?id='+userIds)
                            .then(res => {
                                console.log('删除',res);
                                console.log(res.data.code ==='200')
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
