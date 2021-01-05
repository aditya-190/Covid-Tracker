package com.aditya.covid19.updatesSegment;

public class state {
    private String state;
    private String active;
    private String recover;
    private String death;
    private String confirm;
    private String recover_inc;
    private String death_inc;
    private String confirm_inc;

    public state() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getRecover_inc() {
        return recover_inc;
    }

    public void setRecover_inc(String recover_inc) {
        this.recover_inc = recover_inc;
    }

    public String getDeath_inc() {
        return death_inc;
    }

    public void setDeath_inc(String death_inc) {
        this.death_inc = death_inc;
    }

    public String getConfirm_inc() {
        return confirm_inc;
    }

    public void setConfirm_inc(String confirm_inc) {
        this.confirm_inc = confirm_inc;
    }
}
