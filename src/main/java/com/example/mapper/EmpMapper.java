package com.example.mapper;

import com.example.pojo.Emp;
import com.example.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
    public List<Emp> list(EmpQueryParam empQueryParam);//当 MyBatis 只有一个参数对象时，会自动把对象字段当作参数名传入 XML
    @Options(useGeneratedKeys = true, keyProperty = "id")//useGeneratedKeys=true表示Mybatis主动获取数据库生成的主键，并将主键赋给xx --> keyProperty=“xx”
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    Emp getById(Integer id);


    void updateInfo(Emp emp);

    void deleteByIds(List<Integer> ids);

    /**
     * 统计各个职位的员工人数
     */
    @MapKey("pos")//指定返回的 Map 的 key 对应数据库中的哪个字段。
    List<Map<String,Object>> countEmpJobData();
    @MapKey("name")
    List<Map> countEmpGenderData();
    @Select("select * from emp where job = 1")
    List<Emp> emplist();

    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getUsernameAndPassword(Emp emp);
}
