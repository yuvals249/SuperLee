package Stock.DataAccess;

import Resource.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDetailsDAO {
    // the table of the details :
    //  ID        ,Value
    //  1           ProductIDCounter
    //  2           StorageLocation: ShelfNumber
    //  3           StorageLocation: IndexInShelf
    //  4           StoreLocation: ShelfNumber
    //  5           StoreLocation: IndexInShelf


    private static ProductDetailsDAO instance = new ProductDetailsDAO();
    private static int curr;
    private static int storageShelfNumber;
    private static int storageIndexInShelf;
    private static int storeShelfNumber;
    private static int storeIndexInShelf;
    private static Connection connection;

    public static ProductDetailsDAO getInstance() {
        return instance;
    }

    private ProductDetailsDAO(){
        connection = Connect.getConnection();

        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "1" );
            if(resultSet.next()){
                curr = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "2" );
            if(resultSet.next()){
                storageShelfNumber = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "3" );
            if(resultSet.next()){
                storageIndexInShelf = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "4" );
            if(resultSet.next()){
                storeShelfNumber = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "5" );
            if(resultSet.next()){
                storeIndexInShelf = resultSet.getInt("Value");
            }

        }catch (SQLException e){
            System.out.println("there is a problem with the database");
        }
    }

//    public static void saveDetails(){
//        try{
//            java.sql.Statement statement = connection.createStatement();
//            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "1" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "1"  + "," + Integer.toString(curr) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(curr) + " WHERE ID = "+ "1");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "2" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "2"  + "," + Integer.toString(storageShelfNumber) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storageShelfNumber) + " WHERE ID = "+ "2");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "3" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "3"  + "," + Integer.toString(storageIndexInShelf) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storageIndexInShelf) + " WHERE ID = "+ "3");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "4" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "4"  + "," + Integer.toString(storeShelfNumber) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storeShelfNumber) + " WHERE ID = "+ "4");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "5" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "5"  + "," + Integer.toString(storeIndexInShelf) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storeIndexInShelf) + " WHERE ID = "+ "5");
//            }
//
//
//
//        }catch (SQLException e){
//            System.out.println("there is a problem with the database");
//        }
//    }
public static void saveDetails(){
    try{
        java.sql.Statement statement = connection.createStatement();

        // Use prepared statements to prevent SQL injection attacks
        PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO ProductID (ID, Value) VALUES (?, ?)");
        PreparedStatement updateStmt = connection.prepareStatement("UPDATE ProductID SET Value = ? WHERE ID = ?");

        // Update or insert values for each ID in ProductID table
        updateOrInsertValue(statement, insertStmt, updateStmt, 1, curr);
        updateOrInsertValue(statement, insertStmt, updateStmt, 2, storageShelfNumber);
        updateOrInsertValue(statement, insertStmt, updateStmt, 3, storageIndexInShelf);
        updateOrInsertValue(statement, insertStmt, updateStmt, 4, storeShelfNumber);
        updateOrInsertValue(statement, insertStmt, updateStmt, 5, storeIndexInShelf);

    }catch (SQLException e){
        System.out.println("there is a problem with the database");
    }
}

    private static void updateOrInsertValue(java.sql.Statement statement, PreparedStatement insertStmt, PreparedStatement updateStmt, int id, int value) throws SQLException {
        java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID = " + id);
        if(!resultSet.next()){
            insertStmt.setInt(1, id);
            insertStmt.setInt(2, value);
            insertStmt.executeUpdate();
        }
        else{
            updateStmt.setInt(1, value);
            updateStmt.setInt(2, id);
            updateStmt.executeUpdate();
        }
    }


    public static int getProductId() {
        return ++curr;

    }
    public static int getProductIdNoUpdate(){
        return curr;
    }

    public static int getStorageShelfNumber() {
        return storageShelfNumber;
    }

    public static int getStorageIndexInShelf() {
        return storageIndexInShelf/2;           // always add 1 from store and one from storage
    }
    public static void updateStorageIndexInShelf(){
        storageIndexInShelf++;
    }
    public static void resetIndexInShelf(){
        storageIndexInShelf = 0;
    }

    public static int getStoreShelfNumber() {
        return storeShelfNumber;
    }
    public static void updateStoreShelfNumber(){
        storeShelfNumber++;
    }
    public static void updateStorageShelfNumber(){
        storageShelfNumber++;
    }

    public static int getStoreIndexInShelf() {
        return storeIndexInShelf;
    }
}
