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
                >取消对比</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" stripe :data="tbuserList" @selection-change="handleSelectionChange" style="margin-top: 20px">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="学校" align="center" prop="sname" />
            <el-table-column label="名称" align="center" prop="zname" />
            <el-table-column label="地址" align="center" prop="address" />
            <el-table-column label="公办/民办" align="center" prop="type" />
            <el-table-column label="本科/专科" align="center" prop="sctype" />
            <el-table-column label="优秀专业" align="center" prop="zytype" />
            <el-table-column label="分数" align="center" prop="score">
                <template slot-scope="scope">
                    <span :style="{ color: getScoreColor(scope.row.score) }">{{ scope.row.score }}</span>
                </template>
            </el-table-column>
            <el-table-column label="评分" align="center">
                <template slot-scope="scope">
                    <el-tag :type="getRatingType(scope.row.score)">{{ getRating(scope.row.score) }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)">取消对比</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 对比总结卡片 -->
        <el-card v-if="tbuserList.length > 0" style="margin-top: 20px">
            <template #header>
                <div class="card-header">对比总结</div>
            </template>
            <p>最高分数院校专业: {{ getMaxScoreItem().sname }} - {{ getMaxScoreItem().zname }} (分数: {{ getMaxScore() }})</p>
            <p>最低分数院校专业: {{ getMinScoreItem().sname }} - {{ getMinScoreItem().zname }} (分数: {{ getMinScore() }})</p>
            <p>平均分数: {{ getAverageScore().toFixed(2) }}</p>
        </el-card>

        <pagination
                v-show="total > 0"
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
    data() {
        return {
            user: [],
            url: 'http://localhost:8088',
            uploadhead: "/public/",
            uploadimg: '/upload/image',
            imageUrl: '',
            baseUrl: '',
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
                nickname: ''
            },
            // 表单参数
            form: {
                head: ''
            },
            // 表单校验
            rules: {
                title: [
                    { required: true, message: "标题不能为空", trigger: "blur" }
                ]
            }
        };
    },
    components: {
        pagination
    },
    created() {
        this.user = JSON.parse(sessionStorage.user);
        this.getList();
    },
    methods: {
        /** 查询列表 */
        getList() {
            this.loading = true;
            this.$axios.get('/comparison/pagegetall?page=' + this.queryParams.pageNum + '&limit=' + this.queryParams.pageSize + '&uid=' + this.user.id)
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
                count: null
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
            this.ids = selection.map(item => item.id);
            this.single = selection.length !== 1;
            this.multiple = !selection.length;
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            console.log(row);
            const userIds = row.id || this.ids;
            this.$confirm("确认要取消对比该数据吗, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
              .then(() => {
                    // 删除逻辑
                    this.$axios.get('/comparison/del?id=' + userIds)
                      .then(res => {
                            if (res.data.code === '200') {
                                this.$message.success('取消成功');
                                this.getList();
                            } else {
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
        // 根据分数返回不同颜色
        getScoreColor(score) {
            const maxScore = this.getMaxScore();
            const minScore = this.getMinScore();
            if (score === maxScore) {
                return 'green';
            } else if (score === minScore) {
                return 'red';
            }
            return 'black';
        },
        // 获取最高分数
        getMaxScore() {
            if (this.tbuserList.length === 0) return 0;
            return Math.max(...this.tbuserList.map(item => parseFloat(item.score)));
        },
        // 获取最低分数
        getMinScore() {
            if (this.tbuserList.length === 0) return 0;
            return Math.min(...this.tbuserList.map(item => parseFloat(item.score)));
        },
        // 获取最高分数的院校专业
        getMaxScoreItem() {
            const maxScore = this.getMaxScore();
            return this.tbuserList.find(item => parseFloat(item.score) === maxScore) || {};
        },
        // 获取最低分数的院校专业
        getMinScoreItem() {
            const minScore = this.getMinScore();
            return this.tbuserList.find(item => parseFloat(item.score) === minScore) || {};
        },
        // 获取平均分数
        getAverageScore() {
            if (this.tbuserList.length === 0) return 0;
            const totalScore = this.tbuserList.reduce((sum, item) => sum + parseFloat(item.score), 0);
            return totalScore / this.tbuserList.length;
        },
        // 根据分数获取评分
        getRating(score) {
            const maxScore = this.getMaxScore();
            const minScore = this.getMinScore();
            const range = maxScore - minScore;
            if (score >= maxScore - range * 0.2) {
                return '优秀';
            } else if (score >= maxScore - range * 0.5) {
                return '良好';
            } else if (score >= maxScore - range * 0.8) {
                return '一般';
            } else {
                return '较差';
            }
        },
        // 根据评分获取标签类型
        getRatingType(score) {
            const rating = this.getRating(score);
            switch (rating) {
                case '优秀':
                    return 'success';
                case '良好':
                    return 'info';
                case '一般':
                    return 'warning';
                case '较差':
                    return 'danger';
                default:
                    return '';
            }
        }
    }
};
</script>

<style scoped>
.app-container {
    padding: 20px;
    overflow-x: auto; /* 添加水平滚动条 */
}

.el-table {
    font-size: 14px;
    border-radius: 8px;
    overflow: hidden;
}

.el-table__header th {
    background-color: #f5f7fa;
    color: #606266;
    font-weight: 600;
    border-bottom: 1px solid #ebeef5;
}

.el-table__body tr:nth-child(even) {
    background-color: #fafafa;
}

.el-table__body tr:hover {
    background-color: #f2f6fc;
}

.el-card {
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card-header {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
}
</style>