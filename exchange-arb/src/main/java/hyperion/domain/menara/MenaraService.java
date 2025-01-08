package hyperion.domain.menara;

import hyperion.domain.bybit.BybitService;
import hyperion.domain.mexc.MexcService;
import hyperion.domain.order_book.OrderBookService;
import hyperion.domain.shared.Constants;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MenaraService {
  private static final String SYMBOL = "BTCUSDT";

  private final BybitService bybitService;
  private final MexcService mexcService;
  private final OrderBookService bybitOrderBookService;
  private final OrderBookService mexcOrderBookService;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  private double bybitUsdt = 1000;
  private double bybitBtc = 0.01;
  private double mexcUsdt = 1000;
  private double mexcBtc = 0.01;

  public MenaraService(BybitService bybitService, MexcService mexcService) {
    this.bybitService = bybitService;
    this.mexcService = mexcService;

    bybitOrderBookService = bybitService.getOrderBookService();
    mexcOrderBookService = mexcService.getOrderBookService();
  }

  public void startComparisonWhenReady() {
    log.info("Starting Menara Service");

    while (!bybitService.getOrderBookService().isOrderBookInitialized(SYMBOL)
        || !mexcService.getOrderBookService().isOrderBookInitialized(SYMBOL)) {
      log.info("Waiting for order books to be initialized");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        log.error("Thread was interrupted while waiting for order books to initialize.", e);
      }
    }

    log.info("Order books initialized. Starting Comparison");
    scheduler.scheduleAtFixedRate(this::compareBooks, 0, 1000, TimeUnit.MILLISECONDS);
  }

  private void compareBooks() {
    double bybitBuyPrice = bybitOrderBookService.getBestAskOrder(SYMBOL).getKey();
    double bybitSellPrice = bybitOrderBookService.getBestBidOrder(SYMBOL).getKey();
    double mexcBuyPrice = mexcOrderBookService.getBestAskOrder(SYMBOL).getKey();
    double mexcSellPrice = mexcOrderBookService.getBestBidOrder(SYMBOL).getKey();

    final double quantity = 0.01;

    double bybitBuyCost = getBuyCost(bybitBuyPrice, quantity, Constants.BYBIT_TAKER_FEE_PERCENT);
    double bybitSellGain = getSellGain(bybitSellPrice, quantity, Constants.BYBIT_TAKER_FEE_PERCENT);
    double mexcBuyCost = getBuyCost(mexcBuyPrice, quantity, Constants.MEXC_TAKER_FEE_PERCENT);
    double mexcSellGain = getSellGain(mexcSellPrice, quantity, Constants.MEXC_TAKER_FEE_PERCENT);
    //
    //    log.info(
    //        "Spread: {}. Pnl: {}",
    //        Math.max(mexcSellPrice - bybitBuyPrice, bybitSellPrice - mexcBuyPrice),
    //        Math.max(mexcSellGain - bybitBuyCost, bybitSellGain - mexcBuyCost));

    if (mexcSellGain > bybitBuyCost || bybitSellGain > mexcBuyCost) {
      log.info(
          "MEXC Sell Gain: {}. Bybit Buy Cost: {}. Bybit Sell Gain: {}. MEXC Buy Cost: {}",
          mexcSellGain,
          bybitBuyCost,
          bybitSellGain,
          mexcBuyCost);

      log.info(
          "Bybit Bid Ask: {}:{}. Mexc Bid Ask: {}:{}",
          bybitSellPrice,
          bybitBuyPrice,
          mexcSellPrice,
          mexcBuyPrice);
    }
  }

  private double getBuyCost(double price, double quantity, double feePercentage) {
    return price * quantity * (1 + feePercentage);
  }

  private double getSellGain(double price, double quantity, double feePercentage) {
    return price * quantity * (1 - feePercentage);
  }
}
