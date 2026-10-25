package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /*
     * ===================== 原写法：多个参数(已注释，仅作对比) =====================
     *
     * @GetMapping
     * public Result page(@RequestParam(defaultValue = "1") Integer page,
     *                    @RequestParam(defaultValue = "10") Integer pageSize,
     *                    String name,
     *                    Integer gender,
     *                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
     *                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
     *     log.info("分页查询: page={}, pageSize={}, name={}, gender={}, begin={}, end={}",
     *             page, pageSize, name, gender, begin, end);
     *     PageResult<Emp> pageResult=empService.page(page,pageSize,name,gender,begin,end);
     *     return Result.success(pageResult);
     * }
     * =================================================================================
     */

    /*
     * ===================== 上一版：分页参数单独传(已注释，仅作对比) =====================
     *
     * @GetMapping
     * public Result page(@RequestParam(defaultValue = "1") Integer page,
     *                    @RequestParam(defaultValue = "10") Integer pageSize,
     *                    EmpQueryParam param){
     *     log.info("分页查询: page={}, pageSize={}, param={}", page, pageSize, param);
     *     PageResult<Emp> pageResult=empService.page(page,pageSize,param);
     *     return Result.success(pageResult);
     * }
     * =================================================================================
     */

    /*
     * 分页查询(支持按姓名模糊、性别精确、入职日期范围查询)
     * 只接收一个 EmpQueryParam 对象：Spring 会把请求参数
     * page/pageSize/name/gender/begin/end 全部绑定到该对象的同名字段上
     */
    @GetMapping
    public Result page(EmpQueryParam param){
        log.info("分页查询: param={}", param);
        PageResult<Emp> pageResult=empService.page(param);
        return Result.success(pageResult);
    }

    /**
     * 新增员工信息
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工: {}",emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 删除员工(支持单个删除和批量删除)
     * 请求示例：DELETE /emps?ids=1,2,3
     */
    @DeleteMapping
    public Result delete(@RequestParam(required = false) List<Integer> ids){
        log.info("删除员工: ids={}", ids);
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的员工");
        }
        empService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 根据id查询员工(编辑时回显基本信息和工作经历)
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("查询员工: id={}", id);
        return Result.success(empService.getById(id));
    }

    /**
     * 修改员工(基本信息和全部工作经历)
     */
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工: {}", emp);
        empService.update(emp);
        return Result.success();
    }
}
