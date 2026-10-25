package com.itheima.service;

import com.itheima.pojo.JobOption;
import com.itheima.pojo.GenderData;

import java.util.List;

public interface ReportService {

    /**
     * 统计各职位员工人数
     * @return 职位名称列表和对应的员工人数列表
     */
    JobOption getEmpJobData();

    /**
     * 统计各性别员工人数
     * @return 可直接供前端饼图使用的数据列表
     */
    List<GenderData> getEmpGenderData();
}
