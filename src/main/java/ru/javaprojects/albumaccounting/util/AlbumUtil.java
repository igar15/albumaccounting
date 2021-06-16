package ru.javaprojects.albumaccounting.util;

import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.to.AlbumTo;
import ru.javaprojects.albumaccounting.util.exception.PropertiesException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class AlbumUtil {
    private static final Properties albumsLocation = new Properties();

    static {
        try (Reader r = new InputStreamReader(Objects.requireNonNull(AlbumUtil.class.getClassLoader().getResourceAsStream("albums_location.properties")))) {
            albumsLocation.load(r);
        } catch (IOException e) {
            throw new PropertiesException("Cannot load albums_location.properties", e);
        }
    }

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

    static String getAlbumLocation(String decimalNumber) {
        String[] decNumParts = splitDeNumToParts(decimalNumber);
        String orgCode = decNumParts[0];
        int classCode = Integer.parseInt(decNumParts[1]);
        int regNum = getRegNum(decNumParts);

        Optional<Object> interval = albumsLocation.keySet().stream()
                .filter(decNumInterval -> {
                    String[] intervalBorders = ((String) decNumInterval).split("\\|\\|");

                    String startBorderDecNum = intervalBorders[0];
                    String[] startBorderDecNumParts = splitDeNumToParts(startBorderDecNum);
                    String startBorderDecNumOrgCode = startBorderDecNumParts[0];
                    int startBorderDecNumClassCode = Integer.parseInt(startBorderDecNumParts[1]);
                    int startBorderDecNumRegNum = getRegNum(startBorderDecNumParts);

                    String endBorderDecNum = intervalBorders[1];
                    String[] endBorderDecNumParts = splitDeNumToParts(endBorderDecNum);
                    String endBorderDecNumOrgCode = endBorderDecNumParts[0];
                    int endBorderDecNumClassCode = Integer.parseInt(endBorderDecNumParts[1]);
                    int endBorderDecNumRegNum = getRegNum(endBorderDecNumParts);

                    if (decimalNumber.equalsIgnoreCase(startBorderDecNum) || decimalNumber.equalsIgnoreCase(endBorderDecNum)) {
                        return true;
                    } else if (orgCode.equalsIgnoreCase(startBorderDecNumOrgCode)) {
                        if (classCode < startBorderDecNumClassCode) {
                            return false;
                        } else if (classCode == startBorderDecNumClassCode && regNum < startBorderDecNumRegNum) {
                            return false;
                        }
                        if (!orgCode.equalsIgnoreCase(endBorderDecNumOrgCode)) {
                            return true;
                        } else if (classCode > endBorderDecNumClassCode) {
                            return false;
                        } else if (classCode == endBorderDecNumClassCode && regNum > endBorderDecNumRegNum) {
                            return false;
                        }
                        return true;
                    } else if (!orgCode.equalsIgnoreCase(endBorderDecNumOrgCode)) {
                        return false;
                    } else if (classCode < endBorderDecNumClassCode) {
                        return true;
                    } else if (classCode == endBorderDecNumClassCode && regNum < endBorderDecNumRegNum) {
                        return true;
                    }
                    return false;
                })
                .findFirst();
        return (String) albumsLocation.getOrDefault(interval.orElseGet(() -> "not found"), "Location unknown");
    }

    private static String[] splitDeNumToParts(String decimalNumber) {
        return decimalNumber.split("\\.");
    }

    private static int getRegNum(String[] decNumParts) {
        return Integer.parseInt(decNumParts[2].substring(0, 3));
    }
}