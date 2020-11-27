package marchsoft.modules.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.Scanner;

/**
 * 功能描述：mybatis-plus的代码生成器
 *
 * @author RenShiWei
 * Date: 2020/7/5 15:16
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        // 代表yes或者no
        final String no = "n";
        final String yes = "y";

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String path = "";
        // 因为所有在java.io中的类都是将相对路径名解释为起始于用户的当前工作目录，所以应该清楚当前的目录。
        String isSrc = scanner("是否在项目根目录下生成代码（y为是，n为否）。多模块开发，要在子模块下生成代码请输入n").toLowerCase();
        if (yes.equals(isSrc)) {
            path = System.getProperty("user.dir");
        } else if (no.equals(isSrc)) {
            path = scanner("在项目指定模块下生成代码，请输入模块名");
        } else {
            throw new RuntimeException("【自动生成代码错误】输入错误！！！请输入y或者n");
        }

        // 获取指定项目模块的路径，用于输出路径
        File file = new File(path);
        String projectPath = file.getAbsolutePath();
        gc.setOutputDir(projectPath + "/src/main/java")
                .setAuthor("RenShiWei")
                // 是否打开输出目录
                .setOpen(false)
                // 主键策略
                .setIdType(IdType.AUTO)
                // 是否覆盖已有文件 （默认false）
                .setFileOverride(false)
                // 设置支持ActiveRecord 继承Model
                .setActiveRecord(true)
                // 实体属性 Swagger2 注解
                .setSwagger2(true);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL)
                .setUrl(
                        "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false"
                                + "&allowMultiQueries=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai"
                                + "&rewriteBatchedStatements=true")
                .setDriverName("com.mysql.jdbc.Driver")
                .setUsername("root")
                .setPassword("159357asd");

        // 包配置
        PackageConfig pc = new PackageConfig();
        String parentPath = scanner("请输入代码生成的父级包名称");
        String sonPath = scanner("请输入生成在父级包下的指定子包名称（输入n代表直接在父级包下生成）").toLowerCase();
        pc.setParent(parentPath);
        if (! no.equals(sonPath)) {
            pc.setModuleName(sonPath);
        }

        // 自定义配置
        InjectionConfig cfg =
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                        // to do nothing
                    }
                };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";

        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        String inOrExcludeTable = scanner("y为输入生成数据库的表，n为输入排除数据库的表").toLowerCase();
        String[] tableNames = scanner("请输入生成或者排除数据库表的名称，多个用英文状态下的','分割").split(",");
        // 当enableSqlFilter为false时，允许正则表达式
        if (yes.equals(inOrExcludeTable)) {
            strategy.setInclude(tableNames);
        } else if (no.equals(inOrExcludeTable)) {
            strategy.setExclude(tableNames);
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
                .setRestControllerStyle(true);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板（如果不配置这些，则会按照官方的配置进行生成）
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig
                .setController("template/controller.java")
                .setMapper("template/Mapper.java")
                .setServiceImpl("template/ServiceImpl.java")
                .setXml(null);

        // 代码生成器（将配置设置进代码生成器）
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(gc)
                .setDataSource(dsc)
                .setStrategy(strategy)
                // 设置使用freemarker生成代码，如果使用velocity则可以不用设置
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .setTemplate(templateConfig)
                .setPackageInfo(pc);
        // 执行
        mpg.execute();
    }

    /** 读取控制台内容 */
    public static String scanner(String tip) {
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
}
