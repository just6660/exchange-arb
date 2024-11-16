package hyperion.domain.bybit.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyperion.domain.bybit.order_book.OrderBookUpdate;
import hyperion.domain.order_book.OrderBookService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class BybitWebSocketListener extends WebSocketListener {
  private final OrderBookService orderBookService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public BybitWebSocketListener(OrderBookService orderBookService) {
    this.orderBookService = orderBookService;
  }

  @Override
  public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
    log.info("WebSocket Opened. Response: {}", response);
  }

  @Override
  public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
    try {
      OrderBookUpdate orderBookUpdate = objectMapper.readValue(text, OrderBookUpdate.class);
      String symbol = orderBookUpdate.getData().getS();
      List<List<Double>> bids = orderBookUpdate.getData().getB();
      List<List<Double>> asks = orderBookUpdate.getData().getA();
      if (!bids.isEmpty()) {
        for (List<Double> bid : bids) {
          orderBookService.updateBuyOrder(
              symbol,
              bid.getFirst(),
              bid.get(1),
              orderBookUpdate.getTs(),
              orderBookUpdate.getData().getSeq());
        }
      }
      if (!asks.isEmpty()) {
        for (List<Double> ask : asks) {
          orderBookService.updateSellOrder(
              symbol,
              ask.getFirst(),
              ask.get(1),
              orderBookUpdate.getTs(),
              orderBookUpdate.getData().getSeq());
        }
      }
    } catch (JsonProcessingException e) {
      log.warn("Unknown message. Not Encoding. {}", text);
    }
  }

  @Override
  public void onClosed(WebSocket webSocket, int code, @NotNull String reason) {
    webSocket.close(1000, null);
    log.info("WebSocket Closed. Reason: {}", reason);
  }

  @Override
  public void onFailure(@NotNull WebSocket webSocket, Throwable t, Response response) {
    log.error("WebSocket failure. Throwable: {}. Response: {}", t.getMessage(), response);
  }
}
