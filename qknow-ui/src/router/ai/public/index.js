

export default [
    {
        path: '/system/ai/modelMarket',
        hidden: true,
        children: [
            {
                path: 'detail',
                component: () => import('@/views/ai/modelMarket/detail/index.vue'),
                name: 'ModelMarketDetail',
                meta: { title: '模型详情', activeMenu: '/system/ai/modelMarket'}
            }
        ]
    },
    {
        path: '/system/ai/myModel',
        hidden: true,
        children: [
            {
                path: 'detail',
                component: () => import('@/views/ai/modelMarket/detail/index.vue'),
                name: 'myModelDetail',
                meta: { title: '模型详情', activeMenu: '/system/ai/myModel'  }
            }
        ]
    }
]
