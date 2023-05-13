package Stock.Business;

import Stock.DataAccess.DamagedProductDAO;
import Stock.DataAccess.ExpDateDAO;
import Stock.DataAccess.ProductDAO;
import Stock.DataAccess.ProductDetailsDAO;

import java.util.Date;

public class ProductManager {

    private static final ProductDAO productDAO = ProductDAO.getInstance();
    private static final ExpDateDAO expDateDAO = ExpDateDAO.getInstance();
    private static final DamagedProductDAO damagedProductDAO = DamagedProductDAO.getInstance();
    private static final ProductDetailsDAO productDetailsDAO = ProductDetailsDAO.getInstance();



    private static ProductManager instance = null;

    private static Store store;//= new Store(30); // Freshie check
    private static Storage storage;// = new Storage(30);// Freshie check

    private static Shortages shortages;

    private ProductManager() {
        // private constructor

    }
    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    // other methods and variables of the class

    // Case 1 at Product's menu
    public boolean addNewProduct(String categoryStr, String subCategoryStr, String subSubCategoryStr, String manufacturer,
                                 int quantity, int minQuantity, double weight, Date expirationDate) {
        /**
         * Add a new product to the system.
         * @param categoryStr the name of the product category.
         * @param subCategoryStr the name of the product sub-category.
         * @param subSubCategoryStr the name of the product sub-sub-category.
         * @param manufacturer the name of the product manufacturer.
         * @param quantity the quantity of the new product to add.
         * @param minQuantity the minimum quantity of the new product to have in stock.
         * @param weight the weight of the new product.
         * @param expirationDate the expiration date of the new product.
         * @return true if the product was successfully added, false otherwise.
         */
        // New one
        if (getProductByCategories(subCategoryStr, subSubCategoryStr) == null) {
            // Old one
            //if (getProductByCategories(categoryStr, subCategoryStr, subSubCategoryStr) == null) {
            AProductCategory Ccategory = new AProductCategory(categoryStr);
            AProductCategory CsubCategoryStr = new AProductCategory(subCategoryStr);
            String[] parts = subSubCategoryStr.split(" ");
            double number = Double.parseDouble(parts[0]);
            String unit = parts[1];
            AProductSubCategory CsubSubCategoryStr = new AProductSubCategory(number, unit);
            Location storageLocation = new Location(-1, -1);
            Location storeLocation = new Location(-1, -1);
            Product product = new Product(Ccategory, CsubCategoryStr, CsubSubCategoryStr, storageLocation, storeLocation,
                    manufacturer, quantity, minQuantity, weight, expirationDate);
            // New one
            productDAO.addNewProductToProducts(product);

            // Old one
            //stock.addNewProductToStock(product, minQuantity + 100, minQuantity + 30, minQuantity);

            product.setStoreLocation(store.addProductToStore(product));
            product.setStorageLocation(storage.addProductToStorage(product));
            product.setCatalogNumber();
            productDAO.writeProducts(); // Freshie check
            productDetailsDAO.saveDetails(); // Freshie check
            System.out.println(product.getName() + " : " + (ProductDetailsDAO.getInstance().getProductIdNoUpdate() - quantity + 1)
                    + "-" + ProductDetailsDAO.getInstance().getProductIdNoUpdate());
            return true;
        } else {
            return false;
        }
    }

    // ------------ Helper function for Case 2.1 in Product UI ------------
    public Product getProductByCategories(String subCategory,String subSubCategory){
        String[] subsubSplited = subSubCategory.split(" ");
        String name = subCategory + " " + Double.toString(Double.parseDouble(subsubSplited[subsubSplited.length - 2]))
                + " " + subsubSplited[subsubSplited.length - 1];
        String productCatalogNumber = UniqueStringGenerator.generateUniqueString(name);
        return productDAO.getProduct(productCatalogNumber);
    }

    // Case 2.1 at Product's menu
    public void addMoreItemsToProduct(Product product, Date expDate, int quantity){
        product.addMoreItemsToProduct(quantity,expDate);
        //productDAO.getInstance();
        productDAO.writeProducts();
        //ExpDateDAO.getInstance();
        expDateDAO.writeExpDates();
        productDetailsDAO.saveDetails(); // Freshie check
    }

    // Case 2.2 at Product's menu
    public void sellProductsByUniqueCode(Product soldProduct, int quantitySold){
        int[] sold = soldProduct.sellMultipleItemsFromProduct(quantitySold);
//        for (int i = 0; i < quantitySold; i++) {
//            sales.addSale(product, sold[i]);
//        }
        for(Integer qr : sold){
            expDateDAO.deleteExpDate(qr);
//            soldProduct.getExpirationDates().remove(qr);
        }
        if (soldProduct.getStoreQuantity() == 0) {
            shortages.addProductToShortages(soldProduct);
        }
        //productDAO.getInstance();
        productDAO.writeProducts();
        //ExpDateDAO.getInstance();
        expDateDAO.writeExpDates();
        productDetailsDAO.saveDetails(); // Freshie check
    }


    // ------------ Helper function for Case 3 in Product UI ------------
    public Product getProductByUniqueCode(String productCatalogNumber) {
        return productDAO.getProduct(productCatalogNumber);
    }

    // Case 3 at Product's menu
    public void markAsDamaged(Product defectedProduct, int uniqueCode, String reason){
        if (expDateDAO.isQRfromCatalogNumber(defectedProduct.getCatalogNumber(),uniqueCode)) {
            defectedProduct.markAsDamaged(uniqueCode, reason);
            defectedProduct.getExpirationDates().remove(uniqueCode);
            ExpDateDAO.getInstance().deleteExpDate(uniqueCode);
            if(defectedProduct.getStorageQuantity() > 0){
                defectedProduct.storageQuantityMinus1();
            }
            else{
                if(defectedProduct.getStoreQuantity() > 0) {
                    defectedProduct.storeQuantityMinus1();
                }
            }
            //productDAO.getInstance();
            productDAO.writeProducts();
            //ExpDateDAO.getInstance();
            damagedProductDAO.writeDamagedProducts();

        }
        else {
            System.out.println("the QR is does not belong to this Catalog Number! ");
        }
    }

    // Case 4 at Product's menu
    public void printProductInformation(int productInformationCase, Product product) {
        /**
         * Prints the specified information of the given product to the console.
         * @param productInformationCase an integer indicating which piece of information to print:
         *                               - 1 for the product catalog number
         *                               - 2 for the product name
         * @param product the product whose information to print
         */
        if (productInformationCase == 1) {
            System.out.println(product.getCatalogNumber());
        }
        if (productInformationCase == 2) {
            System.out.println(product.getName());
        }
    }

    // ------------ Case 5 in Product UI ------------
    public void setMinimumQuantity(Product product, int newMinQuantity) {
        product.setMinimumQuantity(newMinQuantity);
        System.out.println("The new minimum quantity of " + product.getName() + " is " + newMinQuantity);
        productDAO.writeProducts();
    }


    public static void setStore(Store store) {          // freshie change
        ProductManager.store = store;
    }

    public static void setStorage(Storage storage) {
        ProductManager.storage = storage;               // freshie change
    }
}
