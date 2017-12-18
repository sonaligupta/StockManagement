/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockapi;

import DatabaseConnection.DataConnect;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import static stockapi.StockApiBean.API_KEY;

/**
 *
 * @author Mac
 */
@ManagedBean(name="watchlistbean")
@SessionScoped
public class watchList implements Serializable{
    private ArrayList watchlist ;
    private ArrayList priceList;

    public ArrayList getPriceList() throws IOException {
                 System.out.println("Hellloo");
           //ArrayList watchList = new ArrayList();
           ArrayList priceList =new ArrayList();
                
                Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
                 System.out.println("Hellloo" + uid);
                 Connection con = null;
	         PreparedStatement ps = null;
                 
                 try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select stock_symbol from watchlist where uid = ?");
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();

                        ResultSetMetaData metadata = rs.getMetaData();
                        int numberOfColumns = metadata.getColumnCount();

			while(rs.next()) {
                            
                            
                                  String StockSymbol = rs.getString(1);
                                  String time="1min";
                                  String price=Price(StockSymbol,time);  
                                       //watchList.add(rs.getString(i));
                                  priceList.add(price);                                      
                          
                              System.out.println("list...."+priceList);
			}
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return priceList;
		} finally {
			DataConnect.close(con);
		}
		
        return priceList;
    }

    public void setPriceList(ArrayList priceList) {
        this.priceList = priceList;
    }

    public void setWatchlist(ArrayList watchlist) {
        this.watchlist = watchlist;
    }
   public ArrayList getWatchlist() throws IOException {
         System.out.println("Hellloo");
           ArrayList watchList = new ArrayList();
           ArrayList priceList =new ArrayList();
                
                Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
                 System.out.println("Hellloo" + uid);
                 Connection con = null;
	         PreparedStatement ps = null;
                 
                 try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select stock_symbol from watchlist where uid = ?");
			ps.setInt(1, uid);
			
                        

			ResultSet rs = ps.executeQuery();

                        ResultSetMetaData metadata = rs.getMetaData();
                        int numberOfColumns = metadata.getColumnCount();

			while(rs.next()) {
                            
                           
                                       watchList.add(rs.getString(1));
                                  //priceList.add(price);                                      
                          
                              System.out.println("list...."+priceList);
			}
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return watchList;
		} finally {
			DataConnect.close(con);
		}
		return watchList;
    }
   public static String Price(String stockSymbol, String time_interval) throws MalformedURLException, IOException{
       installAllTrustingManager();
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + stockSymbol + "&interval=" + time_interval + "&apikey=" + API_KEY;
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        String price = null;
        int count=0;
        System.out.println("object....."+mainJsonObj);
        for (String key : mainJsonObj.keySet()) {
            JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
            for (String subKey : dataJsonObj.keySet()) {
                if (!key.equals("Meta Data") && count==0) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                   count ++;
                     price=subJsonObj.getString("4. close");
                    System.out.println("API Price"+price);
                                     
                }
                if(count==1){
                    break;
                }
            }
            
             if(count==1){
                    break;
                }
            
        }
        
       return price;
   }
    public static void installAllTrustingManager() {
        TrustManager[] trustAllCerts;
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return;
    }
}
