package marchsoft.modules.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import marchsoft.base.BasicModel;
import marchsoft.base.BasicServiceImpl;
import marchsoft.base.IBasicService;

import java.io.File;
import java.util.Scanner;

/**
 * 功能描述：mybatis-plus的代码生成器<p>
 * modify @RenShiWei 2020/12/5 description:进行代码生成的整体优化
 * <p>
 * <Strong>使用参考博客地址：</Strong>
 * <a href="https://blog.csdn.net/qq_42937522/article/details/110725251">自定义深度定制人性化的MybatisPlus的代码生成策略</a>
 *
 * @author RenShiWei
 * Date: 2020/7/5 15:16
 */
public class MybatisPlusGenerator {

    /** 数据库连接URL */
    private final static String URL = "jdbc:log4jdbc:mysql://127.0.0.1:3306/smpe?" +
            "serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
    /** 数据库驱动名 */
    private final static String DRIVER_NAME = "com.alibaba.druid.pool.DruidDataSource";
    /** 数据库账号 */
    private final static String USERNAME = "root";
    /** 数据库密码 */
    private final static String PASSWORD = "159357asd";
    /** 控制台输入内容n（大小写都可） */
    private final static String NO = "n";
    /** 控制台输入内容y（大小写都可） */
    private final static String YES = "y";

    /**
     * description:执行MybatisPlus代码生成
     *
     * @author RenShiWei
     * Date: 2020/12/5 17:46
     */
    public static void main(String[] args) {
        //实例化以下配置（目的：按照以下的代码顺序执行，方便书写步骤）
        GlobalConfig globalConfig = setGlobalConfig();
        DataSourceConfig dataSourceConfig = setDataSourceConfig();
        PackageConfig packageConfig = setPackageConfig();
        StrategyConfig strategyConfig = setStrategyConfig();
        TemplateConfig templateConfig = setTemplateConfig();

        // 代码生成器（将配置设置进代码生成器）
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setPackageInfo(packageConfig)
                .setStrategy(strategyConfig)
                .setTemplate(templateConfig)
                // 设置使用freemarker生成代码，如果使用velocity则可以不用设置
                .setTemplateEngine(new FreemarkerTemplateEngine());
        // 执行
        mpg.execute();
    }


    /**
     * description:读取控制台内容，并返回
     *
     * @param tip 输入内容
     * @return 控制台输入内容
     * @author RenShiWei
     * Date: 2020/12/5 17:29
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (! "".equals(ipt) && ipt != null) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * description:进行MybatisPlus代码生成的全局配置
     *
     * @return GlobalConfig
     * @author RenShiWei
     * Date: 2020/12/5 17:36
     */
    private static GlobalConfig setGlobalConfig() {
        System.out.println("------步骤一：确定代码生成位置及作者信息------");
        GlobalConfig gc = new GlobalConfig();
        String path = "";
        // 因为所有在java.io中的类都是将相对路径名解释为起始于用户的当前工作目录，所以应该清楚当前的目录。
        String isSrc = scanner("1.1：是否在项目根目录下生成代码（y为是，n为否）。多模块开发，要在子模块下生成代码请输入n").toLowerCase();
        if (YES.equals(isSrc)) {
            path = System.getProperty("user.dir");
        } else if (NO.equals(isSrc)) {
            path = scanner("1.2：在项目指定模块下生成代码，请输入模块名");
        } else {
            throw new RuntimeException("【自动生成代码错误】输入错误！！！请输入y或者n");
        }

        String author = scanner("1.3：作者名（方便生成注解及作者相关）");

        // 获取指定项目模块的路径，用于输出路径
        File file = new File(path);
        String projectPath = file.getAbsolutePath();
        gc.setOutputDir(projectPath + "/src/main/java")
                .setAuthor(author)
                // 是否打开输出目录
                .setOpen(false)
                // 主键策略
                .setIdType(IdType.AUTO)
                // 是否覆盖已有文件 （默认false）
                .setFileOverride(false)
                // 设置支持ActiveRecord 实体类继承Model
                .setActiveRecord(true)
                // 实体属性 Swagger2 注解
                .setSwagger2(true);

        return gc;
    }

