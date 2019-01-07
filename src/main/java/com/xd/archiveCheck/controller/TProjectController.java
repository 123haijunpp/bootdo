package com.xd.archiveCheck.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.xd.archiveCheck.domain.TProFileDO;
import com.xd.archiveCheck.domain.TProjectDO;
import com.xd.archiveCheck.service.TProjectService;
import com.xd.common.util.SendApiconfig;
import javafx.scene.shape.Arc;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.xd.archiveCheck.utils.ArchiveCheckUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */

@Controller
@RequestMapping("/archiveCheck/tProject")
public class TProjectController {

    // 跳转模板前缀
    private static final String prefix = "archiveCheck/tProject";

    @Autowired
    private TProjectService tProjectService;

    @Autowired
    private SendApiconfig sendApiconfig;


    /**
     * 跳转到归档检查页面
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/doArchiveCheck"})
    public String doArchiveCheck(HttpServletRequest request) {
        request.getSession().setAttribute("check_role", "研发经理");
        return prefix + "/tProject";
    }

    @GetMapping(value = {"/listArchiveCheck"})
    @ResponseBody
    JSONObject listArchiveCheck(@RequestParam("path") String path) throws UnsupportedEncodingException {
        // 将路径中的 \ 替换为 \\ 便于python端解析
        path = path.replace("\\", "\\\\");
        Map<String, Object> params = new HashMap<>(16);
        ArchiveCheckUtils utils = new ArchiveCheckUtils();
        params.put("rootDir", path);
        JSONObject postData = new JSONObject();
        postData.put("code", "scan_001");
        postData.put("params", params);
        // 所有扫描出来的文件
        List<TProjectDO> projectAll = utils.postGetFile(postData, sendApiconfig);

        JSONObject postData1 = new JSONObject();
        postData1.put("code", "scan_004");
        postData1.put("params", params);
        JSONObject postJsonData = JSONObject.parseObject(sendApiconfig.getPython(postData1));
        String data = postJsonData.get("data").toString();
        // 数据库已经存在的文件
        List<TProjectDO> existsAll = JSONArray.parseArray(data, TProjectDO.class);

        // 找两个list的交集
        boolean flag = projectAll.removeAll(existsAll);

        // 响应到客户端
        JSONObject jsonData = new JSONObject();
        jsonData.put("total", projectAll.size());
        jsonData.put("rows", projectAll);
        return jsonData;
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("archiveCheck:tProject:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        TProjectDO tProject = tProjectService.get(id);
        model.addAttribute("tProject", tProject);
        return "archiveCheck/tProject/edit";
    }

    /**
     * 保存文件
     */
    @ResponseBody
    @PostMapping(value = "/save")
    public R save(@RequestBody String path) {
        List<TProjectDO> list = new ArrayList<>();
        TProjectDO project = null;
        String proName; // 项目名
        String fileName; // 文件名
        String state = null; // 状态
        String location = null; //文件的位置
        int flag = 0; // 从第一个 / 作为标记，但不包括自己
        int flag2 = 0; // 从第二个 / 作为标记，但不包括自己
        //  将要保存的归档资源分割
        String[] split = path.split(",");
        for (String s : split) {
            flag = s.indexOf("/");
            flag2 = s.indexOf("/", flag + 1);
            project = new TProjectDO();

            proName = s.substring(0, flag);
            location = s.substring(flag2 + 1, s.lastIndexOf("/"));
            fileName = s.substring(flag + 1, flag2);
            state = s.substring(s.length() - 1);

            project.setProName(proName);
            project.setFileName(fileName);
            project.setState(state);
            project.setPath(location);
            list.add(project);
        }
        JSONObject postData = new JSONObject();
        postData.put("code", "scan_002");
        postData.put("params", JSONObject.toJSONString(list));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(sendApiconfig.getPython(postData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Boolean.valueOf(jsonObject.get("status").toString())) {
            return R.ok();
        }
        return R.error(jsonObject.get("msg").toString());
    }

    /**
     * 查询开源程序
     */
    @GetMapping(value = {"/openSourceList"})
    @ResponseBody
    JSONObject openSourceList(@RequestParam Map<String, Object> params, HttpServletRequest request) throws
            UnsupportedEncodingException {
        //查询列表数据
        Query query = new Query(params);
        JSONObject postData = new JSONObject();
        postData.put("code", "scan_003");
        postData.put("params", com.alibaba.druid.support.json.JSONUtils.toJSONString(query));

        JSONObject jsonObject = JSONObject.parseObject(sendApiconfig.getPython(postData));
        String data = jsonObject.get("data").toString();
        List<Object> items = JSON.parseObject(JSONObject.parseObject(data).get("items").toString(), new TypeReference<List<Object>>() {
        });
        String total = JSONObject.parseObject(data).get("total").toString();
        //返回json数据,提供给bootStrap的table控件
        JSONObject jsonData = new JSONObject();
        jsonData.put("total", Integer.valueOf(total));
        jsonData.put("rows", items);
        return jsonData;
    }


    /**
     * 跳转查询所有开源页面
     *
     * @return
     */
    @GetMapping(value = {"/doOpenSource"})
    String doOpenSource() {
        return prefix + "/openSource";
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("archiveCheck:tProject:edit")
    public R update(TProjectDO tProject) {
        tProjectService.update(tProject);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("archiveCheck:tProject:remove")
    public R remove(Long id) {
        if (tProjectService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("archiveCheck:tProject:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        tProjectService.batchRemove(ids);
        return R.ok();
    }

}
