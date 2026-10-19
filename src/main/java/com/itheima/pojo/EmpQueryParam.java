package com.itheima.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 员工查询条件参数
 * 把分页参数和查询条件全部封装成一个对象，
 * Controller/Service 只接收这一个参数，避免方法参数过多
 */
@Data
public class EmpQueryParam {
    private Integer page = 1;       //页码(从1开始)
    private Integer pageSize = 10;  //每页记录数

    private String name;      //姓名，模糊查询
    private Integer gender;   //性别(1:男 2:女)，精确查询

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;  //入职日期的开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;    //入职日期的结束时间
}
