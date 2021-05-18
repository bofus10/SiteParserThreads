/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processor;


import static Processor.Hikari.getDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBWriter implements Runnable{
    
    String sql = null;    
    int lock_time = 0;
    LinkedBlockingQueue<String> colaSQL = new LinkedBlockingQueue<String>();
    Connection connThread = null;
    Statement stmntThreadDB = null;
    int count = 0;
    
     public void run() {
  try {
      try {
          Thread.sleep(5000);
      } catch (InterruptedException ex) {
         System.out.println("DBWritter:30"+ex);
      }
      
    HikariDataSource dataSource = getDataSource();
    connThread = dataSource.getConnection();
    stmntThreadDB = connThread.createStatement();

   
   //stmntThread.execute(sql);
   //System.out.println("Atroden");
   //System.out.println(Data.getInstance().getColaParser().size());
         
   while(!Data.getInstance().getColaParser().isEmpty() || !Data.getCoto_categorias().isEmpty() || !SiteParser.getParsesStatus()){
     //while(true){
        //System.out.println("Atroden 2");
        if(!(LocalDateTime.now().getHour() == 0 && LocalDateTime.now().getMinute() < 15) || Data.getInstance().isSyncFlag()){
            Data.getInstance().getColaParser().drainTo(colaSQL,500);
            Data.getInstance().setLockFlag(false);
            //System.out.println("local date");
            if(!colaSQL.isEmpty()){
            String valores = this.colaSQL.toString();
            valores = valores.substring(1, valores.length() - 1);
            //System.out.println(valores);
            //sql = "REPLACE INTO mercadolibre (ID, vendor_id, articulo, url, price, minor_price) VALUES " +valores +";";
            sql = "INSERT INTO productos (ID, vendor_id, articulo, url, price) VALUES " + valores + " ON DUPLICATE KEY UPDATE ID = VALUES(ID),vendor_id = VALUES(vendor_id),articulo = VALUES(articulo),url = VALUES(url),price = VALUES(price);"; 
            stmntThreadDB.execute(sql);
            //System.out.println(Thread.currentThread().getName() +  " ColaSQL=" + colaSQL.size() + " impactando.");
             SiteParser.Write2Log(Thread.currentThread().getName() +  " ColaSQL=" + colaSQL.size() + " impactando.", "vendors");
             connThread.commit();
             colaSQL.clear();
           
            }else{
                
                lock_time++;
                if(lock_time >= 160){
                    Data.getInstance().setLockFlag(true);
                    stmntThreadDB.close();
                    connThread.close();
                    break;
                }
                //System.out.println("Cola Vacia o Threads Corriendo");
                //System.out.println("Threads: "+SiteParser.areAlive() );
                //System.out.println("Cola Size: "+Data.getInstance().getColaParser().size());
                
                SiteParser.Write2Log("Cola Size: "+Data.getInstance().getColaParser().size(), "vendors");
            }
        }else{
            //stmntThreadDB.close();
            //connThread.close();
            //move_db_diary();
            //Se encarga el de MLParser
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt(); // restore interrupted status
                System.out.println("DBWritter:76"+e);
             
            }
            connThread = dataSource.getConnection();
            stmntThreadDB = connThread.createStatement();
        }
         //System.out.println(Data.getInstance().getColaParser().size());
         try {
             Thread.sleep(2000);
         } catch (InterruptedException ex) {
             System.out.println("DBWritter:87"+ex);
             //Thread.currentThread().interrupt(); // restore interrupted status
             //break;
         }
    
   }
   
      System.out.println("ParserStatus: "+SiteParser.getParsesStatus()
              + " ParserCola: "+Data.getInstance().getColaParser().isEmpty()
              //+ " Falabella: " +Data.getCategorias().isEmpty()
              + " Coto: " +Data.getCoto_categorias().isEmpty());
    SiteParser.Write2Log("DB Writter Finished","vendors");
    connThread.close();
    //System.out.println("Empty: "+Data.getTiendas().isEmpty());
    //System.out.println(SiteParser.areAlive());
    Data.getInstance().setDbFlag(true);

  } catch (SQLException e) {
      try {
          //e.printStackTrace();
          this.colaSQL.drainTo(Data.getInstance().getColaParser());
          System.out.println("DBWritter:100"+e);
          System.out.println(sql);
          SiteParser.Write2Log(e.getMessage(),"vendors");
          SiteParser.Write2Log(sql,"vendors");
          Data.getInstance().setDbFlag(true);
      } catch (IOException ex) {
          Logger.getLogger(DBWriter.class.getName()).log(Level.SEVERE, null, ex);
          System.exit(1);
      }
  }     catch (IOException ex) {
            System.out.println("DBWritter:107"+ex);
            System.exit(1);
        }
  
 }
     
     
// public static void move_db_diary() throws SQLException, IOException{
//     
//     
//     
//     Connection connThread = DriverManager.getConnection(Data.getInstance().getDB_URL(),Data.getInstance().getUSER(),Data.getInstance().getPASS()); 
//     connThread.setAutoCommit(false);
//     Statement stmntThread = connThread.createStatement();
//     Statement stmntThread1 = connThread.createStatement();
//     //Movemos a Diario
//    //System.out.println("Backing up Diaries");
//     //MLParser.Write2Log("Backing up Diaries", "sql");
//    String sql = "INSERT INTO productos_diario_30 (ID,vendor_id,articulo,url,day_price,date)  \n" +
//    "	 SELECT ID,vendor_id,articulo,url,price,(curdate()- interval 1 day)\n" +
//    "    FROM `productos`;" ;
//     stmntThread1.execute(sql);
//     connThread.commit();
//     stmntThread1.close();
//     
//    //Generamos el Weekly
//    //System.out.println("Generating Weekly"); 
//    //MLParser.Write2Log("Generating Weekly", "sql");
//    sql = "REPLACE INTO historicos (ID,weekly)  \n" +
//    "	SELECT ID,min(day_price)\n" +
//    "    FROM `productos_diario_30`\n" +
//    "    WHERE date BETWEEN (curdate() - interval 7 day) AND (curdate() - interval 1 day)\n" +
//    "    GROUP BY ID;"; 
//     stmntThread.execute(sql);
//     connThread.commit();
//     stmntThread.close();
//     connThread.close();
//     
//     Data.getInstance().setSyncFlag(true);
// }
    
}