    /**
     * description:进行MybatisPlus代码生成的数据源配置
     *
     * @return DataSourceConfig
     * @author RenShiWei
     * Date: 2020/12/5 17:36
     */
    private static DataSourceConfig setDataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL)
                .setUrl(URL)
                .setDriverName(DRIVER_NAME)
                .setUsername(USERNAME)
                .setPassword(PASSWORD);

        return dsc;
    }

    /**
     * description:进行MybatisPlus代码生成的包配置
     *
     * @return PackageConfig
     * @author RenShiWei
     * Date: 2020/12/5 17:36
     */
    private static PackageConfig setPackageConfig() {
        PackageConfig pc = new PackageConfig();
        String parentPath = scanner("1.4：代码生成的父级包名称（全路径）");
        String sonPath = scanner("1.5：生成在父级包下的指定子包名称（输入n代表直接在父级包下生成）").toLowerCase();
        pc.setParent(parentPath);
        if (! NO.equals(sonPath)) {
            pc.setModuleName(sonPath);
        }

        return pc;
    }

    /**
     * description:进行MybatisPlus代码生成的策略配置
     *
     * @return StrategyConfig
     * @author RenShiWei
     * Date: 2020/12/5 17:36
     */
    private static StrategyConfig setStrategyConfig() {
        System.out.println("------步骤二：确定以哪些数据库的表进行代码生成------");
        StrategyConfig strategy = new StrategyConfig();

        String isAllTable = scanner("2.1：是否选择所有数据库的表（请输入y/n）").toLowerCase();
        if (YES.equals(isAllTable)) {
            System.out.println("即将以数据库全表生成代码...");
        } else if (NO.equals(isAllTable)) {
            //指定策略选择数据库表
            String inOrExcludeTable = scanner("2.2：y为输入生成数据库的表，n为输入排除数据库的表").toLowerCase();
            String[] tableNames = scanner("2.3：请输入生成或者排除数据库表的名称，多个用英文状态下的','分割").split(",");
            // 当enableSqlFilter为false时，允许正则表达式
            if (YES.equals(inOrExcludeTable)) {
                strategy.setInclude(tableNames);
            } else if (NO.equals(inOrExcludeTable)) {
                strategy.setExclude(tableNames);
            } else {
                throw new RuntimeException("【自动生成代码错误】输入错误！！！请输入y或者n");
            }
        } else {
            throw new RuntimeException("【自动生成代码错误】输入错误！！！请输入y或者n");
        }

        strategy
                // 数据库表映射到实体的命名策略（下划线处理）
                .setNaming(NamingStrategy.underline_to_camel)
                // 数据库字段映射到实体的命名策略（下划线处理）
                .setColumnNaming(NamingStrategy.underline_to_camel)
                // 全局大写命名
                .setCapitalMode(true)
                // 是否为lombok模型（默认 false）
                .setEntityLombokModel(true)
                // 是否为链式模型（默认 false）
                .setChainModel(true)
                // Boolean类型字段是否移除is前缀（默认 false）
                .setEntityBooleanColumnRemoveIsPrefix(true)
                .setRestControllerStyle(true)
                /*
                    设置父类策略
                 */
                // 设置实体类的父类
                .setSuperEntityClass(BasicModel.class)
                //设置写入实体父类的字段
//                .setSuperEntityColumns("create_time", "update_time", "create_by", "update_by", "is_deleted")
                .setSuperMapperClass("marchsoft.base.BasicMapper")
                .setSuperServiceClass(IBasicService.class)
                .setSuperServiceImplClass(BasicServiceImpl.class);

        return strategy;
    }

    /**
     * description:进行MybatisPlus代码生成的模板配置
     *
     * @return TemplateConfig
     * @author RenShiWei
     * Date: 2020/12/5 17:36
     */
    private static TemplateConfig setTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板（如果不配置这些，则会按照官方的配置进行生成）
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig
                .setController("template/controller.java")
                .setMapper("template/Mapper.java")
                .setServiceImpl("template/ServiceImpl.java")
                .setXml(null);

        return templateConfig;
    }

    /**
     * description:进行MybatisPlus代码生成的自定义配置（暂未使用）
     *
     * @return TemplateConfig
     * @author RenShiWei
     * Date: 2020/12/5 17:36
     */
    private static InjectionConfig setInjectionConfig() {

        // 自定义配置
        return new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

    }

}
