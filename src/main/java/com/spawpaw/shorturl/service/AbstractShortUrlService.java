package com.spawpaw.shorturl.service;

import com.spawpaw.shorturl.Config;
import com.spawpaw.shorturl.entity.ShortUrl;
import com.spawpaw.shorturl.exception.ErrorCode;
import com.spawpaw.shorturl.exception.ParseExceprion;
import com.spawpaw.shorturl.exception.UrlNotValidException;
import lombok.extern.java.Log;

import javax.annotation.Resource;

@Log
public abstract class AbstractShortUrlService implements ShortUrlService {
    public static final String PROTOCOL_MATCHER = "^[a-zA-Z0-9]+://.+";//用于匹配协议的正则式
    public static final String FTP_AUTH_INFO_MATCHER = "";
    /**
     * a-z,A-Z,0-9, - , _, 共26+26+1+1=64个字符
     */
    protected static final char[] allowedCharacters = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };
    static protected final long[] pow64 = new long[]{1L, 1 << 6, 1L << 12, 1L << 18, 1L << 24, 1L << 30, 1L << 36, 1L << 42, 1L << 48, 1L << 54, 1L << 60};
    static protected final long[] pow64Sum = new long[]{(1L << 2) - 1, (1L << 7) - 1, (1L << 13) - 1, (1L << 19) - 1, (1L << 25) - 1, (1L << 31) - 1, (1L << 37) - 1, (1L << 43) - 1, (1L << 49) - 1, (1L << 55) - 1, (1L << 61) - 1};
    @Resource
    protected Config config;

    /**
     * 根据已知信息还原整个url
     */
    public static String parseFullUrl(ShortUrl shortUrl) {
        //特殊协议
        if ("magnet".equals(shortUrl.getProtocol())) {
            return "magnet:?" + shortUrl.getPath();
        }


        String result = "";
        if (shortUrl.getProtocol() != null && !shortUrl.getProtocol().isEmpty()) {
            if ("//".equals(shortUrl.getProtocol())) {
                result += "//";
            } else
                result += shortUrl.getProtocol() + "://";
        }
        if ("ftp".equals(shortUrl.getProtocol())) {
            if (shortUrl.getFtpUsername() != null && !shortUrl.getFtpUsername().isEmpty()) {
                result += shortUrl.getFtpUsername();
                if (shortUrl.getFtpPassword() != null && !shortUrl.getFtpPassword().isEmpty()) {
                    result += ":" + shortUrl.getFtpPassword();
                }
                result += "@";
            }
        }
        if (shortUrl.getHost() != null && !shortUrl.getHost().isEmpty())
            result += shortUrl.getHost();
        if (shortUrl.getPort() != null && !shortUrl.getPort().equals(-1)) {
            String[] protocols = new String[]{"http", "https"};
            Integer[] ports = new Integer[]{80, 443};
            //省略常用协议端口号
            boolean canAbbrPort = false;
            for (int i = 0; i < protocols.length; i++)
                if (protocols[i].equals(shortUrl.getProtocol()) && ports[i].equals(shortUrl.getPort()))
                    canAbbrPort = true;
            if (!canAbbrPort)
                result += ":" + shortUrl.getPort();
        }
        if (shortUrl.getPath() != null && !shortUrl.getPath().isEmpty())
            result += shortUrl.getPath();
        if (shortUrl.getParam() != null && !shortUrl.getParam().isEmpty())
            result += "?" + shortUrl.getParam();

        return result;
    }

    /**
     * 将数字转换为短码
     *
     * @param id 数字
     * @return 短码，如1 为a，2为b
     */
    public String translateNumberToEncodedString(long id) {
        //n进制问题
        StringBuilder result = new StringBuilder();
        int len = allowedCharacters.length;
        while (id > 0) {
            result.insert(0, allowedCharacters[(int) (id % len)]);
            id /= len;
        }

        return result.toString();
    }

    public long translateEncodedStringToNumber(String code) {
        if (code == null || code.isEmpty())
            throw new ParseExceprion(ErrorCode.ERR_SHORT_URL_CANNOT_BE_NULL);
        if (code.startsWith(config.shortUrlHost)) {
            code = code.substring(config.shortUrlHost.length());
        }
        //n进制问题
        long result = 0;
        char[] chars = code.toCharArray();
        String s = new String(allowedCharacters);
        log.info("code:" + code);
        for (int i = 0; i < chars.length; i++) {
            int n = s.indexOf(chars[i]);
            if (n < 0) throw new IllegalArgumentException("contains illegal character");
            result *= 10;
            result += n;
        }
        return result;
    }

    public ShortUrl parseUrl(String url) {
        if (url == null || url.isEmpty())
            throw new UrlNotValidException(ErrorCode.ERR_INVALID_URL, "url cannot be empty");
        ShortUrl shortUrl = new ShortUrl();

        // : 2018/11/23 解析协议
        if (url.matches(PROTOCOL_MATCHER)) {
            shortUrl.setProtocol(url.substring(0, url.indexOf("://")).toLowerCase());
            url = url.substring(url.indexOf("://") + 3);
        }

        //尝试解析特殊协议
        if (shortUrl.getProtocol() == null || shortUrl.getProtocol().isEmpty()) {
            if (url.startsWith("//")) {//绝对定位
                shortUrl.setProtocol("//");
                url = url.substring(2);
            }
            if (url.startsWith("magnet:?") && url.length() > 10) {//磁力链
                shortUrl.setProtocol("magnet");
                shortUrl.setPath(url.substring(8));
                url = "";
            }
        }

        //如果协议的内容不是 host:port/path?param 格式，需要在此行注释之前处理掉，并将url置为空

        //如果协议为空且需要补全
        if ((shortUrl.getProtocol() == null || shortUrl.getProtocol().isEmpty()) && config.appendProtocolIfEmpty) {
            shortUrl.setProtocol(config.defaultProtocol);
        }

        // : 2018/11/23 如果为ftp协议，则先解析是否有用户名和密码
        if ("ftp".equals(shortUrl.getProtocol()) && url.matches("^[a-zA-Z0-9]+:{0,1}[a-zA-Z0-9]+@.+")) {
            String substring = url.substring(0, url.indexOf("@"));
            String[] split = substring.split(":");
            if (substring.isEmpty() || split.length > 2 || split.length == 0)
                throw new UrlNotValidException(ErrorCode.ERR_INVALID_FTP_AUTH, "ftp url format should be: ftp://username:password@host/file");
            shortUrl.setFtpUsername(split[0]);
            if (split.length > 1)
                shortUrl.setFtpPassword(split[1]);
            else shortUrl.setFtpPassword("");
            url = url.substring(url.indexOf("@") + 1);
        } else {
            shortUrl.setFtpUsername("");
            shortUrl.setFtpPassword("");
        }


        // : 2018/11/23 解析域名和端口号, 第一个/前，且为合法域名格式
        String host;
        Integer port = -1;
        if (url.contains("/")) {
            host = url.substring(0, url.indexOf("/"));
            url = url.substring(url.indexOf("/"));
        } else {
            host = url;
            url = "";
        }
        if (host.contains(":")) {
            String[] split = host.split(":");
            if (!split[1].matches("^[1-9][0-9]+$")) {
                throw new UrlNotValidException(ErrorCode.ERR_INVALID_SERVER_PORT, "server port is not valid, port should be number in oct.");
            }
            if (split[1].length() > 6 || Integer.valueOf(split[1]) > 65535 || Integer.valueOf(split[1]) <= 0)
                throw new UrlNotValidException(ErrorCode.ERR_INVALID_SERVER_PORT, "server port is not valid, port number out of range(0-65535)");
            host = split[0];
            port = Integer.valueOf(split[1]);
        }
        // : 2018/11/24 检查域名是否合法
        shortUrl.setHost(host);
        shortUrl.setPort(port);


        // : 2018/11/23 判断域名是否为ip地址
        boolean isIpAddress = false;
        if (shortUrl.getHost().matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")) {
            String[] split = shortUrl.getHost().split("\\.");
            if ((Integer.valueOf(split[0]) >= 0 && Integer.valueOf(split[0]) <= 255)
                    || (Integer.valueOf(split[1]) >= 0 && Integer.valueOf(split[1]) <= 255)
                    || (Integer.valueOf(split[2]) >= 0 && Integer.valueOf(split[2]) <= 255)
                    || (Integer.valueOf(split[3]) >= 0 && Integer.valueOf(split[3]) <= 255)
            ) isIpAddress = true;
        }
        shortUrl.setIpAddress(isIpAddress);

        // : 2018/11/23 解析地址（包含文件）
        if (url.contains("?")) {
            shortUrl.setPath(url.substring(0, url.indexOf("?")));
            url = url.substring(url.indexOf("?") + 1);
            // : 2018/11/23 解析参数
            shortUrl.setParam(url);
        } else {
            if (shortUrl.getPath() == null)
                shortUrl.setPath(url);
            shortUrl.setParam("");
        }

        shortUrl.setRawUrl(parseFullUrl(shortUrl));

        checkUrl(shortUrl);

        return shortUrl;
    }

    /**
     * 检查url是否符合配置要求
     */
    protected void checkUrl(ShortUrl shortUrl) {
        if (config.checkProtocols) {
            boolean isProtocolValid = false;
            for (String availableProtocol : config.availableProtocols) {
                if (availableProtocol.equals(shortUrl.getProtocol())) {
                    isProtocolValid = true;
                    break;
                }
            }
            if (!isProtocolValid) {
                throw new UrlNotValidException(ErrorCode.ERR_BLOCKED_PROTOCOL, "ERR_BLOCKED_PROTOCOL");
            }
        }
        if ("magnet".equals(shortUrl.getProtocol())) {//特殊协议免检
            return;
        }
        if (config.checkDomain) {
            for (String blockedDomain : config.blockedDomains) {
                if (blockedDomain.equals(shortUrl.getHost()))
                    throw new UrlNotValidException(ErrorCode.ERR_BLOCKED_DOMAIN, "ERR_BLOCKED_DOMAIN");
            }
        }
        if (!config.allowIpAddress && shortUrl.getIpAddress())
            throw new UrlNotValidException(ErrorCode.ERR_CANNOT_USE_IP_ADDRESS_AS_HOST, "ERR_CANNOT_USE_IP_ADDRESS_AS_HOST");
        if (config.checkHostFormat) {
            if (!shortUrl.getHost().matches(config.hostFormatPattern)) {
                throw new UrlNotValidException(ErrorCode.ERR_INVALID_HOST_FORMAT);
            }
        }
        if (config.checkAvailableTopLevelDomains && !shortUrl.getIpAddress()) {
            boolean isTopLevelDomainValid = false;
            for (String availableTopLevelDomain : config.availableTopLevelDomains) {
                if (shortUrl.getHost().endsWith(availableTopLevelDomain)) {
                    isTopLevelDomainValid = true;
                    break;
                }
            }
            if (!isTopLevelDomainValid)
                throw new UrlNotValidException(ErrorCode.ERR_BLOCKED_TOP_LEVEL_DOMAIN, "ERR_BLOCKED_TOP_LEVEL_DOMAIN");
        }
        if (!config.allowServerPort && !shortUrl.getPort().equals(-1))
            throw new UrlNotValidException(ErrorCode.ERR_CANNOT_CUSTOMIZE_SERVER_PORT, "ERR_CANNOT_CUSTOMIZE_SERVER_PORT");

    }

}
