package tech.qiantong.module.dm;

import org.assertj.core.util.Lists;
import tech.qiantong.qknow.module.dm.util.IotClientUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 测试物联网平台数据对接
 *
 * @author shaun
 * @date 2025/03/11 11:33
 **/
public class IotDataReciveTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String attr1 = "1HTLH_WCWD";
        String attr2 = "1HTLH_PJWD";
        String attr3 = "1HTLH_ZXWD";
        String attr4 = "1HTLH_ZDWD";
        ArrayList<String> list = Lists.newArrayList();
        list.add(attr1);
        list.add(attr2);
        list.add(attr3);
        list.add(attr4);
        String deviceData = IotClientUtils.getDeviceData("BYZ_1HTLH", "a1akL5wLmcP", list);
        System.out.println(deviceData);

//        String property = IotClientUtils.getDeviceProperty("a1akL5wLmcP");
//        System.out.println(property);
    }
}
