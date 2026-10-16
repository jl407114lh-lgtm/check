package com.itheima.mapper;

import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpMapper {

    /*
     * ===================== 方式一：手工分页(原写法，已注释，仅作对比) =====================
     * 特点：总数要自己写一条 count SQL；列表要自己算起始索引，并在 SQL 里写 limit
     *
     * @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
     * public Long count();
     *
     * @Select("select e.*, d.name deptName from emp e left join dept d on e.dept_id=d.id " +
     *         "order by e.update_time desc limit #{start},#{pageSize}")
     * public List<Emp> list(Integer start, Integer pageSize);
     * ===================================================================================
     */

    /*
     * ===================== 方式二：PageHelper 分页(现用写法) =====================
     * 特点：SQL 里不写 limit，插件会自动追加 limit，并自动执行一条 count 查询
     *       方法不再需要任何分页参数，只关心"查询哪些数据"
     */
    @Select("select e.*, d.name deptName from emp e left join dept d on e.dept_id=d.id " +
            "order by e.update_time desc")
    public List<Emp> list();
}
