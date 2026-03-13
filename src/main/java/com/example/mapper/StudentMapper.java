package com.example.mapper;

import com.example.pojo.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
     List<Student> list(StuQueryParam stu);

     @Insert("insert into student(name,no,gender,phone,degree,clazz_id,id_card,is_college,address,graduation_date,create_time, update_time)" +
             "values (#{name},#{no},#{gender},#{phone},#{degree},#{clazzId},#{idCard},#{isCollege},#{address},#{graduationDate},#{createTime},#{updateTime})")
    void addStu(Student stu);

     Student getStuById(Integer id);

    void modifyStu(Student stu);

    void deleteStu(List<Integer> ids);

    void violStu(@Param("id") Integer id, @Param("score") Integer score);

    List<ClazzCountOption> clazzStudentCount();

    @MapKey("name")
    List<Map> getstudentDegreeData();
}
