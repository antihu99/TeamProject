package com.sec.bestreviewer.store;

import com.sec.bestreviewer.field.*;

import java.util.Objects;

public class Employee {
    private EmployeeNumber employeeNumber;
    private Name name;
    private CareerLevel careerLevel;
    private PhoneNumber phoneNumber;
    private Birthday birthday;
    private Certi certi;

    public Employee(String employeeNumber,
                    String name,
                    String careerLevel,
                    String phoneNumber,
                    String birthday,
                    String certi) {
        this.employeeNumber = new EmployeeNumber(employeeNumber);
        this.name = new Name(name);
        this.careerLevel = new CareerLevel(careerLevel);
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.birthday = new Birthday(birthday);
        this.certi = new Certi(certi);
    }

    public EmployeeNumber getEmployeeNumber() {
        return employeeNumber;
    }

    public Name getName() {
        return name;
    }

    public CareerLevel getCareerLevel() {
        return careerLevel;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public Certi getCerti() {
        return certi;
    }

    public void setEmployeeNumber(EmployeeNumber employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setCareerLevel(CareerLevel careerLevel) {
        this.careerLevel = careerLevel;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthday(Birthday birthday) {
        this.birthday = birthday;
    }

    public void setCerti(Certi certi) {
        this.certi = certi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeNumber, employee.employeeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber);
    }
}
