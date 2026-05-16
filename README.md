# opencli-spring-boot-starter

Spring Boot Starter，自动装配 [opencli-java-sdk](../opencli-java-sdk)，支持本机 `opencli` 子进程或与 opencli-admin 兼容的远端 Agent（`POST /collect`）。

## Maven

```xml
<dependency>
  <groupId>io.github.hiwepy</groupId>
  <artifactId>opencli-spring-boot-starter</artifactId>
  <version>1.0.x.20260516-SNAPSHOT</version>
</dependency>
```

需先将同版本的 `opencli-java-sdk` 安装到本地或发布到可达的 Maven 仓库。

## 自动配置

- 绑定 `opencli.*` 到 `OpenCliStarterProperties`（继承 SDK `OpenCliProperties`）
- 注册单例 `OpenCliExecutor` 与 `OpenCliClient`（共享同一执行器）
- `META-INF/spring.factories` 与 `AutoConfiguration.imports` 兼容 Boot 2.7 / 3.x 发现机制

## application.yml 示例

```yaml
opencli:
  enabled: true
  executable: opencli
  command-timeout-millis: 300000
  execution-target: REMOTE_AGENT_HTTP   # 或 LOCAL_PROCESS（默认）
  remote-agent-base-url: http://192.168.1.10:19823
  remote-collect-mode: cdp
  remote-output-format: json
  remote-capture-raw-http-response: false
```

## License

Apache License 2.0
