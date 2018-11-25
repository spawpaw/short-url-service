package com.spawpaw.shorturl.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Log {
    private Long id;
    private String ip;
    private Long urlId;
    private Timestamp createTime;
}
