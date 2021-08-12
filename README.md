# SMPE-ADMIN后台管理系统

# 项目简介
一个基于Spring Boot 2.1.0、JDK1.8+ 、 Mybatis Plus、JWT + Spring Security、Redis、Vue的前后端分离的后台管理系统

**[官方文档](https://sanyueruanjian.github.io/smpe-admin-doc/)**  

**默认管理员账号密码：** `admin / 123456`

## 项目源码

|     |   后端源码  |   前端源码  |
|---  |--- | --- |
|  GitHub  | https://github.com/sanyueruanjian/smpe-admin |   https://github.com/sanyueruanjian/smpe-admin-web  |

## 主要特性
- 使用最新技术栈，社区资源丰富。
- 支持接口限流，避免恶意请求导致服务层压力过大
- 支持接口级别的功能权限与数据权限，可自定义操作
- 自定义权限注解与匿名接口注解，可快速对接口拦截与放行
- 前后端统一异常拦截处理，统一输出异常，避免繁琐的判断
- 自定义扩展Mybatis-Plus的功能
- 高效率开发，代码生成器可一键生成前后端代码
- 完善的日志记录体系简单注解即可实现


##  系统功能
- 用户管理：提供用户的相关配置，新增用户后，默认密码为123456
- 角色管理：对权限与菜单进行分配，可根据部门设置角色的数据权限
- 菜单管理：已实现菜单动态路由，后端可配置化，支持多级菜单
- 部门管理：可配置系统组织架构，树形表格展示
- 岗位管理：配置各个部门的职位
- 任务调度：管理定时任务

## 系统监控
- 在线用户：记录登陆系统的用户
- 操作日志：记录用户的操作情况
- 异常日志：记录用户的异常操作情况

## 项目结构
项目采用按功能分模块的开发方式，结构如下

- `smpe-common` 为系统的公共模块，各种工具类，公共配置存在该模块
- `smpe-system` 为系统核心模块也是项目入口模块，也是最终需要打包部署的模块
- `smpe-log` 为日志模块

## 详细结构

```
- smpe-common 公共模块
    - annotation 为系统自定义注解
    - aspect 自定义注解的切面
    - base 提供了常用基类
    - bean 读取yml中的通用配置类
    - config 全局配置文件，例如swagger、MyBatis-Plus、redis、跨域处理等的配置
        - thread 线程池相关
    - enums 全局枚举类
    - exception 项目统一异常的处理
    - response 统一返回前端数据封装
    - utils 系统通用工具类
- smpe-log 日志模块
    - annotation 日志自定义注解
    - aspect 自定义日志切面
    - controller 日志控制层
    - entity 日志实体
    - enums 日志常用枚举
    - mapper 日志接口
    - service 日志服务
- smpe-system 系统核心模块（系统启动入口）
    - config 核心模块配置（非全局配置）
    - modules 系统相关模块(登录授权、用户部门管理等、自定义业务)
        - business 业务模块（一般项目业务开发模块可放在此包下，各模块可构建自己的config、utils、enums等）
        - generator mpbatisplus的代码生成（后端）
        - security 安全认证（SpringSecurity+JWT）
        - system 系统核心模块（用户、角色、部门、岗位、菜单管理等）（各模块文件夹结构可参考如下）
            - controller
            - entity
                - bo
                - dto
            - mapper
            - service
                - impl
                - mapstruct(Java实体映射文件)
            - config（仅限本模块使用的配置文件，没有可忽略）
            - utils（仅限本模块使用的工具类，没有可忽略）
        - upload 文件模块（上传、下载等）
    - utils 核心模块工具类（仅限smpe-system子工程使用的工具类，非全局使用）
- smpe-xxx （自定义待开发模块）
- sql 数据库文件
- Dockerfile 构建后端服务器环境的Dockerfile（基于docker）
- smpe-admin.sh 后端部署脚本
- smpe-admin.conf nginx配置文件

```


# 使用指南
参考[官方文档](https://sanyueruanjian.github.io/smpe-admin-doc/)

## 服务器部署
参考：
1. [docker安装及docker常用命令](https://blog.csdn.net/qq_42937522/article/details/106274293)
2. [docker 构建git+maven+jdk8的centos7环境，实现轻量级的springboot项目的自动化部署](https://blog.csdn.net/qq_42937522/article/details/107755941)
3. [docker安装nginx规范所有项目的反向代理(一个项目一个反向代理的conf配置文件)](https://blog.csdn.net/qq_42937522/article/details/108179441)
4. [docker 构建centos7+git+nvm镜像，实现自主切换node版本统一部署前端vue项目](https://blog.csdn.net/qq_42937522/article/details/108702775)

# 鸣谢
- 感谢 [EL-ADMIN](https://el-admin.vip/) 开源框架
- 感谢[@RenShiWei](https://github.com/duktig666)、[@jiaoqianjin](https://github.com/jiaoqianjin)、[@pangyv ](https://github.com/pangyv)、[@jie-fei30)](https://github.com/jie-fei30)参与初版的开发维护
- 感谢每一位为 SMPE-ADMIN 做出过贡献的开发者

# 反馈交流
QQ交流群：571506692
