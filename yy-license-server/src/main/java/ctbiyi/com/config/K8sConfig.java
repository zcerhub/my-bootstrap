package ctbiyi.com.config;

import ctbiyi.com.util.SSLSocketUtils;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class K8sConfig {

    @Value("${yy.k8s-server}")
    private String k8sServer;

    @Value("${yy.k8s-sa-token}")
    private String k8sSaToken;

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = Config.fromToken(k8sServer, k8sSaToken);
        //去掉https的ssl证书校验客户端
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();
        apiClient.setHttpClient(okHttpClient);
        return apiClient;
    }


    public OkHttpClient getUnsafeOkHttpClient() {
        return new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketUtils.getSSLSocketFactory(), SSLSocketUtils.getX509TrustManager())
                .hostnameVerifier(SSLSocketUtils.getHostnameVerifier())
                .build();
    }

}
