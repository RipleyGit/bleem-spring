package site.bleem.boot.demo.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoRegisterDTO implements Serializable {
    private String company;
    private String biz;
    private String name;
    private String protocol;
    private String raw_codec;
    private String url;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRaw_codec() {
        return raw_codec;
    }

    public void setRaw_codec(String raw_codec) {
        this.raw_codec = raw_codec;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
