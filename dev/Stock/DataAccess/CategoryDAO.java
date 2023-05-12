package Stock.DataAccess;

import Stock.Business.AProductCategory;
import Resource.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CategoryDAO {
    private static CategoryDAO instance  = null;
    private static Map<String, AProductCategory> CategoryMap;
    private static Connection connection;


    private CategoryDAO(){
        CategoryMap = new HashMap<>();
        connection = Connect.getConnection();
    }

    public static CategoryDAO getInstance() {
        if (instance == null) {
            instance = new CategoryDAO();
        }
        return instance;
    }

    public AProductCategory getCategory(String category){
        if(CategoryMap.get(category) == null){
            // look for item in the database
            lookForCategory(category);
        }
        return CategoryMap.get(category);
    }

    private void lookForCategory(String categoryStr){
        String category;
        double discount;
        try{
            java.sql.Statement statement = connection.createStatement();
//            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM Category WHERE Category ==" + categoryStr);
            String sql = "SELECT * FROM Category WHERE Category = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categoryStr);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                category = resultSet.getString("Category");
                discount = resultSet.getDouble("Discount");
                AProductCategory found = new AProductCategory(category);
                found.setDiscount(discount);
                CategoryMap.put(category,found);
            }
        } catch (SQLException e) {
            System.out.println("there was a problem with the database");
        }
    }

    public void writeCategories(){
        for(AProductCategory category: CategoryMap.values()){
            String name;
            double discount;
            try{
                java.sql.Statement statement = connection.createStatement();
                name = category.getName();
                discount = category.getDiscount();
                java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM Category WHERE Category ==" + name);
                if(!resultSet.next()){
                    statement.executeUpdate("INSERT INTO Category (Category, Discount) VALUES (" + "'" + name + "'" + "," + discount + ")");
                }
                else{
                    statement.executeUpdate("UPDATE Category SET Discount ="  + discount+ " WHERE Category = "+ name);
                }
            }
            catch (SQLException e) {
                System.out.println("there was a problem with the database");
            }
        }
    }

    public void writeNewCategory(String categoryName, double discount){
        AProductCategory category = new AProductCategory(categoryName);
        category.setDiscount(discount);
        CategoryMap.put(categoryName,category);
        // should be added to the db?
    }

    public void deleteCategory(String categoryName){
        CategoryMap.remove(categoryName);
        try{
            java.sql.Statement statement = connection.createStatement();
            statement.executeQuery("DELETE  FROM Category WHERE Catrgory ==" + categoryName);

        }catch (SQLException e){
            System.out.println("theres a problem with the database");
        }
    }

    private void loadAllDataToCache(){
        String categoryStr;
        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates");
            while(resultSet.next()){
                categoryStr = resultSet.getString("Category");
                getCategory(categoryStr);
            }
        }catch (SQLException e){
            System.out.println("there is problem with the data base");
        }
    }

    public ArrayList<String> getAllTheCategories(){
        loadAllDataToCache();
        return new ArrayList<>(CategoryMap.keySet());
    }
    public Map<String,AProductCategory> getCategoriesMap(){
        loadAllDataToCache();
        return CategoryMap;
    }


}
