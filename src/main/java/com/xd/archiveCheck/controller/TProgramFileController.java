package com.xd.archiveCheck.controller;

import java.util.List;
import java.util.Map;

import com.xd.archiveCheck.domain.TProgramFileDO;
import com.xd.archiveCheck.service.TProgramFileService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;

/**
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */

@Controller
@RequestMapping("/hzConfigure/tProgramFile")
public class TProgramFileController {
    @Autowired
    private TProgramFileService tProgramFileService;

    @GetMapping()
    @RequiresPermissions("hzConfigure:tProgramFile:tProgramFile")
    String TProgramFile() {
        return "hzConfigure/tProgramFile/tProgramFile";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("hzConfigure:tProgramFile:tProgramFile")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<TProgramFileDO> tProgramFileList = tProgramFileService.list(query);
        int total = tProgramFileService.count(query);
        PageUtils pageUtils = new PageUtils(tProgramFileList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("hzConfigure:tProgramFile:add")
    String add() {
        return "hzConfigure/tProgramFile/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("hzConfigure:tProgramFile:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        TProgramFileDO tProgramFile = tProgramFileService.get(id);
        model.addAttribute("tProgramFile", tProgramFile);
        return "hzConfigure/tProgramFile/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("hzConfigure:tProgramFile:add")
    public R save(TProgramFileDO tProgramFile) {
        if (tProgramFileService.save(tProgramFile) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("hzConfigure:tProgramFile:edit")
    public R update(TProgramFileDO tProgramFile) {
        tProgramFileService.update(tProgramFile);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("hzConfigure:tProgramFile:remove")
    public R remove(Long id) {
        if (tProgramFileService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("hzConfigure:tProgramFile:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        tProgramFileService.batchRemove(ids);
        return R.ok();
    }

}
