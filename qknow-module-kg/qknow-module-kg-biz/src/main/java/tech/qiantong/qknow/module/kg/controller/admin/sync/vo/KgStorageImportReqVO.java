/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 */
package tech.qiantong.qknow.module.kg.controller.admin.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "第三方存储文件导入知识文件请求")
public class KgStorageImportReqVO extends KgStorageSelectionReqVO {

    @NotNull(message = "所属分类不能为空")
    private Long categoryId;

    private String categoryName;

    @Size(max = 1024, message = "文件描述长度不能超过1024个字符")
    private String description;

    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;
}
