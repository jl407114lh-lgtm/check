package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

public interface DeptService {
    /**
     * 查询所有部门信息
     * @return
     */

    List<Dept> findAll();

    void delete(Integer id);

    void add(Dept dept);

    Dept findById(Integer id);

    void update(Dept dept);
}
