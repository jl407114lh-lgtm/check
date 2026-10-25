package com.itheima.exception;

/**
 * 业务异常
 * 用于在 Service 层主动抛出"可以给用户看"的错误提示，
 * 例如：员工不存在、状态不允许操作等。
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
