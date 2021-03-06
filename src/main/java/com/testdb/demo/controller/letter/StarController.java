package com.testdb.demo.controller.letter;

import com.alibaba.fastjson.JSONObject;
import com.testdb.demo.service.letter.LetterService;
import com.testdb.demo.service.letter.StarService;
import com.testdb.demo.utils.response.AjaxResponseBody;
import com.testdb.demo.utils.response.Result;
import com.testdb.demo.utils.response.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/star")
@AjaxResponseBody
public class StarController {

    @Autowired
    StarService ss;

    @Autowired
    LetterService ls;

    /**
     * 点赞
     * @param jsonObject
     * @return
     */
    @GetMapping
    public Result<Void> star(Authentication token,
                             @RequestBody JSONObject jsonObject){
        if(!ss.star(token,
                jsonObject.getString("targetUsername"),
                jsonObject.getLongValue("aid"))){
            return Result.failure(ResultStatus.FAILURE.setMessage("已经给这篇文章点过赞了喔！"));
        }
        return Result.success();
    }

    /**
     * 获取点赞数
     * @param aid
     * @return
     */
    @GetMapping("/count")
    public Result<Long> star(@RequestParam long aid ){
        if(ls.checkInvalidLetterId(aid)){
            return Result.failure(ResultStatus.WRONG_PARAMETERS.setMessage("不存在这封信！"));
        }
        return Result.success(ss.countStar(aid));
    }

}
