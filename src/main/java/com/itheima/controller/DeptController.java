package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/depts")
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    //@RequestMapping(value = "/depts",method = RequestMethod.GET)
    @GetMapping
    public Result list() {
        System.out.println("查询全部部门数据");
        List<Dept> deptList =deptService.findAll();
        return Result.success(deptList);
    }

    //定义一个方法，根据id删除部门信息

    @DeleteMapping("{id}")

    public Result delete(@PathVariable Integer id) {
        System.out.println("删除部门数据");
        deptService.delete(id);
        return Result.success();
    }

    //定义一个方法，新增部门数据

    @PostMapping
    public Result add(@RequestBody Dept dept) {
        System.out.println("新增部门数据");
        deptService.add(dept);
        return Result.success();
    }

    @GetMapping("{id}")
    public Result findById(@PathVariable Integer id) {
        System.out.println("查询单个部门数据");
        Dept dept = deptService.findById(id);
        return Result.success(dept);
    }

    @PutMapping
    public Result update(@RequestBody Dept dept){
        System.out.println("修改部门数据");
        deptService.update(dept);
        return Result.success();
    }


}
