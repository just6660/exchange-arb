package hyperion.domain.bybit;

import hyperion.domain.bybit.websocket.BybitWebSocketListener;
import hyperion.domain.bybit.websocket.BybitWebSocketService;
import hyperion.domain.order_book.OrderBookService;
import hyperion.domain.shared.Constants;
import hyperion.domain.websocket.WebSocketService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BybitService {
  @Getter private final OrderBookService orderBookService = new OrderBookService();

  private final WebSocketService webSocketService;

  public BybitService(OkHttpClient client) {
    this.webSocketService =
        new WebSocketService(
            client, Constants.BYBIT_SOCKET_URL, new BybitWebSocketListener(orderBookService));
  }

  public void init() {
    initOrderBook();
  }

  public void initOrderBook() {
    webSocketService.start();
    webSocketService.sendRequest(BybitWebSocketService.getOrderbookStreamRequest());
  }
}
