<template>
  <div class="mxgraph_container" v-show="visible">
    <table id="splash" width="100%" height="100%" style="background:white;position:absolute;top:0px;left:0px;z-index:4;">
      <tr>
        <td align="center" valign="middle">
          <img src="/static/examples/editors/images/loading.gif">
        </td>
      </tr>
    </table>
 
    <div id="graph" class="base">
      <a-button type="primary" @click="goBack">
        <a-icon type="left" />返回
      </a-button>
      <!-- Graph Here -->
    </div>
    <div id="status" class="base" align="right" style="white-space:nowrap;">
      <!-- Status Here -->
    </div>
  </div>
</template>

<script>
import { getAction, postAction } from '@/api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'


export default {
  name: 'mxWorkflow',
  data () {
    return {
      visible:false,
      editor: null,
    }
  },
  mounted () {
 
  },
  beforeDestroy () {
    this.editor && this.editor.destroy && this.editor.destroy()
    this.editor = null
    const parent = document.querySelector('body')
    const children = document.querySelectorAll('.mxWindow')
    try {
      children.forEach((el) => {
        parent.removeChild(el)
      })
    } catch (e) {
      // console.log(e)
    } 
  },
  methods:{
    show(record){
      this.visible = true
      const con = document.querySelector('.mxgraph_container')
      con.style.display = 'block'
      const token = Vue.ls.get(ACCESS_TOKEN)

      this.editor = createEditor('/static/examples/editors/config/workfloweditor.xml',false,token,
      Vue.ls.get(TENANT_ID),record.id,this.$store.state.user.info)
    },
    goBack(){
      this.visible = false
      this.editor && this.editor.destroy && this.editor.destroy()
      this.editor = null
      const parent = document.querySelector('body')
      const children = document.querySelectorAll('.mxWindow')
      try {
        children.forEach((el) => {
          parent.removeChild(el)
        })
      } catch (e) {
        // console.log(e)
      } 
      this.$emit('back')
    },
    showImage(){
      // this.editor.graph.getModel().endUpdate();
    }
  }
}
</script>
<style  scoped>
.base {
  position: absolute;
  overflow: hidden;
  font-family: Arial;
  font-size: 8pt;
}
#graph {
  height: 100%;
  width:100%;
  border-style: solid;
  border-color: #f2f2f2;
  border-width: 1px;
  background: url('../../../public/static/examples/editors/images/grid.gif') #fff;
}
.mxgraph_container {
  width: 100%;
  min-height: 800px;
  position: relative;
  /* margin-left: -5px; */
  /* margin-right: -5px; */
  background: rgb(255, 255, 255);
}
</style>
