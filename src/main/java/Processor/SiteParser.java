/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processor;


import parsers.Fravega;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.Date;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import parsers.Coto;
import parsers.Dia;
import parsers.Falabella;
import parsers.KioscLea;
import parsers.Linio;
import parsers.Necxus;
import parsers.Sodimac;
import parsers.Walmart;




public class SiteParser {

    static ExecutorService executorPool = Executors.newFixedThreadPool(5);
    static Logger vendors = Logger.getLogger("MyLog");
    static SimpleFormatter formatter = new SimpleFormatter();
    static int count = 0;
    static int flag = 1;
    static int dbcount = 0;
    static int fala = 0;
    

   public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, InterruptedException {

       //Codigo
       DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       Data.getInstance();

		
	while(true){
            if(flag == 1){
                //getFalabellaCategories();
                getCotoCategories();
                //submit work to the thread pool
                executorPool.execute(new DBWriter());
                
                
                    //executorPool.execute(new Linio()); 
                    executorPool.execute(new Dia()); 
                    executorPool.execute(new KioscLea()); 
                    executorPool.execute(new Walmart());  
                    executorPool.execute(new Fravega());  
                    executorPool.execute(new Necxus());   
                    executorPool.execute(new Falabella()); 
                    executorPool.execute(new Sodimac()); 
                    
                    for(int z=0; z< Data.getCoto_categorias().size(); z++){
                        executorPool.execute(new Coto(Data.getCoto_categorias().get(z)));   
                    }
                
                flag = 0;
                Date date = new Date();
                System.out.println(dateFormat.format(date) +" Todo Cargado");
            }                                                              
            else if(Data.getInstance().isDbFlag() && getParsesStatus() && Data.getCoto_categorias().isEmpty()){
                    System.out.println("Realoading Pool");
                    
                //Thread.sleep(5000);
                    
                    if(LocalDateTime.now().getHour() == 0 && LocalDateTime.now().getMinute() < 15){
                        //Data.getInstance().setSyncFlag(false);
                        System.out.println("Waiting for BKP to finish");
                        System.out.println("DB: "+Data.getInstance().isDbFlag()+" Parser: "+getParsesStatus()+" Coto: "+Data.getCoto_categorias().isEmpty());
                    }else{
                        //System.out.println("BKP finished");
                        Data.getInstance().setDbFlag(false);
                        Data.getInstance().setFravega(false);
                        Data.getInstance().setWalmart(false);
                        Data.getInstance().setDia(false);
                        Data.getInstance().setLinio(false);
                        Data.getInstance().setNecxus(false);
                        Data.getInstance().setKLea(false);
                        Data.getInstance().setFalabella(false);
                        Data.getInstance().setSodimac(false);
                        flag = 1;
                         }
            }
            
            try {
                Thread.sleep(3000);
                WatchDogMonitor();
                //System.out.println("Parsers: "+getParsesStatus()+" Fala: "+Data.getCategorias().isEmpty() + "Coto: "+Data.getCoto_categorias().isEmpty());

                 } catch (InterruptedException e) {
                    System.out.println("MLParser:93"+e);
                }
            

        }
        
        
    }
   
   public static void getCotoCategories(){
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-textil/_/N-l8joi7");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-aire-libre/_/N-w7wnle");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-electro/_/N-1ngpk59");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar/_/N-qa34ar");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-decoraci%C3%B3n/_/N-1m5f1r5");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-bazar/_/N-1s7yakw");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-uso-diario/_/N-3wi82x");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-accesorios-de-iluminaci%C3%B3n-y-electricidad/_/N-ci6lta");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-muebles-de-interior/_/N-ik2umm");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-ferreteria/_/N-vkmx7w");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-automotor/_/N-19apk5n");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-jugueteria/_/N-175f40x");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-libreria/_/N-5o2bp1");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-libros/_/N-q08sox");
       Data.getCoto_categorias().add("https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar-mascotas/_/N-1z03vps");
   }
