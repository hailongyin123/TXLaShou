package com.zwy.base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZwyConstellationUtil {

    enum Constellation {
        Capricorn(1, "摩羯座"), Aquarius(2, "水瓶座"), Pisces(3, "双鱼座"), Aries(4,
                "白羊座"), Taurus(5, "金牛座"), Gemini(6, "双子座"), Cancer(7, "巨蟹座"), Leo(
                8, "狮子座"), Virgo(9, "处女座"), Libra(10, "天秤座"), Scorpio(11, "天蝎座"), Sagittarius(
                12, "射手座");

        private Constellation(int code, String chineseName) {
            this.code = code;
            this.chineseName = chineseName;
        }

        private int code;
        private String chineseName;

        int getCode() {
            return this.code;
        }

        String getChineseName() {
            return this.chineseName;
        }
    }

    static final Constellation[] constellationArr = {Constellation.Aquarius,
            Constellation.Pisces, Constellation.Aries, Constellation.Taurus,
            Constellation.Gemini, Constellation.Cancer, Constellation.Leo,
            Constellation.Virgo, Constellation.Libra, Constellation.Scorpio,
            Constellation.Sagittarius, Constellation.Capricorn};

    static final int[] constellationEdgeDay = {21, 20, 21, 21, 22, 22, 23, 24,
            24, 24, 23, 22};

    /**
     * @param birthday yyyy-MM-dd
     * @return
     */
    public static String calculateConstellation(String birthday) {
        if (birthday == null || birthday.trim().length() == 0)
            throw new IllegalArgumentException("the birthday can not be null");
        String[] birthdayElements = birthday.split("-");
        if (birthdayElements.length != 3)
            throw new IllegalArgumentException(
                    "the birthday form is not invalid");
        int month = Integer.parseInt(birthdayElements[1]);
        int day = Integer.parseInt(birthdayElements[2]);
        if (month == 0 || day == 0 || month > 12)
            return "";
        month = day < constellationEdgeDay[month - 1] ? month - 1 : month;
        return month > 0 ? constellationArr[month - 1].getChineseName()
                : constellationArr[11].getChineseName();
    }

    /**
     * @param  date
     * @return
     */
    public static String calculateConstellation(Date date) {
        if (date == null)
            throw new IllegalArgumentException("the birthday can not be null");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = dateFormat.format(date);
        String[] birthdayElements = birthday.split("-");
        if (birthdayElements.length != 3)
            throw new IllegalArgumentException(
                    "the birthday form is not invalid");
        int month = Integer.parseInt(birthdayElements[1]);
        int day = Integer.parseInt(birthdayElements[2]);
        if (month == 0 || day == 0 || month > 12)
            return "";
        month = day < constellationEdgeDay[month - 1] ? month - 1 : month;
        return month > 0 ? constellationArr[month - 1].getChineseName()
                : constellationArr[11].getChineseName();
    }
}