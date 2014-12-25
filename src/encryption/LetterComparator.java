package encryption;

import java.util.Comparator;


public class LetterComparator implements Comparator<Letter> {
    public int compare(Letter l1, Letter l2){
        return l1.n - l2.n;
    }
}
