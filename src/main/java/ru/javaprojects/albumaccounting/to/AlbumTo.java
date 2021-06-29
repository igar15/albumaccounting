package ru.javaprojects.albumaccounting.to;

import ru.javaprojects.albumaccounting.model.Stamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class AlbumTo extends BaseTo {
    @NotBlank
    @Size(min = 12, max = 30)
    private String decimalNumber;

    @NotNull
    private Stamp stamp;

    @NotNull
    private LocalDate receivingDate;

    @NotNull
    private Integer holderId;

    public AlbumTo() {
    }

    public AlbumTo(Integer id, String decimalNumber, Stamp stamp, LocalDate receivingDate, Integer holderId) {
        super(id);
        this.decimalNumber = decimalNumber;
        this.stamp = stamp;
        this.receivingDate = receivingDate;
        this.holderId = holderId;
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

    public Integer getHolderId() {
        return holderId;
    }

    public void setHolderId(Integer holderId) {
        this.holderId = holderId;
    }

    @Override
    public String toString() {
        return "AlbumTo{" +
                "id=" + id +
                ", decimalNumber=" + decimalNumber +
                ", stamp=" + stamp.name() +
                ", receivingDate=" + receivingDate +
                ", holderId=" + holderId +
                '}';
    }
}