package tech.qiantong.qknow.common.enums;

/**
 * 知识文件跟踪
 * @author jw
 * @date 2025/06/26 16:54
 * @return
 */
public enum DocumentTrackEnum {
    PREVIEW(0, "preview"),
    DOWNLOAD(1, "download"),
    ;

    public final Integer code;
    public final String info;

    DocumentTrackEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static DocumentTrackEnum get(Integer code) {
        for (DocumentTrackEnum v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(Integer code) {
        return this.code.equals(code);
    }

    public static String getInfo(Integer code) {
        return DocumentTrackEnum.get(code).getInfo();
    }

    public static Integer getCode(String info) {
        for (DocumentTrackEnum v : values()) {
            if (v.getInfo().equals(info)) {
                return v.getCode();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
