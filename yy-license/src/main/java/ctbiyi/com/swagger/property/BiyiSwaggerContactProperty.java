package ctbiyi.com.swagger.property;

import lombok.Getter;
import lombok.Setter;

/**
 * 联系人信息
 *
 * @author : quankuijin
 */
@Getter
@Setter
public class BiyiSwaggerContactProperty {
    /**
     * 姓名
     */
    private String name;

    /**
     * 联系人主页
     */
    private String url;

    /**
     * 联系人邮箱
     */
    private String email;
}
