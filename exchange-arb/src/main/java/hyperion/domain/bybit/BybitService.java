package hyperion.domain.bybit;

import com.bybit.api.client.config.BybitApiConfig;
import hyperion.domain.bybit.websocket.BybitWebSocketListener;
import hyperion.domain.bybit.websocket.BybitWebSocketService;
import hyperion.domain.order_book.OrderBookService;
import hyperion.domain.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BybitService {
  private static final String SOCKET_URL =
      BybitApiConfig.STREAM_MAINNET_DOMAIN + BybitApiConfig.V5_PUBLIC_SPOT;

  private final OrderBookService orderBookService = new OrderBookService();

  private final WebSocketService webSocketService;

  public BybitService(OkHttpClient client) {
    this.webSocketService =
        new WebSocketService(client, SOCKET_URL, new BybitWebSocketListener(orderBookService));
  }

  public void init() {
    initOrderBook();
  }

  public void initOrderBook() {
    webSocketService.start();
    webSocketService.sendRequest(BybitWebSocketService.getOrderbookStreamRequest());
  }
}
