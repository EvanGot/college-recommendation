import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
// 公共路由
const constantRoutes = new Router({
    mode:'history',
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            name: "Login",
            component: () => import('../views/login'),//指定界面，使用动态导入的方式引入 views/login 组件，实现路由懒加载，提高应用的加载性能。
            meta: {
                title: '登录'
            },
        },
        {
            path: '/regist',
            name: "Regist",
            component: () => import('../views/regist'),
            meta: {
                title: '注册'
            },
        },
        {
            path: '/home',
            name: "Home",
            component: () => import('../views/home'),
            meta: {
                title: '首页'
            },
            redirect: '/sy',//重定位，直接点击测边框就不用跳转到之前那个界面了
            children: [{
                path: '/sy',
                meta: {
                    title: '首页'
                },
                component: () => import('../views/welcome'),

            },{
                path: '/user/list',
                meta: {
                    title: '用户管理'
                },
                component: () => import('../views/user/index'),
            },

                {
                    path: '/school/list',
                    meta: {
                        title: '院校管理'
                    },
                    component: () => import('../views/school/index'),
                },
                {
                    path: '/specialized/list',
                    meta: {
                        title: '专业管理'
                    },
                    component: () => import('../views/specialized/index'),
                },
                {
                    path: '/specialized/chart',
                    meta: {
                        title: '图表'
                    },
                    component: () => import('../views/specialized/chart'),
                },
                {
                    path: '/news/list',
                    meta: {
                        title: '资讯推送'
                    },
                    component: () => import('../views/news/index'),
                },
                {
                    path: '/complain/list',
                    meta: {
                        title: '反馈与帮助'
                    },
                    component: () => import('../views/complain/index'),
                },


            ],
        },
        {
            path: '/main',
            name: "Main",
            component: () => import('../views/main'),
            meta:{
                title:'主页'
            },
            redirect: '/index',
            children: [{
                path: '/index',
                meta: {
                    title: '首页'
                },
                component: () => import('../views/welcome'),

            },
                {
                    path: '/specialized/userchart',
                    meta: {
                        title: '图表'
                    },
                    component: () => import('../views/specialized/userchart'),
                },

                {
                    path: '/recom/index',
                    meta: {
                        title: '推荐'
                    },
                    component: () => import('../views/recom/index'),

                },
                {
                    path: '/school/see',
                    meta: {
                        title: '查看院校'
                    },
                    component: () => import('../views/school/see'),

                },
                {
                    path: '/specialized/see',
                    meta: {
                        title: '查看'
                    },
                    component: () => import('../views/specialized/see'),
                },
                {
                    path: '/news/see',
                    meta: {
                        title: '查看新闻'
                    },
                    component: () => import('../views/news/se'),
                },

                {
                    path: '/sc/see',
                    meta: {
                        title: '我的收藏'
                    },
                    component: () => import('../views/sc/index'),
                },
                {
                    path: '/db/see',
                    meta: {
                        title: '我的对比'
                    },
                    component: () => import('../views/db/index'),
                },
                {
                    path: '/complain/see',
                    meta: {
                        title: '反馈与帮助'
                    },
                    component: () => import('../views/complain/see'),
                },
            ]
        },






    ]
})
// 挂载路由导航守卫：to表示将要访问的路径，from表示从哪里来，next是下一个要做的操作  next：是一个函数，用于控制路由的跳转。
constantRoutes.beforeEach((to, from, next) => {
    // 修改页面 title
    if (to.meta.title) {
        document.title = '高考志愿填报推荐系统的设计与实现 - ' + to.meta.title
    }
    // 放行登录页面
    if (to.path === '/login') {
        return next()
    }
    return next()
})
export default constantRoutes;//将路由实例 constantRoutes 导出，以便在 main.js 中引入并使用
