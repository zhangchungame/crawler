package com.xlh.crawler.dto;

import javax.persistence.Id;

public class CraCorpInfo {
    @Id
    private int id;
    private String enterpriseName;
    private String creditNo;
    private String url;
    private int rightStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRightStatus() {
        return rightStatus;
    }

    public void setRightStatus(int rightStatus) {
        this.rightStatus = rightStatus;
    }
}
