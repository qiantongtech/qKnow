/* Layout */
import Layout from '@/layout/index.vue'

// 知识应用模块动公共路由
export default [
    {
        path: '/app/graphExploration',
        hidden: true,
        children: [
            {
                path: '2',
                component: () => import('@/views/app/graphExploration/index.vue'),
                name: 'graphExploration2',
                meta: { title: '图谱探索', activeMenu: '/app/graphExploration'  }
            }
        ]
    }
]
