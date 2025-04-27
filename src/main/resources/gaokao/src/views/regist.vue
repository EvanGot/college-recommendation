<template>
    <div class="login">
        <el-card class="login_card">
            <p class="login_title">注 册</p>
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
                <el-form-item label="头像:">
                    <el-upload class="avatar-uploader"
                               action="/upload/uploadImage"
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <el-image v-if="loginForm.head !== null && loginForm.head !== ''" :src="url + uploadhead + loginForm.head" class="avatar"></el-image>
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input
                            v-model="loginForm.username"
                            type="text"
                            auto-complete="off"
                            placeholder="账号"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item prop="nickname">
                    <el-input
                            v-model="loginForm.nickname"
                            type="text"
                            auto-complete="off"
                            placeholder="名称"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item prop="tel">
                    <el-input
                            v-model="loginForm.tel"
                            type="text"
                            auto-complete="off"
                            placeholder="电话"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                            show-password
                            v-model="loginForm.password"
                            type="password"
                            auto-complete="off"
                            placeholder="请输入密码"
                            @keyup.enter.native="handleLogin"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item style="width:100%;">
                    <el-button
                            :loading="loading"
                            size="medium"
                            type="primary"
                            style="width:100%; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); transition: all 0.3s ease;"
                            @click.native.prevent="handleLogin"
                            @mouseenter="buttonHover = true"
                            @mouseleave="buttonHover = false"
                    >
                        <span v-if="!loading">注 册</span>
                        <span v-else>注 册 中...</span>
                    </el-button>
                </el-form-item>
                <el-link type="info" @click="gotoback">返回</el-link>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import Vue from 'vue'
export default Vue.extend({
    name: "LoginPage",
    data() {
        return {
            url: 'http://localhost:8088',
            uploadhead: "/public/",
            uploadimg: '/upload/image',
            logo_url: require("@/assets/logo/logo.png"),
            loginRules: {
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
                ]
            },
            loginForm: {
                username: "",
                password: "",
                head: '',
                tel: '',
                nickname: ''
            },
            loading: false,
            buttonHover: false,
            // 验证码开关
            captchaEnabled: true,
            // 注册开关
            register: true,
            redirect: undefined,
            yzm: null
        }
    },
    methods: {
        handleAvatarSuccess(res, file) {
            console.log('file', file)
            // 获取到file下面的响应数据
            this.loginForm.head = res;
            console.log('图片路径：', this.form)
        },
        // 判断用户上传的图片格式和大小
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
                return false;
            } else {
                this.loginForm.head = file.name
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
                return false;
            }
        },
        gotoback() {
            this.$router.go(-1);
        },
        handleLogin() {
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    this.$axios.get('/user/add?username=' + this.loginForm.username + '&pass=' + this.loginForm.password + '&tel=' + this.loginForm.tel + '&nickname=' + this.loginForm.nickname + '&head=' + this.loginForm.head)
                       .then(res => {
                            console.log(res);
                            console.log(res.data.code === '200')
                            if (res.data.code === '200') {
                                this.$router.push("/login");
                                this.$message.success("注册成功，", this.loginForm.nickname);
                            } else {
                                this.$message.error(res.data.msg);
                            }
                        })
                       .catch(() => {
                            this.$message.error("服务器连接失败，请稍后重试......");
                            this.loading = false;
                        });
                } else {
                    return false;
                }
            });
        }
    }
})
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
    border: 1px dashed #8c939d;
    border-radius: 20px;
}
.avatar {
    width: 100px;
    height: 100px;
    display: block;
}
.el-button {
    width: 100%;
}
.login {
    width: 100%;
    height: 100%;
    background-image: url("../assets/images/bj.jpg");
    position: absolute;
    left: 0;
    top: 0;
    background-size: 100% 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
.login_card {
    width: 300px;
    border-radius: 10px;
    text-align: center;
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
    padding: 20px;
}
.login_title {
    font-size: 28px;
    font-weight: bold;
    color: #2661ef;
    margin-top: 10px;
}
.login-form .el-form-item {
    margin-bottom: 20px;
}
.login-form .el-input {
    border-radius: 5px;
}
.el-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}
.el-link {
    display: block;
    margin-top: 20px;
}
</style>