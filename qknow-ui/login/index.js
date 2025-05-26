import { createApp } from 'vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

import Cookies from 'js-cookie'

import './index.scss';
import '@/assets/system/styles/anivia.scss'

import App from './index.vue'

const app = createApp(App);

const store = createPinia()
app.use(store)

app.use(ElementPlus, {
    locale: locale,
    size: Cookies.get('size') || 'default'
})

app.mount('#app')