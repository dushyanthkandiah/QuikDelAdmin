package Models;

/**
 * Created by Dushyanth on 2018-12-04.
 */

public class ClassRequestList {

    private ClassProducts classProducts;
    private ClassRequest classRequest;
    private Double qty, subTotal;

    public ClassRequestList(int reqId, int prdId, String prdName, Double qty, Double subTotal) {
        classProducts = new ClassProducts();
        classRequest = new ClassRequest();
        classRequest.setReqId(reqId);
        classProducts.setId(prdId);
        classProducts.setName(prdName);
        this.qty = qty;
        this.subTotal = subTotal;
    }

    public ClassProducts getClassProducts() {
        return classProducts;
    }

    public void setClassProducts(ClassProducts classProducts) {
        this.classProducts = classProducts;
    }

    public ClassRequest getClassRequest() {
        return classRequest;
    }

    public void setClassRequest(ClassRequest classRequest) {
        this.classRequest = classRequest;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
