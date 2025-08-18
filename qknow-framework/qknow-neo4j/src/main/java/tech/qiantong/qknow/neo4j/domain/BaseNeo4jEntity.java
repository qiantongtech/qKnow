package tech.qiantong.qknow.neo4j.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Date;

/**
 * neo4jEntity基类
 *
 * @author qknow
 */
public class BaseNeo4jEntity {
    /**
     * 搜索值
     */
    @Property(name = "searchValue")
    private String searchValue;

    @Property(name = "creatorId")
    private Long creatorId;

    /**
     * 创建者
     */
    @Property(name = "createBy")
    private String createBy;

    /**
     * 创建时间
     */
    // 创建时间自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Property(name = "createTime")
    private Date createTime;

    @Property(name = "updaterId")
    private Long updaterId;

    /**
     * 更新者
     */
    @Property(name = "updateBy")
    private String updateBy;

    /**
     * 更新时间
     */
    // 更新时间自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Property(name = "updateTime")
    private Date updateTime;

    /**
     * 备注
     */
    @Property(name = "remark")
    private String remark;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
