package javafxapplication2.model;

import java.lang.reflect.Field;
import java.util.List;

public class Content {

    public String content_name;
    public String content_url;
    public String content_language;
    public String content_category;
    public int content_date;
    public int content_filesize;
    public int content_favorites;
    public int content_comments;
    public int content_pages;
    public String content_poster;
    public String content_poster_url;
    public List<Content_attribute> content_tags;
    public List<Content_attribute> content_series;
    public List<Content_attribute> content_artists;
    public Content_images content_images;

    public Content() {
    }

    public void printAll() throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            System.out.println(f.getName() + ": " + f.get(this));
        }
    }
}
