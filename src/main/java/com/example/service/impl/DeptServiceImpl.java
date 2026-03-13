package com.example.service.impl;

import com.example.mapper.DeptMapper;
import com.example.pojo.Dept;
import com.example.pojo.Result;
import com.example.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        if(deptMapper.countEmp(id) > 0){
            throw new RuntimeException("对不起，该部门下还有员工，不能直接删除");
        }
        deptMapper.deleteById(id);
    }

    @Override
    public void addDept(Dept dept) {
        //补全基础属性
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.addDept(dept);
    }

    @Override
    public Dept getInfo(Integer id) {
        return deptMapper.getInfo(id);
    }

    @Override
    public void updateInfo(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.updateInfo(dept);
    }
}
