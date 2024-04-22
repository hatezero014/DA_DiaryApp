package com.example.doan_diaryapp.Controllers;

public class DataHolderController {
    private static DataHolderController instance;
    private String chuoiCanTruyen;

    private DataHolderController() {}

    public static DataHolderController Instance() {
        if (instance == null) {
            instance = new DataHolderController();
        }
        return instance;
    }

    public String getChuoiCanTruyen() {
        return chuoiCanTruyen;
    }

    public void setChuoiCanTruyen(String chuoiCanTruyen) {
        this.chuoiCanTruyen = chuoiCanTruyen;
    }

}
