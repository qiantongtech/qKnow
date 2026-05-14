<template>
  <div class="arch-flow">
    <div class="arch-flow__backdrop" aria-hidden="true">
      <span class="arch-flow__glow arch-flow__glow--left"></span>
      <span class="arch-flow__glow arch-flow__glow--center"></span>
      <span class="arch-flow__glow arch-flow__glow--right"></span>
      <span class="arch-flow__panel arch-flow__panel--top"></span>
      <span class="arch-flow__panel arch-flow__panel--bottom"></span>
      <span class="arch-flow__brand arch-flow__brand--left">qKnow</span>
      <span class="arch-flow__brand arch-flow__brand--right">qKnow</span>
    </div>

    <svg
      class="arch-flow__lines"
      viewBox="0 0 520 260"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
    >
      <defs>
        <linearGradient
          id="arch-line-left-gradient"
          x1="108"
          y1="64"
          x2="158"
          y2="196"
          gradientUnits="userSpaceOnUse"
        >
          <stop offset="0%" stop-color="rgba(117, 201, 255, 0.1)" />
          <stop offset="55%" stop-color="rgba(84, 255, 175, 0.95)" />
          <stop offset="100%" stop-color="rgba(149, 190, 255, 0.72)" />
        </linearGradient>
        <linearGradient
          id="arch-line-right-gradient"
          x1="358"
          y1="64"
          x2="408"
          y2="196"
          gradientUnits="userSpaceOnUse"
        >
          <stop offset="0%" stop-color="rgba(255, 184, 242, 0.72)" />
          <stop offset="50%" stop-color="rgba(255, 148, 229, 0.95)" />
          <stop offset="100%" stop-color="rgba(205, 143, 255, 0.28)" />
        </linearGradient>
      </defs>

      <path
        class="arch-flow__line arch-flow__line--left"
        d="M 158 64 C 140 74, 124 88, 124 102 C 124 110, 116 118, 110 124 C 116 130, 124 138, 124 146 C 124 162, 140 180, 158 196"
      />
      <path
        class="arch-flow__line arch-flow__line--right"
        d="M 358 64 C 376 74, 392 88, 392 102 C 392 110, 400 118, 406 124 C 400 130, 392 138, 392 146 C 392 162, 376 180, 358 196"
      />

      <circle class="arch-flow__trail arch-flow__trail--green" r="4">
        <animateMotion
          dur="2.6s"
          repeatCount="indefinite"
          calcMode="linear"
          keyPoints="0;1"
          keyTimes="0;1"
          path="M 110 124 C 116 118, 124 110, 124 102 C 124 88, 140 74, 158 64"
        />
      </circle>
      <circle class="arch-flow__trail arch-flow__trail--cyan" r="3.5">
        <animateMotion
          dur="2.6s"
          repeatCount="indefinite"
          calcMode="linear"
          keyPoints="0;1"
          keyTimes="0;1"
          path="M 110 124 C 116 130, 124 138, 124 146 C 124 162, 140 180, 158 196"
        />
      </circle>
      <circle class="arch-flow__trail arch-flow__trail--pink" r="4">
        <animateMotion
          dur="2.9s"
          repeatCount="indefinite"
          calcMode="linear"
          keyPoints="0;1"
          keyTimes="0;1"
          path="M 358 64 C 376 74, 392 88, 392 102 C 392 110, 400 118, 406 124"
        />
      </circle>
      <circle class="arch-flow__trail arch-flow__trail--violet" r="3.5">
        <animateMotion
          dur="2.9s"
          repeatCount="indefinite"
          calcMode="linear"
          keyPoints="0;1"
          keyTimes="0;1"
          path="M 358 196 C 376 180, 392 162, 392 146 C 392 138, 400 130, 406 124"
        />
      </circle>
    </svg>

    <div class="arch-node arch-node--left">
      <div class="arch-node__ring">
        <span class="arch-node__ring-glow"></span>
        <svg class="arch-node__ring-svg" viewBox="0 0 92 92" aria-hidden="true">
          <circle class="arch-node__ring-base" cx="46" cy="46" r="33.5" />
          <g ref="heroRotatingArc">
            <path class="arch-node__ring-arc" d="M 46 17 A 29 29 0 0 1 75 46" />
          </g>
        </svg>

        <div class="arch-node__circle">
          <svg
            class="arch-node__icon-svg"
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <rect x="7" y="7" width="10" height="10" rx="2.5"></rect>
            <path
              d="M12 4.8v2.1M12 17.1v2.1M4.8 12h2.1M17.1 12h2.1M6.7 6.7l1.5 1.5M15.8 15.8l1.5 1.5M17.3 6.7l-1.5 1.5M8.2 15.8l-1.5 1.5"
            ></path>
          </svg>
        </div>
      </div>
      <div class="arch-node__label">知识资产 &amp; 模型</div>
    </div>

    <div class="arch-stack">
      <div class="arch-card arch-card--top">
        <div class="arch-card__icon arch-card__icon--green">
          <svg
            class="arch-card__icon-svg"
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path d="M8 8V7a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v1"></path>
            <path
              d="M5.5 9.5h13a1 1 0 0 1 1 1v6.8a1.2 1.2 0 0 1-1.2 1.2H5.7a1.2 1.2 0 0 1-1.2-1.2v-6.8a1 1 0 0 1 1-1Z"
            ></path>
            <path d="M4.5 12.2h15"></path>
          </svg>
        </div>
        <div class="arch-card__content">
          <h3>Bot 构建层</h3>
          <p>智能体技能封装</p>
        </div>
      </div>

      <div class="arch-card arch-card--bottom">
        <div class="arch-card__icon arch-card__icon--blue">
          <svg
            class="arch-card__icon-svg"
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path d="M12 4.8 17.5 8 12 11.2 6.5 8 12 4.8Z"></path>
            <path d="M6.5 8v8l5.5 3.2V11.2"></path>
            <path d="M17.5 8v8L12 19.2"></path>
          </svg>
        </div>
        <div class="arch-card__content">
          <h3>场景应用层</h3>
          <p>场景编排与组装</p>
        </div>
      </div>
    </div>

    <div class="arch-node arch-node--right">
      <div class="arch-node__diamond-wrap">
        <span
          ref="heroPulseTop"
          class="arch-node__pulse arch-node__pulse--top"
        ></span>
        <span
          ref="heroPulseBottom"
          class="arch-node__pulse arch-node__pulse--bottom"
        ></span>

        <div class="arch-node__diamond">
          <svg
            class="arch-node__diamond-svg"
            viewBox="0 0 24 24"
            aria-hidden="true"
          >
            <path d="M6.5 8.5 12 5.5l5.5 3-5.5 3-5.5-3Z"></path>
            <path d="M8 12.1 12 14.3l4-2.2"></path>
            <path d="M9.2 15.3 12 16.8l2.8-1.5"></path>
          </svg>
        </div>
      </div>
      <div class="arch-node__label arch-node__label--right">业务解决方案</div>
    </div>
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from "vue";
import { gsap } from "gsap";

