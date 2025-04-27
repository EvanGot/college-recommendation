<template>
  <div id="app">
    <!-- 路由视图，用于渲染各个页面 -->
    <router-view />
    <!-- 按钮容器 -->
    <div class="button-container">
      <!-- 豆包小logo按钮 -->
      <div class="round-button" @click="goToDoubaoPage">
        <img src="@/assets/doubao-logo.png" alt="豆包" />
      </div>
      <!-- DeepSeek 按钮 -->
      <div class="round-button" @click="goToDeepSeekPage">
        <img src="@/assets/deepseek-logo.png" alt="DeepSeek" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {};
  },
  methods: {
    // 跳转到豆包界面的方法
    goToDoubaoPage() {
      // 这里使用 window.open 打开一个新窗口，跳转到豆包界面
      // 你需要根据实际情况修改豆包界面的 URL
      window.open('https://www.doubao.com', '_blank');
    },
    // 跳转到 DeepSeek 界面的方法
    goToDeepSeekPage() {
      // 这里使用 window.open 打开一个新窗口，跳转到 DeepSeek 界面
      // 你需要根据实际情况修改 DeepSeek 界面的 URL
      window.open('https://www.deepseek.com', '_blank');
    }
  }
};
</script>

<style>
body {
  background: #fff;
  margin: 0;
  padding: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100%;
  margin: 0;
  padding: 0;
}

/* 按钮容器样式 */
.button-container {
  position: fixed;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 20px;
  z-index: 999;
}

/* 圆形按钮样式 */
.round-button {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

.round-button:hover {
  transform: scale(1.1);
}

.round-button img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>