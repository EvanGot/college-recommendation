<template>
    <div class="app-container">
        <!-- 顶部介绍区域 -->
        <div class="intro-section">
            <h1 class="intro-title">高考重要数据可视化</h1>
            <p class="intro-desc">
                本页面展示了全国高考报名人数的趋势以及热门专业的分布情况。这些数据能帮助您更好地了解高考形势，为志愿填报提供参考。数据来源可靠，均来自官方统计。
            </p>
        </div>
        <!-- 图表区域 -->
        <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="12" :xs="12">
                <div class="echart-container">
                    <div class="echart" id="mychart" :style="myChartStyle"></div>
                </div>
            </el-col>
            <el-col :span="12" :xs="12">
                <div class="echart-container">
                    <div class="echart" id="mychartgx" :style="myChartStyle"></div>
                </div>
            </el-col>
        </el-row>
        <!-- 底部说明区域 -->
        <div class="bottom-section">
            <p class="bottom-desc">
                以上数据仅供参考，实际情况可能会有所不同。在做出决策时，请综合考虑多方面因素。
            </p>
        </div>
    </div>


</template>

<script>
import * as echarts from "echarts";

export default {
    data() {
        return {
            myChartStyle: { width: "100%", height: "400px" }, // 图表样式
        };
    },
    created() {

    },
    mounted() {
        this.gxinitEcharts2();
        this.gxinitEcharts();
    },
    methods: {
        gxinitEcharts() {
            const option = {
                title: {
                    text: '全国高考报名人数',
                    left: 'center',
                    textStyle: {
                        color: "#333",
                        fontSize: 18,
                        fontWeight: 'bold',
                    },
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function (params) {
                        var unit = '万';
                        var res = params[0].name + '<br/>';
                        for (var i = 0, l = params.length; i < l; i++) {
                            res += params[i].name + '年全国高考报名人数 : ' + params[i].value + unit + '<br/>';
                        }
                        return res;
                    },
                },
                xAxis: {
                    type: 'category',
                    data: ['2018', '2019', '2020', '2021', '2022', '2023', '2024'],
                    axisLabel: {
                        color: '#666',
                        fontSize: 12,
                    },
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '{value} 万',
                        color: '#666',
                        fontSize: 12,
                    },
                },
                series: [
                    {
                        data: [975, 1031, 1071, 1078, 1193, 1291, 1342],
                        type: 'bar',
                        itemStyle: {
                            color: '#2661ef',
                        },
                        emphasis: {
                            itemStyle: {
                                color: '#42a5f5',
                            },
                        },
                        animationDelay: function (idx) {
                            return idx * 100;
                        },
                    },
                ],
            };
            const mychart2 = echarts.init(document.getElementById("mychartgx"));
            mychart2.setOption(option);
            // 随着屏幕大小调节图表
            window.addEventListener("resize", () => {
                mychart2.resize();
            });
        },
        gxinitEcharts2() {
            const option = {
                title: {
                    text: '热门专业',
                    left: 'center',
                    textStyle: {
                        color: "#333",
                        fontSize: 18,
                        fontWeight: 'bold',
                    },
                },
                tooltip: {
                    trigger: 'item',
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    textStyle: {
                        color: '#666',
                        fontSize: 12,
                    },
                },
                series: [
                    {
                        name: '使用情况',
                        type: 'pie',
                        radius: '50%',
                        data: [
                            { value: 1048, name: '通信工程' },
                            { value: 735, name: '物理学' },
                            { value: 580, name: '机械工程' },
                            { value: 484, name: '自动化' },
                            { value: 300, name: '法学' },
                            { value: 189, name: '计算机类' },
                            { value: 99, name: '数学类' },
                        ],
                        label: {
                            color: '#fff',
                            fontSize: 12,
                        },
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)',
                            },
                        },
                        animationDelay: function (idx) {
                            return idx * 100;
                        },
                    },
                ],
            };
            const mychart2 = echarts.init(document.getElementById("mychart"));
            mychart2.setOption(option);
            // 随着屏幕大小调节图表
            window.addEventListener("resize", () => {
                mychart2.resize();
            });
        },
    },
};
</script>

<style scoped>
.app-container {
    position: relative;
    min-height: 100vh;
    padding: 20px;
    background: linear-gradient(135deg, #e0f7fa, #b2ebf2, #80deea);
}

.intro-section {
    text-align: center;
    margin-bottom: 20px;
}

.intro-title {
    font-size: 28px;
    color: #2661ef;
    margin-bottom: 10px;
}

.intro-desc {
    font-size: 16px;
    color: #666;
}

.echart-container {
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin-bottom: 20px;
}

.echart {
    width: 100%;
    height: 100%;
}

.bottom-section {
    text-align: center;
    margin-top: 20px;
}

.bottom-desc {
    font-size: 14px;
    color: #999;
}
</style>