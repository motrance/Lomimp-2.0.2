package coffeemunchingmonkeys.tools.lomimp.stats;

import java.util.SortedMap;
import java.util.TreeMap;

/***
 *
 * Lomimp
 * stats.Pair
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class Pair {
    SortedMap<Integer, Integer> members = new TreeMap<Integer, Integer>();

    public Pair() {
    }

    /**
     *
     * @param member
     */
    public void addMember(Integer member) {
        members.put(member, member);
    }

    /**
     *
     */
    public String toString() {
        String returnString = "";

        for (Integer key : members.keySet()) {
            String keyString = "" + key;
            if(keyString.length() == 1) {
                keyString = "0" + keyString;
            }
            returnString = returnString + key + " ";
        }

        returnString.trim();
        return returnString;
    }
}
