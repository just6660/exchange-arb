package hyperion.domain.order_book;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class OrderBookService {
  private final Map<String, OrderBook> orderBooks;

  public OrderBookService() {
    this.orderBooks = new ConcurrentHashMap<>();
  }

  public void checkSeqId(String symbol, long seqId) {
    if (!orderBooks.containsKey(symbol)) {
      return;
    }

    if (orderBooks.get(symbol).getSeqId() != 0 && seqId - orderBooks.get(symbol).getSeqId() != 1) {
      log.error("INVALID NEXT SEQ ID: {}, {}", orderBooks.get(symbol).getSeqId(), seqId);
    }
  }

  public synchronized void updateBuyOrder(
      String symbol, double price, double quantity, long updateTimestamp, long seqId) {
    OrderBook orderBook = getOrCreateOrderBook(symbol);
    orderBook.setUpdateTimestamp(updateTimestamp);
    orderBook.setSeqId(seqId);
    updateOrder(orderBook.getBids(), price, quantity, updateTimestamp, seqId);
  }

  public synchronized void updateSellOrder(
      String symbol, double price, double quantity, long updateTimestamp, long seqId) {
    OrderBook orderBook = getOrCreateOrderBook(symbol);
    orderBook.setUpdateTimestamp(updateTimestamp);
    orderBook.setSeqId(seqId);
    updateOrder(orderBook.getAsks(), price, quantity, updateTimestamp, seqId);
  }

  public Map.Entry<Double, OrderLevel> getBestBuyOrder(String symbol) {
    OrderBook orderBook = orderBooks.get(symbol);
    return (orderBook == null || orderBook.getBids().isEmpty())
        ? null
        : orderBook.getBids().entrySet().iterator().next();
  }

  public Map.Entry<Double, OrderLevel> getBestSellOrder(String symbol) {
    OrderBook orderBook = orderBooks.get(symbol);
    return (orderBook == null || orderBook.getAsks().isEmpty())
        ? null
        : orderBook.getAsks().entrySet().iterator().next();
  }

  public void clearOrderBook(String symbol) {
    OrderBook orderBook = orderBooks.get(symbol);
    if (orderBook != null) {
      orderBook.getBids().clear();
      orderBook.getAsks().clear();
    }
  }

  @Override
  public String toString() {
    if (orderBooks.isEmpty()) {
      return "No order books available.";
    }

    StringBuilder sb = new StringBuilder();
    orderBooks.forEach(
        (symbol, orderBook) -> {
          sb.append("Order Book for Symbol: ").append(symbol).append("\n");

          // Bids
          sb.append("Bids:\n");
          if (orderBook.getBids().isEmpty()) {
            sb.append("  No bids available.\n");
          } else {
            orderBook
                .getBids()
                .forEach(
                    (price, level) ->
                        sb.append(
                            String.format(
                                "  Price: %.2f, Quantity: %.2f\n", price, level.getQuantity())));
          }

          // Asks
          sb.append("Asks:\n");
          if (orderBook.getAsks().isEmpty()) {
            sb.append("  No asks available.\n");
          } else {
            orderBook
                .getAsks()
                .forEach(
                    (price, level) ->
                        sb.append(
                            String.format(
                                "  Price: %.2f, Quantity: %.2f\n", price, level.getQuantity())));
          }
        });

    return sb.toString();
  }

  private void updateOrder(
      Map<Double, OrderLevel> quotes,
      double price,
      double quantity,
      long updateTimestamp,
      long seqId) {
    if (quantity == 0) {
      quotes.remove(price);
      return;
    }
    if (quotes.containsKey(price)) {
      OrderLevel level = quotes.get(price);
      level.setQuantity(quantity);
      level.setUpdateTimestamp(updateTimestamp);
      level.setSeqId(seqId);
    } else {
      quotes.put(price, new OrderLevel(quantity, updateTimestamp, seqId));
    }
  }

  private OrderBook getOrCreateOrderBook(String symbol) {
    return orderBooks.computeIfAbsent(symbol, k -> new OrderBook());
  }
}
