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
            <el-form-item label="年份" >
                <el-input
                        v-model="queryParams.year"
                        placeholder="请输入年份"
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
            <el-col :span="1.5">
                <el-button
                        type="info"
                        plain
                        icon="el-icon-back"
                        size="mini"
                        @click="handleback"
                >返回</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" stripe :data="tbuserList" @selection-change="handleSelectionChange" style="margin-top: 20px">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="名称" align="center" prop="name" />
            <el-table-column label="年份" align="center" prop="year" />
            <el-table-column label="分数" align="center" prop="score" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-s-data"
                            @click="handleEacharts(scope.row)">图表</el-button>
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


        <!-- 添加或修改管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="年份" prop="year">
                            <el-input v-model="form.year" placeholder="请输入年份" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="分数" prop="score">
                    <el-input-number v-model="form.score" :min="1" ></el-input-number>
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
    import * as echarts from "echarts";

    export default {
        name: "index",
        data(){
            return{
                myChartStyle: { float: "left", width: "80%", height: "400px" },
                x:"",
                y:"",
                ecopen:false,
                tid:'',
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
                    year:''
                },
                // 表单参数
                form: {
                    head:''
                },
                // 表单校验
                rules: {

                    name: [
                        { required: true, message: "名称不能为空", trigger: "blur" }
                    ],
                    year: [
                        { required: true, message: "年份不能为空", trigger: "blur" }
                    ],
                    score: [
                        { required: true, message: "分数不能为空", trigger: "blur" }
                    ],
                },

            }
        },
        components:{
            pagination
        },

        created(){
            this.getitem()
            this.getList();
        },
        mounted() {
            this.gxinitEcharts()
        },
        methods:{
            gxinitEcharts(){
                const option = {
                    title: {
                        text: '全国高考报名人数',
                        left: 'center',
                        textStyle: {
                            color: "#000",
                        },
                    },
                    tooltip: {
                        trigger: 'axis',
                    },
                    xAxis: {
                        type: 'category',
                        data: ['2018', '2019', '2020', '2021', '2022', '2023', '2024']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} 万'
                        }
                    },
                    series: [
                        {
                            data: [975, 1031, 1071, 1078, 1193, 1291, 1342],

                            type: 'bar'
                        }
                    ]
                };
                const mychart2 = echarts.init(document.getElementById("mychartgx"));
                mychart2.setOption(option);
                //随着屏幕大小调节图表
                window.addEventListener("resize", () => {
                    mychart2.resize();
                });
            },
            handleEacharts(row){
                this.$axios.get('/specialized/findEacharts?tid='+row.tid+'&name='+row.name)
                    .then(res => {
                        console.log(res)
                        if (res.data.code ==='200'){
                            this.$router.push('/specialized/chart?x='+encodeURIComponent(JSON.stringify(res.data.data.listx))+'&y='+encodeURIComponent(JSON.stringify(res.data.data.listy))+'&title='+ res.data.msg+" - "+row.name);
                        }
                    })
                    .catch(() => {
                        this.$message.error("123服务器连接失败，请稍后重试......");
                    });
            },
            handleback(){
                this.$router.go(-1);
            },
            getitem(){
                this.tid=this.$route.query.tid;
            },
            /** 查询管理列表 */
            getList() {
                this.loading = true;
                this.$axios.get('/specialized/pagegetall?page='+this.queryParams.pageNum+'&limit='+this.queryParams.pageSize+'&name='+this.queryParams.nickname+'&year='+this.queryParams.year+'&tid='+this.tid)
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
                    name: null,
                    score: null,
                    year: null,
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
                this.queryParams.year = '';

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
                const ids = row.id || this.ids;
                this.$axios.get('/specialized/get?id='+ids)
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
                            this.$axios.get('/specialized/update?id='+this.form.id+'&name='+this.form.name+'&score='+this.form.score+'&year='+this.form.year)
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
                            this.$axios.get('/specialized/add?name='+this.form.name+'&score='+this.form.score+'&tid='+this.tid+'&year='+this.form.year)
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
                this.$confirm("确认要删除吗, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        // 删除逻辑
                        this.$axios.get('/specialized/del?id='+userIds)
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
