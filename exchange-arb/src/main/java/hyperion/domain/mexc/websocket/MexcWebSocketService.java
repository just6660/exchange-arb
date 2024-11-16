package hyperion.domain.mexc.websocket;

import org.json.JSONArray;
import org.json.JSONObject;

public class MexcWebSocketService {
  public static String getDiffDepthStreamRequest() {
    JSONObject request = new JSONObject();
    request.put("method", "SUBSCRIPTION");
    JSONArray params = new JSONArray();
    params.put("spot@public.increase.depth.v3.api@BTCUSDT");
    request.put("params", params);
    return request.toString();
  }
}
