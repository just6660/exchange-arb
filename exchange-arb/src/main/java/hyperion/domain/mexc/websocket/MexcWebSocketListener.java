package hyperion.domain.mexc.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyperion.domain.mexc.order_book.DiffDepthUpdate;
import hyperion.domain.order_book.OrderBookService;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class MexcWebSocketListener extends okhttp3.WebSocketListener {
  private final OrderBookService orderBookService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public MexcWebSocketListener(OrderBookService orderBookService) {
    this.orderBookService = orderBookService;
  }

  @Override
  public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
    log.info("WebSocket Opened. Response: {}", response);
  }

  @Override
  public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
    try {
      DiffDepthUpdate diffDepthUpdate = objectMapper.readValue(text, DiffDepthUpdate.class);

      if (text.contains("bids")) {
        orderBookService.updateBuyOrder(
            diffDepthUpdate.getSymbol(),
            diffDepthUpdate.getD().getQuote().getFirst().getPrice(),
            diffDepthUpdate.getD().getQuote().getFirst().getQuantity(),
            diffDepthUpdate.getTimestamp(),
            diffDepthUpdate.getD().getSeqID());
      } else if (text.contains("asks")) {
        orderBookService.updateSellOrder(
            diffDepthUpdate.getSymbol(),
            diffDepthUpdate.getD().getQuote().getFirst().getPrice(),
            diffDepthUpdate.getD().getQuote().getFirst().getQuantity(),
            diffDepthUpdate.getTimestamp(),
            diffDepthUpdate.getD().getSeqID());
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
