package tech.qiantong.qknow.module.dm.controller.admin.DmIotDevice;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.module.dm.controller.admin.DmIotDevice.vo.DmDeviceReqVO;
import tech.qiantong.qknow.module.dm.util.IotClientUtils;

import java.io.UnsupportedEncodingException;

/**
 * 物联网设备信息
 *
 * @author shaun
 * @date 2025/03/12 14:40
 **/
@Tag(name = "物联网设备")
@RestController
@RequestMapping("/dm/device")
public class DmIotDeviceController {

    @Operation(summary = "获取设备多个指定的属性值")
    @Log(title = "物联网设备数据", businessType = BusinessType.OTHER)
    @PostMapping("/getDeviceData")
    public CommonResult<String> getDeviceData(@RequestBody DmDeviceReqVO dmDeviceReqVO) throws UnsupportedEncodingException {
        String deviceData = IotClientUtils.getDeviceData(dmDeviceReqVO.getDeviceName(), dmDeviceReqVO.getProductKey(), dmDeviceReqVO.getList());
        return CommonResult.success(deviceData);
    }

    @Operation(summary = "获取设备属性值")
    @Log(title = "物联网设备数据", businessType = BusinessType.OTHER)
    @PostMapping("/getDeviceProperty")
    public CommonResult<String> getDeviceProperty(String productKey) throws UnsupportedEncodingException {
        String property = IotClientUtils.getDeviceProperty(productKey);
        return CommonResult.success(property);
    }
}
