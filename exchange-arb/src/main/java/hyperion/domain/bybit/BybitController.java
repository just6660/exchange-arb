package hyperion.domain.bybit;

import hyperion.domain.order_book.OrderBook;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bybit")
@CrossOrigin(origins = "http://localhost:5173")
public class BybitController {
  private final BybitService bybitService;

  public BybitController(BybitService bybitService) {
    this.bybitService = bybitService;
  }

  @GetMapping("/order-book")
  public ResponseEntity<Map<String, OrderBook>> getOrderBook() {
    try {
      return ResponseEntity.ok(bybitService.getOrderBookService().getOrderBooks());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }
}
