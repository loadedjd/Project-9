import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplewriter.SimpleWriter;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 *
 * @author Jared Williams and Rocky Manrique
 *
 */
public class TCGenerator {

    /**
     * stores each word and how many times it occurs.
     */
    private static Map<String, Integer> wordCounts = new Map1L<String, Integer>();

    /**
     * Generates html code that generates the cloud word page. Calls all other
     * helper methods found in the class TCGEnerator.
     *
     * @param in
     *            SimpleReader variable for input
     * @param out
     *            SimpleWriter variable for output
     * @param inputName
     *            name of the input text file
     * @param numberOfWords
     *            number of words to be included in the generated tag cloud
     * @param seperators
     *            character array of the separators used to separate words
     */
    public static void generateTagCloud(SimpleReader in, SimpleWriter out,
            String inputName, int numberOfWords, char[] seperators) {
        readWordsIntoMap(in, wordCounts, seperators);
        SortingMachine<Pair<String, Integer>> sm = sortWords(wordCounts,
                numberOfWords);
        writeHtmlHeader(out, inputName, numberOfWords);
        writeHtmlBody(out, sm, inputName, numberOfWords);
        writeHtmlFooter(out);
    }

    /**
     * Begins generating the html code for the output file. The beginning
     * includes the header/title.
     *
     * @param out
     *            SimpleWriter variable for output
     * @param inputName
     *            name of the input text file
     * @param n
     *            holds the number of words to be included in the generated tag
     *            cloud
     */
    private static void writeHtmlHeader(SimpleWriter out, String inputName,
            int n) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Top " + n + "words in " + inputName);
        out.println("</title>");
        out.println(
                "<link href='http://cse.osu.edu/software/2231/web-sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css' rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
    }

    /**
     * Generates the body of the html code for each Map.Pair in the
     * sortingMachine and changes their font size based on the number of
     * occurrences counted.
     *
     * @param out
     *            SimpleWriter variable for output
     * @param words
     *            sorts the Map.Pairs of words and their number of occurences
     * @param inputName
     *            name of the input text file
     * @param n
     *            holds the number of words to be included in the generated tag
     *            cloud
     */
    private static void writeHtmlBody(SimpleWriter out,
            SortingMachine<Pair<String, Integer>> words, String inputName,
            int n) {

        out.println("<body>");
        out.println("<h2> Top " + n + " words in " + inputName + " </h2>");
        out.println("<hr>");

        out.println("<div class=\"cdiv\">\r\n" + "<p class=\"cbox\">");

        while (words.size() > 0) {
            Map.Pair<String, Integer> pair = words.removeFirst();

            int font = (48 - 11) * (pair.value() - 30);
            font /= (500 - 30);
            font += 11;
            out.println("<span style='cursor:default' class=\"f" + font
                    + "\" title=\"count: " + pair.value() + "\">" + pair.key()
                    + " </span>");
        }

        out.println("</p>");
        out.println("</div>");

    }

    /**
     * Ends the html program.
     *
     * @param out
     *            SimpleWriter variable for output
     */
    private static void writeHtmlFooter(SimpleWriter out) {
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Clears the given wordMap at the start of the method. Goes through each
     * line of the input text and adds each new word to the Map and counts all
     * of its occurrences.
     *
     * @param in
     *            SimpleReader variable for input
     * @param wordMap
     *            Map of all the words and their counts
     * @param seps
     *            character array of the separators used to separate words
     * @updates wordMap
     */
    private static void readWordsIntoMap(SimpleReader in,
            Map<String, Integer> wordMap, char[] seps) {
        String line = "";
        wordMap.clear();

        while (!in.atEOS()) {
            line = in.nextLine();
            for (char sep : seps) {
                line = line.replace(sep, ':');
            }

            String[] words = line.split(":");

            for (String word : words) {
                if (!wordMap.hasKey(word) && word != " ") {
                    wordMap.add(word, 1);
                } else {
                    int count = wordMap.value(word);
                    count++;
                    wordMap.replaceValue(word, count);
                }
            }
        }

    }

    /**
     * Takes in Map of words and the number of words to sort and returns the
     * sorted words. Uses IntComparator and AlphabeticalComparator to sort.
     *
     * @param words
     *            Map of all the words and their counts
     * @param numberOfWords
     *            number of words to be included in the generated tag cloud
     * @return SortingMachine of Pairs of each word and its count
     */
    private static SortingMachine<Pair<String, Integer>> sortWords(
            Map<String, Integer> words, int numberOfWords) {
        AlphabeticalComparator stringComp = new AlphabeticalComparator();
        IntComparator intComp = new IntComparator();

        SortingMachine<Pair<String, Integer>> intMachine = new SortingMachine1L<>(
                intComp);
        SortingMachine<Pair<String, Integer>> stringMachine = new SortingMachine1L<>(
                stringComp);

        for (Map.Pair<String, Integer> pair : words) {
            intMachine.add(pair);
        }

        intMachine.changeToExtractionMode();

        for (int i = 0; i < numberOfWords; i++) {
            Pair<String, Integer> pair = intMachine.removeFirst();
            System.out.println(pair);
            stringMachine.add(pair);
        }

        stringMachine.changeToExtractionMode();

        return stringMachine;
    }

    /**
     * Compares Pairs by their int values.
     *
     * @author Jared Williams and Rocky Manrique
     *
     */
    private static class IntComparator
            implements Comparator<Pair<String, Integer>> {

        @Override
        public int compare(Pair<String, Integer> arg0,
                Pair<String, Integer> arg1) {
            return arg1.value() - arg0.value();
        }

    }

    /**
     * Compares Pairs by their alphabetical order.
     *
     * @author Jared Williams and Rocky Manrique
     *
     */
    private static class AlphabeticalComparator
            implements Comparator<Pair<String, Integer>> {

        @Override
        public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
            return o1.key().toLowerCase().compareTo(o2.key().toLowerCase());
        }

    }
}
