package com.spawpaw.shorturl;

import com.spawpaw.shorturl.entity.ShortUrl;
import com.spawpaw.shorturl.exception.ErrorCode;
import com.spawpaw.shorturl.exception.UrlNotValidException;
import com.spawpaw.shorturl.service.impl.AutoIncrementShortUrlService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbstractUrlServiceTest {
    private static final String[][] exceptionTestCases = new String[][]{
            {String.valueOf(ErrorCode.ERR_INVALID_SERVER_PORT), "www.example.host:808888"},
            // TODO: 添加能诱发异常情况的测试用例
    };
    @Resource
    Config config;
    @Resource
    AutoIncrementShortUrlService autoIncrementShortUrlService;
    //[]:  rawUrl, expectedParsedFullUrl, protocol, ftpUsername, ftpPassword, host, ipAddress, port, path, param
    private String testCases[][] = new String[][]{
            //   protocol://host
            {"www.example.com", "http://www.example.com", "http", "", "", "www.example.com", "false", "-1", "", ""},
            {"192.168.1.1", "http://192.168.1.1", "http", "", "", "192.168.1.1", "true", "-1", "", ""},
            {"//www.example.com", "//www.example.com", "//", "", "", "www.example.com", "false", "-1", "", ""},
            {"//192.168.1.1", "//192.168.1.1", "//", "", "", "192.168.1.1", "true", "-1", "", ""},
            {"https://www.example.com", "https://www.example.com", "https", "", "", "www.example.com", "false", "-1", "", ""},
            {"https://192.168.1.1", "https://192.168.1.1", "https", "", "", "192.168.1.1", "true", "-1", "", ""},
            {"ftp://www.example.com", "ftp://www.example.com", "ftp", "", "", "www.example.com", "false", "-1", "", ""},
            {"ftp://192.168.1.1", "ftp://192.168.1.1", "ftp", "", "", "192.168.1.1", "true", "-1", "", ""},
            {"ftp://testUser@192.168.1.1", "ftp://testUser@192.168.1.1", "ftp", "testUser", "", "192.168.1.1", "true", "-1", "", ""},
            {"ftp://testUser:password@www.example.com", "ftp://testUser:password@www.example.com", "ftp", "testUser", "password", "www.example.com", "false", "-1", "", ""},
            {"ftp://testUser:password@192.168.1.1", "ftp://testUser:password@192.168.1.1", "ftp", "testUser", "password", "192.168.1.1", "true", "-1", "", ""},
            //   protocol://host/
            {"www.example.com/", "http://www.example.com/", "http", "", "", "www.example.com", "false", "-1", "/", ""},
            {"192.168.1.1/", "http://192.168.1.1/", "http", "", "", "192.168.1.1", "true", "-1", "/", ""},
            {"//www.example.com/", "//www.example.com/", "//", "", "", "www.example.com", "false", "-1", "/", ""},
            {"//192.168.1.1/", "//192.168.1.1/", "//", "", "", "192.168.1.1", "true", "-1", "/", ""},
            {"https://www.example.com/", "https://www.example.com/", "https", "", "", "www.example.com", "false", "-1", "/", ""},
            {"https://192.168.1.1/", "https://192.168.1.1/", "https", "", "", "192.168.1.1", "true", "-1", "/", ""},
            {"ftp://www.example.com/", "ftp://www.example.com/", "ftp", "", "", "www.example.com", "false", "-1", "/", ""},
            {"ftp://192.168.1.1/", "ftp://192.168.1.1/", "ftp", "", "", "192.168.1.1", "true", "-1", "/", ""},
            {"ftp://testUser@192.168.1.1/", "ftp://testUser@192.168.1.1/", "ftp", "testUser", "", "192.168.1.1", "true", "-1", "/", ""},
            {"ftp://testUser:password@www.example.com/", "ftp://testUser:password@www.example.com/", "ftp", "testUser", "password", "www.example.com", "false", "-1", "/", ""},
            {"ftp://testUser:password@192.168.1.1/", "ftp://testUser:password@192.168.1.1/", "ftp", "testUser", "password", "192.168.1.1", "true", "-1", "/", ""},
            //   protocol://host/path
            {"www.example.com/pathPath", "http://www.example.com/pathPath", "http", "", "", "www.example.com", "false", "-1", "/pathPath", ""},
            {"192.168.1.1/pathPath", "http://192.168.1.1/pathPath", "http", "", "", "192.168.1.1", "true", "-1", "/pathPath", ""},
            {"//www.example.com/pathPath", "//www.example.com/pathPath", "//", "", "", "www.example.com", "false", "-1", "/pathPath", ""},
            {"//192.168.1.1/pathPath", "//192.168.1.1/pathPath", "//", "", "", "192.168.1.1", "true", "-1", "/pathPath", ""},
            {"https://www.example.com/pathPath", "https://www.example.com/pathPath", "https", "", "", "www.example.com", "false", "-1", "/pathPath", ""},
            {"https://192.168.1.1/pathPath", "https://192.168.1.1/pathPath", "https", "", "", "192.168.1.1", "true", "-1", "/pathPath", ""},
            {"ftp://www.example.com/pathPath", "ftp://www.example.com/pathPath", "ftp", "", "", "www.example.com", "false", "-1", "/pathPath", ""},
            {"ftp://192.168.1.1/pathPath", "ftp://192.168.1.1/pathPath", "ftp", "", "", "192.168.1.1", "true", "-1", "/pathPath", ""},
            {"ftp://testUser@192.168.1.1/pathPath", "ftp://testUser@192.168.1.1/pathPath", "ftp", "testUser", "", "192.168.1.1", "true", "-1", "/pathPath", ""},
            {"ftp://testUser:password@www.example.com/pathPath", "ftp://testUser:password@www.example.com/pathPath", "ftp", "testUser", "password", "www.example.com", "false", "-1", "/pathPath", ""},
            {"ftp://testUser:password@192.168.1.1/pathPath", "ftp://testUser:password@192.168.1.1/pathPath", "ftp", "testUser", "password", "192.168.1.1", "true", "-1", "/pathPath", ""},
            //   protocol://host/path.type
            {"www.example.com/pathPath.jsoN", "http://www.example.com/pathPath.jsoN", "http", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", ""},
            {"192.168.1.1/pathPath.jsoN", "http://192.168.1.1/pathPath.jsoN", "http", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", ""},
            {"//www.example.com/pathPath.jsoN", "//www.example.com/pathPath.jsoN", "//", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", ""},
            {"//192.168.1.1/pathPath.jsoN", "//192.168.1.1/pathPath.jsoN", "//", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", ""},
            {"https://www.example.com/pathPath.jsoN", "https://www.example.com/pathPath.jsoN", "https", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", ""},
            {"https://192.168.1.1/pathPath.jsoN", "https://192.168.1.1/pathPath.jsoN", "https", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", ""},
            {"ftp://www.example.com/pathPath.jsoN", "ftp://www.example.com/pathPath.jsoN", "ftp", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", ""},
            {"ftp://192.168.1.1/pathPath.jsoN", "ftp://192.168.1.1/pathPath.jsoN", "ftp", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", ""},
            {"ftp://testUser@192.168.1.1/pathPath.jsoN", "ftp://testUser@192.168.1.1/pathPath.jsoN", "ftp", "testUser", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", ""},
            {"ftp://testUser:password@www.example.com/pathPath.jsoN", "ftp://testUser:password@www.example.com/pathPath.jsoN", "ftp", "testUser", "password", "www.example.com", "false", "-1", "/pathPath.jsoN", ""},
            {"ftp://testUser:password@192.168.1.1/pathPath.jsoN", "ftp://testUser:password@192.168.1.1/pathPath.jsoN", "ftp", "testUser", "password", "192.168.1.1", "true", "-1", "/pathPath.jsoN", ""},
            //   protocol://host/path.type?param
            {"www.example.com/pathPath.jsoN?pAram", "http://www.example.com/pathPath.jsoN?pAram", "http", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram"},
            {"192.168.1.1/pathPath.jsoN?pAram", "http://192.168.1.1/pathPath.jsoN?pAram", "http", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram"},
            {"//www.example.com/pathPath.jsoN?pAram", "//www.example.com/pathPath.jsoN?pAram", "//", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram"},
            {"//192.168.1.1/pathPath.jsoN?pAram", "//192.168.1.1/pathPath.jsoN?pAram", "//", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram"},
            {"https://www.example.com/pathPath.jsoN?pAram", "https://www.example.com/pathPath.jsoN?pAram", "https", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram"},
            {"https://192.168.1.1/pathPath.jsoN?pAram", "https://192.168.1.1/pathPath.jsoN?pAram", "https", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram"},
            {"ftp://www.example.com/pathPath.jsoN?pAram", "ftp://www.example.com/pathPath.jsoN?pAram", "ftp", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram"},
            {"ftp://192.168.1.1/pathPath.jsoN?pAram", "ftp://192.168.1.1/pathPath.jsoN?pAram", "ftp", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram"},
            {"ftp://testUser@192.168.1.1/pathPath.jsoN?pAram", "ftp://testUser@192.168.1.1/pathPath.jsoN?pAram", "ftp", "testUser", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram"},
            {"ftp://testUser:password@www.example.com/pathPath.jsoN?pAram", "ftp://testUser:password@www.example.com/pathPath.jsoN?pAram", "ftp", "testUser", "password", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram"},
            {"ftp://testUser:password@192.168.1.1/pathPath.jsoN?pAram", "ftp://testUser:password@192.168.1.1/pathPath.jsoN?pAram", "ftp", "testUser", "password", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram"},
            //   protocol://host/path.type?param=xXx
            {"www.example.com/pathPath.jsoN?pAram=xXx", "http://www.example.com/pathPath.jsoN?pAram=xXx", "http", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"192.168.1.1/pathPath.jsoN?pAram=xXx", "http://192.168.1.1/pathPath.jsoN?pAram=xXx", "http", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"//www.example.com/pathPath.jsoN?pAram=xXx", "//www.example.com/pathPath.jsoN?pAram=xXx", "//", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"//192.168.1.1/pathPath.jsoN?pAram=xXx", "//192.168.1.1/pathPath.jsoN?pAram=xXx", "//", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"https://www.example.com/pathPath.jsoN?pAram=xXx", "https://www.example.com/pathPath.jsoN?pAram=xXx", "https", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"https://192.168.1.1/pathPath.jsoN?pAram=xXx", "https://192.168.1.1/pathPath.jsoN?pAram=xXx", "https", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://www.example.com/pathPath.jsoN?pAram=xXx", "ftp://www.example.com/pathPath.jsoN?pAram=xXx", "ftp", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://192.168.1.1/pathPath.jsoN?pAram=xXx", "ftp://192.168.1.1/pathPath.jsoN?pAram=xXx", "ftp", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://testUser@192.168.1.1/pathPath.jsoN?pAram=xXx", "ftp://testUser@192.168.1.1/pathPath.jsoN?pAram=xXx", "ftp", "testUser", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://testUser:password@www.example.com/pathPath.jsoN?pAram=xXx", "ftp://testUser:password@www.example.com/pathPath.jsoN?pAram=xXx", "ftp", "testUser", "password", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://testUser:password@192.168.1.1/pathPath.jsoN?pAram=xXx", "ftp://testUser:password@192.168.1.1/pathPath.jsoN?pAram=xXx", "ftp", "testUser", "password", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx"},

            //   protocol://host/path.type?param=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\,./:"|<>?
            {"www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http://www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http://192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"//www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"//192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"https://www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https://www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"https://192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https://192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "", "", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://testUser@192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://testUser@192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "testUser", "", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://testUser:password@www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://testUser:password@www.example.com/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "testUser", "password", "www.example.com", "false", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://testUser:password@192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://testUser:password@192.168.1.1/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "testUser", "password", "192.168.1.1", "true", "-1", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            //   protocol://host:port
            {"www.example.com:8088", "http://www.example.com:8088", "http", "", "", "www.example.com", "false", "8088", "", ""},
            {"192.168.1.1:8088", "http://192.168.1.1:8088", "http", "", "", "192.168.1.1", "true", "8088", "", ""},
            {"//www.example.com:8088", "//www.example.com:8088", "//", "", "", "www.example.com", "false", "8088", "", ""},
            {"//192.168.1.1:8088", "//192.168.1.1:8088", "//", "", "", "192.168.1.1", "true", "8088", "", ""},
            {"https://www.example.com:8088", "https://www.example.com:8088", "https", "", "", "www.example.com", "false", "8088", "", ""},
            {"https://192.168.1.1:8088", "https://192.168.1.1:8088", "https", "", "", "192.168.1.1", "true", "8088", "", ""},
            {"ftp://www.example.com:8088", "ftp://www.example.com:8088", "ftp", "", "", "www.example.com", "false", "8088", "", ""},
            {"ftp://192.168.1.1:8088", "ftp://192.168.1.1:8088", "ftp", "", "", "192.168.1.1", "true", "8088", "", ""},
            {"ftp://testUser@192.168.1.1:8088", "ftp://testUser@192.168.1.1:8088", "ftp", "testUser", "", "192.168.1.1", "true", "8088", "", ""},
            {"ftp://testUser:password@www.example.com:8088", "ftp://testUser:password@www.example.com:8088", "ftp", "testUser", "password", "www.example.com", "false", "8088", "", ""},
            {"ftp://testUser:password@192.168.1.1:8088", "ftp://testUser:password@192.168.1.1:8088", "ftp", "testUser", "password", "192.168.1.1", "true", "8088", "", ""},
            //   protocol://host:port/
            {"www.example.com:8088/", "http://www.example.com:8088/", "http", "", "", "www.example.com", "false", "8088", "/", ""},
            {"192.168.1.1:8088/", "http://192.168.1.1:8088/", "http", "", "", "192.168.1.1", "true", "8088", "/", ""},
            {"//www.example.com:8088/", "//www.example.com:8088/", "//", "", "", "www.example.com", "false", "8088", "/", ""},
            {"//192.168.1.1:8088/", "//192.168.1.1:8088/", "//", "", "", "192.168.1.1", "true", "8088", "/", ""},
            {"https://www.example.com:8088/", "https://www.example.com:8088/", "https", "", "", "www.example.com", "false", "8088", "/", ""},
            {"https://192.168.1.1:8088/", "https://192.168.1.1:8088/", "https", "", "", "192.168.1.1", "true", "8088", "/", ""},
            {"ftp://www.example.com:8088/", "ftp://www.example.com:8088/", "ftp", "", "", "www.example.com", "false", "8088", "/", ""},
            {"ftp://192.168.1.1:8088/", "ftp://192.168.1.1:8088/", "ftp", "", "", "192.168.1.1", "true", "8088", "/", ""},
            {"ftp://testUser@192.168.1.1:8088/", "ftp://testUser@192.168.1.1:8088/", "ftp", "testUser", "", "192.168.1.1", "true", "8088", "/", ""},
            {"ftp://testUser:password@www.example.com:8088/", "ftp://testUser:password@www.example.com:8088/", "ftp", "testUser", "password", "www.example.com", "false", "8088", "/", ""},
            {"ftp://testUser:password@192.168.1.1:8088/", "ftp://testUser:password@192.168.1.1:8088/", "ftp", "testUser", "password", "192.168.1.1", "true", "8088", "/", ""},
            //   protocol://host:port/path
            {"www.example.com:8088/pathPath", "http://www.example.com:8088/pathPath", "http", "", "", "www.example.com", "false", "8088", "/pathPath", ""},
            {"192.168.1.1:8088/pathPath", "http://192.168.1.1:8088/pathPath", "http", "", "", "192.168.1.1", "true", "8088", "/pathPath", ""},
            {"//www.example.com:8088/pathPath", "//www.example.com:8088/pathPath", "//", "", "", "www.example.com", "false", "8088", "/pathPath", ""},
            {"//192.168.1.1:8088/pathPath", "//192.168.1.1:8088/pathPath", "//", "", "", "192.168.1.1", "true", "8088", "/pathPath", ""},
            {"https://www.example.com:8088/pathPath", "https://www.example.com:8088/pathPath", "https", "", "", "www.example.com", "false", "8088", "/pathPath", ""},
            {"https://192.168.1.1:8088/pathPath", "https://192.168.1.1:8088/pathPath", "https", "", "", "192.168.1.1", "true", "8088", "/pathPath", ""},
            {"ftp://www.example.com:8088/pathPath", "ftp://www.example.com:8088/pathPath", "ftp", "", "", "www.example.com", "false", "8088", "/pathPath", ""},
            {"ftp://192.168.1.1:8088/pathPath", "ftp://192.168.1.1:8088/pathPath", "ftp", "", "", "192.168.1.1", "true", "8088", "/pathPath", ""},
            {"ftp://testUser@192.168.1.1:8088/pathPath", "ftp://testUser@192.168.1.1:8088/pathPath", "ftp", "testUser", "", "192.168.1.1", "true", "8088", "/pathPath", ""},
            {"ftp://testUser:password@www.example.com:8088/pathPath", "ftp://testUser:password@www.example.com:8088/pathPath", "ftp", "testUser", "password", "www.example.com", "false", "8088", "/pathPath", ""},
            {"ftp://testUser:password@192.168.1.1:8088/pathPath", "ftp://testUser:password@192.168.1.1:8088/pathPath", "ftp", "testUser", "password", "192.168.1.1", "true", "8088", "/pathPath", ""},
            //   protocol://host:port/path.type
            {"www.example.com:8088/pathPath.jsoN", "http://www.example.com:8088/pathPath.jsoN", "http", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", ""},
            {"192.168.1.1:8088/pathPath.jsoN", "http://192.168.1.1:8088/pathPath.jsoN", "http", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", ""},
            {"//www.example.com:8088/pathPath.jsoN", "//www.example.com:8088/pathPath.jsoN", "//", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", ""},
            {"//192.168.1.1:8088/pathPath.jsoN", "//192.168.1.1:8088/pathPath.jsoN", "//", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", ""},
            {"https://www.example.com:8088/pathPath.jsoN", "https://www.example.com:8088/pathPath.jsoN", "https", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", ""},
            {"https://192.168.1.1:8088/pathPath.jsoN", "https://192.168.1.1:8088/pathPath.jsoN", "https", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", ""},
            {"ftp://www.example.com:8088/pathPath.jsoN", "ftp://www.example.com:8088/pathPath.jsoN", "ftp", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", ""},
            {"ftp://192.168.1.1:8088/pathPath.jsoN", "ftp://192.168.1.1:8088/pathPath.jsoN", "ftp", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", ""},
            {"ftp://testUser@192.168.1.1:8088/pathPath.jsoN", "ftp://testUser@192.168.1.1:8088/pathPath.jsoN", "ftp", "testUser", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", ""},
            {"ftp://testUser:password@www.example.com:8088/pathPath.jsoN", "ftp://testUser:password@www.example.com:8088/pathPath.jsoN", "ftp", "testUser", "password", "www.example.com", "false", "8088", "/pathPath.jsoN", ""},
            {"ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN", "ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN", "ftp", "testUser", "password", "192.168.1.1", "true", "8088", "/pathPath.jsoN", ""},
            //   protocol://host:port/path.type?param
            {"www.example.com:8088/pathPath.jsoN?pAram", "http://www.example.com:8088/pathPath.jsoN?pAram", "http", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram"},
            {"192.168.1.1:8088/pathPath.jsoN?pAram", "http://192.168.1.1:8088/pathPath.jsoN?pAram", "http", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram"},
            {"//www.example.com:8088/pathPath.jsoN?pAram", "//www.example.com:8088/pathPath.jsoN?pAram", "//", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram"},
            {"//192.168.1.1:8088/pathPath.jsoN?pAram", "//192.168.1.1:8088/pathPath.jsoN?pAram", "//", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram"},
            {"https://www.example.com:8088/pathPath.jsoN?pAram", "https://www.example.com:8088/pathPath.jsoN?pAram", "https", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram"},
            {"https://192.168.1.1:8088/pathPath.jsoN?pAram", "https://192.168.1.1:8088/pathPath.jsoN?pAram", "https", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram"},
            {"ftp://www.example.com:8088/pathPath.jsoN?pAram", "ftp://www.example.com:8088/pathPath.jsoN?pAram", "ftp", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram"},
            {"ftp://192.168.1.1:8088/pathPath.jsoN?pAram", "ftp://192.168.1.1:8088/pathPath.jsoN?pAram", "ftp", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram"},
            {"ftp://testUser@192.168.1.1:8088/pathPath.jsoN?pAram", "ftp://testUser@192.168.1.1:8088/pathPath.jsoN?pAram", "ftp", "testUser", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram"},
            {"ftp://testUser:password@www.example.com:8088/pathPath.jsoN?pAram", "ftp://testUser:password@www.example.com:8088/pathPath.jsoN?pAram", "ftp", "testUser", "password", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram"},
            {"ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN?pAram", "ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN?pAram", "ftp", "testUser", "password", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram"},
            //   protocol://host:port/path.type?param=xXx
            {"www.example.com:8088/pathPath.jsoN?pAram=xXx", "http://www.example.com:8088/pathPath.jsoN?pAram=xXx", "http", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "http://192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "http", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"//www.example.com:8088/pathPath.jsoN?pAram=xXx", "//www.example.com:8088/pathPath.jsoN?pAram=xXx", "//", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"//192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "//192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "//", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"https://www.example.com:8088/pathPath.jsoN?pAram=xXx", "https://www.example.com:8088/pathPath.jsoN?pAram=xXx", "https", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"https://192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "https://192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "https", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://www.example.com:8088/pathPath.jsoN?pAram=xXx", "ftp://www.example.com:8088/pathPath.jsoN?pAram=xXx", "ftp", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "ftp://192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "ftp", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://testUser@192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "ftp://testUser@192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "ftp", "testUser", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://testUser:password@www.example.com:8088/pathPath.jsoN?pAram=xXx", "ftp://testUser:password@www.example.com:8088/pathPath.jsoN?pAram=xXx", "ftp", "testUser", "password", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx"},
            {"ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN?pAram=xXx", "ftp", "testUser", "password", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx"},
            //   protocol://host:port/path.type?param=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\,./:"|<>?
            {"www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http://www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http://192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "http", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"//www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"//192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "//", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"https://www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https://www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"https://192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https://192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "https", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "", "", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://testUser@192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://testUser@192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "testUser", "", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://testUser:password@www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://testUser:password@www.example.com:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "testUser", "password", "www.example.com", "false", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},
            {"ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp://testUser:password@192.168.1.1:8088/pathPath.jsoN?pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?", "ftp", "testUser", "password", "192.168.1.1", "true", "8088", "/pathPath.jsoN", "pAram=xXx&param2=xxX~!@#$%^&*()_+=-`[]{};'\\,./:\"|<>?"},


            //special protocol
            {"magnet:?xt=urn:btih:6A87068891A5D00EFBDFEB23B08BE5E4F6094232", "magnet:?xt=urn:btih:6A87068891A5D00EFBDFEB23B08BE5E4F6094232", "magnet", "", "", "", "false", "-1", "xt=urn:btih:6A87068891A5D00EFBDFEB23B08BE5E4F6094232", ""},
    };

    @Before
    public void before() {
    }

    @Test
    public void test() {
        for (String[] testCase : testCases) {
            System.out.println("test case: " + Arrays.toString(testCase));
            ShortUrl shortUrl = autoIncrementShortUrlService.parseUrl(testCase[0]);
            System.out.println("url:" + shortUrl.toString());
            Assert.assertEquals(testCase[1], shortUrl.getRawUrl());
            Assert.assertEquals(testCase[2], shortUrl.getProtocol());
            Assert.assertEquals(testCase[3], shortUrl.getFtpUsername());
            Assert.assertEquals(testCase[4], shortUrl.getFtpPassword());
            Assert.assertEquals(testCase[5], shortUrl.getHost());
            Assert.assertEquals(testCase[6], shortUrl.getIpAddress().toString());
            Assert.assertEquals(testCase[7], shortUrl.getPort().toString());
            Assert.assertEquals(testCase[8], shortUrl.getPath());
            Assert.assertEquals(testCase[9], shortUrl.getParam());
            System.out.println();
        }
    }

    @Test
    public void exceptionTest() {
        for (String[] exceptionTestCase : exceptionTestCases) {
            try {
                autoIncrementShortUrlService.parseUrl(exceptionTestCase[1]);
                Assert.fail("the service should produce exception but not");
            } catch (UrlNotValidException e) {
                System.out.printf("successfully produce exception, code:%d, message:%s", e.getCode(), e.getMessage());
            }
        }
    }
}
