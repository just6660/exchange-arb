package hyperion.domain.order_book;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import lombok.Data;

@Data
public class OrderBook {
  private Map<Double, OrderLevel> bids;
  private Map<Double, OrderLevel> asks;

  private long updateTimestamp;
  private long seqId;

  public OrderBook() {
    this.bids =
        new ConcurrentSkipListMap<>((a, b) -> Double.compare(b, a)); // Descending order for bids
    this.asks = new ConcurrentSkipListMap<>(); // Ascending order for asks
  }

  // Utility method to print the order book (bids and asks)
  public void printOrderBook() {
    System.out.println("Bids:");
    bids.forEach(
        (price, level) -> {
          System.out.println("Price: " + price + " Quantity: " + level.getQuantity());
        });

    System.out.println("\nAsks:");
    asks.forEach(
        (price, level) -> {
          System.out.println("Price: " + price + " Quantity: " + level.getQuantity());
        });
  }
}
