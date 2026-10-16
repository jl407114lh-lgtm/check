package com.itheima.mapper;

import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpMapper {
    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
    public Long count();

    @Select("select e.*, d.name deptName from emp e left join dept d on e.dept_id=d.id " +
            "order by e.update_time desc limit #{start},#{pageSize}")
    public List<Emp> list(Integer start, Integer pageSize);
}
