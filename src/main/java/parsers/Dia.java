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
public class Dia implements Runnable{

    public void run()
    {
        System.out.println("Start Dia");
        try {
            String categorias[] = {"electro-hogar?PS=16&O=OrderByBestDiscountDESC&PageNumber=","tecnologia?PS=16&O=OrderByBestDiscountDESC&PageNumber=","muebles-y-deco?PS=16&O=OrderByBestDiscountDESC&PageNumber=","colchones-y-sommiers?PS=16&O=OrderByBestDiscountDESC&PageNumber="};
            Document doc;
            boolean flag_page = false;
            int i = 0, j = 1;
            //System.out.println("Start Dia");

            while(i < categorias.length){
                do{
                        String url = String.format("https://diaonline.supermercadosdia.com.ar/%s%d",categorias[i],j);
                        doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                                .referrer("http://www.google.com")
                                .timeout(180000)
                                .get();
                        //System.out.println(url);
                        //System.out.println(doc);

                            if(!doc.getElementsContainingText("No encontramos lo que estás buscando").text().isEmpty()){
                                //System.out.println("Finalizo");
                                flag_page=true;
                            }else{
                                Elements products = doc.getElementsByClass("product__content");
                                    for(Element product : products){

                                         //System.out.println(product);
                                         String sku = product.getElementsByAttribute("rel").attr("rel");
                                         String name = product.getElementsByAttribute("href").get(0).text().replaceAll("\"", "");
                                         String link = product.getElementsByAttribute("href").get(0).attr("href");
                                         //String price = product.getElementsByClass("product__price").tagName("span").text();
                                         String best_price = product.getElementsByClass("best-price").text().replaceAll("\\$ ", "").replace(".", "").replace(",", ".");
                                         
                                         if(!best_price.isEmpty()){
                                            //System.out.println(link+" "+name+";"+best_price);
                                            String item = String.format("('DIA_%s','278',\"%s\",\"%s\",'%s')",sku,name,link,best_price);
                                            Data.getInstance().setColaParser(item);
                                         }
                                     }

                                j++;  
                            }


                }while(!flag_page);
               flag_page = false;
               i++; 
               j = 1;
            }
            Data.getInstance().setDia(true);
            System.out.println("SupermercadoDia Finalizado");   
            
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("Err.SupermercadoDia Finalizado"); 
            Data.getInstance().setDia(true);
            
        }
        
    }
}