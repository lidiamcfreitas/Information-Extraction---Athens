/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package athens.tutorial3;

import athens.Page;
import athens.Triple;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Turing
 */
public class TypeExtractor extends Extractor{


    @Override
    public Triple extract(Page page) {

        String title = page.title;
        String firstSentence = page.firstSentence();
        firstSentence = firstSentence.replace("mostly ", "");
        firstSentence = firstSentence.replace("hard ", "");
        String typeOf = "(?:type of )";
        String lowWord = "([a-z]+)";
        String subString = "(?:[a-z]|-|\\s)+)";
        String aAndThe = "(?:|a|an|the)";
        List<String> patterns = new ArrayList<>();
        //patterns.add("(is (?:a|an|the) (?:[a-z]ing)* ([a-z]+))");
        //patterns.add("((?:[A-Z][a-z]+ )* such as ([a-z]+))");
        patterns.add("(is "+aAndThe+" kind of "+lowWord+")");
        patterns.add("(is "+aAndThe+" "+lowWord+" (?:in|which))");
        patterns.add("(is the (?:(?:[a-z]+|[0-9]+)(?:st|nd|rd|th)) "+lowWord+" of)");
        patterns.add("(is a (?:[a-z]|-|,|\\s)+"+typeOf+"((?:[a-z]|-)+))");
        patterns.add("(is a "+typeOf+"?("+subString+" for)");
        patterns.add("(is a "+typeOf+"?("+subString+" that)");
        patterns.add("(is a (?:"+subString+" ((?:[a-z])+) of)");
        
        String p = "";
        for (String pattern : patterns) {
            p += "|" + pattern;
        }
        p = p.substring(1); // remove first |
        
        Pattern pattern=Pattern.compile(p);
        Matcher matcher=pattern.matcher(firstSentence);
        while(matcher.find()) {
            //System.out.println(matcher.group());
            for (int i = 1; i < matcher.groupCount(); i+=2){
                if(matcher.group(i)!=null){
                    //System.out.println(matcher.group(i));
                    //System.out.println(i/2);
                    return new Triple(title, "type", matcher.group(i+1).trim());
                }
            }
        }
        return null;
    }
}
