package tech.qiantong.qknow.module.ext.api.extDatasource.dto;

import lombok.*;

/**
 * 数据源 DTO 对象 ext_datasource
 *
 * @author qknow
 * @date 2025-02-25
 */
@Data
public class ExtDatasourceRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据库连接名称 */
    private String name;

    /** 数据库类型 */
    private Integer type;

    /** 数据库地址 */
    private String host;

    /** 端口号 */
    private Long port;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 连接状态 */
    private Integer status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
