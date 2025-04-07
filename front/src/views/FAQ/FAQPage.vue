<template>
  <div>
    <h1>{{ $t('faq.title') }}</h1>
    <div class="faqs-container">
      <div
        v-for="(key, index) in questionKeys"
        :key="key"
        :class="['faq', { active: faqs[index].active }]"
      >
        <h3 class="faq-title">
          {{ $t(`faq.questions.${key}.question`) }}
        </h3>
        <div class="faq-text">
          {{ $t(`faq.questions.${key}.answer`) }}
        </div>
        <button class="faq-toggle" @click="toggleFAQ(index)">
          <i class="fas fa-chevron-down" v-if="!faqs[index].active"></i>
          <i class="fas fa-times" v-else></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      questionKeys: ['features', 'createTask', 'loginIssue', 'saveIssue', 'privacy', 'dataSharing'],
      faqs: Array(6).fill().map(() => ({ active: false }))
    };
  },
  methods: {
    toggleFAQ(index) {
      this.faqs[index].active = !this.faqs[index].active;
    },
  },
  mounted() {
    document.body.style.overflow = "visible";
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css?family=Muli&display=swap");

* {
  box-sizing: border-box;
}

body {
  background-color: #f0f0f0;
  font-family: "Muli", sans-serif;
}

h1 {
  margin: 50px 0 50px;
  text-align: center;
  font-size: 36px;
  color: #555;
}

.faqs-container {
  margin: 0 auto;
  max-width: 800px;
}

.faq {
  background-color: transparent;
  border: 1px solid #9fa4a8;
  border-radius: 10px;
  padding: 30px;
  position: relative;
  overflow: hidden;
  margin: 20px 0;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq.active {
  background: linear-gradient(135deg, #e6f3ff 0%, #ffffff 100%);
  border-color: #1890ff;
  box-shadow: 0 3px 10px rgba(24, 144, 255, 0.1);
}

.faq.active::after,
.faq.active::before {
  color: rgba(24, 144, 255, 0.1);
  content: "\f075";
  font-family: "Font Awesome 5 Free";
  font-size: 7rem;
  position: absolute;
  opacity: 0.2;
  top: 20px;
  left: 20px;
  z-index: 0;
}

.faq.active::before {
  color: rgba(24, 144, 255, 0.05);
  top: -10px;
  left: -30px;
  transform: rotateY(180deg);
}

.faq-title {
  margin: 0 35px 0 0;
  color: #2c3e50;
  font-size: 1.1rem;
  font-weight: 600;
  position: relative;
  z-index: 1;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq.active .faq-title {
  /* color: #1890ff; */
  transform: translateY(-2px);
}

.faq-text {
  max-height: 0;
  opacity: 0;
  margin: 0;
  padding: 0 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  color: #666;
  line-height: 1.6;
  font-size: 0.95rem;
  position: relative;
  z-index: 1;
  /* border-left: 4px solid #1890ff; */
  box-shadow: 0 2px 6px rgba(24, 144, 255, 0.06);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  pointer-events: none;
}

.faq.active .faq-text {
  max-height: 1000px;
  opacity: 1;
  margin: 20px 0 0;
  padding: 20px;
  pointer-events: auto;
}

.faq-toggle {
  background-color: transparent;
  border: 1px solid #e8e8e8;
  border-radius: 50%;
  /* color: #1890ff; */
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  padding: 0;
  position: absolute;
  top: 30px;
  right: 30px;
  height: 30px;
  width: 30px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 2;
}

.faq-toggle:hover {
  background-color: rgba(24, 144, 255, 0.05);
  transform: scale(1.05);
}

.faq.active .faq-toggle {
  /* background-color: #1890ff;
  border-color: #1890ff; */
  color: white;
  transform: rotate(180deg);
}

.faq-toggle i {
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq-toggle .fa-times {
  display: none;
}

.faq.active .faq-toggle .fa-times {
  display: block;
}

.faq-toggle .fa-chevron-down {
  color: #83888e;
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq-toggle .fa-times {
  color: white;
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq.active .faq-toggle {
  /* background-color: #1890ff;
  border-color: #1890ff; */
  color: white;
}

.faq.active .faq-toggle:hover {
  /* background-color: #40a9ff; */
  transform: scale(1.05);
}

/* SOCIAL PANEL CSS */
.social-panel-container {
  position: fixed;
  right: 0;
  bottom: 80px;
  transform: translateX(100%);
  transition: transform 0.4s ease-in-out;
}

.social-panel-container.visible {
  transform: translateX(-10px);
}

.social-panel {
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 16px 31px -17px rgba(0, 31, 97, 0.6);
  border: 5px solid #001f61;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-family: "Muli";
  position: relative;
  height: 169px;
  width: 370px;
  max-width: calc(100% - 10px);
}

.social-panel button.close-btn {
  border: 0;
  color: #97a5ce;
  cursor: pointer;
  font-size: 20px;
  position: absolute;
  top: 5px;
  right: 5px;
}

.social-panel button.close-btn:focus {
  outline: none;
}

.social-panel p {
  background-color: #001f61;
  border-radius: 0 0 10px 10px;
  color: #fff;
  font-size: 14px;
  line-height: 18px;
  padding: 2px 17px 6px;
  position: absolute;
  top: 0;
  left: 50%;
  margin: 0;
  transform: translateX(-50%);
  text-align: center;
  width: 235px;
}

.social-panel p i {
  margin: 0 5px;
}

.social-panel p a {
  color: #ff7500;
  text-decoration: none;
}

.social-panel h4 {
  margin: 20px 0;
  color: #97a5ce;
  font-family: "Muli";
  font-size: 14px;
  line-height: 18px;
  text-transform: uppercase;
}

.social-panel ul {
  display: flex;
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.social-panel ul li {
  margin: 0 10px;
}

.social-panel ul li a {
  border: 1px solid #dce1f2;
  border-radius: 50%;
  color: #001f61;
  font-size: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50px;
  width: 50px;
  text-decoration: none;
}

.social-panel ul li a:hover {
  border-color: #ff6a00;
  box-shadow: 0 9px 12px -9px #ff6a00;
}

.floating-btn {
  border-radius: 26.5px;
  background-color: #001f61;
  border: 1px solid #001f61;
  box-shadow: 0 16px 22px -17px #03153b;
  color: #fff;
  cursor: pointer;
  font-size: 16px;
  line-height: 20px;
  padding: 12px 20px;
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 999;
}

.floating-btn:hover {
  background-color: #ffffff;
  color: #001f61;
}

.floating-btn:focus {
  outline: none;
}

.floating-text {
  background-color: #001f61;
  border-radius: 10px 10px 0 0;
  color: #fff;
  font-family: "Muli";
  padding: 7px 15px;
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  z-index: 998;
}

.floating-text a {
  color: #ff7500;
  text-decoration: none;
}

@media screen and (max-width: 480px) {
  .social-panel-container.visible {
    transform: translateX(0px);
  }

  .floating-btn {
    right: 10px;
  }
}
.h3,
h3 {
  font-size: 1.1rem;
  font-weight: normal;
}

.faq-toggle {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq-toggle i {
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq.active .faq-toggle {
  transform: rotate(180deg);
}

.faq-title {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.faq.active .faq-title {
  transform: translateY(-2px);
}
</style>
