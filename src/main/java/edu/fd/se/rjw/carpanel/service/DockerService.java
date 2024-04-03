package edu.fd.se.rjw.carpanel.service;

import lombok.Getter;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

@SuppressWarnings("deprecation")
@Getter
@Service
public class DockerService {

    @Autowired
    private String dockerComposeFilePath;

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    @Qualifier("monitoredContainerNames")
    private List<String> monitoredContainerNames;

    public String startDockerCompose(){
        var command = "docker compose -f " + dockerComposeFilePath + " up -d";
        try {
            var process = Runtime.getRuntime().exec(command);
            process.waitFor();
            var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()));
            var line = "";
            var total = "";
            while ((line = reader.readLine()) != null) {
                total += line + "\n";
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String stopDockerCompose(){
        var command = "docker compose -f " + dockerComposeFilePath + " down";
        try {
            var process = Runtime.getRuntime().exec(command);
            process.waitFor();
            var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()));
            var line = "";
            var total = "";
            while ((line = reader.readLine()) != null) {
                total += line + "\n";
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String restartDockerCompose(){
        var command = "docker compose -f " + dockerComposeFilePath + " restart";
        try {
            var process = Runtime.getRuntime().exec(command);
            process.waitFor();
            var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()));
            var line = "";
            var total = "";
            while ((line = reader.readLine()) != null) {
                total += line + "\n";
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public Map<String, String> getMonitoredContainerStatuses(){
        Map<String, String> statuses = new java.util.HashMap<>();
        for (var cName : monitoredContainerNames) {
            String status;
            try{
                var container = dockerClient.inspectContainerCmd(cName).exec();
                status = container.getState().getStatus();
            }catch(NotFoundException e){
                status = "NOT FOUND";
            }
            statuses.put(cName, status);
        }
        return statuses;
    }
    
    public List<String> getContainerLogs(String containerName){
        var logContainerCmd = dockerClient.logContainerCmd(containerName);
        logContainerCmd.withStdOut(true);
        logContainerCmd.withStdErr(true);

        final List<String> logs = new java.util.ArrayList<>();

        try{
            logContainerCmd.exec(new LogContainerResultCallback(){
                public void onNext(Frame item){
                    logs.add(item.toString());
                }
            }).awaitCompletion();
        }catch(InterruptedException ex){
            logs.add("InterruptedExeption: " + ex.getMessage());
        }catch(Exception ex){
            logs.clear();
            logs.add("Fatal Exception: " + ex.getMessage());
        }
        
        return logs;
    }

    public String restartDockerContainer(String containerName){
        try{
            dockerClient.restartContainerCmd(containerName).exec();
            return "重启成功";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
