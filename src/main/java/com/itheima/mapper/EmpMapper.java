package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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
     * ===================== 原写法：多个参数(已注释，仅作对比) =====================
     * 特点：参数一多，方法签名就很长；调用方要按顺序传，漏传/传错位置编译期发现不了
     *
     * public List<Emp> list(@Param("name") String name,
     *                       @Param("gender") Integer gender,
     *                       @Param("begin") LocalDate begin,
     *                       @Param("end") LocalDate end);
     * =============================================================================
     */

    /*
     * 现用写法：查询条件封装成 EmpQueryParam 对象
     * SQL 在 resources/com/itheima/mapper/EmpMapper.xml 里(同包同名)，
     * MyBatis 把该对象当作唯一的参数对象，XML 里可以直接用 #{name}、#{gender} 取属性
     */
    public List<Emp> list(EmpQueryParam param);


    /**
     * 新增员工基本信息
     * 现在只保存员工基本信息，所以不需要回填主键；
     * 等要做"保存工作经历"时，再在方法上加：
     * @Options(useGeneratedKeys = true, keyProperty = "id")
     * 把自增主键回填到 emp.id，供 emp_expr.emp_id 使用
     * @param emp
     */
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)\n" +
            "        values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);
}
