# WeBot
 
## project description

A WeChat robot unit ,based on the android xposed framework hook to implement WeChat app robot functions


## Function realization

- [x] message callback
- [x] auto reply message
- [x] Add friend callback
- [x] Default to add new friends
- [x] Auto Reply (text, emoticon)

## Run

Download [latest version](https://github.com/546669204/wechatbot-xposed/releases)
And install the corresponding version of WeChat

## Development and build

## Environmental requirements

- android-sdk
- android Simulator or real machine

```
Git clone https://github.com/546669204/wechatbot-xposed.git
Use Android-Studio open
```

## Interface Description

This project needs to set up a server to accept requests for processing: port 188 accepts callbacks
Tcp protocol

Fixed protocol header + 8 byte message length + fixed protocol tail
Example: `{28285252_hcaiyue_top_15159898} HeartBoom{137621_woyaoyigebaojieshula_159687}`
 
Message callback sample  
`{method:"msgprocess",data:"{takler:\"weid\",content:\"content\",type:\"text\"}"}`
Content == "newfriend" callback for new friends

Send message sample  
`{method:"sendtextmsg",data:"{to:\"weid\",content:\"content\"}"}`


The detailed calling method can read the project [RebateBot](https://github.com/546669204/RebateBot)

## Update log

2018-08-13
Init
