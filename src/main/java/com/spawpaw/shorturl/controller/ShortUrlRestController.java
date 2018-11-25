package com.spawpaw.shorturl.controller;

import com.spawpaw.shorturl.Config;
import com.spawpaw.shorturl.dto.CreateShortUrlRequest;
import com.spawpaw.shorturl.dto.ParseShortUrlRequest;
import com.spawpaw.shorturl.dto.ShortUrlResponse;
import com.spawpaw.shorturl.entity.ShortUrl;
import com.spawpaw.shorturl.exception.ErrorCode;
import com.spawpaw.shorturl.exception.ServiceException;
import com.spawpaw.shorturl.service.ShortUrlService;
import com.spawpaw.shorturl.service.impl.AutoIncrementShortUrlService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api")
public class ShortUrlRestController {

    @Resource(type = AutoIncrementShortUrlService.class)
    ShortUrlService shortUrlService;
    @Resource
    Config config;

    @RequestMapping(path = "create", method = RequestMethod.POST)
    public ShortUrlResponse createShortUrl(@RequestBody CreateShortUrlRequest request) {
        ShortUrlResponse response = new ShortUrlResponse();

        try {
            ShortUrl encodedUrl = shortUrlService.encode(request.getUrl(), 0);
            response.setCode(ErrorCode.OK);
            response.setMsg("OK");
            response.setLongUrl(encodedUrl.getRawUrl());
            response.setShortUrl(config.shortUrlHost + encodedUrl.getShortUrl());
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                response.setCode(((ServiceException) e).getCode());
            } else {
                response.setCode(ErrorCode.ERR_UNKNOWN);
            }
        }

        return response;
    }

    @RequestMapping(path = "parse", method = RequestMethod.POST)
    public ShortUrlResponse parseUrl(@RequestBody ParseShortUrlRequest request) {
        ShortUrlResponse response = new ShortUrlResponse();
        try {
            ShortUrl encodedUrl = shortUrlService.decode(request.getShortUrl());
            response.setCode(ErrorCode.OK);
            response.setMsg("OK");
            response.setLongUrl(encodedUrl.getRawUrl());
            response.setShortUrl(config.shortUrlHost + encodedUrl.getShortUrl());
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ServiceException) {
                response.setCode(((ServiceException) e).getCode());
            } else {
                response.setCode(ErrorCode.ERR_UNKNOWN);
            }
        }
        return response;
    }
}
