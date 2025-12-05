package database;
import java.util.*;
public interface Formatter {
  String getName();
  String formatData(List<Map<String, String>> dataList);
  String getExtension();
}