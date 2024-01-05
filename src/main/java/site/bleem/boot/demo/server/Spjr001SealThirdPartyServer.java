package site.bleem.boot.demo.server;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryException;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import site.bleem.boot.demo.server.dto.ProbeStatusDTO;
import site.bleem.boot.demo.server.dto.PushChannelGeneralDTO;
import site.bleem.boot.demo.server.dto.VideoRegisterDTO;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Component
public class Spjr001SealThirdPartyServer implements ThirdPartyServer {

    @Value("${spjr001.company:320117000001}")
    private String company;
    @Value("${spjr001.ai.url:https://ksjc.jsemsc.cn:8010/spjr001}")
    private String url;
    //带标记的实时播放地址
    @Value("${spjr001.ai.url.video_register:/api/open/v1/ai/video/register}")
    private String registerVideoUrl;
    //智能AI识别视频上传
    @Value("${spjr001.ai.url.channel_general_push:/api/open/v1/ai/channel/general/push}")
    private String pushGeneralUrl;
    //摄像头在线，离线检测信息
    @Value("${spjr001.ai.url.probe_status_push:/api/open/v1/ai/probe/status/push}")
    private String pushProbeStatusUrl;
    private static CloseableHttpClient httpClient;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @PostConstruct
    public void init() {
        System.out.println("初始化");
    }


    //    @Override
//    public void handleAlarmMessage(String alarmMessage) {
//        PushChannelGeneralDTO dto = JSONObject.parseObject(alarmMessage, PushChannelGeneralDTO.class);
//        RetryCallback<Void, RuntimeException> retryCallback = context -> {
//            int currentAttempt = context.getRetryCount() + 1;
//            System.out.println("Executing attempt number: " + currentAttempt);
//            File file = getFileByUrl(dto.getUrl());
//            dto.setFile(file);
//            // Simulate some operation that may fail
//            pushGeneral(dto);
//            return null; // The return type of RetryCallback's doWithRetry method
//        };
//        RetryTemplate retryTemplate = new RetryTemplate();
//        // 设置重试次数
//        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
//        retryPolicy.setMaxAttempts(3);
//        retryTemplate.setRetryPolicy(retryPolicy);
//        // 设置重试间隔
//        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
//        backOffPolicy.setBackOffPeriod(1000);
//        retryTemplate.setBackOffPolicy(backOffPolicy);
//        retryTemplate.execute(retryCallback);
//    }
//
    private static CloseableHttpClient getHttpsClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        // 指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {

                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        // 构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    private HttpResponse execute(HttpUriRequest request) {

        try {
            if (httpClient == null) {
                httpClient = getHttpsClient();
            }
            HttpResponse httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                throw new RetryException("调用失败，进行重试!异常信息：" + responseBody);
            }
            if ("video/mp4".equalsIgnoreCase(httpResponse.getEntity().getContentType().getValue())) {
                return httpResponse;
            }
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            JSONObject responseJson = JSONObject.parse(responseBody);
            String message = responseJson.getString("message");
            if ("success".equalsIgnoreCase(message)) {
                System.out.println("返回结果：" + responseBody);
                return httpResponse;
            } else {
                throw new RetryException("调用失败，业务异常，错误信息：" + message);
            }
        } catch (Exception e) {
            throw new RetryException("接口调用失败：" + request.getURI().toString() + "，错误信息：" + e.getMessage());
        }
    }

    /**
     * /api/open/v1/ai/video/register
     *
     * @param dto
     */
//    @Retryable(value = {RetryException.class}, maxAttempts = -1, backoff = @Backoff(delay = 1000))
    public void pushProbeStatus(ProbeStatusDTO dto) {
        // 创建 ObjectMapper 并配置
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(dto);
            HttpPost httpPost = new HttpPost(url + registerVideoUrl);
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            // 设置请求体（JSON 格式）
            httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));
            execute(httpPost);
        } catch (Exception e) {
            throw new RetryException("调用失败，进行重试!异常信息：" + e.getMessage());
        }
    }

    /**
     * /api/open/v1/ai/video/register
     *
     * @param dto
     */
    public void postRegisterVideo(VideoRegisterDTO dto) {
        // 创建 ObjectMapper 并配置
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(dto);
            HttpPost httpPost = new HttpPost(url + registerVideoUrl);
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");

            // 设置请求体（JSON 格式）
            httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));

            execute(httpPost);
        } catch (Exception e) {
            throw new RetryException("调用失败，进行重试!异常信息：" + e.getMessage());
        }
    }

    public void pushGeneral(PushChannelGeneralDTO dto) {

        long timestamp = Long.valueOf(dto.getPdt()) * 1000;
        Date date = new Date(timestamp);
        LocalDateTime dateTimeFromDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        System.out.println("Converted DateTime (Date): " + dateTimeFromDate);
        String format = formatter.format(dateTimeFromDate);
        String fileName = dto.getCompany() + dto.getBiz() + dto.getChannel() + format;
        System.out.println("企业编码：" + dto.getCompany());
        System.out.println("摄像仪编码：" + dto.getBiz());
        System.out.println("分析结果编码：" + dto.getChannel());
        System.out.println("告警时间戳（秒）：" + dto.getPdt());
        System.out.println("上传告警视频：" + fileName);
        File file = dto.getFile();
        // Simulate the actual operation
        try {
            HttpPost httpPost = new HttpPost(url + pushGeneralUrl);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.RFC6532)
                    .addBinaryBody("file", file, ContentType.create("application/mp4"), fileName + ".mp4")
                    .addTextBody("company", dto.getCompany())
                    .addTextBody("biz", dto.getBiz())
                    .addTextBody("channel", dto.getChannel())
                    .addTextBody("pdt", dto.getPdt())
            ;
            httpPost.setEntity(multipartEntityBuilder.build());
            execute(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            file.delete();
        }
    }

    public File getFileByUrl(String url) {
        File tmpFileDir = new File("./mp4/");
        if (!tmpFileDir.exists()) {
            tmpFileDir.mkdirs();
        }
        String[] split = url.split("/");
        String storeFile = "./mp4/" + split[split.length - 1];
        File tmpStoreFile = new File(storeFile);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                try (InputStream inputStream = httpEntity.getContent();
                     FileOutputStream outputStream = new FileOutputStream(tmpStoreFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                EntityUtils.consume(httpEntity); // 释放资源
            } else {
                throw new RuntimeException("获取文件失败：No content received from server.");
            }
            return tmpStoreFile;
        } catch (Exception e) {
            throw new RuntimeException("获取文件失败：读取文件异常" + e.getMessage(), e);
        }
    }

    @Override
    public void handleAlarmMessage(String alarmMessage) {
        PushChannelGeneralDTO dto = JSONObject.parseObject(alarmMessage, PushChannelGeneralDTO.class);
        handleAlarmMessage(dto);
    }

    @EventListener
    public void handleAlarmMessage(PushChannelGeneralDTO dto) {
        RetryCallback<Void, RuntimeException> retryCallback = context -> {
            int currentAttempt = context.getRetryCount() + 1;
            System.out.println("Executing attempt number: " + currentAttempt);
            File file = getFileByUrl(dto.getUrl());
            dto.setFile(file);
            // Simulate some operation that may fail
            pushGeneral(dto);
            return null; // The return type of RetryCallback's doWithRetry method
        };
        RetryTemplate retryTemplate = new RetryTemplate();
        // 设置重试次数
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        // 设置重试间隔
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.execute(retryCallback);
    }
}