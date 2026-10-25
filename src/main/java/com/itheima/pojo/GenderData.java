package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工性别统计项，对应前端饼图中的一个数据项
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenderData {

    private String name;
    private Long value;
}
