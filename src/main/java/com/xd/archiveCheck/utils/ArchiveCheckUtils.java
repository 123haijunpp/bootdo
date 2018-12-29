package com.xd.archiveCheck.utils;

import com.alibaba.fastjson.JSONObject;
import com.xd.archiveCheck.domain.TProjectDO;
import com.xd.common.util.SendApiconfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供给archiveCheck中的Controller用
 */
@Component
public class ArchiveCheckUtils {

    /**
     * 调用python接口提供的所有PDM中文件
     *
     * @param obj
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public List<TProjectDO> postGetFile(JSONObject obj, SendApiconfig sendApiconfig) throws java.io.UnsupportedEncodingException {
        List<TProjectDO> projectAll = new ArrayList<>();
        // 访问Python接口,获取所有扫描的文件
        JSONObject jsonObject = JSONObject.parseObject(sendApiconfig.getPython(obj));
        String data = jsonObject.get("data").toString().replace("\\\\\\\\", "/").
                replace("\"", "").replace("\\\\", "/");
        String[] file = dataSplit(data);
        TProjectDO projectDO = null;
        String proName; // 项目名
        String fileName; // 文件名
        String filePath; // 文件路径
        int flag = 0; // 以第二个 / 作为标记，但不包括自己
        int flag1 = 0; // 以第一个 / 作为标记，但不包括自己
        int lastFlag = 0; // 以最后一个 / 作为标记，但不包括自己
        for (String f : file) {
            flag = f.indexOf("/", f.indexOf("/") + 1);
            flag1 = f.indexOf("/") + 1;
            lastFlag = f.lastIndexOf("/") + 1;
            projectDO = new TProjectDO();
            proName = f.substring(flag1, flag);
            filePath = f.substring(flag + 1);
            fileName = f.substring(lastFlag);
            projectDO.setProName(proName);
            projectDO.setFileName(fileName);
            projectDO.setPath(proName + "/" + filePath);
            projectDO.setState("1");// 1 用来给bootStart table判断的，用来设置复选框
            projectAll.add(projectDO);
        }
        return projectAll;
    }


    /**
     * 判断字符在字符串中出现的个数
     *
     * @return
     */
    public int getStrCount(String str, String regStr) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).equals(regStr)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 去[]
     *
     * @param data
     * @return
     */
    public String[] dataSplit(String data) {
        String subFile = data.substring(1, data.length() - 1);
        String[] file = subFile.split(",");
        return file;
    }

}
