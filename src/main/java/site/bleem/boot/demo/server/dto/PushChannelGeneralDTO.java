package site.bleem.boot.demo.server.dto;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class PushChannelGeneralDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String company;
    private String biz;
    private String channel;
    private String pdt;
    private String url;
    private File file;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPdt() {
        return pdt;
    }

    public void setPdt(String pdt) {
        this.pdt = pdt;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}