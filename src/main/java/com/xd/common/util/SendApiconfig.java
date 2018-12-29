package com.xd.common.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * 　　* @Description:
 * 　　* @param
 * 　　* @return
 * 　　* @throws
 * 　　* @author Andrew
 * 　　* @date $ $
 */
@Component
public class SendApiconfig {
    @Value("${python.wechatPath}")
    private String ServicePath;

    @Value("${python.apiPath}")
    private String apiPath;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public Map<String, Object> sendApi(String url) {
        Map<String, Object> map = new HashMap<>();
        String str = "";
        str = restTemplate.getForEntity(ServicePath + url, String.class).getBody();
        map.put("result", str);
        return map;
    }

    /**
     * get分页
     *
     * @param url
     * @return
     */
    public String sendApiBypage(String url) {
        Map<String, Object> map = new HashMap<>();
        String str = "";
        str = restTemplate.getForEntity(ServicePath + url, String.class).getBody();
        map.put("result", str);

        return str;
    }

    /**
     * post请求
     *
     * @param url
     * @return
     */
    public int sendPostApi(String url, JSONObject postData) {
        int resultstatu = 0;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(postData.toString(), headers);
        String result = "";
        ResponseEntity<String> responseEntity = restTemplate.exchange(ServicePath + url,
                HttpMethod.POST, formEntity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        status.is2xxSuccessful();
        resultstatu = status.value();
        return resultstatu;
    }

    /**
     * put请求
     *
     * @param url
     * @param jsonObject
     */
    public int sendPutApi(String url, JSONObject jsonObject) {
        int result = 0;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(ServicePath + url,
                HttpMethod.PUT, formEntity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        status.is2xxSuccessful();
        result = status.value();

        return result;
    }

    /**
     * delete请求
     *
     * @param url
     */
    public int sendDeleteApi(String url) {
        int result = 0;
        ResponseEntity<String> responseEntity = restTemplate.exchange(ServicePath + url,
                HttpMethod.DELETE, null, String.class, 100);
        HttpStatus status = responseEntity.getStatusCode();
        status.is2xxSuccessful();
        result = status.value();
        return result;
    }

    /**
     * 访问Python接口
     *
     * @param
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getPython(JSONObject postData) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = token() + ":";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = encoder.encodeToString(textByte);
        //请求头添加权限标识
        headers.add("Authorization", "Basic " + encodedText);
        HttpEntity<String> formEntity = new HttpEntity<String>(postData.toString(), headers);
        String result = "";
        //2.调用PYTHON服务，返回JSON字符串
        ResponseEntity<String> response = restTemplate.exchange(ServicePath,
                HttpMethod.POST, formEntity, String.class);
        return response.getBody();
    }

    /**
     * 获取token
     *
     * @return
     */
    public String token() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Basic d3g6d3g=");
        HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
        String result = "";
        //2.调用PYTHON服务，返回JSON字符串
        String url = apiPath + "/api/token";
//        System.out.println("服务地址： " + ServicePath + "  实际路径：" + url + " apiPaht=" + apiPath);
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET, formEntity, String.class);
        JSONObject report = JSONObject.parseObject(response.getBody());
        return report.get("token").toString();
    }
}
