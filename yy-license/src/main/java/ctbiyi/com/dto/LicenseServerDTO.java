package ctbiyi.com.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class LicenseServerDTO {
    @NotNull(message = "过期时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date expireTime;

    @NotEmpty(message = "mac地址不能为空")
    private List<String> macs;


}
