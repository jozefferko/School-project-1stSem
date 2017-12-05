package modellayer;

import java.util.*;

public class Bundle implements SaleLineItem {
    private String name;
    private double salePrice;
    private String description;
    private String barcode;
    private TreeMap<Integer, Double> discounts;

    private HashMap<Item, Integer> items;

    public Bundle(String barcode, String name, String description, double salePrice) {
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.salePrice = salePrice;
        this.discounts = new TreeMap<>();
        this.items = new HashMap<>();
    }

    public Bundle(String barcode) {
        this.barcode = barcode;
        this.discounts = new TreeMap<>();
        this.items = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

//    public double getSalePrice() {
//        return this.salePrice;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public boolean addItem(Item item, int amount) {
        if (!items.containsKey(item)) {
            items.put(item, amount);
            return true;
        }
        return false;
    }


    @Override
    public double getPrice(int quantity) {
//        return salePrice * quantity;
        if (discounts.lowerKey(quantity) == null)
            return salePrice;
        return salePrice - salePrice * (discounts.lowerKey(quantity) / 100);
    }

    @Override
    public int checkStock(Location location) {
        Iterator it = items.entrySet().iterator();
        int stock = 0;
        Map.Entry pair;
        if (it.hasNext()) {
            pair = (Map.Entry) it.next();
            stock = ((Item) pair.getKey()).checkStock(location) / ((int) pair.getValue());
        }
        while (it.hasNext()) {
            pair = (Map.Entry) it.next();
            if (((Item) pair.getKey()).checkStock(location) / ((int) pair.getValue()) < stock)
                stock = ((Item) pair.getKey()).checkStock(location) / ((int) pair.getValue());
//
//                it.remove();
        }
        return stock;
    }

    @Override
    public boolean removeStock(int amount, Location location) {
        Iterator it = items.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (((Item) pair.getKey()).checkStock(location) < ((int) pair.getValue()) * amount)
                return false;
            it.remove();
        }

        it = items.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ((Item) pair.getKey()).removeStock(amount, location);
            it.remove();
        }
        return true;
    }

    @Override
    public String getBarcode() {
        return barcode;
    }


    //discounts
    @Override
    public double getDiscount(int quantity) {
        return 0;
    }

    public void setDiscount(int amount, double percentage) {
        discounts.put(amount, percentage);
    }

    public boolean removeDiscount(int amount) {
        if (discounts.containsKey(amount)) {
            discounts.remove(amount);
            return true;
        }
        return false;
    }

    public TreeMap<Integer, Double> getDiscounts() {
        return discounts;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
