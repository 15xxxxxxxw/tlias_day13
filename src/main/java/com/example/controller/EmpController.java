package com.example.controller;

import com.example.annotation.LogOperation;
import com.example.pojo.Emp;
import com.example.pojo.EmpQueryParam;
import com.example.pojo.PageResult;
import com.example.pojo.Result;
import com.example.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/emps")
@RestController
public class EmpController {
    @Autowired
    private EmpService empService;

    @GetMapping
    public Result page(EmpQueryParam empQueryParam){
        log.info("查询请求参数： {}", empQueryParam);
        PageResult pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }
    @LogOperation
    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工：{}",emp);
        empService.save(emp);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("显示员工{}的信息",id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }

    @LogOperation
    @PutMapping
    public Result updateInfo(@RequestBody Emp emp){
        log.info("修改员工信息,{}",emp);
        empService.updateInfo(emp);
        return Result.success();
    }

    @LogOperation
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除员工：ids={}",ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/list")
    public Result emplist(){
        log.info("查询所有员工");
        List<Emp> list = empService.list();
        return Result.success(list);
    }
}
