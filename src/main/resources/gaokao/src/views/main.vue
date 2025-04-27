<template>
    <div class="main">
        <el-container>
            <el-header>
                <div class="title">
                    <h1 >高考志愿填报推荐系统的设计与实现</h1>
                </div>
                <el-row :gutter="10" >
                    <el-col :xs="4" :sm="18" :md="20" :lg="21" :xl="22"><div class="grid-content bg-purple">
                        <el-menu  class="el-menu-demo" mode="horizontal"
                                  router
                                  :default-active="activePath"
                                  >
                            <el-menu-item index="/index" @click="saveActiveNav('/index')">
                                <i class="el-icon-house"></i>
                                <span slot="title">首页</span>
                            </el-menu-item>

                            <el-menu-item index="/recom/index" @click="saveActiveNav('/recom/index')">
                                <i class="el-icon-document-copy"></i>
                                <span slot="title">推荐院校</span>
                            </el-menu-item>
                            <el-menu-item index="/school/see" @click="saveActiveNav('/school/see')">
                                <i class="el-icon-tickets"></i>
                                <span slot="title">查看院校</span>
                            </el-menu-item>
                            <el-menu-item index="/news/see" @click="saveActiveNav('/news/see')">
                                <i class="el-icon-news"></i>
                                <span slot="title">资讯推送</span>
                            </el-menu-item>
                            <el-menu-item index="/sc/see" @click="saveActiveNav('/sc/see')">
                                <i class="el-icon-sunny"></i>
                                <span slot="title">我的收藏</span>
                            </el-menu-item>
                            <el-menu-item index="/db/see" @click="saveActiveNav('/db/see')">
                                <i class="el-icon-sort"></i>
                                <span slot="title">院校对比</span>
                            </el-menu-item>
                            <el-menu-item index="/complain/see" @click="saveActiveNav('/complain/see')">
                                <i class="el-icon-box"></i>
                                <span slot="title">帮助与反馈</span>
                            </el-menu-item>
                        </el-menu>
                    </div></el-col>
                    <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="2">
                        <div >
                            <el-dropdown style="float: right; margin: 20px 10px" >
                            <span class="el-dropdown-link" style="color: #000; cursor: pointer">
                               {{user.nickname }}&nbsp;&nbsp;<i class="fa fa-caret-down fa-1x"></i>
                            </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item @click.native="editPasswordDialog = true"
                                    >修改密码</el-dropdown-item
                                    >
                                    <el-dropdown-item @click.native="userdilog = true"
                                    >个人信息</el-dropdown-item
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
                        </div>




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
                <el-dialog  title="修改个人信息"
                            :visible.sync="userdilog"
                            width="30%"
                           >
                    <el-form
                            ref="editPasswordForm"
                            :rules="editUserRule"
                            class="demo-form-inline"
                            :model="editUserForm"
                            label-width="90px"
                    >

                        <el-form-item label="头像:">
                            <el-upload class="avatar-uploader"
                                       action="/upload/uploadImage"
                                       :show-file-list="false"
                                       :on-success="handleAvatarSuccess"
                                       :before-upload="beforeAvatarUpload">
                                <el-image v-if="editUserForm.head !==null&&editUserForm.head !==''" :src="url+uploadhead+editUserForm.head" class="avatar"></el-image>
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                        </el-form-item>

                        <el-form-item prop="nickname" label="名称:">
                            <el-input
                                    v-model="editUserForm.nickname"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="名称"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="tel" label="电话:">
                            <el-input
                                    v-model="editUserForm.tel"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="电话"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="chinese" label="语文:">
                            <el-input
                                    v-model="editUserForm.chinese"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="语文:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="math" label="数学:">
                            <el-input
                                    v-model="editUserForm.math"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="数学:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="math" label="英语:">
                            <el-input
                                    v-model="editUserForm.english"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="英语:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="subject1" label="选科1名称:">
                            <el-input
                                    v-model="editUserForm.subject1"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="选科1:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="subject1" label="选科1分数:">
                            <el-input
                                    v-model="editUserForm.subject1score"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="选科1分数:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="subject1" label="选科2名称:">
                            <el-input
                                    v-model="editUserForm.subject2"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="选科2:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="subject1" label="选科2分数:">
                            <el-input
                                    v-model="editUserForm.subject2score"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="选科2分数:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="subject1" label="选科3名称:">
                            <el-input
                                    v-model="editUserForm.subject3"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="选科3:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="subject1" label="选科3分数:">
                            <el-input
                                    v-model="editUserForm.subject3score"
                                    type="text"
                                    auto-complete="off"
                                    placeholder="选科3分数:"
                            >
                            </el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button size="small" @click="userdilog = false">取 消</el-button>
                            <el-button size="small" type="primary" @click="editUser()">保 存</el-button>
                        </el-form-item>
                    </el-form>
                </el-dialog>

            </el-header>
            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>

    </div>

</template>

