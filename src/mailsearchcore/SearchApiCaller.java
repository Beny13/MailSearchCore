package mailsearchcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Beny
 */
public class SearchApiCaller {
    private static final String accountKey = "UbGWTVoHF1mZbq6aURhLr0MbjsV3EQ40sK7NPu3I2Dk";
    private static final String bingUrlPattern = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%%27%s%%27&$format=JSON";

    public ArrayList<String> findURLFromKeyword(String keyword) throws IOException {
        ArrayList<String> addresses = new ArrayList<>();

        final String query = URLEncoder.encode("'what      is omonoia'", Charset.defaultCharset().name());
        final String bingUrl = String.format(bingUrlPattern, query);

        final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());

        final URL url = new URL(bingUrl);
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            final JSONObject json = new JSONObject(response.toString());
            final JSONArray results = json.getJSONObject("d").getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                addresses.add((String)results.getJSONObject(i).get("Url"));
            }
        }

        return addresses;
    }
}
