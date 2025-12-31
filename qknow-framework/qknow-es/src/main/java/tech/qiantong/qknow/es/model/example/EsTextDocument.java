/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.es.model.example;

import org.dromara.easyes.annotation.HighLight;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

/**
 * 全文索引 ES数据模型
 *
 * @author qknow*/
@IndexName("text_document")
public class EsTextDocument
{
    /**
     * es中的唯一id
     */
    @IndexId(type = IdType.NONE)
    private String id;

    /** 公告ID */
    private Long noticeId;

    /** 公告标题 */
    @HighLight(preTag = "<font color=\"red\">", postTag = "</font>")
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String noticeTitle;

    /** 公告类型 */
    private String noticeType;

    /** 内容 */
    @HighLight(preTag = "<font color=\"red\">", postTag = "</font>")
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String noticeContent;

    /** 公告状态 */
    private String status;

    public String getId()
    {
        return id;
    }

    public Long getNoticeId()
    {
        return noticeId;
    }

    public String getNoticeTitle()
    {
        return noticeTitle;
    }

    public String getNoticeType()
    {
        return noticeType;
    }

    public String getNoticeContent()
    {
        return noticeContent;
    }

    public String getStatus()
    {
        return status;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public void setNoticeTitle(String noticeTitle)
    {
        this.noticeTitle = noticeTitle;
    }

    public void setNoticeType(String noticeType)
    {
        this.noticeType = noticeType;
    }

    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
