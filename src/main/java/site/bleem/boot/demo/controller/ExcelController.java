package site.bleem.boot.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.demo.controller.vo.AlarmVO;
import site.bleem.boot.demo.util.UserDefinedExcelFileUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
public class ExcelController {

    /**
     * 报警记录导出
     *
     * @return 报警记录
     */
    @GetMapping("/exportList")
    public void exportList(HttpServletResponse response) {
        ArrayList<AlarmVO> alarmVOS = new ArrayList<>();
        AlarmVO alarmVo = new AlarmVO();
        alarmVo.setAlarmDegree("1级");
        alarmVo.setAlarmMsg("系统出现故障");
        alarmVo.setCameraCode("A001");
        alarmVo.setPosition("井口");
        alarmVo.setPlaceName("ddd");
        alarmVo.setRuleName("ddddd");
        alarmVOS.add(alarmVo);
        UserDefinedExcelFileUtil.exportExcel(alarmVOS, AlarmVO.class, "报警记录", "报警记录", response);

    }
}
