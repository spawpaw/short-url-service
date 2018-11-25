package com.spawpaw.shorturl.service.impl;

import com.spawpaw.shorturl.dao.ShortUrlDao;
import com.spawpaw.shorturl.entity.ShortUrl;
import com.spawpaw.shorturl.exception.ErrorCode;
import com.spawpaw.shorturl.exception.ParseExceprion;
import com.spawpaw.shorturl.service.AbstractShortUrlService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 基于自增算法的短url生成实现
 */
@Service
@Log
public class AutoIncrementShortUrlService extends AbstractShortUrlService {
    @Resource
    ShortUrlDao shortUrlDao;

    @Override
    public ShortUrl encode(String url, long expiresInSecond) {
        ShortUrl shortUrl = parseUrl(url);

        //是否已存在相同的url
        ShortUrl existedShortUrl = shortUrlDao.findByRawUrl(shortUrl.getRawUrl());
        if (existedShortUrl != null)
            return existedShortUrl;

        shortUrlDao.save(shortUrl);
        shortUrl.setShortUrl(translateNumberToEncodedString(shortUrl.getId()));
        shortUrlDao.save(shortUrl);

        return shortUrl;
    }

    @Override
    public ShortUrl decode(String shortUrl) {
        long id = translateEncodedStringToNumber(shortUrl);
        ShortUrl e = shortUrlDao.findById(id).orElse(null);
        if (e == null) throw new ParseExceprion(ErrorCode.ERR_SHORT_URL_NOT_EXIST, "ERR_SHORT_URL_NOT_EXIST");
        else return e;
    }
}