const heroRotatingArc = ref(null);
const heroPulseTop = ref(null);
const heroPulseBottom = ref(null);

const motionTweens = [];

onMounted(() => {
  if (heroRotatingArc.value) {
    motionTweens.push(
      gsap.to(heroRotatingArc.value, {
        rotation: 360,
        svgOrigin: "46 46",
        duration: 3.6,
        repeat: -1,
        ease: "none",
      })
    );
  }

  if (heroPulseTop.value) {
    motionTweens.push(
      gsap.to(heroPulseTop.value, {
        scale: 1.45,
        opacity: 0.34,
        duration: 1.1,
        repeat: -1,
        yoyo: true,
        ease: "sine.inOut",
      })
    );
  }

  if (heroPulseBottom.value) {
    motionTweens.push(
      gsap.to(heroPulseBottom.value, {
        scale: 1.6,
        opacity: 0.3,
        duration: 1.35,
        repeat: -1,
        yoyo: true,
        ease: "sine.inOut",
        delay: 0.18,
      })
    );
  }
});

onBeforeUnmount(() => {
  motionTweens.forEach((tween) => tween.kill());
});
</script>

<style scoped>
.arch-flow {
  position: relative;
  width: 100%;
  min-height: 260px;
  color: #fff;
}

.arch-flow__backdrop,
.arch-flow__lines {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.arch-flow__backdrop {
  z-index: 0;
  overflow: hidden;
}

.arch-flow__glow,
.arch-flow__panel,
.arch-flow__brand {
  position: absolute;
  display: block;
}

.arch-flow__glow {
  border-radius: 50%;
  filter: blur(1.5px);
}

.arch-flow__glow--left {
  left: -26px;
  top: 84px;
  width: 108px;
  height: 108px;
  background: radial-gradient(
    circle,
    rgba(92, 182, 255, 0.16) 0%,
    rgba(92, 182, 255, 0.06) 54%,
    transparent 68%
  );
}

.arch-flow__glow--center {
  left: 170px;
  top: -86px;
  width: 214px;
  height: 214px;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.1) 0%,
    rgba(255, 255, 255, 0.04) 34%,
    transparent 60%
  );
}

.arch-flow__glow--right {
  right: -34px;
  top: 88px;
  width: 108px;
  height: 108px;
  background: radial-gradient(
    circle,
    rgba(190, 112, 255, 0.14) 0%,
    rgba(190, 112, 255, 0.05) 50%,
    transparent 66%
  );
}

.arch-flow__panel {
  border-radius: 28px;
  opacity: 0.14;
}

