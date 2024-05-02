package br.com.acmattos.articles.dsa.algorithm.string;

public class LC2000 {

    // 1 - Receive a WORD and a LETTER
    public String stringReversePrefixOfWord(String word, char ch) {
        int end = 0;
        // 2 - Find the INDEX of the first occurence of LETTER in the WORD
        while(end < word.length() && word.charAt(end) != ch) {
            end++;
        }
        // 3 - if the INDEX could not be found at WORD, return the WORD
        if(end == word.length()) return word;
        // 4 - Reverse prefix of WORD, starting from index 0 to INDEX (inclusive)
        int bgn = 0;
        while(bgn < end) {
            word = word.substring(0, bgn)
                    + word.charAt(end)
                    + word.substring(bgn + 1, end)
                    + word.charAt(bgn)
                    + word.substring(end + 1);
            end--;
            bgn++;
        }
        // 5 - Return the partialy Reversed WORD
        return word;
    }

    // 1 - Receive a WORD and a LETTER
    public String stringbuilderReversePrefixOfWord(String word, char ch) {
        int end = 0;
        // 2 - Find the INDEX of the first occurence of LETTER in the WORD
        while(end < word.length() && word.charAt(end) != ch) {
            end++;
        }
        // 3 - if the INDEX could not be found at WORD, return the WORD
        if(end == word.length()) return word;
        int bgn = 0;
        StringBuilder b = new StringBuilder(word);
        // 4 - Reverse prefix of WORD, starting from index 0 to INDEX (inclusive)
        while(bgn < end) {
            b.replace(bgn, bgn + 1, "" + word.charAt(end)).
                    replace(end, end + 1, "" + word.charAt(bgn));
            end--;
            bgn++;
        }
        // 5 - Return the partialy Reversed WORD
        return b.toString();
    }

    // 1 - Receive a WORD and a LETTER
    public String charArrayReversePrefixOfWord(String word, char ch) {
        int end = 0;
        char[] letters = word.toCharArray();
        // 2 - Find the INDEX of the first occurence of LETTER in the WORD
        while(end < letters.length && letters[end] != ch) {
            end++;
        }
        // 3 - if the INDEX could not be found at WORD, return the WORD
        if(end == letters.length) return word;
        int bgn = 0;
        // 4 - Reverse prefix of WORD, starting from index 0 to INDEX (inclusive)
        while(bgn < end) {
            char temp = letters[end];
            letters[end] = letters[bgn];
            letters[bgn] = temp;
            end--;
            bgn++;
        }
        // 5 - Return the partialy Reversed WORD
        return new String(letters);
    }

    public static void main(String[] args) {
        LC2000 lc = new LC2000();
        System.out.println("\n-=[Reverse Prefix of Word]=- ");
        System.out.println("-> 8 ms x 42.44 MB");
        System.out.println("   abcdefd: " + lc.stringReversePrefixOfWord("abcdefd", 'd'));
        System.out.println("-> 1 ms x 41.30 MB");
        System.out.println("   xyxzxe : " + lc.stringbuilderReversePrefixOfWord("xyxzxe", 'z'));
        System.out.println("-> 0 ms x 41.37 MB");
        System.out.println("   abcd   : " + lc.charArrayReversePrefixOfWord("abcd", 'z'));
    }
}