/*  
public static void getFalabellaCategories() throws IOException{
            Document doc;
            
            String url = String.format("https://www.falabella.com.ar/falabella-ar/");
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .get();
            
            Elements titles = doc.getElementsByClass("SEO_content_FAAR").get(0).getElementsByTag("p").get(0).getElementsByTag("a");

            for (Element title : titles) {
                //System.out.println("1: "+title.getElementsByAttribute("href").attr("href"));
                String MainCategory = title.getElementsByAttribute("href").attr("href");
                //String CategoryURL = String.format("https://www.falabella.com.ar%s", MainCategory);
                if(MainCategory.contains("/category/")){
                    Data.getCategorias().add(MainCategory);
                    //System.out.println(MainCategory);
                }
               
            }

            if(Data.getCategorias().isEmpty()){
                fala = 1;
                Data.getCategorias().add("Not_Working");
            }
            
   }*/
   
   public static void Write2Log(String msg, String file) throws IOException{
           
          
        if(Data.getInstance().isLogFlag()){
           switch(file){
               case "vendors":
                             vendors.info(msg);
                             //fh_vendors.close();
               break;
               /*
               case "sql":
                          sql.info(msg);
                          //fh.close();
               break;
               
               case "offers":
                             offers.info(msg);
                             //fh.close();
               break;*/
           }
           
        }else{
            
            Data.getInstance().setLogFlag(true);
            FileHandler fh_vendors = new FileHandler("./log/sitios.log"); 
            //FileHandler fh_sql = new FileHandler("./log/sql.log"); 
            //FileHandler fh_offer = new FileHandler("./log/offers.log");
            
            vendors.addHandler(fh_vendors);
            vendors.setUseParentHandlers(false);
            fh_vendors.setFormatter(formatter);
           /* sql.addHandler(fh_sql);
            sql.setUseParentHandlers(false);
            fh_sql.setFormatter(formatter);
            offers.addHandler(fh_offer);
            offers.setUseParentHandlers(false);
            fh_offer.setFormatter(formatter);*/
            
        }  
                      
   }
   /*
   public static void getVendorID_Map() throws SQLException{
       Connection connThread = DriverManager.getConnection(Data.getInstance().getDB_URL(),Data.getInstance().getUSER(),Data.getInstance().getPASS()); 
       //Connection connThread = DriverManager.getConnection("jdbc:mysql://10.11.99.180/parser?autoReconnect=true&useSSL=false","joaco","AvanzitDB@11"); 
       connThread.setAutoCommit(false);
       Statement stmntThreadVendor = connThread.createStatement();
       String sql = "SELECT * FROM vendors_site;";
       
       ResultSet rs = stmntThreadVendor.executeQuery(sql);
       
       while(rs.next()) {
        Data.getVendorID().put(String.valueOf(rs.getInt("vendors_id")), rs.getString("name"));
        //tiendas.add(rs.getString("name"));
        Data.getCategorias().add(rs.getString("name"));
       }
       
       stmntThreadVendor.close();

      
   }*/
   
   public static boolean getParsesStatus(){
       if(Data.getInstance().isFravega() && Data.getInstance().isWalmart() && Data.getInstance().isDia() &&
          Data.getInstance().isLinio()&& Data.getInstance().isNecxus() && Data.getInstance().isKLea() && Data.getInstance().isFalabella()
          && Data.getInstance().isSodimac()
        ){
           return true;
       }else{
           /*
           if(!Data.getInstance().isFravega()){
               System.out.println("Fravega");
           }
           if(!Data.getInstance().isMusi()){
               System.out.println("Musi");
           }
           if(!Data.getInstance().isMega()){
               System.out.println("Mega");
           }
           if(!Data.getInstance().isGarba()){
               System.out.println("Garba");
           }
           if(!Data.getInstance().isEasy()){
               System.out.println("Easy");
           }*/
           
           return false;
       }
   }
   
   public static void WatchDogMonitor(){
       
       if(Data.getInstance().isLockFlag()){
           
               System.out.println("Parse Lock Detected. Restarting...");
               Data.getInstance().setLockFlag(false);
               System.out.println("Fravega: "+Data.getInstance().isFravega()
                       + "Walmart: " +Data.getInstance().isWalmart()
                       + "Dia: " +Data.getInstance().isDia()
                       + "Linio: " +Data.getInstance().isLinio()
                       + "Necxus: " +Data.getInstance().isNecxus()
                       //+ "Falabella: " +Data.getCategorias()
                       + "Coto: " +Data.getCoto_categorias());
               executorPool.shutdownNow();
               System.exit(1);

       }
       if(Data.getInstance().isDbFlag()){
           dbcount++;
           if(dbcount > 150){
               dbcount=0;
               System.out.println("DB Lock Detected. Restarting...");
               Data.getInstance().setDbFlag(false);
               executorPool.shutdownNow();
               System.exit(1);
           }
       }
   }
}
