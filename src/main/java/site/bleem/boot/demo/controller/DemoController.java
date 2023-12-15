package site.bleem.boot.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yubs
 * @desc demo
 * @date 2023/12/15
 */
@RestController
public class DemoController {
    @GetMapping("/hello/{word}")
    public ResponseEntity<String> returnWorld(@PathVariable("word") String word) {
        return ResponseEntity.ok("hello "+word);
    }
}
