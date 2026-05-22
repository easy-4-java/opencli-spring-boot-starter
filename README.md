# opencli-spring-boot-starter

Spring Boot Starter，自动装配 [opencli-java-sdk](../opencli-java-sdk)，支持本机 `opencli` 子进程或与 opencli-admin 兼容的远端 Agent（`POST /collect`）。

## Maven

```xml
<dependency>
  <groupId>io.github.hiwepy</groupId>
  <artifactId>opencli-spring-boot-starter</artifactId>
  <version>3.3.x.20260516-SNAPSHOT</version>
</dependency>
```

需先将**同分支、同日期后缀**的 `opencli-java-sdk` 安装到本地或发布到可达的 Maven 仓库。

发布到阿里云 Packages（`pom.xml` 已配置 `distributionManagement`）：

```bash
python3 scripts/render-branch-pom.py <branch>
mvn clean deploy -Dmaven.test.skip=true
```

`settings.xml` 中需配置 `2624322-release-6F6h6R` 与 `2624322-snapshot-3EoOv3` 的私服账号。

## 多版本与 JDK（对齐 dreamina-spring-boot-starter）

`pom.xml` 由脚本按 Spring Boot 线生成：`spring-boot-starter-parent`、`maven-compiler-plugin` 的 `release` 与 `opencli-java-sdk.version` 与分支前缀一致。

```bash
python3 scripts/render-branch-pom.py <branch>   # 与 opencli-java-sdk 同分支名：3.3.x、2.7.x、4.0.x 等
```

| 分支 | Spring Boot parent | 编译 JDK | 与 SDK 版本 |
|------|-------------------|----------|-------------|
| `2.3.x` | 2.3.12.RELEASE | **8** | 同前缀 `2.3.x.*` |
| `2.7.x` | 2.7.18 | **11** | `2.7.x.*` |
| `3.0.x` … `3.4.x` | 3.0.13 … 3.4.2 | 17 | 与各 `3.x.x.*` 对齐 |
| `3.3.x`（默认） | **3.3.6** | **17** | `3.3.x.*-SNAPSHOT` |
| `3.5.x` | 3.5.6 | 21 | `3.5.x.*` |
| `4.0.x` | 4.0.6 | 21 | `4.0.x.*` |

## 自动配置

- 绑定 `opencli.*` 到 `OpenCliStarterProperties`（继承 SDK `OpenCliProperties`）
- 注册单例 `OpenCliExecutor` 与 `OpenCliClient`（共享同一执行器）
- `opencli.browser-profile` 映射为 `leadingArguments` 前缀 `--profile <name>`（与 CLI profile 一致）
- `opencli.center.ws.*` 可选注册 `OpenCliWsReverseAgentClient`（`enabled=true`）
- `opencli.facade.beans.enabled=true` 时额外注册：`OpenCliMetaClient`、`OpenCliBrowserClient`、`CodexOpenCliClient`、`CursorOpenCliClient`、`ClaudeOpenCliClient`、`ChatgptOpenCliClient`、`GeminiOpenCliClient`、`JimengOpenCliClient`、`NpmOpenCliClient`、`ArxivOpenCliClient`、`WikipediaOpenCliClient`、`BinanceOpenCliClient`
- `META-INF/spring.factories` 与 `AutoConfiguration.imports` 兼容 Boot 2.7 / 3.x 发现机制

## application.yml 示例

```yaml
opencli:
  enabled: true
  executable: opencli
  command-timeout-millis: 300000
  browser-profile: work              # → leadingArguments: --profile work
  execution-target: REMOTE_AGENT_HTTP   # 或 LOCAL_PROCESS（默认）
  remote-agent-base-url: http://192.168.1.10:19823
  remote-collect-mode: cdp
  remote-output-format: json
  remote-capture-raw-http-response: false
  facade:
    beans:
      enabled: true                  # 注册常用强类型 Bean
  center:
    ws:
      enabled: false
```

## License

Apache License 2.0
