package com.testdb.demo.service.mood;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.testdb.demo.entity.mood.BaseMood;
import com.testdb.demo.entity.mood.Mood;
import com.testdb.demo.entity.mood.WeekMood;
import com.testdb.demo.mapper.mood.MoodMapper;
import com.testdb.demo.utils.WeekUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;


@Service
public class MoodService extends ServiceImpl<MoodMapper, Mood> {

    @Autowired
    private MoodMapper moodMapper;

    @SneakyThrows
    public int addMood(Mood mood, String username){

        // 2 心情描述过长 1 今天已经填写过了 0 成功
        if(moodMapper.selectOne(new QueryWrapper<Mood>()
                .eq("username", username)
                .eq("mood_date", LocalDate.now())
                .select("id")) != null){
            return 1;
        }

        int length = mood.getDescription().length();
        if(length > 80) {
            return 2;
        }
        else if(length > 20){
            mood.setPreview(mood.getDescription().substring(0,18)+"...");
        }
        else {
            mood.setPreview(mood.getDescription());
        }

        LocalDate now = LocalDate.now();
        mood.setUsername(username);
        mood.setMoodDate(now);
        mood.setDayOfWeek(now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        moodMapper.insert(mood);
        return 0;
    }

    @SneakyThrows
    public LocalDate getCurrentDay(){
        return LocalDate.now();
    }

    @SneakyThrows
    public String getCurrentDayOfWeek(){
        return getCurrentDay()
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    @SneakyThrows
    public Map<String, WeekMood> getMoodList(String username){
        LocalDate beginTime = LocalDate.now().minusDays(6);
        List<Map<String, Object>> oldList = this.listMaps(new QueryWrapper<Mood>()
                .select("day_of_week", "mood_type","preview")
                .eq("username",username)
                .ge("mood_date",beginTime));
        Map<String, WeekMood> newList = createWeekMoodList(beginTime);
        for(Map<String, Object> day: oldList){
            newList.put((String)day.get("day_of_week"), new WeekMood((String)day.get("mood_type"), (String)day.get("preview")));
        }
        return newList;
    }

    @SneakyThrows
    public Map<String, WeekMood> createWeekMoodList(LocalDate beginTime){
        Map<String, WeekMood> list = new LinkedHashMap<>();
        String beginDayOfWeek = beginTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        list.put(beginDayOfWeek,
                new WeekMood("0","这天没有发布心情哦"));
        for(int i=0;i<6;++i){
            String tomorrow = WeekUtil.next(beginDayOfWeek);
            list.put(tomorrow, new WeekMood("0","这天没有发布心情哦"));
            beginDayOfWeek = tomorrow;
        }
        return list;
    }

    public List<BaseMood> randomize(List<BaseMood> oldList){
        Random random = new Random(System.currentTimeMillis());
        List<BaseMood> newList = new ArrayList<>();
        newList.add(oldList.get(random.nextInt(oldList.size())));
        newList.add(oldList.get(random.nextInt(oldList.size())));
        return newList;
    }

    public List<BaseMood> getRandomMood(String username, String userMoodType) {
        List<BaseMood> oldList = moodMapper.getOthersMoodList(username, getCurrentDay(),userMoodType);
        if(oldList.isEmpty() || oldList.size() == 1) {
            return null;
        }
        return randomize(oldList);
    }
}
