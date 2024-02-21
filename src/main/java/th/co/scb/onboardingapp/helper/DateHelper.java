package th.co.scb.onboardingapp.helper;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public interface DateHelper {

    static String toISO8601(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return df.format(date);
    }

    static String getDateFormat(String format) {
        Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(tz);
        return df.format(date);
    }

    static String formatStrDate(String srcStrDate, String srcFormat, String desFormat) {
        DateFormat srcDf = new SimpleDateFormat(srcFormat);
        Date srcDate = null;
        try {
            srcDate = srcDf.parse(srcStrDate);
        } catch (ParseException e) {
            return "";
        }
        DateFormat desDf = new SimpleDateFormat(desFormat);
        return desDf.format(srcDate);
    }

    static String convertBirthDate(String sourceDate) {
        String dobYear = String.valueOf(Integer.parseInt(sourceDate.substring(0, 4)) + 543);
        dobYear = (dobYear.length() > 4) ? "9999" : dobYear;
        String dobMonth = sourceDate.substring(5, 7);
        String dobDay = sourceDate.substring(8, 10);
        return dobYear + dobMonth + dobDay;
    }

    static String getDateFromLocDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return dateTime.format(formatter);
        } else {
            return "";
        }
    }

    static String getThaiDate(LocalDateTime dateTime) {
        String date = getDateFromLocDateTime(dateTime);
        String dobYear = String.valueOf(Integer.parseInt(date.substring(0, 4)) + 543);
        dobYear = (dobYear.length() > 4) ? "9999" : dobYear;
        String dobMonth = date.substring(5, 7);
        String dobDay = date.substring(8, 10);
        return dobDay + "/" + dobMonth + "/" + dobYear;
    }

    static String convertThaiYear(String sourceDate) {
        String year = sourceDate.substring(0, 4);
        String month = sourceDate.substring(4, 6);
        String day = sourceDate.substring(6, 8);
        String thaiYear = String.valueOf(Integer.valueOf(year) + 543);
        thaiYear = (thaiYear.length() > 4) ? "9999" : thaiYear;
        return thaiYear + month + day;
    }

    static String convertDateYYYYMMDDToDDMMYYYYBY(String sourceDate) {
        if (StringUtils.isEmpty(sourceDate))
            return "";

        if (sourceDate.length() != 10)
            return "";

        String sourceYear = String.valueOf(Integer.valueOf(sourceDate.substring(0, 4)) + 543);
        sourceYear = (sourceYear.length() > 4) ? "9999" : sourceYear;
        String sourceMonth = sourceDate.substring(5, 7);
        String sourceDay = sourceDate.substring(8, 10);

        return sourceDay + "/" + sourceMonth + "/" + sourceYear;
    }

    static String convertThaiDateYYYYMMDDToDDMMYYYYBY(String sourceDate) {
        if (StringUtils.isEmpty(sourceDate))
            return "";

        if (sourceDate.length() != 10)
            return "";

        String sourceYear = String.valueOf(Integer.valueOf(sourceDate.substring(0, 4)) + 543);
        String sourceMonth = sourceDate.substring(5, 7);
        String sourceDay = sourceDate.substring(8, 10);

        return sourceDay + "/" + sourceMonth + "/" + sourceYear;
    }

    static String convertEngDateYYYYMMDDToDDMMYYYYBY(String sourceDate) {
        if (StringUtils.isEmpty(sourceDate))
            return "";

        if (sourceDate.length() != 10)
            return "";

        String sourceYear = String.valueOf(Integer.valueOf(sourceDate.substring(0, 4)));
        String sourceMonth = sourceDate.substring(5, 7);
        String sourceDay = sourceDate.substring(8, 10);

        return sourceDay + "/" + sourceMonth + "/" + sourceYear;
    }

    /**
     * Converts to Thai date, eg. 1920-03-15 to 15-03-2463 if supplied a separator - ,else 15032463
     *
     * @param sourceDate
     * @param separator
     * @return
     */
    static String convertToThaiYear(String sourceDate, String separator) {
        String month = "";
        String day = "";
        String year = String.valueOf(Integer.valueOf(sourceDate.substring(0, 4)) + 543);
        year = (year.length() > 4) ? "9999" : year;
        if (sourceDate.length() == 8) {
            month = sourceDate.substring(4, 6);
            day = sourceDate.substring(6, 8);
        } else if (sourceDate.length() == 10) {
            month = sourceDate.substring(5, 7);
            day = sourceDate.substring(8, 10);
        }
        return day + separator + month + separator + year;
    }

    static String setDateWithSeparator(String sourceDate, String separator) {
        String month = "";
        String day = "";
        String year = String.valueOf(Integer.valueOf(sourceDate.substring(0, 4)));
        year = (year.length() > 4) ? "9999" : year;
        if (sourceDate.length() == 8) {
            month = sourceDate.substring(4, 6);
            day = sourceDate.substring(6, 8);
        } else if (sourceDate.length() == 10) {
            month = sourceDate.substring(5, 7);
            day = sourceDate.substring(8, 10);
        }
        return day + separator + month + separator + year;
    }

    static String getDateTime(String format, LocalDateTime dateTime) {
        if (dateTime != null) {
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
                return dateTime.format(dtf);
            } catch (Exception ex) {
                return dateTime.toString();
            }
        } else {
            return "";
        }
    }

    static String dateThai(String strDate) throws ParseException {
        String[] months = {
                "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน",
                "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม",
                "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = df.parse(strDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        return String.format("%s %s %s", day, months[month], year + 543);
    }

    static String getTime(String strDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return new SimpleDateFormat("HH:mm:ss").format(df.parse(strDate));
    }
}
