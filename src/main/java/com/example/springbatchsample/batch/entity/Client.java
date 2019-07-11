package com.example.springbatchsample.batch.entity;

import java.sql.Timestamp;

/*
 * 取引先データを保持するエンティティ
 */
public class Client {

    private String clientId;
    private String clientName;
    private String salesStaffNo;
    private int isInvalid;
    private String updaterId;
    private Timestamp updatedAt;
    private String createrId;
    private Timestamp createdAt;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSalesStaffNo() {
        return salesStaffNo;
    }

    public void setSalesStaffNo(String salesStaffNo) {
        this.salesStaffNo = salesStaffNo;
    }

    public int getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(int isInvalid) {
        this.isInvalid = isInvalid;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
