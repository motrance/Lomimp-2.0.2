package coffeemunchingmonkeys.tools.lomimp.stats;

import org.junit.Assert;
import org.junit.Test;

public class PairTest {

    @Test
    public void toStringReturnsEmptyStringForNewPair() {
        Pair pair = new Pair();

        Assert.assertEquals("", pair.toString());
    }

    @Test
    public void addMemberReturnsSingleMemberAsFormattedString() {
        Pair pair = new Pair();

        pair.addMember(1);

        Assert.assertEquals("1 ", pair.toString());
    }

    @Test
    public void addMemberSortsAndFormatsMultipleMembers() {
        Pair pair = new Pair();

        pair.addMember(5);
        pair.addMember(2);
        pair.addMember(9);

        Assert.assertEquals("2 5 9 ", pair.toString());
    }

    @Test
    public void addMemberKeepsOnlyUniqueMembers() {
        Pair pair = new Pair();

        pair.addMember(4);
        pair.addMember(4);
        pair.addMember(2);

        Assert.assertEquals("2 4 ", pair.toString());
    }
}
