package javafxapplication2.model;

import java.lang.reflect.Field;
import java.util.List;

public class Stuff {

    Content content;
    
    public Stuff() {
        
    }


    public Content getContent() {
        return content;
    }
    
    /*
     * Too lazy to manually access everything
     */
    public void printAll() throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            System.out.println(f.getName() + ": " + f.get(this));
        }
    }

}
