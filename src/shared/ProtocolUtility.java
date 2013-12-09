package shared;

import java.util.ArrayList;
import java.util.List;

public class ProtocolUtility {
    /**
     * @param s String s that represents serialized data
     * @return List<String> Deserialized data parsed from string
     */
    public static List<String> deserialize(String s) {
        String[] tokens = s.split(" ");
        List<String> result = new ArrayList<String>();
        for (String token: tokens)
            result.add(token);
        return result;
    }
    
    /**
     * Takes a List<String> and serializes it, space separated. 
     * @param <E>
     * @param data List<String to be serialized
     * @return String representing the space separated serialized data
     */
    public static <E> String serialize(List<E> data) {
        String s = "";
        for (E datum : data) {
            s += datum.toString() + " ";
        }
        return s;
    }
    
    /**
     * Tests.
     */
    public static void main(String args[]) {
        List<String> test = new ArrayList<String>();
        test.add("Something");
        test.add("1"); 
        test.add("2");
        test.add("3");
        System.out.println(serialize(test));
        System.out.println(deserialize(serialize(test)));
        
        List<Integer> test2 = new ArrayList<Integer>();
        test2 = new ArrayList<Integer>();
        test2.add(1);
        test2.add(2); 
        test2.add(3);
        test2.add(4);
        System.out.println(serialize(test2));
        System.out.println(deserialize(serialize(test2)));
        
        List<Object> test3 = new ArrayList<Object>();
        test3 = new ArrayList<Object>();
        test3.add(1);
        test3.add("Blah"); 
        test3.add(5);
        test3.add(4);
        System.out.println(serialize(test3));
        System.out.println(deserialize(serialize(test3)));
    }
}
