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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 /* 
public class Megatone implements Runnable{
  
    static ArrayList<String> arrayCategorias = new ArrayList<String>();
    public void run()
    {
    	
        try {
            obtenerCategorias();
            //System.out.println(arrayCategorias.toString());
            obtenerArticulos();
            Data.getInstance().setMega(true);
        } catch (IOException ex) {
            Logger.getLogger(Megatone.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    

    public static void obtenerCategorias() throws IOException {
        String url = "https://www.megatone.net/Webservices/RecursosWeb.asmx/ObtenerHTMLMenuComun?{}";
        String prueba = coneccion(url,null).readLine().toString().replaceAll("\\\\u0027", "'").replaceAll("\\\\u003c", "<").replaceAll("\\\\u003e", ">").replaceAll("\\\\u0026", "&");
        JSONObject htmlJson = new JSONObject(prueba);
        Document html = Jsoup.parse(htmlJson.getJSONObject("d").get("_HTMLMenu").toString());
        Elements categorias = html.getElementsByClass("divSubCategoria");
        for (Element categoria : categorias) {
        	arrayCategorias.add("https://www.megatone.net" + categoria.getElementsByAttribute("href").attr("href"));
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
    
    public static void obtenerArticulos() throws IOException {

    	 Document doc = null;
    	
for (int i = 0; i < arrayCategorias.size(); i++) {
	doc = Jsoup.connect(arrayCategorias.get(i)).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
	        .referrer("http://www.google.com")
	        .timeout(300000)
	        .get();
		
    Pattern pattern = Pattern.compile("var _idMenu = \"(\\d+)\";");
    Pattern pattern2 = Pattern.compile("var _familiasBuscadas = \"(.*)\";");
    Pattern pattern3 = Pattern.compile("var _menorPrecioMultiploDe10 = \"(\\d+)\";");
    Pattern pattern4 = Pattern.compile("var _intervaloPrecios = \"(\\d+)\";");
    Matcher idMenu = pattern.matcher(doc.toString());
    Matcher familiasBuscadas = pattern2.matcher(doc.toString()); 
    Matcher menorPrecioMultiploDe10 = pattern3.matcher(doc.toString());
    Matcher intervaloPrecios = pattern4.matcher(doc.toString());
    if (idMenu.find() && familiasBuscadas.find() && menorPrecioMultiploDe10.find() && intervaloPrecios.find())
     {
    	int cantidadPaginas = 1;
    	int j = 1;
    	do {
    		
    	
    	String jsonArmado = "{\"idMenu\":\"" + idMenu.group(1)  + "\",\"paginaActual\":\"" + j + "\",\"familiasBuscadas\":\"" + familiasBuscadas.group(1) + "\",\"filtroCategorias\":\"\",\"filtroGeneros\":\"\",\"filtroPlataformas\":\"\",\"filtroMarcas\":\"\",\"filtroPrestadoras\":\"\",\"filtroPrecios\":\"\",\"filtroOfertas\":\"0\",\"palabraBuscada\":\"\",\"menorPrecioMultiploDe10\":\"" + menorPrecioMultiploDe10.group(1) + "\",\"intervaloPrecios\":\"" + intervaloPrecios.group(1) + "\",\"tipoListado\":\"Grilla\",\"orden\":\"0\",\"productosBuscados\":\"\",\"filtroCuotas\":\"\",\"NroBoca\":0}";
         JSONObject htmlProductos = new JSONObject(coneccion("https://www.megatone.net/Listado.aspx/CargarMas",jsonArmado).readLine().replaceAll("\\\\u0027", "'").replaceAll("\\\\u003c", "<").replaceAll("\\\\u003e", ">").replaceAll("\\\\u0026", "&"));
         cantidadPaginas = htmlProductos.getJSONObject("d").getInt("_CantidadPaginas");
         doc = Jsoup.parse(htmlProductos.getJSONObject("d").get("_HTMLProductos").toString());
         
    
        Elements productos = doc.getElementsByClass("itemMegatoneComun itemListadoGrilla");

        for (Element producto : productos) {
        	
        	String titulo = producto.getElementsByClass("cl_2").text();
        	String precio = producto.getElementsByClass("cl_6").text().replaceAll("\\$", "").replace(".", "").replace(",", ".");
        	String link = "https://www.megatone.net" + producto.getElementsByAttribute("href").first().attr("href").replaceAll(" - Precios Especiales", "");
        	String id = producto.getElementsByAttribute("onclick").first().attr("onclick");
        	//System.out.println(id.substring(id.indexOf("'") + 1, id.lastIndexOf("'")) + "," + titulo + "," + precio + "," + link);
                String item = String.format("('MGTN_%s','184',\"%s\",\"%s\",'%s')",id.substring(id.indexOf("'") + 1, id.lastIndexOf("'")),titulo.replaceAll("\"", ""),link,precio);
                Data.getInstance().setColaParser(item);
        } 
        j++;
        } while (j <= cantidadPaginas);
     }
       }
	}
	

    
}
*/