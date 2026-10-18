package com.itheima.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public PageResult<Emp> page(Integer page, Integer pageSize,
                                String name, Integer gender,
                                LocalDate begin, LocalDate end) {
//        //0.参数兜底：页码从1开始，每页条数默认10，避免出现负数起始索引
//        if (page == null || page < 1) {
//            page = 1;
//        }
//        if (pageSize == null || pageSize < 1) {
//            pageSize = 10;
//        }

        /* ============ 方式一：手工分页(原写法，已注释，仅作对比) ============
        //1.调用Mapper接口，查询总记录数
        Long total = empMapper.count();

        //2.调用Mapper接口，查询结果列表
        Integer start = (page - 1) * pageSize; //起始索引 = (页码 - 1) * 每页记录数
        List<Emp> rows = empMapper.list(start, pageSize);

        //3.封装结果PageResult
        return new PageResult<Emp>(total, rows);
        =================================================================== */

        /* ============ 方式二：PageHelper 分页(现用写法) ============ */
        //1.设置分页参数(PageHelper 只对它后面紧邻的第一条查询生效)
        PageHelper.startPage(page, pageSize);

        //2.调用Mapper接口，按条件查询结果列表
        //无需再传起始索引：插件会自动追加 limit，并自动执行一条 count 查询统计总记录数
        //条件为 null 或空串时，Mapper 里的 <if> 会自动跳过该条件
        List<Emp> rows = empMapper.list(name, gender, begin, end);

        //3.封装结果PageResult(PageInfo 中包含总记录数 total 和当前页数据 list)
        PageInfo<Emp> pageInfo = new PageInfo<>(rows);
        return new PageResult<Emp>(pageInfo.getTotal(), pageInfo.getList());
    }
}
