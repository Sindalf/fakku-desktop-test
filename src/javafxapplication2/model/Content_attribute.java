package javafxapplication2.model;

public class Content_attribute {

    public String attribute;
    public String attribute_link;

    public Content_attribute() {

    }
    
    @Override
    public String toString() {
        return attribute + ": " + attribute_link; 
    }
}
