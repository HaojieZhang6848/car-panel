package edu.fd.se.rjw.carpanel.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.fd.se.rjw.carpanel.web.bind.AmclInitialPose;
import edu.fd.se.rjw.carpanel.web.bind.CancelNav;
import edu.fd.se.rjw.carpanel.web.bind.CurLoc;
import edu.fd.se.rjw.carpanel.web.bind.Nav;
import edu.fd.se.rjw.carpanel.web.bind.TestNav;

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

    public String nav(double x, double y, double theta){
        var url = "http://localhost:5000/nav";
        var req = new Nav.In(x, y, theta);
        String ret;
        try {
            var res = restTemplate.postForObject(url, req, Nav.Out.class);
            ret = res.status() + String.format(" x: %f, y: %f, theta: %f", x, y, theta);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            var rspBodyStr = ex.getResponseBodyAsString();
            try {
                var rsp = objectMapper.readValue(rspBodyStr, Nav.Error.class);
                ret = String.format("Error: %s", rsp.message());
            } catch (Exception e) {
                ret = String.format("Error: %s", e.getMessage());
            }
        } catch (Exception e) {
            ret = String.format("Error: %s", e.getMessage());
        }
        return ret;
    }

    public Pair<CurLoc.Out, CurLoc.Error> getCurLoc(){
        var url = "http://localhost:5001/loc";
        Pair<CurLoc.Out, CurLoc.Error> ret;
        try {
            var res = restTemplate.getForObject(url, CurLoc.Out.class);
            ret = Pair.of(res, null);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            var rspBodyStr = ex.getResponseBodyAsString();
            try {
                var rsp = objectMapper.readValue(rspBodyStr, CurLoc.Error.class);
                ret = Pair.of(null, rsp);
            } catch (Exception e) {
                ret = Pair.of(null, new CurLoc.Error("getCurLoc", "GET", e.getMessage(), 0, 500));
            }
        } catch (Exception e) {
            ret = Pair.of(null, new CurLoc.Error("getCurLoc", "GET", e.getMessage(), 0, 500));
        }
        return ret;
    }

    public Pair<CancelNav.Out, CancelNav.Error> cancelNav() {
        var url = "http://localhost:5000/cancel_nav";
        Pair<CancelNav.Out, CancelNav.Error> ret;

        try {
            var res = restTemplate.postForObject(url, null, CancelNav.Out.class);
            ret = Pair.of(res, null);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            var rspBodyStr = ex.getResponseBodyAsString();
            try {
                var rsp = objectMapper.readValue(rspBodyStr, CancelNav.Error.class);
                ret = Pair.of(null, rsp);
            } catch (Exception e) {
                ret = Pair.of(null, new CancelNav.Error("cancelNav", "GET", e.getMessage(), 0, 500));
            }
        } catch (Exception e) {
            ret = Pair.of(null, new CancelNav.Error("cancelNav", "GET", e.getMessage(), 0, 500));
        }

        return ret;
    }

    public Pair<TestNav.Out, TestNav.Error> testNav() {
        var url = "http://localhost:5000/nav_test";
        Pair<TestNav.Out, TestNav.Error> ret;

        try {
            var res = restTemplate.postForObject(url, null, TestNav.Out.class);
            ret = Pair.of(res, null);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            var rspBodyStr = ex.getResponseBodyAsString();
            try {
                var rsp = objectMapper.readValue(rspBodyStr, TestNav.Error.class);
                ret = Pair.of(null, rsp);
            } catch (Exception e) {
                ret = Pair.of(null, new TestNav.Error("testNav", "GET", e.getMessage(), 0, 500));
            }
        } catch (Exception e) {
            ret = Pair.of(null, new TestNav.Error("testNav", "GET", e.getMessage(), 0, 500));
        }

        return ret;
    }
}