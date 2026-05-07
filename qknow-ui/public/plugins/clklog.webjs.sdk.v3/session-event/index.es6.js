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

var sdkversion_placeholder="1.25.15";function wrapPluginInitFn(e,t,i){if(t&&(e.plugin_name=t),i&&e.init){var s=e.init;e.init=function(o,r){if(wrapLogFn(o,e,t),o.readyState&&o.readyState.state>=3||!o.on)return n();function n(){s.call(e,o,r)}o.on(i,n)}}return e}function wrapLogFn(e,t,i){function s(t,s){e.logger?e.logger.msg.apply(e.logger,s).module(i+""||"").level(t).log():e.log&&e.log.apply(e,s)}t.log=function(){s("log",arguments)},t.warn=function(){s("warn",arguments)},t.error=function(){s("error",arguments)}}function createPlugin(e,t,i){return wrapPluginInitFn(e,t,i),e.plugin_version=sdkversion_placeholder,e}function addProperties(e,t){if("track"!==e.type)return e;var i=t.sd,s=i._,o=i.saEvent.check,r=s.extend2Lev({properties:{}},e),n=t.customRegister,p=r.properties,a=r.event,l={};return s.each(n,function(e){if(s.isObject(e))s.indexOf(e.events,a)>-1&&o({properties:e.properties})&&(l=s.extend(l,e.properties));else if(s.isFunction(e)){var t=e({event:a,properties:p,data:r});s.isObject(t)&&!s.isEmptyObject(t)&&o({properties:t})&&(l=s.extend(l,t))}}),e.properties=s.extend(p,l),e}function RegisterProperties(){this.sd=null,this.log=window.console&&window.console.log||function(){},this.customRegister=[]}function Store(e){this.sd=e,this._=e._,this.cookie_value=null}RegisterProperties.prototype.init=function(e){if(e){this.sd=e,this._=e._,this.log=e.log;var t=this;e.registerInterceptor("buildDataStage",{extendProps:{priority:0,entry:function(e){return addProperties(e,t)}}})}else this.log("\u795e\u7b56JS SDK\u672a\u6210\u529f\u5f15\u5165")},RegisterProperties.prototype.register=function(e){this.sd?this._.isObject(e)&&this._.isArray(e.events)&&e.events.length>0&&this._.isObject(e.properties)&&!this._.isEmptyObject(e.properties)?this.customRegister.push(e):this.log("RegisterProperties: register \u53c2\u6570\u9519\u8bef"):this.log("\u795e\u7b56JS SDK\u672a\u6210\u529f\u5f15\u5165")},RegisterProperties.prototype.hookRegister=function(e){this.sd?this._.isFunction(e)?this.customRegister.push(e):this.log("RegisterProperties: hookRegister \u53c2\u6570\u9519\u8bef"):this.log("\u795e\u7b56JS SDK\u672a\u6210\u529f\u5f15\u5165")},Store.prototype.saveObjectVal=function(e,t){this._.isString(t)||(t=JSON.stringify(t)),1==this.sd.para.encrypt_cookie&&(t=this.sd.kit.userEncrypt(t)),this._.cookie.isSupport()&&this._.cookie.set(e,t),this.cookie_value=t},Store.prototype.readObjectVal=function(e){var t=this._.cookie.isSupport()?this._.cookie.get(e):this.cookie_value;return t?(t=this.sd.kit.userDecryptIfNeeded(t),this._.safeJSONParse(t)):null};var COOKIE_NAME="sensorsdata2015jssdksession";function SessionEvent(){this.registerProperties=null,this.store=null,this.sd=null,this._=null,this.log=window.console&&window.console.log||function(){},this.cookie_name="",this.prop={}}SessionEvent.prototype.init=function(e){if(e&&"object"==typeof e){var t=this;this.sd=e,this._=e._,this.log=e.log,this.cookie_name=COOKIE_NAME+(e.para.sdk_id||""),this.registerProperties=new RegisterProperties,this.registerProperties.init(e),this.store=new Store(e),this.registerProperties.hookRegister(function(){return t.addSessionID()})}else this.log("Session Event \u63d2\u4ef6\u521d\u59cb\u5316\u5931\u8d25\uff01")},SessionEvent.prototype.addSessionID=function(){var e=+new Date;this.prop=this.store.readObjectVal(this.cookie_name)||{};var t=this.prop.first_session_time,i=this.prop.latest_session_time;if(!t||!i||t>e||i>e||e-t>432e5||e-i>18e5){var s=this._.UUID();this.prop={session_id:s.replace(/-/g,""),first_session_time:e,latest_session_time:e}}else this.prop.latest_session_time=e;return this.store.saveObjectVal(this.cookie_name,this.prop),{$event_session_id:this.prop.session_id}};var instance=new SessionEvent,index=createPlugin(instance,"SessionEvent","sdkReady");export default index;