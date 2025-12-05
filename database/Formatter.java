package database;
import java.util.List;
import java.util.Map;

public interface Formatter {
  String getName();
  String formatData(List<Map<String, String>> dataList);
  String getExtension();
}