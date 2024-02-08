package github.joago.config;

public class FileConfiguration {

  public FileConfiguration() {
    this.documents = getClass().getResource("/var/data/").toString().substring("file:".length());
  }

  public String documents;
}
