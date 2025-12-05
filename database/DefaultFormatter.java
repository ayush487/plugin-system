package database;
import java.util.*;
public class DefaultFormatter implements Formatter {
  @Override
  public String formatData(List<Map<String, String>> dataList) {
    StringBuilder builder = new StringBuilder();
    for (Map<String, String> data : dataList) {
      Set<String> keySet = data.keySet();
      for (String key : keySet) builder.append(key + " : " + data.get(key) + "\n");
      builder.append("\n");
    }
    return builder.toString();
  }
  @Override
  public String getName() { return "Default Formatter"; }
  @Override
  public String getExtension() { return "txt";}
}