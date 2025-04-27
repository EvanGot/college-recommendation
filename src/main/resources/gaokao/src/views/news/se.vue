<template>
    <div class="main">
        <el-card class="box-card">
            <div v-for="(item,index) in newsList" :key="index" class="text item" @click="xq(item)" >
                <div class="pic">
                    <el-image :src="`${url}/public/${item.pic}`" class="picimage"></el-image>
                </div>
                <div class="con">
                    <span class="title">{{item.title}}</span>
                    <span class="time">{{item.time}}</span>
                </div>
            </div>
        </el-card>
        <el-dialog title="查看" :visible.sync="open" width="50%" append-to-body>
            <video v-if="video!==''" width="50%" height="240" controls :src="`${url}/public/${video}`"></video>
            <div v-html="richContent"></div>
            <div>
                {{time}}
            </div>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: "se",
        data(){
            return{
                newsList:[],
                url:'http://localhost:8088',
                uploadhead:"/public/",

                open:false,
                richContent:'',
                time:'',
                video:'',


            }
        },
        created(){
            this.getList();

        },
        methods:{
            xq(e){
                this.time = e.time
                this.richContent = e.content.replace(/<img/g, '<img style=\'width:100%;height:auto;margin:5px\'')
                this.video = e.video
                this.open = true

            },
            getList() {
                this.$axios.get('/news/findAll')
                    .then(res => {
                        console.log('news',res);
                        this.newsList = res.data.data;
                    })
                    .catch(() => {
                        this.$message.error("服务器连接失败，请稍后重试......");
                    });

            },
        }
    }
</script>

<style scoped>
    .main{
        width: 100%;
        height: 100vh;
        display: flex;
        justify-content: center;

    }
    .box-card {
        width:80%;
    }
    .item{
        width: 100%;
        height: 140px;
        border-bottom: 1px solid #f4f5f6;
        padding: 10px;
        box-sizing: border-box;
        display: flex;
    }
    .pic{
        height: 120px;
        width: 120px;

    }
    .picimage{
        width: 100%;
        height: 100%;
        border-radius: 5px;
    }
    .con{
        width: calc(100% - 130px);
        margin-left: 10px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
    .title{
        font-size: 20px;
        color: #000;
    }
    .time{
        color: #666;
        font-size: 12px;
    }

</style>
