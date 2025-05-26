package tech.qiantong.qknow.module.ext.service.extSchema.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchema.ExtSchemaMapper;
import tech.qiantong.qknow.module.ext.service.extSchema.IExtSchemaService;
/**
 * 概念配置Service业务层处理
 *
 * @author qknow
 * @date 2025-02-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtSchemaServiceImpl  extends ServiceImpl<ExtSchemaMapper,ExtSchemaDO> implements IExtSchemaService {
    @Value("${deepke.conceptYaml}")
    private String conceptYaml;

    @Resource
    private ExtSchemaMapper extSchemaMapper;

    @Override
    public PageResult<ExtSchemaDO> getExtSchemaPage(ExtSchemaPageReqVO pageReqVO) {
        return extSchemaMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtSchema(ExtSchemaSaveReqVO createReqVO) {
        ExtSchemaDO dictType = BeanUtils.toBean(createReqVO, ExtSchemaDO.class);
        extSchemaMapper.insert(dictType);

//        PageResult<ExtSchemaDO> pageResult = extSchemaMapper.selectPage(new ExtSchemaPageReqVO());
//        List<ExtSchemaDO> schemaDOList = BeanUtils.toBean(pageResult.getRows(), ExtSchemaDO.class);
//        updateConceptSchemaYAML(schemaDOList);
        return dictType.getId();
    }

    @Override
    public int updateExtSchema(ExtSchemaSaveReqVO updateReqVO) {
        // 相关校验

        // 更新概念配置
        ExtSchemaDO updateObj = BeanUtils.toBean(updateReqVO, ExtSchemaDO.class);

//        PageResult<ExtSchemaDO> pageResult = extSchemaMapper.selectPage(new ExtSchemaPageReqVO());
//        List<ExtSchemaDO> schemaDOList = BeanUtils.toBean(pageResult.getRows(), ExtSchemaDO.class);
//        updateConceptSchemaYAML(schemaDOList);
        return extSchemaMapper.updateById(updateObj);
    }

    /**
     * 修改概念配置yaml
     * @param schemaDOList
     */
    private void updateConceptSchemaYAML(List<ExtSchemaDO> schemaDOList) {
        String[] row = new String[schemaDOList.size()];
        for (int i = 0; i < row.length; i++) {
            row[i] = schemaDOList.get(i).getName();
        }
        List<String> list = Arrays.asList(row);
        String lables = "[" + String.join(", ",
                list.stream()
                        .map(s -> "'" + s + "'")  // 为每个元素添加单引号
                        .toArray(String[]::new))  // 转换为数组传递给 String.join()
                + "]";
        //修改deepke中的lables
        updatePythonYmalContent(conceptYaml,lables);
    }

    /**
     * 修改deepke中yaml
     * @param yamlUrl
     * @param lables
     * @return
     */
    private boolean updatePythonYmalContent(String yamlUrl, String lables) {
        try {
            // 读取 YAML 文件
            File file = new File(yamlUrl);
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.loadAs(new FileReader(file), Map.class);
            // 修改 labels 字段的内容
            data.put("lables", lables);
            // 将修改后的内容写回文件
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 设置输出格式为块样式
            Yaml newYaml = new Yaml(options);
            newYaml.dump(data, new FileWriter(file));
        } catch (IOException e) {
            log.info("修改 lables 字段的内容异常:{}", e);
            return false;
        }
        return true;
    }


    @Override
    public int removeExtSchema(Collection<Long> idList) {
        // 批量删除概念配置
        return extSchemaMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtSchemaDO getExtSchemaById(Long id) {
        return extSchemaMapper.selectById(id);
    }

    @Override
    public List<ExtSchemaDO> getExtSchemaList() {
        return extSchemaMapper.selectList();
    }

    @Override
    public Map<Long, ExtSchemaDO> getExtSchemaMap() {
        List<ExtSchemaDO> extSchemaList = extSchemaMapper.selectList();
        return extSchemaList.stream()
                .collect(Collectors.toMap(
                        ExtSchemaDO::getId,
                        extSchemaDO -> extSchemaDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入概念配置数据
     *
     * @param importExcelList 概念配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importExtSchema(List<ExtSchemaRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (ExtSchemaRespVO respVO : importExcelList) {
            try {
                ExtSchemaDO extSchemaDO = BeanUtils.toBean(respVO, ExtSchemaDO.class);
                Long extSchemaId = respVO.getId();
                if (isUpdateSupport) {
                    if (extSchemaId != null) {
                        ExtSchemaDO existingExtSchema = extSchemaMapper.selectById(extSchemaId);
                        if (existingExtSchema != null) {
                            extSchemaMapper.updateById(extSchemaDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + extSchemaId + " 的概念配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + extSchemaId + " 的概念配置记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<ExtSchemaDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", extSchemaId);
                    ExtSchemaDO existingExtSchema = extSchemaMapper.selectOne(queryWrapper);
                    if (existingExtSchema == null) {
                        extSchemaMapper.insert(extSchemaDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + extSchemaId + " 的概念配置记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + extSchemaId + " 的概念配置记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }

    @Override
    public List<ExtSchemaDO> getExtSchemaAllList(ExtSchemaDO extSchemaDO) {
        extSchemaDO.setDelFlag(false);
        return extSchemaMapper.getExtSchemaAllList(extSchemaDO);
    }
}
