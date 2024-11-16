package hyperion;

import hyperion.domain.bybit.BybitService;
import hyperion.domain.mexc.MexcService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ExchangeArbApplicationRunner implements ApplicationRunner {
  private final MexcService mexcService;
  private final BybitService bybitService;

  public ExchangeArbApplicationRunner(MexcService mexcService, BybitService bybitService) {
    this.mexcService = mexcService;
    this.bybitService = bybitService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    mexcService.init();
    bybitService.init();
  }
}
