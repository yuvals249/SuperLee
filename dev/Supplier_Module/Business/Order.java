package Supplier_Module.Business;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.DAO.OrderDAO;

import java.time.LocalDate;
import java.util.Map;

public class Order {
    private Supplier supplier;
    private String address;
    private LocalDate startDate;
    private LocalDate supplyDate;
    private final int order_id;
    private Map<SupplierProduct, Integer> products_list_order;
    private int kind;


    private double total_price;

    /**constructor
     *
     */
    public Order(Supplier supplier1, Map<SupplierProduct,Integer> products_list_order1, LocalDate date_s, int order_id){
        this.supplier = supplier1;
        this.products_list_order = products_list_order1;
        this.order_id = order_id;
        this.startDate = date_s;
        this.supplyDate = date_s.plusDays(supplier1.agreement.getMethodSupply().GetSupplyDate(date_s));
        this.total_price=fullOrderPrice();
    }

    public Order(Supplier supplier1, Map<SupplierProduct,Integer> products_list_order1, LocalDate startDate, LocalDate supplyDate , int order_id){
        this.supplier = supplier1;
        this.products_list_order = products_list_order1;
        this.order_id = order_id;
        this.startDate = startDate;
        this.supplyDate = supplyDate;
        this.total_price=fullOrderPrice();
    }
    public Order(Supplier supplier1, Map<SupplierProduct,Integer> products_list_order1, LocalDate startDate, LocalDate supplyDate , int order_id, int kind){
        this.supplier = supplier1;
        this.products_list_order = products_list_order1;
        this.order_id = order_id;
        this.startDate = startDate;
        this.supplyDate = supplyDate;
        this.total_price=fullOrderPrice();
        this.kind = kind;
    }

    public Map<SupplierProduct, Integer> getProducts_list_order() {
        return products_list_order;
    }
    public Map<SupplierProduct, Integer> getProducts_list_order2() {
        return OrderDAO.getInstance().getProductsByOrderID(this.order_id);
    }

    public void setProducts_list_order(Map<SupplierProduct, Integer> products_list_order) {
        this.products_list_order = products_list_order;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     *Getter and Setters
     */
    public String getSupplier_name() {
        return this.supplier.card.getSupplier_name();
    }

    public int getSupplier_id() {
        return this.supplier.card.getSupplier_number();
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(LocalDate supplyDate) {
        this.supplyDate = supplyDate;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getContact_phone() {
        return this.supplier.card.getContact_members().get(0).getPhone_number();
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    /**
     * the function print the order info
     */
    public void PrintOrder() {
        System.out.println("--------ORDER-------");
        String name = this.supplier.card.getSupplier_name();
        String address1 = this.supplier.card.getAddress();
        int sup_id = this.supplier.card.getSupplier_number();
        String num = this.supplier.card.getContact_members().get(0).getPhone_number();
        System.out.println("Supplier name: " + name + ", Address: " + address1 + ", Order id: " + this.order_id);
        System.out.println("Supplier id: " + sup_id + ", Start date: " + this.startDate.toString() + ", Supply date: " + this.supplyDate +", Phone number to Contact: " + num);
        for(Map.Entry<SupplierProduct, Integer> iter : this.products_list_order.entrySet()){
            iter.getKey().printProductToOrder(iter.getValue());
        }
        System.out.println("");
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public double totalDiscountBytotalAmount()
    {
        int total_amount=0;
        for(Integer p_amount: this.products_list_order.values())
            total_amount+=p_amount;

        for(PrecentageDiscount pd: this.supplier.getAgreement().getTotal_orderDiscount())
        {
            int min= pd.getAmountRange().getMin();
            int max= pd.getAmountRange().getMax();
            if(total_amount>= min && total_amount<= max)
                return pd.getPercentage();
        }

        return 0;
    }

    public double fullOrderPrice(){
        double sum = 0;
        for(Map.Entry <SupplierProduct,Integer> sp : this.products_list_order.entrySet()){
            double x=sp.getKey().discountByAmount(sp.getValue());
            double Precentage= 100-sp.getKey().discountByAmount(sp.getValue());
            double discount = Precentage*0.01;

            //double discount = (100-(sp.getKey().discountByAmount(sp.getValue()))*0.01);
            int amount = sp.getValue();
            double u_price = sp.getKey().getUnit_price();
            sum += (discount*u_price*amount);

        }
        return sum*this.totalDiscountBytotalAmount();
    }


}

