package Models;


import org.json.JSONException;
import org.json.JSONObject;

public class ClassItemCart {

    private int prdId, billId;
    private double quantity, subTotal;
    private String itemName;

    public ClassItemCart(int prdId, double quantity, double subTotal, String itemName) {

        this.prdId = prdId;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.itemName = itemName;
    }

    public ClassItemCart() {
    }


    public int getPrdId() {
        return prdId;
    }

    public void setPrdId(int prdId) {
        this.prdId = prdId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("itemName", itemName);
            obj.put("prdId", prdId);
            obj.put("quantity", quantity);
            obj.put("subTotal", subTotal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

}
