package com.example.demoscheduler.setGet;

public class SetGetJobDetails {
    private int id;
    private String subject,desc,entDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEntDateTime() {
        return entDateTime;
    }

    public void setEntDateTime(String entDateTime) {
        this.entDateTime = entDateTime;
    }
}
