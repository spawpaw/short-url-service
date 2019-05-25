package com.spawpaw.shorturl;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class Config {
    // <protocol>://<host>[:<port>][/[url]][?[<key>=<value>][&...]]
    /**
     * 支持的协议
     */
    @Value("${customProps.availableProtocols}")
    public List<String> availableProtocols;
    /**
     * 当不存在协议（无://）时自动添加的协议
     */
    @Value("${customProps.defaultProtocol}")
    public String defaultProtocol;
    @Value("${customProps.checkUrlMaxLength}")
    public Boolean checkUrlMaxLength;

    @Value("${customProps.maxUrlLength}")
    public Integer maxUrlLength;

    /**
     * 是否允许使用IP地址作为域名
     */
    @Value("${customProps.allowIpAddress}")
    public Boolean allowIpAddress;
    @Value("${customProps.allowServerPort}")
    public Boolean allowServerPort;
    @Value("${customProps.checkHostFormat}")
    public Boolean checkHostFormat;
    @Value("${customProps.hostFormatPattern}")
    public String hostFormatPattern;


    /**
     * 是否检查域名在禁止列表
     */
    @Value("${customProps.checkDomain}")
    public Boolean checkDomain;
    /**
     * 域名禁止列表
     */
    @Value("${customProps.blockedDomains}")
    public List<String> blockedDomains;

    /**
     * 是否仅允许在白名单内的顶级域名生成短链接
     */
    @Value("${customProps.checkAvailableTopLevelDomains}")
    public Boolean checkAvailableTopLevelDomains;
    /**
     * 允许生成短链的顶级域名
     */
    @Value("${customProps.availableTopLevelDomains}")
    public List<String> availableTopLevelDomains;


    /**
     * 当用户输入的网址没有匹配的协议时自动添加的协议
     */
    @Value("${customProps.appendProtocolIfEmpty}")
    public Boolean appendProtocolIfEmpty;
    /**
     * 是否检查url中的协议在允许列表中
     */
    @Value("${customProps.checkProtocols}")
    public Boolean checkProtocols;


    @Value("${customProps.shortUrlHost}")
    public String shortUrlHost;

    /**
     * 开始编码的url长度，
     */
    @Value("${customProps.preferStartLength}")
    public Integer preferStartLength;

    /////////////////////////////////////////////////////////////////////////////////////////////////////other config
    /**
     * 网站备案号
     */
    @Value("${customProps.icp}")
    public String icp;
}
