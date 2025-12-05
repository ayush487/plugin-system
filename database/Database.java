package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Database {

  private static List<Map<String, String>> dataList;
  private static List<String> keys;
  private List<Formatter> formatters;
  private PrintWriter pw;

  private static void loadDataFromFile(Scanner scanner) {
    String metaData = scanner.nextLine();
    String[] keyArray = metaData.split(",");
    for (String key : keyArray) {
      keys.add(key);
    }
    while (scanner.hasNextLine()) {
      String data = scanner.nextLine();
      String[] dataArray = data.split(",");
      Map<String, String> map = new HashMap<>(keyArray.length);
      for (int i = 0; i < keyArray.length; i++) {
        map.put(keyArray[i], dataArray[i]);
      }
      dataList.add(map);
    }
  }

  public static void main(String[] args) {
    dataList = new ArrayList<>();
    keys = new ArrayList<>();

    Database db = new Database();
    db.formatters = new ArrayList<>();
    db.addFormatter(new DefaultFormatter());
    db.addFormatter(new CSVFormatter());

    File tempDB = new File("database.csv");
    boolean previousDataExists = false;
    Scanner databaseScanner = null;
    if (tempDB.exists()) {
      try {
        databaseScanner = new Scanner(tempDB);
        if (databaseScanner.hasNext())
          previousDataExists = true;
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

    Scanner sc = new Scanner(System.in);
    if (previousDataExists) {
      System.out.println("Previous data found into the database\nDo you want to load the database(y/n) : ");
      String choice = sc.nextLine().toLowerCase();
      if (choice.startsWith("y")) {
        db.setDatabaseFile(tempDB);
        System.out.println("Loading data from database...");
        loadDataFromFile(databaseScanner);
        databaseScanner.close();
      } else {
        databaseScanner.close();
        boolean isDeleted = tempDB.delete();
        System.out.println("Deleted : " + isDeleted);
        db.takeMetaDataInput(sc);
      }
    } else
      db.takeMetaDataInput(sc);

    String choice;
    do {
      db.displayOptions();
      choice = sc.nextLine();
      db.execute(choice, sc);
    } while (!choice.equals("0"));

    sc.close();
  }

  private void execute(String choice, Scanner sc) {
    if (choice.equals("1")) insertData(sc);
    else if (choice.equals("2")) requestExport(sc);
    else if (choice.equals("0")) return;
    else if (choice.equals("9")) addFormatter(sc);
    else {
      int idx = Integer.parseInt(choice)-3;
      if (idx>=formatters.size()) {
        System.out.println("Invalid choice");
        return;
      }
      String data = formatters.get(idx).formatData(dataList);
      System.out.println(data);
    }
  }

  private void insertData(Scanner sc) {
    Map<String, String> map = new HashMap<>();
    for (String key : keys) {
      System.out.println("Enter " + key + " :");
      String input = sc.nextLine();
      map.put(key, input);
    }
    dataList.add(map);
    this.writeData(map);
  }

  private void requestExport(Scanner sc) {
    System.out.println("Select exporting format :");
    for (int i=1;i<formatters.size();i++) {
      System.out.println(String.format("%d. %s", i, formatters.get(i).getName()));
    }
    System.out.println("0. Cancel");
    String choice = sc.nextLine();
    try {
      int idx = Integer.parseInt(choice);
      if (idx == 0) return;
      else if (idx<0 || idx>=formatters.size()) throw new Exception();
      else {
        String data = formatters.get(idx).formatData(dataList);
        if (createFile(data, "export_data." + formatters.get(idx).getExtension())) System.out.println("File created successfully");
        else System.out.println("Error creating file");
      }
    } catch (Exception e) {
      System.out.println("Please select a valid option.");
      return;
    }
  }

  public void addFormatter(Formatter formatter) {
    this.formatters.add(formatter);
  }

  private void displayOptions() {
    System.out.println("Select an option :");
    System.out.println("1. Insert Data");
    System.out.println("2. Export Data");
    for (int i = 0; i < formatters.size(); i++) {
      System.out.println(String.format("%d. Fetch Data (%s)", i + 3, formatters.get(i).getName()));
    }
    System.out.println("9. Add Formatter");
    System.out.println("0. Exit");
  }

  private void setDatabaseFile(File file) {
    try {
      this.pw = new PrintWriter(new FileWriter(file, true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeData(Map<String, String> data) {
    ArrayList<String> al = new ArrayList<>();
    for (String key : keys) {
      al.add(data.get(key));
    }
    String line = String.join(",", al);
    pw.println(line);
    pw.flush();
  }

  private boolean createFile(String content, String filename) {
    File file = new File(filename);
    try {
      FileOutputStream fos = new FileOutputStream(file);
      for (char c : content.toCharArray()) {
        fos.write(c);
      }
      fos.flush();
      fos.close();
      return true;
    } catch (IOException e) { return false; }
  }

  private void addFormatter(Scanner sc) {
    System.out.println("Enter full class name of the formatter: ");
    String formatterName = sc.nextLine();
    try {
      Class<?> formatter = Class.forName(formatterName);
      if (Formatter.class.isAssignableFrom(formatter)) {
        try {
          formatters.add((Formatter) formatter.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
          System.out.println("Error creating formatter instance: " + e.getMessage());
        }
      } else System.out.println("Not a valid formatter");
    } catch (ClassNotFoundException e) {
      System.out.println("Class Not Found");
    }
  }

  private void takeMetaDataInput(Scanner sc) {
    File f = new File("database.csv");
    f.delete();
    setDatabaseFile(new File("database.csv"));
    System.out.println("Enters column names separated by comma(,) : ");
    String colNames = sc.nextLine();
    String[] colArray = colNames.split(",");
    for (String key : colArray) {
      keys.add(key);
    }
    pw.println(colNames);
    pw.flush();
  }
}
