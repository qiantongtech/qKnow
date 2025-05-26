package tech.qiantong.qknow.module.system.ca.service.impl;

import java.util.List;

import tech.qiantong.qknow.module.system.ca.domain.CaCert;
import tech.qiantong.qknow.module.system.ca.mapper.CaCertMapper;
import tech.qiantong.qknow.module.system.ca.service.ICaCertService;
import tech.qiantong.qknow.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 证书管理Service业务层处理
 *
 * @author qknow
 * @date 2024-08-18
 */
@Service
public class CaCertServiceImpl implements ICaCertService
{
    @Autowired
    private CaCertMapper caCertMapper;

    /**
     * 查询证书管理
     *
     * @param id 证书管理主键
     * @return 证书管理
     */
    @Override
    public CaCert selectCaCertById(Long id)
    {
        return caCertMapper.selectCaCertById(id);
    }

    /**
     * 查询证书管理列表
     *
     * @param caCert 证书管理
     * @return 证书管理
     */
    @Override
    public List<CaCert> selectCaCertList(CaCert caCert)
    {
        return caCertMapper.selectCaCertList(caCert);
    }

    /**
     * 新增证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    @Override
    public int insertCaCert(CaCert caCert)
    {
        caCert.setCreateTime(DateUtils.getNowDate());
        return caCertMapper.insertCaCert(caCert);
    }

    /**
     * 修改证书管理
     *
     * @param caCert 证书管理
     * @return 结果
     */
    @Override
    public int updateCaCert(CaCert caCert)
    {
        caCert.setUpdateTime(DateUtils.getNowDate());
        return caCertMapper.updateCaCert(caCert);
    }

    /**
     * 批量删除证书管理
     *
     * @param ids 需要删除的证书管理主键
     * @return 结果
     */
    @Override
    public int deleteCaCertByIds(Long[] ids)
    {
        return caCertMapper.deleteCaCertByIds(ids);
    }

    /**
     * 删除证书管理信息
     *
     * @param id 证书管理主键
     * @return 结果
     */
    @Override
    public int deleteCaCertById(Long id)
    {
        return caCertMapper.deleteCaCertById(id);
    }
}
