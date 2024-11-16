package hyperion.domain.order_book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderLevel {
  private double quantity;
  private long updateTimestamp;
  private long seqId;
}
