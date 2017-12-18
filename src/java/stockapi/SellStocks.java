
import DatabaseConnection.DataConnect;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import static dao.LoginDAO.findUsername;
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
import managedbean.AccountBalance;
import managedbean.User;
import stockapi.StockApiBean;
import static stockapi.watchList.Price;



@ManagedBean(name="sellStockbean")
@SessionScoped
public class SellStocks implements Serializable { 
    private List<StockApiBean> purchaseList;
    private String sellstock;
     static final String API_KEY = "AF93E6L5I01EA39O";
    private String Message;
    private List<StockApiBean> purchaseHistory;
    private List<StockApiBean> soldHistory;
   private List<AccountBalance> CurrentBalance;
   private List<AccountBalance> CurrentmanagerBalance;

    public List<AccountBalance> getCurrentmanagerBalance() {
        List<AccountBalance> accountBalance = new ArrayList<AccountBalance>();
        FacesContext fc = FacesContext.getCurrentInstance();
                        User userr=(User) fc.getExternalContext().getSessionMap().get("User");
                      
                        String Username = userr.getUsername();
                      String amountRequested=  findRequestBal(Username);
                      int amountReq= Integer.parseInt(amountRequested);
                      System.out.println(".......total purchase"+totalPurchase);
                      
        
        AccountBalance accountBal = new AccountBalance();
       int currentbal=0;
         currentbal = amountReq-totalPurchase+totalSold;
         accountBal.setTotalAmountSpent(totalPurchase);
         accountBal.setTotalAmountGained(totalSold);
         accountBal.setAmountGiventoManager(amountReq);
         accountBal.setCurrentBal(currentbal);
         accountBalance.add(accountBal);
                        
        return accountBalance;
    }

    public void setCurrentmanagerBalance(List<AccountBalance> CurrentmanagerBalance) {
        this.CurrentmanagerBalance = CurrentmanagerBalance;
    }
   

    public List<AccountBalance> getCurrentBalance() {
        
        FacesContext fc = FacesContext.getCurrentInstance();
                        User userr=(User) fc.getExternalContext().getSessionMap().get("User");
                      
                        String Username = userr.getUsername();
        
        List<AccountBalance> accountBalance = new ArrayList<AccountBalance>();
        System.out.println(".......total purchase"+totalPurchase);
        System.out.println(".......total sold"+totalSold);
        AccountBalance accountBal = new AccountBalance();
        int amountgiventoman =findRequestBalUser();
        
        //int amountgiventomannager= Integer.parseInt(amountgiventoman);
        
        int managersfee= 100;
        int currentbal=0;
         currentbal = 100000-totalPurchase+amountgiventoman+managersfee-totalSold;
        
        accountBal.setTotalAmountSpent(totalPurchase);
        accountBal.setTotalAmountGained(totalSold);
        accountBal.setAmountGiventoManager(amountgiventoman);
        accountBal.setManagersFee(managersfee);
        accountBal.setCurrentBal(currentbal);
        
        accountBalance.add(accountBal);
        
        
        return accountBalance;
    }

    public void setCurrentBalance(List<AccountBalance> CurrentBalance) {
        this.CurrentBalance = CurrentBalance;
    }

    
   int totalPurchase=0;
   int totalSold=0;
    public List<StockApiBean> getSoldHistory() {
        
        List<StockApiBean> purchaselist = new ArrayList<StockApiBean>();
 Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
                 System.out.println("Hellloo" + uid);
                 Connection conn = null;
	         Statement statement = null;
                 
                 try {
			conn = DataConnect.getConnection();
                        statement=conn.createStatement();
                        ResultSet rs=statement.executeQuery("SELECT * FROM `soldStocks` WHERE uid='"+uid+"';");

			while(rs.next()) {
                            StockApiBean stock= new StockApiBean();
                            stock.setSymbol(rs.getString("symbol"));                       
                            stock.setPrice(rs.getInt("price"));
                            stock.setQty(rs.getInt("quantity"));                       
                            stock.setAmt(rs.getInt("amount"));
                           purchaselist.add(stock);
                           totalSold=totalSold+rs.getInt("amount");
                        }
                        System.out.println(".......sold amount "+totalSold);
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return purchaselist;
		} finally {
			DataConnect.close(conn);
		}
            return purchaselist;
    }

