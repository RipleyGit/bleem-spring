package site.bleem.boot.demo.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.demo.server.dto.PushChannelGeneralDTO;
import site.bleem.boot.demo.server.ThirdPartyServer;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class RetryController {


    @Resource
    private ThirdPartyServer thirdPartyServer;

    @GetMapping("/api/v1/retry")
    public ResponseEntity<Object> queryMethod() {
        PushChannelGeneralDTO dto = new PushChannelGeneralDTO();
        String dateStr = "2023-12-26 09:39:43";
        Date date = new Date();//DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd HH:mm:ss"});
        Long second = date.getTime() / 1000;
        dto.setCompany("320117000001");//企业编码
        dto.setBiz("0500000");
        dto.setChannel("0040000");
        dto.setPdt(String.valueOf(second));
        dto.setUrl("http://172.23.57.90:8080/savingVideos/20231226/3201170000010400000620231224103943.mp4");
//        dto.setFile(file);
//        File newFile = new File("./target/" + dto.getCompany() + dto.getBiz() + dto.getChannel() + DateUtils.formatDate(date, "yyyyMMDDHHmmss") + ".mp4");
//        file.renameTo(newFile);
//        dto.setFile(newFile);
        try {
            thirdPartyServer.handleAlarmMessage(JSONObject.toJSONString(dto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("this is restful ok！");
    }
}
