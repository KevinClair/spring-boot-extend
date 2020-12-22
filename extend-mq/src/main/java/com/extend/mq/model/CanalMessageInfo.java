package com.extend.mq.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * CanalMessageInfo.
 *
 * @author KevinClair
 */
@Data
public class CanalMessageInfo<T> {

    // 数据实体
    private List<T> data;

    // 数据库名
    private String database;

    // 数据库字段类型
    private Map<String,String> mysqlType;

    // 修改前的数据，如果是Insert类型，这个值为Null，对于多条修改，顺序和data中的对应
    private List<T> old;

    // 表名
    private String table;

    // 消息类型，详情请查看com.example.common.constant.CanalMessageTypeConstant
    private String type;
}
