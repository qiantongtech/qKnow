/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 */
package tech.qiantong.qknow.module.kg.controller.admin.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "第三方存储文件节点")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KgStorageFileRespVO {

    private String name;
    private String path;
    private Boolean directory;
    private Boolean hasChildren;
    private Long size;
    private String lastModified;
    private String fileType;
}
