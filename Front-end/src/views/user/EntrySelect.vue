<template>
    <div id="app" class="main-container">
        <header class="header">
            <!-- <h1>保证金智慧管理服务平台</h1> -->
        </header>
        <div class="card-container">
            <div class="card1" @click="goToLogin('mohrss')">
                <div class="icon">
                    <img src="@/assets/mohrss.png" alt="icon" class="icon-img" />
                </div>
                <div class="text">人社入口</div>
            </div>
            <div class="card2" @click="goToLogin('enterprise')">
                <div class="icon">
                    <img src="@/assets/enterprise.png" alt="icon" class="icon-img" />
                </div>
                <div class="text">企业入口</div>
            </div>
            <div class="card3" @click="goToLogin('brokerage')">
                <div class="icon">
                    <img src="@/assets/brokerage.png" alt="icon" class="icon-img" />
                </div>
                <div class="text">经纪公司入口</div>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    methods: {
        goToLogin(department) {
            console.log(department);
            this.$router.push({ path: '/user/login', query: { department } });
        }
    }
}
</script>

<style scoped>
.header h1 {
    color: #333;
    padding: 20px;
}

.main-content {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 70vh;
}

.card-container {
    display: flex;
    justify-content: space-around;
    /* 在卡片之间均匀分布空隙 */
    align-items: center;
    width: 100%;
    margin-top: 100px;
    margin-left: auto;
    margin-right: auto;
    background-color: transparent;
    gap: 20px;
    /* 减少卡片之间的间距 */
}

@media (min-width: 1200px) {
    .card-container {
        width: 90%;
    }
}

/* 设置每个卡片的样式 */
.card1,
.card2,
.card3 {
    flex: 1;
    min-width: 200px;
    max-width: 280px;
    height: 400px;
    border-radius: 15px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    cursor: pointer;
    margin-bottom: 90px;
}

.card1 {
    background: linear-gradient(135deg, #ff9a9e, #fecfef);
}

.card2 {
    background: linear-gradient(135deg, #a8e6cf, #dcedc1);
}

.card3 {
    background: linear-gradient(135deg, #d6a2e8, #e5b0e4);
}


.icon {
    width: 100px;
    height: 100px;
    background-color: white;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
}

.icon-img {
    width: 40px;
    height: 40px;
}

.text {
    margin-top: 20px;
    font-size: 2em;
    color: white;
}

.footer {
    color: #666;
    font-size: 14px;
    position: absolute;
    bottom: 20px;
    width: 100%;
}
</style>