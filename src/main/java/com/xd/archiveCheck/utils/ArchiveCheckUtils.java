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
    public List<TProjectDO> commonList(JSONObject obj, SendApiconfig sendApiconfig) throws java.io.UnsupportedEncodingException {
        List<TProjectDO> projectAll = new ArrayList<>();
        // 访问Python接口,获取所有扫描的文件
        JSONObject jsonObject = JSONObject.parseObject(sendApiconfig.getPython(obj));
        String data = jsonObject.get("data").toString();
        String[] file = dataSplit(data);
        TProjectDO projectDO;
        // 用来保存截取后的项目名与文件名
        String proFile;
        // 项目名
        String proName;
        // 文件名
        String fileName;
        // 文件路径
        String filePath;
        for (String f : file) {
            projectDO = new TProjectDO();
            proFile = f.substring(f.indexOf("\\\\", f.indexOf("\\\\") + 1) + 2, f.length() - 1);
            proName = proFile.substring(0, proFile.indexOf("\\\\"));
            filePath = proFile.substring(proFile.indexOf("\\\\") + 2);
            fileName = proFile.substring(proFile.lastIndexOf("\\\\") + 2);
            projectDO.setProName(proName);
            if (getStrCount(proFile, "\\") == 2) {
                projectDO.setPath(proName + "/" + fileName);
            } else {
                projectDO.setPath(proName + "/" + filePath.replace("\\\\", "/"));
            }
            projectDO.setFileName(fileName);
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
     * 只获取data的数据
     *
     * @param data 后台传递的数据
     * @return
     */
    public String[] dataSplit(String data) {
        String subFile = data.substring(data.indexOf("[") + 1, data.indexOf("]"));
        String[] file = subFile.split(",");
        return file;
    }

}