    public void setSoldHistory(List<StockApiBean> soldHistory) {
        this.soldHistory = soldHistory;
    }

    public List<StockApiBean> getPurchaseHistory() {
        
         List<StockApiBean> purchaselist = new ArrayList<StockApiBean>();
 Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
                 System.out.println("Hellloo" + uid);
                 Connection conn = null;
	         Statement statement = null;
                 
                 try {
			conn = DataConnect.getConnection();
                        statement=conn.createStatement();
                        ResultSet rs=statement.executeQuery("SELECT * FROM `purchase` WHERE uid='"+uid+"';");

			while(rs.next()) {
                            StockApiBean stock= new StockApiBean();
                            stock.setSymbol(rs.getString("stock_symbol"));                       
                            stock.setPrice(rs.getInt("price"));
                            stock.setQty(rs.getInt("qty"));
                            stock.setAmt(rs.getInt("amt"));
                            totalPurchase=totalPurchase+rs.getInt("amt");
                           purchaselist.add(stock);
                        }
                        
                        
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return purchaselist;
		} finally {
			DataConnect.close(conn);
		}
            return purchaselist;
    }
    

    public void setPurchaseHistory(List<StockApiBean> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<StockApiBean> getPurchaseList() throws IOException {
        List<StockApiBean> purchaselist = new ArrayList<StockApiBean>();
 Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
                 System.out.println("Hellloo" + uid);
                 Connection conn = null;
	         Statement statement = null;
                 
                 try {
			conn = DataConnect.getConnection();
                        statement=conn.createStatement();
                        ResultSet rs=statement.executeQuery("SELECT * FROM `purchase` WHERE uid='"+uid+"';");

			while(rs.next()) {
                            StockApiBean stock= new StockApiBean();
                            stock.setSymbol(rs.getString("stock_symbol"));
                            String symbol=rs.getString("stock_symbol");
                            //stock.setPrice(rs.getDouble("price"));
                            
                            String time="1min";
                            String price=Price(symbol,time);
                            double number = Double.parseDouble(price);
                            stock.setPrice(number);
                            stock.setQty(rs.getInt("qty"));
                            
                           purchaselist.add(stock);
                        }
                        System.out.println(".......listttt"+purchaselist);
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return purchaselist;
		} finally {
			DataConnect.close(conn);
		}
            return purchaselist;
    
    }

    public void setPurchaseList(List<StockApiBean> purchaseList) {
        this.purchaseList = purchaseList;
    }
    
     public String getSellstock() {
         
        return sellstock;
    }
 
    
    public void setSellstock(String sellstock) {
        this.sellstock = sellstock;
    }
    
    public String sellStocks(String Symbol, int price, int qty){
        System.out.println("...in SELL"+Symbol);
      Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
      int amount = qty*price;
        
        int i=0;
        Connection con = null;
        PreparedStatement ps = null;
          try {
          con = DataConnect.getConnection();
                        
			ps = (PreparedStatement) con.prepareStatement("INSERT INTO soldStocks(uid,price,quantity,symbol,amount) VALUES(?,?,?,?,?)");
                          ps.setInt(1,uid );
                          ps.setInt(2,price );
                          ps.setInt(3, qty);
                           ps.setString(4, Symbol);
                           ps.setInt(5,amount);
			 i = ps.executeUpdate();
                         
    }catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }               
             if (i > 0) {
                Message = "Sold Successfully";
            return "buyAndSell";
        } else
                Message ="Error in selling";
            return "buyAndSell";
    }
    public String findRequestBal(String username){
        System.out.println("....in reqqq"+username);
        Connection con = null;
	PreparedStatement ps = null;
        	try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select amount from UserToManager where manager = ?");
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();
 
			if (rs.next()) {                         
				return rs.getString(1);
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
                        return "failed";
			
		} finally {
			DataConnect.close(con);
		}
		return "failed";
    }
    public int findRequestBalUser(){
               //System.out.println("....in reqqq"+username);
               Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
        Connection con = null;
	PreparedStatement ps = null;
        	try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select amount from UserToManager where uid = ?");
			ps.setInt(1, uid);

			ResultSet rs = ps.executeQuery();
 
			if (rs.next()) {                         
				return rs.getInt(1);
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
                        return 0;
			
		} finally {
			DataConnect.close(con);
		}
		return 0;
    }
        
    
}