package com.bocom.domain.pac;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PersonnelInformation {
    private String name;

    private String sex;

    private String id_card;

    private String job_title;

    private Date graduation_time;

    private Date entry_time;

    private String nation;

    private Date birth_date;

    private String political_status;

    private String highest_education;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getGraduation_time() {
        return graduation_time;
    }

    public void setGraduation_time(Date graduation_time) {
        this.graduation_time = graduation_time;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(Date entry_time) {
        this.entry_time = entry_time;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getPolitical_status() {
        return political_status;
    }

    public void setPolitical_status(String political_status) {
        this.political_status = political_status;
    }

    public String getHighest_education() {
        return highest_education;
    }

    public void setHighest_education(String highest_education) {
        this.highest_education = highest_education;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}