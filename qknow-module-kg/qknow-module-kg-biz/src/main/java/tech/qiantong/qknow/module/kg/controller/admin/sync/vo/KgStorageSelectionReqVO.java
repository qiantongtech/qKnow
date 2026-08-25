/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 */
package tech.qiantong.qknow.module.kg.controller.admin.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "第三方存储文件选择请求")
public class KgStorageSelectionReqVO extends KgStorageBrowseReqVO {

    @Valid
    @NotEmpty(message = "请选择至少一个文件或文件夹")
    private List<SelectedItem> selectedItems;

    @Data
    public static class SelectedItem {
        private String name;
        @NotBlank(message = "文件路径不能为空")
        private String path;
        private Boolean directory;
    }
}
