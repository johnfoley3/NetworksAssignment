import java.util.Scanner;

/**
 * Translates text into piglatin.
 */
public class PiglatinTranslator {

    /**
     * Method to translate a sentence word by word.
     * @param s The sentence in English
     * @return The pig latin version
     */
    public static String piglatin( String s ) {
        String latin = "";
        int i = 0;
        while ( i < s.length() ) {

            // Take care of punctuation and spaces
            while ( i < s.length() && !isLetter( s.charAt( i ) ) ) {
                latin = latin + s.charAt( i );
                i++;
            }

            // If there aren't any words left, stop.
            if ( i >= s.length() ) break;


            // Beginning of word
            int begin = i;

            while ( i < s.length() && isLetter( s.charAt( i ) ) ) {
                i++;
            }

            // End of word, translate
            int end = i;

            latin = latin + pigWord( s.substring( begin, end ) );
        }

        return latin;

    }

    /**
     * Helper function to find character
     * @param c The character to test
     * @return True if it's a letter
     */
    private static boolean isLetter( char c ) {
        return ( ( c >='A' && c <='Z' ) || ( c >='a' && c <='z' ) );
    }

    /**
     * Translate one word to piglatin
     * @param word The word in english
     * @return The pig latin version
     */
    private static String pigWord( String word ) {

        int split = firstVowel( word );

        char firstLetter = word.charAt(0);

        if (firstLetter >= 'A' && firstLetter <= 'Z') {
            word = word.substring(0, 1).toLowerCase() + word.substring(1, split) +
                    word.substring( split, split+1 ).toUpperCase() +
                    word.substring(split+1, word.length());
            System.out.println(word);
        }

        return word.substring( split ) + "-" + word.substring( 0, split ) + "ay";

    }

    /**
     * Return index of the first vowel
     * @param word The word to search
     * @return The index of the first vowel
     */
    private static int firstVowel( String word ) {
        word = word.toLowerCase();
        for ( int i = 0; i < word.length(); i++ )
            if ( word.charAt(i)=='a' || word.charAt(i)=='e' ||
                    word.charAt(i)=='i' || word.charAt(i)=='o' ||
                    word.charAt(i)=='u' || word.charAt(i)=='y' )
                return i;
        return 0;
    }

}