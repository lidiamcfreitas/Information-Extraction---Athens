/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package athens.tutorial5;

import athens.Page;
import athens.Triple;
import athens.tutorial3.Extractor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Turing
 */
public class ImprovedTypeExtractor extends Extractor{
    List<String> patterns = new ArrayList<>();
    @Override
    public Triple extract(Page page) {
        
        patterns.add("(((?:(?:[A-Z][a-z]+/NNP) )+)is/VBZ (?:[a-z]+)/DT (?:(?:[^.| ]+)/JJ)* (?:example/NN of/IN (?:[a-z]+)/DT (?:(?:[^.| ]+)/JJ)* )?((?:(?:[^.| ]+/NN)| )+))");
        // line 401 problem
        String p = "";
        for (String pattern : patterns) {
            p += "|" + pattern;
        }
        p = p.substring(1); // remove first |
        
        Pattern pattern=Pattern.compile(p);
        Matcher matcher=pattern.matcher(page.content);
        while(matcher.find()) {
            //System.out.println(matcher.group());
            for (int i = 1; i < matcher.groupCount(); i+=2){
                if(matcher.group(i)!=null){
                    //System.out.println(matcher.group(i));
                    return new Triple(matcher.group(i+1).replaceAll("/NNP", "").trim(), "type", matcher.group(i+2).replaceAll("/NN", "").trim());
                }
            }
        }
        return null;
    }
    
}
