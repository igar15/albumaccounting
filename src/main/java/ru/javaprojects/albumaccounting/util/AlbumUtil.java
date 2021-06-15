package ru.javaprojects.albumaccounting.util;

import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.to.AlbumTo;

public class AlbumUtil {

    private AlbumUtil() {
    }

    public static Album createFromTo(AlbumTo albumTo) {
        return new Album(albumTo.getId(), albumTo.getDecimalNumber(), albumTo.getStamp());
    }

    public static Album updateFromTo(Album album, AlbumTo albumTo) {
        album.setDecimalNumber(albumTo.getDecimalNumber());
        album.setStamp(albumTo.getStamp());
        return album;
    }
}