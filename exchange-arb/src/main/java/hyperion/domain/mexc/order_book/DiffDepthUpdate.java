package hyperion.domain.mexc.order_book;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;
import lombok.Data;

@Data
public class DiffDepthUpdate {
  private String c; // Channel name
  private Data d; // Data

  @JsonAlias("s")
  private String symbol; // Symbol

  @JsonAlias("t")
  private long timestamp; // Timestamp

  @lombok.Data
  public static class Data {
    @JsonAlias({"bids", "asks"})
    private List<Quote> quote; // Quote

    private String e; // Channel name

    @JsonAlias("r")
    private long seqID; // Request ID
  }

  @lombok.Data
  public static class Quote {
    @JsonAlias("p")
    private double price; // Price

    @JsonAlias("v")
    private double quantity; // Volume
  }
}
