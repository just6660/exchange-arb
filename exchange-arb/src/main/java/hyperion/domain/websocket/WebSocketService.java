package hyperion.domain.websocket;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@Slf4j
public class WebSocketService {
  private final OkHttpClient client;
  private final String socketUrl;
  private final WebSocketListener listener;

  private WebSocket webSocket;

  public WebSocketService(OkHttpClient client, String socketUrl, WebSocketListener listener) {
    this.client = client;
    this.socketUrl = socketUrl;
    this.listener = listener;
  }

  public void start() {
    Request request = new Request.Builder().url(socketUrl).build();
    webSocket = client.newWebSocket(request, listener);
    log.info("WebSocket connection initiated.");
  }

  public void sendRequest(String request) {
    log.info("Sent request {} to {}", request, socketUrl);
    if (webSocket != null && webSocket.send(request)) {
      log.info("Subscription request sent.");
    } else {
      log.error("Failed to send subscription request.");
    }
  }

  public void stop() {
    if (webSocket != null) {
      webSocket.close(1000, "Normal closure");
      log.info("WebSocket connection closed.");
    }
  }

  public void reconnect() {
    stop();
    start();
  }
}
