# WeBot 
 
## 项目描述

一个微信机器人单元，基于android的xposed框架 hook 微信app 实现机器人功能


## 功能实现

- [x] 消息回调
- [x] 自动回复消息
- [x] 新增好友回调
- [x] 默认同意新增好友
- [x] 自动回复(文字,表情)

## 运行

下载[最新版](https://github.com/546669204/RebateBot/releases)
并安装对应版本微信

## 开发和构建

## 环境要求

- android-sdk
- android 模拟器或真机

```
git clone https://github.com/546669204/RebateBot.git
use Android-Studio open
```

## 接口描述

本项目需要一个接受处理请求服务器建立:188端口接受回调  
tcp 协议

固定协议头 + 8字节消息长度 + 固定协议尾  
样例：`{28285252_hcaiyue_top_15159898}       	HeartBoom{137621_woyaoyigebaojieshula_159687}`
 
消息回调样例  
`{method:"msgprocess",data:"{takler:\"weid\",content:\"content\",type:\"text\"}"}`  
content == "newfriend" 为新增好友回调

发送消息样例  
`{method:"sendtextmsg",data:"{to:\"weid\",content:\"content\"}"}`


详细调用方法可以阅读项目 [RebateBot](https://github.com/546669204/RebateBot)

## 更新日志 

2018-08-13   
init