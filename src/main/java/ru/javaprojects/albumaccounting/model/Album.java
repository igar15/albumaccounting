package ru.javaprojects.albumaccounting.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "albums", uniqueConstraints = {@UniqueConstraint(columnNames = {"decimal_number", "stamp"}, name = "albums_unique_decimal_number_stamp_idx")})
public class Album extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 10, max = 30)
    @Column(name = "decimal_number", nullable = false)
    private String decimalNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "stamp", nullable = false)
    private Stamp stamp;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id", nullable = false)
    private Employee holder;

    public Album() {
    }

    public Album(Integer id, String decimalNumber, Stamp stamp) {
        super(id);
        this.decimalNumber = decimalNumber;
        this.stamp = stamp;
    }

    public Album(Integer id, String decimalNumber, Stamp stamp, Employee holder) {
        super(id);
        this.decimalNumber = decimalNumber;
        this.stamp = stamp;
        this.holder = holder;
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
                '}';
    }
}