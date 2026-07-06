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

package tech.qiantong.qknow.module.app.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类封装NamedParameterSql
 */
public class ParsedSql implements Serializable {

    private static final long serialVersionUID=1L;

    private String originalSql;
    //参数名
    private List<String> paramNames = new ArrayList<>();
    //参数在sql中对应的位置
    private List<int[]> paramIndexs = new ArrayList<>();
    //统计参数个数（不包含重复）
    private int namedParamCount;
    //统计sql中？的个数
    private int unnamedParamCount;

    private int totalParamCount;

    public ParsedSql(String originalSql){
        this.originalSql = originalSql;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void addParamNames(String paramName,int startIndex,int endIndex) {
        paramNames.add(paramName);
        paramIndexs.add(new int[]{startIndex,endIndex});
    }

    public int[] getParamIndexs(int position) {
        return paramIndexs.get(position);
    }

    public String getOriginalSql() {
        return originalSql;
    }

    public int getNamedParamCount() {
        return namedParamCount;
    }

    public void setNamedParamCount(int namedParamCount) {
        this.namedParamCount = namedParamCount;
    }

    public int getUnnamedParamCount() {
        return unnamedParamCount;
    }

    public void setUnnamedParamCount(int unnamedParamCount) {
        this.unnamedParamCount = unnamedParamCount;
    }

    public int getTotalParamCount() {
        return totalParamCount;
    }

    public void setTotalParamCount(int totalParamCount) {
        this.totalParamCount = totalParamCount;
    }

    @Override
    public String toString() {
        return "ParsedSql{" +
                "originalSql='" + originalSql + '\'' +
                ", paramNames=" + paramNames +
                ", paramIndexs=" + paramIndexs +
                ", namedParamCount=" + namedParamCount +
                ", unnamedParamCount=" + unnamedParamCount +
                ", totalParamCount=" + totalParamCount +
                '}';
    }
}
