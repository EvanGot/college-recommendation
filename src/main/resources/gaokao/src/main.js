import Vue from 'vue'
import App from './App.vue'
// 引入Element
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(Element)
import axios from 'axios'

Vue.prototype.$axios = axios

Vue.config.productionTip = false
// 引入路由n
import router from "./router"



import echarts from 'echarts'

Vue.prototype.$echarts = echarts


import VueQuillEditor from 'vue-quill-editor'

// 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.use(VueQuillEditor, /* { 默认全局 } */)


new Vue({
  render: h => h(App),
  router:router
}).$mount('#app')
