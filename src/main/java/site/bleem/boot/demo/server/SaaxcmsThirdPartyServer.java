package site.bleem.boot.demo.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 当配置文件中：saaxcms.enabled: true 配置存在时，该Bean才会被spring进行初始化加载
 */
@Component
@ConditionalOnProperty(name = "saaxcms.enabled", havingValue = "true")
public class SaaxcmsThirdPartyServer {

}