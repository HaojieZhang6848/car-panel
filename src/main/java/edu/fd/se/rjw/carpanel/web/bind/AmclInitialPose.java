package edu.fd.se.rjw.carpanel.web.bind;

public class AmclInitialPose {

    public record In(double x, double y, double theta) {
    }

    public record Out(String status) {
    }

    public record Error(String path, String method, String message, Integer timestamp, Integer code) {
    }

}
