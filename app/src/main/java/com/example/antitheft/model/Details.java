package com.example.antitheft.model;

public class Details
{
    String name,district,pin,phone,carNum,model;
    private boolean expanded;

    public Details(){

    }

    public Details(String name, String district, String pin, String phone, String carNum) {
        this.name = name;
        this.district = district;
        this.pin = pin;
        this.phone = phone;
        this.carNum = carNum;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Details{" +
                "name='" + name + '\'' +
                ", district='" + district + '\'' +
                ", pin='" + pin + '\'' +
                ", carNum='" + carNum + '\'' +
                ", model='" + model + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
