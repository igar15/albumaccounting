package ru.javaprojects.albumaccounting.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javaprojects.albumaccounting.util.AlbumUtil.getAlbumLocation;

class AlbumUtilTest {

    @Test
    void getAlbumLocationStartBorderEquals() {
        assertEquals("С-3/П-1", getAlbumLocation("АБВГ.301551.002"));
    }

    @Test
    void getAlbumLocationEndBorderEquals() {
        assertEquals("С-5/П-2", getAlbumLocation("ТА3.550.182"));
    }

    @Test
    void getAlbumLocationSameStartBorderCode() {
        assertEquals("С-3/П-1", getAlbumLocation("АБВГ.401551.002"));
    }

    @Test
    void getAlbumLocationSameEndBorderCode() {
        assertEquals("С-4/П-2", getAlbumLocation("ТА1.300.017"));
    }

    @Test
    void getAlbumLocationSameStartEndBorderCode() {
        assertEquals("С-1/П-4", getAlbumLocation("АБВГ.301214.008"));
    }

    @Test
    void getAlbumLocationNotFound() {
        assertEquals("Unknown", getAlbumLocation("ТА5.550.182"));
    }
}