<template>
    <div class="main">
        <el-container class="home-container">
            <!-- header -->
            <el-header>
                <el-row>
                    <el-col :span="4">
                        <div class="logo-p-icon">
                            <el-image :src="logo_home" style="width: 30px;height: 30px"></el-image>
                            <p class="system-name">高考志愿填报推荐系统</p>
                        </div>

                    </el-col>
                    <el-col :offset="12" :span="8" style="min-width: 150px">
                        <el-dropdown style="float: right; margin: 20px 10px">
              <span class="el-dropdown-link" style="color: #fff; cursor: pointer">
                {{user.nickname}} &nbsp;&nbsp;<i class="fa fa-caret-down fa-1x"></i>
              </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="editPasswordDialog = true"
                                >修改密码</el-dropdown-item
                                >
                                <el-dropdown-item @click.native="logout()"
                                >退出系统</el-dropdown-item
                                >
                            </el-dropdown-menu>
                        </el-dropdown>
                        <el-avatar
                                shape="square"
                                :src="`${url}/public/${user.head}`"
                                style="margin: 10px; float: right"
                        ></el-avatar>
                    </el-col>
                </el-row>
                <!-- 修改密码 dialog -->
                <el-dialog
                        title="修改密码"
                        :visible.sync="editPasswordDialog"
                        width="30%"
                        :before-close="closeEditPassword"
                        :close-on-click-modal="false"
                >
                    <el-form
                            ref="editPasswordForm"
                            :rules="editPasswordRule"
                            class="demo-form-inline"
                            :model="editPasswordForm"
                            label-width="90px"
                    >
                        <el-form-item label="原密码:" prop="oldPassword">
                            <el-input
                                    v-model="editPasswordForm.oldPassword"
                                    placeholder="请输入原密码"
                            />
                        </el-form-item>
                        <el-form-item label="新密码:" prop="newPassword">
                            <el-input
                                    v-model="editPasswordForm.newPassword"
                                    placeholder="请输入新密码"
                            />
                        </el-form-item>
                        <el-form-item label="确认密码:" prop="confirmPassword">
                            <el-input
                                    v-model="editPasswordForm.confirmPassword"
                                    placeholder="请确认密码"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-button size="small" @click="closeEditPassword()">取 消</el-button>
                            <el-button size="small" type="primary" @click="editPassword()">保 存</el-button>
                        </el-form-item>
                    </el-form>
                </el-dialog>
            </el-header>

            <el-container style="overflow: auto">
                <!-- 菜单 -->
                <el-aside>
                    <div class="toggle-button" @click="isCollapse = !isCollapse">
                        <i v-if="isCollapse" class="el-icon-s-unfold"></i>
                        <i v-if="!isCollapse" class="el-icon-s-fold"></i>
                    </div>
                    <el-menu
                            router
                            :default-active="activePath"
                            class="el-menu-vertical-demo"
                            :collapse="isCollapse"
                    >
                        <el-menu-item index="/sy" @click="saveActiveNav('/sy')">
                            <i class="el-icon-house"></i>
                            <span slot="title">首页</span>
                        </el-menu-item>
                        <el-menu-item index="/user/list" @click="saveActiveNav('/user/list')">
                            <i class="el-icon-user"></i>
                            <span slot="title">用户管理</span>
                        </el-menu-item>

                        <el-menu-item index="/school/list" @click="saveActiveNav('/school/list')">
                            <i class="el-icon-tickets"></i>
                            <span slot="title">院校专业管理</span>
                        </el-menu-item>
                        <el-menu-item index="/news/list" @click="saveActiveNav('/news/list')">
                            <i class="el-icon-tickets"></i>
                            <span slot="title">资讯推送</span>
                        </el-menu-item>

                        <el-menu-item index="/complain/list" @click="saveActiveNav('/achievement/list')">
                            <i class="el-icon-tickets"></i>
                            <span slot="title">反馈与帮助</span>
                        </el-menu-item>
                    </el-menu>
                </el-aside>
                <el-container>
                    <el-main>
                        <!-- 面包屑 -->
                        <Breadcrumb />
                        <!-- 主要内容 -->
                        <router-view></router-view>
                    </el-main>
                    <el-footer>高考志愿填报推荐系统的设计与实现</el-footer>
                </el-container>
            </el-container>
        </el-container>
    </div>

