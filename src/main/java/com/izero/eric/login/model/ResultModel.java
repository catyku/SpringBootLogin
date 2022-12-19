/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.izero.eric.login.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 *
 * @author Eric
 */
@Component
public class ResultModel implements Serializable{
    private Boolean success;
    private BigDecimal total;
    private String msg;
    private String codeMsg;
    private List rows;
    private BigDecimal code;
    private String outfile;
    private Timestamp resptime;

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }

    
    
    public Timestamp getResptime() {
        return resptime;
    }

    public void setResptime(Timestamp resptime) {
        this.resptime = resptime;
    }
    
    
    
    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }
    
    public ResultModel(boolean success,String msg) {
        this.success = success;
        total = BigDecimal.ZERO;
        code = BigDecimal.ZERO;
        this.msg = msg;
        outfile = "";
        rows = new ArrayList<Object>();
    }
    public ResultModel() {
        success = false;
        total = BigDecimal.ZERO;
        code = BigDecimal.ZERO;
        msg = "初始化異常";
        outfile = "";
        rows = new ArrayList<Object>();
    }
    public void setFail(String msg){
        success = false;
        this.msg = msg;
    }
    
    public void setSuccess(String msg){
        success = true;
        this.msg = msg;
    }
    public void setSuccess(){
        success = true;
        this.msg = "";
    }
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public String getOutfile() {
        return outfile;
    }

    public void setOutfile(String outfile) {
        this.outfile = outfile;
    }
    
}
