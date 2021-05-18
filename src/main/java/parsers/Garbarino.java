/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import Processor.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.http.HTTPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
public class Garbarino implements Runnable{
    static ArrayList<String> arrayCategorias = new ArrayList<String>();
    
    public void run()
    {
                try {
                    obtenerCategorias();
                    obtenerArticulos();
                    Thread.sleep(1000);
                    Data.getInstance().setGarba(true);
                } catch (IOException | HTTPException | InterruptedException ex) {
                    System.out.println(ex);
                }
    	
        
    }
   
    

    public static void obtenerCategorias() throws IOException, HTTPException {
    	arrayCategorias.clear();
        String url = "https://www.garbarino.com/normandia/services/getMobileMenu/";
        JSONArray json = new JSONArray(coneccion(url,null).readLine());
        for (int i = 0; i < json.length() ; i++) {
        	arrayCategorias.add("https:" + json.getJSONObject(i).get("permalink").toString());
        }
    }
    
    public static BufferedReader coneccion(String URL, String content) throws IOException {
    	HttpURLConnection cLogin = (HttpURLConnection) new URL(URL).openConnection();
		cLogin.setRequestProperty("content-type", "application/json; charset=utf-8");
		cLogin.setUseCaches(false);
		cLogin.setDoOutput(true);
		cLogin.setDoInput(true);
		if (content != null) {
			cLogin.setDoOutput(true);
			OutputStream os = cLogin.getOutputStream();
			os.write(content.getBytes());
			os.flush();
			os.close();
		}
		Object obj = cLogin.getContent();
	    BufferedReader BF =  new BufferedReader(new InputStreamReader(cLogin.getInputStream()));
	    //System.out.println(BF.readLine());
	    return BF;
    }
    
    public static void obtenerArticulos() throws InterruptedException {

    	 Document doc = null;
    	
        for (int i = 0; i < arrayCategorias.size(); i++) {
                int j = 1;
                int cantidadPaginas = 1;
                boolean primeraPasada = false;
	do {
		
	
	try {
		doc = Jsoup.connect(arrayCategorias.get(i) + "?page=" + j).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
		        .referrer("http://www.google.com")
		        .timeout(300000)
		        .get();
		
		if (!primeraPasada && doc.getElementsByClass("pagination__page").size() > 3) {
			cantidadPaginas = Integer.parseInt(doc.getElementsByClass("pagination__page").get(3).getElementsByTag("a").first().text());
			primeraPasada = true;
		}
		
	Elements productos = doc.getElementsByClass("itemBox--info");

        for (Element producto : productos) {
        	//System.out.println(producto);
        	String precio = null;
        	String precio1 = producto.getElementsByClass("value-item ").text();
        	String precio2 = producto.getElementsByClass("value-item  value-item--full-price ").text();
        	String precio3 = producto.getElementsByClass("value-item  ").text();
        	if(!precio1.isEmpty()) {
        		precio = precio1;
        	}else if (!precio2.isEmpty()) {
        		precio = precio2;
        	}else if (!precio3.isEmpty()){
        		precio = precio3;
        	}
        	String titulo = producto.getElementsByClass("itemBox--title").first().text();
        	String link = "https://www.garbarino.com" + producto.getElementsByAttribute("href").first().attr("href");
        	String id = link.substring(link.lastIndexOf("/") + 1, link.length());
        	//System.out.println(id + "," + titulo + "," + precio + "," + link );
                String item = String.format("('Garba_%s','185',\"%s\",\"%s\",'%s')",id,titulo.replaceAll("\"", ""),link,precio.replaceAll("\\$", "").replace(".", "").replace(",", "."));
                Data.getInstance().setColaParser(item);
        } 
        j++;
	} catch (IOException e) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	};
                Thread.sleep(1000);
		} while (j <= cantidadPaginas);
     
       }
	}
	

}
*/