package com.pagescraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.net.URLEncoder;
import java.util.*;

public class PageScraper {
    public static final Map<String, Integer> GRADES = mapinit();
    private static Map<String, Integer> mapinit(){
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("A",56);
        map.put("B",48);
        map.put("C",40);
        map.put("D",32);
        map.put("E",24);
        map.put("F",16);
        return Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {
        String search_test = "Android";


        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        try{
            String searchUrl = "https://www.ucl.ac.uk/chemistry/study-here/undergraduate-degrees/bsc-chemistry/chemistry-bsc";//+ URLEncoder.encode(search_test, "UTF-8");
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
                if(sub.contains("AAA") || sub.contains("AAB") || sub.contains("ABB") || sub.contains("BBB") || sub.contains("BBC") || sub.contains("BCC") || sub.contains("CCC")){

                    requirementSeen.add(sub.replace("&nbsp;"," ").replace("</p>","").replaceAll("/[^a-zA-Z ]/g", ""));
                }
            }
            ArrayList<String> requirementFinal = new ArrayList<String>();
            for (String res : requirementSeen) {
                String[] result = res.split(" ");
                for (String word: result){
                    if(word.length() == 3 && word.toUpperCase().equals(word)) {
                        requirementFinal.add(word);
                    }
                }
            }
            System.out.println(requirementFinal);

        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
