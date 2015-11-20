package athens.tutorial4;

import athens.Page;
import athens.Parser;
import athens.Triple;
import athens.tutorial2.Trie;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dipre {

	/**
	 * Finds patterns in Wikipedia from seed facts.
	 * For example:
	 *   Wikipedia="blah blah Elvis is married to Priscilla blah blah"
	 *   seeds={<Elvis,hasSpouse,Priscilla>}
	 *   Result={" is married to "}
	 * Hint: Use a regular expression to find patterns,
	 * and restrict patterns to 10-25 letters and spaces.
	 * Include the boundary spaces in the pattern.
     * @param wikipedia
     * @param seeds
     * @return 
     * @throws java.io.IOException
	 */
	public static Trie findPatterns(File wikipedia, Set<Triple> seeds)
			throws IOException {
            Trie result = new Trie();
            
            //String triplePredicate;
            String subString = " ((?:[a-z]|-|\\s)+) ";
            String tripleSubject;
            String tripleObject;
            String p;
            Page page;
            Pattern pattern;
            Matcher matcher;
            
            for(Triple t: seeds){
                tripleSubject = t.subject;
                //triplePredicate = t.predicate;
                tripleObject = t.object;
                //p = "\\s"+tripleSubject + subString + tripleObject +"\\,|\\.|\\s";
                p = tripleSubject + subString + tripleObject;
                try (Parser pages = new Parser(wikipedia)) {
                    while (pages.hasNext()) {
                        page = pages.next();
                        String content = page.content.replaceAll("\\(.*\\) ", "");
                        pattern=Pattern.compile(p);
                        matcher=pattern.matcher(content);
                        
                        while(matcher.find()) {
                            String pat = " " + matcher.group(1).trim() + " ";
                            int patLength = pat.length();
                            if (patLength<25 && patLength>10){ 
                                System.out.println(pat);
                                result.add(pat);
                                //Triple test = new Triple(t.subject, matcher.group(1), t.object);
                            }
                            
                        }
                    }
                }
            }
            return (result);
	}

	/**
	 * Given Wikipedia, a set of patterns, a relation name,
	 * and a set of entities, finds facts.
	 *
	 * For example:
	 *   Wikipedia = "blah blah Barack is married to Michelle blah blah"
	 *   patterns = {"is married to", "is in love with"}
	 *   relation = "hasSpouse"
	 *   entities = {Michelle, Barack, Elvis, Priscilla}
	 *   Result = {<Barack, hasSpouse, Michelle>}
	 *
	 * Hint: First find the subject, then the pattern, and then the object
	 */
	public static Set<Triple> findFacts(File wikipedia, Trie patterns,
            String relation, Trie entities) throws IOException {
            Set<Triple> result = new HashSet<>();
            Page page;
            String title;
            String content;
            String subject = "";
            String object = "";
            int i, j, k;
            int lengthPatterns;
            int lengthEnt1 = 0;
            int lengthEnt2 = 0;
            
            try (Parser pages = new Parser(wikipedia)) {
                    while (pages.hasNext()) {
                        page = pages.next();
                        title = page.title;
                        content = page.content.replaceAll("\\(.*\\) ", "");
                        lengthPatterns = 0;
                        
                        for (i = 0; i < content.length(); i++) {
                            lengthPatterns = patterns.containedLength(content, i);
                            
                            if (lengthPatterns > 0 ){
                                // supposed: the fact can only appear in the same sentence
                                
                                // find the rightmost entity which appears left to the pattern
                                for(j = i; j >= 0; j--){
                                    //System.out.print(content.charAt(j));
                                    if(content.charAt(j) == '.') break;
                                    if(content.charAt(j) == ',') break;
                                    lengthEnt1 = entities.containedLength(content, j);
                                    if (lengthEnt1 > 0 ) {
                                        subject = content.substring(j, lengthEnt1 + j);
                                        break;
                                    }
                                }
                                i += lengthPatterns; 
                                // search on the other half of the content for an entity
                                for(k = i; k < content.length(); k++){
                                    if(content.charAt(k) == '.') break;
                                    if(content.charAt(k) == ',') break;
                                    lengthEnt2 = entities.containedLength(content, k);
                                    if (lengthEnt2 > 0 ){
                                        object = content.substring(k, lengthEnt2 + k);
                                        break;
                                    }
                                }
                                if (lengthEnt1!=0 && lengthEnt2 != 0 && object!="" && subject != ""){
                                    //System.out.println(new Triple(subject,relation,object).toString());
                                    result.add(new Triple(subject,relation,object));
                                }
                            }
                        }
                    }
            }
            return (result);
	}

	/**
	 * Given
	 * - a wikipedia file
	 * - a file with entity names
	 * - a seed subject
	 * - a seed relation
	 * - a seed object
	 * - a number of iterations
	 * in the args[], runs DIPRE
	 *
	 * Hints:
	 * - first find patterns for the facts
	 * - then find facts for the patterns
	 * - iterate this
	 * - Keep one single set of facts, and one single Trie of patterns.
	 *   It is OK if this makes the algorithm find again the same patterns,
	 *   and find again the same facts.
     * @param args
     * @throws java.io.IOException
	 */
	public static void main(String args[]) throws IOException {
            Triple triple = new Triple("Dublin","isCapitalOf","the Republic of Ireland");
            Set<Triple> seeds = new HashSet<>();
            Set<Triple> facts = new HashSet<>();
            seeds.add(triple);
            Trie patterns;
            FileWriter fileWriter= new FileWriter("fact.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            Trie entities = new Trie(new File("entities.txt"));
            for(int iterations = 0; iterations < 1; iterations++){
                patterns = findPatterns(new File(args[0]), seeds);
                for(Triple triple_aux: findFacts(new File(args[0]), patterns, triple.predicate, entities)){
                    seeds.add(triple_aux);
                }
            }
            for(Triple triple_print: seeds){
                bufferedWriter.write(triple_print.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
	}
}