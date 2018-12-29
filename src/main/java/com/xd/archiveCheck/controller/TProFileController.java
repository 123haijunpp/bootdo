package com.xd.archiveCheck.controller;

import java.util.List;
import java.util.Map;

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
import com.xd.archiveCheck.domain.TProFileDO;
import com.xd.archiveCheck.service.TProFileService;


/**
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */

@Controller
@RequestMapping("/hzConfigure/tProFile")
public class TProFileController {
    @Autowired
    private TProFileService tProFileService;

    @GetMapping()
    @RequiresPermissions("hzConfigure:tProFile:tProFile")
    String TProFile() {
        return "hzConfigure/tProFile/tProFile";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("hzConfigure:tProFile:tProFile")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<TProFileDO> tProFileList = tProFileService.list(query);
        int total = tProFileService.count(query);
        PageUtils pageUtils = new PageUtils(tProFileList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("hzConfigure:tProFile:add")
    String add() {
        return "hzConfigure/tProFile/add";
    }

    @GetMapping("/edit/{proCode}")
    @RequiresPermissions("hzConfigure:tProFile:edit")
    String edit(@PathVariable("proCode") Long proCode, Model model) {
        TProFileDO tProFile = tProFileService.get(proCode);
        model.addAttribute("tProFile", tProFile);
        return "hzConfigure/tProFile/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("hzConfigure:tProFile:add")
    public R save(TProFileDO tProFile) {
        if (tProFileService.save(tProFile) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("hzConfigure:tProFile:edit")
    public R update(TProFileDO tProFile) {
        tProFileService.update(tProFile);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("hzConfigure:tProFile:remove")
    public R remove(Long proCode) {
        if (tProFileService.remove(proCode) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("hzConfigure:tProFile:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] proCodes) {
        tProFileService.batchRemove(proCodes);
        return R.ok();
    }

}
