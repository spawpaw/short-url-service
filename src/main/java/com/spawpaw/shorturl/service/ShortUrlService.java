package com.spawpaw.shorturl.service;

import com.spawpaw.shorturl.entity.ShortUrl;

public interface ShortUrlService {

    /**
     * @param url             要缩短的url
     * @param expiresInSecond 多少秒后过期，0为不过期
     * @return 缩短后的url
     */
    ShortUrl encode(String url, long expiresInSecond);

    /**
     * 解码短url
     *
     * @param shortUrl 要解码的短url
     * @return 长url
     */
    ShortUrl decode(String shortUrl);
}
