package tech.qiantong.qknow.module.ext.api.extStructTask.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * 结构化抽取任务 DTO 对象 ext_struct_task
 *
 * @author qknow
 * @date 2025-02-25
 */
@Data
public class ExtStructTaskRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 任务名称 */
    private String name;

    /** 任务状态 */
    private Integer status;

    /** 发布状态 */
    private Integer publishStatus;

    /** 发布时间 */
    private Date publishTime;

    /** 发布人id */
    private Long publisherId;

    /** 发布人 */
    private String publishBy;

    /** 数据源id */
    private Long datasourceId;

    /** 数据源名称 */
    private String datasourceName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
