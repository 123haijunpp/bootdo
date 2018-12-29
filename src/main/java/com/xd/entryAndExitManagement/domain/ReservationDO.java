package com.xd.entryAndExitManagement.domain;

/**
 * @author
 * @ClassName:
 * @Description: (这里用一句话描述这个类的作用)
 * @date
 */
public class ReservationDO {
    private String id;
    private String hallname;
    private String reservationsum;
    private String address;
    private String dday;
    private String printedcount;

    public String getPrintedcount() {
        return printedcount;
    }

    public void setPrintedcount(String printedcount) {
        this.printedcount = printedcount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHallname() {
        return hallname;
    }

    public void setHallname(String hallname) {
        this.hallname = hallname;
    }

    public String getReservationsum() {
        return reservationsum;
    }

    public void setReservationsum(String reservationsum) {
        this.reservationsum = reservationsum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDday() {
        return dday;
    }

    public void setDday(String dday) {
        this.dday = dday;
    }
}