.arch-flow__panel--top {
  left: 176px;
  top: -18px;
  width: 138px;
  height: 180px;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.16) 0%,
    rgba(255, 255, 255, 0.02) 100%
  );
  transform: rotate(8deg);
}

.arch-flow__panel--bottom {
  left: 196px;
  bottom: -56px;
  width: 130px;
  height: 208px;
  background: linear-gradient(
    180deg,
    rgba(110, 122, 255, 0.14) 0%,
    rgba(72, 82, 204, 0.02) 100%
  );
  transform: rotate(7deg);
}

.arch-flow__brand {
  color: rgba(24, 26, 95, 0.15);
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: lowercase;
}

.arch-flow__brand--left {
  left: 24px;
  top: 34px;
  transform: rotate(-27deg);
}

.arch-flow__brand--right {
  right: 46px;
  top: 156px;
  transform: rotate(-31deg);
}

.arch-flow__lines {
  z-index: 1;
  width: 100%;
  height: 100%;
}

.arch-flow__line {
  fill: none;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-dasharray: 3.5 9;
}

.arch-flow__line--left {
  stroke: url(#arch-line-left-gradient);
}

.arch-flow__line--right {
  stroke: url(#arch-line-right-gradient);
}

.arch-flow__trail {
  filter: drop-shadow(0 0 8px currentColor);
}

.arch-flow__trail--green {
  fill: #62ffb0;
  color: #62ffb0;
}

.arch-flow__trail--cyan {
  fill: #8ebeff;
  color: #8ebeff;
}

.arch-flow__trail--pink {
  fill: #ff9ce8;
  color: #ff9ce8;
}

.arch-flow__trail--violet {
  fill: #d59cff;
  color: #d59cff;
}

.arch-node,
.arch-stack {
  position: absolute;
  z-index: 3;
}

.arch-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  text-align: center;
  cursor: pointer;
}

.arch-node--left {
  left: 30px;
  top: 70px;
}

.arch-node--right {
  right: 25px;
  top: 60px;
}

.arch-node__ring {
  position: relative;
  width: 92px;
  height: 92px;
  overflow: hidden;
  border-radius: 50%;
  transition: transform 0.36s cubic-bezier(0.22, 1, 0.36, 1),
    box-shadow 0.32s ease;
}

.arch-node__ring-glow {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  background: radial-gradient(
    circle,
    rgba(95, 189, 255, 0.2) 0%,
    rgba(95, 189, 255, 0.08) 54%,
    transparent 76%
  );
  filter: blur(1px);
}

.arch-node__ring-svg {
  position: absolute;
  inset: 0;
  width: 92px;
  height: 92px;
}

.arch-node__ring-base {
  fill: rgba(64, 117, 233, 0.18);
  stroke: rgba(118, 192, 255, 0.34);
  stroke-width: 1.7;
}

.arch-node__ring-arc {
  fill: none;
  stroke: #58d7ff;
  stroke-width: 3;
  stroke-linecap: round;
  filter: drop-shadow(0 0 9px rgba(88, 215, 255, 0.7));
}

.arch-node__circle {
  position: absolute;
  inset: 12px;
  display: grid;
  place-items: center;
  border-radius: 50%;
  background: radial-gradient(
      circle at 30% 26%,
      rgba(144, 202, 255, 0.28) 0%,
      rgba(144, 202, 255, 0.08) 28%,
      transparent 55%
    ),
    linear-gradient(
      180deg,
      rgba(73, 118, 255, 0.18) 0%,
      rgba(35, 89, 216, 0.12) 100%
    );
  box-shadow: inset 0 0 0 1px rgba(127, 192, 255, 0.24),
    0 0 24px rgba(85, 166, 255, 0.14);
  transition: transform 0.36s cubic-bezier(0.22, 1, 0.36, 1),
    box-shadow 0.32s ease;
}

.arch-node__icon-svg {
  width: 24px;
  height: 24px;
  stroke: #a9d7ff;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
  fill: none;
  transform-origin: center;
  transition: transform 0.34s cubic-bezier(0.22, 1, 0.36, 1);
}

.arch-node__label {
  white-space: nowrap;
  color: rgba(255, 255, 255, 0.84);
  font-size: 14px;
  font-weight: 600;
  text-shadow: 0 2px 10px rgba(17, 23, 92, 0.28);
}

.arch-node__label--right {
  color: rgba(255, 236, 255, 0.84);
}

.arch-stack {
  left: 154px;
  top: 28px;
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.arch-card {
  display: flex;
  align-items: center;
  width: 184px;
  min-height: 68px;
  padding: 12px 16px;
  border-radius: 18px;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.18) 0%,
    rgba(255, 255, 255, 0.08) 100%
  );
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.1),
    0 18px 36px rgba(24, 34, 114, 0.2);
  backdrop-filter: blur(18px);
  cursor: pointer;
  transition: transform 0.36s cubic-bezier(0.22, 1, 0.36, 1),
    box-shadow 0.32s ease, border-color 0.32s ease;
}

