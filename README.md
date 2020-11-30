# SMPE-ADMIN后台管理系统

# 项目简介
一个基于[EL-ADMIN](https://el-admin.vip/)、Spring Boot 2.1.0 、 Mybatis Plus、JWT + Spring Security、Redis、Vue的前后端分离的后台管理系统

**开发文档：**  待完善
**默认管理员账号密码：** `admin / 123456`

## 项目源码

|     |   后端源码  |   前端源码  |
|---  |--- | --- |
|  GitHub  | https://github.com/shiwei-Ren/smpe-admin |   https://github.com/shiwei-Ren/smpe-admin-web  |

## 主要特性
- 使用最新技术栈，社区资源丰富。
- 支持接口限流，避免恶意请求导致服务层压力过大
- 支持接口级别的功能权限与数据权限，可自定义操作
- 自定义权限注解与匿名接口注解，可快速对接口拦截与放行
- 前后端统一异常拦截处理，统一输出异常，避免繁琐的判断

##  系统功能
- 用户管理：提供用户的相关配置，新增用户后，默认密码为123456
- 角色管理：对权限与菜单进行分配，可根据部门设置角色的数据权限
- 菜单管理：已实现菜单动态路由，后端可配置化，支持多级菜单
- 部门管理：可配置系统组织架构，树形表格展示
- 岗位管理：配置各个部门的职位

## 项目结构
项目采用按功能分模块的开发方式，结构如下

- `smpe-common` 为系统的公共模块，各种工具类，公共配置存在该模块
- `smpe-system` 为系统核心模块也是项目入口模块，也是最终需要打包部署的模块

## 详细结构

```
- smpe-common 公共模块
    - annotation 为系统自定义注解
    - aspect 自定义注解的切面
    - base 提供了常用基类
    - bean 读取yml中的通用配置类
    - config 全局配置文件，例如swagger、mybatisplus、redis、跨域处理等的配置
        - thread 线程池相关
    - enums 全局枚举类
    - exception 项目统一异常的处理
    - response 统一返回前端数据封装
    - utils 系统通用工具类
- smpe-system 系统核心模块（系统启动入口）
    - config 核心模块配置（非全局配置）
    - modules 系统相关模块(登录授权、用户部门管理等、自定义业务)
        - business 业务模块（一般项目业务开发模块可放在此包下，各模块可构建自己的config、utils、enums等）
        - generator mpbatisplus的代码生成（后端）
        - security 安全认证（SpringSecurity+JWT）
        - system 系统核心模块（用户、角色、部门、岗位、菜单管理等）
        - upload 文件模块（上传、下载等）
    - utils 核心模块工具类
- smpe-xxx （自定义待开发模块）
- sql 数据库文件
- Dockerfile 构建后端服务器环境的Dockerfile（基于docker）
- smpe-admin.sh 后端部署脚本
- smpe-admin.conf nginx配置文件

```

# 使用指南

## 常用技术参考文档

1. 后端持久层框架使用MybatisPlus，参考：[MybatisPlus官方文档](https://hutool.cn/docs/#/)
2. Java轻量级开发工具包HuTool，参考：[HuTool官方文档](https://hutool.cn/docs/#/)。*暂时不使用hutool-json*
3. lombok，参考：
4. Java 实体映射工具——MapStruct，参考：[SpringBoot使用MapStruct自动映射DTO](https://www.jianshu.com/p/3f20ca1a93b0)
5. FastJson（阿里巴巴），参考[Fastjson 简明教程](https://www.runoob.com/w3cnote/fastjson-intro.html)
6. 待完善...

- **开发流程**
  - 数据库新建表之后，使用MybatisPlusGenerator在business下生成相应文件。
  - 之后接口开发和往常相同，接口需要权限则加上@PreAuthorize("@smpe.check('xxx')")；
  
## 服务器部署
参考：
1. [docker安装及docker常用命令](https://blog.csdn.net/qq_42937522/article/details/106274293)
2. [docker 构建git+maven+jdk8的centos7环境，实现轻量级的springboot项目的自动化部署](https://blog.csdn.net/qq_42937522/article/details/107755941)
3. [docker安装nginx规范所有项目的反向代理(一个项目一个反向代理的conf配置文件)](https://blog.csdn.net/qq_42937522/article/details/108179441)
4. [docker 构建centos7+git+nvm镜像，实现自主切换node版本统一部署前端vue项目](https://blog.csdn.net/qq_42937522/article/details/108702775)

# 鸣谢
- [@三月软件](http://www.marchsoft.cn/)提供技术支持
- 主要参考[EL-ADMIN](https://el-admin.vip/)
- 由[@shiwei-Ren](https://github.com/shiwei-Ren)、[@jiaoqianjin](https://github.com/jiaoqianjin)、[@pangyv](https://github.com/pangyv)、[@jie-fei30](https://github.com/jie-fei30)参与初版的开发维护

