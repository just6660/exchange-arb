package hyperion.domain.bybit.order_book;

import java.util.List;
import lombok.Data;

@Data
public class OrderBookUpdate {
  private String topic;
  private String type;
  private long ts; // Timestamp
  private Data data;
  private long cts; // Match engine timestamp

  @lombok.Data
  public static class Data {
    private String s; // Symbol name
    private List<List<Double>> b; // Bids
    private List<List<Double>> a; // Asks
    private long u; // Update ID
    private long seq; // Sequence ID
  }
}
