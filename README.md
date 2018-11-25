# 短Url生成器 （Short Url Generator）


## 使用
在线演示: [http://s.neko.chat](http://s.neko.chat)
### 配置
在`src/main/resources/application.yml`目录下配置数据源、服务域名、url过滤规则等。
注意：数据库使用mysql，需要先在数据库中创建名为`short_url`的数据库
### 运行
直接运行`com.spawpaw.shorturl.ShortUrlServiceApplication`  
或使用`mvn package` 打包成jar包后运行


### 通过网页访问
默认情况下直接访问`localhost:8080`即可

### 使用接口调用
#### 创建短链接请求

`POST` `/api/create`
```json
{
  "url":"https://github.com/spawpaw"
}
```

#### 解析短链接请求
`POST` `/api/parse`
```json
{
  "shortUrl":"短编码 或者 网址，如 http://s.neko.chat/a"
}
```

#### 响应
两种请求的响应是一样的
```json
{
    "code": 0,
    "msg": "OK",
    "shortUrl": "http://s.neko.chat/a",
    "longUrl": "https://github.com/spawpaw"
}
```
**错误代码：**
com.spawpaw.shorturl.exception.ErrorCode: 

|               code                | des |
| :-------------------------------- | :-- |
| OK                                |    |
| ERR_UNKNOWN                       |    |  
| ERR_SHORT_URL_NOT_EXIST           |    |              
| ERR_SHORT_URL_CANNOT_BE_NULL      |    |                  
| ERR_INVALID_REQUEST_PARAM         |    |              
| ERR_INVALID_URL                   |    |      
| ERR_INVALID_FTP_AUTH              |    |          
| ERR_INVALID_SERVER_PORT           |    |              
| ERR_INVALID_HOST_FORMAT           |    |              
| ERR_CANNOT_CUSTOMIZE_SERVER_PORT  |    |                      
| ERR_CANNOT_USE_IP_ADDRESS_AS_HOST |    |                      
| ERR_BLOCKED_PROTOCOL              |    |          
| ERR_BLOCKED_TOP_LEVEL_DOMAIN      |    |                  
| ERR_BLOCKED_DOMAIN                |    |      


## 原理


### 基于自增序列的生成
url允许的字符为[a-zA-Z0-9]，可通过这些字符构造一个62进制的数字。
这样就可以直接将短url的id转化为短编码，优点是生成速度快，无重复，缺点是容易被猜出来规律。
### 基于hash值的生成
为url生成hash，然后截取其中一些位来当作其短编码，这种方式的缺点是容易产生碰撞（短编码越短越容易碰撞）

