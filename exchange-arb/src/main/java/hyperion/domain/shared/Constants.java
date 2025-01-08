package hyperion.domain.shared;

import com.bybit.api.client.config.BybitApiConfig;

public class Constants {
  public static final String MEXC_SOCKET_URL = "wss://wbs.mexc.com/ws";
  public static final String BYBIT_SOCKET_URL =
      BybitApiConfig.STREAM_MAINNET_DOMAIN + BybitApiConfig.V5_PUBLIC_SPOT;

  public static final double MEXC_MAKER_FEE_PERCENT = 0.05 / 100;
  public static final double MEXC_TAKER_FEE_PERCENT = 0.05 / 100;
  public static final double BYBIT_MAKER_FEE_PERCENT = 0.1 / 100;
  public static final double BYBIT_TAKER_FEE_PERCENT = 0.1 / 100;
}
