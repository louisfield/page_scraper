package com.pagescraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
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
    public static final String[] GRADES_STRING = new String[]{
            "A*A*A*","A*A*A","A*AA","AAA","AAB","ABB","BBB","BBC","BCC","CCC","CCD","CDD","DDD"
    };
    public static void main(String[] args) {
        String search_test = "aberystwyth  Computer Science University Requirements";


        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        Document doc = null;
        try {
            doc = Jsoup.connect("http://google.com/search?q=" + search_test).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements links = doc.select("a[href]");
        for(Element el: links){
            try{


                String searchUrl = el.attr("abs:href");;;//+ URLEncoder.encode(search_test, "UTF-8");
                HtmlPage page = webClient.getPage(searchUrl);
                //System.out.println(page.toString());
                WebResponse response = page.getWebResponse();
                Whitelist whitelist = Whitelist.none();
                whitelist.addTags(new String[]{"p"});
                String safe = Jsoup.clean(response.getContentAsString(), whitelist);
                String[] safeSplit = safe.split("<p>");
                ArrayList<String> requirementSeen = new ArrayList<String>();


                for (String sub : safeSplit) {

                    sub = sub.replace("&nbsp;"," ").replace("-"," ").replaceAll("[^\\P{P}*]+", " ");

                    for(String word : sub.split(" ")){
                        if(Arrays.stream(GRADES_STRING).anyMatch(word::equals)){

                            requirementSeen.add(word);

                        }
                    }



                }
                HashMap<String, Integer> fgrades = new HashMap<String,Integer>();

                for (String val : requirementSeen) {
                    String[] grades = val.split("");
                    int finalval = 0;
                    for (String grade : grades){
                        if(GRADES.containsKey(grade)){
                            finalval += GRADES.get(grade);
                        }
                    }
                    fgrades.put(val, finalval);

                }
                Map.Entry<String, Integer> min = null;
                for (Map.Entry<String,Integer> ent : fgrades.entrySet()){
                    if (min == null || min.getValue() > ent.getValue()) {
                        min = ent;
                    }
                }
                if(!requirementSeen.isEmpty() && !fgrades.isEmpty()) {
                    System.out.println(requirementSeen);
                    System.out.println(min.getKey());
                    break;
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }



    }
}
