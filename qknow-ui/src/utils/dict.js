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

import useDictStore from '@/store/system/dict';
import { getDicts } from '@/api/system/system/dict/data';

/**
 * 获取字典数据
 */
export function useDict(...args) {
    const res = ref({});
    return (() => {
        args.forEach((dictType, index) => {
            res.value[dictType] = [];
            const dicts = useDictStore().getDict(dictType);
            if (dicts) {
                res.value[dictType] = dicts;
            } else {
                getDicts(dictType).then((resp) => {
                    res.value[dictType] = resp.data.map((p) => ({
                        label: p.dictLabel,
                        value: p.dictValue,
                        elTagType: p.listClass,
                        elTagClass: p.cssClass
                    }));
                    useDictStore().setDict(dictType, res.value[dictType]);
                });
            }
        });
        return toRefs(res.value);
    })();
}
