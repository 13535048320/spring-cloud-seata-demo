## 1. Seata-Server
### 1.1 下载
```
https://github.com/seata/seata/releases
```

### 2.2 配置
#### 2.2.1 registry.conf
```
注册到eureka的配置

registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "eureka"
  
  eureka {
    serviceUrl = "http://localhost:6789/eureka"
    application = "seata-server"
    weight = "1"
  }
}
```

#### 2.2.2 file.conf
```
conf/file.conf

修改
    vgroup_mapping.my_test_tx_group = "default"
为
    vgroup_mapping.my_group = "seata-server"
    
    注意：vgroup_mapping.组名 = "服务名"
    组名与项目yml配置中的spring.cloud.alibaba.seata.tx-service-group对应
    服务名与seata-server注册到eureka的服务名一致（即与registry.conf的eureka.application一致）
```

```
修改store方式，默认为file
这里使用db方式

store {
  ## store mode: file、db
  mode = "db"

  ## database store
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
    datasource = "dbcp"
    ## mysql/oracle/h2/oceanbase etc.
    db-type = "mysql"
    driver-class-name = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://localhost:3306/seata"
    user = "root"
    password = "root"
    min-conn = 1
    max-conn = 3
    global.table = "global_table"
    branch.table = "branch_table"
    lock-table = "lock_table"
    query-limit = 100
  }
}
```

### 2.5 创建seata数据库
在seata-server配置所连接的mysql服务器创建数据库seata，并使用下载的seata-server内的db_store.sql文件初始化数据表

### 2.4 启动
```
先启动eureka
然后到seata-server的bin目录下，Windows启动seata-server.bat，Linux启动seata-server.sh

启动后到eureka页面查看服务名是否正确
```

## 2. Seata-Client
### 2.1 添加Maven依赖
在项目中需要用到事务的模块添加一下依赖
```
    <dependency>
        <groupId>io.seata</groupId>
        <artifactId>seata-all</artifactId>
        <version>0.8.1</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-seata</artifactId>
        <version>2.1.0.RELEASE</version>
    </dependency>
```

### 2.2 拷贝seata-server配置文件
将Seata-Server的conf目录下的file.conf和registry.conf拷贝到模块下的resources下

![picture1](https://github.com/13535048320/spring-cloud-seata-demo/pictures/1.png)


### 2.3 在springboot配置文件内添加
```
spring:
  cloud:
    alibaba:
      seata:
        tx-service-group: my_group # 与file.conf内的vgroup_mapping相对应
```

### 2.4 DataSource配置
在被调用的模块将我们原先的数据源替换成seata自带的代理数据源DataSourceProxy。使用代理数据源的原因是seata rm要在原先的数据操作上增加自己的一些业务处理，以此来达到分布式事务的功能。

例子中 client-one 使用 Feign 调用 client-two，所以在 client-two 中配置。
```
package com.example.clienttwo.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Primary
    @Bean("dataSourceProxy")
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean("jdbcTemplate")
    @ConditionalOnBean(DataSourceProxy.class)
    public JdbcTemplate jdbcTemplate(DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }
}
```

### 2.5 创建表
在模块所连接的数据库创建表
![picture2](https://github.com/13535048320/spring-cloud-seata-demo/pictures/2.png)
```
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```
建表语句可以在seata-server内的db_undo_log.sql文件查看

### 2.6 添加注解 @GlobalTransactional
```
在事务开始处添加注解
    @GlobalTransactional
```

### 2.7 启动项目
```
启动项目后，访问
http://localhost:6791/testFeignInsert

可以看到seata-server日志显示如下
![picture3](https://github.com/13535048320/spring-cloud-seata-demo/pictures/3.png)

并且数据库内没插入新数据
```