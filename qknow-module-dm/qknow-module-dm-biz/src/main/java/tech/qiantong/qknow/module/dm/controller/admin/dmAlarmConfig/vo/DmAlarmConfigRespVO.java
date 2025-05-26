package tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qknow.common.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * 告警配置 Response VO 对象 dm_alarm_config
 *
 * @author qknow
 * @date 2025-02-19
 */
@Schema(description = "告警配置 Response VO")
@Data
public class DmAlarmConfigRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Excel(name = "告警名称")
    @Schema(description = "告警名称", example = "")
    private String name;

    @Excel(name = "工况编码")
    @Schema(description = "工况编码", example = "")
    private String condCode;

    @Excel(name = "工况")
    @Schema(description = "工况", example = "")
    private String operateCond;

    @Excel(name = "告警类型")
    @Schema(description = "告警类型", example = "")
    private Integer type;

    @Excel(name = "阈值上限")
    @Schema(description = "阈值上限", example = "")
    private BigDecimal upper;

    @Excel(name = "阈值下限")
    @Schema(description = "阈值下限", example = "")
    private BigDecimal lower;

    @Excel(name = "告警内容")
    @Schema(description = "告警内容", example = "")
    private String content;

    @Excel(name = "预案")
    @Schema(description = "预案", example = "")
    private String plan;

    @Excel(name = "预案简称")
    @Schema(description = "预案简称", example = "")
    private String planShort;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
