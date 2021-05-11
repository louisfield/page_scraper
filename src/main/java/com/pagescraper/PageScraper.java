package com.pagescraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
public class PageScraper {
    public static final Map<String, Integer> GRADES = mapinit();
    private static Map<String, Integer> mapinit(){
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("Z",56);
        map.put("A",48);
        map.put("B",40);
        map.put("C",32);
        map.put("D",24);
        map.put("E",16);
        return Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {
        String search_test = "Android";


        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        try{
            String searchUrl = "https://www.undergraduate.study.cam.ac.uk/courses/computer-science";//+ URLEncoder.encode(search_test, "UTF-8");
            HtmlPage page = webClient.getPage(searchUrl);
            //System.out.println(page.toString());
            WebResponse response = page.getWebResponse();
            Whitelist whitelist = Whitelist.none();
            whitelist.addTags(new String[]{"p", "ul"});
            String safe = Jsoup.clean(response.getContentAsString(), whitelist);
            String[] safeSplit = safe.split("<p>");
            ArrayList<String> requirementSeen = new ArrayList<String>();

            System.out.println(safeSplit[0]);
            for (String sub : safeSplit) {
                if( sub.contains("AAA") || sub.contains("AAB") || sub.contains("ABB") || sub.contains("BBB") || sub.contains("BBC") || sub.contains("BCC") || sub.contains("CCC")){
                    System.out.println(sub);
                    requirementSeen.add(sub.replace("&nbsp;"," ").replace("</p>","").replaceAll("/[^a-zA-Z ]/g", ""));
                } else if(sub.contains("A*A*A*")){
                    requirementSeen.add("ZZZ");
                } else if(sub.contains("A*A*A")){
                    requirementSeen.add("ZZA");
                } else if(sub.contains("A*AA")){
                    requirementSeen.add("ZAA");
                }
            }
            List<String> requirementFinal = new ArrayList<String>();
            for (String res : requirementSeen) {
                String[] result = res.split(" ");
                for (String word: result){

                    if(word.length() < 7 && word.toUpperCase().equals(word) && !word.contains("C") && !word.contains("U") && !word.contains("L") & !word.matches("[0-9]+")) {
                        requirementFinal.add(word);
                    }
                }
            }
            ArrayList<Integer> finalvals = new ArrayList<Integer>();
            for (String val : requirementFinal) {
                String[] grades = val.split("");
                int finalval = 0;
                for (String grade : grades){
                    if(GRADES.containsKey(grade)){
                        finalval += GRADES.get(grade);
                    }
                }
                finalvals.add(finalval);
            }


            System.out.println(requirementFinal);
            System.out.println(finalvals);

        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
