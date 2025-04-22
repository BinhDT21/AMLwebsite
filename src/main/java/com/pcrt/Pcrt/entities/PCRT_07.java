package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pcrt_07")
public class PCRT_07 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "gdv_id")
    private User gdv;

    @ManyToOne
    @JoinColumn(name = "ksv_id")
    private User ksv;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "pcrt_07", cascade = CascadeType.REMOVE)
    private List<PCRT_07_item> itemList;

    @Column(name = "file_path")
    private String filePath;



    public PCRT_07() {
    }

    public PCRT_07(int id, LocalDate createdDate, User gdv, User ksv, User manager, List<PCRT_07_item> itemList) {
        this.id = id;
        this.createdDate = createdDate;
        this.gdv = gdv;
        this.ksv = ksv;
        this.manager = manager;
        this.itemList = itemList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getGdv() {
        return gdv;
    }

    public void setGdv(User gdv) {
        this.gdv = gdv;
    }

    public User getKsv() {
        return ksv;
    }

    public void setKsv(User ksv) {
        this.ksv = ksv;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<PCRT_07_item> getItemList() {
        return itemList;
    }

    public void setItemList(List<PCRT_07_item> itemList) {
        this.itemList = itemList;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
