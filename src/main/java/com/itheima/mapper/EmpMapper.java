package com.itheima.mapper;

import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
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
     * ===================== 注解方式写 SQL(已改成注释，仅作对比) =====================
     * 特点：SQL 直接写在注解里，只适合简单语句；条件多了以后不易读，IDE 里也容易误报
     *
     * @Select("""
     *         <script>
     *         select e.*, d.name deptName
     *         from emp e left join dept d on e.dept_id = d.id
     *         <where>
     *             <if test="name != null and name != ''">
     *                 and e.name like concat('%', #{name}, '%')
     *             </if>
     *             <if test="gender != null">
     *                 and e.gender = #{gender}
     *             </if>
     *             <if test="begin != null">
     *                 and e.entry_date &gt;= #{begin}
     *             </if>
     *             <if test="end != null">
     *                 and e.entry_date &lt;= #{end}
     *             </if>
     *         </where>
     *         order by e.update_time desc
     *         </script>
     *         """)
     * =================================================================================
     */

    /*
     * ===================== XML 映射文件方式(现用写法) =====================
     * SQL 写在 resources/mapper/EmpMapper.xml 里，通过 namespace + id 与这里对应，
     * 需要在 application.yml 中配置 mybatis.mapper-locations: classpath:mapper/*.xml
     * 分页仍由 PageHelper 自动完成：插件会自动追加 limit，并自动执行 count 查询
     */
    public List<Emp> list(@Param("name") String name,
                          @Param("gender") Integer gender,
                          @Param("begin") LocalDate begin,
                          @Param("end") LocalDate end);
}
