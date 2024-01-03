package site.bleem.boot.demo.controller.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 报警记录查询返回实体类
 */
@Data
public class AlarmVO {

//    // 序号列，使用 @Excel 注解指定列名
//    @Excel(name = "序号", orderNum = "0", width = 5)
//    private Integer serialNumber;

    /**
     * 场所名称
     */
    @Excel(name = "场所名称", width = 20)
    private String placeName;
    /**
     * 规则id
     */
    private Integer ruleId;
    /**
     * 报警规则
     */
    @Excel(name = "报警规则", width = 20)
    private String ruleName;
    /**
     * 摄像仪编号
     */
    @Excel(name = "摄像仪编号", width = 20)
    private String cameraCode;
    /**
     * 摄像仪安装位置
     */
    @Excel(name = "摄像仪安装位置", width = 20)
    private String position;
    /**
     * 报警等级1/2/3
     */
    @Excel(name = "报警等级", width = 10)
    private String alarmDegree;
    /**
     * 报警时间
     */
    @Excel(name = "报警时间", width = 20)
    private String time;
    /**
     * 报警信息
     */
    @Excel(name = "报警信息", width = 50)
    private String alarmMsg;


//    public Integer getSerialNumber() {
//        return serialNumber;
//    }
//
//    public void setSerialNumber(Integer serialNumber) {
//        this.serialNumber = serialNumber;
//    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCameraCode() {
        return cameraCode;
    }

    public void setCameraCode(String cameraCode) {
        this.cameraCode = cameraCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAlarmDegree() {
        return alarmDegree;
    }

    public void setAlarmDegree(String alarmDegree) {
        this.alarmDegree = alarmDegree;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarmMsg() {
        return alarmMsg;
    }

    public void setAlarmMsg(String alarmMsg) {
        this.alarmMsg = alarmMsg;
    }
}