</template>

<script>
    import Breadcrumb from "../components/Breadcrumb.vue";

    export default {
        name: "home",
        components: { Breadcrumb },
        data(){
            return{
                url:'http://localhost:8088',
                user:[],
                logo_home: require("@/assets/logo/logo.png"),
                avatar: require("@/assets/images/avator.jpg"),
                isCollapse: false,
                // 被激活的链接地址,默认是首页
                activePath: "",
                editPasswordDialog: false,
                editPasswordForm: {
                    oldPassword: "",
                    newPassword: "",
                    confirmPassword: "",
                },
                editPasswordRule: {
                    oldPassword: [
                        { required: true, message: "请输入原密码", trigger: "blur" },
                    ],
                    newPassword: [
                        { required: true, message: "请输入新密码", trigger: "blur" },
                    ],
                    confirmPassword: [
                        { required: true, message: "请确认新密码", trigger: "blur" },
                    ],
                },

            }
        },
        mounted() {
            this.user = JSON.parse(sessionStorage.user);
        },
        created() {
            this.activePath = sessionStorage.getItem("activePath")
                ? sessionStorage.getItem("activePath")
                : "/sy";
        },
        methods: {
            // 保存链接的激活状态
            saveActiveNav(activePath) {
                sessionStorage.setItem("activePath", activePath);
                this.activePath = activePath;
            },
            // 修改密码
            editPassword() {
                this.$refs.editPasswordForm.validate(async (valid) => {
                    console.log(valid)
                    if (!valid) return;

                    if (this.editPasswordForm.oldPassword===this.user.pass){
                        if (this.editPasswordForm.newPassword !== this.editPasswordForm.confirmPassword) {
                            return this.$message.error("两次密码不正确，请重新输入！");
                        }
                        else {
                            const loadingInstance = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            })
                            this.$axios.get('/manager/update?id='+this.user.id+'&pass='+this.editPasswordForm.confirmPassword)
                                .then(res => {
                                    console.log(res);
                                    console.log(res.data.code ==='200')
                                    if (res.data.code ==='200'){
                                        loadingInstance.close();
                                        this.$message.success("密码修改成功，请重新登录！");
                                        sessionStorage.clear();
                                        this.$router.push("/login");
                                    }
                                    else {
                                        this.$message.error(res.data.msg);
                                    }
                                })
                                .catch(() => {
                                    this.$message.error("服务器连接失败，请稍后重试......");
                                });
                        }
                    }
                    else {
                        this.$message.error('原密码不正确');

                    }



                });
            },
            // 取消关闭密码
            closeEditPassword() {
                this.editPasswordDialog = false;
                // 坑：resetFields 方法只能重置带有 props 属性的元素
                this.$refs.editPasswordForm.resetFields();
            },
            // 退出系统
            logout() {
                this.$confirm("确定要退出系统吗?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        // 清除缓存
                        sessionStorage.clear();
                        this.$router.push("/login");
                    })
                    .catch(() => {
                        return false;
                    });
            },
        },
    }
</script>

<style scoped>
    .logo-p-icon{
        display: flex;
        align-items: center;
    }
    .home-container {
        position: absolute;
        height: 100%;
        top: 0px;
        left: 0px;
        width: 100%;
        background: #f2f3f5;
    }

    .el-header {
        background: #2661ef;
        padding: 0 10px;
        overflow: hidden;
    }

    .system-name {
        color: #fff;
        font-size: 18px;
    }

    .el-aside {
        background: white;
        width: auto !important;
    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }

    .el-footer {
        color: #cccccc;
        text-align: center;
        line-height: 60px;
    }

    .el-footer:hover {
        color: #2661ef;
    }

    .toggle-button {
        background-color: #d9e0e7;
        font-size: 18px;
        line-height: 24px;
        color: #fff;
        text-align: center;
        letter-spacing: 0.2em;
        cursor: pointer;
        color: black;
    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }

    .el-menu-item.is-active {
        color: #fff !important;
        font-size: 15px;
        font-weight: bold;
        background-color: #2661ef !important;
        border-radius: 2px;
        height: 50px;
        line-height: 50px;
        box-sizing: border-box;
        margin: 2px 5px 0px 2px;
    }
</style>
