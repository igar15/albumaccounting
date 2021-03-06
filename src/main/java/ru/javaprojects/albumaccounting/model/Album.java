package ru.javaprojects.albumaccounting.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "albums", uniqueConstraints = {@UniqueConstraint(columnNames = {"decimal_number", "stamp"}, name = "albums_unique_decimal_number_stamp_idx")})
public class Album extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 12, max = 30)
    @Column(name = "decimal_number", nullable = false)
    private String decimalNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "stamp", nullable = false)
    private Stamp stamp;

    @NotNull
    @Column(name = "receiving_date", nullable = false, columnDefinition = "date default now()")
    private LocalDate receivingDate = LocalDate.now();

    @NotBlank
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id", nullable = false)
    private Employee holder;

    public Album() {
    }

    public Album(Integer id, String decimalNumber, Stamp stamp, LocalDate receivingDate, String location) {
        super(id);
        this.decimalNumber = decimalNumber;
        this.stamp = stamp;
        this.receivingDate = receivingDate;
        this.location = location;
    }

    public String getDecimalNumber() {
        return decimalNumber;
    }

    public void setDecimalNumber(String decimalNumber) {
        this.decimalNumber = decimalNumber;
    }

    public Stamp getStamp() {
        return stamp;
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    public LocalDate getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(LocalDate receivingDate) {
        this.receivingDate = receivingDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Employee getHolder() {
        return holder;
    }

    public void setHolder(Employee holder) {
        this.holder = holder;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", decimalNumber=" + decimalNumber +
                ", stamp=" + stamp +
                ", receivingDate=" + receivingDate +
                ", location=" + location +
                '}';
    }
}