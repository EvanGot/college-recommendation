<template>
    <div class="main">
        <div class="head">
            <el-card class="card">
                <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                    <el-form-item label="专业" prop="name">
                        <el-select v-model="form.name" multiple  placeholder="请选择">
                            <el-option
                                    v-for="item in specializedList"
                                    :key="item.value"
                                    :label="item.name"
                                    :value="item.name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="分数" prop="score">
                        <el-input v-model.number="form.score" placeholder="请输入分数" />

                    </el-form-item>
                    <el-form-item label="城市" prop="address">
                        <el-input v-model="form.address" placeholder="请输入城市" />
                    </el-form-item>
                </el-form>
                <el-row>
                    <el-col>
                        <el-button @click="reset" style="margin-left: 20%">重 置</el-button>
                        <el-button type="primary" @click="submitForm">查询</el-button>
                        <el-button type="success" @click="downloadfile" v-if="schollList.length>0">下载</el-button>
                    </el-col>
                </el-row>


            </el-card>
        </div>
        <span style="color: #F56C6C">1-20为冲刺院校，21-40为稳院校，41-60为保送院校</span>
        <el-table v-loading="loading" stripe :data="schollList" style="margin-top: 20px">
            <el-table-column label="学校" align="center" prop="sname" />
            <el-table-column label="公办/民办" align="center" prop="type" />
            <el-table-column label="本科/专科" align="center" prop="sctype" />
            <el-table-column label="专业" align="center" prop="name" />
            <el-table-column label="年份" align="center" prop="year" />
            <el-table-column label="分数" align="center" prop="score" />
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
        components:{
            pagination
        },
        data(){
            return{
                total: 0,
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                    name: '',
                    score:'',
                    address:'',
                },
                loading:false,
                schollList:[],
                form:{
                    address:''

                },
                specializedList:[],
                rules:{

                    score: [
                        { required: true, message: '分数不能为空'},
                        { type: 'number', message: '分数必须为数字值'}
                    ],

                }
            }
        },
        created(){
            this.reset();
            this.getzy();

        },
        methods:{
            downloadfile(){
                window.location.href='http://localhost:8088/specialized/importTemplate?name='+JSON.stringify(this.form.name)+'&score='+this.form.score+'&address='+this.form.address
                this.$message.success('下载成功');

            },
            getList() {
                this.loading = true;
                this.$axios.get('/specialized/findrecom?page='+this.queryParams.pageNum+'&limit='+this.queryParams.pageSize+'&name='+JSON.stringify(this.form.name)+'&score='+this.form.score+'&address='+this.form.address)
                    .then(res => {
                        this.loading = false;
                        this.schollList = res.data.data;
                        this.total = res.data.count;
                        this.loading = false;

                    })
                    .catch(() => {
                        this.$message.error("服务器连接失败，请稍后重试......");
                    });

            },
            submitForm() {
                this.$refs.form.validate(valid => {
                    if (valid){
                        this.loading = true
                        this.getList();
                    }
                    else {
                        return false
                    }
                })

            },
            getzy(){
                this.$axios.get('/specialized/findgroup')
                    .then(res => {
                        this.specializedList = res.data.data;
                    })
                    .catch(() => {
                        this.$message.error("服务器连接失败，请稍后重试......");
                    });
            },
            reset() {
                this.form = {
                    name: null,
                    score: null,
                    address:null
                };
            },
        }
    }
</script>

<style scoped>
    .head{
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .card{
        width: 30%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

</style>
