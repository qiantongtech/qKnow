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

!function(){"use strict";function t(t,i,s){if(i&&(t.plugin_name=i),s&&t.init){var o=t.init;t.init=function(r,n){function p(){o.call(t,r,n)}return e(r,t,i),r.readyState&&r.readyState.state>=3||!r.on?p():void r.on(s,p)}}return t}function e(t,e,i){function s(e,s){t.logger?t.logger.msg.apply(t.logger,s).module(i+""||"").level(e).log():t.log&&t.log.apply(t,s)}e.log=function(){s("log",arguments)},e.warn=function(){s("warn",arguments)},e.error=function(){s("error",arguments)}}function i(e,i,s){return t(e,i,s),e.plugin_version=p,e}function s(t,e){if("track"!==t.type)return t;var i=e.sd,s=i._,o=i.saEvent.check,r=s.extend2Lev({properties:{}},t),n=e.customRegister,p=r.properties,h=r.event,c={};return s.each(n,function(t){if(s.isObject(t))s.indexOf(t.events,h)>-1&&o({properties:t.properties})&&(c=s.extend(c,t.properties));else if(s.isFunction(t)){var e=t({event:h,properties:p,data:r});s.isObject(e)&&!s.isEmptyObject(e)&&o({properties:e})&&(c=s.extend(c,e))}}),t.properties=s.extend(p,c),t}function o(){this.sd=null,this.log=window.console&&window.console.log||function(){},this.customRegister=[]}function r(t){this.sd=t,this._=t._,this.cookie_value=null}function n(){this.registerProperties=null,this.store=null,this.sd=null,this._=null,this.log=window.console&&window.console.log||function(){},this.cookie_name="",this.prop={}}var p="1.25.15";o.prototype.init=function(t){if(t){this.sd=t,this._=t._,this.log=t.log;var e=this;t.registerInterceptor("buildDataStage",{extendProps:{priority:0,entry:function(t){return s(t,e)}}})}else this.log("\u795e\u7b56JS SDK\u672a\u6210\u529f\u5f15\u5165")},o.prototype.register=function(t){return this.sd?void(this._.isObject(t)&&this._.isArray(t.events)&&t.events.length>0&&this._.isObject(t.properties)&&!this._.isEmptyObject(t.properties)?this.customRegister.push(t):this.log("RegisterProperties: register \u53c2\u6570\u9519\u8bef")):void this.log("\u795e\u7b56JS SDK\u672a\u6210\u529f\u5f15\u5165")},o.prototype.hookRegister=function(t){return this.sd?void(this._.isFunction(t)?this.customRegister.push(t):this.log("RegisterProperties: hookRegister \u53c2\u6570\u9519\u8bef")):void this.log("\u795e\u7b56JS SDK\u672a\u6210\u529f\u5f15\u5165")},r.prototype.saveObjectVal=function(t,e){this._.isString(e)||(e=JSON.stringify(e)),1==this.sd.para.encrypt_cookie&&(e=this.sd.kit.userEncrypt(e)),this._.cookie.isSupport()&&this._.cookie.set(t,e),this.cookie_value=e},r.prototype.readObjectVal=function(t){var e=this._.cookie.isSupport()?this._.cookie.get(t):this.cookie_value;return e?(e=this.sd.kit.userDecryptIfNeeded(e),this._.safeJSONParse(e)):null};var h="sensorsdata2015jssdksession";n.prototype.init=function(t){if(!t||"object"!=typeof t)return void this.log("Session Event \u63d2\u4ef6\u521d\u59cb\u5316\u5931\u8d25\uff01");var e=this;this.sd=t,this._=t._,this.log=t.log,this.cookie_name=h+(t.para.sdk_id||""),this.registerProperties=new o,this.registerProperties.init(t),this.store=new r(t),this.registerProperties.hookRegister(function(){return e.addSessionID()})},n.prototype.addSessionID=function(){var t=+new Date;this.prop=this.store.readObjectVal(this.cookie_name)||{};var e=this.prop.first_session_time,i=this.prop.latest_session_time;if(!e||!i||e>t||i>t||t-e>432e5||t-i>18e5){var s=this._.UUID();this.prop={session_id:s.replace(/-/g,""),first_session_time:t,latest_session_time:t}}else this.prop.latest_session_time=t;return this.store.saveObjectVal(this.cookie_name,this.prop),{$event_session_id:this.prop.session_id}};var c=new n,u=i(c,"SessionEvent","sdkReady");return u}();