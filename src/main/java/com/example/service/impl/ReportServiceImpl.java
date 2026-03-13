package com.example.service.impl;

import com.example.mapper.ClazzMapper;
import com.example.mapper.EmpMapper;
import com.example.mapper.StudentMapper;
import com.example.pojo.JobOption;
import com.example.pojo.ClazzCountOption;
import com.example.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public JobOption getEmpJobData() {
        List<Map<String,Object>> list = empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new JobOption(jobList, dataList);
    }

    @Override
    public List<Map> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    @Override
    public Map<String,Object> clazzStudentCount() {
        List<ClazzCountOption> list = studentMapper.clazzStudentCount();

        List<String> clazzList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        for (ClazzCountOption item : list) {
            clazzList.add(item.getClazzName());
            dataList.add(item.getData());
        }

        Map<String,Object> res = new HashMap<>();
        res.put("clazzList", clazzList);
        res.put("dataList", dataList);

        return res;
    }

    @Override
    public List<Map> getstudentDegreeData() {
        return studentMapper.getstudentDegreeData();
    }
}