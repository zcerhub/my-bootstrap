package ctbiyi.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.mybatis.spring.annotation.MapperScan;

/**
 * springboot启动类
 */
@SpringBootApplication
@MapperScan("ctbiyi.com.*.repository")
public class WebApplication {

    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);

    private static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
    private static final String SWAGGER_PAGE = "swagger-ui/index.html"; // swagger3
    private static final String KNIFE4J_PAGE = "doc.html"; // knife4j

    /**
     * 主方法
     *
     * @param args 主方法参数
     * @throws UnknownHostException 未知主机异常
     */
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(WebApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        String serverPort = env.getProperty("server.port");
        String protocol = "http";
        if (env.getProperty(SERVER_SSL_KEY_STORE) != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\t{}://localhost:{}\n\t"
                        + "External: \t{}://{}:{}\n\t"
                        + "API doc: \t{}://{}:{}/{}\n\t"
                        + "Knife4j: \t{}://{}:{}/{}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                serverPort,
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                serverPort,
                SWAGGER_PAGE,
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                serverPort,
                KNIFE4J_PAGE,
                env.getActiveProfiles());
    }

}

