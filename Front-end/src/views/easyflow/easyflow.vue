<template>
  <div>
    <div>
      <a-button-group>
        <a-button type="primary" icon="plus" @click="openEasyflow">新增</a-button>
      </a-button-group>
    </div>
    <panel ref="panel"></panel>
  </div>
</template>
<script>
import panel from './ef/panel.vue'
export default {
  name: 'easyflow',
  components: { panel },
  data() {
    return {}
  },
  created() {},
  methods: {
    openEasyflow(){
      this.$refs.panel.openModel()
    }
  },
  mounted() {},
}
</script>
