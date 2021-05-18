/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import Processor.Data;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class Falabella implements Runnable{
  
    public void run(){
       System.out.println("Start Falabella");
       String cate[] = {"cat10026/Otros", "cat10024/Mujer", "cat10022/Belleza", "cat10020/Deportes", "cat10018/Ninos", "cat10016/Muebles", "cat10014/Deco-hogar", "cat10012/Dormitorio", "cat10010/Tecnologia", "cat10008/Electrodomesticos"};
       String url = "";
       boolean page = true;
       Document doc =null;
       int num = 1, PageMax = 0;
       JSONArray arr = null;
       
	try {
            for (int i = 0; i < cate.length; i++) {
            page = true;
            num = 1; PageMax = 0;
                
            do{    
            url = String.format("https://www.falabella.com.ar/falabella-ar/category/%s?page=%d",cate[i],num);
            doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36")
            .referrer("http://www.google.com")
            .timeout(180000)     
            .get();
            
                //System.out.println(url);
                //exit(0);
                if(page){
                 String pages[] = doc.select("li[class*=pagination-item]").text().split(" ");
                 PageMax = Integer.parseInt(pages[pages.length-1]);
                 //System.out.println("Page: "+PageMax);
                 //exit(0);
                 //System.out.println(doc.getElementsByClass("sorting__info").text().replaceAll(".+de ", "").replaceAll(" Items.+", ""));
                 page = false;  
                 //exit(0);
                }
                
                JSONObject jsonObject = new JSONObject(doc.select("script[id^=__NEXT_DATA__]").toString().replaceAll("<script id=\"__NEXT_DATA__\" type=\"application/json\">","").replaceAll("</script>", ""));
                
                try{
                arr = (JSONArray) jsonObject.getJSONObject("props").getJSONObject("pageProps").get("results");
                //System.out.println(arr);
               
            
                if(arr.length() == 0){
                    System.out.println("Falabella Vacio");
                 }else{
                     for (int j = 0; j < arr.length(); j++)
                     {
                         String id = String.valueOf(arr.getJSONObject(j).optInt("skuId"));
                         if(id.equals("0")){
                             id = arr.getJSONObject(j).optString("skuId");
                         }
                         String articulo = arr.getJSONObject(j).getString("displayName").replaceAll("\"", "");
                         String link = arr.getJSONObject(j).getString("url").replaceAll("\"", "");
                         
                         String price = "";
                         
                         JSONArray prices = (JSONArray) arr.getJSONObject(j).get("prices");
                         for (int z = 0; z < prices.length(); z++)
                         {
                             if (prices.getJSONObject(z).getString("label").contains("Precio")){
                                 //price = String.valueOf(prices.getJSONObject(z).getInt("price")).replace(",", "").replaceAll("[^\\.0123456789]","");  
                                 price = prices.getJSONObject(z).getJSONArray("price").toString().replace(".", "").replaceAll("[^\\.0123456789]","");  
                                 break;
                             }
                             
                        }
                        //System.out.println("name: "+articulo+" sku: "+id+" link: "+link+" price: "+price);

                        //System.out.println("id: "+id + " articulo "+ articulo + " price" + price + "link" + link);
                        if(!price.isEmpty()){
                        String item = String.format("('FBL_%s','183',\"%s\",\"%s\",'%s')",id,articulo,link,price);
                        //System.out.println(item);
                        Data.getInstance().setColaParser(item);
                        }
                     }      
            
                } 
                }catch (JSONException ex){
                 System.out.println(ex);
                 //System.out.println(url);
                 //System.out.println(arr);
                }
                num++;
                //System.out.println("NUMEROOOOOOOOOOOOOOOOOOOOOOOOOOO: "+num);
              }while(num <= PageMax);  
                //exit(0);
            } //for         
         
          System.out.println("Falabella Finished");
          Data.getInstance().setFalabella(true);
        } catch (IOException | NumberFormatException |NullPointerException ex) {
            System.out.println("Err Falabella Finished");
            System.out.println(ex);
            System.out.println(url);
            System.out.println(arr);
            Data.getInstance().setFalabella(true);
            //Data.getCategorias().remove(Categoria);
        }
            

        } 
    
}
