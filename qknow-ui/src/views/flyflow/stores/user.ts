// import {defineStore} from "pinia";
//
// import {loginApi, loginByTokenApi, logoutApi,loginAtDingTalkApi} from "../api/auth";
// import {getUserInfo} from "../api/user";
// //TODO
// import store from "@/store";
//
// import {LoginData} from "../api/auth/types";
// import {UserInfo} from "../api/user/types";
//
// import {useStorage} from "@vueuse/core";
//
// import {ref} from 'vue'
// //TODO
// import { resetRouter } from "@/router";
// export const useUserStore = defineStore("user", () => {
//     // state
//     const userId = ref();
//     const token = useStorage("accessToken", "");
//     const tenantId = useStorage("tenantId", "");
//     const nickname = ref("");
//     const avatar = ref("");
//     const roles = ref<Array<string>>([]); // 用户角色编码集合 → 判断路由权限
//     const perms = ref<Array<string>>([]); // 用户权限编码集合 → 判断按钮权限
//
//     /**
//      * 登录调用
//      *
//      * @param {LoginData}
//      * @returns
//      */
//     function login(loginData: LoginData) {
//
//
//         return new Promise<void>((resolve, reject) => {
//             loginApi(loginData)
//                 .then((response) => {
//                     const {tokenValue} = response.data;
//                     token.value = tokenValue; // Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
// 					tenantId.value = loginData.tenantId; // Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
//                     resolve();
//                 })
//                 .catch((error) => {
//                     reject(error);
//                 });
//         });
//     }
//
//     /**
//      * 登录调用
//      *
//      * @param {LoginData}
//      * @returns
//      */
//     function loginByToken(t: string) {
//         return new Promise<void>((resolve, reject) => {
//             loginByTokenApi(t)
//                 .then((response) => {
//                     const {tokenValue} = response.data;
//                     token.value = tokenValue; // Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
//                     resolve();
//                 })
//                 .catch((error) => {
//                     reject(error);
//                 });
//         });
//     }
// 	/**
// 	 * 钉钉h5登录调用
// 	 *
// 	 * @param {LoginData}
// 	 * @returns
// 	 */
// 	function loginAtDingTalk(t: string) {
// 		return new Promise<void>((resolve, reject) => {
// 			loginAtDingTalkApi(t)
// 				.then((response) => {
// 					const {tokenValue} = response.data;
// 					token.value = tokenValue; // Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
// 					resolve();
// 				})
// 				.catch((error) => {
// 					reject(error);
// 				});
// 		});
// 	}
//
//
//     // 获取信息(用户昵称、头像、角色集合、权限集合)
//     function getInfo() {
//         return new Promise<UserInfo>((resolve, reject) => {
//             getUserInfo()
//                 .then(({data}) => {
//                     if (!data) {
//                         return reject("Verification failed, please Login again.");
//                     }
//                     if (!data.roles || data.roles.length <= 0) {
//                         reject("getUserInfo: roles must be a non-null array!");
//                     }
//                     userId.value = data.id;
//                     nickname.value = data.name;
//                     avatar.value = data.avatarUrl;
//                     roles.value = data.roles;
//                     perms.value = data.perms;
//                     resolve(data);
//                 })
//                 .catch((error) => {
//                     reject(error);
//                 });
//         });
//     }
//
//
//     // 注销
//     function logout() {
//         return new Promise<void>((resolve, reject) => {
//             logoutApi()
//                 .then(() => {
//                     //TODO 修改这里  返回登录
//                     resetRouter();
//                     resetToken();
//                     location.reload(); // 清空路由
//                     resolve();
//                 })
//                 .catch((error) => {
//                     reject(error);
//                 });
//         });
//     }
//
//     // 重置
//     function resetToken() {
//         token.value = "";
//         nickname.value = "";
//         avatar.value = "";
//         roles.value = [];
//         perms.value = [];
//     }
//
//     function setToken(tt: string) {
//         token.value = tt
//     }
//
//     return {
//         token,
//         nickname,
//         setToken,
//         avatar,
//         roles,
//         perms,
//         login,
//         loginByToken,loginAtDingTalk,
//         getInfo,
//         resetToken,
//         logout,
// 		tenantId,
//         /**
//          * 当前登录用户ID
//          */
//         userId,
//     };
// });
//
// // 非setup
// export function useUserStoreHook() {
//     return useUserStore(store);
// }
