package tech.qiantong.qknow.module.kmc.service.kmcDocument.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.kmcCategory.KmcCategoryMapper;
import tech.qiantong.qknow.module.kmc.dal.mapper.kmcDocument.KmcDocumentMapper;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;
import tech.qiantong.qknow.redis.service.IRedisService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识文件Service业务层处理
 *
 * @author qknow
 * @date 2025-02-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcDocumentServiceImpl  extends ServiceImpl<KmcDocumentMapper, KmcDocumentDO> implements IKmcDocumentService {

    @Value("${upload.filePath}")
    private String storagePath;

    @Resource
    private KmcDocumentMapper kmcDocumentMapper;

    @Resource
    private KmcCategoryMapper kmcCategoryMapper;

    @Resource
    private IRedisService redisService;

    @Override
    public PageResult<KmcDocumentDO> getKmcDocumentPage(KmcDocumentPageReqVO pageReqVO) {
        return kmcDocumentMapper.selectPage(pageReqVO);
    }

    @Override
    public List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids) {
        return kmcDocumentMapper.getKmcDocumentListByIds(ids);
    }

    @Override
    public Long createKmcDocument(KmcDocumentSaveReqVO createReqVO) {
        KmcDocumentDO dictType = BeanUtils.toBean(createReqVO, KmcDocumentDO.class);
        kmcDocumentMapper.insert(dictType);
        redisService.leftPush("create:file", dictType.getId().toString());
        return dictType.getId();
    }

    @Override
    public int updateKmcDocument(KmcDocumentSaveReqVO updateReqVO) {
        redisService.leftPush("update:file", updateReqVO.getId().toString());
        // 相关校验

        // 更新知识文件
        KmcDocumentDO updateObj = BeanUtils.toBean(updateReqVO, KmcDocumentDO.class);
        return kmcDocumentMapper.updateById(updateObj);
    }
    @Override
    public int removeKmcDocument(Collection<Long> idList) {
        redisService.leftPushAll("delete:file", idList.stream()
                .map(String::valueOf)
                .collect(Collectors.toList()));
        // 批量删除知识文件
        return kmcDocumentMapper.deleteBatchIds(idList);
    }

    @Override
    public KmcDocumentDO getKmcDocumentById(Long id) {
        return kmcDocumentMapper.selectById(id);
    }

    @Override
    public List<KmcDocumentDO> getKmcDocumentList() {
        return kmcDocumentMapper.selectList();
    }

    @Override
    public Map<Long, KmcDocumentDO> getKmcDocumentMap() {
        List<KmcDocumentDO> kmcDocumentList = kmcDocumentMapper.selectList();
        return kmcDocumentList.stream()
                .collect(Collectors.toMap(
                        KmcDocumentDO::getId,
                        kmcDocumentDO -> kmcDocumentDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importKmcDocument(List<KmcDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KmcDocumentRespVO respVO : importExcelList) {
            try {
                KmcDocumentDO kmcDocumentDO = BeanUtils.toBean(respVO, KmcDocumentDO.class);
                Long kmcDocumentId = respVO.getId();
                if (isUpdateSupport) {
                    if (kmcDocumentId != null) {
                        KmcDocumentDO existingKmcDocument = kmcDocumentMapper.selectById(kmcDocumentId);
                        if (existingKmcDocument != null) {
                            kmcDocumentMapper.updateById(kmcDocumentDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kmcDocumentId + " 的知识文件记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kmcDocumentId + " 的知识文件记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KmcDocumentDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kmcDocumentId);
                    KmcDocumentDO existingKmcDocument = kmcDocumentMapper.selectOne(queryWrapper);
                    if (existingKmcDocument == null) {
                        kmcDocumentMapper.insert(kmcDocumentDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kmcDocumentId + " 的知识文件记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kmcDocumentId + " 的知识文件记录已存在。");
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
    public File getFileByHttpURL(String path){
        String newUrl = path.split("[?]")[0];
        String[] suffix = newUrl.split("/");
        //得到最后一个分隔符后的名字
        String fileName = suffix[suffix.length - 1];
        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            file = File.createTempFile("report",fileName,new File(storagePath));//创建临时文件
            URL urlFile = new URL(newUrl);
            inputStream = urlFile.openStream();
            outputStream = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead=inputStream.read(buffer,0,8192))!=-1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public List<KmcDocumentDO> selectList(KmcDocumentPageReqVO kmcDocument) {
        QueryWrapper<KmcDocumentDO> queryWrapper = new QueryWrapper<>();

        // 如果传入的 orderByColumn 有值，则根据该字段进行排序
        if (kmcDocument.getOrderByColumn() != null && kmcDocument.getIsAsc() != null) {
            String orderByColumn = kmcDocument.getOrderByColumn();  // 获取排序字段
            boolean isAsc = "asc".equalsIgnoreCase(kmcDocument.getIsAsc());  // 判断是升序还是降序

            // 根据传入的字段来排序
            if ("downloadCount".equalsIgnoreCase(orderByColumn)) {
                queryWrapper.orderBy(true, isAsc, "download_count");
            } else if ("previewCount".equalsIgnoreCase(orderByColumn)) {
                queryWrapper.orderBy(true, isAsc, "preview_count");
            } else {
                queryWrapper.orderByDesc("COALESCE(preview_count, 0) + COALESCE(download_count, 0)")
                        .orderByDesc("create_time"); // 如果前者相等，则按 create_time 降序排列
            }
            // 始终按照 create_time 排序，依据传入的 isAsc 排序
            queryWrapper.orderBy(true, isAsc, "create_time"); // 排序依据 create_time
        } else {
            // 如果 orderByColumn 没有值，使用 COALESCE 排序
            queryWrapper.orderByDesc("COALESCE(preview_count, 0) + COALESCE(download_count, 0)")
                    .orderByDesc("create_time"); // 如果前者相等，则按 create_time 降序排列
        }

        return kmcDocumentMapper.selectList(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getFileTypes() {
        // 1. 获取所有未删除的分类数据
        KmcCategoryDO kmcCategoryDO = new KmcCategoryDO();
        kmcCategoryDO.setDelFlag(false); // 只查询未删除的分类
        List<KmcCategoryDO> allCategories = kmcCategoryMapper.getKmcCategoryAllList(kmcCategoryDO);

        // 2. 获取文档统计信息（按分类名称分组计数）
        List<Map<String, Object>> docStats = kmcDocumentMapper.getFileTypes();
        // 将统计结果转换为Map<分类名称, 文档数量>方便后续查询
        Map<String, Integer> statsMap = docStats.stream()
                .collect(Collectors.toMap(
                        stat -> (String) stat.get("category_name"), // 分类名称作为key
                        stat -> ((Number) stat.get("count")).intValue() // 文档数量作为value
                ));

        // 3. 按父ID分组所有子分类（parentId ≠ 0的才是子分类）
        Map<Long, List<KmcCategoryDO>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != 0) // 过滤掉顶级分类（parentId=0的）
                .collect(Collectors.groupingBy(KmcCategoryDO::getParentId)); // 按parentId分组

        // 4. 准备最终返回的结果列表
        List<Map<String, Object>> result = new ArrayList<>();

        // 5. 处理所有顶级分类（parentId=0的）
        allCategories.stream()
                .filter(c -> c.getParentId() == 0) // 筛选顶级分类
                .forEach(topCategory -> {
                    // 创建当前顶级分类的节点
                    Map<String, Object> categoryNode = new HashMap<>();
                    categoryNode.put("id", topCategory.getId()); // 分类ID
                    categoryNode.put("name", topCategory.getName()); // 分类名称

                    // 递归构建子分类树，并计算总文档数
                    // 使用数组来传递引用，便于在递归中修改值
                    int[] totalCount = new int[]{statsMap.getOrDefault(topCategory.getName(), 0)};
                    List<Map<String, Object>> children = buildCategoryTree(
                            topCategory.getId(), // 当前分类ID作为父ID
                            childrenMap, // 子分类分组数据
                            statsMap, // 分类文档统计
                            totalCount // 用于累计总文档数
                    );

                    // 设置子分类列表
                    categoryNode.put("children", children);
                    // 当前分类的直接文档数（不包含子分类）
                    categoryNode.put("count", statsMap.getOrDefault(topCategory.getName(), 0));
                    // 当前分类及其所有子分类的总文档数
                    categoryNode.put("totalCount", totalCount[0]);

                    // 将当前顶级分类添加到结果列表
                    result.add(categoryNode);
                });

        return result;
    }

    /**
     * 递归构建分类树结构并累计文档总数
     * @param parentId 当前父分类ID
     * @param childrenMap 所有子分类的分组映射（按parentId分组）
     * @param statsMap 分类文档统计信息（分类名称 -> 文档数）
     * @param parentTotalCount 父分类的总文档数数组（使用数组以便在递归中修改值）
     * @return 当前父分类下的子分类树结构
     */
    private List<Map<String, Object>> buildCategoryTree(
            Long parentId,
            Map<Long, List<KmcCategoryDO>> childrenMap,
            Map<String, Integer> statsMap,
            int[] parentTotalCount
    ) {
        // 如果当前父ID没有子分类，返回空列表
        if (!childrenMap.containsKey(parentId)) {
            return new ArrayList<>();
        }

        // 处理当前父ID下的所有子分类
        return childrenMap.get(parentId).stream()
                .map(child -> {
                    // 创建当前子分类节点
                    Map<String, Object> childNode = new HashMap<>();
                    childNode.put("id", child.getId()); // 分类ID
                    childNode.put("name", child.getName()); // 分类名称

                    // 获取当前分类的直接文档数
                    int currentCount = statsMap.getOrDefault(child.getName(), 0);
                    childNode.put("count", currentCount);

                    // 递归处理子分类的子分类（孙分类）
                    // 使用数组来累计当前分类及其所有子分类的总文档数
                    int[] childTotalCount = new int[]{currentCount};
                    List<Map<String, Object>> grandchildren = buildCategoryTree(
                            child.getId(), // 当前分类作为父分类
                            childrenMap,
                            statsMap,
                            childTotalCount // 用于累计当前分类树的总文档数
                    );

                    // 设置子分类列表
                    childNode.put("children", grandchildren);
                    // 设置当前分类及其所有子分类的总文档数
                    childNode.put("totalCount", childTotalCount[0]);

                    // 将当前分类树的总文档数累加到父分类的总数中
                    parentTotalCount[0] += childTotalCount[0];

                    return childNode;
                })
                .collect(Collectors.toList());
    }
}
