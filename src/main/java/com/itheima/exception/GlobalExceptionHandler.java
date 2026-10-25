package com.itheima.exception;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 * 统一把异常转换成 Result 返回给前端（HTTP 200 + code=0），
 * 前端根据 code 判断失败，并把 msg 提示给用户。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常：直接把异常里的提示返回给前端
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 唯一键冲突：emp.username、emp.phone 都有唯一索引
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKey(DuplicateKeyException e) {
        log.warn("唯一键冲突: {}", e.getMessage());
        return Result.error("数据已存在：用户名或手机号不能重复");
    }

    /**
     * 其他数据完整性异常：字段超长、非空约束等
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrity(DataIntegrityViolationException e) {
        log.warn("数据完整性异常: {}", e.getMessage());
        String cause = e.getMostSpecificCause().getMessage();
        if (cause != null && cause.contains("Data too long")) {
            return Result.error("输入内容过长，请检查后重试");
        }
        return Result.error("数据不符合要求，请检查输入内容");
    }

    /**
     * 上传文件超过大小限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSize(MaxUploadSizeExceededException e) {
        log.warn("上传文件过大: {}", e.getMessage());
        return Result.error("上传文件过大，单个文件不能超过 10MB");
    }

    /**
     * 参数校验失败（@Valid / @Validated）
     */
    @ExceptionHandler(BindException.class)
    public Result handleValidation(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null ? "请求参数校验失败" : fieldError.getDefaultMessage();
        log.warn("参数校验失败: {}", message);
        return Result.error(message);
    }

    /**
     * 请求体不是合法 JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败: {}", e.getMessage());
        return Result.error("请求参数格式不正确");
    }

    /**
     * 缺少必填的请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParameter(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数: {}", e.getParameterName());
        return Result.error("缺少必要的请求参数：" + e.getParameterName());
    }

    /**
     * 参数类型不匹配，例如 /emps/abc 的 id 不是数字
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {}", e.getName());
        return Result.error("参数类型不正确：" + e.getName());
    }

    /**
     * 请求的资源不存在
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result handleNoResource(NoResourceFoundException e) {
        log.warn("资源不存在: {}", e.getResourcePath());
        return Result.error("请求的资源不存在");
    }

    /**
     * 兜底处理：没被上面规则匹配到的异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统繁忙，请稍后再试");
    }
}
