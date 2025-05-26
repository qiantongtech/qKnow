package tech.qiantong.qknow.common.exception.user;

import tech.qiantong.qknow.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author qknow
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
