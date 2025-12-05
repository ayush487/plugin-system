package database;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultFormatter implements Formatter {

  private String name = "Default Formatter";

  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public String formatData(List<Map<String, String>> dataList) {
    StringBuilder builder = new StringBuilder();
    for (Map<String, String> data : dataList) {
      Set<String> keySet = data.keySet();
      for (String key : keySet) {
        builder.append(key + " : " + data.get(key) + "\n");
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  @Override
  public String getExtension() { return "txt";}
}
