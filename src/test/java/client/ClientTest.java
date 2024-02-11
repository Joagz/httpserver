package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import github.joago.config.EnvironmentConfig;

public class ClientTest {

  public static void main(String[] args) {
    try {
      if (EnvironmentConfig.DEBUG)
        System.out.println("Running client");
      ClientTest test = new ClientTest();
      test.test();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Basic communication example.
  private void test() throws UnknownHostException, IOException {

    Socket client = new Socket("127.0.0.1", 8000);
    OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream());

    File file = new File(getClass().getClassLoader().getResource("request.txt").getFile());
    FileReader fileReader = new FileReader(file);
    char[] content = new char[1024];

    while (fileReader.read(content) != -1) {
      writer.write(content);
    }

    writer.flush();
    fileReader.close();

    InputStreamReader in = new InputStreamReader(client.getInputStream());
    BufferedReader reader = new BufferedReader(in);

    char[] buffer = new char[1024];
    StringBuilder sb = new StringBuilder();

    while (reader.read(buffer, 0, buffer.length) != -1) {
      sb.append(new String(buffer));
    }

    client.close();

  }

}
