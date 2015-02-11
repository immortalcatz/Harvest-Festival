package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.config.Calendar;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CalendarHelper {
    @SideOnly(Side.CLIENT)
    public static int getClientDay() {
        return handler.getClient().getCalendar().getDate().getDay();
    }
    
    @SideOnly(Side.CLIENT)
    public static Season getClientSeason() {
        return handler.getClient().getCalendar().getDate().getSeason();
    }
    
    @SideOnly(Side.CLIENT)
    public static int getClientYear() {
        return handler.getClient().getCalendar().getDate().getYear();
    }
    
    @SideOnly(Side.CLIENT)
    public static CalendarDate getClientDate() {
        return handler.getClient().getCalendar().getDate();
    }
    
    public static CalendarDate getServerDate() {
        return handler.getServer().getCalendar().getDate();
    }
    
    public static void setDate(int day, Season season, int year) {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            handler.getClient().getCalendar().getDate().setDay(day).setSeason(season).setYear(year);
        } else {
            handler.getServer().getCalendar().setDate(day, season, year);
        }
    }
    
    public static Season getSeason() {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return handler.getClient().getCalendar().getDate().getSeason();
        } else return handler.getServer().getCalendar().getDate().getSeason();
    }
    
    public static int getTotalDays(CalendarDate date) {
        int current_days = date.getDay();
        int season_days = Calendar.DAYS_PER_SEASON * date.getSeason().ordinal();
        int year_days = (date.getYear() - 1) * (Calendar.DAYS_PER_SEASON * 4);
        return current_days + season_days + year_days;
    }

    public static int getYearsPassed(CalendarDate birthday, CalendarDate date) {
        int current_total_days = getTotalDays(date);
        int birthday_total_days = getTotalDays(birthday);
        int one_year = Calendar.DAYS_PER_SEASON * 4;
        
        int years_passed = current_total_days / one_year; 
        int birthday_years = birthday_total_days / one_year;
                
        return years_passed - birthday_years;
    }
}
