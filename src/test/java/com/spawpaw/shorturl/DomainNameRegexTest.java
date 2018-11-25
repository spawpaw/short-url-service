package com.spawpaw.shorturl;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * 测试用于匹配域名的正则式
 */
public class DomainNameRegexTest {

    private static final String MATCH_PATTERN = "^([a-zA-Z0-9]+[.])+[a-zA-Z0-9]+$";

    private static final String[][] testCases = new String[][]{
            {"true", "example.com"},
            {"true", "www.example.com"},
            {"true", "192.168.1.1"},
            {"false", "www..example.com"},
            {"false", "www..example..com"},
            {"false", "example.com."},
            {"false", ".example.com"},
    };

    @Test
    public void test() {
        for (String[] testCase : testCases) {
            System.out.println("test case: " + Arrays.toString(testCase));
            Assert.assertEquals(testCase[0], String.valueOf(testCase[1].matches(MATCH_PATTERN)));
        }
    }


}