<script>
    export default {
        name: "main",
        data(){
            return{
                uploadhead:"/public/",
                editUserForm:{
                    id:'',
                    username: "",
                    pass: "",
                    head:'',
                    tel:'',
                    nickname:'',
                    subject1:'',
                    subject1score:'',
                    subject2:'',
                    subject2score:'',
                    subject3:'',
                    subject3score:'',
                    chinese:'',
                    math:'',
                    english:'',

                },
                userdilog:false,
                activePath: "",
                avatar: require("@/assets/images/avator.jpg"),
                editPasswordDialog:false,
                user:[],
                url:'http://localhost:8088',
                userid:'',
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
                editUserRule:{
                    pic: [
                        { required: true, trigger: "blur", message: "请添加您的头像" }
                    ],

                    password: [
                        { required: true, trigger: "blur", message: "密码格式8到20位大小写字母、数字、特殊字符" },
                        { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" },
                        { min: 6, max: 12, message: '密码长度在 8 到 20 个字符', trigger: 'blur' }
                    ],
                    username: [
                        { required: true, message: "用户名称不能为空", trigger: "blur" },
                        { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
                    ],
                    nickname: [
                        { required: true, message: "用户昵称不能为空", trigger: "blur" }
                    ],
                    tel: [
                        {
                            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
                            message: "请输入正确的手机号码",
                            trigger: "blur"
                        }
                    ],
                    subject1: [
                        { required: true, message: "科目1名称不能为空", trigger: "blur" }
                    ],
                    subject2: [
                        { required: true, message: "科目2名称不能为空", trigger: "blur" }
                    ],
                    subject3: [
                        { required: true, message: "科目3名称不能为空", trigger: "blur" }
                    ],
                    subject1score: [
                        { required: true, message: "科目1分数不能为空", trigger: "blur" }
                    ],
                    subject2score: [
                        { required: true, message: "科目2分数不能为空", trigger: "blur" }
                    ],
                    subject3score: [
                        { required: true, message: "科目3分数不能为空", trigger: "blur" }
                    ],
                }
            }
        },

        created(){
            this.user = JSON.parse(sessionStorage.user);
            this.editUserForm = JSON.parse(sessionStorage.user);
            this.activePath = sessionStorage.getItem("activePath")
                ? sessionStorage.getItem("activePath")
                : "/index";
        },
        methods:{
            editUser(){

                        this.$axios.get('/user/update?tel='+this.editUserForm.tel+'&nickname='+this.editUserForm.nickname+'&head='+this.editUserForm.head+'&id='+this.editUserForm.id
                            +'&subject1='+this.editUserForm.subject1 +'&subject1score='+this.editUserForm.subject1score+'&subject2='+this.editUserForm.subject2+'&subject2score='+this.editUserForm.subject2score
                            +'&subject3='+this.editUserForm.subject3+'&subject3score='+this.editUserForm.subject3score+'&chinese='+this.editUserForm.chinese
                            +'&math='+this.editUserForm.math+'&english='+this.editUserForm.english)
                            .then(res => {
                                console.log(res);
                                console.log(res.data.code ==='200')
                                if (res.data.code ==='200'){
                                    this.$message.success("个人修改成功，请重新登录！");
                                    this.user = res.data.data;
                                    sessionStorage.clear();
                                    sessionStorage.setItem("user",JSON.stringify(res.data.data));
                                    this.userdilog = false
                                }
                                else {
                                    this.$message.error(res.data.msg);
                                }
                            })
                            .catch(() => {
                                this.$message.error("服务器连接失败，请稍后重试......");
                            });

            },
            handleAvatarSuccess(res, file) {
                console.log('file',file)
                //获取到file下面的响应数据
                this.editUserForm.head= res;
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
                    this.editUserForm.head= file.name
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                    return false;
                }
            },
            saveActiveNav(activePath) {
                sessionStorage.setItem("activePath", activePath);
                this.activePath = activePath;
            },
            // 取消关闭密码
            closeEditPassword() {
                this.editPasswordDialog = false;
                this.$refs.editPasswordForm.resetFields();
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

            loginbtn(){
                this.$router.push("/login");
            },
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
        }
    }
</script>

<style scoped>
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
        width: 100px;
        height: 100px;
        line-height: 100px;
        text-align: center;
        border: 1px dashed  #8c939d;
        border-radius: 20px;
    }
    .avatar {
        width: 100px;
        height: 100px;
        display: block;
    }
    .el-header {
        position: relative;
        width: 100%;
        height: 120px;
    }
    .el-main {
        position: absolute;
        left: 0;
        right: 0;
        top: 120px;
        bottom: 0;
        overflow-y: scroll;

    }

    .title{
        background-color:#409EFF ;
        height: 50px;
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .title h1{
        color: #fff;
    }
    .el-col {
        border-radius: 4px;
    }
    .bg-purple-dark {
        background: #99a9bf;
    }
    .bg-purple {
        background: #d3dce6;
    }
    .bg-purple-light {
        background: #e5e9f2;
    }
    .grid-content {
        border-radius: 4px;
        height: 100%;
    }
</style>
