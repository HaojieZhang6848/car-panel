package edu.fd.se.rjw.carpanel.web.bind;

public class CurLoc {
    public record Out(double x, double y, double theta) {
    }
    
    public record Error(String path, String method, String message, Integer timestamp, Integer code) {
    }
}
