package com.example.service;

import com.example.pojo.PageResult;
import com.example.pojo.StuQueryParam;
import com.example.pojo.Student;

import java.util.List;

public interface StudentService {
    PageResult page(StuQueryParam stu);

    void addStu(Student stu);

    Student getStuById(Integer id);

    void modifyStu(Student stu);

    void deleteStu(List<Integer> ids);

    void violStu(Integer id,Integer score);
}
