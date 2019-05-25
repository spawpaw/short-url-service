package com.spawpaw.shorturl.service.impl;

import com.spawpaw.shorturl.Config;
import com.spawpaw.shorturl.dao.ShortUrlDao;
import com.spawpaw.shorturl.entity.ShortUrl;
import com.spawpaw.shorturl.exception.ErrorCode;
import com.spawpaw.shorturl.exception.ParseExceprion;
import com.spawpaw.shorturl.service.AbstractShortUrlService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 基于hash值的短url编码，
 *
 */
@Service
@Log
public class HashBasedShortUrlService extends AbstractShortUrlService {
    @Resource
    ShortUrlDao shortUrlDao;
    @Resource
    Config config;


    @Override
    public ShortUrl encode(String url, long expiresInSecond) {

        return null;
    }

    @Override
    public ShortUrl decode(String shortUrl) {
        long id = translateEncodedStringToNumber(shortUrl);
        ShortUrl e = shortUrlDao.findById(id).orElse(null);
        if (e == null) throw new ParseExceprion(ErrorCode.ERR_SHORT_URL_NOT_EXIST, "ERR_SHORT_URL_NOT_EXIST");
        else return e;
    }
}
