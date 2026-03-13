package com.example.service;

import com.example.pojo.Emp;
import com.example.pojo.EmpQueryParam;
import com.example.pojo.LoginInfo;
import com.example.pojo.PageResult;

import java.util.List;

public interface EmpService {

    PageResult page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    Emp getById(Integer id);

    void updateInfo(Emp emp);

    void deleteByIds(List<Integer> ids);

    List<Emp> list();

    LoginInfo login(Emp emp);
}
