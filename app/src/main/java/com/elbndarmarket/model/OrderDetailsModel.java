package com.elbndarmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("0")
    @Expose
    private Order order;

    //Getter
    public Boolean getStatus() {
        return status;
    }

    public Order getOrder() {
        return order;
    }

    // Order Class
    public static class Order {
        @SerializedName("information")
        private Information information;
        @SerializedName("products")
        private List<Products> products;

        // Getter
        public Information getInformation() {
            return information;
        }

        public List<Products> getProducts() {
            return products;
        }
    }


    // Information Class
    public static class Information {
        @SerializedName("Order_id")
        private int Order_id;
        @SerializedName("Order_User_id")
        private int Order_User_id;
        @SerializedName("ItemsNo")
        private int ItemsNo;
        @SerializedName("Order_Status")
        private String Order_Status;
        @SerializedName("TotalCost")
        private int TotalCost;
        @SerializedName("TotalShippingCost")
        private int TotalShippingCost;
        @SerializedName("PayMethod")
        private String PayMethod;
        @SerializedName("Order_created_at")
        private String Order_created_at;
        @SerializedName("Order_updated_at")
        private String Order_updated_at;


        // Getter
        public int getOrder_id() {
            return Order_id;
        }

        public int getOrder_User_id() {
            return Order_User_id;
        }

        public int getItemsNo() {
            return ItemsNo;
        }

        public String getOrder_Status() {
            return Order_Status;
        }

        public int getTotalCost() {
            return TotalCost;
        }

        public int getTotalShippingCost() {
            return TotalShippingCost;
        }

        public String getPayMethod() {
            return PayMethod;
        }

        public String getOrder_created_at() {
            return Order_created_at;
        }

        public String getOrder_updated_at() {
            return Order_updated_at;
        }
    }


    // Products Class
    public static class Products {
        @SerializedName("OrderProduct_id")
        private int OrderProduct_id;
        @SerializedName("Order_id")
        private int Order_id;
        @SerializedName("Product_id")
        private int Product_id;
        @SerializedName("Status")
        private String Status;
        @SerializedName("Quantity")
        private int Quantity;
        @SerializedName("UnitCost")
        private int UnitCost;
        @SerializedName("TotalCost")
        private int TotalCost;
        @SerializedName("Section_id")
        private int Section_id;
        @SerializedName("Storeid")
        private int Storeid;
        @SerializedName("Seller_id")
        private int Seller_id;
        @SerializedName("Categoryid")
        private int Categoryid;
        @SerializedName("SubCategoryid")
        private int SubCategoryid;
        @SerializedName("Brand_id")
        private int Brand_id;
        @SerializedName("Nationalbarcode")
        private String Nationalbarcode;
        @SerializedName("ActivationMode")
        private String ActivationMode;
        @SerializedName("product_name_ar")
        private String product_name_ar;
        @SerializedName("product_name_en")
        private String product_name_en;
        @SerializedName("product_desc_ar")
        private String product_desc_ar;
        @SerializedName("product_desc_en")
        private String product_desc_en;
        @SerializedName("Price")
        private int Price;
        @SerializedName("Discount_Per")
        private int Discount_Per;
        @SerializedName("ReturnActivation")
        private String ReturnActivation;
        @SerializedName("MaxReturnDays")
        private int MaxReturnDays;
        @SerializedName("AvailableQty")
        private int AvailableQty;
        @SerializedName("DeliveryCost")
        private int DeliveryCost;
        @SerializedName("MinimumQTY")
        private int MinimumQTY;
        @SerializedName("StartDate")
        private String StartDate;
        @SerializedName("EndDate")
        private String EndDate;
        @SerializedName("PreparationDays")
        private int PreparationDays;
        @SerializedName("height")
        private int height;
        @SerializedName("length")
        private int length;
        @SerializedName("weight")
        private int weight;
        @SerializedName("width")
        private int width;
        @SerializedName("IsFeatured")
        private int IsFeatured;
        @SerializedName("Shape_Status")
        private String Shape_Status;
        @SerializedName("ProductCountry")
        private String ProductCountry;
        @SerializedName("ProductCity")
        private String ProductCity;
        @SerializedName("ProductAddress")
        private String ProductAddress;
        @SerializedName("produc_sologn_ar")
        private String produc_sologn_ar;
        @SerializedName("produc_sologn_en")
        private String produc_sologn_en;
        @SerializedName("ProductApartment")
        private String ProductApartment;
        @SerializedName("ProductFloor")
        private String ProductFloor;
        @SerializedName("ProductArea")
        private String ProductArea;
        @SerializedName("ProductStreet")
        private String ProductStreet;
        @SerializedName("ProductBulding")
        private String ProductBulding;
        @SerializedName("created_at")
        private String created_at;


        // Getter
        public int getOrderProduct_id() {
            return OrderProduct_id;
        }

        public int getOrder_id() {
            return Order_id;
        }

        public int getProduct_id() {
            return Product_id;
        }

        public String getStatus() {
            return Status;
        }

        public int getQuantity() {
            return Quantity;
        }

        public int getUnitCost() {
            return UnitCost;
        }

        public int getTotalCost() {
            return TotalCost;
        }

        public int getSection_id() {
            return Section_id;
        }

        public int getStoreid() {
            return Storeid;
        }

        public int getSeller_id() {
            return Seller_id;
        }

        public int getCategoryid() {
            return Categoryid;
        }

        public int getSubCategoryid() {
            return SubCategoryid;
        }

        public int getBrand_id() {
            return Brand_id;
        }

        public String getNationalbarcode() {
            return Nationalbarcode;
        }

        public String getActivationMode() {
            return ActivationMode;
        }

        public String getProduct_name_ar() {
            return product_name_ar;
        }

        public String getProduct_name_en() {
            return product_name_en;
        }

        public String getProduct_desc_ar() {
            return product_desc_ar;
        }

        public String getProduct_desc_en() {
            return product_desc_en;
        }

        public int getPrice() {
            return Price;
        }

        public int getDiscount_Per() {
            return Discount_Per;
        }

        public String getReturnActivation() {
            return ReturnActivation;
        }

        public int getMaxReturnDays() {
            return MaxReturnDays;
        }

        public int getAvailableQty() {
            return AvailableQty;
        }

        public int getDeliveryCost() {
            return DeliveryCost;
        }

        public int getMinimumQTY() {
            return MinimumQTY;
        }

        public String getStartDate() {
            return StartDate;
        }

        public String getEndDate() {
            return EndDate;
        }

        public int getPreparationDays() {
            return PreparationDays;
        }

        public int getHeight() {
            return height;
        }

        public int getLength() {
            return length;
        }

        public int getWeight() {
            return weight;
        }

        public int getWidth() {
            return width;
        }

        public int getIsFeatured() {
            return IsFeatured;
        }

        public String getShape_Status() {
            return Shape_Status;
        }

        public String getProductCountry() {
            return ProductCountry;
        }

        public String getProductCity() {
            return ProductCity;
        }

        public String getProductAddress() {
            return ProductAddress;
        }

        public String getProduc_sologn_ar() {
            return produc_sologn_ar;
        }

        public String getProduc_sologn_en() {
            return produc_sologn_en;
        }

        public String getProductApartment() {
            return ProductApartment;
        }

        public String getProductFloor() {
            return ProductFloor;
        }

        public String getProductArea() {
            return ProductArea;
        }

        public String getProductStreet() {
            return ProductStreet;
        }

        public String getProductBulding() {
            return ProductBulding;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
