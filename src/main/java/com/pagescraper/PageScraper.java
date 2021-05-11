package com.pagescraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.net.URLEncoder;

public class PageScraper {
    public static void main(String[] args) {
        String search_test = "Android";


        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        try{
            String searchUrl = "https://www.pcmag.com/picks/the-best-android-phones";//+ URLEncoder.encode(search_test, "UTF-8");
            HtmlPage page = webClient.getPage(searchUrl);
            //System.out.println(page.toString());
            WebResponse response = page.getWebResponse();
            Whitelist whitelist = Whitelist.none();
            whitelist.addTags(new String[]{"p", "ul"});
            String safe = Jsoup.clean(response.getContentAsString(), whitelist);
            System.out.println(safe);
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
