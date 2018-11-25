package com.spawpaw.shorturl.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "short_url")
@Data
@NoArgsConstructor
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;
    /**
     * 不包含域名的短编码
     * 例如 aWdF  ,而不是https://s.neko.chat/aWdF
     */
    @Column(name = "short_url", unique = true)
    private String shortUrl;
    @Column(name = "raw_url", unique = true)
    private String rawUrl;
    @Column(name = "protocol")
    private String protocol;

    /**
     * 如果为ftp协议，地址中的用户名
     */
    @Column(name = "ftp_username")
    private String ftpUsername;
    /**
     * 如果为ftp协议，地址中的密码
     */
    @Column(name = "ftp_password")
    private String ftpPassword;

    @Column(name = "host")
    private String host;
    @Column(name = "ip_address")
    private Boolean ipAddress;

    @Column(name = "port")
    private Integer port;

    @Column(name = "path")
    private String path;

    @Column(name = "param")
    private String param;


}
