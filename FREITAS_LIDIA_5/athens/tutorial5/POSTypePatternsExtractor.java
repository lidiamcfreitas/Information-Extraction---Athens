package athens.tutorial5;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import athens.Page;
import athens.Parser;
import athens.Triple;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POSTypePatternsExtractor {

	/**
	 * A suggested value for the maximum length of the textual patterns.
	 */
	private static final int MAXLENGTH = 70;

	public static List<String> STOPCHARS = Arrays.asList("./.", ",/,", ";/;");

	public static void main(String args[]) throws IOException {
		File posTaggedWikipedia = new File(args[0]);
		List<String> seeds = Files.readAllLines(FileSystems.getDefault().getPath(args[1]),
				Charset.defaultCharset());
		for (String seed : seeds) {
			String[] seedFact = seed.split("\\t");
			Triple triple = new Triple(seedFact[0], seedFact[1], seedFact[2]);
			findPatterns(triple, posTaggedWikipedia);
		}
 	}

	/**
	 * Given a POS-tagged triple (e.g. Rio/NNP de/NNP Janeiro/NNP type	city/NN) and a
	 * POS-tagged corpus, it extracts all the textual patterns lying between the subject and prints them out
	 * and the object of the triple that occur in the corpus.
	 * @param seedFact
	 * @param posTaggedWikipedia
	 * @throws IOException
	 */
	private static void findPatterns(Triple seedFact, File posTaggedWikipedia) throws IOException {
            try (Parser pages = new Parser(posTaggedWikipedia)) {
                //String subString = " ((?:[a-z]|-|\\s)+) ";
                String subString = " ((?:[^.]+)) ";
                String p = seedFact.subject + subString + seedFact.object;
                String patternFound;
                Pattern pattern;
                Matcher matcher;
                FileWriter fileWriter= new FileWriter("garbage.txt");
                
                try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                    while (pages.hasNext()) {
                        Page page = pages.next();
                        pattern=Pattern.compile(p);
                        matcher=pattern.matcher(page.content);
                        
                        while(matcher.find()) {
                            patternFound = " " + matcher.group(1).trim() + " ";
                            System.out.println(patternFound);
                            bufferedWriter.write(patternFound);
                            bufferedWriter.newLine();
                        }
                        // TODO(Extract textual patterns between seedFact.subject and seedFact.object)
                    }
                }
            }
	}
}