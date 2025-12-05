package database.plugins;
import java.util.*;
import database.Formatter;

public class JSONFormatter implements Formatter {
  @Override
  public String formatData(List<Map<String, String>> dataList) {
    StringBuilder jsonBuilder = new StringBuilder("[\n");
    int c1 = 0;
    for (Map<String, String> dataMap : dataList) {
      c1++;
      Set<String> keys = dataMap.keySet();
      jsonBuilder.append("  {\n");
      int c2 = 0;
      for (String key : keys) {
        c2++;
        jsonBuilder.append(String.format("    \"%s\" : \"%s\"", key, dataMap.get(key)));
        jsonBuilder.append(c2 == keys.size() ? "\n" : ",\n");
      }
      jsonBuilder.append(c1 == dataList.size() ? "  }\n" : "  },\n");
    }
    return jsonBuilder.append("]").toString();
  }
  @Override
  public String getName() { return "JSON Formatter"; }
  @Override
  public String getExtension() { return "json";}
}
