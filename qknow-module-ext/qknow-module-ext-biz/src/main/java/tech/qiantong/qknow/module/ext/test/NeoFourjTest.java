package tech.qiantong.qknow.module.ext.test;//package tech.qiantong.qknow.module.qknow.knowledge.test;
//
//import org.apache.commons.compress.utils.*;
//import org.junit.*;
//import org.junit.runner.*;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.boot.test.context.*;
//import org.springframework.test.context.junit4.*;
//import tech.qiantong.monkey.common.utils.*;
//import tech.qiantong.monkey.module.system.*;
//import tech.qiantong.monkey.module.system.know.service.*;
//import tech.qiantong.monkey.module.system.neofourj.domain.*;
//import tech.qiantong.monkey.module.system.neofourj.service.*;
//
//import java.util.*;
//
///**
// * @author 鑸掓湀閼�
// * @date 2023/10/07 15:53
// **/
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = MonkeySystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class NeoFourjTest {
//
//    @Autowired
//    private ProgramLanguageService programLanguageService;
//
//    @Autowired
//    private UpdateESFileService updateESFileService;
//
//    @Test
//    public void putData() {
//        putOneNode("花果山水库", "水库");
//        // 流域概况
//        putOneNode("南小河沟流域", "流域");
//        putOneLink("花果山水库", "南小河沟流域", "所在流域");
//        putOneNode("甘肃省庆阳市", "行政区划");
//        putOneLink("南小河沟流域", "甘肃省庆阳市", "所在位置");
//        putOneNode("黄土高塬沟壑区", "流域类型");
//        putOneLink("南小河沟流域", "黄土高塬沟壑区", "流域类型");
//        putOneNode("38.93 平方千米", "面积");
//        putOneLink("南小河沟流域", "38.93 平方千米", "流域面积");
//        putOneNode("15.80 平方千米", "面积");
//        putOneLink("南小河沟流域", "15.80 平方千米", "沟壑面积");
//        putOneNode("13.60 千米", "长度");
//        putOneLink("南小河沟流域", "13.60 千米", "流域总长度");
//        putOneNode("11.70 千米", "长度");
//        putOneLink("南小河沟流域", "11.70 千米", "主沟长");
//        putOneNode("3.40 千米", "宽度");
//        putOneLink("南小河沟流域", "3.40 千米", "平均宽度");
//        putOneNode("183 条", "数量");
//        putOneLink("南小河沟流域", "183 条", "毛沟数量");
//        putOneNode("40.50 %", "面积");
//        putOneLink("南小河沟流域", "40.50 %", "占流域总面积");
//        putOneNode("561.5 mm", "降雨量");
//        putOneLink("南小河沟流域", "561.5 mm", "多年平均降雨量");
//        putOneNode("南小河沟流域位于甘肃省庆阳市西峰区后官寨镇境内，是泾河支流蒲河左岸的一条支沟，属典型的黄土高塬沟壑区，流域面积38.93平方千米，其中沟壑面积15.80平方千米，占流域总面积的40.50%。流域地处中纬度，属于温带大陆性气候，，暴雨集中在7、8、9三个月。", "流域概况");
//        putOneLink("南小河沟流域", "南小河沟流域位于甘肃省庆阳市西峰区后官寨镇境内，是泾河支流蒲河左岸的一条支沟，属典型的黄土高塬沟壑区，流域面积38.93平方千米，其中沟壑面积15.80平方千米，占流域总面积的40.50%。流域地处中纬度，属于温带大陆性气候，，暴雨集中在7、8、9三个月。", "流域概况");
//
//        // 水库简介
//        // putOneNode("花果山水库", "水库"); 如已创建无需重复创建
//        putOneNode("107.3332", "经度");
//        putOneLink("花果山水库", "107.3332", "东经");
//        putOneNode("35.4217", "纬度");
//        putOneLink("花果山水库", "35.4217", "北纬");
//        putOneNode("26.7 平方千米", "面积");
//        putOneLink("花果山水库", "26.7 平方千米", "控制流域面积");
//        putOneNode("小（1）型", "工程规模");
//        putOneLink("花果山水库", "小（1）型", "工程规模");
//        putOneNode("Ⅳ等", "工程等别");
//        putOneLink("花果山水库", "Ⅳ等", "工程等别");
//        putOneNode("45.90 米", "高度");
//        putOneLink("花果山水库", "45.90 米", "坝高");
//        putOneNode("1197.30 米", "高度");
//        putOneLink("花果山水库", "1197.30 米", "坝顶高程");
//        putOneNode("205 米", "长度");
//        putOneLink("花果山水库", "205 米", "坝顶长");
//        putOneNode("5 米", "宽度");
//        putOneLink("花果山水库", "5 米", "坝顶宽");
//        putOneNode("1.10 米", "高度");
//        putOneLink("花果山水库", "1.10 米", "防浪墙高");
//        putOneNode("1960 年", "始建于");
//        putOneLink("花果山水库", "1960 年", "始建于");
//        putOneNode("6 次", "加高加固");
//        putOneLink("花果山水库", "6 次", "加高加固");
//
//        // 花果山水库工程特性表
//        // putOneNode("花果山水库", "水库"); 如已创建无需重复创建
//        putOneNode("313m³/s", "流量");
//        putOneLink("花果山水库", "313m³/s", "设计洪水标准及洪峰流量");
//        putOneNode("521m³/s", "流量");
//        putOneLink("花果山水库", "521m³/s", "校核洪水标准及洪峰流量");
//        putOneNode("154万 m³", "洪量");
//        putOneLink("花果山水库", "154万 m³", "设计洪量");
//        putOneNode("269万 m³", "洪量");
//        putOneLink("花果山水库", "269万 m³", "校核洪量");
//        putOneNode("1197.30m", "水位");
//        putOneLink("花果山水库", "1197.30m", "校核洪水位");
//        putOneNode("1194.79m", "水位");
//        putOneLink("花果山水库", "1194.79m", "设计洪水位");
//        putOneNode("1192.80m", "水位");
//        putOneLink("花果山水库", "1192.80m", "正常水位");
//        putOneNode("1183.40m", "水位");
//        putOneLink("花果山水库", "1183.40m", "死水位");
//        putOneNode("1187.00m", "水位");
//        putOneLink("花果山水库", "1187.00m", "汛限水位");
//        putOneNode("524.00万 m³", "库容");
//        putOneLink("花果山水库", "524.00万 m³", "总库容");
//        putOneNode("235.54万 m³", "库容");
//        putOneLink("花果山水库", "235.54万 m³", "防洪库容");
//        putOneNode("6.00万 m³", "库容");
//        putOneLink("花果山水库", "6.00万 m³", "兴利库容");
//        putOneNode("282.46万 m³", "库容");
//        putOneLink("花果山水库", "282.46万 m³", "淤积库容");
//        putOneNode("均质土坝", "坝型");
//        putOneLink("花果山水库", "均质土坝", "坝型");
//        putOneNode("1座", "数量");
//        putOneLink("花果山水库", "1座", "输水隧洞");
//        putOneNode("370m", "长度");
//        putOneLink("花果山水库", "370m", "输水管线");
//        putOneNode("1m³/s", "流量");
//        putOneLink("花果山水库", "1m³/s", "最大泄流量");
//        putOneNode("1197.3m", "坝顶高程");
//        putOneLink("花果山水库", "1197.3m", "坝顶高程");
//
//
//        putOneNode("花果山水库进口明渠段", "溢洪道");
//        putOneLink("花果山水库", "花果山水库进口明渠段", "溢洪道");
//        putOneNode("C25 混凝土底板及边坡防护型", "溢洪道类型");
//        putOneLink("花果山水库进口明渠段", "C25 混凝土底板及边坡防护型", "溢洪道类型");
//        putOneNode("0.5m", "数量");
//        putOneLink("花果山水库进口明渠段", "0.5m", "板厚");
//        putOneNode("1.5m", "数量");
//        putOneLink("花果山水库进口明渠段", "1.5m", "底宽");
//        putOneNode("直角", "角度");
//        putOneLink("花果山水库进口明渠段", "直角", "边坡比");
//        putOneNode("1190.80m", "高度");
//        putOneLink("花果山水库进口明渠段", "1190.80m", "底板高程");
//
//        putOneNode("花果山水库溢流堰段", "溢洪道");
//        putOneLink("花果山水库", "花果山水库溢流堰段", "溢洪道");
//        putOneNode("无闸门控制宽顶式溢流堰", "溢洪道类型");
//        putOneLink("花果山水库溢流堰段", "无闸门控制宽顶式溢流堰", "溢洪道类型");
//        putOneNode("1192.80m", "高度");
//        putOneLink("花果山水库溢流堰段", "1192.80m", "堰顶高程");
//        putOneNode("1.5m ", "宽度");
//        putOneLink("花果山水库溢流堰段", "1.5m ", "堰底宽");
//        putOneNode("0.3m", "宽度");
//        putOneLink("花果山水库溢流堰段", "0.3m", "两侧边坡厚");
//        putOneNode("1:0.3", "比例");
//        putOneLink("花果山水库溢流堰段", "1:0.3", "边坡");
//
//        putOneNode("173.5m", "长度");
//        putOneLink("花果山水库", "173.5m", "陡槽段");
//
//        putOneNode("1座 ", "数量");
//        putOneLink("花果山水库", "1座", "消力池");
//
//        putOneNode("35m", "长度");
//        putOneLink("花果山水库", "35m", "出口段");
//
//        putOneNode("1190.80m", "溢洪道高程");
//        putOneLink("花果山水库", "1190.80m", "溢洪道高程");
//
//        putOneNode("花果山水库始建于1960年，前后经过6次加高加固，形成坝高45.9米，坝长205米，总库容为524.0万立方米的小（1）型水库，流域控制面积26.7km²。水库主要由大坝、溢洪道及放水建筑物三部分组成。", "描述");
//        putOneLink("花果山水库", "花果山水库始建于1960年，前后经过6次加高加固，形成坝高45.9米，坝长205米，总库容为524.0万立方米的小（1）型水库，流域控制面积26.7km²。水库主要由大坝、溢洪道及放水建筑物三部分组成。", "描述");
//
//        putOneNode("黄河水土保持西峰治理监督局", "水库运行管护主体");
//        putOneLink("花果山水库", "黄河水土保持西峰治理监督局", "水库运行管护主体");
//
//        putOneNode("花果山水库汇流", "汇流");
//        putOneLink("花果山水库", "花果山水库汇流", "汇流");
//        putOneNode("水库上游包括南小河沟主沟及冰草沟、高家沟两条支沟，上游做有沟头防护，库区无沟道径流汇入，水库主要水源为降雨。", "水库上游");
//        putOneLink("花果山水库汇流", "水库上游包括南小河沟主沟及冰草沟、高家沟两条支沟，上游做有沟头防护，库区无沟道径流汇入，水库主要水源为降雨。", "水库上游");
//        putOneNode("水库下游包括董庄沟、杨家沟等在内的多条支毛沟，水库下泄主河道为南小河沟主沟，经十八亩台淤地坝流入下游4公里处的南小河沟水库(库容115万m³)。", "水库下游");
//        putOneLink("花果山水库汇流", "水库下游包括董庄沟、杨家沟等在内的多条支毛沟，水库下泄主河道为南小河沟主沟，经十八亩台淤地坝流入下游4公里处的南小河沟水库(库容115万m³)。", "水库下游");
//
//        //所拥有的地坝
//        putOneNode("地坝", "地坝");
//        putOneNode("十八亩台淤地坝", "地坝");
//        putOneLink("地坝", "十八亩台淤地坝", "包含");
//        putOneNode("HP0206210020000001", "编码");
//        putOneLink("十八亩台淤地坝", "HP0206210020000001", "编码");
//        putOneNode("107.5399833", "经度");
//        putOneLink("十八亩台淤地坝", "107.5399833", "经度");
//        putOneNode("35.6998944", "纬度");
//        putOneLink("十八亩台淤地坝", "35.6998944", "纬度");
//        putOneNode("6.08", "控制面积（km2）");
//        putOneLink("十八亩台淤地坝", "6.08", "控制面积");
//        putOneNode("25", "坝高（m）");
//        putOneLink("十八亩台淤地坝", "25", "坝高");
//        putOneNode("5", "坝顶宽（m）");
//        putOneLink("十八亩台淤地坝", "5", "坝顶宽");
//        putOneNode("112", "坝顶长（m）");
//        putOneLink("十八亩台淤地坝", "112", "坝顶长");
//        putOneNode("115", "总库容（万m3）");
//        putOneLink("十八亩台淤地坝", "115", "总库容");
//        putOneNode("62.33", "拦泥库容（万m3）");
//        putOneLink("十八亩台淤地坝", "62.33", "拦泥库容");
//        putOneNode("十八亩台骨干坝位于甘肃省庆阳市西峰区后官寨乡境内的泾河流域蒲河支流南小河沟内", "描述");
//        putOneLink("十八亩台淤地坝", "十八亩台骨干坝位于甘肃省庆阳市西峰区后官寨乡境内的泾河流域蒲河支流南小河沟内，坝址位于东经107.40.，北纬35.41.，距庆阳市13km。该工程始建于1953年，是一座以拦泥淤地、防治水土流失为主，兼有防洪滞洪功能的骨干坝工程。坝址以上控制流域面积32.78km2，其中花果山水库至十八亩台骨干坝区间面积为6.08km2。除张塔山骨干坝坝控面积2km2外，十八亩台骨干坝实际防洪面积为4.08km2。十八亩台骨干坝总库容115万m3，防洪库容52.67万m3。", "描述");
//
//        putOneNode("范家沟淤地坝", "地坝");
//        putOneLink("地坝", "范家沟淤地坝", "包含");
//        putOneNode("HP0206210020000002", "编码");
//        putOneLink("范家沟淤地坝", "HP0206210020000002", "编码");
//        putOneNode("107.5512", "经度");
//        putOneLink("范家沟淤地坝", "107.5512", "经度");
//        putOneNode("35.7039139", "纬度");
//        putOneLink("范家沟淤地坝", "35.7039139", "纬度");
//        putOneNode("0.3", "控制面积（km2）");
//        putOneLink("范家沟淤地坝", "0.3", "控制面积");
//        putOneNode("9", "坝高（m）");
//        putOneLink("范家沟淤地坝", "9", "坝高");
//        putOneNode("6", "坝顶宽（m）");
//        putOneLink("范家沟淤地坝", "6", "坝顶宽");
//        putOneNode("61", "坝顶长（m）");
//        putOneLink("范家沟淤地坝", "61", "坝顶长");
//        putOneNode("5.7", "总库容（万m3）");
//        putOneLink("范家沟淤地坝", "5.7", "总库容");
//        putOneNode("2.3", "拦泥库容（万m3）");
//        putOneLink("范家沟淤地坝", "2.3", "拦泥库容");
//        putOneNode("范家沟小型淤地坝，其组成为一大件均质土坝，位于西峰局下属的南小河沟试验场场区，土地产权为国有。建设资金来源全部为国家投资。范家沟淤地坝始建于2007年，对拦蓄沟道泥沙、保护下游科研及生产基地安全和场内交通畅通，起到了重要作用。", "描述");
//        putOneLink("范家沟淤地坝", "范家沟小型淤地坝，其组成为一大件均质土坝，位于西峰局下属的南小河沟试验场场区，土地产权为国有。建设资金来源全部为国家投资。范家沟淤地坝始建于2007年，对拦蓄沟道泥沙、保护下游科研及生产基地安全和场内交通畅通，起到了重要作用。", "描述");
//
//
//        putOneNode("张塔山淤地坝", "地坝");
//        putOneLink("地坝", "张塔山淤地坝", "包含");
//        putOneNode("HP0206210020000003", "编码");
//        putOneLink("张塔山淤地坝", "HP0206210020000003", "编码");
//        putOneNode("107.5560833", "经度");
//        putOneLink("张塔山淤地坝", "107.5560833", "经度");
//        putOneNode("35.7056194", "纬度");
//        putOneLink("张塔山淤地坝", "35.7056194", "纬度");
//        putOneNode("3.1", "控制面积（km2）");
//        putOneLink("张塔山淤地坝", "3.1", "控制面积");
//        putOneNode("20.2", "坝高（m）");
//        putOneLink("张塔山淤地坝", "20.2", "坝高");
//        putOneNode("5", "坝顶宽（m）");
//        putOneLink("张塔山淤地坝", "5", "坝顶宽");
//        putOneNode("71.2", "坝顶长（m）");
//        putOneLink("张塔山淤地坝", "71.2", "坝顶长");
//        putOneNode("34.8", "总库容（万m3）");
//        putOneLink("张塔山淤地坝", "34.8", "总库容");
//        putOneNode("7.4", "拦泥库容（万m3）");
//        putOneLink("张塔山淤地坝", "7.4", "拦泥库容");
//        putOneNode("张塔山沟中型淤地坝隶属于黄河水土保持西峰治理监督局（以下简称西峰局）的直管淤地坝，位于南小河沟右岸张塔山支毛沟，始建于上世纪七十年代初，由人工夯筑而成，距下游沟口约五十米。该坝建成后，对防止张塔山沟小流域水土流失、保护下游十八亩台300多亩科研及生产基地安全起到了重要作用。到1996年该坝基本淤满，失去了调洪能力，随即对其进行首次加高。经过近10年运行，库内大部分库容被泥沙淤积,所剩库容极为有限，不能满足拦蓄流域的水沙需要。因此，2007年又对坝体进行了二次加高，并新增了放水工程，提高了工程拦蓄能力和安全性。", "描述");
//        putOneLink("张塔山淤地坝", "张塔山沟中型淤地坝隶属于黄河水土保持西峰治理监督局（以下简称西峰局）的直管淤地坝，位于南小河沟右岸张塔山支毛沟，始建于上世纪七十年代初，由人工夯筑而成，距下游沟口约五十米。该坝建成后，对防止张塔山沟小流域水土流失、保护下游十八亩台300多亩科研及生产基地安全起到了重要作用。到1996年该坝基本淤满，失去了调洪能力，随即对其进行首次加高。经过近10年运行，库内大部分库容被泥沙淤积,所剩库容极为有限，不能满足拦蓄流域的水沙需要。因此，2007年又对坝体进行了二次加高，并新增了放水工程，提高了工程拦蓄能力和安全性。", "描述");
//
//        putOneNode("竹儿沟淤地坝", "地坝");
//        putOneLink("地坝", "竹儿沟淤地坝", "包含");
//        putOneNode("HP0206210020000004", "编码");
//        putOneLink("竹儿沟淤地坝", "HP0206210020000004", "编码");
//        putOneNode("107.5629556", "经度");
//        putOneLink("竹儿沟淤地坝", "107.5629556", "经度");
//        putOneNode("35.706325", "纬度");
//        putOneLink("竹儿沟淤地坝", "35.706325", "纬度");
//        putOneNode("0.3", "控制面积（km2）");
//        putOneLink("竹儿沟淤地坝", "0.3", "控制面积");
//        putOneNode("16.2", "坝高（m）");
//        putOneLink("竹儿沟淤地坝", "16.2", "坝高");
//        putOneNode("6", "坝顶宽（m）");
//        putOneLink("竹儿沟淤地坝", "6", "坝顶宽");
//        putOneNode("88", "坝顶长（m）");
//        putOneLink("竹儿沟淤地坝", "88", "坝顶长");
//        putOneNode("6.44", "总库容（万m3）");
//        putOneLink("竹儿沟淤地坝", "6.44", "总库容");
//        putOneNode("1.5", "拦泥库容（万m3）");
//        putOneLink("竹儿沟淤地坝", "1.5", "拦泥库容");
//        putOneNode("竹儿沟小型淤地坝，其组成为一大件均质土坝，位于西峰局下属的南小河沟试验场场区，土地产权为国有。建设资金来源全部为国家投资。竹儿沟淤地坝始建于1978年，对拦蓄沟道泥沙、保护下游科研及生产基地安全和场内交通畅通，起到了重要作用。竹儿沟土坝以坝代路，作为南小河沟试验场区整个道路系统的组成部分，2011年坝面被硬化。", "描述");
//        putOneLink("竹儿沟淤地坝", "竹儿沟小型淤地坝，其组成为一大件均质土坝，位于西峰局下属的南小河沟试验场场区，土地产权为国有。建设资金来源全部为国家投资。竹儿沟淤地坝始建于1978年，对拦蓄沟道泥沙、保护下游科研及生产基地安全和场内交通畅通，起到了重要作用。竹儿沟土坝以坝代路，作为南小河沟试验场区整个道路系统的组成部分，2011年坝面被硬化。", "描述");
//
//
//        putOneNode("老官山东沟淤地坝", "地坝");
//        putOneLink("地坝", "老官山东沟淤地坝", "包含");
//        putOneNode("HP0206210020000005", "编码");
//        putOneLink("老官山东沟淤地坝", "HP0206210020000005", "编码");
//        putOneNode("107.5469833", "经度");
//        putOneLink("老官山东沟淤地坝", "107.5469833", "经度");
//        putOneNode("35.7045833", "纬度");
//        putOneLink("老官山东沟淤地坝", "35.7045833", "纬度");
//        putOneNode("0.16", "控制面积（km2）");
//        putOneLink("老官山东沟淤地坝", "0.16", "控制面积");
//        putOneNode("11.2", "坝高（m）");
//        putOneLink("老官山东沟淤地坝", "11.2", "坝高");
//        putOneNode("4", "坝顶宽（m）");
//        putOneLink("老官山东沟淤地坝", "4", "坝顶宽");
//        putOneNode("42.6", "坝顶长（m）");
//        putOneLink("老官山东沟淤地坝", "42.6", "坝顶长");
//        putOneNode("1.95", "总库容（万m3）");
//        putOneLink("老官山东沟淤地坝", "1.95", "总库容");
//        putOneNode("0.58", "拦泥库容（万m3）");
//        putOneLink("老官山东沟淤地坝", "0.58", "拦泥库容");
//        putOneNode("老官山东沟淤地坝建于20世纪八十年中期,主要用于水土保持科学试验观测。坝址地理坐标：东经107.32.49.9,北纬35.42.12.11，距老官山东沟沟口约100m，仅有土坝，无放水建筑物及泄洪设施，坝址断面控制面积0.16km2，主沟道长650m，主沟道平均比降4.5%，原工程建设标准较低，设计文件丢失。现场测量总坝高6m，坝顶宽仅0.5m，坝坡坡比接近1:1，总库容为0.25万m3，现状淤积面已接近坝顶。", "描述");
//        putOneLink("老官山东沟淤地坝", "老官山东沟淤地坝建于20世纪八十年中期,主要用于水土保持科学试验观测。坝址地理坐标：东经107.32.49.9,北纬35.42.12.11，距老官山东沟沟口约100m，仅有土坝，无放水建筑物及泄洪设施，坝址断面控制面积0.16km2，主沟道长650m，主沟道平均比降4.5%，原工程建设标准较低，设计文件丢失。现场测量总坝高6m，坝顶宽仅0.5m，坝坡坡比接近1:1，总库容为0.25万m3，现状淤积面已接近坝顶。", "描述");
//
//
//        putOneNode("老官山西沟淤地坝", "地坝");
//        putOneLink("地坝", "老官山西沟淤地坝", "包含");
//        putOneNode("HP0206210020000006", "编码");
//        putOneLink("老官山西沟淤地坝", "HP0206210020000006", "编码");
//        putOneNode("107.5427556", "经度");
//        putOneLink("老官山西沟淤地坝", "107.5427556", "经度");
//        putOneNode("35.7028861", "纬度");
//        putOneLink("老官山西沟淤地坝", "35.7028861", "纬度");
//        putOneNode("0.13", "控制面积（km2）");
//        putOneLink("老官山西沟淤地坝", "0.13", "控制面积");
//        putOneNode("8", "坝高（m）");
//        putOneLink("老官山西沟淤地坝", "8", "坝高");
//        putOneNode("4", "坝顶宽（m）");
//        putOneLink("老官山西沟淤地坝", "4", "坝顶宽");
//        putOneNode("32.06", "坝顶长（m）");
//        putOneLink("老官山西沟淤地坝", "32.06", "坝顶长");
//        putOneNode("1", "总库容（万m3）");
//        putOneLink("老官山西沟淤地坝", "1", "总库容");
//        putOneNode("0.5", "拦泥库容（万m3）");
//        putOneLink("老官山西沟淤地坝", "0.5", "拦泥库容");
//        putOneNode("老官山西沟淤地坝建于20世纪八十年中期，主要用于水土保持科学试验观测。坝址位于南小河沟右岸支沟老官山西沟沟口，地理坐标为东经107.32′35，北纬35.42′09，地处庆阳市西峰区后官寨乡境内，属泾河流域蒲河水系。老官山西沟淤地坝坝址距沟口100m，于1985年建成，为黄土均质坝，控制面积0.13km2，仅有土坝“一大件”，由于年代久远，原设计文件遗失，设计标准不详。现场测量总坝高6m，坝顶宽0.5m，坝顶长20.5m，迎水坡坡比1:1，背水坡坡比1:1。总库容为0.5万m3，现已淤满。", "描述");
//        putOneLink("老官山西沟淤地坝", "老官山西沟淤地坝建于20世纪八十年中期，主要用于水土保持科学试验观测。坝址位于南小河沟右岸支沟老官山西沟沟口，地理坐标为东经107.32′35，北纬35.42′09，地处庆阳市西峰区后官寨乡境内，属泾河流域蒲河水系。老官山西沟淤地坝坝址距沟口100m，于1985年建成，为黄土均质坝，控制面积0.13km2，仅有土坝“一大件”，由于年代久远，原设计文件遗失，设计标准不详。现场测量总坝高6m，坝顶宽0.5m，坝顶长20.5m，迎水坡坡比1:1，背水坡坡比1:1。总库容为0.5万m3，现已淤满。", "描述");
//
//
//        putOneNode("湫沟淤地坝", "地坝");
//        putOneLink("地坝", "湫沟淤地坝", "包含");
//        putOneNode("HP0206210020000007", "编码");
//        putOneLink("湫沟淤地坝", "HP0206210020000007", "编码");
//        putOneNode("107.56495", "经度");
//        putOneLink("湫沟淤地坝", "107.56495", "经度");
//        putOneNode("35.7020944", "纬度");
//        putOneLink("湫沟淤地坝", "35.7020944", "纬度");
//
//        putOneNode("湫沟新材料淤地坝", "地坝");
//        putOneLink("地坝", "湫沟新材料淤地坝", "包含");
//        putOneNode("HP0206210020000008", "编码");
//        putOneLink("湫沟新材料淤地坝", "HP0206210020000008", "编码");
//        putOneNode("107.5666472", "经度");
//        putOneLink("湫沟新材料淤地坝", "107.5666472", "经度");
//        putOneNode("35.6987889", "纬度");
//        putOneLink("湫沟新材料淤地坝", "35.6987889", "纬度");
//
//        putOneNode("岘子沟淤地坝", "地坝");
//        putOneLink("地坝", "岘子沟淤地坝", "包含");
//        putOneNode("HP0206210020000009", "编码");
//        putOneLink("岘子沟淤地坝", "HP0206210020000009", "编码");
//        putOneNode("107.5406444", "经度");
//        putOneLink("岘子沟淤地坝", "107.5406444", "经度");
//        putOneNode("35.7012444", "纬度");
//        putOneLink("岘子沟淤地坝", "35.7012444", "纬度");
//    }
//
//    public void putOneNode(String nodeName, String nodeType) {
//        ProgramLanguage programLanguage = new ProgramLanguage();
//        programLanguage.setName(nodeName);
//        programLanguage.setType(nodeType);
////        programLanguage.setRemark("");
//        List<Map<String, Object>> eSearch = updateESFileService.eSearch(programLanguage.getName());
//        List<String> esIdList = Lists.newArrayList();
//        for (Map<String, Object> searchMap : eSearch) {
//            esIdList.add(searchMap.get("esId").toString());
//        }
//        programLanguage.setESRetrievalIdList(esIdList);
//        List<ProgramLanguage> byIdProgramLanguage = programLanguageService.getByIdProgramLanguage(programLanguage.getName(), null);
//        if (ListUtil.isNotEmpty(byIdProgramLanguage)){
//            programLanguageService.setProgramLanguage(programLanguage,null);
//            System.out.println("有这个节点");
//        }else {
//            programLanguageService.createProgramLanguage(programLanguage, "Nami");
//            System.out.println("没有这个节点");
//        }
//    }
//
//    public void putOneLink(String source, String target, String field) {
//        Links links = new Links();
//        links.setSource(source);
//        links.setTarget(target);
//        links.setFields(field);
//        if (!programLanguageService.selectRelationship(links, null)) {
//            programLanguageService.createRelationship(links, "Nami");
//            System.out.println("没有这个关系");
//        } else {
//            System.out.println("有这个关系");
//        }
//    }
//}
