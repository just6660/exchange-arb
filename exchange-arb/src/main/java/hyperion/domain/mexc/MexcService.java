package hyperion.domain.mexc;

import hyperion.domain.mexc.websocket.MexcWebSocketListener;
import hyperion.domain.mexc.websocket.MexcWebSocketService;
import hyperion.domain.order_book.OrderBookService;
import hyperion.domain.shared.Constants;
import hyperion.domain.websocket.WebSocketService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MexcService {
  @Getter private final OrderBookService orderBookService = new OrderBookService();

  private final WebSocketService webSocketService;

  public MexcService(OkHttpClient client) {
    this.webSocketService =
        new WebSocketService(
            client, Constants.MEXC_SOCKET_URL, new MexcWebSocketListener(orderBookService));
  }

  public void init() {
    initOrderBook();
  }

  private void initOrderBook() {
    webSocketService.start();
    webSocketService.sendRequest(MexcWebSocketService.getDiffDepthStreamRequest());
  }
}
