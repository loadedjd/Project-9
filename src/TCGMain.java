import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author Jared Williams and Rocky Manrique
 *
 */
public class TCGMain {

    /**
     * character array of separators.
     */
    private static final char[] SEPS = { '\t', '.', '\n', ',', ' ', '[', '?',
            '!', ']' };

    /**
     * main method.
     *
     * @param args
     */
    public static void main(String[] args) {
        SimpleReader sr = new SimpleReader1L();
        SimpleWriter sw = new SimpleWriter1L();

        sw.print("Input File: ");
        String inputName = sr.nextLine();

        sw.print("Output File: ");
        String outputName = sr.nextLine();

        sw.print("Number of words to include: ");
        int numberOfWords = sr.nextInteger();

        SimpleReader in = new SimpleReader1L(inputName);
        SimpleWriter out = new SimpleWriter1L(outputName);
        TCGenerator.generateTagCloud(in, out, inputName, numberOfWords, SEPS);
    }

}
