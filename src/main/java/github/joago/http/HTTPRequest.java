package github.joago.http;

import java.util.HashMap;

public record HTTPRequest(String method, String url, HashMap<String, Object> headers, String body) {

}
