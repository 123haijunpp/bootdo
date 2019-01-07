package com.xd.archiveCheck.utils;

import com.alibaba.fastjson.JSONObject;
import com.xd.archiveCheck.domain.TProjectDO;
import com.xd.common.util.SendApiconfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            projectAll.add(projectDO);
        }
        return projectAll;
    }


    /**
     * 判断字符在字符串中出现的个数
     *
     * @return
     */
    private int getStrCount(String str, String regStr) {
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
    private String[] dataSplit(String data) {
        String subFile = data.substring(1, data.length() - 1);
        String[] file = subFile.split(",");
        return file;
    }

    /**
     * 两个List不管有多少个重复，只要重复的元素在两个List都能找到，则不应该包含在返回值里面，所以在做第二次循环时，这样判断：
     * 如果当前元素在map中找不到，则肯定需要添加到返回值中，如果能找到则value++，遍历完之后diff里面已经包含了只在list2里而没在list2里的元素，
     * 剩下的工作就是找到list1里有list2里没有的元素，遍历map取value为1的即可：
     *
     * @param projectAll
     * @param existsAll
     * @return
     */
    public static List<TProjectDO> getDifferent(List<TProjectDO> projectAll, List<TProjectDO> existsAll) {
        List<TProjectDO> diff = new ArrayList<>();
        List<TProjectDO> maxList = projectAll;
        List<TProjectDO> minList = existsAll;
        if (projectAll.size() > existsAll.size()) {
            maxList = existsAll;
            minList = projectAll;
        }
        Map<TProjectDO, Integer> map = new HashMap<>(maxList.size());
        for (TProjectDO pro : maxList) {
            map.put(pro, 1);
        }
        for (TProjectDO pro : minList) {
            if (map.get(pro) != null) {
                map.put(pro, 2);
                continue;
            }
            diff.add(pro);
        }
        for (Map.Entry<TProjectDO, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        return diff;
    }

}
