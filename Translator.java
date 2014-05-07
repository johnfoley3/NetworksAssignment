/** Definitions and utilities capable of translating texting speak
 * as it is used by ignorant people into proper English.
 * @author matthews
 *
 */
public class Translator {

   private static final String[] ORIG = {
      "omg",
      "i", 
      "u",
      "r",
      "ur",
      "bff",
      "lol",
      "lmao",
      "thx",
      "h8",
      "y",
      "b",
      "zzz",
      "m"};

   private static final String [] TRANSLATED = {
      "you don't say",
      "I",
      "you",
      "are",
      "you are",
      "close friend",
      "I find that humorous",
      "I find that very humorous",
      "thank you",
      "hate",
      "why",
      "be",
      "I'm feeling drowsy",
      "am"};

   /** Convert a String to its texting translation, if possible.
    * @param originalWord the word to translate
    * @return the translated word, if it has a translation.
    */
   public static String translate(String originalWord)
   {
      for (int i = 0; i < ORIG.length; i++) {
         if (originalWord.compareTo(ORIG[i]) == 0) {
            return TRANSLATED[i];
         }
      }
      // Word not found in translation list.
      return originalWord;
   }

}



