package Models;

/**
 * Created by Dushyanth on 2018-12-04.
 */

public class ClassRequest {

    private int reqId, status;
    private ClassUsers classUsers;
    private String reqDate, remarks;
    private Double total;

    public ClassRequest(int reqId, int cusId, int status, String cusName, String reqDate, String remarks, Double total) {
        classUsers = new ClassUsers();
        this.reqId = reqId;
        this.status = status;
        this.reqDate = reqDate;
        this.total = total;
        this.remarks = remarks;
        classUsers.setId(cusId);
        classUsers.setName(cusName);
    }

    public ClassRequest() {

    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ClassUsers getClassUsers() {
        return classUsers;
    }

    public void setClassUsers(ClassUsers classUsers) {
        this.classUsers = classUsers;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
