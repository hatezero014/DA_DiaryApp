package com.example.doan_diaryapp.ui.image;

import android.content.Context;

import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.EntryEmotion;
import com.example.doan_diaryapp.Models.Partner;
import com.example.doan_diaryapp.Models.Weather;
import com.example.doan_diaryapp.Service.ActivityService;
import com.example.doan_diaryapp.Service.EmotionService;
import com.example.doan_diaryapp.Service.EntryActivityService;
import com.example.doan_diaryapp.Service.EntryEmotionService;
import com.example.doan_diaryapp.Service.EntryPartnerService;
import com.example.doan_diaryapp.Service.EntryWeatherService;
import com.example.doan_diaryapp.Service.PartnerService;
import com.example.doan_diaryapp.Service.WeatherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Image {
    public Map<String,Integer> getMood(int year, int month, Context context){
        Map<String,Integer> emotionCount = new HashMap<>();

        EmotionService emotionService = new EmotionService(context);
        List<Emotion> emotions = emotionService.GetAll(Emotion.class);

        for(Emotion emotion: emotions){
            if(emotion.getIcon().equals("add"))
                continue;
            emotionCount.put(emotion.getIcon(),0);
        }

        EntryEmotionService entryEmotionService = new EntryEmotionService(context);
        if(month == 0){
            List<Emotion> entryEmotions = entryEmotionService.getEmotionIdByYear(year);
            for(Emotion emotion : entryEmotions){
                String icon = emotion.getIcon();
                if(emotionCount.containsKey(icon)){
                    emotionCount.put(icon,emotionCount.get(icon)+1);
                }
            }
        }
        else {
            List<Emotion> entryEmotions = entryEmotionService.getEmotionIdByMonthYear(month,year);
            for(Emotion emotion : entryEmotions){
                String icon = emotion.getIcon();
                if(emotionCount.containsKey(icon)){
                    emotionCount.put(icon,emotionCount.get(icon)+1);
                }
            }
        }

        return emotionCount;
    }

    public Map<String,Integer> getActivity(int year, int month, Context context){
        Map<String,Integer> activityCount = new HashMap<>();

        ActivityService activityService = new ActivityService(context);
        List<Activity> activities = activityService.GetAll(Activity.class);

        for(Activity activity : activities){
            if(activity.getIcon().equals("add"))
                continue;
            activityCount.put(activity.getIcon(),0);
        }

        EntryActivityService entryActivityService = new EntryActivityService(context);
        if(month == 0){
            List<Activity> entryActivities = entryActivityService.getActivityIdByYear(year);
            for(Activity activity: entryActivities){
                String icon = activity.getIcon();
                if(activityCount.containsKey(icon)){
                    activityCount.put(icon,activityCount.get(icon)+1);
                }
            }
        }
        else {
            List<Activity> entryActivities = entryActivityService.getActivityIdByMonthYear(month, year);
            for(Activity activity: entryActivities){
                String icon = activity.getIcon();
                if(activityCount.containsKey(icon)){
                    activityCount.put(icon,activityCount.get(icon)+1);
                }
            }
        }

        return activityCount;
    }

    public Map<String,Integer> getPartner(int year, int month, Context context){
        Map<String,Integer> partnerCount = new HashMap<>();

        PartnerService partnerService = new PartnerService(context);
        List<Partner>partners = partnerService.GetAll(Partner.class);

        for(Partner partner:partners){
            if(partner.getIcon().equals("add"))
                continue;
            partnerCount.put(partner.getIcon(),0);
        }

        EntryPartnerService entryPartnerService = new EntryPartnerService(context);
        if(month == 0){
            List<Partner> entryPartners = entryPartnerService.getPartnerIdByYear(year);
            for(Partner partner:entryPartners){
                String icon = partner.getIcon();
                if(partnerCount.containsKey(icon)){
                    partnerCount.put(icon,partnerCount.get(icon)+1);
                }
            }
        }
        else {
            List<Partner> entryPartners = entryPartnerService.getPartnerIdByMonthYear(month, year);
            for(Partner partner:entryPartners){
                String icon = partner.getIcon();
                if(partnerCount.containsKey(icon)){
                    partnerCount.put(icon,partnerCount.get(icon)+1);
                }
            }
        }

        return partnerCount;
    }

    public Map<String,Integer> getWeather(int year, int month, Context context){
        Map<String,Integer> weatherCount = new HashMap<>();

        WeatherService weatherService = new WeatherService(context);
        List<Weather> weathers = weatherService.GetAll(Weather.class);

        for(Weather weather:weathers){
            if(weather.getIcon().equals("add"))
                continue;
            weatherCount.put(weather.getIcon(),0);
        }

        EntryWeatherService entryWeatherService = new EntryWeatherService(context);
        if(month == 0){
            List<Weather> entryWeathers = entryWeatherService.getWeatherIdByYear(year);
            for(Weather weather:entryWeathers){
                String icon = weather.getIcon();
                if(weatherCount.containsKey(icon)){
                    weatherCount.put(icon,weatherCount.get(icon)+1);
                }
            }
        }
        else {
            List<Weather> entryWeathers = entryWeatherService.getWeatherIdByMonthYear(month, year);
            for(Weather weather:entryWeathers){
                String icon = weather.getIcon();
                if(weatherCount.containsKey(icon)){
                    weatherCount.put(icon,weatherCount.get(icon)+1);
                }
            }
        }

        return weatherCount;
    }

    public Map<String,Integer> getMoodByDate(int year, int month, int day, Context context){
        Map<String,Integer> emotionCount = new HashMap<>();

        EmotionService emotionService = new EmotionService(context);
        List<Emotion> emotions = emotionService.GetAll(Emotion.class);

        for(Emotion emotion: emotions){
            if(emotion.getIcon().equals("add"))
                continue;
            emotionCount.put(emotion.getIcon(),0);
        }

        EntryEmotionService entryEmotionService = new EntryEmotionService(context);
        List<Emotion> entryEmotions = entryEmotionService.getEmotionIdByDayMonthYear(day, month, year);
        for(Emotion emotion:entryEmotions){
            String icon = emotion.getIcon();
            if(emotionCount.containsKey(icon)){
                emotionCount.put(icon,emotionCount.get(icon)+1);
            }
        }

        return emotionCount;
    }

    public Map<String,Integer> getActivityByDate(int year, int month, int day, Context context){
        Map<String,Integer> activityCount = new HashMap<>();

        ActivityService activityService = new ActivityService(context);
        List<Activity> activities = activityService.GetAll(Activity.class);

        for(Activity activity : activities){
            if(activity.getIcon().equals("add"))
                continue;
            activityCount.put(activity.getIcon(),0);
        }

        EntryActivityService entryActivityService = new EntryActivityService(context);

        List<Activity> entryActivities = entryActivityService.getActivityIdByDayMonthYear(day, month, year);
        for(Activity activity: entryActivities){
            String icon = activity.getIcon();
            if(activityCount.containsKey(icon)){
                activityCount.put(icon,activityCount.get(icon)+1);
            }
        }

        return activityCount;
    }

    public Map<String,Integer> getPartnerByDate(int year, int month, int day, Context context){
        Map<String,Integer> partnerCount = new HashMap<>();

        PartnerService partnerService = new PartnerService(context);
        List<Partner>partners = partnerService.GetAll(Partner.class);

        for(Partner partner:partners){
            if(partner.getIcon().equals("add"))
                continue;
            partnerCount.put(partner.getIcon(),0);
        }

        EntryPartnerService entryPartnerService = new EntryPartnerService(context);
        List<Partner> entryPartners = entryPartnerService.getPartnerIdByDayMonthYear(day, month, year);
        for(Partner partner:entryPartners){
            String icon = partner.getIcon();
            if(partnerCount.containsKey(icon)){
                partnerCount.put(icon,partnerCount.get(icon)+1);
            }
        }

        return partnerCount;
    }

    public Map<String,Integer> getWeatherByDate(int year, int month, int day, Context context){
        Map<String,Integer> weatherCount = new HashMap<>();

        WeatherService weatherService = new WeatherService(context);
        List<Weather> weathers = weatherService.GetAll(Weather.class);

        for(Weather weather:weathers){
            if(weather.getIcon().equals("add"))
                continue;
            weatherCount.put(weather.getIcon(),0);
        }

        EntryWeatherService entryWeatherService = new EntryWeatherService(context);
        List<Weather> entryWeathers = entryWeatherService.getWeatherIdByDayMonthYear(day, month, year);
        for(Weather weather:entryWeathers){
            String icon = weather.getIcon();
            if(weatherCount.containsKey(icon)){
                weatherCount.put(icon,weatherCount.get(icon)+1);
            }
        }

        return weatherCount;
    }

    public Map<String,Integer> getMoodCustom(int byear, int bmonth, int ayear, int amonth, Context context){
        Map<String,Integer> emotionCount = new HashMap<>();

        EmotionService emotionService = new EmotionService(context);
        List<Emotion> emotions = emotionService.GetAll(Emotion.class);

        for(Emotion emotion: emotions){
            if(emotion.getIcon().equals("add"))
                continue;
            emotionCount.put(emotion.getIcon(),0);
        }

        EntryEmotionService entryEmotionService = new EntryEmotionService(context);
        List<Emotion> entryEmotions = entryEmotionService.getEmotionIdCustom(byear, bmonth, ayear, amonth);
        for(Emotion emotion:entryEmotions){
            String icon = emotion.getIcon();
            if(emotionCount.containsKey(icon)){
                emotionCount.put(icon,emotionCount.get(icon)+1);
            }
        }

        return emotionCount;
    }

    public Map<String,Integer> getActivityCustom(int byear, int bmonth, int ayear, int amonth, Context context){
        Map<String,Integer> activityCount = new HashMap<>();

        ActivityService activityService = new ActivityService(context);
        List<Activity> activities = activityService.GetAll(Activity.class);

        for(Activity activity : activities){
            if(activity.getIcon().equals("add"))
                continue;
            activityCount.put(activity.getIcon(),0);
        }

        EntryActivityService entryActivityService = new EntryActivityService(context);

        List<Activity> entryActivities = entryActivityService.getActivityIdCustom(byear, bmonth, ayear, amonth);
        for(Activity activity: entryActivities){
            String icon = activity.getIcon();
            if(activityCount.containsKey(icon)){
                activityCount.put(icon,activityCount.get(icon)+1);
            }
        }

        return activityCount;
    }

    public Map<String,Integer> getPartnerCustom(int byear, int bmonth, int ayear, int amonth, Context context){
        Map<String,Integer> partnerCount = new HashMap<>();

        PartnerService partnerService = new PartnerService(context);
        List<Partner>partners = partnerService.GetAll(Partner.class);

        for(Partner partner:partners){
            if(partner.getIcon().equals("add"))
                continue;
            partnerCount.put(partner.getIcon(),0);
        }

        EntryPartnerService entryPartnerService = new EntryPartnerService(context);
        List<Partner> entryPartners = entryPartnerService.getPartnerIdCustom(byear, bmonth, ayear, amonth);
        for(Partner partner:entryPartners){
            String icon = partner.getIcon();
            if(partnerCount.containsKey(icon)){
                partnerCount.put(icon,partnerCount.get(icon)+1);
            }
        }

        return partnerCount;
    }

    public Map<String,Integer> getWeatherCustom(int byear, int bmonth, int ayear, int amonth, Context context){
        Map<String,Integer> weatherCount = new HashMap<>();

        WeatherService weatherService = new WeatherService(context);
        List<Weather> weathers = weatherService.GetAll(Weather.class);

        for(Weather weather:weathers){
            if(weather.getIcon().equals("add"))
                continue;
            weatherCount.put(weather.getIcon(),0);
        }

        EntryWeatherService entryWeatherService = new EntryWeatherService(context);
        List<Weather> entryWeathers = entryWeatherService.getWeatherIdCustom(byear, bmonth, ayear, amonth);
        for(Weather weather:entryWeathers){
            String icon = weather.getIcon();
            if(weatherCount.containsKey(icon)){
                weatherCount.put(icon,weatherCount.get(icon)+1);
            }
        }

        return weatherCount;
    }
}
