package test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by heming on 7/13/2016.
 */
public class SetConflictTest {
    public static void main(String args[]) {
        Set<String> set = new HashSet<String>();
        set.add("1");
        set.add("1");
        System.out.println(set.contains("1"));
    }
}
