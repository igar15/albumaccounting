package ru.javaprojects.albumaccounting.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javaprojects.albumaccounting.util.AlbumUtil.getAlbumLocation;

class AlbumUtilTest {

    @Test
    void getAlbumLocationStartBorderEquals() {
        assertEquals("С-3/П-1", getAlbumLocation("ВУИА.301551.002"));
    }

    @Test
    void getAlbumLocationEndBorderEquals() {
        assertEquals("С-5/П-2", getAlbumLocation("БА3.550.182"));
    }

    @Test
    void getAlbumLocationSameStartBorderCode() {
        assertEquals("С-3/П-1", getAlbumLocation("ВУИА.401551.002"));
    }

    @Test
    void getAlbumLocationSameEndBorderCode() {
        assertEquals("С-4/П-2", getAlbumLocation("БА1.300.017"));
    }

    @Test
    void getAlbumLocationSameStartEndBorderCode() {
        assertEquals("С-1/П-4", getAlbumLocation("ВУИА.301214.008"));
    }

    @Test
    void getAlbumLocationNotFound() {
        assertEquals("Unknown", getAlbumLocation("БА5.550.182"));
    }
}