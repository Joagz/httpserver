package github.joago.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import github.joago.http.HTTPResponse;

public class Commons {
  public static boolean isContentTypeImage(HTTPResponse res) {
    return res.headers().get("Content-Type").contains("image");
  }

  public static boolean isContentTypeImage(String str) {
    return str.contains("Content-Type: image");
  }

  public static String ByteBufferToString(ByteBuffer buffer, Charset charset) {
    byte[] bytes;
    if (buffer.hasArray()) {
      bytes = buffer.array();
    } else {
      bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
    }
    return new String(bytes, charset);
  }
  public static ByteBuffer StringToByteBuffer(String msg, Charset charset){
    return ByteBuffer.wrap(msg.getBytes(charset));
}

}
