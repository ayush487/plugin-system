package database.plugins;

import java.util.List;
import java.util.Map;
import java.util.Set;

import database.Formatter;

public class JSONFormatter implements Formatter {
  public String formatData(Map<String, String> dataMap) {
    Set<String> keys = dataMap.keySet();
    StringBuilder jsonBuilder = new StringBuilder("{\n");
    for (String key : keys) {
      jsonBuilder.append(String.format("\"%s\" : \"%s\",\n", key, dataMap.get(key)));
    }
    return jsonBuilder.toString();
  }

  @Override
  public String formatData(List<Map<String, String>> dataList) {
    StringBuilder jsonBuilder = new StringBuilder("[\n");
    for (Map<String, String> dataMap : dataList) {
      Set<String> keys = dataMap.keySet();
      jsonBuilder.append("  {\n");
      for (String key : keys) {
        jsonBuilder.append(String.format("    \"%s\" : \"%s\",\n", key, dataMap.get(key)));
      }
      jsonBuilder.append("  },\n");
    }

    jsonBuilder.append("\n]");
    return jsonBuilder.toString();
  }

  @Override
  public String getName() {
    return "JSON Formatter";
  }
}
