package tech.qiantong.qknow.module.ext.extEnum;

import lombok.Getter;

/**
 * 抽取任务类型
 */
@Getter
public enum ExtractType {

    STRUCTURED(1),//结构化
    UNSTRUCTURED(2);//非结构化

    private final Integer type;

    // 构造方法
    private ExtractType(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return this.type;
    }
}
