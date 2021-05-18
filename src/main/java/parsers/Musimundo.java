package parsers;
import Processor.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Musimundo implements Runnable
{
	
    static ArrayList<String> arrayCategorias = new ArrayList<String>();
    public void run()
    {
    	
        try {
            obtenerCategorias();
            //System.out.println(arrayCategorias.toString());
            obtenerArticulos();
        } catch (IOException ex) {
            Logger.getLogger(Musimundo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Data.getInstance().setMusi(true);
    }
   
    

    public static void obtenerCategorias() throws IOException {
        String url = "https://www.musimundo.com/";
        String cate = null;
        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
               .referrer("http://www.google.com")
               .get();
        Elements content = doc.getElementsByClass("sub");
        Elements categorias = content.first().getElementsByAttribute("href");
        for (Element categoria : categorias) {
        	if(categoria.attr("href").contains("Listado") && !categoria.attr("href").contains("=")) {
        		cate = categoria.attr("href");
        	}else if (!categoria.attr("href").contains("=") && !categoria.attr("href").contains("musica") && !categoria.attr("href").contains("peliculas")) {
        		cate = categoria.attr("href") + "/Listado";
        	}
       
            Document doc2 = Jsoup.connect(cate).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .get();
            String cantArticulos = doc2.getElementsByClass("count").first().text().substring(doc2.getElementsByClass("count").first().text().lastIndexOf(" ") + 1);
            int j= 0;
            for (int i = 1; i< Integer.parseInt(cantArticulos) ; i=i + 36) {
            	j++;
                arrayCategorias.add(cate + "?page="+ j + "&limitRows=36&sort=dateadded%20desc&typeGrid=grid&idRootCat=0&cf=&maxprice=0,00&minprice=0,00&onlyNews=&maxfrigorias=0&minfrigorias=0");
            }
        }
    }
    
    public static void obtenerArticulos() throws IOException {

    	 Document doc = null;
    	
for (int i = 0; i < arrayCategorias.size(); i++) {
	doc = Jsoup.connect(arrayCategorias.get(i)).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
	        .referrer("http://www.google.com")
	        .timeout(300000)
                .maxBodySize(0) 
	        .get();
		
        Elements productos = doc.getElementsByClass("product");

        for (Element producto : productos) {
        	if (!producto.text().contains("<") && !producto.text().contains("Subtotal") && producto.text().toString().length() < 1000) {
        	String precio = producto.getElementsByClass("price online").text();
        	String id = producto.getElementsByAttribute("data-product-id").attr("data-product-id");
        	String link = producto.getElementsByClass("productClicked").attr("href");
        	//System.out.println("Musimundo_"+id + ",Musimundo," + producto.getElementsByClass("name productClicked").text() + "," + link + "," + precio.substring(precio.indexOf(" ") + 1));
        	String item = String.format("('Musimundo_%s','182',\"%s\",\"%s\",'%s')",id,producto.getElementsByClass("name productClicked").text().replaceAll("\"", ""),link,precio.substring(precio.indexOf(" ") + 1).replaceAll(",","."));
                Data.getInstance().setColaParser(item);
                }
        }
       }
	}
	


}

