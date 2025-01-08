package hyperion;

import hyperion.domain.bybit.BybitService;
import hyperion.domain.menara.MenaraService;
import hyperion.domain.mexc.MexcService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ExchangeArbApplicationRunner implements ApplicationRunner {
  private final MexcService mexcService;
  private final BybitService bybitService;
  private final MenaraService menaraService;

  public ExchangeArbApplicationRunner(
      MexcService mexcService, BybitService bybitService, MenaraService menaraService) {
    this.mexcService = mexcService;
    this.bybitService = bybitService;
    this.menaraService = menaraService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    bybitService.init();
    mexcService.init();
    menaraService.startComparisonWhenReady();
  }
}
