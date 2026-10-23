package com.itheima.mapper;


import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface EmpExprMapper {

    /**
     * 批量保存员工的工作经历信息
     * @param exprList
     */
    //@Insert("insert into emp_expr(emp_id, begin, end, company, job) values ()")
    void insertBatch(List<EmpExpr> exprList);

    /**
     * 根据员工id批量删除工作经历
     */
    void deleteByEmpIds(@Param("ids") List<Integer> ids);
}
