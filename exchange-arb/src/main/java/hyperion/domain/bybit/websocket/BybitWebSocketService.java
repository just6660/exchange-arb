package hyperion.domain.bybit.websocket;

import org.json.JSONArray;
import org.json.JSONObject;

public class BybitWebSocketService {
  public static String getOrderbookStreamRequest() {
    return getOrderbookStreamRequest("", "1", "BTCUSDT");
  }

  public static String getOrderbookStreamRequest(String req_id, String depth, String symbol) {
    JSONObject request = new JSONObject();
    request.put("req_id", req_id);
    request.put("op", "subscribe");
    JSONArray args = new JSONArray();
    args.put("orderbook" + "." + depth + "." + symbol);
    request.put("args", args);
    System.out.println(request.toString());
    return request.toString();
  }
}
