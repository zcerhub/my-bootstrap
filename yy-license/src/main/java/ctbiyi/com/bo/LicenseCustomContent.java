package ctbiyi.com.bo;

import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LicenseCustomContent {

    private List<String> macs;

    private int limit;
    private int used;


}
