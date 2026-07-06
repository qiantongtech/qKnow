/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

(function (para) {
    var p = para.sdk_url,
        n = para.name,
        w = window,
        d = document,
        s = 'script',
        x = null,
        y = null;
    if (typeof w['sensorsDataAnalytic201505'] !== 'undefined') {
        return false;
    }
    w['sensorsDataAnalytic201505'] = n;
    w[n] =
        w[n] ||
        function (a) {
            return function () {
                (w[n]._q = w[n]._q || []).push([a, arguments]);
            };
        };
    var ifs = [
        'track',
        'quick',
        'register',
        'registerPage',
        'registerOnce',
        'trackSignup',
        'trackAbtest',
        'setProfile',
        'setOnceProfile',
        'appendProfile',
        'incrementProfile',
        'deleteProfile',
        'unsetProfile',
        'identify',
        'login',
        'logout',
        'trackLink',
        'clearAllRegister',
        'getAppStatus'
    ];
    for (var i = 0; i < ifs.length; i++) {
        w[n][ifs[i]] = w[n].call(null, ifs[i]);
    }
    if (!w[n]._t) {
        w[n].para = para;

        var scriptURLs = [
            '/plugins/clklog.webjs.sdk.v3/session-event/index.js', //引用的session-event插件路径
            p
        ];

        function loadScript(index) {
            if (index >= scriptURLs.length) {
                return false;
            }

            var x = d.createElement(s);
            y = d.getElementsByTagName(s)[0];
            x.async = 1;
            x.src = scriptURLs[index];
            x.setAttribute('charset', 'UTF-8');
            x.onload = function () {
                loadScript(index + 1);
            };
            y.parentNode.insertBefore(x, y);
        }

        loadScript(0);
    }
    sensors.quick('isReady', function () {
        sensors.use('PageLeave', { heartbeat_interval_time: 5 });
        sensors.use('PageLoad');
        sensors.use('SessionEvent');
    });
    sensors.quick('autoTrackSinglePage');
})({
    sdk_url: '/plugins/clklog.webjs.sdk.v3/sensorsdata2.js',
    name: 'sensors',
    show_log: true,
    is_track_single_page: false,
    send_type: 'beacon',
    // server_url: 'http://110.42.38.62:45003/receiver/api/gp?project=ceshi&token=d7a41dff-f42e-4d4a-880d-2472fd594ab5', //请修改接收地址为clklog_receiver 的接收服务地址，必须传入project和token参数
    server_url:
        'https://clklog.qiantong.tech:7103/receiver/api/gp?project=qKnow-pro&token=e512a2f1-68ae-4094-be1a-614e72d24c82', //请修改接收地址为clklog_receiver 的接收服务地址，必须传入project和token参数
    // server_url: 'http://fengchao.qiantong.tech/prod-api/clklog?project=ceshi&token=d7a41dff-f42e-4d4a-880d-2472fd594ab5', //请修改接收地址为clklog_receiver 的接收服务地址，必须传入project和token参数
    heatmap: {
        clickmap: 'default',
        scroll_notice_map: 'default',
        collect_tags: {
            div: true,
            img: true
        }
    },
    preset_properties: { latest_referrer_host: true }
});
