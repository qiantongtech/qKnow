// /*
//  * @Date: 2022-08-25 14:13:11
//  * @LastEditors: StavinLi 495727881@qq.com
//  * @LastEditTime: 2023-05-24 15:00:32
//  * @FilePath: /Workflow-Vue3/src/store/index.js
//  */
// import {defineStore} from "pinia";
// import {FormVO} from "../api/form/types";
// import {reactive} from "vue";
//
//
// export const useFlowStore = defineStore("flow", {
//     state: () => {
//         return {
// 			formValue:{
//
// 			},
//             step1: {
//                 logo: "",
//                 name: "",
//                 flowId: "",
// 				uniqueId: "",
//                 groupId: undefined,
//                 admin: reactive([]),
//                 rangeList: reactive([]),
//                 remark: "",
//             },
//             step3: {},
//             step2: [] as FormVO[],
//             step2Form: [] as FormVO[],
//             step2Pc: [] as FormVO[]
//         };
//     },
//     actions: {
// 		setFormValue(v){
// 			this.formValue=v;
// 		},
//         setStep2(p: FormVO[]) {
//             this.step2 = p;
//         },
//         setStep2Pc(p: FormVO[]) {
//             this.step2Pc = p;
//         },
//         setStep2Form(p: FormVO[]) {
//             this.step2Form = p;
//         },
//         setStep3(p: any) {
//             this.step3 = p;
//         }
//     },
// });
