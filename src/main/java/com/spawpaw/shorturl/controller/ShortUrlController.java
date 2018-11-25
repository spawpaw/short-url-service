package com.spawpaw.shorturl.controller;

import com.spawpaw.shorturl.Config;
import com.spawpaw.shorturl.entity.ShortUrl;
import com.spawpaw.shorturl.exception.ErrorCode;
import com.spawpaw.shorturl.exception.ServiceException;
import com.spawpaw.shorturl.service.ShortUrlService;
import com.spawpaw.shorturl.service.impl.AutoIncrementShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ShortUrlController {

    @Resource(type = AutoIncrementShortUrlService.class)
    ShortUrlService shortUrlService;

    @Autowired
    Config config;

    @RequestMapping(path = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("config", config);
        return "index";
    }

    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST)
    public String createShortUrl(@RequestParam("url") String url, @RequestParam("action") String action, @RequestParam(value = "expiresInSecond", required = false, defaultValue = "0") Long expiresInSecond, Model model) {
        try {
            ShortUrl shortUrl;
            if ("create".equals(action))
                shortUrl = shortUrlService.encode(url, expiresInSecond);
            else if ("parse".equals(action))
                shortUrl = shortUrlService.decode(url);
            else
                throw new ServiceException(ErrorCode.ERR_INVALID_REQUEST_PARAM, "action should be `create` or `parse`");
            model.addAttribute("shortUrl", shortUrl);
        } catch (ServiceException e) {
            e.printStackTrace();
            model.addAttribute("ex", e);
        }

        model.addAttribute("config", config);
        return "index";
    }

    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(path = "/{shortUrl}", method = RequestMethod.GET)
    public void redirect(@PathVariable(value = "shortUrl", required = true) String shortUrl, HttpServletResponse response) {
        String rawUrl = shortUrlService.decode(shortUrl).getRawUrl();
        response.setHeader("Location", rawUrl);
    }
}
