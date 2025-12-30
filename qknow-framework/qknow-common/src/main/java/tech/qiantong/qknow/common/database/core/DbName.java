package tech.qiantong.qknow.common.database.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DbName {

    /**
     * 列名
     */
    private String dbName;

    /**
     * 当前层级
     */
    private int level;

    /**
     * 总层级
     */
    private int totalLevels;


    private List<DbName> children;
}
