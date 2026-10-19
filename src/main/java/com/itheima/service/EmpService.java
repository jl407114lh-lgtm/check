package com.itheima.service;


import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;

public interface EmpService {

    /*
     * ===================== 原写法：多个参数(已注释，仅作对比) =====================
     *
     * 分页查询员工数据(支持条件查询)
     * @param page 页码(从1开始)
     * @param pageSize 每页记录数
     * @param name 员工姓名，模糊查询，可为空
     * @param gender 性别(1:男 2:女)，精确查询，可为空
     * @param begin 入职日期的开始时间，可为空
     * @param end 入职日期的结束时间，可为空
     * @return 总记录数 + 当前页的员工数据列表
     *
     * PageResult<Emp> page(Integer page, Integer pageSize,
     *                      String name, Integer gender,
     *                      LocalDate begin, LocalDate end);
     * ==============================================================================
     */

    /*
     * ===================== 上一版：分页参数单独传(已注释，仅作对比) =====================
     *
     * 分页查询员工数据(支持条件查询)
     * @param page 页码(从1开始)
     * @param pageSize 每页记录数
     * @param param 查询条件(姓名模糊、性别精确、入职日期范围)
     * @return 总记录数 + 当前页的员工数据列表
     *
     * PageResult<Emp> page(Integer page, Integer pageSize, EmpQueryParam param);
     * =============================================================================
     */

    /**
     * 分页查询员工数据(支持条件查询)
     * @param param 查询参数(页码、每页条数、姓名、性别、入职日期范围)
     * @return 总记录数 + 当前页的员工数据列表
     */
    PageResult<Emp> page(EmpQueryParam param);
}
