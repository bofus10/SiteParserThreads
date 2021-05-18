/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import Processor.Data;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Mauro
 */
public class Necxus implements Runnable{
    
    public void run(){
        
    try {
            LinkedHashSet<String> categorias = new LinkedHashSet<String>();
            Iterator it = null;
            Document doc;
            String j = "", cat ="";
            boolean flag_categoria = true, page = true;
            Integer p = 1;

                do{
                    try{
                    while(page){
                    String url = String.format("https://www.necxus.com.ar/%s%s",cat,j);
                    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .referrer("http://www.google.com")
                            .timeout(180000)
                            .ignoreContentType(true)
                            .get();
                    //System.out.println(doc);
                    if(flag_categoria){
                        Elements contents = doc.getElementsByClass("nav navbar-nav navbar");
                        Elements categories = contents.get(0).getElementsByAttribute("style");
                        //System.out.println(categories);
                        for (Element category : categories) {
                            if(!category.getElementsByTag("a").attr("href").replaceAll(".+categorias/(.+)/.+", "$1").replaceAll("/.+", "").isEmpty()){
                            categorias.add("json/category_"+category.getElementsByTag("a").attr("href").replaceAll(".+categorias/(.+)/.+", "$1").replaceAll("/.+", "")+"___");
                            //System.out.println(category.getElementsByTag("a").attr("href").replaceAll("page/1", "page/"));
                            }
                        }
                        categorias.removeIf(s -> s.contains("http"));

                        //System.out.println(categorias.toString());
                        it = categorias.iterator();
                        cat = it.next().toString();
                        j = "1_.json";
                        flag_categoria = false;
                    }else{

                    //System.out.println(doc.getElementsByTag("body").text().replaceAll("^\\[", "").replaceAll("\\]$", ""));
                    //JSONObject obj = new JSONObject(doc.getElementsByTag("body").text());
                
                    JSONArray arr = new JSONArray(doc.getElementsByTag("body").text());
                    
                    //System.out.println(arr.toString());
                    
                    if(arr.length() == 0){
                           System.out.println("LOGG Hardware Vacio");
                           System.exit(0);
                        }else{
                            for (int i = 0; i < arr.length(); i++)
                            {
                                int id = arr.getJSONObject(i).getInt("id");
                                String articulo = arr.getJSONObject(i).getString("name").replaceAll("[^a-zA-Z0-9 ]+","");
                                //String price = String.valueOf(arr.getJSONObject(i).getInt("precio_web")).replace(".", "").replace(",", ".");
                                String link = arr.getJSONObject(i).getString("link").replaceAll("\"", "");
                               String price = arr.getJSONObject(i).getString("price").replace(".", "").replace(",", ".");
                               //System.out.println("id: "+id + " articulo "+ articulo + " price" + price + "link" + link);
                               if(!price.isEmpty()){
                               String item = String.format("('Necxus_%s','218',\"%s\",\"https://www.necxus.com.ar%s\",'%s')",id,articulo,link,price);
                               //System.out.println(item);
                               Data.getInstance().setColaParser(item);
                               }
                            }  
                        } 
            
                    }
                 p++;   
                 j = j.replaceAll(".+_\\.json", p+"_\\.json");
                }
             }catch(JSONException ex){
                //System.out.println("END!");
                p = 1;  
                j = "1_.json";
                cat = it.next().toString();
             }
            }while(it.hasNext());     

          System.out.println("Necxus Finished");
          Data.getInstance().setNecxus(true);
       
        } catch (IOException ex) {
            System.out.println("Error Nexcus: "+ex);
            Data.getInstance().setNecxus(true);
        }
    }
    
}
