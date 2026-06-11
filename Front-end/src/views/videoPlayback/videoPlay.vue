<template>
  <div class="item">
    <div class="player">
      <video-player
        class="vjs-custom-skin"
        ref="videoPlayer"
        :options="playerOptions"
        :playsinline="true"
        @play="onPlayerPlay($event)"
        @pause="onPlayerPause($event)"
        @ended="onPlayerEnded($event)"
        @loadeddata="onPlayerLoadeddata($event)"
        @waiting="onPlayerWaiting($event)"
        @playing="onPlayerPlaying($event)"
        @timeupdate="onPlayerTimeupdate($event)"
        @canplay="onPlayerCanplay($event)"
        @canplaythrough="onPlayerCanplaythrough($event)"
        @ready="playerReadied"
        @statechanged="playerStateChanged($event)"
      >
      </video-player>
    </div>
  </div>
</template>

<script>
// custom skin css

import { axios } from '../../utils/request'
import api from '../../api/index'
export default {
  data() {
    return {
      a: '',
      myValue: { formDesignerId: '', onlineTableId: '', onlineDataId: '' },
      formJson: {},
      // videojs options
      playerOptions: {
        height: '360',
        autoplay: true,
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        muted: true,
        language: 'en',
        playbackRates: [0.7, 1.0, 1.5, 2.0],
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true // 全屏按钮
        },
        sources: [{
          type: "video/mp4",
          // mp4
          //src: "http://vjs.zencdn.net/v/oceans.mp4",
          //src: "http://vjs.zencdn.net",
          // webm
          src: "https://cdn.theguardian.tv/webM/2015/07/20/150716YesMen_synd_768k_vp8.webm"

        }],
        poster: "https://surmon-china.github.io/vue-quill-editor/static/images/surmon-1.jpg",
      }
    }
  },
  mounted() {
    // console.log('this is current player instance object', this.player)
    setTimeout(() => {
      console.log('dynamic change options', this.player)
      this.playerOptions['sources'][0]['src'] = res.result.video;
      this.player.muted(false)
    }, 5000)
  },
  created() {
    this.getLink()
  },
  computed: {
    player() {
      return this.$refs.videoPlayer.player
    }
  },
  methods: {

    openForm() {
      this.myValue.formDesignerId = this.$store.state.myValue.formDesignerId
      this.myValue.onlineTableId = this.$store.state.myValue.onlineTableId
      this.myValue.onlineDataId = this.$store.state.myValue.onlineDataId
      console.log('this.myValue', this.myValue)
      this.formJson = {}
      this.getForm()
    },
    getLink() {
      this.openForm();
    },
    //得到表单
    getForm() {
      console.log('this.myValue', this.myValue)
      var id = this.myValue.formDesignerId
      console.log("id", this.myValue.formDesignerId)
      axios({
        url: `/admin/desform/${id}/getConent`,
        method: 'get',
        baseURL: api.server_url + api.global_fmaking_baseURL,
      })
        .then((res) => {
          const onlineTableId = this.myValue.onlineTableId
          const onlineDataId = this.myValue.onlineDataId
          const formJson = JSON.parse(res.result)
          if (onlineTableId != '' && onlineDataId != '') {
            this.getOnlineJson(onlineTableId, onlineDataId, formJson)
          } else {
            this.formJson = formJson
            this.attributeFormFlag = true
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //得到online的data
    getOnlineJson(onlineTableId, onlineDataId, formJson) {
      axios({
        url: `/cgform/api/form/${onlineTableId}/${onlineDataId}`,
        method: 'get',
        baseURL: api.server_url + api.global_online_baseURL,
      })
        .then((res) => {
          this.playerOptions.sources.src = res.result.video
          //  console.log("223",this.options['sources'][0]['src'])
          this.playerOptions['sources'][0]['src'] = res.result.video;
          this.$forceUpdate()

        })
        .catch((err) => {
          console.log(err)
        })
    },
    // listen event
    onPlayerPlay(player) {
      // console.log('player play!', player)
    },
    onPlayerPause(player) {
      // console.log('player pause!', player)
    },
    onPlayerEnded(player) {
      // console.log('player ended!', player)
    },
    onPlayerLoadeddata(player) {
      // console.log('player Loadeddata!', player)
    },
    onPlayerWaiting(player) {
      // console.log('player Waiting!', player)
    },
    onPlayerPlaying(player) {
      // console.log('player Playing!', player)
    },
    onPlayerTimeupdate(player) {
      // console.log('player Timeupdate!', player.currentTime())
    },
    onPlayerCanplay(player) {
      // console.log('player Canplay!', player)
    },
    onPlayerCanplaythrough(player) {
      // console.log('player Canplaythrough!', player)
    },
    // or listen state event
    playerStateChanged(playerCurrentState) {
      // console.log('player current update state', playerCurrentState)
    },
    // player is ready
    playerReadied(player) {
      // seek to 10s
      console.log('example player 1 readied', player)
      //player.currentTime(10)
      // console.log('example 01: the player is readied', player)
    }
  }
}
</script>