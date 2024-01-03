package site.bleem.boot.demo.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author yubs
 * @desc 文件传输相关接口
 */
@RestController
public class TransferFileController {

    // Java 8 使用 Paths.get
    private static final Path UPLOAD_DIR = Paths.get("uploads");
    // Java 11 及以上可以使用 Path.of
    // Path path2 = Path.of("path/to/your/file.txt");

    private static final String UPLOAD_URL = "http://127.0.0.1:8080/upload";
    private static final String FILE_PATH = "path/to/your/file.txt";
    private static final int CHUNK_SIZE = 1024 * 1024; // 1MB

    public static void main(String[] args) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // 获取文件总大小

        long totalSize = Files.size(Paths.get(FILE_PATH));

        // 读取文件块并上传
        for (long startByte = 0; startByte < totalSize; startByte += CHUNK_SIZE) {
            long endByte = Math.min(startByte + CHUNK_SIZE - 1, totalSize - 1);

            // 读取文件块
            byte[] fileBytes = Files.readAllBytes(Paths.get(FILE_PATH));
            byte[] chunkBytes = new byte[(int) (endByte - startByte + 1)];
            System.arraycopy(fileBytes, (int) startByte, chunkBytes, 0, chunkBytes.length);

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + startByte + "-" + endByte + "/" + totalSize);

            // 构建请求体
            RequestCallback requestCallback = restTemplate.httpEntityCallback(chunkBytes, byte[].class);

            // 发送请求
            ResponseEntity<String> responseEntity = restTemplate.execute(
                    UPLOAD_URL,
                    HttpMethod.POST,
                    requestCallback,
                    restTemplate.responseEntityExtractor(String.class),
                    headers
            );

            // 处理响应
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                System.out.println("File chunk uploaded successfully");
            } else {
                System.out.println("File chunk upload failed. Status code: " + statusCode);
                // 处理上传失败的逻辑，可能需要重试等
            }
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "Content-Range", required = false) String contentRangeHeader,
            @RequestParam(value = "requestId", required = false) String requestId
    ) throws IOException {
        // 检查是否已处理相同的请求
        if (requestId != null && isRequestProcessed(requestId)) {
            return ResponseEntity.ok("Request already processed");
        }

        // 解析 Content-Range 头
        long startByte = parseStartByte(contentRangeHeader);

        // 保存文件块到服务器
        Path filePath = UPLOAD_DIR.resolve("uploaded-file");
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        // 记录已处理的请求
        if (requestId != null) {
            recordProcessedRequest(requestId);
        }

        // 返回上传状态
        String responseMessage = "File uploaded successfully";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + startByte + "-" + (startByte + file.getSize() - 1) + "/" + file.getSize());

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    private boolean isRequestProcessed(String requestId) {
        // 实现判断请求是否已处理的逻辑
        // 返回 true 表示已处理过，返回 false 表示未处理过
        // 可以使用数据库、缓存等存储已处理的请求标识符
        return false;
    }

    private void recordProcessedRequest(String requestId) {
        // 实现记录已处理请求的逻辑
        // 可以使用数据库、缓存等存储已处理的请求标识符
    }

    private long parseStartByte(String contentRangeHeader) {
        // 解析 Content-Range 头
        // 省略实现细节，可以参考前面的示例
        return 0;
    }

}
