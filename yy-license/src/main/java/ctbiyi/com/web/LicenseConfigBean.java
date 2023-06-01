package ctbiyi.com.web;


import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "yy.license")
public class LicenseConfigBean {

    private String licenseFileName;
    private String subject;
    private String keyPwd;
    private String storePwd;
    private String privateStorePath;
    private String dname;
    private Date expireTime;
    private String privateAlias;


}
