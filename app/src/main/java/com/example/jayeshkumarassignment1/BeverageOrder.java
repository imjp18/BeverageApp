package com.example.jayeshkumarassignment1;
  //this class stores the beverage order details
public class BeverageOrder {
    private final String customerName;
    private final String customerEmail;
    private final String customerPhone;
    private final String region;
    private final String store;
    private final String beverageType;
    private final String beverageSize;
    private final boolean addMilk;
    private final boolean addSugar;
    private final String flavour;
    private final String date;
    private double finalAmount;

    public BeverageOrder(String customerName, String customerEmail, String customerPhone, String region, String store,
                         String beverageType, String beverageSize, boolean addMilk, boolean addSugar, String flavour, String date) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.region = region;
        this.store = store;
        this.beverageType = beverageType;
        this.beverageSize = beverageSize;
        this.addMilk = addMilk;
        this.addSugar = addSugar;
        this.flavour = flavour;
        this.date = date;

        calculateAmounts();
    }
     // this method calculates the total amount
    private void calculateAmounts() {
        double total = 0;
        if (beverageType.equals("Coffee")) {
            switch (beverageSize) {
                case "Small":
                    total += 1.75;
                    break;
                case "Medium":
                    total += 2.75;
                    break;
                case "Large":
                    total += 3.75;
                    break;
            }
        } else {
            switch (beverageSize) {
                case "Small":
                    total += 1.50;
                    break;
                case "Medium":
                    total += 2.50;
                    break;
                case "Large":
                    total += 3.25;
                    break;
            }
        }
        if (addMilk) {
            total += 1.25;
        }
        if (addSugar) {
            total += 1.00;
        }
        switch (flavour) {
            case "Pumpkin Spice":
                total += 0.50;
            case "Chocolate":
                total += 0.75;
                break;
            case "Lemon":
                total += 0.25;
                break;
            case "Ginger":
                total += 0.75;
                break;
        }
        double totalAmount = total;
        double taxAmount = total * 0.13;
        this.finalAmount = totalAmount + taxAmount;
    }
   // the receipt is returned as a string to be displayed in the ui
    public String getReceipt() {
        return String.format("Name: %s\nEmail: %s\nPhone: %s\nBeverage: %s\nSize: %s\nMilk: %b\nSugar: %b\nFlavour: %s\nRegion: %s\nStore: %s\nDate: %s\nTotal: $%.2f",
                customerName, customerEmail, customerPhone, beverageType, beverageSize, addMilk, addSugar, flavour, region, store, date, finalAmount);
    }


}
