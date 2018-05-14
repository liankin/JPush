package com.admin.jpush.testdemo;

import com.alibaba.fastjson.JSON;

import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21 0021.
 */
public class JsonRespondParse implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        //判断整个数据是不是由一个List包裹起来的
        if (resultClass == List.class) {
            //将List解析成一个个小的对象（List的名字叫datas）
            result = JSON.parseObject(result).getString("datas");
            //解析成小的vo对象
            return JSON.parseArray(result, (Class<?>) ParameterizedTypeUtil.getParameterizedType(resultType, List.class, 0));
        } else {  //如果不是用List组成的则直接将Json进行解析成vo实体类
            return JSON.parseObject(result, resultClass);
        }
    }
}
