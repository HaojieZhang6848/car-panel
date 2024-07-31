package edu.fd.se.rjw.carpanel.web.bind;

public class TestNav {
    public record In() {
    }

    public record Out(String status, String message) {
    }

    public record Error(String path, String method, String message, Integer timestamp, Integer code) {
    }
}