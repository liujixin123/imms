package com.example.imms.web.model;

import java.io.Serializable;

public class BaseSysdictionary  implements Serializable {

    private String   id;

    /** 字典编码 */
    private String   codevalue;

    /** 字典名称 */
    private String   lable;

    /** 字典类型 */
    private String   columntype;

    private String   remark;

    private String   status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodevalue() {
        return codevalue;
    }

    public void setCodevalue(String codevalue) {
        this.codevalue = codevalue;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getColumntype() {
        return columntype;
    }

    public void setColumntype(String columntype) {
        this.columntype = columntype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BaseSysdictionary{" +
                "id='" + id + '\'' +
                ", codevalue='" + codevalue + '\'' +
                ", lable='" + lable + '\'' +
                ", columntype='" + columntype + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
