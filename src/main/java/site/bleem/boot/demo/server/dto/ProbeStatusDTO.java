package site.bleem.boot.demo.server.dto;

import java.io.Serializable;


public class ProbeStatusDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String company;
    private String biz;

    private String biz_type;

    private String link_type="AI";

    private Integer freq=0;

    private String freq_channel="";

    private Integer status;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getBiz_type() {
        return biz_type;
    }

    public void setBiz_type(String biz_type) {
        this.biz_type = biz_type;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public Integer getFreq() {
        return freq;
    }

    public void setFreq(Integer freq) {
        this.freq = freq;
    }

    public String getFreq_channel() {
        return freq_channel;
    }

    public void setFreq_channel(String freq_channel) {
        this.freq_channel = freq_channel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
