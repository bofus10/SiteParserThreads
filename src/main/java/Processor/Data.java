/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Mauro
 */
public class Data {
    
     private LinkedBlockingQueue<String> colaParser = new LinkedBlockingQueue<String>();
     
     //private static ArrayList<String> categorias = new ArrayList<String>();
     private static ArrayList<String> coto_categorias = new ArrayList<String>();
     //private static String[] coto_categorias = {"https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-textil/_/N-l8joi7","https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-hogar/_/N-qa34ar","https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-aire-libre/_/N-w7wnle","https://www.cotodigital3.com.ar/sitios/cdigi/browse/catalogo-electro/_/N-1ngpk59"};
     private static BiMap<String, String> VendorID = HashBiMap.create();
     private static BiMap<String, String> VendorDisplay = HashBiMap.create();
     
     private boolean dbFlag = false;
     private boolean OSFlag = false;
     private boolean SyncFlag = false;
     private boolean LogFlag = false;
     private boolean LockFlag = false;
     
     private boolean Fravega = false;
     private boolean Falabella = false;
     private boolean Sodimac = false;
     private boolean Musi = false;
     private boolean Necxus = false;
     private boolean Linio = false;
     private boolean Dia = false;
     private boolean Walmart = false;
     private boolean Klea = false;

    private static Data instance = null;
    
    
    public Data() {
      
    }
    
    public static Data getInstance(){
       if(instance==null){
       instance = new Data();
      }
      return instance;

    }

    public LinkedBlockingQueue<String> getColaParser() {
        return colaParser;
    }

    /*public static ArrayList<String> getCategorias() {
        return categorias;
    }*/
    
    public static ArrayList<String> getCoto_categorias() {
        return coto_categorias;
    }

    /*public static void setCategorias(ArrayList<String> categorias) {
        Data.categorias = categorias;
    }*/

    public boolean isDbFlag() {
        return dbFlag;
    }

    public boolean isOSFlag() {
        return OSFlag;
    }

    public void setOSFlag(boolean OSFlag) {
        this.OSFlag = OSFlag;
    }

    public boolean isSyncFlag() {
        return SyncFlag;
    }

    public void setSyncFlag(boolean SyncFlag) {
        this.SyncFlag = SyncFlag;
    }

    public boolean isLogFlag() {
        return LogFlag;
    }

    public void setLogFlag(boolean LogFlag) {
        this.LogFlag = LogFlag;
    }

    public void setDbFlag(boolean dbFlag) {
        this.dbFlag = dbFlag;
    }

    public boolean isFravega() {
        return Fravega;
    }

    public void setFravega(boolean Fravega) {
        this.Fravega = Fravega;
    }
    
    public boolean isFalabella() {
        return Falabella;
    }

    public void setFalabella(boolean Falabella) {
        this.Falabella = Falabella;
    }
    
    public boolean isSodimac() {
        return Sodimac;
    }

    public void setSodimac(boolean Sodimac) {
        this.Sodimac = Sodimac;
    }
    
    public void setWalmart(boolean Walmart) {
        this.Walmart = Walmart;
    }

    public boolean isWalmart() {
        return Walmart;
    }
    
    public void setNecxus(boolean Necxus) {
        this.Necxus = Necxus;
    }

    public boolean isNecxus() {
        return Necxus;
    }
    
    public void setKLea(boolean Klea) {
        this.Klea = Klea;
    }

    public boolean isKLea() {
        return Klea;
    }
    
    public void setLinio(boolean Linio) {
        this.Linio = Linio;
    }

    public boolean isLinio() {
        return Linio;
    }

    public boolean isMusi() {
        return Musi;
    }

    public void setMusi(boolean Musi) {
        this.Musi = Musi;
    }

    public boolean isDia() {
        return Dia;
    }

    public void setDia(boolean Easy) {
        this.Dia = Easy;
    }
    
    
    
    public boolean isLockFlag() {
        return LockFlag;
    }

    public void setLockFlag(boolean LockFlag) {
        this.LockFlag = LockFlag;
    }
    
    public static BiMap<String, String> getVendorID() {
        return VendorID;
    }
    
    public static BiMap<String, String> getVendorDisplay() {
        return VendorDisplay;
    }


    public static void setVendorID(BiMap<String, String> VendorID) {
        Data.VendorID = VendorID;
    }


    public void setColaParser(String colaParser) {
        this.colaParser.add(colaParser);
    }

    public void setColaParser(LinkedBlockingQueue<String> colaParser) {
        this.colaParser = colaParser;
    }
    
}

