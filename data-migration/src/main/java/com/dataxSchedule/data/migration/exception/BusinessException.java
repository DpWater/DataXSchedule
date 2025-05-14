package com.dataxSchedule.data.migration.exception;

import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class BusinessException extends RuntimeException{
    private Integer code;
    public BusinessException(Integer code,String msg){
        super(msg);
        this.code = code;
    }
    public BusinessException(String msg){
        super(msg);
    }
}
