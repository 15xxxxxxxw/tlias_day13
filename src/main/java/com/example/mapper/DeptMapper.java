package com.example.mapper;

import com.example.pojo.Dept;
import com.example.pojo.Result;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface DeptMapper {
    @Select("select * from dept")
    List<Dept> findAll();

    @Select("select count(*) from emp where dept_id = #{id}")
    Integer countEmp(Integer id);

    @Delete("delete from dept where id = #{id}")
    void deleteById(int id);

    @Insert("insert into dept(name,create_time,update_time) values (#{name},#{createTime},#{updateTime})")
    void addDept(Dept dept);
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getInfo(Integer id);

    @Update("update dept set name = #{name},update_time = #{updateTime} where id = #{id}")
    void updateInfo(Dept dept);
}
