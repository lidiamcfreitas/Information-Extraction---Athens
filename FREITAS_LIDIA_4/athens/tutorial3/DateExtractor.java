/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package athens.tutorial3;

import athens.Page;
import athens.Triple;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Turing
 */
public class DateExtractor extends Extractor {

    
    @Override
    public Triple extract(Page page) {
        

        int monthNumber = -1;

        String title    = page.title;
        String content  = page.content;
        
        String yearPattern      = "([1-2][0-9]{3})";
        String numeral          = "[1-9][0-9]*";
        String numEnding        = "(nd|rd|th)";
        String acbc             = "("+ numeral + " (BC|AC))";
        String capitalWord      = "([A-H[J-Z]][a-z]+)";
        String dayPattern       = "([0-9]|[0-2][0-9]|3[0-1])";
        String monthPatternNumber = "(0[0-9]|1[0-2]|[0-9])";
        String separator        = "(-|/)";
        String monthSeparator   = "january|february|march|april|may|june|july|august|september|october|november|december";
        String[] months = monthSeparator.split("\\|");
        
        String pattern1 = capitalWord + " " + dayPattern + ", "+ yearPattern;
        String pattern2 = capitalWord + " " + yearPattern;
        String pattern3 = monthPatternNumber+separator+dayPattern+separator+yearPattern;
        String pattern4 = dayPattern+separator+monthPatternNumber+separator+yearPattern;
        String pattern5 = numeral + " years ago";
        String pattern6 = yearPattern+"s";
        String pattern7 = yearPattern;
        String pattern8 = numeral+numEnding+" century";
        String pattern9 = acbc;
        
        Pattern pattern=Pattern.compile(pattern1+"|" + 
                pattern2 + "|" + pattern3 + "|" + pattern4 + "|" + 
                pattern5 + "|" + pattern6 + "|" + pattern7 + "|" + 
                pattern8 + "|" + pattern9);
        
        Matcher matcher=pattern.matcher(content);
        while(matcher.find()) {
            if(matcher.group(1)!=null){
                for(int i = 0; i < months.length; i++){
                    if (matcher.group(1).toLowerCase() == null ? months[i] == null : matcher.group(1).toLowerCase().equals(months[i]))
                        monthNumber = i + 1;
                }
                if (monthNumber !=-1)
                    return new Triple(title, "hasDate",  matcher.group(3) + "-" + monthNumber + "-" + matcher.group(2));
                else
                    return new Triple(title, "hasDate", matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3));
            }
            return new Triple(title, "hasDate", matcher.group());
        }
        return null;
    }
    
}
