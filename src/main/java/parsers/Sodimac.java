/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import Processor.Data;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Sodimac implements Runnable{
    String url = "";
    public void run(){
        System.out.println("Start Sodimac");
        try {
            Document cat;
            boolean flag_page = true;
               int k = 1;
               int NumMax = 0; 
               do { 
        	//String MainCategory = title.getElementsByClass("fb-masthead__child-links__item__link js-masthead__child-links__item__link").attr("href");
               //CategoryURL = String.format("%s?isPLP=1&page=%d", Categoria,k);
               String url = String.format("https://www.sodimac.com.ar/sodimac-ar/search/?No=%s",k);
               cat = Jsoup.connect(url)
               .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
               .referrer("http://www.google.com")
               .timeout(180000)
               .maxBodySize(0) 
               .get();
               
                 //System.out.println(cat.body());
                 //Thread.sleep(1000);

                if(flag_page){
                    
                    NumMax = Integer.parseInt(cat.getElementsByClass("tab-box").text().replaceAll(".+\\(", "").replaceAll("\\).+", ""));
                    //System.out.println(cat.getElementsByClass("tab-box").text().replaceAll(".+\\(", "").replaceAll("\\).+", ""));
                    flag_page = false;
                    
               }
  
               Elements products = cat.body().getElementsByClass("col-md-3 col-xs-12 col-sm-12 item jq-item one-prod");
                for(Element product : products){

                     //System.out.println(product);
                     String sku = product.getElementsByClass("sku").get(0).text().replaceAll("SKU:", "");
                     String name = product.getElementsByAttribute("title").get(1).attr("title");
                     String link = product.getElementsByAttribute("title").get(1).attr("href");
                     String price = product.getElementsByAttributeValue("itemprop","price").get(0).text();
                     //String best_price = product.getElementsByClass("best-price").text().replaceAll("\\$ ", "").replace(".", "").replace(",", ".");

                     //System.out.println(product.getElementsByAttributeValue("itemprop","price").get(0).text());
                     String item = String.format("('SM_%s','272',\"%s\",\"https://www.sodimac.com.ar%s\",'%s')",sku,name.replaceAll("\"", ""),link,price.replace(".", "").replace(",", ".").replaceAll("[^\\.0123456789]",""));
                     Data.getInstance().setColaParser(item);
                     //System.out.println("sku: "+sku+" name: "+name+" link: "+link+" price: "+price);
                     
                 }
               
               
                
             
               
            //System.out.println("PageNum: "+k);           
            k=k+32;
            Thread.sleep(100);
         } while (k <= NumMax); 
                
          //Thread.sleep(1000);
          Data.getInstance().setSodimac(true);
          System.out.println("Sodimac Finished");
        } catch (IOException | InterruptedException | NullPointerException ex) {
            System.out.println("Err Sodimac Finished");
            System.out.println(ex);
            System.out.println(url);
            Data.getInstance().setSodimac(true);
            //Data.getCategorias().remove(Categoria);
        }
        
       
        
    }
    
}
