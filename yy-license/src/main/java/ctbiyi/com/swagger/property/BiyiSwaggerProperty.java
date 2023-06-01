
package ctbiyi.com.swagger.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * desc
 *
 * @author : quankuijin
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "biyi.swagger")
public class BiyiSwaggerProperty {

    /**
     * 分组名
     */
    private String groupName;

    /**
     * swagger的标题
     */
    private String title;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 指定包路径下的接口
     */
    private String basePackage;

    /**
     * 过滤url路径：需要使用通配符**（优先级低）
     */
    private String path;

    /**
     * 过滤url路径-正则表达式（优先级高）
     */
    private String pathRegex;

    /**
     * 联系人信息
     */
    private BiyiSwaggerContactProperty contact;


    /**
     * 支持同一路径，不同param的场景
     * 但是swagger对此支持不太友好，使用该场景的系统，推荐使用knife4j
     */
    private Boolean enableUrlTemplating = false;

}
