package hyperion.domain.mexc;

import hyperion.domain.mexc.websocket.MexcWebSocketListener;
import hyperion.domain.mexc.websocket.MexcWebSocketService;
import hyperion.domain.order_book.OrderBookService;
import hyperion.domain.websocket.WebSocketService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MexcService {
  private static final String SOCKET_URL = "wss://wbs.mexc.com/ws";

  @Getter private final OrderBookService orderBookService = new OrderBookService();

  private final WebSocketService webSocketService;

  public MexcService(OkHttpClient client) {
    this.webSocketService =
        new WebSocketService(client, SOCKET_URL, new MexcWebSocketListener(orderBookService));
  }

  public void init() {
    initOrderBook();
  }

  private void initOrderBook() {
    webSocketService.start();
    webSocketService.sendRequest(MexcWebSocketService.getDiffDepthStreamRequest());
    // webSocketService.stop();
  }
}
