package com.testdb.demo.controller;

import com.testdb.demo.entity.mood.BaseMood;
import com.testdb.demo.entity.mood.Mood;
import com.testdb.demo.entity.mood.WeekMood;
import com.testdb.demo.service.MoodService;
import com.testdb.demo.utils.AjaxResponseBody;
import com.testdb.demo.utils.Result;
import com.testdb.demo.utils.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/api/mood")
@AjaxResponseBody
public class MoodController {

    @Autowired
    MoodService ms;

    @GetMapping
    public Result<Map<String, WeekMood>> getMoodList(Principal principal){
        return Result.success(ms.getMoodList(principal.getName()));
    }

    @PostMapping
    public Result<Void> addMood(Principal principal,
                                @RequestBody Mood mood){
        int status = ms.addMood(mood, principal.getName());
        if(status == 1){
            return Result.failure(ResultStatus.FAILURE.setMessage("今天已经记录过心情啦！"));
        }else if(status == 2){
            return Result.failure(ResultStatus.WRONG_PARAMETERS.setMessage("你的一句话太长啦！"));
        }
        return Result.success();
    }

    @GetMapping("/others")
    public Result<List<BaseMood>> getRandomMood(Principal principal, @RequestParam("moodType") String moodType){
        List<BaseMood> results =ms.getRandomMood(principal.getName(),moodType);
        if(results == null) {
            return Result.failure(ResultStatus.FAILURE.setMessage("今天还没有小伙伴儿发布类似的心情喔！"));
        }
        return Result.success(results);
    }

}
