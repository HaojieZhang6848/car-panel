package edu.fd.se.rjw.carpanel.controller.thymeleaf;

import edu.fd.se.rjw.carpanel.service.DockerService;
import edu.fd.se.rjw.carpanel.service.RobotService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class MainThymeleafController {

    @Autowired
    private DockerService dockerService;

    @Autowired
    private RobotService robotService;

    @Value("${car-panel.model}")
    private String carModel;

    @Autowired
    private String dockerComposeFilePath;

    @Autowired
    @Qualifier("monitoredContainerNames")
    private List<String> monitoredContainerNames;

    @RequestMapping(value = "index")
    public String index(Model model){
        var monitoredContainerStatuses = dockerService.getMonitoredContainerStatuses();

        model.addAttribute("dockerComposeFilePath", dockerComposeFilePath);
        model.addAttribute("monitoredContainerNames", monitoredContainerNames);
        model.addAttribute("monitoredContainerStatuses", monitoredContainerStatuses);
        return "m/index";
    }

    @RequestMapping("startDockerCompose")
    public String startDockerCompose(Model model){
        String msg = dockerService.startDockerCompose();
        if(msg.isBlank()){
            msg = "启动成功";
        }
        model.addAttribute("message", msg);
        return "m/startDockerCompose";
    }
    
    @RequestMapping("stopDockerCompose")
    public String stopDockerCompose(Model model){
        String msg = dockerService.stopDockerCompose();
        if(msg.isBlank()){
            msg = "停止成功";
        }
        model.addAttribute("message", msg);
        return "m/stopDockerCompose";
    }

    @RequestMapping("restartDockerCompose")
    public String restartDockerCompose(Model model){
        String msg = dockerService.restartDockerCompose();
        if(msg.isBlank()){
            msg = "重启成功";
        }
        model.addAttribute("message", msg);
        return "m/restartDockerCompose";
    }
    

    @RequestMapping("containerLogs")
    public String containerLogs(@RequestParam(required = true) String containerName, Model model){
        var logs = dockerService.getContainerLogs(containerName);
        model.addAttribute("containerName", containerName);
        model.addAttribute("logs", logs);
        return "m/containerLogs";
    }

    @RequestMapping("restartDockerContainer")
    public String restartDockerContainer(@RequestParam(required = true) String containerName, Model model){
        String msg = dockerService.restartDockerContainer(containerName);
        if(msg.isBlank()){
            msg = "重启成功";
        }
        model.addAttribute("message", msg);
        model.addAttribute("containerName", containerName);
        return "m/restartDockerContainer";
    }

    @RequestMapping("setAmclInitialPose")
    public String setAmclInitialPose(@RequestParam(required = true) double x,
                                     @RequestParam(required = true) double y,
                                     @RequestParam(required = true) double theta,
                                     Model model){
        String msg = robotService.setAmclInitialPose(x, y, theta);
        if(msg.isBlank()){
            msg = String.format("设置成功，x=%f, y=%f, theta=%f", x, y, theta);
        }
        model.addAttribute("message", msg);
        return "m/setAmclInitialPose";
    }
    
}
