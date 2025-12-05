package database.plugins;

import java.util.List;
import java.util.Map;

import database.Formatter;

public class XMLFormatter implements Formatter {
  @Override
  public String formatData(List<Map<String, String>> dataList) {
    StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    xmlBuilder.append("<root>\n");
    for (Map<String,String> data : dataList) {
      xmlBuilder.append(" <row>\n");
      var keySet = data.keySet();
      for (String key : keySet) {
        xmlBuilder.append(String.format("  <%s>%s</%s>\n", key, data.get(key), key));
      }
      xmlBuilder.append(" </row>\n");
    }
    xmlBuilder.append("</root>");
    return xmlBuilder.toString();
  }

  @Override
  public String getName() {
    return "XML Formatter";
  }

}