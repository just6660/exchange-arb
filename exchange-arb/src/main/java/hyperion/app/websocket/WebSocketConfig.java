package hyperion.app.websocket;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {
  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS).build();
  }
}
