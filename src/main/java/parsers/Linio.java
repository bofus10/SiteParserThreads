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

import java.util.Iterator;
import java.util.LinkedHashSet;
/**
 *
 * @author Mauro
 */
public class Linio implements Runnable{
    
    public void run(){
            String url = null;
            try {
            LinkedHashSet<String> categorias = new LinkedHashSet<String>();
            Document doc;
            String j = "", page_num = "", international = "";
            String[] rango = {"price=0-100","price=100-150","price=150-500","price=500-99999",""};
            String[] rango2 = {"price=0-300","price=300-800","price=800-3000","price=3000-99999",""};
            boolean flag_categoria = true, flag_page = true;
            Iterator it = null;
            int i = 1, z = 0;
            
            
            do{
                    url = String.format("https://www.linio.com.ar%s%s", j,international);
                    //System.out.println(url);
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
                            .referrer("http://www.google.com")
                            .timeout(180000)
                            .get();
                        //System.out.println(doc);
                        
                    if(flag_categoria){
                        Elements categories = doc.getElementsByClass("nav-item");
                        for (Element category : categories) {
                            categorias.add(category.getElementsByAttribute("href").attr("href"));
                        }
                        
                        categorias.removeIf(s -> !s.contains("/c/"));
                        categorias.removeIf(s -> s.contains("/c/electrodomesticos"));
                        categorias.removeIf(s -> s.contains("/c/computacion"));
                        categorias.removeIf(s -> s.contains("belleza-y-cuidado-personal"));
                        categorias.add("/c/electrodomesticos/linea-blanca");
                        categorias.add("/c/electrodomesticos/pequenos-electrodomesticos");
                        categorias.add("/c/computacion/pc-escritorio");
                        categorias.add("/c/computacion/impresoras-y-scanners");
                        categorias.add("/c/computacion/almacenamiento");
                        categorias.add("/c/computacion/accesorios-de-computadoras");
                        categorias.add("/c/pc-portatil/notebooks");
                        categorias.add("/c/linea-blanca/climatizacion");
                        categorias.removeIf(s -> s.contains("libros-y-peliculas"));
                        //System.out.println(categorias.size());
                        it = categorias.iterator();
                        j = it.next().toString();
			international = "?is_international=0&price=0-100";
                        //System.out.println(categorias.toString());
                        flag_categoria = false;
                    }else{
                        if(flag_page){
                            //?is_international=0
                            Elements categories = doc.getElementsByClass("page-link page-link-icon");
                            if(categories.size() > 1){
                            page_num = categories.get(1).getElementsByAttribute("href").attr("href").replaceAll(".+page=", "");
                            flag_page = false;
                            }else{
                                page_num = "1";
                                //System.out.println("Numero Null");
                            }
                            //international = "?is_international=0&price=0-100&page=1";
                            //System.out.println("Numero de Paginas: "+page_num);
                        }
                           Elements products = doc.getElementsByClass("catalogue-product");
                           for(Element product : products){
                               
                               //System.out.println(product);
                                String name = product.getElementsByClass("specs-section").get(0).getElementsByAttribute("title").attr("title");
                                String link = product.getElementsByClass("specs-section").get(0).getElementsByAttribute("href").attr("href");
                                String sku = product.getElementsByAttributeValue("itemprop", "sku").attr("content");
                                
                                if(product.getElementsByClass("price-main-md").size() > 0){
                                String precio = product.getElementsByClass("price-main-md").get(0).text().replaceAll("\\$", "").replace(".", "").replace(",", ".");
                                //System.out.println("name: "+name+" link: "+link+" precio: "+precio+" sku: "+sku);
                                String item = String.format("('LN_%s','256',\"%s\",\"https://www.linio.com.ar%s\",'%s')",sku,name.replaceAll("\"", ""),link,precio.replaceAll("[^\\.0123456789]",""));
                                Data.getInstance().setColaParser(item);
                                }
                                //System.out.println(item);
                            }
                           //System.out.println(j);
            
                        i++;
                        if(i < Integer.parseInt(page_num)+1){
                            if(j.contains("celulares")){
                                international = String.format("?is_international=0&%s&page=%s", rango[z],i );
                            }else{
                                international = String.format("?is_international=0&%s&page=%s", rango2[z],i );
                            }
                            
                        }else{
                            z++;
                            i = 1;
                            if(j.contains("celulares")){
                                international = String.format("?is_international=0&%s&page=%s", rango[z],i );
                            }else{
                                international = String.format("?is_international=0&%s&page=%s", rango2[z],i );
                            }
                            flag_page = true;
                            
                            if(z > 3){
                                j = it.next().toString();
                                flag_page = true;
                                i = 1;
                                z = 0;
                                if(j.contains("celulares")){
                                    international = String.format("?is_international=0&%s&page=%s", rango[z],i );
                                }else{
                                    international = String.format("?is_international=0&%s&page=%s", rango2[z],i );
                                }
                            }
                        }
                        
                        //System.out.println(url);
                         

                    }
                    

            }while(it.hasNext()); 
            System.out.println("Linio Finished");
            Data.getInstance().setLinio(true);
            
        } catch (Exception ex) {
            System.out.println(ex);
            Data.getInstance().setLinio(true);
                System.out.println(url);
            
        }
    }
}