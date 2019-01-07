package com.xd.archiveCheck.domain;

import java.io.Serializable;
import java.util.Objects;


/**
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */
public class TProjectDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键:项目ID,自动增长
    private Long id;
    //项目名字
    private String proName;
    //路径
    private String path;
    //备注
    private String remark;

    //文件名
    private String fileName;
    //状态
    private String state;

    /**
     * 设置：主键:项目ID,自动增长
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键:项目ID,自动增长
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：项目名字
     */
    public void setProName(String proName) {
        this.proName = proName;
    }

    /**
     * 获取：项目名字
     */
    public String getProName() {
        return proName;
    }

    /**
     * 设置：路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取：路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置：备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 文件名
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TProjectDO)) return false;
        TProjectDO projectDO = (TProjectDO) o;
        return Objects.equals(id, projectDO.id) &&
                Objects.equals(proName, projectDO.proName) &&
                Objects.equals(path, projectDO.path) &&
                Objects.equals(remark, projectDO.remark) &&
                Objects.equals(fileName, projectDO.fileName) &&
                Objects.equals(state, projectDO.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, proName, path, remark, fileName, state);
    }

    @Override
    public String toString() {
        return "TProjectDO{" +
                "id=" + id +
                ", proName='" + proName + '\'' +
                ", path='" + path + '\'' +
                ", remark='" + remark + '\'' +
                ", fileName='" + fileName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
