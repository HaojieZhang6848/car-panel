package edu.fd.se.rjw.carpanel.docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Configuration
@Slf4j
public class DockerComposeInfoProvider {

    @Value("${car-panel.docker-compose-file-path}")
    private String dockerComposeFilePath_cfg;

    @Value("${car-panel.model}")
    private String carModel;

    @Bean
    protected List<String> monitoredContainerNames(String dockerComposeFilePath) throws IOException {
        // 检查docker-compose文件是否存在
        var file = new File(dockerComposeFilePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("docker-compose文件不存在: " + dockerComposeFilePath);
        }
        var yaml = new Yaml();
        var dockerCompose = yaml.load(new FileInputStream(dockerComposeFilePath));
        var jsonWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        var json = jsonWriter.writeValueAsString(dockerCompose);
        var ctx = JsonPath.parse(json);
        var jsr = (JSONArray) ctx.read("$.services.*.container_name");
        var monitoredContainerNames = jsr.stream().map(Object::toString).toList();
        log.info("监控的容器名称: {}", monitoredContainerNames);
        return monitoredContainerNames;
    }

    @Bean
    protected DockerClient dockerClient() {
        return DockerClientBuilder.getInstance().withDockerHttpClient(new ApacheDockerHttpClient.Builder().dockerHost(URI.create("unix:///var/run/docker.sock")).build()).build();
    }

    @Bean
    protected String dockerComposeFilePath() {
        if (dockerComposeFilePath_cfg != null && !dockerComposeFilePath_cfg.isBlank()) {
            log.info("--car-panel.docker-compose-file-path 选项已设置，使用指定的docker-compose文件路径: {}", dockerComposeFilePath_cfg);
            return dockerComposeFilePath_cfg;
        } else {
            var homeDir = System.getProperty("user.home");
            var defaultDockerComposeFilePath = homeDir + File.separator + carModel + File.separator + "docker-compose.yml";
            log.info("--car-panel.docker-compose-file-path 选项未设置，使用默认的docker-compose文件路径: {}", defaultDockerComposeFilePath);
            return defaultDockerComposeFilePath;
        }
    }
}
