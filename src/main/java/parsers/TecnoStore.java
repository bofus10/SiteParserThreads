/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import Processor.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Mauro
 */
public class TecnoStore implements Runnable{
    
    
    public void run(){
        try {
            Document doc;
            int i = 1;
            String item = null;
            
            while(true){
                    String url = String.format("https://tecnostores.com.ar/categoria-producto/categoria/page/%s/", i);
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .referrer("http://www.google.com")
                            .timeout(180000)
                            .get();
                        //System.out.println(doc);
                    
                        Elements categories = doc.getElementsByClass("item-info");
                        //System.out.println(categories);
                        for (Element category : categories) {
                            //System.out.println(category);
                            String link = category.getElementsByAttribute("href").get(0).attr("href");
                            String name = category.getElementsByAttribute("target").text();
                            String sku = category.getElementsByAttribute("data-product_id").attr("data-product_id");
                            String price[] = category.getElementsByClass("price").text().replaceAll("\\$", ",").split(",");
                            //System.out.println(price.length);
                           //System.out.println(category.getElementsByClass("price").text().replaceAll("\\$", ","));
                           String precio = null;
                            if(price.length > 1){
                                if(price.length == 3){
                                     //System.out.println(price[2]);
                                     precio = price[2];
                                 }else{
                                     //System.out.println(price[1]);
                                     precio = price[1];
                                 }

                                 //System.out.println(link+" "+name.replace("?","")+" "+sku+" "+precio);
                                 item = String.format("('TS_%s',\"255\",\"%s\",\"%s\",'%s')",sku,name.replace("?",""),link,precio.replace(".", "").replace(",", "."));
                                 Data.getInstance().setColaParser(item);
                                 //System.out.println(item);
                            }        
                        }

                  i++;
            }
            
        } catch (Exception ex) {
            System.out.println(ex);
            
        }
    }
}
    