.arch-card--top {
  margin-left: 12px;
}

.arch-card--bottom {
  margin-left: 12px;
  margin-top: 17px;
}

.arch-card__icon {
  display: grid;
  place-items: center;
  flex-shrink: 0;
  width: 42px;
  height: 42px;
  margin-right: 14px;
  border-radius: 13px;
  color: #fff;
  transition: transform 0.36s cubic-bezier(0.22, 1, 0.36, 1),
    box-shadow 0.32s ease;
}

.arch-card__icon--green {
  background: linear-gradient(135deg, #43e08b 0%, #1fc9a8 100%);
  box-shadow: 0 12px 24px rgba(40, 212, 152, 0.28);
}

.arch-card__icon--blue {
  background: linear-gradient(135deg, #78a0ff 0%, #60b9ff 100%);
  box-shadow: 0 12px 24px rgba(102, 149, 255, 0.28);
}

.arch-card__icon-svg {
  width: 21px;
  height: 21px;
  stroke: currentColor;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
  fill: none;
}

.arch-card__content h3,
.arch-card__content p {
  margin: 0;
}

.arch-card__content h3 {
  font-size: 15px;
  font-weight: 700;
  line-height: 1.25;
}

.arch-card__content p {
  margin-top: 6px;
  color: rgba(255, 255, 255, 0.76);
  font-size: 11px;
  font-weight: 600;
  line-height: 1.2;
}

.arch-card:hover {
  transform: translateY(-8px);
  border-color: rgba(255, 255, 255, 0.28);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.16),
    0 24px 40px rgba(24, 34, 114, 0.28);
}

.arch-card:hover .arch-card__icon {
  transform: translateY(-2px);
}

.arch-node__diamond-wrap {
  position: relative;
  width: 102px;
  height: 102px;
}

.arch-node__diamond {
  position: absolute;
  inset: 17px;
  display: grid;
  place-items: center;
  border-radius: 18px;
  background: linear-gradient(
    180deg,
    rgba(112, 60, 220, 0.62) 0%,
    rgba(84, 36, 168, 0.42) 100%
  );
  box-shadow: inset 0 0 0 1px rgba(234, 192, 255, 0.14),
    0 20px 36px rgba(72, 24, 156, 0.34);
  transform: rotate(45deg);
  transform-origin: center;
  transition: transform 0.42s cubic-bezier(0.22, 1, 0.36, 1),
    border-radius 0.32s ease, box-shadow 0.32s ease;
}

.arch-node__diamond-svg {
  width: 24px;
  height: 24px;
  stroke: #edd6ff;
  stroke-width: 1.7;
  stroke-linecap: round;
  stroke-linejoin: round;
  fill: none;
  transform: rotate(-45deg);
  transform-origin: center;
  transition: transform 0.42s cubic-bezier(0.22, 1, 0.36, 1), opacity 0.28s ease;
}

.arch-node__pulse {
  position: absolute;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  z-index: 3;
  box-shadow: 0 0 12px currentColor;
}

.arch-node__pulse--top {
  left: 2px;
  top: 28px;
  color: #ff9fe6;
  background: currentColor;
}

.arch-node__pulse--bottom {
  left: 6px;
  bottom: 28px;
  color: #d59cff;
  background: currentColor;
}

.arch-node--left:hover .arch-node__ring {
  transform: translateY(-1px);
}

.arch-node--left:hover .arch-node__circle {
  box-shadow: inset 0 0 0 1px rgba(127, 192, 255, 0.3),
    0 0 28px rgba(85, 166, 255, 0.2);
}

.arch-node--left:hover .arch-node__icon-svg {
  transform: scale(1.16);
}

.arch-node--right:hover .arch-node__diamond {
  transform: rotate(0deg);
  border-radius: 14px;
  box-shadow: inset 0 0 0 1px rgba(234, 192, 255, 0.18),
    0 22px 40px rgba(72, 24, 156, 0.42);
}

.arch-node--right:hover .arch-node__diamond-svg {
  transform: rotate(0deg) scale(1.04);
}

@media (max-width: 900px) {
  .arch-flow {
    min-height: 280px;
  }
}

@media (max-width: 640px) {
  .arch-flow {
    min-height: 248px;
  }

  .arch-node--left {
    left: 0;
    transform: scale(0.92);
    transform-origin: left center;
  }

  .arch-node--right {
    right: -2px;
    transform: scale(0.92);
    transform-origin: right center;
  }

  .arch-stack {
    left: 148px;
    transform: scale(0.95);
    transform-origin: left top;
  }
}
</style>
