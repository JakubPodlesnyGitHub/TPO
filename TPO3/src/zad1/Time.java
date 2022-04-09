/**
 * @author Podleśny Jakub S20540
 */

package zad1;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {
    public static String passed(String from, String to) throws DateTimeParseException {
        try {
            if (isDateTime(from))
                return createDateTimeInfo(LocalDateTime.parse(from), LocalDateTime.parse(to));
            else
                return createDateInfo(LocalDate.parse(from), LocalDate.parse(to));
        } catch (DateTimeParseException de) {
            return "*** " + de.getClass().getName() + ": " + de.getMessage();
        }
    }

    private static boolean isDateTime(String s) {
        if (s.contains("T"))
            return true;
        else
            return false;
    }

    private static String createDateInfo(LocalDate dateFrom, LocalDate dateTo) {
        String info = "Od " + dateFrom.format(DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)", new Locale("pl"))) + " do "
                + dateTo.format(DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)", new Locale("pl"))) + "\n- mija: " + countDays(dateFrom, dateTo) + ", " + countWeeks(dateFrom, dateTo);
        if (ChronoUnit.DAYS.between(dateFrom, dateTo) != 0)
            info += "\n- kalendarzowo: " + createCalendarPeriod(dateFrom, dateTo);
        return info;
    }

    private static String createDateTimeInfo(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
        String info = "Od " + dateTimeFrom.format(DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE) 'godz.' HH:mm", new Locale("pl")))
                + " do " + dateTimeTo.format(DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE) 'godz.' HH:mm", new Locale("pl")))
                + "\n- mija: " + countDays(dateTimeFrom.toLocalDate(), dateTimeTo.toLocalDate()) + ", " + countWeeks(dateTimeFrom.toLocalDate(), dateTimeTo.toLocalDate())
                + "\n- " + countHoursMinutes(dateTimeFrom, dateTimeTo);
        if (ChronoUnit.DAYS.between(dateTimeFrom.toLocalDate(), dateTimeTo.toLocalDate()) != 0)
            info += "\n- kalendarzowo: " + createCalendarPeriod(dateTimeFrom.toLocalDate(), dateTimeTo.toLocalDate());
        return info;
    }

    private static String countWeeks(LocalDate localDateFrom, LocalDate localDateTo) {
        double period = ChronoUnit.DAYS.between(localDateFrom, localDateTo) / 7.0;
        return "tygodni " + String.format(new Locale("xx"), "%.2f", period);
    }

    private static String countDays(LocalDate localDateFrom, LocalDate localDateTimeTo) {
        int days = (int) ChronoUnit.DAYS.between(localDateFrom, localDateTimeTo);
        if (days > 1)
            return days + " dni";
        else
            return days + " dzień";
    }

    private static String countHoursMinutes(LocalDateTime localDateTimeFrom, LocalDateTime localDateTimeTo) {
        ZonedDateTime zonedDateTimeFrom = ZonedDateTime.of(localDateTimeFrom, ZoneId.of("Europe/Warsaw"));
        ZonedDateTime zonedDateTimeTo = ZonedDateTime.of(localDateTimeTo, ZoneId.of("Europe/Warsaw"));
        int hours = (int) ChronoUnit.HOURS.between(zonedDateTimeFrom, zonedDateTimeTo);
        return "godzin: " + hours + ", " + countMinutes(zonedDateTimeFrom, zonedDateTimeTo);
    }

    private static String countMinutes(ZonedDateTime zonedDateTimeFrom, ZonedDateTime zonedDateTimeTo) {
        int minutes = (int) ChronoUnit.MINUTES.between(zonedDateTimeFrom, zonedDateTimeTo);
        return "minut: " + minutes;
    }

    public static String createCalendarPeriod(LocalDate localDateFrom, LocalDate localDateTo) {
        String calendarString = "";
        int years = Period.between(localDateFrom, localDateTo).getYears();
        int months = Period.between(localDateFrom, localDateTo).getMonths();
        int days = Period.between(localDateFrom, localDateTo).getDays();
        calendarString += addYears(years) + addMonths(months) + addDays(days);
        return calendarString.substring(0,calendarString.length()-2);
    }

    private static String addYears(int years) {
        if (years != 0) {
            if (years == 1) {
                return years + " rok, ";
            } else if (years < 5) {
                return years + " lata, ";
            } else {
                return years + " lat, ";
            }
        }
        return "";
    }

    private static String addMonths(int months) {
        if (months != 0) {
            if (months == 1) {
                return months + " miesiąc, ";
            } else if (months < 5) {
                return months + " miesiące, ";
            } else {
                return months + " miesięcy, ";
            }
        }
        return "";
    }

    private static String addDays(int days) {
        if (days != 0) {
            if (days != 1)
                return days + " dni, ";
            else
                return days + " dzień, ";

        }
        return "";
    }

}
