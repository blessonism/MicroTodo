<template>
  <div class="background">
    <div class="container">
      <div class="panel pricing-table">
        <div
          class="pricing-plan"
          v-for="(plan, index) in pricingPlans"
          :key="index"
        >
          <img :src="plan.imgSrc" :alt="plan.header" class="pricing-img" />
          <h2 class="pricing-header">{{ plan.header }}</h2>
          <ul class="pricing-features">
            <li
              class="pricing-features-item"
              v-for="(feature, featureIndex) in plan.features"
              :key="featureIndex"
            >
              {{ feature }}
            </li>
          </ul>
          <span class="pricing-price">{{ plan.price }}</span>
          <a
            :href="plan.buttonLink"
            class="pricing-button"
            :class="{ 'is-featured': plan.isFeatured }"
          >
            {{ plan.buttonText }}
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PricingTable",
  mounted() {
    // 设置 body 的样式

    document.body.style.overflow = "hidden";
  },
  data() {
    return {
      pricingPlans: [
        {
          header: "Basic",
          imgSrc: "https://s22.postimg.cc/8mv5gn7w1/paper-plane.png",
          features: ["Create and manage tasks", "Monthly task limit: 50"],
          price: "Free",
          buttonText: "Sign up",
          buttonLink: "#/",
          isFeatured: false,
        },
        {
          header: "Premium",
          imgSrc: "https://s28.postimg.cc/ju5bnc3x9/plane.png",
          features: ["Unlimited tasks", "Monthly task limit: 500"],
          price: "$9.99",
          buttonText: "Free trial",
          buttonLink: "#/",
          isFeatured: true,
        },
        {
          header: "Enterprise",
          imgSrc: "https://s21.postimg.cc/tpm0cge4n/space-ship.png",
          features: ["Professional support", "Custom AI feature"],
          price: "$49.99",
          buttonText: "Free trial",
          buttonLink: "#/",
          isFeatured: false,
        },
      ],
    };
  },
};
</script>

<style scoped>
html {
  box-sizing: border-box;
  font-family: "Open Sans", sans-serif;
}
.background {
  padding: 0 25px 25px;
  position: relative;
  width: 100%;
}

.background::after {
  content: "";
  background: #60a9ff;
  background: -moz-linear-gradient(top, #60a9ff 0%, #4394f4 100%);
  background: -webkit-linear-gradient(top, #60a9ff 0%, #4394f4 100%);
  background: linear-gradient(to bottom, #60a9ff 0%, #4394f4 100%);
  filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#60a9ff', endColorstr='#4394f4',GradientType=0 );
  height: 350px;
  left: 0;
  position: absolute;
  top: 0;
  width: 100%;
  z-index: 1;
}

@media (min-width: 900px) {
  .background {
    padding: 0 0 25px;
  }
}

.container {
  margin: 0 auto;
  padding: 50px 0 0;
  max-width: 960px;
  width: 100%;
}

.panel {
  background-color: #fff;
  border-radius: 10px;
  padding: 15px 25px;
  position: relative;
  width: 100%;
  z-index: 10;
}

.pricing-table {
  box-shadow: 0px 10px 13px -6px rgba(0, 0, 0, 0.08),
    0px 20px 31px 3px rgba(0, 0, 0, 0.09), 0px 8px 20px 7px rgba(0, 0, 0, 0.02);
  display: flex;
  flex-direction: column;
}

@media (min-width: 900px) {
  .pricing-table {
    flex-direction: row;
  }
}

.pricing-table * {
  text-align: center;
  text-transform: uppercase;
}

.pricing-plan {
  border-bottom: 1px solid #e1f1ff;
  padding: 25px;
}

.pricing-plan:last-child {
  border-bottom: none;
}

@media (min-width: 900px) {
  .pricing-plan {
    border-bottom: none;
    border-right: 1px solid #e1f1ff;
    flex-basis: 100%;
    padding: 25px 50px;
  }

  .pricing-plan:last-child {
    border-right: none;
  }
}

.pricing-img {
  margin-bottom: 25px;
  max-width: 100%;
}

.pricing-header {
  color: #888;
  font-weight: 600;
  letter-spacing: 1px;
}

.pricing-features {
  list-style-type: none; /* 去掉列表前的点 */
  padding: 0;
  color: #016ff9;
  letter-spacing: 1px;
  margin: 50px 0 25px;
}

.pricing-features-item {
  border-top: 1px solid #e1f1ff;
  font-size: 12px;
  line-height: 1.5;
  padding: 15px 0;
}

.pricing-features-item:last-child {
  border-bottom: 1px solid #e1f1ff;
}

.pricing-price {
  color: #016ff9;
  display: block;
  font-size: 32px;
  font-weight: 700;
}

.pricing-button {
  border: 1px solid #9dd1ff;
  border-radius: 10px;
  color: #348efe;
  display: inline-block;
  margin: 25px 0;
  padding: 15px 35px;
  text-decoration: none;
  transition: all 150ms ease-in-out;
}

.pricing-button:hover,
.pricing-button:focus {
  background-color: #e1f1ff;
}

.pricing-button.is-featured {
  background-color: #48aaff;
  color: #fff;
}

.pricing-button.is-featured:hover,
.pricing-button.is-featured:active {
  background-color: #269aff;
}
</style>
