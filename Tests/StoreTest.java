import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    AProductCategory category = new AProductCategory("Milk Products");
    AProductSubCategory subCategory = new AProductCategory("Milk 3%");
    AProductSubCategory subSubCategory = new AProductSubCategory(1,"Litter") ;

    Location storeLocation = new Location(3,3);
    Location storageLocation = new Location(2,2);
    Date expDate = new Date(2023, Calendar.DECEMBER,12);


    AProductCategory category1 = new AProductCategory("Meat");
    AProductSubCategory subCategory1 = new AProductCategory("Burgers");
    AProductSubCategory subSubCategory1 = new AProductSubCategory(1,"Kg") ;

    Location storeLocation1 = new Location(3,3);
    Location storageLocation1 = new Location(2,2);
    Date expDate1 = new Date(2023, Calendar.MAY,12);

    Product product = new Product(category,subCategory,subSubCategory,storageLocation,storeLocation,"Yotvata",40,10,1.2,expDate);
    Product product1 = new Product(category1,subCategory1,subSubCategory1,storageLocation1,storeLocation1,"Yumyum",30,20,1.0,expDate1);

    Location firstItem = new Location(0,0);
    Location secondItem = new Location(0,1);
    Store store = new Store(20);

    @Test
    void addProductToStore() {
        product.setStorageLocation(store.addProductToStore(product));
        product.setStoreLocation(store.addProductToStore(product));
        assertEquals(store.getShelves()[0].getItems()[0], product);
        assertEquals(store.getShelves()[0].getItems()[1], product1);
        assertEquals(product.getStorageLocation().getLocation(), firstItem.getLocation());
        assertEquals(product1.getStorageLocation().getLocation(), secondItem.getLocation());
        store.updateStoreShelvesNumber(40);
        assertEquals(store.getShelves()[0].getItems()[0], product);
        assertEquals(store.getShelves()[0].getItems()[1], product1);

    }
}