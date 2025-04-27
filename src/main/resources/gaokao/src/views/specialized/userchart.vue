<template>
    <div class="app-container" style="margin-top:20px;display: flex;flex-direction: column;" >
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="info"
                        plain
                        icon="el-icon-back"
                        size="mini"
                        @click="gotoback"
                >返回</el-button>
            </el-col>
        </el-row>
            <el-row :gutter="20" style="margin-top: 10px;background-color: #fff">
                <div class="echart" id="mychartgx" :style="myChartStyle"></div>
            </el-row>
    </div>

</template>

<script>
    import * as echarts from "echarts";

    export default {
        data() {
            return {
                x:[],
                y:[],
                title:'',
                myChartStyle: { float: "left", width: "100%", height: "400px" }, //图表样式
            };
        },
        created() {
            this.getitem();

        },
        mounted() {
            this.gxinitEcharts()
        },
        methods: {
            gotoback(){
                this.$router.back();

            },
            getitem() {
                this.x = JSON.parse(this.$route.query.x)
                this.y = JSON.parse(this.$route.query.y)
                this.title = this.$route.query.title
                this.gxinitEcharts()
            },
            gxinitEcharts(){
                const option = {
                    title: {
                        text: this.title,
                        left: 'center',
                        textStyle: {
                            color: "#000",
                        },
                    },
                    tooltip: {
                        trigger: 'axis',

                    },
                    xAxis: {
                        type: 'category',
                        data: this.x
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} 分'
                        }
                    },
                    series: [
                        {
                            data:this.y,
                            type: 'line'
                        }
                    ]
                };
                const mychart2 = echarts.init(document.getElementById("mychartgx"));
                mychart2.setOption(option);
                //随着屏幕大小调节图表
                window.addEventListener("resize", () => {
                    mychart2.resize();
                });
            },

        },

    };
</script>

<style scoped>


</style>
