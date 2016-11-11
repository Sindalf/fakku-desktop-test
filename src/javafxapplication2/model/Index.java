package javafxapplication2.model;

import java.lang.reflect.Field;

public class Index {

    Content[] index;

    public Index() {

    }

    public Content[] getContent() {
        return index;
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
