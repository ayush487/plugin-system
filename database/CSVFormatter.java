package database;
import java.util.*;
public class CSVFormatter implements Formatter {
  @Override
  public String formatData(List<Map<String, String>> dataList) {
    StringBuilder builder = new StringBuilder();
    if (dataList.isEmpty()) return builder.toString();
    Map<String, String> firstRow = dataList.get(0);
    Set<String> keys = firstRow.keySet();
    List<String> keyList = new ArrayList<>(keys);
    builder.append(String.join(",", keys));
    builder.append("\n");
    for (Map<String, String> data : dataList) {
      for (int i=0;i<keyList.size();i++) {
        builder.append(data.get(keyList.get(i)));
        builder.append(i != keyList.size()-1 ? "," : "");
      }
      builder.append("\n");
    }
    return builder.toString();
  }
  @Override
  public String getName() { return "CSV Formatter"; }
  @Override
  public String getExtension() { return "csv";}
}