package com.ctbiyi;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.ctbiyi.generator.core.BiyiFastAutoGenerator;
import com.ctbiyi.generator.core.converts.BiyiMySqlTypeConvert;
import com.ctbiyi.generator.core.engine.MyVelocityTemplateEngine;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * desc
 * 代码生成启动类
 * @author : quankuijin
 * @createTime : 2022/8/16 18:17
 */
public final class GenerateApplication {


    private GenerateApplication() {

    }

    public static final String  PREFIX = "biyi.code.generator.";
    public static void main(String[] args) {
        generator();
    }

    private static void generator() {
        YamlPropertiesFactoryBean yamlMapFactoryBean = new YamlPropertiesFactoryBean();
        yamlMapFactoryBean.setResources(new ClassPathResource("config/application-generator.yml"));
        yamlMapFactoryBean.afterPropertiesSet();
        Properties yamlProperties = yamlMapFactoryBean.getObject();

        List<IFill> filledList = new LinkedList<>();
        filledList.add(new Column("create_time", FieldFill.INSERT));
        filledList.add(new Column("create_user_id", FieldFill.INSERT));
        filledList.add(new Column("create_user", FieldFill.INSERT));
        filledList.add(new Column("create_time", FieldFill.INSERT));
        filledList.add(new Column("create_user_id", FieldFill.INSERT));
        filledList.add(new Column("create_user", FieldFill.INSERT));
        filledList.add(new Column("date", FieldFill.INSERT_UPDATE));
        filledList.add(new Column("update_time", FieldFill.INSERT_UPDATE));
        filledList.add(new Column("update_user_id", FieldFill.INSERT_UPDATE));
        filledList.add(new Column("update_user", FieldFill.INSERT_UPDATE));
        filledList.add(new Column("deleted", FieldFill.INSERT_UPDATE));

        String finalProjectPath = System.getProperty("user.dir");

        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder(yamlProperties.getProperty(PREFIX + "url"), yamlProperties.getProperty(PREFIX + "username"), yamlProperties.getProperty(PREFIX + "password"))
                .typeConvert(new BiyiMySqlTypeConvert(filledList));

        BiyiFastAutoGenerator fastAutoGenerator = BiyiFastAutoGenerator.create(dataSourceConfigBuilder)
                .globalConfig(builder -> {
                    builder.author(yamlProperties.getProperty(PREFIX + "auth")) // 设置作者
                            .disableOpenDir() //禁止打开输出目录
                            .outputDir(finalProjectPath + "/src/main/java") // 指定输出目录
                            .dateType(DateType.ONLY_DATE)
                            .commentDate("yyyy-MM-dd")
                            .fileOverride()
                    ;

                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "swagger"))) {
                        builder.enableSwagger(); // 开启 swagger 模式
                    }
                })
                .packageConfig(builder -> {
                    builder.parent(yamlProperties.getProperty(PREFIX + "packageConfig.parent")) // 设置父包名
                            //.moduleName("test") // 设置父包模块名
                            .entity(yamlProperties.getProperty(PREFIX + "packageConfig.entity")) //设置entity包名
                            .mapper(yamlProperties.getProperty(PREFIX + "packageConfig.mapper"))
                            .other(yamlProperties.getProperty(PREFIX + "packageConfig.other")); // 设置vo包名
                            if(StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "packageConfig.xmlOutput"))) {
                                builder.xml(yamlProperties.getProperty(PREFIX + "packageConfig.xml"))
                                       .pathInfo(Collections.singletonMap(OutputFile.xml, finalProjectPath+"/src/main/java/" + yamlProperties.getProperty(PREFIX + "packageConfig.xmlOutput")))
                        ; // 设置mapperXml生成路径
                    }

                })
                .templateConfig(builder -> {
                    builder
                            .controller(yamlProperties.getProperty(PREFIX + "templateConfig.controller")) // vm模板
                            .entity(yamlProperties.getProperty(PREFIX + "templateConfig.entity")) // vm模板
                            .mapper(yamlProperties.getProperty(PREFIX + "templateConfig.mapper")) // vm模板
                            .xml(yamlProperties.getProperty(PREFIX + "templateConfig.xml")) // vm模板
                            //                            .xml(null)
                            .service(yamlProperties.getProperty(PREFIX + "templateConfig.service")) // vm模板
                            .serviceImpl(yamlProperties.getProperty(PREFIX + "templateConfig.serviceImpl")) // vm模板
                    ;
                })
                .strategyConfig(builder -> {
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.addTableSuffix"))) {
                        builder.addTableSuffix(yamlProperties.getProperty(PREFIX + "addTableSuffix"));
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.addTablePrefix"))) {
                        builder.addTablePrefix(yamlProperties.getProperty(PREFIX + "addTablePrefix"));
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.addFieldPrefix"))) {
                        builder.addFieldPrefix(yamlProperties.getProperty(PREFIX + "addFieldPrefix"));
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.addFieldSuffix"))) {
                        builder.addFieldSuffix(yamlProperties.getProperty(PREFIX + "addFieldSuffix"));
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.entityBuilder.superClass"))) {
                        builder.entityBuilder().superClass(yamlProperties.getProperty(PREFIX + "StrategyConfig.entity.superClass"));
                    }
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.entityBuilder.disableSerialVersionUID"))) {
                        builder.entityBuilder().disableSerialVersionUID();
                    }
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.entityBuilder.enableColumnConstant"))) {
                        builder.entityBuilder().enableColumnConstant();
                    }
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.entityBuilder.enableRemoveIsPrefix"))) {
                        builder.entityBuilder().enableRemoveIsPrefix();
                    }
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.entityBuilder.enableLombok"))) {
                        builder.entityBuilder().enableLombok();
                    }

                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.entityBuilder.enableFileOverride"))) {
                        builder.entityBuilder().fileOverride();
                    }
                    builder.entityBuilder()
                            .addTableFills(filledList);
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.mapperBuilder.enableMapperAnnotation"))) {
                        builder.mapperBuilder().enableMapperAnnotation();
                    }

                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.mapperBuilder.enableFileOverride"))) {
                        builder.mapperBuilder().fileOverride();
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.serviceBuilder.superServiceClass"))) {
                        builder.serviceBuilder().superServiceClass(yamlProperties.getProperty(PREFIX + "StrategyConfig.serviceBuilder.superServiceClass"));
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.serviceBuilder.superServiceImplClass"))) {
                        builder.serviceBuilder().superServiceImplClass(yamlProperties.getProperty(PREFIX + "StrategyConfig.serviceBuilder.superServiceImplClass"));
                    }
                    builder.serviceBuilder()
                            // service不以I开头
                            .convertServiceFileName((entityName -> entityName + ConstVal.SERVICE));
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "StrategyConfig.controllerBuilder.superClass"))) {
                        builder.controllerBuilder().superClass(yamlProperties.getProperty(PREFIX + "StrategyConfig.controllerBuilder.superClass"));
                    }
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.controllerBuilder.enableRestStyle"))) {
                        builder.controllerBuilder().enableRestStyle();
                    }
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.controllerBuilder.enableFileOverride"))) {
                        builder.controllerBuilder().fileOverride();
                    }
                    if (Boolean.parseBoolean(yamlProperties.getProperty(PREFIX + "StrategyConfig.controllerBuilder.superClass"))) {
                        builder.controllerBuilder().enableRestStyle();
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "tableNames"))) {
                        builder.addInclude(yamlProperties.getProperty(PREFIX + "tableNames"));
                    }
                })
                .voConfig(builder -> {
                    builder.generateVO(true);
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "voConfig.suffix"))) {
                        builder.suffix(yamlProperties.getProperty(PREFIX + "voConfig.suffix"));
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "voConfig.packageSuffix"))) {
                        builder.packageSuffix(yamlProperties.getProperty(PREFIX + "voConfig.packageSuffix"));
                    }
                    if (StringUtils.isNotBlank(yamlProperties.getProperty(PREFIX + "voConfig.template"))) {
                        builder.template(yamlProperties.getProperty(PREFIX + "voConfig.template"));
                    }
                })
                .injectionConfig(consumer -> {
                })
                .templateEngine(new MyVelocityTemplateEngine()); // vm模板/

        fastAutoGenerator.execute();
    }
}
