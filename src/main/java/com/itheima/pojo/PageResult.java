package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果封装
 * @param <T> 当前页数据的类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;    //总记录数
    private List<T> rows;  //当前页的数据列表
}
