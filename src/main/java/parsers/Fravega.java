/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import Processor.Data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;



public class Fravega implements Runnable{
   
    public void run(){
        System.out.println("Start Fravega");
        String url = "";
        boolean page = true;
        Document doc =null;
        
        try {
         int PageMax = 0, i =  1;

         do{

            url = String.format("https://www.fravega.com/l/?page=%d",i);
            doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36")
            .referrer("http://www.google.com")
            .timeout(180000)     
            .get();

              //System.out.println(doc);
              //Thread.sleep(1000);

             if(page){
                 String pages[] = doc.getElementsByClass("ant-pagination-item").text().split(" ");
                 PageMax = Integer.parseInt(pages[pages.length-1]);
                 //System.out.println("Page: "+PageMax);
                 //System.out.println(doc.getElementsByClass("sorting__info").text().replaceAll(".+de ", "").replaceAll(" Items.+", ""));
                 page = false;  
                 //exit(0);
             }

                Elements products = doc.select("div[class^=ProductCard__Card]");
                //System.out.println(products);

                for(Element product : products){

                    //System.out.println(product);


                  String sku = product.getElementsByTag("a").attr("href").replaceAll(".+-(\\d+|\\w+)$", "$1");
                  String name = product.select("h4[class^=PieceTitle-sc]").text().replaceAll("\"", "");
                  String link = product.getElementsByTag("a").attr("href").replaceAll("\"", "");
                  String price = product.select("span[class^=ListPrice-sc]").text().replace(".", "").replaceAll("[^\\.0123456789]","");

                  //System.out.println("Name: "+name+" link: "+link+" sku: "+sku+" price: "+price);

                  if(!price.isEmpty()){
                  String item = String.format("('Fravega_%s','181',\"%s\",\"https://www.fravega.com%s\",'%s')",sku,name,link,price);
                  //System.out.println(item);
                  Data.getInstance().setColaParser(item);
                  }

               }  

              i++;
         }while(i < PageMax);
               
 
         System.out.println("Fravega Finished");
         Data.getInstance().setFravega(true);
        } catch (NumberFormatException e) {
            System.out.println(e);
            Data.getInstance().setFravega(true);
        } catch (IOException ex) {
            System.out.println(ex);
            Data.getInstance().setFravega(true);
        }
        
        Data.getInstance().setFravega(true);
        
    }
    
    
}





////////// OLD Version ///////////////
// Uses API from: https://developers.vtex.com/vtex-developer-docs/reference/catalog-api-product
/*
public class Fravega implements Runnable{

    static ArrayList<String> categorias = new ArrayList<String>();
    static String buscarPagina = null;

    public void run(){
        try {
                try {
                    obtenerCategorias();
                    obtenerArticulos(); 
                    System.out.println("Fravega Finished");
               } 
               catch (Exception ex) {
                   System.out.println("Frav: "+ex);
                   Data.getInstance().setFravega(true);
               }

        } catch (NumberFormatException e) {
            System.out.println(e);
            Data.getInstance().setFravega(true);
        }
        
        Data.getInstance().setFravega(true);
        
    }
    
       public static void obtenerArticulos() throws IOException, InterruptedException {
    	
    	for (int fromI = 0; fromI < categorias.size(); fromI++) {
    		int cantidadPaginas = obtenerCantidadPaginas(categorias.get(fromI));
    		for (int i = 1; i <= cantidadPaginas ; i++) {
    			
    		Document doc = Jsoup.connect("https://www.fravega.com/" + buscarPagina + i).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
    		        .referrer("http://www.google.com")
    		        .timeout(300000)
    		        .get();
    		Elements articulos = doc.getElementsByClass("wrapData");
    		for (Element producto : articulos) {
    			if (producto.getElementsByAttribute("title").size() > 1) {
    				Element producto2 = producto.getElementsByAttribute("title").get(1);
    				String link = producto2.attr("href");
    				String precio = producto2.getElementsByClass("BestPrice").text().replace(".", "").replace(",", ".");
    				//System.out.println("Fravega_"+link.substring(link.lastIndexOf("-") + 1, link.length() - 2) + ",Fravega," + producto2.attr("title") + "," + link + "," + precio.substring(2, precio.length()));
                                String item = String.format("('Fravega_%s','181',\"%s\",\"%s\",'%s')",link.substring(link.lastIndexOf("-") + 1, link.length() - 2),producto2.attr("title").replaceAll("\"", ""),link,precio.substring(2, precio.length()));
                                Data.getInstance().setColaParser(item);
                        
                        }

    		}
    		
    		}
    	}

    }
    	
    public static BufferedReader coneccion(String URL) throws IOException {
    	HttpURLConnection cLogin = (HttpURLConnection) new URL(URL).openConnection();
		cLogin.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
		cLogin.setUseCaches(false);
		cLogin.setDoOutput(true);
		cLogin.setDoInput(true);
		Object obj = cLogin.getContent();
	    BufferedReader BF =  new BufferedReader(new InputStreamReader(cLogin.getInputStream()));
	    //System.out.println(BF.readLine());
            cLogin.disconnect();
	    return BF;
    }
    
    public static int obtenerCantidadPaginas(String URL) throws IOException {
		Document doc = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
		        .referrer("http://www.google.com")
		        .timeout(300000)
		        .get();
		Elements articulos = doc.getElementsByClass("vitrine resultItemsWrapper");
		for (Element producto : articulos) {
			String script = producto.getElementsByTag("script").toString();
			Pattern pattern = Pattern.compile("pagecount_\\d+ = (\\d+);");
			Pattern pattern2 = Pattern.compile(".load\\('(.*)'");
			Matcher matcher = pattern.matcher(script);
			Matcher matcher2 = pattern2.matcher(script);
			if (matcher.find() && matcher2.find())
			{
				buscarPagina = matcher2.group(1);
			    return Integer.parseInt(matcher.group(1));
			}

		}
		
		return 0;
    }
    public static void obtenerCategorias() throws IOException {
    	JSONArray json = new JSONArray(coneccion("https://www.fravega.com/api/catalog_system/pub/category/tree/0/").readLine());
    	for (int i=0; i < json.length(); i++)
    	{
    		categorias.add(json.getJSONObject(i).getString("url"));
    	}
    }
    
    
}
*/