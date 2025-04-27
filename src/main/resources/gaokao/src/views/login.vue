<template>
    <div class="login">
        <el-card class="login_card animated fadeIn">
            <el-image class="login_image" :src="logo_url"></el-image>
            <p class="login_title">登 录</p>
            <p class="login_desc">欢迎高考志愿填报推荐系统</p>
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
                <el-form-item prop="username">
                    <el-input
                            v-model="loginForm.username"
                            type="text"
                            auto-complete="off"
                            placeholder="账号"
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
                <el-form-item prop="validCode">
                    <div style="display: flex">
                        <el-input v-model="loginForm.validCode" style="flex: 1" placeholder="请输入验证码" size="medium"></el-input>
                        <ValidCode @input="createValidCode" ref="child"/>
                    </div>
                </el-form-item>
                <el-form-item prop="type">
                    <el-radio-group v-model="loginForm.type">
                        <el-radio label="管理员">管理员</el-radio>
                        <el-radio label="用户">用户</el-radio>
                    </el-radio-group>
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
                        <span v-if="!loading">登 录</span>
                        <span v-else>登 录 中...</span>
                    </el-button>
                </el-form-item>
                <el-link type="primary" @click="gotoregist">注册账号</el-link>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import ValidCode from "@/components/ValidCode";
import Vue from 'vue'
export default Vue.extend({
    name: "LoginPage",
    components: {
        ValidCode
    },
    data() {
        return {
            validCode: '',
            loading: false,
            buttonHover: false,
            logo_url: require("@/assets/logo/logo.png"),
            loginRules: {
                username: [
                    { required: true, trigger: "blur", message: "请输入您的账号" }
                ],
                password: [
                    { required: true, trigger: "blur", message: "请输入您的密码" }
                ],
                validCode: [
                    { required: true, trigger: "blur", message: "请输入您的验证码" }
                ]
            },
            loginForm: {
                username: "",
                password: "",
                type: '管理员',
                validCode: ""
            },
            // 验证码开关
            captchaEnabled: true,
            // 注册开关
            register: true,
            redirect: undefined,
            yzm: null
        }
    },
    created() {

    },
    methods: {
        createValidCode(data) {
            this.validCode = data
        },
        gotoregist() {
            this.$router.push('/regist');
        },
        handleLogin() {
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    if (this.loginForm.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
                        this.$message.error("验证码错误")
                        this.$refs.child.sing();
                    } else {
                        this.loading = true;
                        this.$axios.get('/user/login?username=' + this.loginForm.username + '&pass=' + this.loginForm.password + '&type=' + this.loginForm.type)
                           .then(res => {
                                console.log(res);
                                console.log(res.data.code === '200')
                                if (res.data.code === '200') {
                                    sessionStorage.setItem("user", JSON.stringify(res.data.data));
                                    if (res.data.data.type === '管理员') {
                                        this.$router.push('/home');
                                    } else {
                                        this.$router.push('/main');
                                    }
                                } else {
                                    this.$message.error(res.data.msg);
                                    this.loading = false;
                                }
                            })
                           .catch(() => {
                                this.$message.error("服务器连接失败，请稍后重试......");
                                this.loading = false;
                            });
                    }
                } else {
                    return false;
                }
            });
        }
    }
})
</script>

<style scoped>
.el-button {
    width: 100%;
}
.login {
    width: 100%;
    height: 100%;
    background-image: url("../assets/images/bj5.jpg");
    position: absolute;
    left: 0;
    top: 0;
    background-size: 100% 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
.login_card {
    /* 扩大登录框架 */
    width: 450px;
    border-radius: 10px;
    text-align: center;
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
    padding: 40px;
    animation-duration: 0.5s;
}
.login_image {
    width: 100px;
    height: 100px;
    margin-top: 20px;
}
.login_title {
    font-size: 32px;
    font-weight: bold;
    color: #2661ef;
    margin-top: 10px;
}
.login_desc {
    letter-spacing: 2px;
    color: #666;
    margin-bottom: 30px;
}
.login-form .el-form-item {
    margin-bottom: 25px;
}
.login-form .el-input {
    border-radius: 5px;
    transition: border-color 0.3s ease;
}
.login-form .el-input:focus {
    border-color: #2661ef;
    box-shadow: 0 0 8px rgba(38, 97, 239, 0.3);
}
.login-form .el-radio-group {
    display: flex;
    justify-content: center;
}
.login-form .el-radio {
    margin: 0 15px;
}
.el-button:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}
.el-link {
    display: block;
    margin-top: 25px;
}
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
.animated {
    animation-fill-mode: both;
}
.fadeIn {
    animation-name: fadeIn;
}
</style>