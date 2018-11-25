package com.spawpaw.shorturl.dto;

import lombok.Data;

@Data
public class ShortUrlResponse {
    private Integer code;
    private String msg;
    private String shortUrl;
    private String longUrl;
}
