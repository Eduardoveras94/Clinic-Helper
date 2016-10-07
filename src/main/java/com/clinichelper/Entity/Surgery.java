package com.clinichelper.Entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * Created by eva_c on 9/25/2016.
 */
@Entity
@Table(name="surgeries")
public class Surgery {
    @Id
    private String surgeryId;
    private String surgeryName;
    private String surgeryDescription;
    private Date surgeryDate;
    private Timestamp surgeryTime;
    private String surgeryRoom;
    @ManyToMany
    private Set<Staff> staffs;
    @OneToMany
    private Set<Equipment> equipments;
    @OneToOne
    private Appointment appointment;

    public Surgery(){

    }

    public Surgery(String surgeryName, String surgeryDescription, Date surgeryDate, Timestamp surgeryTime, String surgeryRoom, Set<Staff> staffs, Set<Equipment> equipments, Appointment appointment) {
        this.setSurgeryId("JASC-S-" + UUID.randomUUID().toString().split("-")[0].toUpperCase());
        this.setSurgeryName(surgeryName);
        this.setSurgeryDescription(surgeryDescription);
        this.setSurgeryDate(surgeryDate);
        this.setSurgeryTime(surgeryTime);
        this.setSurgeryRoom(surgeryRoom);
        this.setStaffs(staffs);
        this.setEquipments(equipments);
        this.setAppointment(appointment);
    }

    public String getSurgeryId() {
        return surgeryId;
    }

    public void setSurgeryId(String surgeryId) {
        this.surgeryId = surgeryId;
    }

    public String getSurgeryName() {
        return surgeryName;
    }

    public void setSurgeryName(String surgeryName) {
        this.surgeryName = surgeryName;
    }

    public String getSurgeryDescription() {
        return surgeryDescription;
    }

    public void setSurgeryDescription(String surgeryDescription) {
        this.surgeryDescription = surgeryDescription;
    }

    public Date getSurgeryDate() {
        return surgeryDate;
    }

    public void setSurgeryDate(Date surgeryDate) {
        this.surgeryDate = surgeryDate;
    }

    public Timestamp getSurgeryTime() {
        return surgeryTime;
    }

    public void setSurgeryTime(Timestamp surgeryTime) {
        this.surgeryTime = surgeryTime;
    }

    public String getSurgeryRoom() {
        return surgeryRoom;
    }

    public void setSurgeryRoom(String surgeryRoom) {
        this.surgeryRoom = surgeryRoom;
    }

    public Set<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(Set<Staff> staffs) {
        this.staffs = staffs;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Set<Equipment> equipments) {
        this.equipments = equipments;
    }
}