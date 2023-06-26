<template>
    <div class="main-container">
        <div class="main-content">
            <slot></slot>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PageBody"
    }
</script>

<style scoped>
    .main-container{
        margin-top: 67px;
        display: flex;
        justify-content: center;
    }
    .main-content{
        width: 1100px;
        background-color: #ebf2ff;
        min-height: 90vh;
    }
</style>