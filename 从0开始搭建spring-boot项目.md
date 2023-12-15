# 从0开始搭建spring-boot项目
## 版本
java: 8
spring-boot：2.4.13

## 目标
搭建一个基于spring-boot框架的web项目并实现hello word接口

## 环境
### Java环境
项目运行的基础环境
### maven环境
项目运行所需的依赖管理工具，本项目是基于maven打包构建的

## 实现
### 定义pom
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>site.bleem</groupId>
    <artifactId>bleem-spring</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging>

    <description>Demo project for Spring Boot</description>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>2.4.13</spring-boot.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!--spring boot start -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--spring boot end -->

    </dependencies>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

</project>

```
### 编写启动类
```
package site.bleem.boot.demo;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BootDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {

        new SpringApplicationBuilder(BootDemoApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
```
### 编写接口实现类
```
/**
 * @author yubs
 * @desc demo
 * @date 2023/12/15
 */
@RestController
public class DemoController {
    @GetMapping("/hello/{word}")
    public ResponseEntity<String> returnWorld(@PathVariable("word") String word) {
        return ResponseEntity.ok("hello "+word);
    }
}
```
## 结果验证
默认启动在8080端口
![Img](./FILES/从0开始搭建spring-boot项目.md/img-20231215160552.png)
