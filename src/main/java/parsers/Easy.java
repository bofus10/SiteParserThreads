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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/* 
public class Easy implements Runnable{
   
   static int indice = 1;
    public static boolean hayMas = true;
    
    public void run(){
    	
         do {
             try {
                 Thread[] arrayProcesos = new Thread[10];
                 for(int i = 0; i < arrayProcesos.length; i++) {
                     arrayProcesos[i] = new Thread(new getHTML(indice));
                     arrayProcesos[i].start();
                     indice = indice + 100;
                 }
                 
                 for (Thread proceso : arrayProcesos)
                     proceso.join();
             } catch (InterruptedException ex) {
                 Logger.getLogger(Easy.class.getName()).log(Level.SEVERE, null, ex);
             }
        	
        }while(hayMas);
         Data.getInstance().setEasy(true);
    } 
    
}

class getHTML implements Runnable {
	int i = 0;
	
	public getHTML(int indice) {
		i = indice;
	}

	public void run() {
		
		int limite = i + 100;
		Document doc = null;
        for(int j = i;j < limite;j=j+10) {
        
        String url = "https://www.easy.com.ar/tienda/es/easyar/search/AjaxCatalogSearchResultContentView?searchType=1001&beginIndex=" + j + "&sType=SimpleSearch&pageSize=10&catalogId=10051&storeId=10151";
        try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
			       .referrer("http://www.google.com")
			       .timeout(0)
			       .get();
			
			if(doc.hasText()) {
				
				
		        //System.out.println(doc);
		        Elements articulos = doc.getElementsByAttributeValueContaining("id", "WC_CatalogSearchResultDisplay_div");
		        for (Element articulo : articulos) {
		        	Element tituloLinkId = articulo.getElementsByAttributeValueMatching("id", "WC_CatalogEntryDBThumbnailDisplayJSPF_.*_link_.*").get(0);
		        	if(articulo.getElementsByClass("thumb-price-e").size()>0 && !articulo.getElementsByClass("thumb-price-e").get(0).text().isEmpty()) {
			        	String precio = articulo.getElementsByClass("thumb-price-e").get(0).text().replaceAll("\\$", "").replace(".", "").replace(",", ".");
			        	String titulo = null;
			        	String id = tituloLinkId.attr("id").substring(tituloLinkId.attr("id").indexOf("_") + 1, tituloLinkId.attr("id").lastIndexOf("_"));
			        	if(tituloLinkId.hasAttr("title")){ 
			        		titulo = tituloLinkId.attr("title");
			        	}else {
			        		titulo = tituloLinkId.text().replace("...", "");
			        	}
			        	
			        	String item = String.format("('Easy_%s','170',\"%s\",\"%s\",'%s')",id.substring(id.indexOf("_") + 1, id.lastIndexOf("_")),titulo.replaceAll("\"", ""),tituloLinkId.attr("href"),precio);
                                        //System.out.println(item);
                                        Data.getInstance().setColaParser(item);
			        	}
		        	}
		         i = i + 10;
		         
				}else {
					Easy.hayMas = false;
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (j - 10 > limite - 100) {
				j = j - 10;
			}else {
				j = limite - 100;
			}
		}

	        
		}

}
		
	}
	
*/
