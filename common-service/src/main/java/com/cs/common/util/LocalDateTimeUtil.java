package com.cs.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * @Author: CS
 * @Date: 2020/6/2 5:03 下午
 * @Description:
 */
public class LocalDateTimeUtil {

    /**
     * 默认东8区
     */
    private static ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

    private static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);

    private static String Y_M_PATTERN = "yyyyMM";

    /**
     * 初始化，第一周至少四天
     */
    private static WeekFields WFS = WeekFields.of(DayOfWeek.MONDAY, 4);

    /**
     * @param time 需要处理的时间
     * @return 毫秒时间戳（13位）
     */
    public static Long timeToEpochMilli(LocalDateTime time) {
        Assert.notNull(time, "time can not be null!");
        return time.toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli();
    }

    /**
     * @param time 需要处理的时间
     * @return 秒时间戳（10位）
     */
    public static Long timeToEpochSecond(LocalDateTime time) {
        Assert.notNull(time, "time can not be null!");
        return time.toEpochSecond(DEFAULT_ZONE_OFFSET);
    }

    /**
     * 时间格式化
     *
     * @param time
     * @return
     */
    public static String timeFormatPattern(LocalDateTime time) {
        Assert.notNull(time, "time can not be null!");
        return time.format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 时间格式化
     *
     * @param time    需要处理的时间
     * @param pattern 格式花类型
     * @return
     */
    public static String timeFormatWithPattern(LocalDateTime time, String pattern) {
        Assert.notNull(time, "time can not be null!");
        Assert.hasText(pattern, "time format pattern is missing!");
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取xx年xx周的星期x的日期
     *
     * @param year
     * @param num
     * @param dayOfWeek
     * @return
     */
    public static LocalDate getDateByYearAndWeekNumAndDayOfWeek(Integer year, Integer num, DayOfWeek dayOfWeek) {
        //周数小于10在前面补个0
        String numStr = num < 10 ? "0" + num : String.valueOf(num);
        //2019-W01-01获取第一周的周一日期，2019-W02-07获取第二周的周日日期
        String weekDate = String.format("%s-W%s-%s", year, numStr, dayOfWeek.getValue());
        return LocalDate.parse(weekDate, DateTimeFormatter.ISO_WEEK_DATE);
    }

    /**
     * 获取指定时间所在年的周数
     * <p>
     * 配合
     * select DATE_FORMAT(def_date, '%Y%u') weeks
     * from dw_sale_behavior_dashboard;
     * 可以做到数据以周为纬度进行统计
     *
     * @param localDate
     * @return
     */
    public static int weekOfYear(LocalDate localDate) {
        //一年最后一天日期的LocalDate，如果该天获得的周数为1或52，那么该年就只有52周，否则就是53周
        return localDate.get(WFS.weekOfYear());
    }

    /**
     * 数据日期包含的weekOfYear
     *
     * @param localDate 数据日期
     * @return
     */
    public static List<Integer> containWeeksByDate(LocalDate localDate) {
        LocalDate weekStartDate = localDate.with(DayOfWeek.MONDAY);
        LocalDate weekEndDate = localDate.with(DayOfWeek.SUNDAY);

        Integer start = weekOfYearWithYear(weekStartDate);
        Integer end = weekOfYearWithYear(weekEndDate);

        return start.equals(end) ? Arrays.asList(start) : Arrays.asList(start, end);
    }

    /**
     * 一段时间范围内，一周含有两个年周的
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Integer> needUnionWeeksByDateRange(LocalDate start, LocalDate end) {
        // 是否跨年
        int startYear = start.getYear();
        int endYear = end.getYear();
        if (startYear != endYear) {
            LocalDate lastWeekMonday = start.with(TemporalAdjusters.lastDayOfYear()).with(DayOfWeek.MONDAY);
            LocalDate lastWeekSunday = start.with(TemporalAdjusters.lastDayOfYear()).with(DayOfWeek.SUNDAY);
            int lastWeekMondayOfYear = weekOfYear(lastWeekMonday);
            int lastWeekSundayOfYear = weekOfYear(lastWeekSunday);
            if (lastWeekMondayOfYear != lastWeekSundayOfYear) {
                return Lists.newArrayList(Integer.valueOf(endYear + "01"), Integer.valueOf(startYear + String.valueOf(lastWeekMondayOfYear)));
            }
        }
        return Collections.emptyList();
    }

    public static Integer weekOfYearWithYear(LocalDate localDate) {
        int year = localDate.getYear();
        int week = weekOfYear(localDate);
        String weekVal = week < 10 ? "0" + week : String.valueOf(week);
        return Integer.valueOf(year + weekVal);
    }

    /**
     * 根据开始日期和结束日期获得时间区间各个月份中包含的年周数集合
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return
     */
    public static Map<String, Set<Integer>> classifyMonthWeek(LocalDate start, LocalDate end) {
        Long unit = Long.valueOf(end.format(DateTimeFormatter.ofPattern(Y_M_PATTERN))) - Long.valueOf(start.format(DateTimeFormatter.ofPattern(Y_M_PATTERN)));

        start = start.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate tempStart;
        LocalDate tempEnd;
        Map<String, Set<Integer>> map = new LinkedHashMap<>();
        for (int i = 0; i <= unit; i++) {
            tempStart = start.plusMonths(i);
            tempEnd = tempStart.with(TemporalAdjusters.lastDayOfMonth());
            String key = tempStart.format(DateTimeFormatter.ofPattern(Y_M_PATTERN));

            int startWeek = weekOfYear(tempStart);
            int endWeek = weekOfYear(tempEnd);
            Set<Integer> weeks = Sets.newHashSet();
            for (int j = startWeek; j <= endWeek; j++) {
                weeks.add(j);
            }
            map.put(key, weeks);

        }
        return map;
    }

    public static Map<String, Integer> getRecent16WeeksRangeYm() {
        // 当前时间所在周
        String year = String.valueOf(LocalDate.now().getYear());
        int endWeek = weekOfYear(LocalDate.now());
        Integer endWeekYear = Integer.valueOf(year + endWeek);
        Integer startWeekYear;
        // 16周之前所在年周
        int compare = endWeek - 16;
        if (compare < 0) {
            String lastYear = String.valueOf(LocalDate.now().minusYears(1).getYear());
            // 需要跨年
            int weeks = weekOfYear(LocalDate.now().minusYears(1).with(TemporalAdjusters.lastDayOfYear()));
            int end = weeks + compare;
            startWeekYear = Integer.valueOf(lastYear + end);
        } else {
            startWeekYear = Integer.valueOf(year + compare);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("start", startWeekYear);
        map.put("end", endWeekYear);

        return map;
    }
}
