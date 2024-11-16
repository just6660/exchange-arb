package hyperion.domain.mexc;

import hyperion.domain.order_book.OrderBook;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mexc")
public class MexcController {
  private final MexcService mexcService;

  public MexcController(MexcService mexcService) {
    this.mexcService = mexcService;
  }

  @GetMapping("/order-book")
  public ResponseEntity<Map<String, OrderBook>> getOrderBook() {
    try {
      return ResponseEntity.ok(mexcService.getOrderBookService().getOrderBooks());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}
