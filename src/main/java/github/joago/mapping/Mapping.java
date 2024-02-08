package github.joago.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Mapping {

  private HashMap<String, String> mappings;

  public Mapping() {
    this.mappings = new HashMap<String, String>();
    seedMappings();
  }

  public String findMappingByUrl(String url) {
    String root = url.split("/")[1];
    if (mappings.containsKey(root)) {
      String found = mappings.get(root);
      return found;
    }
    return null;
  }

  public boolean updateHost(String host, String newHost) {
    if (mappings.containsKey(host)) {

      String dir = mappings.get(host);
      mappings.remove(host);
      mappings.put(newHost, dir);
      return true;
    }
    return false;
  }

  public boolean updateUrl(String key, String dir, String newDir) {
    if (mappings.containsValue(dir)) {
      mappings.replace(key, dir, newDir);
      return true;
    }
    return false;
  }

  void seedMappings() {

    File mappingsFile = new File(getClass().getClassLoader().getResource("mappings.txt").getFile());
    try (FileReader reader = new FileReader(mappingsFile)) {
      BufferedReader br = new BufferedReader(reader);
      String line;

      try {
        while ((line = br.readLine()) != null) {
          String[] parts = line.split("=");
          mappings.put(parts[0], parts[1]);
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public HashMap<String, String> getMappings() {
    return mappings;
  }

}
