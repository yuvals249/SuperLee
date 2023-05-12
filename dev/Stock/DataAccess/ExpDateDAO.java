package Stock.DataAccess;

import Stock.Business.Product;
import Resource.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExpDateDAO {
    private static ExpDateDAO instance = null;
    private  static Map<Integer, Date> ExpDateMap;
    private static  Map<String, HashMap<Integer,Date>> catalogExpDate;
    private static  Map<Integer,String> qrToCatalogNumber;
    private static Connection connection;

    private ExpDateDAO(){
        connection = Connect.getConnection();
        ExpDateMap = new HashMap<>();
        qrToCatalogNumber = new HashMap<>();
        catalogExpDate = new HashMap<>();
    }

    public static ExpDateDAO getInstance() {
        if (instance == null) {
            instance = new ExpDateDAO();
        }
        return instance;
    }

    public Date getQrExpDate(int qr){

        if (ExpDateMap.get(qr) == null) {
            // read from the db
            lookForExpDate(qr);
        }
        return ExpDateMap.get(qr);
    }

    private void lookForExpDate(int qr){
        int barcode;
        String catalogNumber;
        Date expDate;

        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates WHERE QRCode ==" + qr);
            while(resultSet.next()){
                barcode = resultSet.getInt("QRCode");
                catalogNumber = resultSet.getString("catalog_number");
                expDate = resultSet.getDate("Date");
                ExpDateMap.put(barcode,expDate);
                qrToCatalogNumber.put(barcode,catalogNumber);

            }

        }catch (SQLException e){
            System.out.println("there is problem with the data base");
        }
    }

    public HashMap<Integer,Date> readExpForCatalogNumber(String catalogNumber){
        if(catalogExpDate.get(catalogNumber) == null){
            getExpForCatalogNumber(catalogNumber);
        }
        return catalogExpDate.get(catalogNumber);
    }

    private void getExpForCatalogNumber(String catalogNumber){
        HashMap<Integer,Date> productExpDates = new HashMap<>();
        int barcode;
        Date expDate;
        try{
            java.sql.Statement statement = connection.createStatement();
//            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates WHERE catalog_number ==" + catalogNumber);
            String sql = "SELECT * FROM ExpDates WHERE catalog_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, catalogNumber);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                barcode = resultSet.getInt("QRCode");
                expDate = resultSet.getDate("Date");
                productExpDates.put(barcode,expDate);
                ExpDateMap.putIfAbsent(barcode, expDate);
                qrToCatalogNumber.putIfAbsent(barcode,catalogNumber);
            }
            catalogExpDate.put(catalogNumber,productExpDates);
        }
        catch (SQLException e){
            System.out.println("theres problem with the database");
        }

    }


    public void writeExpDates(){
        for(Integer qr: ExpDateMap.keySet()){
            try{
                java.sql.Statement statement = connection.createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates WHERE QRCode ==" + qr);
                if(!resultSet.next()){
//                    statement.executeUpdate("INSERT INTO ExpDates (QRCode, catalog_number, Date) VALUES (" + qr  + ","+"'" + qrToCatalogNumber.get(qr) +"'"+","+ExpDateMap.get(qr)+ ")");
                    PreparedStatement pstmt = connection.prepareStatement("INSERT INTO ExpDates (QRCode, catalog_number, Date) VALUES (?, ?, ?)");
                    pstmt.setInt(1, qr);
                    pstmt.setString(2, qrToCatalogNumber.get(qr));
                    pstmt.setDate(3, new java.sql.Date(ExpDateMap.get(qr).getTime()));
                    pstmt.executeUpdate();
                }
                else{
                    String updateExpDatesQuery = "UPDATE ExpDates SET Date = ?, catalog_number = ? WHERE QRCode = ?";
                    PreparedStatement updateExpDatesStmt = connection.prepareStatement(updateExpDatesQuery);
                    updateExpDatesStmt.setDate(1, new java.sql.Date(ExpDateMap.get(qr).getTime()));
                    updateExpDatesStmt.setString(2, qrToCatalogNumber.get(qr));
                    updateExpDatesStmt.setInt(3, qr);
                    updateExpDatesStmt.executeUpdate();
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void writeExpDateForQR(int QR,String catalogNumber, Date date){
        if(!ExpDateMap.containsKey(QR)){
            ExpDateMap.put(QR,date);
            qrToCatalogNumber.put(QR,catalogNumber);
            // should be added to the database ?
        }
       // else{System.out.println("the qr is not new");}
    }
    public void updateExpDate(String catalogNumber, HashMap<Integer,Date> updatedExpDates){
        for(Integer qr: updatedExpDates.keySet()){
            ExpDateMap.putIfAbsent(qr,updatedExpDates.get(qr));
            qrToCatalogNumber.putIfAbsent(qr,catalogNumber);
        }
    }

    public void deleteExpDate(int qr){
        ExpDateMap.remove(qr);
        catalogExpDate.get(qrToCatalogNumber.get(qr)).remove(qr);
        qrToCatalogNumber.remove(qr);
        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("DELETE  FROM ExpDates WHERE QRCode ==" + Integer.toString(qr));

        }catch (SQLException e){
            System.out.println("theres a problem with the database");
        }

    }
    public Boolean isQRfromCatalogNumber(String catalogNumber, int qr){
        lookForExpDate(qr);
        return Objects.equals(qrToCatalogNumber.get(qr), catalogNumber);
    }


}