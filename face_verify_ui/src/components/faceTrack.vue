<template>
  <div class="container">
    <div class="face-container">
      <div class="video-box">
        <video id="video" height="720" preload autoplay loop muted></video>
        <canvas id="canvas" width="480" height="720"></canvas>
      </div>
      <canvas id="screenshotCanvas" width="480" height="720"></canvas>
    </div>
    <div class="user-container userinfo">
      <el-form label-position="left">
        姓名：
        <el-form-item class="uname">
          <el-input
            v-model="user.uname"
            placeholder="输入姓名注册人脸信息"
          ></el-input>
        </el-form-item>
        <el-form-item class="btn">
          <el-button type="primary" @click="register">注册</el-button>
          <span v-if="verifyFlag">人脸识别已开启  </span>
          <span v-if="!verifyFlag">人脸识别已关闭  </span>
          <el-switch
            v-model="verifyFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
          >
          </el-switch>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
require('../assets/tracking.js');
require('../assets/data/face-min.js');
import userApi from '@/api/user'
export default {
  data() {
    return {
      // 视频 element
      video: null,
      // canvas element
      screenshotCanvas: null,
      // 用户信息
      user: {
        uid: null,
        uname: null,
        ufaceId: null,
        uimage: null
      },
      // 是否开启人脸验证
      verifyFlag: false,
      // 最后验证事件 用来控制重复请求
      lastTime: null
    }
  },
  created() {
    this.lastTime = Date.now()
  },
  mounted() {
    this.init();
  },
  methods: {
    // 初始化设置
    init() {
      this.video = document.getElementById('video');
      this.screenshotCanvas = document.getElementById('screenshotCanvas');
      let canvas = document.getElementById('canvas');
      let context = canvas.getContext('2d');

      // 固定写法
      let tracker = new window.tracking.ObjectTracker("face");
      tracker.setInitialScale(4);
      tracker.setStepSize(2);
      tracker.setEdgesDensity(0.1);
      window.tracking.track('#video', tracker, {
        camera: true
      });

      let _this = this;
      tracker.on('track', function (event) {
        setTimeout(() => { }, 200)
        // 检测出人脸 绘画人脸位置
        context.clearRect(0, 0, canvas.width, canvas.height);
        event.data.forEach(function (rect) {
          context.strokeStyle = '#ffc600';
          context.lineWidth = 5
          context.strokeRect(rect.x, rect.y, rect.width, rect.height);
          // 人脸比对
          // 两秒防重复
          if (_this.verifyFlag) {
            if (Date.now() - _this.lastTime > 2000) {
              _this.verify()
              _this.lastTime = Date.now()
            }
          }
        });
      });
    },
    // 获取base64图片数据
    screenshotAndUpload() {
      // 绘制当前帧图片转换为base64格式
      let canvas = this.screenshotCanvas;
      let video = this.video;
      let ctx = canvas.getContext('2d');
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      let base64Img = canvas.toDataURL('image/jpeg');
      // 使用 base64Img 请求接口即可
      console.log('发送请求')
      this.user.uimage = base64Img
    },
    // 人脸注册
    register() {
      this.screenshotAndUpload();
      userApi.register(this.user).then((result) => {
        console.log(result.data);
        console.log(result.data.msg);
        if (result.data.code === 200) {
          this.$message({
            message: result.data.msg,
            type: 'success'
          });
        } else {
          this.$message.error(result.data.msg);
        }
      })
    },
    // 人脸比对
    verify() {
      this.screenshotAndUpload();
      userApi.verify(this.user).then(res => {
        if (res.data.code === 200) {
          this.$message({
            message: res.data.msg,
            type: 'success'
          });
          console.log("用户信息：", res.data.data);
        }
      })
    }
  }
}
</script>

<style  scoped>
.container {
  min-width: 1200px;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: aliceblue;
  background-color: rgb(48, 56, 45);
}

/* 绘图canvas 不需显示隐藏即可 */
#screenshotCanvas {
  display: none;
}

.face-container {
  width: 50%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  
}

.video-box {
  margin: 0 auto;
  position: relative;
  height: 720px;
}

video,
canvas {
  position: absolute;
  top: 0;
  left: 0;
}
video{
  box-shadow: 0px 0px 8px seashell;
}
.user-container {
  display: flex;
  width: 50%;
  justify-content: center;
  align-items: center;
  text-align: left;
  flex-direction: column;
  font-size: 18px;
}
.btn {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.el-button {
  margin: 20px 40px;
}
.el-switch {
  margin: 20px 40px 20px 10px;
  font-size: 18px;
}
.uname{
  margin: 20px 0;
}
</style>
