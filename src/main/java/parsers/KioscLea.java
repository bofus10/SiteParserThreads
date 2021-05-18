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
public class KioscLea implements Runnable
{   
    
    public void run()
    {
	try {

            Document doc;

                    String url = String.format("http://www.elkiosquitodelea.com.ar/articulos.asp");
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .referrer("http://www.google.com")
                            .timeout(180000)
                            //.ignoreContentType(true)
                            .get();
                    //System.out.println(doc);

                    
                    Elements products = doc.getElementsByClass("col-sm-12 col-md-4 col-lg-3 portfolio-item item");
                                    for(Element product : products){
                                        //System.out.println(product);
                                        String name[] = product.getElementsByClass("portfolio-details").text().split("\\$");
                                        String link = product.getElementsByAttribute("title").attr("href");
                                        String id = link.replaceAll(".+articulo=", "").replaceAll("\\&.+", "");
                                        if(name.length > 1){ 
                                            //System.out.println(id+","+name[0].replaceAll(" $", "") + "," + name[1].replaceAll(" ", "") + ",http://www.elkiosquitodelea.com.ar/" + link);
                                            String item = String.format("('KL_%s','317',\"%s\",\"http://www.elkiosquitodelea.com.ar/%s\",'%s')",id,name[0],link,name[1].replace(".", "").replace(",", "."));
                                            Data.getInstance().setColaParser(item);
                                        }
                                            
                                        //System.out.println(product.getElementsByClass("portfolio-details").text());
                                    }
                    
            

            System.out.println("KioscLea Finished");
            Data.getInstance().setKLea(true);
            
        } catch (Exception ex) {
            Data.getInstance().setKLea(true);
            System.out.println(ex);
            System.out.println("Err.KioscLea Finished");
        }
        
    }


}
