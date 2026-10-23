package com.itheima.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    @Autowired
    private EmpLogService empLogService;
    /*
     * ===================== 改造说明 =====================
     * 原来方法有 6 个参数(page、pageSize、name、gender、begin、end)；
     * 现在分页参数和查询条件全部封装进 EmpQueryParam，方法只接收这 1 个参数。
     * 之前的写法都完整保留在下方注释里，方便对比。
     * ===================================================
     */

    @Override
    public PageResult<Emp> page(EmpQueryParam param) {
        //0.分页参数兜底：页码从1开始，每页条数默认10
        Integer page = param.getPage();
        Integer pageSize = param.getPageSize();
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        //1.设置分页参数(PageHelper 只对它后面紧邻的第一条查询生效)
        PageHelper.startPage(page, pageSize);

        //2.调用Mapper接口，按条件查询结果列表
        //条件对象里为 null 或空串的字段，XML 中的 <if> 会自动跳过该条件
        List<Emp> rows = empMapper.list(param);

        //3.封装结果PageResult(PageInfo 中包含总记录数 total 和当前页数据 list)
        PageInfo<Emp> pageInfo = new PageInfo<>(rows);
        return new PageResult<Emp>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional
    public void save(Emp emp) {
        try {
            //1.保存员工基本信息
            LocalDateTime now = LocalDateTime.now();
            emp.setCreateTime(now);
            emp.setUpdateTime(now);
            empMapper.insert(emp);

            //2.保存员工工作经历信息
            List<EmpExpr> exprList = emp.getExprList();
            if(!CollectionUtils.isEmpty(exprList)){
                //遍历集合，为empId赋值
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList);
            }

            //3.记录成功日志
            //EmpLogService 使用 REQUIRES_NEW，会挂起业务事务、独立提交日志
            saveLog("新增员工成功: " + buildEmpInfo(emp));

        } catch (Exception e) {
            //记录失败日志；日志写入失败不能覆盖原来的业务异常
            try {
                saveLog("新增员工失败: " + buildEmpInfo(emp) + ", 原因: " + e.getMessage());
            } catch (Exception logException) {
                log.error("记录员工操作日志失败", logException);
            }
            //继续抛出原异常，让业务事务回滚
            throw e;
        }
    }

    /**
     * 写入员工操作日志
     */
    private void saveLog(String info) {
        //info 字段是 varchar(2000)，超长截断，避免日志写入失败
        String content = info.length() > 2000 ? info.substring(0, 2000) : info;
        empLogService.insertLog(new EmpLog(null, LocalDateTime.now(), content));
    }

    /**
     * 拼接日志中记录的员工关键信息
     */
    private String buildEmpInfo(Emp emp) {
        return "empId=" + emp.getId()
                + ", username=" + emp.getUsername()
                + ", name=" + emp.getName()
                + ", deptId=" + emp.getDeptId()
                + ", entryDate=" + emp.getEntryDate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        //先删子表(工作经历)，再删主表(员工)，两步在同一个事务里
        empExprMapper.deleteByEmpIds(ids);
        empMapper.deleteByIds(ids);
        log.info("删除员工: ids={}", ids);
    }


    /*
     * ===================== 上一版：分页参数单独传(已注释，仅作对比) =====================
     *
     * @Override
     * public PageResult<Emp> page(Integer page, Integer pageSize, EmpQueryParam param) {
     *     PageHelper.startPage(page, pageSize);
     *     List<Emp> rows = empMapper.list(param);
     *     PageInfo<Emp> pageInfo = new PageInfo<>(rows);
     *     return new PageResult<Emp>(pageInfo.getTotal(), pageInfo.getList());
     * }
     * =============================================================================
     */

    // ===================== 原写法：6 个参数(已注释，仅作对比) =====================
    //
    // @Override
    // public PageResult<Emp> page(Integer page, Integer pageSize,
    //                             String name, Integer gender,
    //                             LocalDate begin, LocalDate end) {
    //     //0.参数兜底：页码从1开始，每页条数默认10，避免出现负数起始索引
    //     if (page == null || page < 1) {
    //         page = 1;
    //     }
    //     if (pageSize == null || pageSize < 1) {
    //         pageSize = 10;
    //     }
    //
    //     // 方式一：手工分页(原写法，仅作对比)
    //     //1.调用Mapper接口，查询总记录数
    //     Long total = empMapper.count();
    //
    //     //2.调用Mapper接口，查询结果列表
    //     Integer start = (page - 1) * pageSize; //起始索引 = (页码 - 1) * 每页记录数
    //     List<Emp> rows = empMapper.list(start, pageSize);
    //
    //     //3.封装结果PageResult
    //     return new PageResult<Emp>(total, rows);
    //
    //     // 方式二：PageHelper 分页
    //     //1.设置分页参数(PageHelper 只对它后面紧邻的第一条查询生效)
    //     PageHelper.startPage(page, pageSize);
    //
    //     //2.调用Mapper接口，按条件查询结果列表
    //     List<Emp> rows = empMapper.list(name, gender, begin, end);
    //
    //     //3.封装结果PageResult
    //     PageInfo<Emp> pageInfo = new PageInfo<>(rows);
    //     return new PageResult<Emp>(pageInfo.getTotal(), pageInfo.getList());
    // }
    // ===========================================================================
}
