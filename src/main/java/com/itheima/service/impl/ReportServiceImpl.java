package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.GenderData;
import com.itheima.pojo.JobOption;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public JobOption getEmpJobData() {
        List<Map<String, Object>> rows = empMapper.countEmpByJob();

        List<String> jobList = new ArrayList<>(rows.size());
        List<Long> dataList = new ArrayList<>(rows.size());

        for (Map<String, Object> row : rows) {
            jobList.add(String.valueOf(row.get("jobName")));

            Object count = row.get("empCount");
            dataList.add(count instanceof Number number ? number.longValue() : 0L);
        }

        return new JobOption(jobList, dataList);
    }

    @Override
    public List<GenderData> getEmpGenderData() {
        return empMapper.countEmpByGender();
    }
}
