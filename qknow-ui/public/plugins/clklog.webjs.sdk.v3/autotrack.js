/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
 */

;(function (para) {
  var p = para.sdk_url,
    n = para.name,
    w = window,
    d = document,
    s = 'script',
    x = null,
    y = null
  if (typeof w['sensorsDataAnalytic201505'] !== 'undefined') {
    return false
  }
  w['sensorsDataAnalytic201505'] = n
  w[n] =
    w[n] ||
    function (a) {
      return function () {
        ;(w[n]._q = w[n]._q || []).push([a, arguments])
      }
    }
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
  ]
  for (var i = 0; i < ifs.length; i++) {
    w[n][ifs[i]] = w[n].call(null, ifs[i])
  }
  if (!w[n]._t) {
    w[n].para = para

    var scriptURLs = [
      '/plugins/clklog.webjs.sdk.v3/session-event/index.js', //引用的session-event插件路径
      p
    ]

    function loadScript(index) {
      if (index >= scriptURLs.length) {
        return false
      }

      var x = d.createElement(s)
      y = d.getElementsByTagName(s)[0]
      x.async = 1
      x.src = scriptURLs[index]
      x.setAttribute('charset', 'UTF-8')
      x.onload = function () {
        loadScript(index + 1)
      }
      y.parentNode.insertBefore(x, y)
    }

    loadScript(0)
  }
  sensors.quick('isReady', function () {
    sensors.use('PageLeave', { heartbeat_interval_time: 5 })
    sensors.use('PageLoad')
    sensors.use('SessionEvent')
  })
  sensors.quick('autoTrackSinglePage')
})({
  sdk_url: '/plugins/clklog.webjs.sdk.v3/sensorsdata2.js',
  name: 'sensors',
  show_log: true,
  is_track_single_page: false,
  send_type: 'beacon',
  // server_url: 'http://110.42.38.62:45003/receiver/api/gp?project=ceshi&token=d7a41dff-f42e-4d4a-880d-2472fd594ab5', //请修改接收地址为clklog_receiver 的接收服务地址，必须传入project和token参数
  server_url: 'https://clklog.qiantong.tech:7103/receiver/api/gp?project=qKnow-pro&token=e512a2f1-68ae-4094-be1a-614e72d24c82', //请修改接收地址为clklog_receiver 的接收服务地址，必须传入project和token参数
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
})
