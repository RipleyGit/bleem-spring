package site.bleem.boot.demo.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.bleem.boot.demo.server.dto.ProbeStatusDTO;
import site.bleem.boot.demo.server.dto.PushChannelGeneralDTO;
import site.bleem.boot.demo.server.dto.VideoRegisterDTO;
import site.bleem.boot.demo.server.Spjr001SealThirdPartyServer;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Spjr001FeginClientTest {


//    @Resource
//    private Spjr001FeginClient spjr001FeginClient;

    @Resource
    private Spjr001SealThirdPartyServer spjr001HttpClient;

    @Test
    public void testRegisterVideo(){
        VideoRegisterDTO dto = new VideoRegisterDTO();
        dto.setCompany("320113000001");
        dto.setBiz("0100000");
        dto.setName("主井口");
        dto.setProtocol("MJPEG");
        dto.setRaw_codec("H.264");
        dto.setUrl("https://192.168.55.135:33390");

        spjr001HttpClient.postRegisterVideo(dto);
        System.out.println("请求成功！");
    }

    @Test
    public void testPushProbeStatus(){
        ProbeStatusDTO dto = new ProbeStatusDTO();
        dto.setCompany("320117000001");
        dto.setBiz("0500101");
        dto.setBiz_type("0030001");
        dto.setStatus(1);
        spjr001HttpClient.pushProbeStatus(dto);
        System.out.println("请求成功！");
    }

    @Test
    public void pushGeneralTest(){
//        File file = new File("Y:\\bestway\\imine\\v3.3\\FILES\\矿山调度中心_20231225104443(空岗)\\3201170000010400000620231224103943.mp4");
        File file = spjr001HttpClient.getFileByUrl("http://172.23.57.90:8080/savingVideos/20231226/3201170000010400000620231224103943.mp4");
        PushChannelGeneralDTO dto = new PushChannelGeneralDTO();
        String dateStr= "2023-12-26 09:39:43";
        Date date = new Date();//DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd HH:mm:ss"});
        Long second = date.getTime() / 1000;
        dto.setCompany("320117000001");//企业编码
        dto.setBiz("0500000");
        dto.setChannel("0040000");
        dto.setPdt(String.valueOf(second));
        dto.setFile(file);
//        File newFile = new File("./target/" + dto.getCompany() + dto.getBiz() + dto.getChannel() + DateUtils.formatDate(date, "yyyyMMDDHHmmss") + ".mp4");
//        file.renameTo(newFile);
//        dto.setFile(newFile);
        spjr001HttpClient.pushGeneral(dto);

    }

}