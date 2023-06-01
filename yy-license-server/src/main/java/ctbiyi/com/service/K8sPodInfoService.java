package ctbiyi.com.service;

import java.util.List;

public interface K8sPodInfoService {

    List<String> getPodIPsByLable(String lableKey,String lableValue) ;

}
