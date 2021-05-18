/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import Processor.Data;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Mauro
 */
public class Walmart implements Runnable{
    
        static BiMap<Integer,String> categorias2 = HashBiMap.create();
	static ArrayList<String> categorias = new ArrayList<String>();
	static String buscarPagina = null;
        static ArrayList<String> ids = new ArrayList<String>(); 
        static Integer arr[] = { 145,158,193,235,249,448,467,488,506,509,514,551,556,560,591,600,609,614,620,628,630,651,657,
        661,671,672,681,686,691,700,704,717,727,742,752,757,761,767,829,852,878,909,920,933,969,1007,1025}; 
    
    public void run(){
            try {
                obtenerCategorias();
                //System.out.println(categorias2.toString());
                obtenerArticulos();
                Data.getInstance().setWalmart(true);
                System.out.println("Walmart Finish");
            } catch (IOException ex) {
                System.out.println("Musi: "+ex);
                Data.getInstance().setWalmart(true);
            }
    }
    
    public static void obtenerArticulos() throws IOException  {

    	for (String categoria : categorias2.values()) {
    		int cantidadPaginas = obtenerCantidadPaginas(categoria);
    		for (int i = 1; i <= cantidadPaginas ; i++) {
    			Document doc;
                        doc = Jsoup.connect("https://www.walmart.com.ar/buscapagina?sl=" + buscarPagina + "&PS=48&cc=50&PageNumber=" + i + "&O=OrderByReviewRateDESC&sm=0&fq=C:/" + categorias2.inverse().get(categoria) + "/&sc=15").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                                .referrer("http://www.google.com")
                                .timeout(300000)
                                .get();
                        Elements articulos = doc.getElementsByClass("prateleira__content");
                        for (Element producto : articulos) {
                            String id = producto.getElementsByClass("buy-button-normal").attr("name");
                            String precio = producto.getElementsByClass("prateleira__best-price").text().replaceAll("\\$ ","").replace(".","").replace(",",".");
                            String titulo = producto.getElementsByClass("prateleira__name").text();
                            String link = producto.getElementsByClass("prateleira__name").attr("href");
                            //System.out.println(id + "," + titulo + "," + precio + "," + link);
                            if(!precio.isEmpty()){
                                String item = String.format("('WM_%s','277',\"%s\",\"%s\",'%s')",id,titulo.replaceAll("\"", ""),link,precio);
                                Data.getInstance().setColaParser(item);
                                //System.out.println(item);
                            
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
	    return BF;
    }
    
    public static int obtenerCantidadPaginas(String URL) throws IOException {
    	//System.out.println(URL);
		Document doc = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
		        .referrer("http://www.google.com")
		        .timeout(300000)
		        .get();
		Elements articulos = doc.getElementsByClass("vitrine resultItemsWrapper");
		for (Element producto : articulos) {
			String script = producto.getElementsByTag("script").toString();
			Pattern pattern = Pattern.compile("pagecount_\\d+ = (\\d+);");
			Pattern pattern2 = Pattern.compile("sl=(.*)&cc");
			Matcher matcher = pattern.matcher(script);
			Matcher matcher2 = pattern2.matcher(script);
			if (matcher.find() && matcher2.find())
			{
				buscarPagina = matcher2.group(1);
				//System.out.println(matcher.group(1));
			    return Integer.parseInt(matcher.group(1));
			}

		}
		
		return 0;
    }
    public static void obtenerCategorias() throws IOException {
    	JSONArray json = new JSONArray(coneccion("https://www.walmart.com.ar/api/catalog_system/pub/category/tree/0/").readLine());
    	//System.out.println(json);
    	for (int i=0; i < json.length(); i++)
    	{       
                if(Arrays.asList(arr).contains(json.getJSONObject(i).getInt("id"))){
    		categorias2.put(json.getJSONObject(i).getInt("id"), json.getJSONObject(i).getString("url"));
    		//categorias.add(json.getJSONObject(i).getString("url"));
                }
    	}
        //System.out.println(categorias2.toString());
    }
}
