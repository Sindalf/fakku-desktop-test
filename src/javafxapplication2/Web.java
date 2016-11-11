package javafxapplication2;

import javafxapplication2.model.Content;
import javafxapplication2.model.Stuff;
import javafxapplication2.model.Index;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Web {

    Map<String, String> cookies;
    String[] tags;
    Main application;

    int lastsearchMod = 0;
    String lastbaseURL;
    String lastTerm;
    int maxPage = 1;

    public int getMaxPage() {
        return maxPage;
    }

    public void setApp(Main item) {
        application = item;
    }

    public String[] populateTags() {
        tags = null;
        try {
            Document doc = Jsoup.connect("https://www.fakku.net/tags").cookies(cookies).get();
            Elements items = doc.select("#tag-list");
            Element e = items.first();
            
            items = e.select("[title]");
            System.out.println(items.size());
            tags = new String[items.size()];
            
            for (int i = 0; i < tags.length; i++) {
                System.out.println(items.get(i).text());
                tags[i] = items.get(i).text();
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Web.class.getName()).log(Level.SEVERE, null, ex);
        }
            return tags;
    }

    /*
     * And here I wanted to avoid string parsing. Special cases due
     * inconsistantcies with certain tags See the links at
     * https://www.fakku.net/tags
     */
    public String specialCase(String terms) {
        terms = terms.toLowerCase();
        switch (terms) {
            case "monster girl":
                terms = "monstergirl";
                break;
            case "dark skin":
                terms = "darkskin";
                break;
            case "office lady":
                terms = "office-lady";
                break;
            case "slice of life":
                terms = "slice-of-life";
                break;
            case "body swap":
                terms = "body-swap";
                break;
            default:
                break;
        }

        return terms;
    }

    public Content[] search(String terms, int page, int searchMod) throws IOException, IllegalArgumentException, IllegalAccessException {

        Content[] content = null;
        System.out.println("Searching");
        String baseURL;
        switch (searchMod) {
            case 1:
                baseURL = "https://www.fakku.net/search/";
                break;
            case 2:
                baseURL = "https://www.fakku.net/tags/";
                terms = specialCase(terms);
                break;
            default:
                System.out.println("Invalid search modifier returning null");
                return null;
        }

        try {

            Document doc = Jsoup.connect(baseURL + terms + "/page/" + page).cookies(cookies).get();
            Elements items = doc.select("#content");
            Element e = items.first();
            items = e.children();
            items = items.select(".images.four.columns");

            content = new Content[items.size()];
            for (int i = 0; i < content.length; i++) {
                String book = items.get(i).select("a[href]").attr("href");
                book = book.substring(8, book.length());
                content[i] = singleManga(book);
            }

            lastbaseURL = baseURL;
            lastTerm = terms;
            maxPage = getPageLength(doc);

        } catch (HttpStatusException ex) { // Fakku returns GONE 410 for incorrect searches
            System.out.println("Your search returned no results");
            System.out.println(ex);
        }
        return content;
    }

    public Content[] lastPagedSearch(int page) throws IOException, IllegalArgumentException, IllegalAccessException {
        Content[] content = null;
        try {

            if (page > maxPage) {
                System.out.println("Outside of range! maxPage=" + maxPage + " page=" + page);
            } else {
                Document doc = Jsoup.connect(lastbaseURL + lastTerm + "/page/" + page).cookies(cookies).get();
                Elements items = doc.select("#content");
                Element e = items.first();
                items = e.children();
                items = items.select(".images.four.columns");

                content = new Content[items.size()];
                for (int i = 0; i < content.length; i++) {
                    String book = items.get(i).select("a[href]").attr("href");
                    book = book.substring(8, book.length());
                    content[i] = singleManga(book);
                }
            }

        } catch (HttpStatusException ex) { // Fakku returns GONE 410 for incorrect searches
            System.out.println("Your search returned no results");
            System.out.println(ex);
        }
        return content;
    }

    public boolean login(String username, String password) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.fakku.net/login/submit")
                .data("username", username, "password", password)
                .method(Connection.Method.POST).execute();
        System.out.println(res.statusMessage());

        if (res.url().toString().equals("https://www.fakku.net/login/submit")) {
            return false;
        } else {
            cookies = res.cookies();
            System.out.println(cookies);

            return true;
        }
    }

    public int getPageLength(Document doc) {
        Elements items = doc.select(".links");
        Element e = items.last();
        String[] thing = e.text().split(" ");
        System.out.println(thing[thing.length - 1]);
        return Integer.parseInt(thing[thing.length - 1]);
    }

    public Content singleManga(String path) throws IOException, IllegalArgumentException, IllegalAccessException {
        InputStream input = new URL("https://api.fakku.net/manga/" + path).openStream();
        Reader reader = new InputStreamReader(input, "UTF-8");

        Stuff m = new Gson().fromJson(reader, Stuff.class);
        Content manga = m.getContent();
        return manga;
    }

    public Content[] index() throws IOException, IllegalArgumentException, IllegalAccessException {
        InputStream input = new URL("https://api.fakku.net/index").openStream();
        Reader reader = new InputStreamReader(input, "UTF-8");

        Index m = new Gson().fromJson(reader, Index.class);
        Content[] manga = m.getContent();

        return manga;
    }

    public void setCookies(Map cookies) {
        this.cookies = cookies;
    }

}
