package tech.qiantong.qknow.common.core.page;

import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页结果
 * @author Ming
 */

@Schema(description = "分页结果")
@Data
public final class PageResult<T> implements Serializable {

    @Schema(description = "数据")
    private List<?> rows;

    @Schema(description = "总量")
    private Long total;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total) {
        this.rows = list;
        this.total = total;
    }

    public PageResult(Long total) {
        this.rows = new ArrayList<>();
        this.total = total;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L);
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(total);
    }


    /**
     * 获取当前页的数据列表，并将其转换为指定类型 T 的列表
     * @return 数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> getList() {
        if (this.rows == null) {
            return new ArrayList<>();
        }
        return (List<T>) this.rows;
    }
}
