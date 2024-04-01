package edu.fd.se.rjw.carpanel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.fd.se.rjw.carpanel.web.bind.AmclInitialPose;

/**
 * RobotService
 */
@Service
public class RobotService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public String setAmclInitialPose(double x,
            double y,
            double theta) {
        var url = "http://localhost:5000/initial_pose";
        var req = new AmclInitialPose.In(x, y, theta);
        String ret;
        try {
            var res = restTemplate.postForObject(url, req, AmclInitialPose.Out.class);
            ret = res.status() + String.format(" x: %f, y: %f, theta: %f", x, y, theta);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            var rspBodyStr = ex.getResponseBodyAsString();
            try {
                var rsp = objectMapper.readValue(rspBodyStr, AmclInitialPose.Error.class);
                ret = String.format("Error: %s", rsp.message());
            } catch (Exception e) {
                ret = String.format("Error: %s", e.getMessage());
            }
        } catch (Exception e) {
            ret = String.format("Error: %s", e.getMessage());
        }
        return ret;
    }

}