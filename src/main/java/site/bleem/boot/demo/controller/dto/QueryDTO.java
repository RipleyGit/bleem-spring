package site.bleem.boot.demo.controller.dto;

/**
 * @author yubs
 * @desc 查询
 * @date 2023/12/16
 */

public class QueryDTO {
    private Integer id;
    private String keyWord;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
