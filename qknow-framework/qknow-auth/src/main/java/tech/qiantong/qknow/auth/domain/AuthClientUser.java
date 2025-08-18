package tech.qiantong.qknow.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import tech.qiantong.qknow.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 应用和用户关联对象 auth_client_user
 *
 * @author qknow
 * @date 2024-08-31
 */
@Data
@TableName(value = "auth_client_user")
public class AuthClientUser extends BaseEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 应用ID */
    @Excel(name = "应用ID")
    @TableId(type = IdType.AUTO)
    private Long clientId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** open_id */
    @Excel(name = "open_id")
    private String openId;


}
