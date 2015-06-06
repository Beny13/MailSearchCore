package mailsearchcore;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Beny
 */
class WebPageParser {
    private static final int depth = 2;
    private static final String addressRegex = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9]+)+";

    public Collection<String> findAddressesFromURL(String url) throws IOException, InterruptedException {
        Matcher m;

        Stack<String> toParse = new Stack<>();
        toParse.add(url);

        Set<String> parsedURLs = new HashSet<>();
        Set<String> addresses = new HashSet<>();

        Document doc;
        String urlToParse;

        for (int i = 0; i < depth; i++) {
            Stack<String> currentToParse = (Stack)toParse.clone();
            while (!currentToParse.isEmpty()) {
                urlToParse = currentToParse.pop();
                if (!parsedURLs.contains(urlToParse)) {
                    parsedURLs.add(urlToParse);

                    try {
                        doc = Jsoup.connect(urlToParse).get();
                    } catch (Exception e) {
                        continue;
                    }

                    // Getting mail addresses
                    m = Pattern.compile(addressRegex).matcher(doc.toString());
                    while (m.find()) {
                        addresses.add(m.group());
                    }

                    // Getting URLS
                    Elements newURLS = doc.select("a[href]");
                    for(Element newURL: newURLS){
                        if(newURL.attr("abs:href").contains(url)) {
                            toParse.add(newURL.attr("abs:href"));
                        }
                    }
                }
            }
        }

        return addresses;
    }

}
