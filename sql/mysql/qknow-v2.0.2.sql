/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

-- v2.0.2版本change.sql

-- ----------------------------
-- Table structure for kg_knowledge_category
-- ----------------------------
DROP TABLE IF EXISTS `kg_knowledge_category`;
CREATE TABLE `kg_knowledge_category`  (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                          `parent_id` bigint(20) NOT NULL COMMENT '父级id',
                                          `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
                                          `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
                                          `ancestors` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '祖级列表',
                                          `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                          `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                          `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                          `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                          `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                          `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                          `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of kg_knowledge_category
-- ----------------------------
INSERT INTO `kg_knowledge_category` VALUES (1, 1001, 0, '疾病与诊断', 1, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (2, 1001, 0, '治疗与干预', 2, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (3, 1001, 0, '药物与器械', 3, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (4, 1001, 0, '人体与功能', 4, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (5, 1001, 0, '人员与机构', 5, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (6, 1001, 1, '疾病实体', 1, '0,1', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (7, 1001, 1, '诊断实体', 2, '0,1', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (8, 1001, 2, '治疗方法', 1, '0,2', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (9, 1001, 2, '治疗方案', 2, '0,2', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (10, 1001, 3, '药品实体', 1, '0,3', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (11, 1001, 3, '医疗器械', 2, '0,3', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (12, 1001, 4, '解剖结构', 1, '0,4', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (13, 1001, 4, '生理功能', 2, '0,4', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (14, 1001, 5, '医疗人员', 1, '0,5', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (15, 1001, 5, '医疗机构', 2, '0,5', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);

-- ----------------------------
-- Table structure for kg_knowledge_document
-- ----------------------------
DROP TABLE IF EXISTS `kg_knowledge_document`;
CREATE TABLE `kg_knowledge_document`  (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                          `category_id` bigint(20) NOT NULL COMMENT '知识分类id',
                                          `category_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '知识分类名称',
                                          `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名称',
                                          `path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
                                          `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件描述',
                                          `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                          `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                          `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                          `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                          `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                          `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                          `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                          `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识文件表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for kg_knowledge_document_log
-- ----------------------------
DROP TABLE IF EXISTS `kg_knowledge_document_log`;
CREATE TABLE `kg_knowledge_document_log`  (
                                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                              `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                              `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
                                              `user_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
                                              `document_id` int(11) NULL DEFAULT NULL COMMENT '文件id',
                                              `document_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件名',
                                              `type` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '操作类型;0：预览，1：下载',
                                              `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                              `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                              `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                              `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                              `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                              `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                              `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件操作日志' ROW_FORMAT = DYNAMIC;

INSERT INTO system_menu VALUES (2223, '知识中心', 2132, 2, ':kgId/knowledge', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'book-open-fill', '吴同', '2025-10-20 09:43:47', '吴同', '2025-10-22 18:36:23', '');
INSERT INTO system_menu VALUES (2224, '知识分类', 2223, 1, 'category', 'kg/knowledge/category/index', NULL, '', 1, 0, 'C', '0', '0', 'kg:knowledge:category:list', '#', 'admin', '2025-10-20 09:50:56', '吴同', '2025-10-20 09:56:13', '知识分类菜单');
INSERT INTO system_menu VALUES (2225, '知识分类查询', 2224, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:query', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2226, '知识分类新增', 2224, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:add', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2227, '知识分类修改', 2224, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:edit', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2228, '知识分类删除', 2224, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:remove', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2229, '知识分类导出', 2224, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:export', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2230, '知识分类导入', 2224, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:import', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2231, '知识文件', 2223, 1, 'document', 'kg/knowledge/document/index', NULL, '', 1, 0, 'C', '0', '0', 'kg:knowledge:document:list', '#', 'admin', '2025-10-20 09:51:05', '吴同', '2025-10-20 09:56:20', '知识文件菜单');
INSERT INTO system_menu VALUES (2232, '知识文件查询', 2231, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:query', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2233, '知识文件新增', 2231, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:add', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2234, '知识文件修改', 2231, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:edit', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2235, '知识文件删除', 2231, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:remove', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2236, '知识文件导出', 2231, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:export', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2237, '知识文件导入', 2231, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:import', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');


# 2026-04-30
DROP TABLE IF EXISTS `kmc_document`;
CREATE TABLE `kmc_document`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                 `category_id` bigint(20) NOT NULL COMMENT '知识分类id',
                                 `knowledge_base_id` bigint(20) NULL DEFAULT NULL COMMENT '知识库id',
                                 `knowledge_base_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识库名称',
                                 `category_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识分类名称',
                                 `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
                                 `path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
                                 `description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件描述',
                                 `preview_count` bigint(20) NULL DEFAULT 0 COMMENT '预览次数',
                                 `download_count` bigint(20) NULL DEFAULT 0 COMMENT '下载次数',
                                 `sync_status` tinyint(4) NULL DEFAULT 0 COMMENT '同步状态',
                                 `mode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分段模式',
                                 `parent_mode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父分段的召回模式',
                                 `remove_extra_spaces` tinyint(1) NULL DEFAULT NULL COMMENT '替换连续空格、换行符、制表符',
                                 `remove_urls_emails` tinyint(1) NULL DEFAULT NULL COMMENT '删除 URL、电子邮件地址',
                                 `chunk_overlap` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分段重叠',
                                 `max_tokens` int(11) NULL DEFAULT NULL COMMENT '最大长度',
                                 `separator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义分段标识符',
                                 `subchunk_max_tokens` int(11) NULL DEFAULT NULL COMMENT '子分段最大长度',
                                 `subchunk_separator` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子分段分隔符',
                                 `doc_form` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引内容的形式',
                                 `doc_language` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '在 Q&A 模式下，指定文档的语言',
                                 `chat_model` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对话模型',
                                 `chat_model_provider` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对话模型提供商',
                                 `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                 `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                 `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                 `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                 `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                 `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '知识文件表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS kmc_sync;
CREATE TABLE kmc_sync(
                         `id` BIGINT AUTO_INCREMENT COMMENT 'ID' ,
                         `workspace_id` BIGINT NOT NULL  COMMENT '工作区id' ,
                         `document_id` BIGINT NOT NULL  COMMENT '文件id' ,
                         `qm_dataset_id` VARCHAR(128) NOT NULL  COMMENT '灵桐知识库id' ,
                         `qm_document_id` VARCHAR(128) NOT NULL  COMMENT '灵桐的文件id' ,
                         `qm_batch` VARCHAR(256) NOT NULL  COMMENT '上传批次号' ,
                         `valid_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效' ,
                         `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除' ,
                         `create_by` VARCHAR(32)   COMMENT '创建人' ,
                         `creator_id` BIGINT   COMMENT '创建人id' ,
                         `create_time` DATETIME  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
                         `update_by` VARCHAR(32)   COMMENT '更新人' ,
                         `updater_id` BIGINT   COMMENT '更新人id' ,
                         `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
                         `remark` VARCHAR(512)   COMMENT '备注' ,
                         PRIMARY KEY (id)
)  COMMENT = '文件同步表';

DROP TABLE IF EXISTS kmc_category;
CREATE TABLE kmc_category(
                             `id` BIGINT AUTO_INCREMENT COMMENT 'ID' ,
                             `workspace_id` BIGINT NOT NULL  COMMENT '工作区id' ,
                             `parent_id` BIGINT NOT NULL  COMMENT '父级id' ,
                             `knowledge_base_id` BIGINT   COMMENT '知识库id' ,
                             `name` VARCHAR(128) NOT NULL  COMMENT '分类名称' ,
                             `order_num` INT   COMMENT '显示顺序' ,
                             `ancestors` VARCHAR(128)   COMMENT '祖级列表' ,
                             `valid_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效' ,
                             `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除' ,
                             `create_by` VARCHAR(32)   COMMENT '创建人' ,
                             `creator_id` BIGINT   COMMENT '创建人id' ,
                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
                             `update_by` VARCHAR(32)   COMMENT '更新人' ,
                             `updater_id` BIGINT   COMMENT '更新人id' ,
                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
                             `remark` VARCHAR(512)   COMMENT '备注' ,
                             PRIMARY KEY (id)
)  COMMENT = '知识分类';


DROP TABLE IF EXISTS kmc_document_log;
CREATE TABLE kmc_document_log(
                                 `id` BIGINT AUTO_INCREMENT COMMENT 'ID' ,
                                 `workspace_id` BIGINT NOT NULL  COMMENT '工作区id' ,
                                 `user_id` BIGINT   COMMENT '用户id' ,
                                 `user_name` VARCHAR(256)   COMMENT '用户名' ,
                                 `document_id` INT   COMMENT '文件id' ,
                                 `document_name` VARCHAR(256)   COMMENT '文件名' ,
                                 `type` TINYINT(4) UNSIGNED   COMMENT '操作类型;0：预览，1：下载' ,
                                 `valid_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效' ,
                                 `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除' ,
                                 `create_by` VARCHAR(32)   COMMENT '创建人' ,
                                 `creator_id` BIGINT   COMMENT '创建人id' ,
                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
                                 `update_by` VARCHAR(32)   COMMENT '更新人' ,
                                 `updater_id` BIGINT   COMMENT '更新人id' ,
                                 `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
                                 `remark` VARCHAR(512)   COMMENT '备注' ,
                                 PRIMARY KEY (id)
)  COMMENT = '文件操作日志';


-- ----------------------------
-- Table structure for kmc_document_segment
-- ----------------------------
DROP TABLE IF EXISTS `kmc_document_segment`;
CREATE TABLE `kmc_document_segment`  (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         `workspace_id` bigint(20) NOT NULL COMMENT '工作空间id',
                                         `document_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名称',
                                         `document_id` bigint(20) NOT NULL COMMENT '文件id',
                                         `qm_segment_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'dify段落id',
                                         `position` int(11) NULL DEFAULT NULL COMMENT '位置',
                                         `qm_document_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'dify所属文档ID',
                                         `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '分段内容文本',
                                         `sign_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '签名内容文本',
                                         `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '答案内容(如果有)',
                                         `word_count` int(11) NULL DEFAULT NULL COMMENT '内容长度',
                                         `tokens` int(11) NULL DEFAULT NULL COMMENT 'token数量',
                                         `keywords` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '关键词',
                                         `index_node_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '索引节点ID',
                                         `index_node_hash` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '索引节点哈希值',
                                         `hit_count` int(11) NULL DEFAULT NULL COMMENT '访问次数',
                                         `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态',
                                         `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
                                         `completed_at` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '完成时间戳',
                                         `error` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '错误信息',
                                         `child_chunks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '子模块',
                                         `sync_status` tinyint(3) UNSIGNED NULL DEFAULT 0 COMMENT '分段添加dify状态;0-添加中、1-添加成功、2-添加失败',
                                         `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '父级id',
                                         `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                         `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                         `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                         `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                         `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                         `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         INDEX `idx_document_id`(`document_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件分段' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for kmc_knowledge_base
-- ----------------------------
DROP TABLE IF EXISTS `kmc_knowledge_base`;
CREATE TABLE `kmc_knowledge_base`  (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                       `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                       `qm_dataset_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '灵桐知识库id',
                                       `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
                                       `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
                                       `cover_image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图',
                                       `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签',
                                       `indexing_technique` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '索引方式',
                                       `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限',
                                       `embedding_model` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Embedding 模型名称',
                                       `embedding_model_provider` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Embedding 模型供应商',
                                       `search_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '检索方法',
                                       `reranking_enable` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '是否开启 rerank',
                                       `reranking_provider_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Rerank 模型的提供商',
                                       `reranking_model_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Rerank 模型的名称',
                                       `top_k` int(11) NULL DEFAULT NULL COMMENT '召回条数',
                                       `score_threshold_enabled` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '是否开启召回分数限制',
                                       `score_threshold` decimal(10, 1) NULL DEFAULT NULL COMMENT '召回分数限制',
                                       `reranking_mode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '混合模式下的模型选择',
                                       `keyword_weight` decimal(10, 1) NULL DEFAULT NULL COMMENT '语义',
                                       `vector_weight` decimal(10, 1) NULL DEFAULT NULL COMMENT '关键字',
                                       `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                       `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                       `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                       `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                       `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识库' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for kmc_knowledge_recall_log
-- ----------------------------
DROP TABLE IF EXISTS `kmc_knowledge_recall_log`;
CREATE TABLE `kmc_knowledge_recall_log`  (
                                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                             `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                             `knowledge_id` bigint(20) NULL DEFAULT NULL COMMENT '知识库id',
                                             `query` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '问题',
                                             `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                             `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                             `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                             `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                             `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                             `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '召回记录表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for kmc_knowledge_role
-- ----------------------------
DROP TABLE IF EXISTS `kmc_knowledge_role`;
CREATE TABLE `kmc_knowledge_role`  (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                       `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                       `knowledge_id` bigint(20) NOT NULL COMMENT '知识库id',
                                       `role_id` bigint(20) NOT NULL COMMENT '角色id',
                                       `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                       `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                       `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                       `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                       `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识库角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of kmc_knowledge_role
-- ----------------------------
INSERT INTO `kmc_knowledge_role` VALUES (1, 1001, 1, 5, 1, 0, '吴同', 2, '2026-04-24 10:01:44', '吴同', NULL, '2026-04-24 10:01:44', NULL);
INSERT INTO `kmc_knowledge_role` VALUES (2, 1001, 2, 5, 1, 0, '吴同', 2, '2026-04-24 10:01:44', '吴同', NULL, '2026-04-24 10:01:44', NULL);
INSERT INTO `kmc_knowledge_role` VALUES (3, 1001, 3, 5, 1, 0, '吴同', 2, '2026-04-24 10:01:44', '吴同', NULL, '2026-04-24 10:01:44', NULL);


INSERT INTO system_menu VALUES (2124, '知识库', 2000, 0, 'knowledgeBase', 'kmc/knowledgeBase/index', NULL, '', 1, 0, 'C', '1', '0', 'kmc:knowledgeBase:knowledgebase:list', '#', 'admin', '2025-10-14 09:05:56', '吴同', '2025-10-14 09:06:33', '知识库菜单');
INSERT INTO system_menu VALUES (2125, '知识库查询', 2124, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:query', '#', 'admin', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO system_menu VALUES (2126, '知识库新增', 2124, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:add', '#', 'admin', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO system_menu VALUES (2127, '知识库修改', 2124, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:edit', '#', 'admin', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO system_menu VALUES (2128, '知识库删除', 2124, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:remove', '#', 'admin', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO system_menu VALUES (2129, '知识库导出', 2124, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:export', '#', 'admin', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO system_menu VALUES (2130, '知识库导入', 2124, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:import', '#', 'admin', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO system_menu VALUES (2131, '召回测试', 2000, 3, ':kbId/recall', 'kmc/knowledgeBase/components/recall', NULL, 'recall', 1, 0, 'C', '0', '0', '', 'box-3-fill', '吴同', '2025-10-14 16:26:06', '吴同', '2025-10-22 18:43:34', '');



INSERT INTO system_menu VALUES (2262, '知识库设置', 2000, 4, ':kbId/knowledgeBase', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'briefcase-4-fill', '吴同', '2025-11-05 16:10:31', '吴同', '2025-11-05 17:23:16', '');
INSERT INTO system_menu VALUES (2315, '基础设置', 2262, 0, 'kmcBasic', 'kmc/knowledgeBase/components/settings', NULL, '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO system_menu VALUES (2316, '权限设置', 2262, 1, 'roles', 'kmc/knowledgeBase/components/roleTable', NULL, '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO system_menu VALUES (2317, '检索设置', 2262, 2, 'querySet', 'kmc/knowledgeBase/components/querySet', NULL, '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO system_menu VALUES (2318, '删除设置', 2262, 3, 'kmcDel', 'kmc/knowledgeBase/components/kmcDel', NULL, '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');

INSERT INTO system_job VALUES (102, '文件同步状态更新', 'DEFAULT', 'kmcSyncServiceImpl.updateResult()', '0 0/5 * * * ?', '1', '1', '0', '吴同', '2026-05-06 17:39:04', '', '2026-05-06 17:39:11', '');
