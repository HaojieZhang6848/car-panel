package edu.fd.se.rjw.carpanel.web.bind;

public class Nav {
    public record In(double x, double y, double theta) {
    }

    public record Out(String status, String message) {
    }

    public record Error(String path, String method, String message, Integer timestamp, Integer code) {
    }
}
