package ru.javaprojects.albumaccounting;

import org.springframework.data.domain.*;
import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.to.AlbumTo;

import java.util.List;

import static ru.javaprojects.albumaccounting.EmployeeTestData.EMPLOYEE_1_ID;
import static ru.javaprojects.albumaccounting.model.AbstractBaseEntity.START_SEQ;
import static ru.javaprojects.albumaccounting.model.Stamp.II_STAMP;
import static ru.javaprojects.albumaccounting.model.Stamp.I_STAMP;

public class AlbumTestData {
    public static final TestMatcher<Album> ALBUM_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Album.class, "holder");

    public static final int ALBUM_1_ID = START_SEQ + 11;
    public static final int ALBUM_2_ID = START_SEQ + 12;
    public static final int ALBUM_3_ID = START_SEQ + 13;
    public static final int ALBUM_4_ID = START_SEQ + 14;
    public static final int NOT_FOUND = 10;

    public static final Album album1 = new Album(ALBUM_1_ID, "АБВГ.444444.005", I_STAMP, "С-3/П-1");
    public static final Album album2 = new Album(ALBUM_2_ID, "АБВГ.444444.006", I_STAMP, "С-3/П-1");
    public static final Album album3 = new Album(ALBUM_3_ID, "АБВГ.444444.007", I_STAMP, "С-3/П-1");
    public static final Album album4 = new Album(ALBUM_4_ID, "АБВГ.444444.008", I_STAMP, "С-3/П-1");

    public static final Pageable PAGEABLE = PageRequest.of(0, 20);
    public static final Page<Album> PAGE = new PageImpl<>(List.of(album1, album2, album3, album4), PAGEABLE, 4);
    public static final Page<Album> PAGE_BY_KEYWORD_ONE_TOTAL = new PageImpl<>(List.of(album1), PAGEABLE, 1);
    public static final Page<Album> PAGE_BY_KEYWORD_FOUR_TOTAL = new PageImpl<>(List.of(album1, album2, album3, album4), PAGEABLE, 4);

    public static Album getNew() {
        return new Album(null, "АБВГ.444444.009", I_STAMP, "С-3/П-1");
    }

    public static AlbumTo getNewTo() {
        return new AlbumTo(null, "АБВГ.444444.009", I_STAMP, EMPLOYEE_1_ID);
    }

    public static Album getUpdated() {
        return new Album(ALBUM_1_ID, "АБВГ.301257.009", II_STAMP, "С-2/П-3");
    }

    public static AlbumTo getUpdatedTo() {
        return new AlbumTo(ALBUM_1_ID, "АБВГ.301257.009", II_STAMP, EMPLOYEE_1_ID);
    }
}