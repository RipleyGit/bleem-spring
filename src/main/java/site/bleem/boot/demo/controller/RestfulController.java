package site.bleem.boot.demo.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.bleem.boot.demo.controller.dto.QueryDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author yubs
 * @desc restful接口格式
 * @date 2023/12/15
 */
@RestController
public class RestfulController {

    @GetMapping("/api/v1/get/{word}")
    public ResponseEntity<String> getMethod(@PathVariable("word") String word) {
        return ResponseEntity.ok("this is restful:get PathVariable "+word);
    }

    @GetMapping("/api/v1/query")
    public ResponseEntity<Object> queryMethod(QueryDTO dto) {
        return ResponseEntity.ok("this is restful:id:"+dto.getId() +" keyword:"+dto.getKeyWord());
    }

    @PostMapping(value = "/api/v1/upload/xlsx", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> uploadXlsx(@RequestParam("xlsxFile") MultipartFile xlsxFile, @RequestParam("id") Integer id, @RequestParam("questionType") Integer questionType) {
        //基础校验
        if (xlsxFile == null || xlsxFile.isEmpty() || xlsxFile.getSize() == 0) {
            throw new RuntimeException("文件不能为空！");
        }
        String originalFilename = xlsxFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("文件名称不能为空！");
        }
        return ResponseEntity.ok("文件名："+originalFilename+" 参数id: "+id+" 参数questionType："+questionType);
    }
}