package com.itheima.service;


import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;

public interface EmpService {

    /**
     * 分页查询员工数据
     * @param page 页码(从1开始)
     * @param pageSize 每页记录数
     * @return 总记录数 + 当前页的员工数据列表
     */
    PageResult<Emp> page(Integer page, Integer pageSize);
}
