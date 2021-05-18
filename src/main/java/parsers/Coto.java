/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import Processor.Data;
import Processor.SiteParser;
import java.io.IOException;
import javax.xml.ws.http.HTTPException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Mauro
 */
public class Coto implements Runnable{
    String categoria = null;
    
    public Coto(String x){
        categoria = x;
    }
    
    public void run(){
        String item = null;
        try {
            Document doc;
            
            int j = 1;
            int count = 0;
            do{
                try {
                    String url = String.format("%s?Nf=product.endDate%%7CGTEQ+1.5484608E12%%7C%%7Cproduct.startDate%%7CLTEQ+1.5484608E12&No=%d&Nr=AND%%28product.language%%3Aespa%%C3%%B1ol%%2COR%%28product.siteId%%3ACotoDigital%%29%%29&Nrpp=100",categoria ,j);
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .referrer("http://www.google.com")
                            .timeout(180000)
                            .get();
                    //System.out.println(doc);
                    Element content = doc.getElementById("products");
                    //System.out.println(content);
                    //Elements links = content.getElementsByTag("a");
                    count = Integer.parseInt(doc.getElementsByClass("titleSearchResults").text().replaceAll("[^\\.0123456789]",""));
                    //System.out.println("Result Count: "+ doc.getElementsByClass("titleSearchResults").text().replaceAll("[^\\.0123456789]",""));
                    Elements products = content.getElementsByClass("clearfix");
                    //Elements articulos = content.getElementsByClass("product_info_container");
                    //Elements prices = content.getElementsByClass("info_discount");
                    //System.out.println(url);
                    for (Element product : products) {
                        //System.out.println(product);
                        Elements Name = product.getElementsByClass("descrip_full");
                        Elements Link = product.getElementsByAttribute("href");
                        String SKU = product.getElementsByClass("descrip_full").attr("id").replaceAll("descrip_full_","");
                        // 2 Posibles precios
                        String Precio = "";
                        if(!Name.text().isEmpty()){
                            if(product.getElementsByClass("price_discount").size() > 1){
                               Precio = product.getElementsByClass("price_discount").get(1).text().replaceAll("\\$", ""); 
                            }else{
                               Precio = product.getElementsByClass("price_discount").text().replaceAll("\\$", "");
                            }
                            if(Precio.isEmpty()){
                                Elements Precio1 = product.getElementsByClass("atg_store_newPrice");
                                if(Precio1.isEmpty()){
                                    //System.out.println("ACA producto: "+Link.attr("href"));
                                    //System.out.println(url);
                                }else{
                                    //System.out.println("Coto_"+SKU+","+Name.text().replaceAll("\"", "")+",https://www.cotodigital3.com.ar"+Link.attr("href")+","+Precio);
                                    item = String.format("('Coto_%s','210',\"%s\",\"https://www.cotodigital3.com.ar%s\",'%s')",SKU,Name.text().replaceAll("\"", ""),Link.attr("href"),Precio1.get(0).text().replaceAll("[^\\.0123456789]","").replaceAll("c/u", ""));
                                    Data.getInstance().setColaParser(item);
                                }
                            }else{
                                //System.out.println("Coto_"+SKU+","+Name.text().replaceAll("\"", "")+",https://www.cotodigital3.com.ar"+Link.attr("href")+","+Precio);
                                item = String.format("('Coto_%s','210',\"%s\",\"https://www.cotodigital3.com.ar%s\",'%s')",SKU,Name.text().replaceAll("\"", ""),Link.attr("href"),Precio.replaceAll("[^\\.0123456789]","").replaceAll("c/u", ""));
                                Data.getInstance().setColaParser(item);
                            }
                        }

                    }
                    j = j+50;
                } catch (IOException | NumberFormatException | HTTPException ex) {
                    System.out.println("Coto:81"+ex);
                }
                
            }while(j < count);
            SiteParser.Write2Log("Parser: "+categoria, "vendors");
        } catch (IOException ex) {
            Data.getCoto_categorias().remove(categoria);
            System.out.println("Coto:87"+ex);
            System.out.println(item);
        }
        
        Data.getCoto_categorias().remove(categoria);
    }
    
}
