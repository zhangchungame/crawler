package com.xlh.crawler.dto;

import com.alibaba.fastjson.JSON;

public class ProxyDaXiang {
    private String ip;
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
