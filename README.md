# RDSP微服务

## 模块规划
```lua
rdsp-cloud -- 主POM
│  ├─api-server -- 接口服务POM
│  │  ├─api-commons -- Api工具类
│  │  ├─api-provider-- 接口服务的提供者
│  │  ├─api-auth -- 接口授权、鉴权模块[端口] 
│  │─rdsp-gateway -- 网关模块
│  ├─rdsp-cloud-server -- RDSP后台主POM
│  │  ├─xxx -- xxx 模块一[端口]
│  │  ├─xxx -- xxx 模块二[端口]
│  │  ├─xxx -- xxx 模块三[端口]
│  │  ├─xxx -- xxx 模块四[端口]
│  │  ├─xxx -- xxx 模块五[端口]
│  │  ├─xxx -- xxx 模块六[端口]
│  │  ├─xxx -- xxx 模块七[端口]
│  │  ├─xxx -- xxx 模块八[端口]
```