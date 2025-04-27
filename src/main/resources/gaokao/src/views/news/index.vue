<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

            <el-form-item label="标题" prop="nickname">
                <el-input
                        v-model="queryParams.title"
                        placeholder="请输入标题"
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
            <el-table-column label="标题" align="center" prop="title" />
            <el-table-column label="缩略图" align="center" prop="avatar" width="100">
                <template slot-scope="scope">
                    <div>
                        <el-popover placement="right" trigger="hover">
                            <el-image
                                    style="height: 300px"
                                    :src="`${url}${uploadhead}${scope.row.pic}`"
                                    fit="contain"
                            />
                            <el-image
                                    slot="reference"
                                    style="height: 100px"
                                    :src="`${url}${uploadhead}${scope.row.pic}`"
                                    fit="contain"
                            />
                        </el-popover>
                    </div>
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
                    <el-form-item label="标题" prop="nickname">
                        <el-input v-model="form.title" placeholder="请输入名称" maxlength="30"/>
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="缩略图">
                            <el-upload class="avatar-uploader"
                                       action="/upload/uploadImage"
                                       :show-file-list="false"
                                       :on-success="handleAvatarSuccess"
                                       :before-upload="beforeAvatarUpload">
                                <el-image v-if="form.pic!==null&&form.pic !==''" :src="url+uploadhead+form.pic" class="avatar"></el-image>
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="视频" prop="video">
                            <el-upload class="avatar-uploader"
                                       action="/upload/uploadImage"
                                       :show-file-list="false"
                                       :on-success="handleAvatarSuccessvideo"
                                       :before-upload="beforeAvatarUploadvideo">
                                <el-button size="small" type="primary">点击上传</el-button>
                                <span v-if="form.video !==null&&form.video !==''" style="margin-left: 10px">{{form.video}}</span>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-form-item label="内容" prop="nickname">
                        <editor v-model="form.content"
                                :min-height="192"
                        />

                    </el-form-item>

                </el-row>

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
    import editor from '../../components/Editor';



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
                    title: '',
                },
                // 表单参数
                form: {
                    pic:''
                },
                // 表单校验
                rules: {

                },

            }
        },
        components:{
            pagination,editor
        },

        created(){
            this.getList();
        },

        methods:{

            handleAvatarSuccessvideo(res, file) {
                console.log('file',file)
                //获取到file下面的响应数据
                this.form.video= res;
                console.log('图片路径：',this.form)
            },
            beforeAvatarUploadvideo(file) {
                const isJPG = file.type === 'video/mp4';

                if (!isJPG) {
                    this.$message.error('上传视频只能是 mp4 格式!');
                    return false;
                }
                else {
                    this.form.video= file.name
                }

            },

            handleAvatarSuccess(res, file) {
                console.log('file',file)
                //获取到file下面的响应数据
                this.form.pic= res;
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
                    this.form.pic= file.name

                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                    return false;

                }
            },
            /** 查询用户管理列表 */
            getList() {
                this.loading = true;
                this.$axios.get('/news/pagegetall?page='+this.queryParams.pageNum+'&limit='+this.queryParams.pageSize+'&title='+this.queryParams.title)
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
                    id: '',
                    pic: '',
                    title: '',
                    content: '',
                    video: '',

                };
            },

            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.queryParams.title = '';
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
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.title = "修改";
                this.reset();
                const ids = row.id || this.ids;
                this.$axios.get('/news/get?id='+ids)
                    .then(res => {
                        console.log(res)
                        console.log(res.data.code ==='200')
                        if (res.data.code ==='200'){
                            this.form = res.data.data
                            this.open=true
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
                            this.$axios.get('/news/update?id='+this.form.id+'&pic='+this.form.pic+'&title='+this.form.title+'&content='+this.form.content+'&video='+this.form.video)
                                .then(res => {
                                    console.log(res);
                                    console.log(res.data.code ==="200")
                                    if (res.data.code ==="200"){
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
                            this.$axios.get('/news/add?content='+this.form.content+'&pic='+this.form.pic+'&title='+this.form.title+'&video='+this.form.video)
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
                this.$confirm("确认要删除该记录吗, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        // 删除逻辑
                        this.$axios.get('/news/del?id='+userIds)
                            .then(res => {
                                console.log('删除',res);
                                if (res.data.code ==="200"){
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
