package tech.qiantong.qknow.module.dm.controller.admin.DmIotDevice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.assertj.core.util.Lists;
import tech.qiantong.qknow.common.core.page.PageParam;

import java.util.List;

/**
 * 物联网设备 Request VO 对象 dm_measure_point
 *
 * @author qknow
 * @date 2025-02-20
 */
@Schema(description = "物联网设备 Request VO")
@Data
public class DmDeviceReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "设备名称", example = "")
    private String deviceName;

    @Schema(description = "产品key", example = "")
    private String productKey;

    @Schema(description = "属性列表", example = "")
    private List<String> list;
}
