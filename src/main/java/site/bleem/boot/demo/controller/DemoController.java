package site.bleem.boot.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.demo.config.AccessLimit;
import site.bleem.boot.demo.exception.BusinessException;

/**
 * @author yubs
 * @desc demo
 */
@RestController
public class DemoController {
    @AccessLimit(message="参数已处理过")
    @GetMapping("/hello/{word}")
    public ResponseEntity<String> returnWorld(@PathVariable("word") String word) {
        return ResponseEntity.ok("hello "+word);
    }    @GetMapping("/exception")
    public ResponseEntity<String> exceptionMsg() {
        throw new BusinessException("这里抛出一个异常");
    }
}
