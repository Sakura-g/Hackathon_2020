package com.testdb.demo.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.testdb.demo.entity.QiniuCallbackMessage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class QiniuUtil {

    /**
     * 七牛云工具类
     */

    static String ACCESS_KEY = "bauMxdmpiAfyHKZAPW5nsT7pwI0PSRLnDLpaKRgP";
    static String SECRET_KEY = "lgQHDim9JWvhZUJDU0Od2I-WObd1IGCAmJLIeI8N";
    static String bucketName = "test-lz";

    static String key = null;
    static long expireSeconds = 600L;

    static String callbackUrl = "https://thenebula.cn/api/callback/avatar";
    static String callbackUrlTest = "http://9fpht5.natappfree.cc/api/callback/avatar";
    static String callbackBodyType = "application/json";


    static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    static String uploadToken = auth.uploadToken(bucketName);

    // 华东仓库
    static Configuration cfg = new Configuration(Region.region0());
    static UploadManager uploadManager = new UploadManager(cfg);

    // 获取上传令牌
    @SneakyThrows
    public static String getToken(String uploadPath){
        key = uploadPath;
        return auth.uploadToken(bucketName, key);
    }

    /**
     * 获取随机key的上传token
     * @return
     */
    @SneakyThrows
    public static String getRandomKeyToken() {
        key = UuidMaker.getUuid();
        System.out.println(key);
        return auth.uploadToken(bucketName, key);
    }

    /**
     * 返回带policy的token
     * @param uploadPath
     * @param putPolicy
     * @return
     */
    @SneakyThrows
    public static String getTokenWithPolicy(String uploadPath, StringMap putPolicy){
        key = uploadPath;
        return auth.uploadToken(bucketName, key, expireSeconds, putPolicy);
    }

    public static void uploadTest(String sourceUrl,String targetPath, String uploadToken) throws IOException {
        try {
            Response response = uploadManager.put(sourceUrl, targetPath, uploadToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 效验回调有效性
     * @param request
     * @param callbackBody
     * @return
     */
    @SneakyThrows
    public static Boolean validateCallback(HttpServletRequest request,
                                               byte[] callbackBody) {
        String callbackAuthHeader = request.getHeader("Authorization");
        return auth.isValidCallback(callbackAuthHeader, callbackUrl, callbackBody, callbackBodyType);
    }

    @SneakyThrows
    public static Boolean validateCallbackTest(HttpServletRequest request,
                                                           byte[] callbackBody) {
        String callbackAuthHeader = request.getHeader("Authorization");
        return auth.isValidCallback(callbackAuthHeader, callbackUrlTest, callbackBody, callbackBodyType);
    }
}