package javafxapplication2.model;

import java.util.HashMap;

/*
 * Key is the content url such as "school-regulation-violation-english-1469229804"
 * Value is the stored object from the api call. A lot of this data should never
 * change so keeping a cache should be acceptable.
 * 
 * In reality we should only really use get, exists, and put. 
 * What if the cache becomes too big? Solve this problem. 
 */
public class Cache {

    HashMap<String, Content> map;

    public Cache() {
        map = new HashMap();
    }
    
    public Content get(String key) {
        return map.get(key);
    }
    
    public boolean exists(String key) {
        return map.containsKey(key);
    }

    /*
     * Returns value if the key, value pair is properly added.
     * Returns null if already exists.
     */
    public Content add(String key, Content value) {
        return map.put(key, value);
    }
    
    public Content remove(String key) {
        return map.remove(key);
    }
    
    public boolean remove(String key, Content value) {
        return map.remove(key, value);
    }
    
    public void deleteAll() {
        map = new HashMap();
    }
    
   
}
