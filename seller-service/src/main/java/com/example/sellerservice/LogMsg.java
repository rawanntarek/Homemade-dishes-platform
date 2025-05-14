package com.example.sellerservice;

public class LogMsg {
    private String severity;
    private String serviceName;
    private String message;
    public LogMsg(String severity, String serviceName, String message) {
        this.severity = severity;
        this.message = message;
        this.serviceName = serviceName;
    }
    public LogMsg()
    {

    }
    public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
