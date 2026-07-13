/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : qknow_dev

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 08/07/2026 11:46:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


CREATE DATABASE IF NOT EXISTS qknow_demo
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE qknow_demo;

-- ----------------------------

-- Table structure for ai_api_key
-- ----------------------------
DROP TABLE IF EXISTS `ai_api_key`;
CREATE TABLE `ai_api_key`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `api_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '秘钥',
  `platform` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '平台',
  `url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'API地址',
  `platform_tag` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '平台标签',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `deploy_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部署方式',
  `status` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '状态',
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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'API秘钥' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_api_key
-- ----------------------------
INSERT INTO `ai_api_key` VALUES (1, 1001, '通义千问', 'sk-797df8d943694c978bbe7ea753e07c3b', 'TongYi', NULL, '1,2,3,4,5,6', '通义千问提供的模型。', '2', 2, 1, 0, '吴同', 2, '2026-04-29 09:27:03', '吴同', NULL, '2026-05-07 15:36:00', NULL);
INSERT INTO `ai_api_key` VALUES (2, 1001, 'DeepSeek', NULL, 'DeepSeek', NULL, '1,2,3', '深度求索提供的模型，例如 deepseek-chat、deepseek-coder 。', '2', 0, 1, 1, '吴同', 2, '2026-04-29 09:27:03', '吴同', NULL, '2026-04-29 10:28:58', NULL);
INSERT INTO `ai_api_key` VALUES (3, 1001, 'Ollama', NULL, 'Ollama', NULL, '1,2,3', 'ollama', '1', 0, 1, 1, '吴同', 2, '2026-04-29 09:27:03', '吴同', NULL, '2026-04-29 10:29:04', NULL);
INSERT INTO `ai_api_key` VALUES (4, 1001, 'OpenAI', 'ldfef#01', 'OpenAI', 'http://192.144.173.87:7001', '1,2,3,4,5,6', '符合 openai 规范的模型', '1', 2, 1, 0, '吴同', 2, '2026-04-21 13:57:13', '吴同', NULL, '2026-04-21 16:33:47', NULL);

-- ----------------------------
-- Table structure for ai_model
-- ----------------------------
DROP TABLE IF EXISTS `ai_model`;
CREATE TABLE `ai_model`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
  `key_id` bigint(20) NULL DEFAULT NULL COMMENT '秘钥id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模型名称',
  `model` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模型标志',
  `platform` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '平台',
  `type` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序值',
  `status` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '状态',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1642 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI 模型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_model
-- ----------------------------
INSERT INTO `ai_model` VALUES (1160, 1001, 1, 'qwen3-tts-vd-2026-01-26', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1161, 1001, 1, 'vidu/viduq3-turbo_img2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1162, 1001, 1, 'qwen3-vl-235b-a22b-thinking', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1163, 1001, 1, 'qwen-vl-plus-2025-05-07', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1164, 1001, 1, 'pixverse/pixverse-v5.6-t2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1165, 1001, 1, 'paraformer-v2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1166, 1001, 1, 'pixverse/pixverse-c1-it2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1167, 1001, 1, 'paraformer-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1168, 1001, 1, 'qwen2.5-vl-3b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1169, 1001, 1, 'qwen-max', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1170, 1001, 1, 'qwen-mt-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1171, 1001, 1, 'qwen2.5-14b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1172, 1001, 1, 'tongyi-embedding-vision-flash', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1173, 1001, 1, 'qwen3-32b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1174, 1001, 1, 'qwen-omni-turbo-2025-03-26', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1175, 1001, 1, 'wan2.6-image', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1176, 1001, 1, 'qwen3.6-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1177, 1001, 1, 'sambert-perla-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1178, 1001, 1, 'pixverse/pixverse-v6-t2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1179, 1001, 1, 'qwen-omni-turbo-realtime-2025-05-08', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1180, 1001, 1, 'qwen-max-2025-01-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1181, 1001, 1, 'qwen3-tts-vd-realtime-2026-01-15', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1182, 1001, 1, 'emoji-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1183, 1001, 1, 'siliconflow/deepseek-v3.1-terminus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1184, 1001, 1, 'shoemodel-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1185, 1001, 1, 'qwen-tts-realtime-2025-07-15', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1186, 1001, 1, 'happyhorse-1.0-t2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1187, 1001, 1, 'wanx2.1-t2v-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1188, 1001, 1, 'qwen3-vl-flash-2025-10-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1189, 1001, 1, 'qwen3-livetranslate-flash-2025-12-01', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1190, 1001, 1, 'qwen3-omni-flash-realtime-2025-12-01', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1191, 1001, 1, 'gui-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1192, 1001, 1, 'qwen3-0.6b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1193, 1001, 1, 'fun-asr-2025-08-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1194, 1001, 1, 'qwen3-coder-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1195, 1001, 1, 'sambert-betty-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1196, 1001, 1, 'qwen3-next-80b-a3b-thinking', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1197, 1001, 1, 'qwen3-tts-flash', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1198, 1001, 1, 'qwen-math-turbo-0919', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1199, 1001, 1, 'qwen3-vl-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1200, 1001, 1, 'qwen2.5-32b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1201, 1001, 1, 'qwen-math-plus-0919', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1202, 1001, 1, 'vidu/viduq2-pro-fast_img2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1203, 1001, 1, 'kling/kling-v3-image-generation', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1204, 1001, 1, 'qwen-image-edit-max', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1205, 1001, 1, 'qwen-image-2.0-pro-2026-03-03', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1206, 1001, 1, 'qwen-plus-2025-12-01', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1207, 1001, 1, 'deepseek-r1-distill-qwen-14b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1208, 1001, 1, 'animate-anyone-template-gen2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1209, 1001, 1, 'MiniMax/speech-2.8-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1210, 1001, 1, 'qwen-math-turbo-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1211, 1001, 1, 'wan2.7-image-pro', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1212, 1001, 1, 'qvq-max-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1213, 1001, 1, 'qwen-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1214, 1001, 1, 'sambert-camila-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1215, 1001, 1, 'sambert-zhijing-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1216, 1001, 1, 'vidu/viduq3-turbo_text2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1217, 1001, 1, 'kimi-k2.5', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1218, 1001, 1, 'kimi-k2.6', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1219, 1001, 1, 'qwen2.5-vl-embedding', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1220, 1001, 1, 'qwen-coder-plus-1106', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1221, 1001, 1, 'qwen-vl-ocr-2025-11-20', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1222, 1001, 1, 'sambert-zhide-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1223, 1001, 1, 'qwen-image-edit-plus', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1224, 1001, 1, 'sambert-zhiyue-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1225, 1001, 1, 'qwen-image-edit-plus-2025-10-30', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1226, 1001, 1, 'wan2.7-i2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1227, 1001, 1, 'qwen3-tts-instruct-flash-2026-01-26', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1228, 1001, 1, 'qwen-vl-plus-0102', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1229, 1001, 1, 'pixverse/pixverse-v6-kf2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1230, 1001, 1, 'wanx2.0-t2i-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1231, 1001, 1, 'qwen3-tts-vc-realtime-2025-11-27', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1232, 1001, 1, 'fun-music-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1233, 1001, 1, 'qwen3-tts-instruct-flash-realtime', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1234, 1001, 1, 'qwen-turbo-2025-02-11', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1235, 1001, 1, 'qwen-image-edit-max-2026-01-16', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1236, 1001, 1, 'Moonshot-Kimi-K2-Instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1237, 1001, 1, 'qwen-plus-2025-09-11', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1238, 1001, 1, 'aitryon', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1239, 1001, 1, 'fun-asr', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1240, 1001, 1, 'happyhorse-1.0-r2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1241, 1001, 1, 'wanx2.1-vace-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1242, 1001, 1, 'sambert-zhigui-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1243, 1001, 1, 'qwen-vl-max', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1244, 1001, 1, 'wan2.5-t2i-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1245, 1001, 1, 'qwen3-tts-vc-realtime-2026-01-15', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1246, 1001, 1, 'wan2.2-i2v-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1247, 1001, 1, 'pixverse/pixverse-v6-it2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1248, 1001, 1, 'qwen3-vl-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1249, 1001, 1, 'wan2.2-t2i-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1250, 1001, 1, 'qwen3-vl-embedding', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1251, 1001, 1, 'deepseek-r1-distill-qwen-1.5b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1252, 1001, 1, 'qwen3-30b-a3b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1253, 1001, 1, 'qwen-mt-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1254, 1001, 1, 'qwen-coder-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1255, 1001, 1, 'kling/kling-v3-omni-image-generation', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1256, 1001, 1, 'pixverse/pixverse-c1-kf2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1257, 1001, 1, 'tongyi-embedding-vision-plus', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1258, 1001, 1, 'sambert-zhiyuan-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1259, 1001, 1, 'wan2.2-s2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1260, 1001, 1, 'qwen3-coder-flash-2025-07-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1261, 1001, 1, 'qwen3.5-omni-flash-2026-03-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1262, 1001, 1, 'qwen3-asr-flash-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1263, 1001, 1, 'qwen-image-2.0-pro', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1264, 1001, 1, 'qwen3-omni-flash-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1265, 1001, 1, 'sambert-zhiwei-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1266, 1001, 1, 'vanchin/deepseek-v3.1-terminus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1267, 1001, 1, 'qwen-omni-turbo-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1268, 1001, 1, 'qwen2.5-math-72b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1269, 1001, 1, 'deepseek-r1-0528', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1270, 1001, 1, 'deepseek-v3.2-exp', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1271, 1001, 1, 'qvq-max-2025-03-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1272, 1001, 1, 'qwen-coder-turbo-0919', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1273, 1001, 1, 'pixverse/pixverse-v5.6-it2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1274, 1001, 1, 'qwen2.5-vl-72b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1275, 1001, 1, 'sambert-zhishu-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1276, 1001, 1, 'qwen-vl-plus-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1277, 1001, 1, 'deepseek-r1-distill-qwen-7b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1278, 1001, 1, 'vidu/viduq3_reference2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1279, 1001, 1, 'emo-detect-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1280, 1001, 1, 'qwen3.6-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1281, 1001, 1, 'qwen-vl-max-2025-08-13', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1282, 1001, 1, 'deepseek-r1-distill-qwen-32b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1283, 1001, 1, 'qwen-image-edit', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1284, 1001, 1, 'paraformer-realtime-v2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1285, 1001, 1, 'paraformer-realtime-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1286, 1001, 1, 'qwen-long', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1287, 1001, 1, 'qwen-coder-plus-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1288, 1001, 1, 'qwen3-tts-instruct-flash', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1289, 1001, 1, 'wan2.2-animate-move', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1290, 1001, 1, 'sambert-zhilun-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1291, 1001, 1, 'wanx2.1-t2v-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1292, 1001, 1, 'sambert-zhimao-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1293, 1001, 1, 'qwen2.5-0.5b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1294, 1001, 1, 'qwen3-max-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1295, 1001, 1, 'vidu/viduq3-turbo_reference2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1296, 1001, 1, 'qwen3-asr-flash-2025-09-08', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1297, 1001, 1, 'qwen3-omni-30b-a3b-captioner', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1298, 1001, 1, 'sambert-zhixiao-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1299, 1001, 1, 'qwen3-omni-flash-realtime-2025-09-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1300, 1001, 1, 'gte-rerank-v2', NULL, 'TongYi', 6, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1301, 1001, 1, 'vidu/viduq2_text2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1302, 1001, 1, 'qwen2.5-coder-14b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1303, 1001, 1, 'qwen-vl-max-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1304, 1001, 1, 'pixverse/pixverse-v5.6-kf2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1305, 1001, 1, 'qwen-turbo-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1306, 1001, 1, 'MiniMax-M2.5', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1307, 1001, 1, 'qwen3-max-2025-09-23', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1308, 1001, 1, 'qwen-flash-character', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1309, 1001, 1, 'MiniMax-M2.1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1310, 1001, 1, 'qwen-image-plus', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1311, 1001, 1, 'qwen-image-plus-2026-01-09', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1312, 1001, 1, 'wanx2.1-kf2v-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1313, 1001, 1, 'wan2.6-r2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1314, 1001, 1, 'qwen3-livetranslate-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1315, 1001, 1, 'qwen3-asr-flash-filetrans', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1316, 1001, 1, 'qwen-flash-2025-07-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1317, 1001, 1, 'qwen3-omni-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1318, 1001, 1, 'fun-asr-flash-8k-realtime-2026-01-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1319, 1001, 1, 'vanchin/deepseek-r1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1320, 1001, 1, 'qwen3.5-plus-2026-04-20', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1321, 1001, 1, 'sambert-cally-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1322, 1001, 1, 'qwen-long-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1323, 1001, 1, 'sambert-zhishuo-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1324, 1001, 1, 'qwen-vl-ocr-2025-04-13', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1325, 1001, 1, 'qwen3-max', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1326, 1001, 1, 'facechain-generation', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1327, 1001, 1, 'qwen3.5-plus-2026-02-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1328, 1001, 1, 'qvq-plus-2025-05-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1329, 1001, 1, 'liveportrait-detect', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1330, 1001, 1, 'MiniMax/speech-2.8-hd', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1331, 1001, 1, 'sambert-zhiya-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1332, 1001, 1, 'qwen-doc-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1333, 1001, 1, 'liveportrait', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1334, 1001, 1, 'text-embedding-v1', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1335, 1001, 1, 'text-embedding-v3', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1336, 1001, 1, 'qwen3-vl-plus-2025-09-23', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1337, 1001, 1, 'text-embedding-v2', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1338, 1001, 1, 'qwen3-coder-next', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1339, 1001, 1, 'text-embedding-v4', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1340, 1001, 1, 'sambert-zhistella-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1341, 1001, 1, 'sambert-zhihao-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1342, 1001, 1, 'deepseek-v4-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1343, 1001, 1, 'qwen-image-max-2025-12-30', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1344, 1001, 1, 'qwq-32b-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1345, 1001, 1, 'sambert-zhichu-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1346, 1001, 1, 'tongyi-embedding-vision-flash-2026-03-06', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1347, 1001, 1, 'wanx2.1-t2i-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1348, 1001, 1, 'qwen3-asr-flash-filetrans-2025-11-17', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1349, 1001, 1, 'qwen3-vl-flash-2026-01-22', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1350, 1001, 1, 'kling/kling-v3-omni-video-generation', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1351, 1001, 1, 'qwen3-vl-235b-a22b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1352, 1001, 1, 'wanx2.1-i2v-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1353, 1001, 1, 'qwen-mt-lite', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1354, 1001, 1, 'qwen-plus-2025-01-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1355, 1001, 1, 'wan2.7-i2v-2026-04-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1356, 1001, 1, 'qwen3-1.7b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1357, 1001, 1, 'qwen-max-0919', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1358, 1001, 1, 'wan2.2-s2v-detect', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1359, 1001, 1, 'image-out-painting', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1360, 1001, 1, 'qwen3-omni-flash-2025-09-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1361, 1001, 1, 'sambert-zhijia-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1362, 1001, 1, 'emoji-detect-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1363, 1001, 1, 'sambert-clara-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1364, 1001, 1, 'animate-anyone-detect-gen2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1365, 1001, 1, 'image-erase-completion', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1366, 1001, 1, 'sambert-waan-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1367, 1001, 1, 'fun-asr-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1368, 1001, 1, 'virtualmodel-v2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1369, 1001, 1, 'qwen-image-2.0-pro-2026-04-22', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1370, 1001, 1, 'wan2.7-image', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1371, 1001, 1, 'vidu/viduq2-pro_img2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1372, 1001, 1, 'tongyi-embedding-vision-plus-2026-03-06', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1373, 1001, 1, 'qwen-math-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1374, 1001, 1, 'wanx-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1375, 1001, 1, 'qwen2.5-math-7b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1376, 1001, 1, 'qwen3-vl-32b-thinking', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1377, 1001, 1, 'fun-asr-mtl', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1378, 1001, 1, 'qwen-omni-turbo-realtime-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1379, 1001, 1, 'glm-5', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1380, 1001, 1, 'sambert-eva-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1381, 1001, 1, 'qwen3-vl-30b-a3b-thinking', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1382, 1001, 1, 'qwen3-livetranslate-flash-realtime-2025-09-22', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1383, 1001, 1, 'siliconflow/deepseek-r1-0528', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1384, 1001, 1, 'qwen-vl-ocr-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1385, 1001, 1, 'qwen2.5-7b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1386, 1001, 1, 'sambert-indah-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1387, 1001, 1, 'sambert-cindy-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1388, 1001, 1, 'qwen-vl-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1389, 1001, 1, 'vidu/viduq3-pro_start-end2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1390, 1001, 1, 'wan2.2-t2v-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1391, 1001, 1, 'wan2.7-t2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1392, 1001, 1, 'qwen3.5-35b-a3b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1393, 1001, 1, 'glm-4.5-air', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1394, 1001, 1, 'wanx2.1-i2v-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1395, 1001, 1, 'qwen3-coder-480b-a35b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1396, 1001, 1, 'qwen3-omni-flash-2025-12-01', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1397, 1001, 1, 'wordart-texture', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1398, 1001, 1, 'vidu/viduq3-pro_text2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1399, 1001, 1, 'kling/kling-v3-video-generation', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1400, 1001, 1, 'qwen2.5-14b-instruct-1m', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1401, 1001, 1, 'qwen-plus-0112', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1402, 1001, 1, 'qwen-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1403, 1001, 1, 'qwen-math-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1404, 1001, 1, 'qwen-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1405, 1001, 1, 'voice-enrollment', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1406, 1001, 1, 'qvq-max', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1407, 1001, 1, 'paraformer-8k-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1408, 1001, 1, 'paraformer-8k-v2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1409, 1001, 1, 'vidu/viduq2-pro_reference2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1410, 1001, 1, 'qwen3-tts-vc-2026-01-22', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1411, 1001, 1, 'tongyi-xiaomi-analysis-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1412, 1001, 1, 'deepseek-r1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1413, 1001, 1, 'cosyvoice-v2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1414, 1001, 1, 'cosyvoice-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1415, 1001, 1, 'qwen-plus-character', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1416, 1001, 1, 'qwen-tts-realtime', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1417, 1001, 1, 'animate-anyone-gen2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1418, 1001, 1, 'paraformer-mtl-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1419, 1001, 1, 'sambert-zhifei-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1420, 1001, 1, 'fun-asr-flash-8k-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1421, 1001, 1, 'vidu/viduq3-pro_img2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1422, 1001, 1, 'qwen-max-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1423, 1001, 1, 'MiniMax/speech-02-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1424, 1001, 1, 'wan2.6-i2v-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1425, 1001, 1, 'speech-biasing', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1426, 1001, 1, 'qwen2.5-coder-32b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1427, 1001, 1, 'qwen-max-0428', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1428, 1001, 1, 'qwen-plus-1220', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1429, 1001, 1, 'qwen-voice-design', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1430, 1001, 1, 'kimi/kimi-k2.5', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1431, 1001, 1, 'kimi/kimi-k2.6', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1432, 1001, 1, 'qwen3.5-122b-a10b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1433, 1001, 1, 'qwen2.5-7b-instruct-1m', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1434, 1001, 1, 'qwen3-tts-flash-2025-09-18', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1435, 1001, 1, 'siliconflow/deepseek-v3.2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1436, 1001, 1, 'glm-5.1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1437, 1001, 1, 'sambert-beth-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1438, 1001, 1, 'kimi-k2-thinking', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1439, 1001, 1, 'qwen3.6-max-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1440, 1001, 1, 'qwen2.5-omni-7b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1441, 1001, 1, 'deepseek-v3.1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1442, 1001, 1, 'qwen3.5-397b-a17b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1443, 1001, 1, 'wan2.6-t2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1444, 1001, 1, 'deepseek-v3.2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1445, 1001, 1, 'qwen-image', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1446, 1001, 1, 'tongyi-xiaomi-analysis-pro', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1447, 1001, 1, 'qwen3.5-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1448, 1001, 1, 'qwen3.5-omni-plus-2026-03-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1449, 1001, 1, 'wanx-x-painting', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1450, 1001, 1, 'wanx2.1-imageedit', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1451, 1001, 1, 'qwen-image-2.0', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1452, 1001, 1, 'wan2.7-r2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1453, 1001, 1, 'qwen3-30b-a3b-thinking-2507', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1454, 1001, 1, 'qvq-max-2025-05-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1455, 1001, 1, 'qwen2.5-72b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1456, 1001, 1, 'siliconflow/deepseek-v3-0324', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1457, 1001, 1, 'aitryon-parsing-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1458, 1001, 1, 'qwen3-livetranslate-flash-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1459, 1001, 1, 'vidu/viduq2-turbo_img2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1460, 1001, 1, 'qwen-turbo-1101', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1461, 1001, 1, 'qwen3-rerank', NULL, 'TongYi', 6, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1462, 1001, 1, 'qwen-deep-research', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1463, 1001, 1, 'MiniMax/speech-02-hd', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1464, 1001, 1, 'farui-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1465, 1001, 1, 'qwen3-coder-30b-a3b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1466, 1001, 1, 'qwen3.6-27b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1467, 1001, 1, 'qwen3-235b-a22b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1468, 1001, 1, 'cosyvoice-v3-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1469, 1001, 1, 'sambert-zhiming-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1470, 1001, 1, 'sambert-brian-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1471, 1001, 1, 'qwen-vl-plus-2025-01-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1472, 1001, 1, 'qwen3.6-flash-2026-04-16', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1473, 1001, 1, 'wan2.5-i2v-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1474, 1001, 1, 'qwen-turbo-2025-07-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1475, 1001, 1, 'wan2.2-t2i-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1476, 1001, 1, 'sambert-donna-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1477, 1001, 1, 'wan2.2-i2v-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1478, 1001, 1, 'qwen2.5-vl-32b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1479, 1001, 1, 'glm-4.5', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1480, 1001, 1, 'glm-4.6', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1481, 1001, 1, 'glm-4.7', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1482, 1001, 1, 'qwen3.5-omni-flash-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1483, 1001, 1, 'sambert-zhixiang-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1484, 1001, 1, 'qwen3-asr-flash-realtime-2025-10-27', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1485, 1001, 1, 'qwq-32b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1486, 1001, 1, 'deepseek-v3', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1487, 1001, 1, 'gui-plus-2026-02-26', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1488, 1001, 1, 'qwen3-tts-vd-realtime-2025-12-16', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1489, 1001, 1, 'qwen3-vl-plus-2025-12-19', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1490, 1001, 1, 'MiniMax/MiniMax-M2.1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1491, 1001, 1, 'qwen-plus-2025-04-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1492, 1001, 1, 'qwen-mt-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1493, 1001, 1, 'gummy-chat-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1494, 1001, 1, 'MiniMax/MiniMax-M2.5', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1495, 1001, 1, 'qwen-vl-max-2025-01-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1496, 1001, 1, 'qwen3.5-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1497, 1001, 1, 'wanx-poster-generation-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1498, 1001, 1, 'vidu/viduq3-turbo_start-end2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1499, 1001, 1, 'wan2.6-t2i', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1500, 1001, 1, 'qwen-math-plus-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1501, 1001, 1, 'qvq-72b-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1502, 1001, 1, 'vanchin/deepseek-v3', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1503, 1001, 1, 'qwen3.6-plus-2026-04-02', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1504, 1001, 1, 'fun-asr-realtime-2026-02-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1505, 1001, 1, 'wanx-background-generation-v2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1506, 1001, 1, 'image-instance-segmentation', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1507, 1001, 1, 'wan2.6-r2v-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1508, 1001, 1, 'happyhorse-1.0-i2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1509, 1001, 1, 'MiniMax/MiniMax-M2.7', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1510, 1001, 1, 'cosyvoice-v3.5-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1511, 1001, 1, 'Tripo/Tripo-H3.1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1512, 1001, 1, 'sambert-zhina-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1513, 1001, 1, 'cosyvoice-v3.5-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1514, 1001, 1, 'gummy-realtime-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1515, 1001, 1, 'sambert-zhida-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1516, 1001, 1, 'qwen-plus-2025-07-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1517, 1001, 1, 'paraformer-realtime-8k-v2', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1518, 1001, 1, 'paraformer-realtime-8k-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1519, 1001, 1, 'qwen-omni-turbo', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1520, 1001, 1, 'sambert-zhiye-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1521, 1001, 1, 'qwen-tts-2025-05-22', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1522, 1001, 1, 'qwen-tts-realtime-latest', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1523, 1001, 1, 'qwen3.5-omni-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1524, 1001, 1, 'fun-asr-2025-11-07', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1525, 1001, 1, 'fun-asr-realtime-2025-09-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1526, 1001, 1, 'wordart-semantic', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1527, 1001, 1, 'sambert-zhiying-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1528, 1001, 1, 'z-image-turbo', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1529, 1001, 1, 'wan2.6-i2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1530, 1001, 1, 'qwen-vl-plus-2025-07-10', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1531, 1001, 1, 'wan2.5-t2v-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1532, 1001, 1, 'qwen-omni-turbo-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1533, 1001, 1, 'qwen3-coder-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1534, 1001, 1, 'qwen3-vl-8b-thinking', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1535, 1001, 1, 'qwen-tts-latest', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1536, 1001, 1, 'qwen2.5-1.5b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1537, 1001, 1, 'qwen3.5-flash-2026-02-23', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1538, 1001, 1, 'qwen-vl-ocr-1028', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1539, 1001, 1, 'qwen3-8b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1540, 1001, 1, 'qwen3-asr-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1541, 1001, 1, 'qwen-omni-turbo-2025-01-19', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1542, 1001, 1, 'qwen-mt-image', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1543, 1001, 1, 'sambert-zhimo-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1544, 1001, 1, 'happyhorse-1.0-video-edit', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1545, 1001, 1, 'qwen-vl-plus-2025-08-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1546, 1001, 1, 'pixverse/pixverse-c1-t2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1547, 1001, 1, 'sambert-zhiru-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1548, 1001, 1, 'qwen3.5-27b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1549, 1001, 1, 'qwen3.5-omni-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1550, 1001, 1, 'qwen3-tts-flash-realtime-2025-09-18', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1551, 1001, 1, 'qvq-plus-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1552, 1001, 1, 'sambert-zhiqi-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1553, 1001, 1, 'aitryon-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1554, 1001, 1, 'vidu/viduq2-pro_start-end2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1555, 1001, 1, 'qwen3-14b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1556, 1001, 1, 'qwen3-tts-flash-2025-11-27', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1557, 1001, 1, 'qwen-image-max', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1558, 1001, 1, 'deepseek-v4-pro', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1559, 1001, 1, 'wan2.5-i2i-preview', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1560, 1001, 1, 'qwen-deep-research-2025-12-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1561, 1001, 1, 'qwen3-30b-a3b-instruct-2507', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1562, 1001, 1, 'fun-asr-mtl-2025-08-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1563, 1001, 1, 'wan2.2-animate-mix', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1564, 1001, 1, 'qwen3-235b-a22b-instruct-2507', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1565, 1001, 1, 'qwen3-coder-plus-2025-07-22', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1566, 1001, 1, 'qwen-image-edit-plus-2025-12-15', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1567, 1001, 1, 'wan2.7-t2v-2026-04-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1568, 1001, 1, 'qwen-vl-ocr', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1569, 1001, 1, 'sambert-zhimiao-emo-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1570, 1001, 1, 'Tripo/Tripo-P1.0', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1571, 1001, 1, 'qwen-vl-max-2025-04-02', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1572, 1001, 1, 'qwen-vl-max-1119', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1573, 1001, 1, 'qwen-vl-max-2025-04-08', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1574, 1001, 1, 'qwen3-tts-flash-realtime', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1575, 1001, 1, 'qwen-voice-enrollment', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1576, 1001, 1, 'tongyi-intent-detect-v3', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1577, 1001, 1, 'wan2.7-videoedit', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1578, 1001, 1, 'qwen-vl-max-1230', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1579, 1001, 1, 'vidu/viduq3-mix_reference2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1580, 1001, 1, 'qwen3-235b-a22b-thinking-2507', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1581, 1001, 1, 'qwen2.5-vl-7b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1582, 1001, 1, 'qwq-plus-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1583, 1001, 1, 'multimodal-embedding-v1', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1584, 1001, 1, 'qwen-tts-2025-04-10', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1585, 1001, 1, 'wanx-sketch-to-image-lite', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1586, 1001, 1, 'pixverse/pixverse-v5.6-r2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1587, 1001, 1, 'wanx-virtualmodel', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1588, 1001, 1, 'qwen-tts', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1589, 1001, 1, 'qwen-math-plus-0816', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1590, 1001, 1, 'vidu/viduq2-turbo_start-end2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1591, 1001, 1, 'qwen3-vl-32b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1592, 1001, 1, 'qwen3-tts-instruct-flash-realtime-2026-01-22', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1593, 1001, 1, 'vidu/viduq2_reference2video', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1594, 1001, 1, 'videoretalk', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1595, 1001, 1, 'qwen3-coder-plus-2025-09-23', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1596, 1001, 1, 'qwen2.5-3b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1597, 1001, 1, 'qwen3.5-omni-flash-realtime-2026-03-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1598, 1001, 1, 'qwen-plus-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1599, 1001, 1, 'qwen3-max-2026-01-23', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1600, 1001, 1, 'wanx-style-repaint-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1601, 1001, 1, 'wan2.2-kf2v-flash', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1602, 1001, 1, 'qwen3.5-omni-plus-realtime', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1603, 1001, 1, 'qwen3-vl-30b-a3b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1604, 1001, 1, 'aitryon-refiner', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1605, 1001, 1, 'qwen-flash-character-2026-02-26', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1606, 1001, 1, 'sambert-zhinan-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1607, 1001, 1, 'qwen3-4b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1608, 1001, 1, 'text-embedding-async-v1', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1609, 1001, 1, 'text-embedding-async-v2', NULL, 'TongYi', 5, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1610, 1001, 1, 'qwen2.5-coder-7b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1611, 1001, 1, 'qwen-coder-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1612, 1001, 1, 'pixverse/pixverse-c1-r2v', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1613, 1001, 1, 'fun-asr-realtime-2025-11-07', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1614, 1001, 1, 'qwen3-vl-rerank', NULL, 'TongYi', 6, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1615, 1001, 1, 'wanx2.1-t2i-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1616, 1001, 1, 'emo-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1617, 1001, 1, 'sambert-hanna-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1618, 1001, 1, 'qwen3-asr-flash-realtime-2026-02-10', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1619, 1001, 1, 'qwen-coder-turbo-latest', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1620, 1001, 1, 'qwen-turbo-2025-04-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1621, 1001, 1, 'qwen-image-2.0-2026-03-03', NULL, 'TongYi', 2, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1622, 1001, 1, 'sambert-zhiting-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1623, 1001, 1, 'qwen3-vl-8b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1624, 1001, 1, 'qwen-vl-ocr-2025-08-28', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1625, 1001, 1, 'qvq-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1626, 1001, 1, 'qwen3.5-omni-plus-realtime-2026-03-15', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1627, 1001, 1, 'cosyvoice-v3-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1628, 1001, 1, 'qwen3-asr-flash-2026-02-10', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1629, 1001, 1, 'video-style-transform', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1630, 1001, 1, 'qwen-long-2025-01-25', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1631, 1001, 1, 'qwen3.6-35b-a3b', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1632, 1001, 1, 'vanchin/deepseek-ocr', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1633, 1001, 1, 'qwen-plus-2025-07-14', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1634, 1001, 1, 'qwq-plus', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1635, 1001, 1, 'qwen3-tts-flash-realtime-2025-11-27', NULL, 'TongYi', 3, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1636, 1001, 1, 'facechain-facedetect', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1637, 1001, 1, 'qwq-plus-2025-03-05', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1638, 1001, 1, 'qwen3-next-80b-a3b-instruct', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1639, 1001, 1, 'cosyvoice-clone-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1640, 1001, 1, 'sambert-zhiqian-v1', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);
INSERT INTO `ai_model` VALUES (1641, 1001, 1, 'vanchin/deepseek-v3.2-think', NULL, 'TongYi', 1, NULL, 1, 1, 0, '吴同', 2, '2026-05-08 17:21:46', '吴同', 2, '2026-05-08 17:21:46', NULL);

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint(20) NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', '吴同', '2026-04-20 09:32:54', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `system_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', '吴同', '2026-04-20 09:32:54', '', NULL, '初始化密码 123456');
INSERT INTO `system_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', '吴同', '2026-04-20 09:32:54', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `system_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', '吴同', '2026-04-20 09:32:54', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `system_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', '吴同', '2026-04-20 09:32:54', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `system_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', '吴同', '2026-04-20 09:32:54', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `system_config` VALUES (7, '非结构化抽取线程数量', 'ext.thread.concurrency', '3', 'Y', '吴同', '2026-04-20 09:36:58', '', NULL, '最大50');

-- ----------------------------
-- Table structure for system_content
-- ----------------------------
DROP TABLE IF EXISTS `system_content`;
CREATE TABLE `system_content`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sys_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '系统名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '系统logo',
  `login_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录页面logo',
  `carousel_image` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '轮播图',
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `copyright` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '版权方',
  `record_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备案号',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '删除标记',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `updater_id` int(11) NULL DEFAULT NULL COMMENT '修改人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_content
-- ----------------------------
INSERT INTO `system_content` VALUES (1, NULL, '', '', '', '400-660-8208', 'support@qiantong.tech', 'Copyright© 2025 江苏千桐科技有限公司 版权所有', '苏ICP备2022008519号-3', 0, NULL, NULL, NULL, NULL, '吴同', 2, '2025-01-13 13:18:06', NULL);

-- ----------------------------
-- Table structure for system_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 203 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dept
-- ----------------------------
INSERT INTO `system_dept` VALUES (100, 0, '0', '千桐科技', 0, '唐朝辉', '15888888888', 'support@qiantong.tech', '0', '0', '吴同', '2026-04-20 09:32:53', '', NULL);
INSERT INTO `system_dept` VALUES (200, 100, '0,100', '合作伙伴', 3, NULL, NULL, NULL, '0', '0', '吴同', '2026-06-03 11:11:18', '', NULL);
INSERT INTO `system_dept` VALUES (201, 200, '0,100,200', '研发部门', 1, NULL, NULL, NULL, '0', '0', '吴同', '2026-06-03 11:11:31', '', NULL);
INSERT INTO `system_dept` VALUES (202, 200, '0,100,200', '市场部门', 2, NULL, NULL, NULL, '0', '0', '吴同', '2026-06-03 11:11:40', '', NULL);

-- ----------------------------
-- Table structure for system_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_data`;
CREATE TABLE `system_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(11) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 182 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict_data
-- ----------------------------
INSERT INTO `system_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '性别男');
INSERT INTO `system_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '性别女');
INSERT INTO `system_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '性别未知');
INSERT INTO `system_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '显示菜单');
INSERT INTO `system_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '隐藏菜单');
INSERT INTO `system_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '停用状态');
INSERT INTO `system_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '停用状态');
INSERT INTO `system_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '默认分组');
INSERT INTO `system_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '系统分组');
INSERT INTO `system_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '系统默认是');
INSERT INTO `system_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '系统默认否');
INSERT INTO `system_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '通知');
INSERT INTO `system_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '公告');
INSERT INTO `system_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '关闭状态');
INSERT INTO `system_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '其他操作');
INSERT INTO `system_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '新增操作');
INSERT INTO `system_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '修改操作');
INSERT INTO `system_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '删除操作');
INSERT INTO `system_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '授权操作');
INSERT INTO `system_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '导出操作');
INSERT INTO `system_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '导入操作');
INSERT INTO `system_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '强退操作');
INSERT INTO `system_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '生成操作');
INSERT INTO `system_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '清空操作');
INSERT INTO `system_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '停用状态');
INSERT INTO `system_dict_data` VALUES (30, 0, '文本', '0', 'ext_data_type', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '文本类型');
INSERT INTO `system_dict_data` VALUES (31, 1, '整数', '1', 'ext_data_type', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '整数类型');
INSERT INTO `system_dict_data` VALUES (32, 2, '小数', '2', 'ext_data_type', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '小数类型');
INSERT INTO `system_dict_data` VALUES (33, 3, '时间', '3', 'ext_data_type', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '时间类型');
INSERT INTO `system_dict_data` VALUES (34, 4, '字节类型', '4', 'ext_data_type', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '字节类型');
INSERT INTO `system_dict_data` VALUES (35, 5, '布尔值', '5', 'ext_data_type', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '布尔值类型');
INSERT INTO `system_dict_data` VALUES (36, 0, '唯一性校验', '0', 'ext_data_check', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '唯一性校验');
INSERT INTO `system_dict_data` VALUES (37, 1, '长度校验', '1', 'ext_data_check', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '长度校验');
INSERT INTO `system_dict_data` VALUES (38, 2, '区间校验', '2', 'ext_data_check', '', 'default', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '区间校验');
INSERT INTO `system_dict_data` VALUES (39, 0, '未发布', '0', 'publish_status', '', 'warning', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '未发布状态');
INSERT INTO `system_dict_data` VALUES (40, 1, '已发布', '1', 'publish_status', '', 'success', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '已发布状态');
INSERT INTO `system_dict_data` VALUES (41, 0, '未执行', '0', 'ext_task_status', '', 'primary', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '未执行状态');
INSERT INTO `system_dict_data` VALUES (42, 1, '进行中', '1', 'ext_task_status', '', 'warning', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '进行中状态');
INSERT INTO `system_dict_data` VALUES (43, 2, '已完成', '2', 'ext_task_status', '', 'success', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '已完成状态');
INSERT INTO `system_dict_data` VALUES (44, 3, '执行失败', '3', 'ext_task_status', '', 'danger', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '执行失败状态');
INSERT INTO `system_dict_data` VALUES (45, 1, 'MySql', 'MySql', 'datasource_type', '', 'primary', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, 'MySql数据库');
INSERT INTO `system_dict_data` VALUES (47, 3, 'Oracle', 'Oracle', 'datasource_type', '', 'primary', 'N', '0', '吴同', '2026-04-20 09:32:54', '吴同', '2026-05-09 16:18:03', 'Oracle数据库');
INSERT INTO `system_dict_data` VALUES (50, 0, '未映射', '0', 'ext_mapping_status', NULL, 'warning', 'N', '0', '吴同', '2026-04-20 09:32:54', '吴同', '2026-04-20 09:32:54', NULL);
INSERT INTO `system_dict_data` VALUES (51, 1, '已映射', '1', 'ext_mapping_status', NULL, 'success', 'N', '0', '吴同', '2026-04-20 09:32:54', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (52, 1, '失败', '0', 'ext_log_status', NULL, 'danger', 'N', '0', '吴同', '2026-04-20 09:36:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (53, 2, '成功', '1', 'ext_log_status', NULL, 'success', 'N', '0', '吴同', '2026-04-20 09:36:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (54, 1, '结构化抽取', '0', 'ext_task_type', NULL, 'default', 'N', '0', '吴同', '2026-04-20 09:36:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (55, 2, '非结构化抽取', '1', 'ext_task_type', NULL, 'default', 'N', '0', '吴同', '2026-04-20 09:36:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (56, 1, '队列中', '4', 'ext_task_status', NULL, 'info', 'N', '0', '吴同', '2026-04-20 09:36:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (93, 0, '待解析', '0', 'document_sync_status', NULL, 'info', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (94, 0, '解析中', '1', 'document_sync_status', NULL, 'warning', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (95, 0, '解析成功', '2', 'document_sync_status', NULL, 'success', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (96, 0, '解析失败', '3', 'document_sync_status', NULL, 'danger', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (97, 0, '高质量', 'high_quality', 'kmc_know_index', 'high_quality', 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (98, 1, '经济', 'economy', 'kmc_know_index', 'economy', 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (99, 0, '禁用', 'false', 'kmc_know_valid', 'disabled', 'default', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (117, 1, '否', '0', 'sys_is_or_not', NULL, 'primary', 'N', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-06-08 11:58:57', NULL);
INSERT INTO `system_dict_data` VALUES (118, 0, '是', '1', 'sys_is_or_not', NULL, 'danger', 'Y', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-06-08 11:59:08', NULL);
INSERT INTO `system_dict_data` VALUES (122, 2, '全量更新', '0', 'ext_update_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (123, 2, '增量更新', '1', 'ext_update_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (151, 1, '本地部署', '1', 'ai_deploy_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (152, 2, 'API接入（开放平台）', '2', 'ai_deploy_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (153, 1, 'DeepSeek', 'DeepSeek', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (154, 1, 'CHAT', '1', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (155, 2, 'IMAGE', '2', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (156, 3, 'VOICE', '3', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (157, 4, 'VIDEO', '4', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (158, 5, 'EMBEDDING', '5', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (159, 6, 'RERANK', '6', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (160, 1, '未配置', '0', 'ai_apikey_status', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (161, 2, '已配置', '1', 'ai_apikey_status', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (162, 3, '通义千问', 'TongYi', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-04-24 10:01:38', NULL);
INSERT INTO `system_dict_data` VALUES (163, 2, 'Ollama', 'Ollama', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-04-24 10:01:38', NULL);
INSERT INTO `system_dict_data` VALUES (164, 3, '已配置', '2', 'ai_apikey_status', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-04-24 10:01:38', NULL);
INSERT INTO `system_dict_data` VALUES (172, 4, 'OpenAI', 'OpenAI', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-21 13:57:05', '吴同', '2026-04-21 13:57:05', NULL);
INSERT INTO `system_dict_data` VALUES (173, 1, '启用', 'true', 'kmc_know_valid', 'enabled', 'default', 'N', '0', '吴同', '2026-04-21 13:57:05', '吴同', '2026-05-07 14:14:41', NULL);
INSERT INTO `system_dict_data` VALUES (175, 1, '工作流', '0', 'kg_bot_type', NULL, 'primary', 'N', '0', '吴同', '2026-05-07 16:47:34', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (176, 2, 'Chatflow', '1', 'kg_bot_type', NULL, 'primary', 'N', '0', '吴同', '2026-05-07 16:47:34', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (177, 3, 'Agent', '2', 'kg_bot_type', NULL, 'primary', 'N', '0', '吴同', '2026-05-07 16:47:34', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (178, 0, '结构化', '0', 'ext_task_log_type', NULL, 'primary', 'N', '0', '吴同', '2026-05-14 11:08:45', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (179, 1, '非结构化', '1', 'ext_task_log_type', NULL, 'primary', 'N', '0', '吴同', '2026-05-14 11:08:55', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (180, 0, '停用', '0', 'kac_horizontal_status', NULL, 'warning', 'N', '0', '吴同', '2026-04-23 19:35:48', '吴同', '2026-04-23 19:36:17', NULL);
INSERT INTO `system_dict_data` VALUES (181, 1, '正常', '1', 'kac_horizontal_status', NULL, 'primary', 'N', '0', '吴同', '2026-04-23 19:36:08', '', NULL, NULL);

-- ----------------------------
-- Table structure for system_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_type`;
CREATE TABLE `system_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict_type
-- ----------------------------
INSERT INTO `system_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '用户性别列表');
INSERT INTO `system_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '菜单状态列表');
INSERT INTO `system_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '系统开关列表');
INSERT INTO `system_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '任务状态列表');
INSERT INTO `system_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '任务分组列表');
INSERT INTO `system_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '系统是否列表');
INSERT INTO `system_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '通知类型列表');
INSERT INTO `system_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '通知状态列表');
INSERT INTO `system_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '操作类型列表');
INSERT INTO `system_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '登录状态列表');
INSERT INTO `system_dict_type` VALUES (11, '数据类型', 'ext_data_type', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '数据类型列表');
INSERT INTO `system_dict_type` VALUES (12, '数据校验', 'ext_data_check', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '数据校验列表');
INSERT INTO `system_dict_type` VALUES (13, '发布状态', 'publish_status', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '发布状态列表');
INSERT INTO `system_dict_type` VALUES (14, '任务执行状态', 'ext_task_status', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '任务执行状态列表');
INSERT INTO `system_dict_type` VALUES (15, '数据源类型', 'datasource_type', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '数据源类型列表');
INSERT INTO `system_dict_type` VALUES (16, '导入表映射状态', 'ext_mapping_status', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '导入表映射状态');
INSERT INTO `system_dict_type` VALUES (17, '知识抽取日志状态', 'ext_log_status', '0', '吴同', '2026-04-20 09:36:58', '', NULL, '知识抽取日志状态');
INSERT INTO `system_dict_type` VALUES (18, '知识抽取任务类型', 'ext_task_type', '0', '吴同', '2026-04-20 09:36:58', '', NULL, '知识抽取任务类型');
INSERT INTO `system_dict_type` VALUES (30, '文档同步状态', 'document_sync_status', '0', '吴同', '2026-04-21 13:57:05', '', NULL, '文档预览下载埋点类型');
INSERT INTO `system_dict_type` VALUES (31, '索引方式', 'kmc_know_index', '0', '吴同', '2026-04-24 10:01:38', '', NULL, '索引方式列表');
INSERT INTO `system_dict_type` VALUES (32, '知识库启用状态', 'kmc_know_valid', '0', '吴同', '2026-04-21 13:57:05', '', NULL, '知识库启用状态');
INSERT INTO `system_dict_type` VALUES (37, '是否类型', 'sys_is_or_not', '0', '吴同', '2026-04-24 10:01:38', '', NULL, '是否选择列表');
INSERT INTO `system_dict_type` VALUES (39, '结构化抽取更新类型', 'ext_update_type', '0', '吴同', '2026-04-24 10:01:38', '', NULL, '结构化抽取更新类型列表');
INSERT INTO `system_dict_type` VALUES (51, 'ai apikey状态', 'ai_apikey_status', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (52, 'ai模型提供平台', 'ai_model_platform', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (53, 'ai平台部署方式', 'ai_deploy_type', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (54, 'ai模型标签', 'ai_model_tag', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (56, 'Bot 类型', 'kg_bot_type', '0', '吴同', '2026-05-07 16:47:34', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (57, '应用插件状态', 'kac_horizontal_status', '0', '吴同', '2026-04-23 19:35:19', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (100, '抽取日志类型', 'ext_task_log_type', '0', '吴同', '2026-05-14 11:08:02', '', NULL, NULL);

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_job
-- ----------------------------
INSERT INTO `system_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_job` VALUES (100, '结构化任务抽取', 'DEFAULT', 'extStructTaskServiceImpl.consumeQueue()', '0 0/1 * * * ?', '1', '1', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_job` VALUES (101, '非结构化任务抽取', 'DEFAULT', 'extUnstructTaskServiceImpl.consumeQueue()', '0 0/5 * * * ?', '1', '1', '0', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_job` VALUES (102, '文件同步状态更新', 'DEFAULT', 'kmcSyncServiceImpl.updateResult()', '0 0/5 * * * ?', '1', '1', '0', '吴同', '2026-05-06 17:39:04', '', '2026-05-06 17:39:11', '');

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int(11) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(11) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2473 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu` VALUES (1, '系统管理', 0, 6, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', '系统中心', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-14 10:50:01', '系统管理目录');
INSERT INTO `system_menu` VALUES (2, '系统监控', 0, 7, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'airplay-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 11:05:08', '系统监控目录');
INSERT INTO `system_menu` VALUES (3, '系统工具', 0, 8, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'centos-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 10:57:19', '系统工具目录');
INSERT INTO `system_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-14 11:08:39', '用户管理菜单');
INSERT INTO `system_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'parent-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-14 11:11:54', '角色管理菜单');
INSERT INTO `system_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'menu-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-14 11:13:00', '菜单管理菜单');
INSERT INTO `system_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'organization-chart', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 10:18:06', '部门管理菜单');
INSERT INTO `system_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'account-box-2-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 10:15:52', '岗位管理菜单');
INSERT INTO `system_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'book-ai-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 10:16:58', '字典管理菜单');
INSERT INTO `system_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'sound-module-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 10:08:37', '参数设置菜单');
INSERT INTO `system_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message-2-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-14 11:15:01', '通知公告菜单');
INSERT INTO `system_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'booklet-line', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 10:20:49', '日志管理菜单');
INSERT INTO `system_menu` VALUES (109, '在线用户', 2, 1, 'online', 'system/monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', '吴同', '2026-04-20 09:32:53', '', NULL, '在线用户菜单');
INSERT INTO `system_menu` VALUES (110, '定时任务', 2, 2, 'job', 'system/monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', '吴同', '2026-04-20 09:32:53', '', NULL, '定时任务菜单');
INSERT INTO `system_menu` VALUES (111, '服务监控', 2, 4, 'server', 'system/monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', '吴同', '2026-04-20 09:32:53', '', NULL, '服务监控菜单');
INSERT INTO `system_menu` VALUES (112, '缓存监控', 2, 5, 'cache', 'system/monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', '吴同', '2026-04-20 09:32:53', '', NULL, '缓存监控菜单');
INSERT INTO `system_menu` VALUES (113, '缓存列表', 2, 6, 'cacheList', 'system/monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'list-view', '吴同', '2026-04-20 09:32:53', '吴同', '2026-05-15 10:35:38', '缓存列表菜单');
INSERT INTO `system_menu` VALUES (114, '代码生成', 3, 2, 'gen', 'system/tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', '吴同', '2026-04-20 09:32:53', '', NULL, '代码生成菜单');
INSERT INTO `system_menu` VALUES (115, '系统接口', 3, 3, 'swagger', 'system/tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', '吴同', '2026-04-20 09:32:53', '', NULL, '系统接口菜单');
INSERT INTO `system_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'system/monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', '吴同', '2026-04-20 09:32:53', '', NULL, '操作日志菜单');
INSERT INTO `system_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', '吴同', '2026-04-20 09:32:53', '', NULL, '登录日志菜单');
INSERT INTO `system_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', '吴同', '2026-04-20 09:32:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1055, '生成查询', 114, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1056, '生成修改', 114, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1057, '生成删除', 114, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1058, '导入代码', 114, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1059, '预览代码', 114, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (1060, '生成代码', 114, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2000, '知识库', 0, 4, 'kmc', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识库', '吴同', '2026-04-20 09:32:54', '吴同', '2026-05-14 10:41:35', '');
INSERT INTO `system_menu` VALUES (2001, '知识分类', 2000, 2, ':kbId/kmcCategory', 'kmc/kmcCategory/index', '', NULL, 1, 0, 'C', '0', '0', 'kmc:kmcCategory:kmcCategory:list', 'menu-search-line', '吴同', '2026-04-20 09:32:54', '吴同', '2026-05-15 17:02:36', '');
INSERT INTO `system_menu` VALUES (2002, '知识分类导出', 2001, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2003, '知识分类导入', 2001, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2004, '知识分类详情', 2001, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2005, '知识分类新增', 2001, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2006, '知识分类修改', 2001, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2007, '知识分类删除', 2001, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmc:kmcCategory:kmcCategory:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2008, '知识文件', 2000, 1, ':kbId/kmcDocument', 'kmc/kmcDocument/index', '', NULL, 1, 0, 'C', '0', '0', 'kmcDocument:kmcDocument:document:list', 'file-text-line', '吴同', '2026-04-20 09:32:54', '吴同', '2026-05-15 17:02:46', '');
INSERT INTO `system_menu` VALUES (2009, '知识文件导出', 2008, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2010, '知识文件导入', 2008, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2011, '知识文件详情', 2008, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2012, '知识文件新增', 2008, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2013, '知识文件修改', 2008, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2014, '知识文件删除', 2008, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'kmcDocument:kmcDocument:document:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2015, '知识抽取', 2058, 2, 'ext', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'file-ai-line', '吴同', '2026-04-20 09:32:54', '吴同', '2026-05-14 10:59:42', '');
INSERT INTO `system_menu` VALUES (2016, '概念配置', 2015, 1, 'schema', 'ext/extSchema/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extSchema:schema:list', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2017, '概念配置查询', 2016, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2018, '概念配置新增', 2016, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2019, '概念配置修改', 2016, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2020, '概念配置删除', 2016, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2021, '概念配置导出', 2016, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2022, '概念配置导入', 2016, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchema:schema:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2023, '关系配置', 2015, 2, 'relation', 'ext/extSchemaRelation/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extSchemaRelation:relation:list', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2024, '关系配置查询', 2023, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2025, '关系配置新增', 2023, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2026, '关系配置修改', 2023, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2027, '关系配置删除', 2023, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2028, '关系配置导出', 2023, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2029, '关系配置导入', 2023, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extSchemaRelation:relation:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2030, '非结构化抽取', 2015, 3, 'unstructTask', 'ext/extUnstructTask/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extUnstructTask:unstructtask:list', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2031, '非结构化抽取任务查询', 2030, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2032, '非结构化抽取任务新增', 2030, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2033, '非结构化抽取任务修改', 2030, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2034, '非结构化抽取任务删除', 2030, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2035, '非结构化抽取任务导出', 2030, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2036, '非结构化抽取任务导入', 2030, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2037, '结构化抽取', 2015, 4, 'extStructTask', 'ext/extStructTask/index', NULL, NULL, 1, 0, 'C', '0', '0', 'ext:extStructTask:struct:list', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2038, '结构化抽取任务查询', 2037, 1, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2039, '结构化抽取任务新增', 2037, 2, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2040, '结构化抽取任务修改', 2037, 3, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2041, '结构化抽取任务删除', 2037, 4, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2042, '结构化抽取任务导出', 2037, 5, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2043, '结构化抽取任务导入', 2037, 6, '#', '', NULL, NULL, 1, 0, 'F', '0', '0', 'ext:extStructTask:struct:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2044, '知识应用', 2058, 3, 'app', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '知识应用', '吴同', '2026-04-20 09:32:54', '吴同', '2026-04-20 17:24:35', '');
INSERT INTO `system_menu` VALUES (2045, '图谱探索', 2044, 0, 'graphExploration', 'app/graphExploration/index', NULL, NULL, 1, 0, 'C', '0', '0', 'app:graphExploration:graphExploration:list', '#', '吴同', '2026-04-20 09:32:54', '吴同', '2026-06-05 13:55:45', '');
INSERT INTO `system_menu` VALUES (2046, '数据管理', 2058, 4, 'dm', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'database-2-line', '吴同', '2026-04-20 09:32:54', '吴同', '2026-05-15 10:24:52', '');
INSERT INTO `system_menu` VALUES (2047, '数据源', 2046, 4, 'dmDatasource', 'dm/dmDatasource/index', NULL, NULL, 1, 0, 'C', '0', '0', 'dm:datasource:datasource:list', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2048, '数据源查询', 2047, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:query', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2049, '数据源新增', 2047, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:add', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2050, '数据源修改', 2047, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:edit', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2051, '数据源删除', 2047, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:remove', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2052, '数据源导出', 2047, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:export', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2053, '数据源导入', 2047, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'dm:datasource:datasource:import', '#', '吴同', '2026-04-20 09:32:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2054, '非结构化抽取任务日志', 2030, 8, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:taskLog', '#', '吴同', '2026-04-20 09:36:58', '吴同', '2026-04-20 09:36:58', '');
INSERT INTO `system_menu` VALUES (2055, '抽取日志', 2015, 5, 'extTaskLog', 'ext/extTaskLog/index', NULL, '', 1, 0, 'C', '0', '0', 'ext:extTasklog:tasklog:list', '#', '吴同', '2026-04-20 09:36:58', '吴同', '2026-04-20 09:36:58', '');
INSERT INTO `system_menu` VALUES (2057, 'AI工作台', 0, 1, 'kb', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'ai工作台', '吴同', '2026-04-20 17:22:18', '吴同', '2026-05-14 09:52:28', '');
INSERT INTO `system_menu` VALUES (2058, '知识图谱', 0, 3, 'kg', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '知识图谱', '吴同', '2026-04-20 17:23:39', '吴同', '2026-05-14 10:48:03', '');
INSERT INTO `system_menu` VALUES (2061, '首页', 0, 0, 'kd', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '首页', '吴同', '2026-04-21 11:19:30', '吴同', '2026-04-27 15:37:39', '');
INSERT INTO `system_menu` VALUES (2062, '首页', 2061, 0, 'integrated', 'system/index', NULL, '', 1, 0, 'C', '0', '0', '', '首页', '吴同', '2026-04-21 11:20:52', '吴同', '2026-04-27 15:37:54', '');
INSERT INTO `system_menu` VALUES (2063, '应用中心', 0, 2, 'kac', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '应用中心', '吴同', '2026-04-27 09:35:56', '吴同', '2026-05-14 10:33:49', '');
INSERT INTO `system_menu` VALUES (2064, '插件中心', 0, 5, 'plugin', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '插件', '吴同', '2026-04-27 09:37:31', '吴同', '2026-05-14 10:46:47', '');
INSERT INTO `system_menu` VALUES (2079, '产品动态', 2061, 1, 'dynamic', 'system/developing/index', NULL, '', 1, 0, 'C', '1', '0', '', 'education', '吴同', '2026-04-27 15:43:52', '吴同', '2026-05-13 11:47:42', '');
INSERT INTO `system_menu` VALUES (2080, '模型市场', 1, 11, 'ai', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'brain-ai-3-line', '吴同', '2026-04-21 13:57:04', '吴同', '2026-05-14 11:15:56', '');
INSERT INTO `system_menu` VALUES (2123, '非结构化抽取结果', 2030, 7, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:extractResults', '#', '吴同', '2025-06-18 08:45:45', '', NULL, '');
INSERT INTO `system_menu` VALUES (2124, '知识库', 2000, 0, 'knowledgeBase', 'kmc/knowledgeBase/index', NULL, '', 1, 0, 'C', '1', '0', 'kmc:knowledgeBase:knowledgebase:list', '#', '吴同', '2025-10-14 09:05:56', '吴同', '2025-10-14 09:06:33', '知识库菜单');
INSERT INTO `system_menu` VALUES (2125, '知识库查询', 2124, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:query', '#', '吴同', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2126, '知识库新增', 2124, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:add', '#', '吴同', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2127, '知识库修改', 2124, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:edit', '#', '吴同', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2128, '知识库删除', 2124, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:remove', '#', '吴同', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2129, '知识库导出', 2124, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:export', '#', '吴同', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2130, '知识库导入', 2124, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeBase:knowledgebase:import', '#', '吴同', '2025-10-14 09:05:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2131, '召回测试', 2000, 3, ':kbId/recall', 'kmc/knowledgeBase/components/recall', NULL, 'recall', 1, 0, 'C', '0', '0', '', 'box-3-line', '吴同', '2025-10-14 16:26:06', '吴同', '2026-05-14 11:05:30', '');
INSERT INTO `system_menu` VALUES (2190, '文件分段', 2008, 1, 'knowledgeSegment', 'kmc/knowledgeSegment/index', NULL, '', 1, 0, 'C', '0', '0', 'kmc:knowledgeSegment:knowledgesegment:list', '#', 'admin', '2025-08-28 11:44:56', '', NULL, '文件分段菜单');
INSERT INTO `system_menu` VALUES (2191, '文件分段查询', 2190, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeSegment:knowledgesegment:query', '#', 'admin', '2025-08-28 11:44:57', '', NULL, '');
INSERT INTO `system_menu` VALUES (2192, '文件分段新增', 2190, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeSegment:knowledgesegment:add', '#', 'admin', '2025-08-28 11:44:57', '', NULL, '');
INSERT INTO `system_menu` VALUES (2193, '文件分段修改', 2190, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeSegment:knowledgesegment:edit', '#', 'admin', '2025-08-28 11:44:57', '', NULL, '');
INSERT INTO `system_menu` VALUES (2194, '文件分段删除', 2190, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeSegment:knowledgesegment:remove', '#', 'admin', '2025-08-28 11:44:57', '', NULL, '');
INSERT INTO `system_menu` VALUES (2195, '文件分段导出', 2190, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeSegment:knowledgesegment:export', '#', 'admin', '2025-08-28 11:44:57', '', NULL, '');
INSERT INTO `system_menu` VALUES (2196, '文件分段导入', 2190, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kmc:knowledgeSegment:knowledgesegment:import', '#', 'admin', '2025-08-28 11:44:58', '', NULL, '');

INSERT INTO `system_menu` VALUES (2223, '知识中心', 2058, 1, 'knowledge', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'book-open-line', '吴同', '2025-10-20 09:43:47', '吴同', '2026-05-14 10:57:49', '');
INSERT INTO `system_menu` VALUES (2224, '知识分类', 2223, 1, 'category', 'kg/knowledge/category/index', NULL, '', 1, 0, 'C', '0', '0', 'kg:knowledge:category:list', '#', '吴同', '2025-10-20 09:50:56', '吴同', '2025-10-20 09:56:13', '知识分类菜单');
INSERT INTO `system_menu` VALUES (2225, '知识分类查询', 2224, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:query', '#', '吴同', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2226, '知识分类新增', 2224, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:add', '#', '吴同', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2227, '知识分类修改', 2224, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:edit', '#', '吴同', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2228, '知识分类删除', 2224, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:remove', '#', '吴同', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2229, '知识分类导出', 2224, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:export', '#', '吴同', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2230, '知识分类导入', 2224, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:import', '#', '吴同', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2231, '知识文件', 2223, 0, 'document', 'kg/knowledge/document/index', NULL, '', 1, 0, 'C', '0', '0', 'kg:knowledge:document:list', '#', '吴同', '2025-10-20 09:51:05', '吴同', '2026-05-13 13:47:33', '知识文件菜单');
INSERT INTO `system_menu` VALUES (2232, '知识文件查询', 2231, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:query', '#', '吴同', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2233, '知识文件新增', 2231, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:add', '#', '吴同', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2234, '知识文件修改', 2231, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:edit', '#', '吴同', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2235, '知识文件删除', 2231, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:remove', '#', '吴同', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2236, '知识文件导出', 2231, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:export', '#', '吴同', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2237, '知识文件导入', 2231, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:import', '#', '吴同', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2262, '知识库设置', 2000, 4, ':kbId/knowledgeBase', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '知识库', '吴同', '2025-11-05 16:10:31', '吴同', '2026-05-14 11:06:28', '');
INSERT INTO `system_menu` VALUES (2315, '基础设置', 2262, 0, 'kmcBasic', 'kmc/knowledgeBase/components/settings', NULL, '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO `system_menu` VALUES (2316, '权限设置', 2262, 1, 'roles', 'kmc/knowledgeBase/components/roleTable', NULL, '', 1, 0, 'C', '0', '0', 'kmc:knowledgeBase:role:list', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-06-04 14:30:21', '');
INSERT INTO `system_menu` VALUES (2317, '检索设置', 2262, 2, 'querySet', 'kmc/knowledgeBase/components/querySet', NULL, '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO `system_menu` VALUES (2318, '删除设置', 2262, 3, 'kmcDel', 'kmc/knowledgeBase/components/kmcDel', NULL, '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO `system_menu` VALUES (2322, '模型市场', 2080, 1, 'modelMarket', 'ai/modelMarket/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:modelMarket:key:list', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO `system_menu` VALUES (2323, '我的模型', 2080, 2, 'myModel', 'ai/myModel/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:modelMarket:key:list', '#', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO `system_menu` VALUES (2324, 'api密钥配置', 2080, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'ai:modelMarket:key:edit', '#', '吴同', '2026-04-21 13:57:04', '', NULL, '');
INSERT INTO `system_menu` VALUES (2325, 'api密钥查看', 2080, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'ai:modelMarket:key:query', '#', '吴同', '2026-04-21 13:57:04', '', NULL, '');
INSERT INTO `system_menu` VALUES (2335, 'Bot 管理', 2057, 1, 'bot', 'Bot 管理', '', '', 1, 0, 'M', '0', '0', 'kb:bot:bot:list', 'bot管理', '吴同', '2026-04-13 10:38:02', '吴同', '2026-05-13 17:20:53', '');
INSERT INTO `system_menu` VALUES (2336, '新增 Bot', 2335, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:add', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO `system_menu` VALUES (2337, '修改 Bot', 2335, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:edit', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO `system_menu` VALUES (2338, '删除 Bot', 2335, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:remove', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO `system_menu` VALUES (2339, '获取 Bot', 2335, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:query', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO `system_menu` VALUES (2395, '工具管理', 2472, 2, 'tool', 'kb/tool/index', NULL, 'kbTool', 1, 0, 'C', '0', '0', 'kb:tool:tool:list', '#', '吴同', '2026-04-21 16:15:27', '吴同', '2026-06-24 17:48:30', '工具管理菜单');
INSERT INTO `system_menu` VALUES (2396, '工具管理查询', 2395, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:query', '#', '吴同', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO `system_menu` VALUES (2397, '工具管理新增', 2395, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:add', '#', '吴同', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO `system_menu` VALUES (2398, '工具管理修改', 2395, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:edit', '#', '吴同', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO `system_menu` VALUES (2399, '工具管理删除', 2395, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:remove', '#', '吴同', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO `system_menu` VALUES (2400, '工具管理导出', 2395, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:export', '#', '吴同', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO `system_menu` VALUES (2401, '工具管理导入', 2395, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:import', '#', '吴同', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO `system_menu` VALUES (2402, '知识资产看板', 2061, 3, 'knowledgeAsset', 'kd/knowledgeAsset/index', NULL, '', 1, 0, 'C', '0', '0', '', 'trello-line', '吴同', '2026-05-13 10:34:41', '吴同', '2026-05-15 09:59:22', '');
INSERT INTO `system_menu` VALUES (2403, 'Bot运营看板', 2061, 4, 'botOperation', 'kd/botOperation/index', NULL, '', 1, 0, 'C', '0', '0', '', 'slideshow-line', '吴同', '2026-05-13 10:43:41', '吴同', '2026-05-15 09:44:19', '');
INSERT INTO `system_menu` VALUES (2404, '应用运营看板', 2061, 5, 'appOperations', 'kd/appOperations/index', NULL, '', 1, 0, 'C', '0', '0', '', 'bar-chart-box-ai-line', '吴同', '2026-05-13 10:44:06', '吴同', '2026-05-15 09:42:48', '');
INSERT INTO `system_menu` VALUES (2405, '概览', 2063, 0, 'overview', 'kac/overview/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:overview:overview:list', '概览', '吴同', '2026-05-13 13:42:03', '吴同', '2026-06-05 13:53:33', '');
INSERT INTO `system_menu` VALUES (2407, '横向通用应用', 2063, 2, 'horizontal', 'kac/horizontal/index', NULL, '', 1, 0, 'C', '0', '0', '', '横向', '吴同', '2026-05-13 13:42:58', '吴同', '2026-05-15 16:58:51', '');
INSERT INTO `system_menu` VALUES (2408, '纵向行业应用', 2063, 3, 'vertical', 'kac/vertical/index', NULL, '', 1, 0, 'C', '0', '0', '', '纵向', '吴同', '2026-05-13 13:43:26', '吴同', '2026-05-15 16:59:01', '');
INSERT INTO `system_menu` VALUES (2410, '我的应用', 2063, 5, 'myApp', 'kac/myApp/index', NULL, '', 1, 0, 'C', '0', '0', '', '我的应用', '吴同', '2026-05-13 13:44:26', '吴同', '2026-05-15 16:59:12', '');
INSERT INTO `system_menu` VALUES (2411, '插件管理', 2064, 0, 'plugin', 'kac/plugin/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:plugin:plugin:list', '插件管理', '吴同', '2026-05-13 13:45:32', '吴同', '2026-06-05 13:54:18', '');
INSERT INTO `system_menu` VALUES (2412, '工作流', 2335, 1, 'workflow', 'kb/bot/index', '{\"botType\":0}', '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-05-13 17:22:28', '吴同', '2026-05-14 14:10:12', '');
INSERT INTO `system_menu` VALUES (2413, 'Chatflow', 2335, 2, 'chatflow', 'kb/bot/index', '{\"botType\":1}', '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-05-14 09:22:25', '吴同', '2026-05-14 09:26:26', '');
INSERT INTO `system_menu` VALUES (2414, 'Agent', 2335, 3, 'agent', 'kb/bot/index', '{\"botType\":2}', '', 1, 0, 'C', '0', '0', '', '#', '吴同', '2026-05-14 09:22:48', '吴同', '2026-05-14 09:31:26', '');
INSERT INTO `system_menu` VALUES (2415, '插件管理删除', 2411, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kac:plugin:plugin:remove', '#', '吴同', '2026-06-05 11:45:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2416, '插件管理新增', 2411, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kac:plugin:plugin:add', '#', '吴同', '2026-06-05 11:45:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2417, '我的应用修改', 2410, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kac:apply:apply:edit', '#', '吴同', '2026-06-09 09:19:04', '吴同', '2026-06-09 09:20:39', '');
INSERT INTO `system_menu` VALUES (2418, '我的应用删除', 2410, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kac:apply:apply:remove', '#', '吴同', '2026-06-09 09:21:02', '', NULL, '');
INSERT INTO `system_menu` VALUES (2471, '工具分类', 2472, 1, 'toolCategory', 'kb/toolCategory/index', NULL, 'toolCategory', 1, 0, 'C', '0', '0', NULL, '#', '吴同', '2026-06-24 17:19:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2472, '工具管理', 2057, 12, 'tool', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'tool_new_icon', '吴同', '2026-06-24 17:48:09', '吴同', '2026-07-07 10:56:24', '');

-- ----------------------------
-- Table structure for system_notice
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_notice
-- ----------------------------
INSERT INTO `system_notice` VALUES (1, 'qKnow智能体构建平台正式开源！', '2', 0x3C703EE79FA5E8AF86E4B8ADE5BF83E38081E79FA5E8AF86E68ABDE58F96E38081E79FA5E8AF86E59BBEE8B0B1E6A0B8E5BF83E4B889E5A4A7E58A9FE883BDE5B7B2E58F91E5B8833C2F703E, '0', '吴同', '2025-05-28 18:00:00', '吴同', '2026-05-15 13:47:04', NULL);
INSERT INTO `system_notice` VALUES (2, 'qKnow期待与您携手共建知识体系！', '1', 0xE69C9FE5BE85E682A8E79A84E58AA0E585A5, '0', '吴同', '2026-04-20 09:32:55', '吴同', NULL, NULL);
INSERT INTO `system_notice` VALUES (3, 'qKnow 1.0.0 正式发布', '2', 0x3C68313EF09F9A8020714B6E6F7720312E302E3020E6ADA3E5BC8FE58F91E5B8833C2F68313E3C703E714B6E6F7720312E302E3020E698AFE68891E4BBACE79A84E9A696E4B8AAE6ADA3E5BC8FE78988E69CACEFBC8CE6A087E5BF97E79D80E79FA5E8AF86E7AEA1E79086E4B88EE699BAE883BDE68ABDE58F96E883BDE58A9BE79A84E585A8E99DA2E890BDE59CB0E38082E69CACE78988E69CACE8819AE784A6E4BA8EE79FA5E8AF86E585A8E7949FE591BDE591A8E69C9FE7AEA1E79086E79A84E6A0B8E5BF83E58A9FE883BDE5BBBAE8AEBEEFBC8CE99B86E68890E58588E8BF9BE79A84E79FA5E8AF86E68ABDE58F96E68A80E69CAFE4B88EE79BB4E8A782E79A84E58FAFE8A786E58C96E68EA2E7B4A2E883BDE58A9BEFBC8CE887B4E58A9BE4BA8EE68993E980A0E4BC81E4B89AE7BAA7E79FA5E8AF86E4B8ADE69EA2E5B9B3E58FB0E380823C2F703E3C68323EE29CA820E6A0B8E5BF83E58A9FE883BDE4BAAEE782B93C2F68323E3C756C3E3C6C693EF09FA7A020E79FA5E8AF86E4B8ADE5BF833C2F6C693E3C6C693EE99B86E4B8ADE58C96E7AEA1E79086E59084E7B1BBE69687E6A1A3E8B584E6BA90EFBC8CE694AFE68C81E5A49AE7BBB4E5BAA6E58886E7B1BBE4B88EE9AB98E69588E6A380E7B4A2EFBC8CE69E84E5BBBAE7BB9FE4B880E79A84E79FA5E8AF86E8B584E4BAA7E5BA93EFBC8CE58AA9E58A9BE794A8E688B7E5BFABE9809FE5AE9AE4BD8DE4B88EE88EB7E58F96E68980E99C80E4BFA1E681AFE380823C2F6C693E3C6C693EF09F94A420E6A682E5BFB5E9858DE7BDAE3C2F6C693E3C6C693EE694AFE68C81E887AAE5AE9AE4B989E79FA5E8AF86E68ABDE58F96E6A682E5BFB5E6A8A1E59E8BE4B88EE8A784E58899EFBC8CE781B5E6B4BBE98082E9858DE4B88DE5908CE4B89AE58AA1E59CBAE699AFEFBC8CE68F90E58D87E585B3E994AEE5AE9EE4BD93E8AF86E588ABE79A84E58786E7A1AEE680A7E4B88EE8AFADE4B989E8A1A8E8BEBEE883BDE58A9BE380823C2F6C693E3C6C693EF09F949720E585B3E7B3BBE9858DE7BDAE3C2F6C693E3C6C693EE58FAFE781B5E6B4BBE5AE9AE4B989E5AE9EE4BD93E997B4E8AFADE4B989E585B3E7B3BBE68ABDE58F96E8A784E58899EFBC8CE5AE9EE78EB0E79FA5E8AF86E997B4E585B3E88194E79A84E7B2BEE7BB86E58C96E5BBBAE6A8A1EFBC8CE5A29EE5BCBAE79FA5E8AF86E7BD91E7BB9CE79A84E7BB93E69E84E58C96E8A1A8E8BEBEE4B88EE68EA8E79086E883BDE58A9BE380823C2F6C693E3C6C693EF09F938420E99D9EE7BB93E69E84E58C96E68ABDE58F963C2F6C693E3C6C693EE59FBAE4BA8E20446565704B4520E7AD89E58588E8BF9BE79FA5E8AF86E68ABDE58F96E5B7A5E585B7EFBC8CE4BB8EE69687E69CACE38081E69687E6A1A3E7AD89E99D9EE7BB93E69E84E58C96E695B0E68DAEE4B8ADE887AAE58AA8E8AF86E588ABE5AE9EE4BD93E38081E5B19EE680A7E58F8AE585B3E7B3BBEFBC8CE9878AE694BEE99A90E680A7E79FA5E8AF86E4BBB7E580BCE380823C2F6C693E3C6C693EF09F92BE20E7BB93E69E84E58C96E68ABDE58F963C2F6C693E3C6C693EE4BB8EE695B0E68DAEE5BA93E38081E8A1A8E6A0BCE7AD89E7BB93E69E84E58C96E695B0E68DAEE6BA90E4B8ADE887AAE58AA8E58C96E68F90E58F96E38081E8BDACE68DA2E5B9B6E58AA0E8BDBDE79FA5E8AF86E58583E7B4A0EFBC8CE5AE9EE78EB0E7BB93E69E84E58C96E4BFA1E681AFE59091E79FA5E8AF86E59BBEE8B0B1E79A84E9AB98E69588E8BF81E7A7BBE4B88EE99B86E68890E380823C2F6C693E3C6C693EF09F8C9020E59BBEE8B0B1E68EA2E7B4A23C2F6C693E3C6C693EE68F90E4BE9BE58FAFE8A786E58C96E38081E4BAA4E4BA92E5BC8FE79FA5E8AF86E59BBEE8B0B1E6B58FE8A788E4B88EE58886E69E90E5B9B3E58FB0EFBC8CE694AFE68C81E585B3E7B3BBE8B7AFE5BE84E8BFBDE8B8AAE38081E5AD90E59BBEE68C96E68E98E4B88EE8AFADE4B989E69FA5E8AFA2EFBC8CE58AA9E58A9BE6B7B1E5BAA6E6B49EE5AF9FE5A48DE69D82E585B3E88194E7BD91E7BB9CE380823C2F6C693E3C6C693EE29A99EFB88F20E7B3BBE7BB9FE7AEA1E790863C2F6C693E3C6C693EE6B6B5E79B96E7B3BBE7BB9FE9858DE7BDAEE38081E794A8E688B7E69D83E99990E7AEA1E79086E38081E8BF90E8A18CE78AB6E68081E79B91E68EA7E38081E6938DE4BD9CE697A5E5BF97E5AEA1E8AEA1E58F8AE69C8DE58AA1E599A8E8B584E6BA90E79B91E68EA7E7AD89E58A9FE883BDEFBC8CE4BF9DE99A9CE7B3BBE7BB9FE5AE89E585A8E38081E7A8B3E5AE9AE4B88EE58FAFE7BBB4E68AA4E680A7E380823C2F6C693E3C2F756C3E3C703EF09F8E8920E789B9E588ABE8AFB4E6988EEFBC9A3C2F703E3C703E714B6E6F7720312E302E3020E4BD9CE4B8BAE9A696E4B8AAE6ADA3E5BC8FE58F91E5B883E78988E69CACEFBC8CE5A5A0E5AE9AE4BA86E5B9B3E58FB0E79A84E6A0B8E5BF83E69EB6E69E84E4B88EE58A9FE883BDE4BD93E7B3BBE38082E68891E4BBACE8AF9AE98280E682A8E5BC80E590AFE79FA5E8AF86E699BAE883BDE58C96E4B98BE69785EFBC8CE585B1E5908CE69E84E5BBBAE58FAFE8BFBDE6BAAFE38081E58FAFE58886E69E90E38081E58FAFE68EA8E79086E79A84E4BC81E4B89AE79FA5E8AF86E5A4A7E88491EFBC813C2F703E, '0', '吴同', '2026-05-15 13:46:35', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (4, 'qKnow 1.0.3 稳定版发布', '2', 0x3C68313EF09F9A8020714B6E6F7720312E302E3320E7A8B3E5AE9AE78988E58F91E5B8833C2F68313E3C703E714B6E6F7720312E302E3320E698AFE7BBA7E9A696E4B8AAE6ADA3E5BC8FE78988E69CACE5908EE79A84E58F88E4B880E9878DE8A681E8BFADE4BBA3EFBC8CE8819AE784A6E4BA8EE7B3BBE7BB9FE7A8B3E5AE9AE680A7E68F90E58D87E38081E983A8E7BDB2E4BD93E9AA8CE4BC98E58C96E4B88EE5A49AE9A1B9E585B3E994AEE997AEE9A298E4BFAEE5A48DE38082E69CACE78988E69CACE59CA8E4BF9DE68C81E6A0B8E5BF83E79FA5E8AF86E7AEA1E79086E883BDE58A9BE79A84E59FBAE7A180E4B88AEFBC8CE8BF9BE4B880E6ADA5E68993E7A3A8E4BAA7E59381E7BB86E88A82EFBC8CE68F90E58D87E5BC80E58F91E4B88EE4BDBFE794A8E69588E78E87EFBC8CE4B8BAE794A8E688B7E5B8A6E69DA5E69BB4E6B581E79585E38081E58FAFE99DA0E79A84E5B9B3E58FB0E4BD93E9AA8CE380823C2F703E3C68323EE29CA820E69CACE6ACA1E69BB4E696B0E4BAAEE782B93C2F68323E3C756C3E3C6C693EF09F90B320446F636B657220436F6D706F736520E5BFABE9809FE983A8E7BDB2E694AFE68C813C2F6C693E3C6C693EE696B0E5A29EE59FBAE4BA8E20446F636B657220436F6D706F736520E79A84E4B880E994AEE983A8E7BDB2E696B9E6A188EFBC8CE5A4A7E5B985E7AE80E58C96E69CACE59CB0E58F8AE6B58BE8AF95E78EAFE5A283E690ADE5BBBAE6B581E7A88BEFBC8CE58AA9E58A9BE5BC80E58F91E88085E5928CE8BF90E7BBB4E4BABAE59198E5BFABE9809FE590AFE58AA820714B6E6F7720E5B9B3E58FB0E380823C2F6C693E3C6C693EF09F9BA0EFB88F20E5A49AE9A1B9E585B3E994AEE997AEE9A298E4BFAEE5A48D3C2F6C693E3C6C693EE99288E5AFB9E7A4BEE58CBAE58F8DE9A688E4B88EE58685E983A8E6B58BE8AF95E58F91E78EB0E79A84E997AEE9A298EFBC8CE99B86E4B8ADE4BFAEE5A48DE4BBA5E4B88BE58685E5AEB9EFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE4BFAEE5A48DE4BE9DE8B596204A415220E58C85E4B88BE8BDBDE8BF87E7A88BE4B8ADE69687E4BBB6E68D9FE59D8FE79A84E997AEE9A298EFBC9B3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE8A7A3E586B3E9A1B9E79BAEE697A0E6B395E6ADA3E5B8B8E68993E58C85E69E84E5BBBAE79A84E69E84E5BBBAE5BC82E5B8B8EFBC9B3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE4BFAEE5A48DE68EA5E58FA3E69687E6A1A3EFBC8853776167676572EFBC89E68993E5BC80E697B6E68AA5E99499E79A84E997AEE9A298EFBC9B3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE4BC98E58C96E6B58FE8A788E599A8E9A1B5E7ADBEE59BBEE6A087E698BEE7A4BAE4B88DE585A8E79A8420554920E997AEE9A298EFBC9B3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE4BFAEE5A48DE69687E4BBB6E4B88BE8BDBDE4B88EE9A284E8A788E58A9FE883BDE4B8ADE79A84E88BA5E5B9B220427567EFBC9B3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE4BFAEE5A48DE696B0E5A29EE6A682E5BFB5E4BF9DE5AD98E5908EE58897E8A1A8E69CAAE6ADA3E7A1AEE588B7E696B0E79A84E698BEE7A4BAE5BC82E5B8B8EFBC9B3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE8A1A5E58585E7BCBAE5A4B1E79A84E99D99E68081E8B7AFE794B1E9858DE7BDAEEFBC8CE7A1AEE4BF9DE5898DE7ABAFE9A1B5E99DA2E8AEBFE997AEE7A8B3E5AE9AE680A7E380823C2F6C693E3C6C693EF09FA7A9204E656F346A20E5889DE5A78BE58C96E695B0E68DAEE4BC98E58C963C2F6C693E3C6C693EE69BB4E696B0204E656F346A20E59BBEE695B0E68DAEE5BA93E79A84E5889DE5A78BE58C96E695B0E68DAEE69687E4BBB6EFBC8CE7A1AEE4BF9DE696B0E983A8E7BDB2E5AE9EE4BE8BE585B7E5A487E69BB4E5AE8CE695B4E38081E8A784E88C83E79A84E5889DE5A78BE79FA5E8AF86E7BB93E69E84E380823C2F6C693E3C6C693EF09F939A20E69687E6A1A3E7AB99E5908CE6ADA5E69BB4E696B03C2F6C693E3C6C693EE5908CE6ADA5E5AE8CE59684E5AE98E696B9E69687E6A1A3E7AB99E58685E5AEB9EFBC8CE6B6B5E79B96E983A8E7BDB2E68C87E58D97E38081E5B8B8E8A781E997AEE9A298E8A7A3E7AD94E58F8A2041504920E4BDBFE794A8E8AFB4E6988EEFBC8CE5B8AEE58AA9E794A8E688B7E69BB4E9AB98E69588E59CB0E4B88AE6898BE4B88EE99B86E6889020714B6E6F77E380823C2F6C693E3C2F756C3E3C703EF09F8E8920E887B4E8B0A2E4B88EE5B195E69C9B3C2F703E3C703EE6849FE8B0A2E6AF8FE4B880E4BD8DE697A9E69C9FE794A8E688B7E79A84E4BFA1E4BBBBE4B88EE58F8DE9A688EFBC81714B6E6F7720312E302E3320E79A84E58F91E5B883E698AFE68891E4BBACE68C81E7BBADE68993E7A3A8E4BAA7E59381E38081E8B4B4E8BF91E794A8E688B7E99C80E6B182E79A84E9878DE8A681E4B880E6ADA5E38082E69CAAE69DA5E68891E4BBACE5B086E7BBA7E7BBADE68EA8E8BF9BE79FA5E8AF86E68ABDE58F96E7B2BEE5BAA6E68F90E58D87E38081E59BBEE8B0B1E68EA8E79086E883BDE58A9BE5A29EE5BCBAE4B88EE5A49AE6BA90E695B0E68DAEE89E8DE59088E7AD89E696B9E59091EFBC8CE68993E980A0E69BB4E699BAE883BDE38081E69BB4E5BC80E694BEE79A84E4BC81E4B89AE7BAA7E79FA5E8AF86E4B8ADE69EA2E380823C2F703E3C703EE7AB8BE58DB3E58D87E7BAA7EFBC8CE4BD93E9AA8CE69BB4E7A8B3E5AE9AE38081E69BB4E4BEBFE68DB7E79A8420714B6E6F7720E79FA5E8AF86E5B9B3E58FB0EFBC813C2F703E, '0', '吴同', '2026-05-15 13:47:26', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (5, 'qKnow 1.1.0 稳定版发布', '2', 0x3C68313EF09F9A8020714B6E6F7720312E312E3020E7A8B3E5AE9AE78988E58F91E5B8833C2F68313E3C703E714B6E6F7720312E312E3020E698AFE7BBA720312E302E3320E78988E69CACE5908EE79A84E4B880E6ACA1E9878DE8A681E883BDE58A9BE58D87E7BAA7EFBC8CE8819AE784A6E4BA8EE7BB93E69E84E58C96E695B0E68DAEE68ABDE58F96E883BDE58A9BE79A84E6B7B1E5BAA6E689A9E5B195E4B88EE7B3BBE7BB9FE69EB6E69E84E4BC98E58C96E38082E69CACE78988E69CACE59CA8E4BF9DE68C81E5B9B3E58FB0E7A8B3E5AE9AE58FAFE99DA0E79A84E59FBAE7A180E4B88AEFBC8CE585A8E99DA2E5A29EE5BCBAE5AFB9E4B8BBE6B581E695B0E68DAEE5BA93EFBC88E58C85E68BACE59BBDE4BAA7E695B0E68DAEE5BA93EFBC89E79A84E694AFE68C81EFBC8CE5B9B6E5AE8CE59684E5A48DE69D82E8A1A8E7BB93E69E84E4B88BE79A84E79FA5E8AF86E5BBBAE6A8A1E883BDE58A9BEFBC8CE4B8BAE794A8E688B7E68F90E4BE9BE69BB4E781B5E6B4BBE38081E69BB4E7B2BEE58786E79A84E695B0E68DAEE68EA5E585A5E4B88EE59BBEE8B0B1E69E84E5BBBAE4BD93E9AA8CE380823C2F703E3C68323EE29CA820E69CACE6ACA1E69BB4E696B0E4BAAEE782B93C2F68323E3C68333EE7BB93E69E84E58C96E68ABDE58F96E883BDE58A9BE689A9E5B1953C2F68333E3C756C3E3C6C693EF09F94A720E5AE8CE68890E4BBA3E7A081E980BBE8BE91E9878DE69E84EFBC8CE68F90E58D87E58FAFE7BBB4E68AA4E680A73C2F6C693E3C6C693EE5AFB9E7BB93E69E84E58C96E68ABDE58F96E6A0B8E5BF83E6A8A1E59D97E8BF9BE8A18CE7B3BBE7BB9FE680A7E9878DE69E84EFBC8CE4BC98E58C96E6A8A1E59D97E8818CE8B4A3E58892E58886E4B88EE8B083E794A8E993BEE8B7AFEFBC8CE698BEE89197E68F90E58D87E4BBA3E7A081E58FAFE8AFBBE680A7E4B88EE5908EE7BBADE8BFADE4BBA3E69588E78E87E380823C2F6C693E3C6C693EF09F8C9020E696B0E5A29EE5AFB9204F7261636C65E38081E8BEBEE6A2A6E38081506F737467726553514CE38081E4BABAE5A4A7E98791E4BB93E7AD89E4B8BBE6B581E695B0E68DAEE5BA93E79A84E694AFE68C813C2F6C693E3C6C693EE78EB0E5B7B2E694AFE68C81E5A49AE7A78DE585B3E7B3BBE59E8BE695B0E68DAEE5BA93E4BD9CE4B8BAE7BB93E69E84E58C96E695B0E68DAEE6BA90EFBC8CE58C85E68BACE59BBDE4BAA7E695B0E68DAEE5BA9320E8BEBEE6A2A6EFBC88444DEFBC8920E5928C20E4BABAE5A4A7E98791E4BB93EFBC884B696E67626173654553EFBC89E380823C2F6C693E3C6C693EF09F918920E4BDBFE794A8E68F90E7A4BAEFBC9AE9858DE7BDAEE695B0E68DAEE6BA90E697B6EFBC8CE8AFB7E59CA8E9A9B1E58AA8E7B1BBE59E8BE4B88BE68B89E88F9CE58D95E4B8ADE98089E68BA9E5AFB9E5BA94E695B0E68DAEE5BA93E7B1BBE59E8BEFBC8CE7B3BBE7BB9FE5B086E887AAE58AA8E58AA0E8BDBDE98082E9858DE599A8E5B9B6E5AE8CE68890E8BF9EE68EA5E6A0A1E9AA8CE380823C2F6C693E3C6C693EF09F949720E694AFE68C81E5A49AE5AFB9E5A49AE8A1A8E7BB93E69E84E4B88BE79A84E6A682E5BFB5E4B88EE585B3E7B3BBE698A0E5B084E9858DE7BDAE3C2F6C693E3C6C693EE99288E5AFB9E4B89AE58AA1E7B3BBE7BB9FE4B8ADE5B8B8E8A781E79A84E5A49AE5AFB9E5A49AE585B3E88194E59CBAE699AFEFBC88E5A682E794A8E688B72DE8A792E889B22DE69D83E99990E4B8ADE997B4E8A1A8EFBC89EFBC8CE696B0E5A29EE4B893E794A8E698A0E5B084E980BBE8BE91EFBC8CE794A8E688B7E58FAFE9809AE8BF87E58FAFE8A786E58C96E9858DE7BDAEE7B2BEE58786E5AE9AE4B989E8B7A8E8A1A8E5AE9EE4BD93E585B3E7B3BBEFBC8CE68F90E58D87E79FA5E8AF86E59BBEE8B0B1E5BBBAE6A8A1E79A84E5AE8CE695B4E680A7E4B88EE58786E7A1AEE680A7E380823C2F6C693E3C2F756C3E3C703EF09F8E8920E887B4E8B0A2E4B88EE5B195E69C9B3C2F703E3C703EE6849FE8B0A2E5B9BFE5A4A7E794A8E688B7E4B880E79BB4E4BBA5E69DA5E79A84E4BFA1E4BBBBE4B88EE58F8DE9A688EFBC81714B6E6F7720312E312E3020E79A84E58F91E5B883E6A087E5BF97E79D80E68891E4BBACE59CA8E5A4AFE5AE9EE695B0E68DAEE68EA5E585A5E5BA95E5BAA7E38081E68BA5E68AB1E5A49AE58583E68A80E69CAFE7949FE68081E696B9E99DA2E8BF88E587BAE4BA86E59D9AE5AE9EE4B880E6ADA5E38082E69CAAE69DA5EFBC8CE68891E4BBACE5B086E68C81E7BBADE4BC98E58C96E79FA5E8AF86E68ABDE58F96E7B2BEE5BAA6E38081E4B8B0E5AF8CE59BBEE8B0B1E5BBBAE6A8A1E883BDE58A9BEFBC8CE5B9B6E6B7B1E58C96E5AFB9E4BFA1E5889BE78EAFE5A283E79A84E694AFE68C81EFBC8CE887B4E58A9BE4BA8EE68993E980A0E69BB4E699BAE883BDE38081E69BB4E58FAFE99DA0E79A84E4BC81E4B89AE7BAA7E79FA5E8AF86E7AEA1E79086E5B9B3E58FB0E380823C2F703E3C703EE7AB8BE58DB3E58D87E7BAA7EFBC8CE4BD93E9AA8CE69BB4E5BCBAE5A4A7E38081E69BB4E781B5E6B4BBE79A8420714B6E6F7720E7BB93E69E84E58C96E68ABDE58F96E883BDE58A9BEFBC813C2F703E, '0', '吴同', '2026-05-15 13:47:41', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (6, 'qKnow 1.1.1 稳定版发布', '2', 0x3C68313EF09F9A8020714B6E6F7720312E312E3120E7A8B3E5AE9AE78988E58F91E5B8833C2F68313E3C703E714B6E6F7720312E312E3120E698AFE7BBA720312E312E3020E78988E69CACE5908EE79A84E58F88E4B880E6ACA1E9878DE8A681E8BFADE4BBA3EFBC8CE69CACE6ACA1E69BB4E696B0E8819AE784A6E4BA8EE99D9EE7BB93E69E84E58C96E695B0E68DAEE68ABDE58F96E883BDE58A9BE79A84E6B7B1E5BAA6E5A29EE5BCBAE38081E7A094E58F91E6B581E7A88BE8A784E88C83E58C96E4BBA5E58F8AE6A0B8E5BF83E4BD93E9AA8CE79A84E7BB86E88A82E68993E7A3A8E38082E68891E4BBACE696B0E5A29EE4BA86E5AFB920576F726420E69687E6A1A3E79A84E699BAE883BDE8AF86E588ABE694AFE68C81EFBC8CE9878DE69E84E4BA86E4BBBBE58AA1E689A7E8A18CE5BC95E6938EE4BBA5E694AFE68C81E69BB4E9AB98E69588E79A84E5B9B6E58F91E68EA7E588B6E4B88EE8BF9BE5BAA6E8BFBDE8B8AAEFBC8CE5B9B6E585A8E99DA2E4BC98E58C96E4BA86E5AE98E696B9E69687E6A1A3E7AB99EFBC8CE697A8E59CA8E4B8BAE794A8E688B7E68F90E4BE9BE4B880E4B8AAE69BB4E5BCBAE5A4A7E38081E69BB4E9808FE6988EE38081E69BB4E69893E794A8E79A84E79FA5E8AF86E7AEA1E79086E5B9B3E58FB0E380823C2F703E3C68323EE29CA820E69CACE6ACA1E69BB4E696B0E4BAAEE782B93C2F68323E3C756C3E3C6C693EF09F938420E99D9EE7BB93E69E84E58C96E68ABDE58F96E883BDE58A9BE9878DE5A4A7E58D87E7BAA73C2F6C693E3C6C693EE99288E5AFB9E5A48DE69D82E69687E6A1A3E5A484E79086E59CBAE699AFEFBC8CE68891E4BBACE5AFB9E5BA95E5B182E68ABDE58F96E5BC95E6938EE8BF9BE8A18CE4BA86E585A8E696B9E4BD8DE9878DE69E84EFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE696B0E5A29E20576F726420E69687E6A1A3E8AF86E588ABE694AFE68C81EFBC9AE6ADA3E5BC8FE68EA5E585A5203C636F6465207374796C653D22636F6C6F723A207267626128302C20302C20302C20302E38293B206261636B67726F756E642D636F6C6F723A20726762283234372C203234372C20323439293B223E2E646F63783C2F636F64653E20E6A0BCE5BC8FE8A7A3E69E90EFBC8CE68993E7A0B4E7BAAFE69687E69CACE99990E588B6EFBC8CE8BDBBE69DBEE68F90E58F96E58A9EE585ACE69687E6A1A3E4B8ADE79A84E585B3E994AEE79FA5E8AF86E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE4BBBBE58AA1E9989FE58897E78AB6E68081E58FAFE8A786E58C96EFBC9AE59CA8E4BBBBE58AA1E58897E8A1A8E4B8ADE6B885E699B0E5B195E7A4BAE2809CE68E92E9989FE4B8ADE2809DE38081E2809CE689A7E8A18CE4B8ADE2809DE38081E2809CE5B7B2E5AE8CE68890E2809DE7AD89E5AE9EE697B6E78AB6E68081EFBC8CE8AEA9E695B0E68DAEE5A484E79086E6B581E7A88BE4B880E79BAEE4BA86E784B6E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE699BAE883BDE5B9B6E58F91E68EA7E588B6EFBC9AE5BC95E585A5E5AE9EE99985E689A7E8A18CE4BBBBE58AA1E79A84E5B9B6E58F91E695B0E99990E588B6E69CBAE588B6EFBC8CE69C89E69588E998B2E6ADA2E8B584E6BA90E8BF87E8BDBDEFBC8CE7A1AEE4BF9DE9AB98E8B49FE8BDBDE4B88BE79A84E7B3BBE7BB9FE7A8B3E5AE9AE680A7E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE585A8E993BEE8B7AFE689A7E8A18CE697A5E5BF97EFBC9AE696B0E5A29EE78BACE7AB8BE79A84E689A7E8A18CE697A5E5BF97E8A1A8EFBC8CE8AEB0E5BD95E6AF8FE4B880E6ACA1E68ABDE58F96E4BBBBE58AA1E79A84E8AFA6E7BB86E8BF90E8A18CE8BDA8E8BFB9EFBC8CE4BEBFE4BA8EE69585E99A9CE68E92E69FA5E4B88EE5AEA1E8AEA1E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE5AE9EE697B6E8BF9BE5BAA6E8BFBDE8B8AAEFBC9AE5A29EE58AA0E5BD93E5898DE689A7E8A18CE8BF9BE5BAA6E69DA1E698BEE7A4BAEFBC8CE794A8E688B7E58FAFE5AE9EE697B6E68E8CE68FA1E995BFE4BBBBE58AA1E79A84E5A484E79086E8BF9BE5B195EFBC8CE5918AE588ABE79BB2E79BAEE7AD89E5BE85E380823C2F6C693E3C6C693EF09F9BA0EFB88F20E6A0B8E5BF83E997AEE9A298E4BFAEE5A48DE4B88EE4BD93E9AA8CE4BC98E58C963C2F6C693E3C6C693EE68C81E7BBADE580BEE590ACE794A8E688B7E58F8DE9A688EFBC8CE8A7A3E586B3E5BDB1E5938DE4BDBFE794A8E4BD93E9AA8CE79A84E585B3E994AEE997AEE9A298EFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE4BFAEE5A48DE6B58FE8A788E599A8E59BBEE78987E4B88DE698BEE7A4BAE997AEE9A298EFBC9AE5BDBBE5BA95E8A7A3E586B3E4BA86E983A8E58886E6B58FE8A788E599A8E78EAFE5A283E4B88BE99D99E68081E8B584E6BA90E58AA0E8BDBDE5A4B1E8B4A5E5AFBCE887B4E59BBEE78987E697A0E6B395E6B8B2E69F93E79A8420427567EFBC8CE7A1AEE4BF9DE59BBEE69687E5B195E7A4BAE6ADA3E5B8B8E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE5898DE7ABAFE4BAA4E4BA92E7BB86E88A82E68993E7A3A8EFBC9AE4BC98E58C96E4BA86E5A49AE5A484E9A1B5E99DA2E58AA0E8BDBDE980BBE8BE91EFBC8CE68F90E58D87E695B4E4BD93E5938DE5BA94E9809FE5BAA6E380823C2F6C693E3C6C693EF09F939D20E7A094E58F91E6B581E7A88BE8A784E88C83E58C962028505220466C6F77293C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE696B0E5A29E20505220E6B581E7A88BE8A784E88C83EFBC9AE6ADA3E5BC8FE7A1AEE7AB8B2050756C6C205265717565737420E68F90E4BAA4E38081E4BBA3E7A081E5AEA1E69FA5EFBC88436F646520526576696577EFBC89E58F8AE59088E5B9B6E6A087E58786E38082E8BF99E4B88DE4BB85E68F90E58D87E4BA86E4BBA3E7A081E5BA93E79A84E8B4A8E9878FE4B88EE58FAFE7BBB4E68AA4E680A7EFBC8CE4B99FE4B8BAE7A4BEE58CBAE8B4A1E78CAEE88085E68F90E4BE9BE4BA86E6B885E699B0E79A84E58D8FE4BD9CE68C87E58D97EFBC8CE68EA8E58AA8E9A1B9E79BAEE59091E69BB4E5BC80E6BA90E38081E69BB4E8A784E88C83E79A84E696B9E59091E58F91E5B195E380823C2F6C693E3C6C693EF09F939A20E5AE98E7BD91E69687E6A1A3E7AB99E585A8E99DA2E78495E696B03C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE69687E6A1A3E7AB99E6B7B1E5BAA6E4BC98E58C96EFBC9AE5AFB9E5AE98E696B9E69687E6A1A3E7AB99E782B9E8BF9BE8A18CE4BA86E7BB93E69E84E9878DE7BB84E4B88EE58685E5AEB9E5A29EE8A1A5E38082E696B0E5A29EE4BA86E99D9EE7BB93E69E84E58C96E68ABDE58F96E9858DE7BDAEE68C87E58D97E38081E4BBBBE58AA1E79B91E68EA7E8AFB4E6988EE58F8AE69585E99A9CE68E92E69FA5E6898BE5868CEFBC8CE6909CE7B4A2E4BD93E9AA8CE4B88EE99885E8AFBBE68E92E78988E5908CE6ADA5E58D87E7BAA7EFBC8CE5B8AEE58AA9E794A8E688B7E69BB4E5BFABE4B88AE6898BE696B0E58A9FE883BDE380823C2F6C693E3C2F756C3E3C703EF09F8E8920E887B4E8B0A2E4B88EE5B195E69C9B3C2F703E3C703EE6849FE8B0A2E6AF8FE4B880E4BD8DE5BC80E58F91E88085E5928CE794A8E688B7E79A84E88090E5BF83E999AAE4BCB4EFBC81714B6E6F7720312E312E3120E6A087E5BF97E79D80E68891E4BBACE59CA8E2809CE5A49AE6A8A1E68081E79FA5E8AF86E88EB7E58F96E2809DE98193E8B7AFE4B88AE8BF88E587BAE4BA86E59D9AE5AE9EE79A84E4B880E6ADA5E38082E4BB8EE58D95E7BAAFE79A84E69687E69CACE8A7A3E69E90E588B0E694AFE68C8120576F726420E69687E6A1A3EFBC8CE4BB8EE9BB91E79B92E689A7E8A18CE588B0E585A8E993BEE8B7AFE58FAFE8A782E6B58BEFBC8CE68891E4BBACE887B4E58A9BE4BA8EE8AEA9E79FA5E8AF86E68ABDE58F96E58F98E5BE97E69BB4E7AE80E58D95E38081E69BB4E58FAFE68EA7E380823C2F703E3C703EE68EA5E4B88BE69DA5EFBC8CE68891E4BBACE5B086E7BBA7E7BBADE68EA2E7B4A22050444620E8A1A8E6A0BCE8BF98E58E9FE38081E5A49AE8AFADE8A880E6B7B7E59088E8AF86E588ABE7AD89E6B7B1E6B0B4E58CBAE68A80E69CAFEFBC8CE5B9B6E8BF9BE4B880E6ADA5E5BC80E694BE2041504920E7949FE68081E380823C2F703E3C703EE7AB8BE58DB3E58D87E7BAA7E887B320714B6E6F7720312E312E31EFBC8CE4BD93E9AA8CE69BB4E9AB98E69588E38081E69BB4E9808FE6988EE79A84E699BAE883BDE79FA5E8AF86E69E84E5BBBAE4B98BE69785EFBC813C2F703E, '0', '吴同', '2026-05-15 13:47:57', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (7, 'qKnow 2.0.0 稳定版发布', '2', 0x3C68313EF09F9A8020714B6E6F7720322E302E3020E7A8B3E5AE9AE78988E58F91E5B8833C2F68313E3C703E714B6E6F7720322E302E3020E698AFE7BBA720312E312E3120E78988E69CACE5908EE79A84E4B880E6ACA1E9878DE8A681E6A0B8E5BF83E69EB6E69E84E8BFADE4BBA3E58D87E7BAA7EFBC8CE69CACE6ACA1E69BB4E696B0E8819AE784A6E4BA8EE585A8E5B180E88F9CE58D95E69EB6E69E84E695B4E4BD93E9878DE69E84E4BC98E58C96E38081E7B3BBE7BB9FE88F9CE58D95E4BD93E7B3BBE7BB9FE4B880E8A784E695B4E4BBA5E58F8AE585A8E5B9B3E58FB0E58A9FE883BDE5B883E5B180E7B2BEE7BB86E58C96E8B083E4BC98E38082E68891E4BBACE585A8E99DA2E9878DE5A191E5B9B3E58FB0E695B4E4BD93E88F9CE58D95E5B195E7A4BAE7BB93E69E84EFBC8CE68F90E5898DE5AE8CE68890E5A49AE7B1BBE6A0B8E5BF83E58A9FE883BDE88F9CE58D95E9A284E59F8BE9A284E7BDAEEFBC8CE4B8BAE5908EE7BBADE5B9B3E58FB0E9AB98E998B6E883BDE58A9BE8BFADE4BBA3E38081E585A8E696B0E58A9FE883BDE6A8A1E59D97E4B88AE7BABFE7AD91E789A2E5BA95E5B182E69EB6E69E84E6A0B9E59FBAEFBC8CE697A8E59CA8E4B8BAE5B9BFE5A4A7E794A8E688B7E68993E980A0E69EB6E69E84E69BB4E7BB9FE4B880E38081E6938DE4BD9CE69BB4E9A1BAE6898BE38081E68B93E5B195E680A7E69BB4E5BCBAE38081E995BFE69C9FE8BFADE4BBA3E69BB4E7A8B3E5AE9AE79A84E4B880E7AB99E5BC8FE79FA5E8AF86E7AEA1E79086E5B9B3E58FB0E380823C2F703E3C68323EE29CA820E69CACE6ACA1E69BB4E696B0E4BAAEE782B93C2F68323E3C756C3E3C6C693EF09F938B20E585A8E696B0E689B9E9878FE696B0E5A29EE6A0B8E5BF83E4B89AE58AA1E88F9CE58D953C2F6C693E3C6C693EE4B8BAE98082E9858DE5B9B3E58FB0E995BFE69C9FE58A9FE883BDE58D87E7BAA7E8BFADE4BBA3E695B4E4BD93E8A784E58892EFBC8CE5AFB9E7B3BBE7BB9FE9A1B6E5B182E5AFBCE888AAE88F9CE58D95E8BF9BE8A18CE585A8E696B9E4BD8DE9878DE69E84E58D87E7BAA7EFBC8CE690ADE5BBBAE6A8A1E59D97E58C96E79A84E7BB9FE4B880E88F9CE58D95E69EB6E69E84E4BD93E7B3BBE38082E69CACE6ACA1E4BC98E58588E5AE8CE68890E68980E69C89E696B0E5A29EE88F9CE58D95E5898DE58FB0E5B195E7A4BAE9858DE7BDAEE5B7A5E4BD9CEFBC8CE69A82E697A0E9858DE5A597E58685E983A8E5AE9EE6938DE58A9FE883BDE9A1B5E99DA2EFBC8CE782B9E587BBE5AFB9E5BA94E88F9CE58D95E5B086E698BEE7A4BAE58A9FE883BDE5BC80E58F91E4B8ADEFBC8CE59084E7B1BBE585A8E696B0E6A0B8E5BF83E4BDBFE794A8E883BDE58A9BE5908EE7BBADE5B086E68C89E8A784E58892E68C81E7BBADE8BFADE4BBA3E38081E99986E7BBADE4B88AE7BABFEFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE9A696E9A1B5E88F9CE58D95E8B083E695B4EFBC9AE4BF9DE79599E78BACE7AB8BE9A696E9A1B5E4B880E7BAA7E88F9CE58D95EFBC8CE58C85E590ABE9A696E9A1B5E38081E4BAA7E59381E58AA8E68081E5AD90E6A08FE79BAEEFBC8CE58E9FE69C89E9A696E9A1B5E5B195E7A4BAE58685E5AEB9E5AE8CE695B4E4BF9DE79599E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE696B0E5A29E4149E5B7A5E4BD9CE58FB0EFBC9AE696B0E5A29E4149E5B7A5E4BD9CE58FB0E88F9CE58D95EFBC8CE5908EE7BBADE5B086E58685E7BDAE426F74E7AEA1E79086E38081E5B7A5E585B7E7AEA1E79086E6A8A1E59D97EFBC8CE9A284E79599E699BAE883BDE58C96E883BDE58A9BE585A5E58FA3E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE696B0E5A29EE5BA94E794A8E4B8ADE5BF83EFBC9AE696B0E5A29EE5BA94E794A8E4B8ADE5BF83E88F9CE58D95EFBC8CE5908EE7BBADE5B086E58C85E590ABE6A682E8A788E38081E8A7A3E586B3E696B9E6A188E38081E6A8AAE59091E9809AE794A8E5BA94E794A8E38081E7BAB5E59091E8A18CE4B89AE5BA94E794A8E38081E68891E79A84E8A7A3E586B3E696B9E6A188E38081E68891E79A84E5BA94E794A8E7AD89E6A08FE79BAEE380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE696B0E5A29EE79FA5E8AF86E5BA93EFBC9AE696B0E5A29EE79FA5E8AF86E5BA93E7AEA1E79086E88F9CE58D95EFBC8CE5908EE7BBADE5B086E6B6B5E79B96E79FA5E8AF86E5BA93E38081E79FA5E8AF86E58886E7B1BBE38081E79FA5E8AF86E69687E4BBB6E38081E58FACE59B9EE6B58BE8AF95E38081E79FA5E8AF86E5BA93E8AEBEE7BDAEE7AD89E7AEA1E79086E9A1B9E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE696B0E5A29EE68F92E4BBB6E4B8ADE5BF83EFBC9AE696B0E5A29EE68F92E4BBB6E4B8ADE5BF83E88F9CE58D95EFBC8CE5908EE7BBADE5B086E9858DE5A487E68F92E4BBB6E7AEA1E79086E6A8A1E59D97EFBC8CE4B8BAE5B9B3E58FB0E7949FE68081E689A9E5B195E9A284E79599E59FBAE7A180E883BDE58A9BE380823C2F6C693E3C6C693EF09F9BA0EFB88F20E58E9FE69C89E58A9FE883BDE7BB9FE4B880E5BD92E99B86E8B083E695B43C2F6C693E3C6C693EE4B8BAE98082E9858DE585A8E696B0E6A087E58786E58C96E88F9CE58D95E69EB6E69E84EFBC8CE8A784E695B4E585A8E5B9B3E58FB0E58A9FE883BDE58886E5B883E980BBE8BE91EFBC8CE4BC98E58C96E794A8E688B7E697A5E5B8B8E6938DE4BD9CE58AA8E7BABFEFBC8CE5AE9EE78EB0E58A9FE883BDE99B86E4B8ADE58C96E99B86E7BAA6E58C96E7AEA1E79086EFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE5AD98E9878FE58A9FE883BDE8BF81E7A7BBE695B4E59088EFBC9AE7B3BBE7BB9FE58E9FE69C89E585A8E983A8E5B7B2E4B88AE7BABFE58A9FE883BDEFBC8CE7BB9FE4B880E5BD92E99B86E887B3E79FA5E8AF86E59BBEE8B0B1E88F9CE58D95E58685E99B86E4B8ADE7AEA1E79086EFBC8CE58E86E58FB2E695B0E68DAEE38081E9858DE7BDAEE4BFA1E681AFE585A8E983A8E4BF9DE79599EFBC8CE4B88DE5BDB1E5938DE697A5E5B8B8E6ADA3E5B8B8E4BDBFE794A8E380823C2F6C693E3C2F756C3E3C703EF09F8E8920E887B4E8B0A2E4B88EE5B195E69C9B3C2F703E3C703EE6849FE8B0A2E6AF8FE4B880E4BD8DE5BC80E58F91E88085E79A84E58CA0E5BF83E68993E7A3A8E4B88EE6AF8FE4B880E4BD8DE794A8E688B7E79A84E995BFE4B985E4BFA1E4BBBBE999AAE4BCB4EFBC81714B6E6F7720322E302E3020E5B7B2E5AE8CE68890E5B9B3E58FB0E88F9CE58D95E69EB6E69E84E585A8E99DA2E7BB9FE4B880E58D87E7BAA7EFBC8CE4B8BAE5908EE7BBADE9AB98E998B6E58A9FE883BDE8BFADE4BBA3E69BB4E696B0E38081E5B9B3E58FB0E7BBBCE59088E883BDE58A9BE585A8E99DA2E58D87E7BAA7E7AD91E789A2E59D9AE5AE9EE6A0B8E5BF83E6A0B9E59FBAE380823C2F703E3C703EE5908EE7BBADE68891E4BBACE5B086E68C89E785A7E697A2E5AE9AE5BC80E58F91E8BFADE4BBA3E8AEA1E58892EFBC8CE7A8B3E6ADA5E68EA8E8BF9BE59084E696B0E5A29EE88F9CE58D95E9858DE5A597E6A0B8E5BF83E58A9FE883BDE79A84E5BC80E58F91E890BDE59CB0E4B88EE4BC98E58C96E68993E7A3A8EFBC8CE68C81E7BBADE58D87E7BAA7E5B9B3E58FB0E699BAE883BDE58C96E5A484E79086E38081E4B893E4B89AE58C96E79FA5E8AF86E7AEA1E79086E38081E5A49AE58583E58C96E5BA94E794A8E7949FE68081E7AD89E6A0B8E5BF83E883BDE58A9BEFBC8CE4B88DE696ADE5AE8CE59684E4BAA7E59381E4BDBFE794A8E4BD93E9AA8CE380823C2F703E3C703EE7AB8BE58DB3E58D87E7BAA7E887B320714B6E6F7720322E302E30EFBC8CE4BD93E9AA8CE585A8E696B0E69EB6E69E84E4B98BE69785EFBC813C2F703E, '0', '吴同', '2026-05-15 13:48:11', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (8, 'qKnow 2.0.1 稳定版发布', '2', 0x3C68313EF09F9A8020714B6E6F7720322E302E3120E7A8B3E5AE9AE78988E58F91E5B8833C2F68313E3C703E714B6E6F7720322E302E3120E698AFE7BBA720322E302E3020E78988E69CACE5908EE79A84E4B880E6ACA1E9878DE8A681E5BA95E5B182E68A80E69CAFE69EB6E69E84E8B783E8BF81E58D87E7BAA72B4149E6A0B8E5BF83E883BDE58A9BE696B0E5A29EE8BFADE4BBA3EFBC8CE69CACE6ACA1E69BB4E696B0E8819AE784A6E4BA8EE7B3BBE7BB9FE6A0B8E5BF83E68A80E69CAFE6A088E585A8E99DA2E68DA2E4BBA3E38081E8BF90E8A18CE5BA95E5B182E680A7E883BDE5AE89E585A8E5BCBAE58C96E4BBA5E58F8AE585A8E696B04149E6A8A1E59E8BE68EA5E585A5E7AEA1E79086E4BD93E7B3BBE9878DE7A385E4B88AE7BABFE38082E68891E4BBACE585A8E99DA2E5AE8CE68890E5908EE7ABAFE68A80E69CAFE5BA95E5BAA7E78988E69CACE8B7A8E8B68AE5BC8FE58D87E7BAA7EFBC8CE5A4AFE5AE9EE5B9B3E58FB0E9AB98E680A7E883BDE38081E9AB98E5AE89E585A8E38081E9AB98E689A9E5B195E79A84E995BFE69C9FE8BF90E8A18CE6A0B9E59FBAEFBC8CE5908CE6ADA5E696B0E5A29EE4B880E7AB99E5BC8F4149E6A8A1E59E8BE5B882E59CBAE7AEA1E79086E883BDE58A9BEFBC8CE4B8BAE5908EE7BBADE5B9B3E58FB0E699BAE883BDE58C96E6B7B1E5BAA6E8B58BE883BDE380814149E585A8E59CBAE699AFE4B89AE58AA1E890BDE59CB0E7AD91E789A2E6A0B8E5BF83E694AFE69291EFBC8CE697A8E59CA8E4B8BAE5B9BFE5A4A7E794A8E688B7E68993E980A0E68A80E69CAFE5BA95E5BAA7E69BB4E58588E8BF9BE38081E8BF90E8A18CE680A7E883BDE69BB4E5BCBAE58AB2E380814149E68EA5E585A5E69BB4E4BEBFE68DB7E38081E6A8A1E59E8BE7AEA1E79086E69BB4E7BB9FE4B880E79A84E4B880E7AB99E5BC8FE79FA5E8AF86E7AEA1E79086E4B88EE699BAE883BDE5BA94E794A8E5B9B3E58FB0E380823C2F703E3C68323EE29CA820E69CACE6ACA1E69BB4E696B0E4BAAEE782B93C2F68323E3C756C3E3C6C693EF09F92BB20E585A8E99DA2E5AE8CE68890E5BA95E5B182E6A0B8E5BF83E68A80E69CAFE69EB6E69E84E9878DE7A385E58D87E7BAA73C2F6C693E3C6C693EE4B8BAE98082E9858DE5B9B3E58FB0E995BFE69C9FE4BAA7E59381E8BFADE4BBA3E9AB98E680A7E883BDE8BF90E8A18CE8A681E6B182E38081E7B4A7E8B79FE4B8BBE6B581E68A80E69CAFE7949FE68081E6A087E58786E38081E5BCBAE58C96E7B3BBE7BB9FE695B4E4BD93E5AE89E585A8E7A8B3E5AE9AE680A7EFBC8CE5AFB9E5B9B3E58FB0E5908EE7ABAFE6A0B8E5BF83E68A80E69CAFE6A088E8BF9BE8A18CE585A8E9878FE78988E69CACE8BFADE4BBA3E58D87E7BAA7EFBC8CE695B4E4BD93E9878DE69E84E5BA95E5B182E8BF90E8A18CE78EAFE5A283EFBC8CE585A8E9878FE4B89AE58AA1E58A9FE883BDE5AE8CE68890E98082E9858DE585BCE5AEB9E694B9E980A0EFBC8CE58D87E7BAA7E8BF87E7A88BE5B9B3E6BB91E697A0E6849FEFBC8CE4B88DE5BDB1E5938DE58E86E58FB2E4B89AE58AA1E695B0E68DAEE4B88EE78EB0E69C89E58A9FE883BDE6ADA3E5B8B8E4BDBFE794A8EFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE6A0B8E5BF83E6A186E69EB6E58D87E7BAA7EFBC9A537072696E67426F6F7420E78988E69CACE4BB8E20737072696E67626F6F743220E6ADA3E5BC8FE585A8E99DA2E58D87E7BAA7E4B8BA20737072696E67626F6F7433EFBC8CE98082E9858DE585A8E696B0E5BA95E5B182E58685E6A0B8E8A784E88C83EFBC8CE4BC98E58C96E9A1B9E79BAEE69EB6E69E84E4BD93E7B3BBEFBC8CE68F90E58D87E695B4E4BD93E6A186E69EB6E8BF90E8A18CE7A8B3E5AE9AE680A7E4B88EE5908EE7BBADE68A80E69CAFE68B93E5B195E883BDE58A9BE380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE8BF90E8A18CE78EAFE5A283E58D87E7BAA7EFBC9A4A444B20E78988E69CACE4BB8EE88081E697A7206A646B3820E6ADA3E5BC8FE58D87E7BAA7E4B8BA206A646B3137EFBC8CE995BFE69C9FE694AFE68C81E7A8B3E5AE9AE78988E69CACEFBC8CE5A4A7E5B985E4BC98E58C96E58685E5AD98E59B9EE694B6E69CBAE588B6E38081E68F90E58D87E7B3BBE7BB9FE8BF90E8A18CE5938DE5BA94E9809FE5BAA6E38081E5BCBAE58C96E7B3BBE7BB9FE5AE89E585A8E998B2E68AA4E883BDE58A9BEFBC8CE4B8BAE5B9B3E58FB0E9AB98E5B9B6E58F91E38081E5A4A7E5AEB9E9878FE4B89AE58AA1E59CBAE699AFE68F90E4BE9BE59D9AE5AE9EE8BF90E8A18CE5BA95E5BAA7E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE585A8E9878FE585BCE5AEB9E4BF9DE99A9CEFBC9AE68980E69C89E58E86E58FB2E4B89AE58AA1E58A9FE883BDE38081E794A8E688B7E9858DE7BDAEE695B0E68DAEE38081E5B9B3E58FB0E79FA5E8AF86E5BA93E4BFA1E681AFE38081E88F9CE58D95E5B883E5B180E8AEBEE7BDAEE585A8E983A8E5AE8CE695B4E4BF9DE79599EFBC8CE58D87E7BAA7E5898DE5908EE4B89AE58AA1E980BBE8BE91E697A0E58F98E69BB4E38081E4BDBFE794A8E696B9E5BC8FE697A0E58F98E58C96E38081E697A5E5B8B8E6938DE4BD9CE4B88DE58F97E4BBBBE4BD95E5BDB1E5938DE380823C2F6C693E3C6C693EF09FA49620E585A8E696B0E9878DE7A385E696B0E5A29EE6A0B8E5BF834149E6A8A1E59E8BE68EA5E585A5E883BDE58A9BE28094E28094E6A8A1E59E8BE5B882E59CBA3C2F6C693E3C6C693EE4B8BAE58AA0E5BFABE5B9B3E58FB0E699BAE883BDE58C96E883BDE58A9BE5B883E5B180EFBC8CE8A1A5E9BD90E5A49AE58E82E59586E5A4A7E6A8A1E59E8BE7BB9FE4B880E68EA5E585A5E4B88EE99B86E4B8ADE58C96E7AEA1E79086E79FADE69DBFEFBC8CE585A8E696B0E4B88AE7BABFE6A8A1E59E8BE5B882E59CBAE6A0B8E5BF83E58A9FE883BDE88F9CE58D95E4BD93E7B3BBEFBC8CE6A087E58786E58C96E7BB9FE4B880E59084E7B1BBE4B8BBE6B5814149E5A4A7E6A8A1E59E8BE68EA5E585A5E8B7AFE5BE84EFBC8CE7AE80E58C96E6A8A1E59E8BE9858DE7BDAEE38081E590AFE794A8E38081E7AEA1E79086E585A8E6B581E7A88BEFBC8CE8AEA9E794A8E688B7E68C89E99C80E781B5E6B4BBE5AFB9E68EA5E59084E7B1BB4149E883BDE58A9BEFBC8CE5BFABE9809FE8B58BE883BDE79FA5E8AF86E699BAE883BDE5889BE4BD9CE38081E699BAE883BDE997AEE7AD94E38081E79FA5E8AF86E8BE85E58AA9E5A484E79086E7AD89E59CBAE699AFEFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE696B0E5A29EE6A8A1E59E8BE5B882E59CBAE88F9CE58D95EFBC9AE585A8E696B0E5A29EE8AEBEE6A8A1E59E8BE5B882E59CBAE88F9CE58D95EFBC8CE4B88BE8AEBEE6A8A1E59E8BE5B882E59CBAE38081E68891E79A84E6A8A1E59E8BE4B8A4E5A4A7E6A0B8E5BF83E5AD90E88F9CE58D95EFBC8CE695B4E4BD93E88F9CE58D95E4BD93E7B3BBE4B8A5E6A0BCE5AFB9E9BD90322E302E30E6A087E58786E58C96E88F9CE58D95E69EB6E69E84EFBC8CE5B883E5B180E7BB9FE4B880E38081E6938DE4BD9CE980BBE8BE91E4B880E887B4E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE6A8A1E59E8BE5B882E59CBAEFBC9AE99B86E4B8ADE8819AE59088E59084E7B1BBE4B8BBE6B581E59586E794A8E58F8AE69CACE59CB0E5A4A7E6A8A1E59E8BE8B584E6BA90EFBC8CE5BD93E5898DE5B7B2E6ADA3E5BC8FE6B7B1E5BAA6E98082E9858DE694AFE68C81446565705365656BE380814F6C6C616D61E38081E9809AE4B989E58D83E997AEE380814F70656E4149E59B9BE5A4A7E4B8BBE6B5814149E6A8A1E59E8BEFBC8CE5908EE7BBADE5B086E68C81E7BBADE696B0E5A29EE69BB4E5A49AE6A8A1E59E8BE7B1BBE59E8BEFBC8CE4B8B0E5AF8CE5B9B3E58FB0E699BAE883BDE7949FE68081E98089E68BA9E380823C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE68891E79A84E6A8A1E59E8BEFBC9AE4BD9CE4B8BAE4B8AAE4BABAE4B893E5B19EE6A8A1E59E8BE8819AE59088E7AEA1E79086E998B5E59CB0EFBC8CE794A8E688B7E58FAAE99C80E59CA8E6A8A1E59E8BE5B882E59CBAE58685E5AE8CE68890E5AFB9E5BA94E6A8A1E59E8B415049E5AF86E992A5E7AE80E58D95E9858DE7BDAEE4BF9DE5AD98E5908EEFBC8CE5B7B2E9858DE7BDAEE7949FE69588E79A84E6A8A1E59E8BE5B086E887AAE58AA8E5908CE6ADA5E5BD92E99B86E887B3E68891E79A84E6A8A1E59E8BE58897E8A1A8EFBC8CE5AE9EE78EB0E4B880E994AEE5BFABE9809FE8B083E794A8E38081E7BB9FE4B880E7AEA1E79086E38081E4BEBFE68DB7E58887E68DA2E4BDBFE794A8E380823C2F6C693E3C2F756C3E3C703EF09F8E8920E887B4E8B0A2E4B88EE5B195E69C9B3C2F703E3C703EE6849FE8B0A2E6AF8FE4B880E4BD8DE7A094E58F91E4BABAE59198E79A84E6B7B1E88095E68993E7A3A8E4B88EE6AF8FE4B880E4BD8DE794A8E688B7E79A84E4B880E8B7AFE4BFA1E4BBBBE4B88EE995BFE69C9FE999AAE4BCB4EFBC81714B6E6F7720322E302E3120E5B7B2E9A1BAE588A9E5AE8CE68890E5B9B3E58FB0E5BA95E5B182E68A80E69CAFE69EB6E69E84E585A8E99DA2E68DA2E4BBA3E58D87E7BAA7E4B88EE6A0B8E5BF834149E6A8A1E59E8BE7AEA1E79086E883BDE58A9BE585A8E696B0E890BDE59CB0EFBC8CE697A2E5A4AFE5AE9EE4BA86E5B9B3E58FB0E995BFE69C9FE7A8B3E5AE9AE8BF90E8A18CE79A84E68A80E69CAFE6A0B9E59FBAEFBC8CE4B99FE8A1A5E9BD90E4BA86E5B9B3E58FB0E699BAE883BDE58C96E6A0B8E5BF83E883BDE58A9BE79FADE69DBFE380823C2F703E3C703EE5908EE7BBADE68891E4BBACE5B086E68C89E785A7E697A2E5AE9AE4BAA7E59381E8BFADE4BBA3E8A784E58892EFBC8CE68C81E7BBADE4BC98E58C96E6A8A1E59E8BE8B083E794A8E680A7E883BDE38081E689A9E58585E69BB4E5A49A4149E6A8A1E59E8BE8B584E6BA90E38081E68993E7A3A8E699BAE883BDE5BA94E794A8E4BD93E9AA8CEFBC8CE7A8B3E6ADA5E68EA8E8BF9B4149E5B7A5E4BD9CE58FB0E38081E5BA94E794A8E4B8ADE5BF83E38081E79FA5E8AF86E5BA93E7AD89E59084E696B0E5A29EE88F9CE58D95E9858DE5A597E6A0B8E5BF83E58A9FE883BDE5BC80E58F91E890BDE59CB0EFBC8CE68C81E7BBADE5BCBAE58C96E5B9B3E58FB0E699BAE883BDE58C96E5A484E79086E38081E4B893E4B89AE58C96E79FA5E8AF86E7AEA1E79086E38081E5A49AE58583E58C964149E5BA94E794A8E7949FE68081E6A0B8E5BF83E883BDE58A9BEFBC8CE4B88DE696ADE8BFADE4BBA3E4BC98E58C96E4BAA7E59381E4BDBFE794A8E4BD93E9AA8CE380823C2F703E3C703EE7AB8BE58DB3E58D87E7BAA7E887B320714B6E6F7720322E302E31EFBC8CE4BD93E9AA8CE585A8E696B0E58588E8BF9BE68A80E69CAFE69EB6E69E84E4B88EE4B880E7AB99E5BC8F4149E6A8A1E59E8BE5B882E59CBAE7AEA1E79086E696B0E69785E7A88BEFBC813C2F703E, '0', '吴同', '2026-05-15 13:48:26', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (10, 'qKnow 2.0.2 稳定版发布说明', '2', 0x3C68313EF09F9A8020714B6E6F7720322E302E3220E7A8B3E5AE9AE78988E58F91E5B883E8AFB4E6988E3C2F68313E3C68323EF09FA7AD20E78988E69CACE6A682E8BFB03C2F68323E3C703EE69CACE6ACA1E78988E69CACE4BE9DE68998322E302E31E585A8E696B0E9878DE69E84E68A80E69CAFE5BA95E5BAA7EFBC8CE6ADA3E5BC8FE4B88AE7BABFE585A8E9878FE79FA5E8AF86E5BA93E6A0B8E5BF83E883BDE58A9BE38082E9809AE8BF87E890BDE59CB0E5A49AE5BA93E99A94E7A6BBE7AEA1E79086E38081E69687E4BBB6E59091E9878FE8A7A3E69E90E38081E699BAE883BDE58FACE59B9EE6A380E7B4A2E38081E69D83E99990E9858DE7BDAEE4BD93E7B3BBEFBC8CE8A1A5E9BD90E5B9B3E58FB0E4B893E4B89AE58C96E79FA5E8AF86E8B584E4BAA7E7AEA1E79086E79FADE69DBFEFBC8CE5AE9EE78EB0E79FA5E8AF86E4BB8EE4B88AE4BCA0E38081E5BD92E7B1BBE38081E5AD98E582A8E588B0E6A380E7B4A2E79A84E585A8E6B581E7A88BE997ADE78EAFEFBC8CE4B8BAE794A8E688B7E68F90E4BE9BE5AE89E585A8E58FAFE68EA7E38081E699BAE883BDE9AB98E69588E79A84E7A781E69C89E58C96E79FA5E8AF86E7AEA1E79086E69C8DE58AA1E380823C2F703E3C68323EF09F9BA020E4B8BBE8A681E69BB4E696B0E58685E5AEB93C2F68323E3C68333E31EFB88FE283A320E6A0B8E5BF83E883BDE58A9BEFBC9AE5A49AE79FA5E8AF86E5BA93E7AEA1E79086E4BD93E7B3BB3C2F68333E3C756C3E3C6C693EE78BACE7AB8BE79FA5E8AF86E5BA93E99A94E7A6BBEFBC9AE696B0E5A29EE5A49AE79FA5E8AF86E5BA93E5889BE5BBBAE58A9FE883BDEFBC8CE4B88DE5908CE79FA5E8AF86E5BA93E695B0E68DAEE79BB8E4BA92E99A94E7A6BBE38081E8BF90E7BBB4E4BA92E4B88DE5B9B2E689B0EFBC8CE98082E9858DE4BC81E4B89AE58886E983A8E997A8E38081E58886E4B89AE58AA1E79FA5E8AF86E6B289E6B780E59CBAE699AFEFBC9B3C2F6C693E3C6C693EE7B2BEE7BB86E58C96E79FA5E8AF86E58886E7B1BBEFBC9A3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE58D95E79FA5E8AF86E5BA93E58685E694AFE68C81E887AAE5AE9AE4B989E5A49AE7BAA7E58886E7B1BBE79BAEE5BD95EFBC8CE58FAFE68C89E4B89AE58AA1E6A8A1E59D97E38081E69687E4BBB6E7B1BBE59E8BE38081E8A18CE4B89AE7BBB4E5BAA6E8A784E695B4E8B584E69699EFBC9B3C2F6C693E3C6C6920636C6173733D22716C2D696E64656E742D31223EE690ADE5BBBAE6A087E58786E58C96E79FA5E8AF86E7B1BBE79BAEE4BD93E7B3BBEFBC8CE8A7A3E586B3E69687E4BBB6E69D82E4B9B1E38081E6BAAFE6BA90E59BB0E99ABEE79A84E7AEA1E79086E7979BE782B9EFBC9B3C2F6C693E3C2F756C3E3C68333E32EFB88FE283A320E69687E4BBB6E883BDE58A9BEFBC9AE887AAE58AA8E58C96E59091E9878FE8A7A3E69E90E5AD98E582A83C2F68333E3C756C3E3C6C693EE699BAE883BDE69687E4BBB6E5A484E79086EFBC9AE694AFE68C81E5B8B8E794A8E79FA5E8AF86E69687E4BBB6E4B88AE4BCA0EFBC8CE7B3BBE7BB9FE887AAE58AA8E5AE8CE68890E69687E69CACE68F90E58F96E38081E58685E5AEB9E68B86E58886E38081E59091E9878FE8BDACE58C96EFBC9B3C2F6C693E3C6C693EE59091E9878FE68C81E4B985E58C96E585A5E5BA93EFBC9AE5A484E79086E5908EE79A84E59091E9878FE695B0E68DAEE5AD98E585A5E4B893E5B19EE79FA5E8AF86E5BA93EFBC8CE4B8BAE699BAE883BDE6A380E7B4A2E380814149E997AEE7AD94E68F90E4BE9BE5BA95E5B182E695B0E68DAEE694AFE69291EFBC9B3C2F6C693E3C2F756C3E3C68333E33EFB88FE283A320E6A380E7B4A2E883BDE58A9BEFBC9AE58FACE59B9EE6B58BE8AF95E7B2BEE58786E6BAAFE6BA903C2F68333E3C756C3E3C6C693EE699BAE883BDE59091E9878FE58CB9E9858DEFBC9AE694AFE68C81E585B3E994AEE8AF8DE38081E887AAE784B6E8AFADE8A880E6A380E7B4A2EFBC8CE5BFABE9809FE58CB9E9858DE79FA5E8AF86E5BA93E58685E585B3E88194E79FA5E8AF86E78987E6AEB5EFBC9B3C2F6C693E3C6C693EE6BA90E5A4B4E7B2BEE58786E6BAAFE6BA90EFBC9AE6A380E7B4A2E7BB93E69E9CE58FAFE5AE9AE4BD8DE58E9FE5A78BE4B88AE4BCA0E69687E4BBB6EFBC8CE6A087E6B3A8E585B3E88194E6AEB5E890BDEFBC8CE6A380E7B4A2E8BF87E7A88BE9808FE6988EE58FAFE69FA5EFBC9B3C2F6C693E3C2F756C3E3C68333E34EFB88FE283A320E7AEA1E68EA7E883BDE58A9BEFBC9AE585A8E7BBB4E5BAA6E79FA5E8AF86E5BA93E9858DE7BDAE3C2F68333E3C756C3E3C6C693EE59FBAE7A180E8AEBEE7BDAEEFBC9AE694AFE68C81E887AAE5AE9AE4B989E79FA5E8AF86E5BA93E5908DE7A7B0E38081E68F8FE8BFB0EFBC8CE5AE8CE59684E59FBAE7A180E6A1A3E6A188E4BFA1E681AFEFBC9B3C2F6C693E3C6C693EE69D83E99990E8AEBEE7BDAEEFBC9AE7B2BEE7BB86E58C96E9858DE7BDAEE8A792E889B2E8AEBFE997AEE4B88EE6938DE4BD9CE69D83E99990EFBC8CE4BF9DE99A9CE6A0B8E5BF83E79FA5E8AF86E8B584E4BAA7E5AE89E585A8EFBC9B3C2F6C693E3C6C693EE6A380E7B4A2E8AEBEE7BDAEEFBC9AE887AAE5AE9AE4B989E6A380E7B4A2E69D83E9878DE38081E58CB9E9858DE8A784E58899EFBC8CE98082E9858DE5A49AE6A0B7E58C96E6A380E7B4A2E99C80E6B182EFBC9B3C2F6C693E3C6C693EE588A0E999A4E8AEBEE7BDAEEFBC9AE694AFE68C81E79FA5E8AF86E5BA93E4B880E994AEE588A0E999A4EFBC8CE5BFABE9809FE9878AE694BEE7B3BBE7BB9FE8B584E6BA90EFBC9B3C2F6C693E3C2F756C3E3C68323EF09F938C20E78988E69CACE680BBE7BB933C2F68323E3C703EE69CACE6ACA1E78988E69CACE6A0B8E5BF83E5AE8CE68890E4BA8620E7A781E69C89E58C96E79FA5E8AF86E5BA93E4B89AE58AA1E997ADE78EAF20E79A84E69E84E5BBBAE38082E4BE9DE68998E4B88AE4B880E78988E69CACE58D87E7BAA7E79A84E68A80E69CAFE5BA95E5BAA7EFBC8CE5AE9EE78EB0E79FA5E8AF86E5BA93E4BB8EE9A284E59F8BE78AB6E68081E588B0E6ADA3E5BC8FE59586E794A8E79A84E883BDE58A9BE890BDE59CB0E38082E5A49AE5BA93E99A94E7A6BBE883BDE58A9BE6BBA1E8B6B3E4BC81E4B89AE58886E7BAA7E7AEA1E68EA7E8AF89E6B182EFBC8CE59091E9878FE8A7A3E69E90E4B88EE58FACE59B9EE6A380E7B4A2E8B58BE883BDE79FA5E8AF86E699BAE883BDE8B083E58F96EFBC8CE7B2BEE7BB86E58C96E9858DE7BDAEE4BF9DE99A9CE695B0E68DAEE5AE89E585A8E58FAFE68EA7E38082E69CACE78988E69CACE8A1A5E9BD90E5B9B3E58FB0E79FA5E8AF86E7AEA1E79086E6A0B8E5BF83E883BDE58A9BEFBC8CE69E84E5BBBAE8B5B7E69BB4E58AA0E4B893E4B89AE38081E7A8B3E5AE9AE38081E69893E794A8E79A84E7A781E69C89E58C96E79FA5E8AF86E8B584E4BAA7E7AEA1E79086E696B0E4BD93E9AA8CE380823C2F703E, '0', '吴同', '2026-05-15 13:48:38', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (11, 'qKnow 2.1.0 稳定版发布说明', '2', 0x3C68313EF09F9A8020714B6E6F7720322E312E3020E7A8B3E5AE9AE78988E58F91E5B883E8AFB4E6988E3C2F68313E3C68323EF09FA7AD20E78988E69CACE6A682E8BFB03C2F68323E3C703EE69CACE6ACA1E78988E69CACE4B8BAE79FA5E8AF86E5BA93E68890E7869FE8BFADE4BBA3E5908EE79A844149E7BC96E68E92E883BDE58A9BE9878DE7A385E58D87E7BAA7E78988E69CACEFBC8CE8819AE784A64149E5B7A5E4BD9CE58FB0E4BD93E7B3BBE689A9E5AEB9E38081E699BAE883BD426F74E7BC96E68E92E890BDE59CB0E38081E887AAE5AE9AE4B989E5B7A5E585B7E7949FE68081E690ADE5BBBAE38082E4BE9DE68998E5898DE69C9FE5AE8CE59684E79A84E79FA5E8AF86E5BA93E5BA95E5B182E69EB6E69E84EFBC8CE696B0E5A29E426F74E7AEA1E79086E38081E5B7A5E585B7E7AEA1E79086E4B8A4E5A4A7E6A0B8E5BF83E88F9CE58D95EFBC8CE5AE8CE695B4E890BDE59CB0E5B7A5E4BD9CE6B581E3808143686174666C6F77E380814167656E74E4B889E5A4A7E7BC96E68E92E883BDE58A9BEFBC8CE5AE9EE78EB0E887AAE5AE9AE4B989E5B7A5E585B7E5BC80E58F91E4B88EE699BAE883BDE4BBA3E79086E88194E58AA8E8B083E794A8E38082E8A1A5E9BD90E5B9B3E58FB04149E699BAE883BDE58C96E7BC96E68E92E79FADE69DBFEFBC8CE68993E980A0426F74E887AAE794B1E7BC96E68E92E38081E6B581E7A88BE58FAFE8A786E58C96E9858DE7BDAEE380814167656E74E699BAE883BDE88194E58AA8E38081E5B7A5E585B7E887AAE5AE9AE4B989E68B93E5B195E79A84E4B880E7AB99E5BC8FE7A781E69C89E58C9641492BE79FA5E8AF86E8B584E4BAA7E7AEA1E79086E5B9B3E58FB0E380823C2F703E3C68323EF09F9BA020E4B8BBE8A681E69BB4E696B0E58685E5AEB93C2F68323E3C68333E31EFB88FE283A320E6A0B8E5BF83E883BDE58A9BEFBC9A426F74E7AEA1E79086E7BC96E68E92E4BD93E7B3BB3C2F68333E3C756C3E3C6C693E426F74E7BB9FE4B880E7AEA1E79086E88F9CE58D95EFBC9A4149E5B7A5E4BD9CE58FB0E696B0E5A29E426F74E7AEA1E79086E4B893E5B19EE88F9CE58D95EFBC8CE8819AE59088E5B7A5E4BD9CE6B581E3808143686174666C6F77E380814167656E74E4B889E5A4A7E6A0B8E5BF83E7BC96E68E92E6A8A1E59D97EFBC8CE5AE9EE78EB04149E7BC96E68E92E883BDE58A9BE997ADE78EAFEFBC8CE98082E9858DE887AAE58AA8E58C96E6B581E7A88BE38081E699BAE883BDE5AFB9E8AF9DE38081E4B893E5B19EE4BBA3E79086E7AD89E4B89AE58AA1E59CBAE699AFEFBC9B3C2F6C693E3C6C693EE585A8E6B581E7A88BE7BC96E68E92E7AEA1E68EA7EFBC9AE68980E69C89426F74E9858DE7BDAEE38081E7BC96E68E92E38081E8B083E8AF95E38081E5A48DE588B6E6938DE4BD9CE7BB9FE4B880E997ADE78EAFE7AEA1E79086EFBC8CE6938DE4BD9CE8B7AFE5BE84E6B885E699B0E38081E980BBE8BE91E7BB9FE4B880EFBC8CE9998DE4BD8E4149E5BA94E794A8E690ADE5BBBAE68890E69CACE380823C2F6C693E3C2F756C3E3C68333E32EFB88FE283A320E5B7A5E4BD9CE6B581E58FAFE8A786E58C96E7BC96E68E923C2F68333E3C756C3E3C6C693EE887AAE794B1E88A82E782B9E68BBCE8A385EFBC9AE694AFE68C81E4B89AE58AA1E88A82E782B9E887AAE794B1E98089E68BA9E380814149E6A8A1E59E8BE781B5E6B4BBE98089E9858DEFBC8CE68C89E99C80E68BBCE8A385E5AE9AE588B6E4B893E5B19EE4B89AE58AA1E6B581E7A88BEFBC9B3C2F6C693E3C6C693EE5AE8CE695B4E8BF90E7BBB4E883BDE58A9BEFBC9AE68F90E4BE9BE59CA8E7BABFE8B083E8AF95E38081E4B880E994AEE5A48DE588B6E38081E9858DE7BDAEE4BF9DE5AD98E883BDE58A9BEFBC8CE694AFE68C81E6B581E7A88BE6A0A1E9AA8CE38081E696B9E6A188E5A48DE794A8E38081E5BFABE9809FE8BFADE4BBA3EFBC8CE68F90E58D87E4B89AE58AA1E6B581E7A88BE690ADE5BBBAE69588E78E87E380823C2F6C693E3C2F756C3E3C68333E33EFB88FE283A3204167656E74E699BAE883BDE4BBA3E79086E9858DE7BDAE3C2F68333E3C756C3E3C6C693EE5A49AE7BBB4E8B584E6BA90E7BB91E5AE9AEFBC9AE694AFE68C81E6A8A1E59E8BE98089E68BA9E38081E7A781E69C89E79FA5E8AF86E5BA93E585B3E88194E38081E887AAE5AE9AE4B989E5B7A5E585B7E7BB91E5AE9AEFBC9B3C2F6C693E3C6C693EE59CA8E7BABFE5AFB9E8AF9DE8B083E8AF95EFBC9AE9858DE7BDAEE5AE8CE68890E5908EE58FAFE5AE9EE697B6E5AFB9E8AF9DE6B58BE8AF95EFBC8CE6A0A1E9AA8CE997AEE7AD94E5938DE5BA94E38081E79FA5E8AF86E5BA93E58FACE59B9EE38081E5B7A5E585B7E8B083E794A8E69588E69E9CEFBC8CE5BFABE9809FE98082E9858DE4B89AE58AA1E4BDBFE794A8E59CBAE699AFE380823C2F6C693E3C2F756C3E3C68333E34EFB88FE283A32043686174666C6F77E5AFB9E8AF9DE6B581E7A88BE9858DE7BDAE3C2F68333E3C756C3E3C6C693EE58FAFE8A786E58C96E5AFB9E8AF9DE7BC96E68E92EFBC9AE6B2BFE794A8E5B7A5E4BD9CE6B581E7BC96E68E92E6A8A1E5BC8FEFBC8CE9809AE8BF87E88A82E782B9E68BBCE8A385E8AEBEE8AEA1E5A49AE8BDAEE5AFB9E8AF9DE38081E58886E694AFE8B7B3E8BDACE38081E4BAA4E4BA92E980BBE8BE91EFBC9B3C2F6C693E3C6C693EE5AFB9E8AF9DE6B581E7A88BE8B083E8AF95EFBC9AE694AFE68C81E5AE9EE697B6E5AFB9E8AF9DE6A8A1E68B9FEFBC8CE8A782E6B58BE58886E694AFE6B581E8BDACE4B88EE59B9EE5A48DE69588E69E9CEFBC8CE4BC98E58C96E8AF9DE69CAFE980BBE8BE91EFBC8CE98082E9858DE5A48DE69D82E592A8E8AFA2E997AEE7AD94E59CBAE699AFE380823C2F6C693E3C2F756C3E3C68333E35EFB88FE283A320E887AAE5AE9AE4B989E5B7A5E585B7E7AEA1E79086E7949FE680813C2F68333E3C756C3E3C6C693EE78BACE7AB8BE5B7A5E585B7E7AEA1E79086E88F9CE58D95EFBC9AE696B0E5A29EE5B7A5E585B7E7AEA1E79086E6A8A1E59D97EFBC8CE694AFE68C81E794A8E688B7E887AAE4B8BBE7BC96E58699E38081E9858DE7BDAEE4B89AE58AA1E7A781E69C89E5B7A5E585B7EFBC8CE6B289E6B780E4BC81E4B89AE5B7A5E585B7E8B584E4BAA7EFBC9B3C2F6C693E3C6C693E4167656E74E88194E58AA8E8B083E794A8EFBC9AE5B7B2E9858DE7BDAEE5B7A5E585B7E58FAFE79BB4E68EA5E7BB91E5AE9AE887B34167656E74EFBC8CE781B5E6B4BBE68B93E5B195E699BAE883BDE4BBA3E79086E4B89AE58AA1E8BEB9E7958CEFBC8CE6BBA1E8B6B3E4B8AAE680A7E58C96E5A48DE69D82E887AAE58AA8E58C96E99C80E6B182E380823C2F6C693E3C2F756C3E3C68323EF09F938C20E78988E69CACE680BBE7BB933C2F68323E3C703EE69CACE6ACA1E78988E69CACE6A0B8E5BF83E5AE8CE68890E4BA86204149E7BC96E68E922BE887AAE5AE9AE4B989E5B7A5E585B7E7949FE68081E997ADE78EAF20E79A84E69E84E5BBBAE38082E5B9B3E58FB0E794B1E58D95E4B880E79FA5E8AF86E7AEA1E79086E883BDE58A9BE58D87E7BAA7E4B8BAE79FA5E8AF862B4149E7BC96E68E922BE887AAE5AE9AE4B989E5B7A5E585B7E4B880E4BD93E58C96E699BAE883BDE5B9B3E58FB0EFBC8CE5AE8CE59684E7A781E69C89E58C96E59CBAE699AFE4B88BE79A84E781B5E6B4BBE7BC96E68E92E38081E4BA8CE6ACA1E68B93E5B195E883BDE58A9BE38082E4BE9DE68998E58FAFE8A786E58C96426F74E4BD93E7B3BBEFBC8CE9998DE4BD8E4149E5BA94E794A8E690ADE5BBBAE997A8E6A79BEFBC8CE5B8AEE58AA9E4BC81E4B89AE5BFABE9809FE890BDE59CB0E887AAE58AA8E58C96E6B581E7A88BE38081E699BAE883BDE5AFB9E8AF9DE38081E4B893E5B19EE4BBA3E79086E883BDE58A9BEFBC8CE69E84E5BBBAE69BB4E58AA0E781B5E6B4BBE38081E58FAFE68B93E5B195E38081E9AB98E98082E9858DE79A84E7A781E69C89E58C96E699BAE883BDE5BA94E794A8E7949FE68081E380823C2F703E, '0', '吴同', '2026-05-15 13:48:58', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (12, 'qKnow 2.1.1 发布说明', '2', 0x3C68313EF09F9A8020714B6E6F7720322E312E3120E58F91E5B883E8AFB4E6988E3C2F68313E3C68323EF09FA7AD20E78988E69CACE6A682E8BFB03C2F68323E3C703EE69CACE6ACA1E78988E69CACE4B8BAE79FA5E8AF86E59BBEE8B0B1E68ABDE58F96E4B893E9A1B9E4BC98E58C96E8BFADE4BBA3E78988E69CACEFBC8CE8819AE784A6E99D9EE7BB93E69E84E58C96E68ABDE58F96E883BDE58A9BE58D87E7BAA7E4B88EE68ABDE58F96E5BC95E6938EE781B5E6B4BBE98082E9858DE38082E59FBAE4BA8EE4B88AE69C9F426F74E5B7A5E4BD9CE6B581E5BA95E5B182E883BDE58A9BEFBC8CE9878DE69E84E79FA5E8AF86E59BBEE8B0B1E68ABDE58F96E980BBE8BE91EFBC8CE5AE9EE78EB0E68ABDE58F96E5BC95E6938EE58FAFE58AA8E68081E58887E68DA2EFBC8CE5B9B6E689A9E58585E7BB93E69E84E58C96E695B0E68DAEE5BA93E98082E9858DE88C83E59BB4E38082E8BF9BE4B880E6ADA5E5BCBAE58C96E5B9B3E58FB0E7BB93E69E84E58C96E79FA5E8AF86E89083E58F96E883BDE58A9BEFBC8CE68F90E58D87E4BC81E4B89AE4B89AE58AA1E695B0E68DAEE59091E79FA5E8AF86E59BBEE8B0B1E8BDACE58C96E79A84E585BCE5AEB9E680A7E4B88EE781B5E6B4BBE680A7EFBC8CE5AE8CE59684E7A781E69C89E58C96E59CBAE699AFE4B88BE5A49AE6BA90E7BB93E69E84E58C96E695B0E68DAEE79A84E79FA5E8AF86E69E84E5BBBAE883BDE58A9BE380823C2F703E3C68323EF09F9BA020E4B8BBE8A681E69BB4E696B0E58685E5AEB93C2F68323E3C68333E31EFB88FE283A320E79FA5E8AF86E59BBEE8B0B1E68ABDE58F96E5BC95E6938EE4BC98E58C963C2F68333E3C756C3E3C6C693EE58F8CE68ABDE58F96E5BC95E6938EE58FAFE58887E68DA2EFBC9AE79FA5E8AF86E59BBEE8B0B1E68ABDE58F96E6A8A1E59D97E696B0E5A29EE5BC95E6938EE58887E68DA2E883BDE58A9BEFBC8CE58E9FE7949FE694AFE68C81204C4C4DE68ABDE58F9620E4B88E20446565704B45E68ABDE58F9620E4B8A4E7A78DE6A8A1E5BC8FE887AAE794B1E58887E68DA2EFBC8CE98082E9858DE4B88DE5908CE69687E69CACE68ABDE58F96E38081E79FA5E8AF86E89083E58F96E4B89AE58AA1E59CBAE699AFEFBC9B3C2F6C693E3C6C693EE5A48DE794A8426F74E695B0E68DAEE5B7A5E4BD9CE6B581E883BDE58A9BEFBC9A4C4C4DE68ABDE58F96E883BDE58A9BE6B7B1E5BAA6E5A48DE794A8E5B9B3E58FB0426F74E4BD93E7B3BBE58685E5B7B2E890BDE59CB0E79A84E695B0E68DAEE5B7A5E4BD9CE6B581E7B1BBE59E8BEFBC8CE68993E9809A4149E7BC96E68E92E993BEE8B7AFEFBC8CE5A48DE794A8E78EB0E69C89E6A8A1E59E8BE8B083E5BAA6E38081E6B581E7A88BE9858DE7BDAEE38081E4BBBBE58AA1E689A7E8A18CE883BDE58A9BEFBC8CE69EB6E69E84E7BB9FE4B880E4B894E585BCE5AEB9E680A7E69BB4E5BCBAE380823C2F6C693E3C2F756C3E3C68333E32EFB88FE283A320E7BB93E69E84E58C96E695B0E68DAEE6BA90E689A9E5AEB9E98082E9858D3C2F68333E3C756C3E3C6C693EE696B0E5A29EE4B8BBE6B581E695B0E68DAEE5BA93E98082E9858DEFBC9AE7BB93E69E84E58C96E68ABDE58F96E883BDE58A9BE68B93E5B195E695B0E68DAEE6BA90E7B1BBE59E8BEFBC8CE6ADA3E5BC8FE694AFE68C81204D7953514CE380814F7261636C6520E4B8A4E5A4A7E7B1BBE4BC81E4B89AE5B8B8E794A8E585B3E7B3BBE59E8BE695B0E68DAEE5BA93EFBC9B3C2F6C693E3C6C693EE7BB93E69E84E58C96E79FA5E8AF86E5BFABE9809FE89083E58F96EFBC9AE694AFE68C81E79BB4E8BF9EE695B0E68DAEE5BA93E689B9E9878FE68ABDE58F96E7BB93E69E84E58C96E4B89AE58AA1E695B0E68DAEEFBC8CE887AAE58AA8E8A7A3E69E90E695B0E68DAEE8A1A8E7BB93E69E84E38081E5AD97E6AEB5E585B3E88194E585B3E7B3BBEFBC8CE5BFABE9809FE7949FE68890E6A087E58786E58C96E79FA5E8AF86E59BBEE8B0B1E5AE9EE4BD93E4B88EE585B3E7B3BBE380823C2F6C693E3C2F756C3E3C68323EF09F938C20E78988E69CACE680BBE7BB933C2F68323E3C703EE69CACE6ACA1E78988E69CACE6A0B8E5BF83E5AE8CE68890E79FA5E8AF86E59BBEE8B0B1E68ABDE58F96E5BC95E6938EE5A49AE58583E58C962BE7BB93E69E84E58C96E695B0E68DAEE6BA90E689A9E5AEB9E883BDE58A9BE58D87E7BAA7E38082E4BE9DE68998E5B7B2E68890E59E8BE79A84426F74E5B7A5E4BD9CE6B581E5BA95E5BAA7E8B58BE883BD4C4C4DE68ABDE58F96E883BDE58A9BEFBC8CE5AE9EE78EB0E699BAE883BDE58C96E68ABDE58F96E4B88EE4BCA0E7BB9FE6A8A1E59E8BE68ABDE58F96E58F8CE6A8A1E5BC8FE5B9B6E8A18CEFBC9BE5908CE697B6E98082E9858D4D7953514CE380814F7261636C65E695B0E68DAEE5BA93EFBC8CE5A4A7E5B985E68B93E5AEBDE4BC81E4B89AE4B89AE58AA1E7BB93E69E84E58C96E695B0E68DAEE68EA5E585A5E88C83E59BB4E38082E68C81E7BBADE5BCBAE58C96E5B9B3E58FB0E79FA5E8AF86E89083E58F96E5BA95E5BAA7EFBC8CE4B8BAE5908EE7BBADE5A48DE69D82E79FA5E8AF86E69E84E5BBBAE38081E5A49AE6BA90E695B0E68DAEE89E8DE59088E38081E699BAE883BDE58C96E79FA5E8AF86E5BA94E794A8E68F90E4BE9BE69BB4E7A8B3E5AE9AE38081E781B5E6B4BBE79A84E5BA95E5B182E694AFE69291E380823C2F703E, '0', '吴同', '2026-05-15 13:49:11', '', NULL, NULL);

-- ----------------------------
-- Table structure for system_post
-- ----------------------------
DROP TABLE IF EXISTS `system_post`;
CREATE TABLE `system_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_post
-- ----------------------------
INSERT INTO `system_post` VALUES (1, 'CEO', '董事长', 1, '0', '吴同', '2026-04-20 09:32:53', '吴同', '2026-06-03 11:13:05', '');
INSERT INTO `system_post` VALUES (5, 'DM', '部门经理', 2, '0', '吴同', '2026-06-03 11:13:24', '', NULL, NULL);
INSERT INTO `system_post` VALUES (6, 'AR', '架构师', 3, '0', '吴同', '2026-06-03 11:13:39', '', NULL, NULL);
INSERT INTO `system_post` VALUES (7, 'SE', '软件工程师', 4, '0', '吴同', '2026-06-03 11:13:57', '', NULL, NULL);
INSERT INTO `system_post` VALUES (8, 'PM', '产品经理', 5, '0', '吴同', '2026-06-03 11:14:16', '', NULL, NULL);
INSERT INTO `system_post` VALUES (9, 'QA', '测试工程师', 6, '0', '吴同', '2026-06-03 11:14:25', '', NULL, NULL);
INSERT INTO `system_post` VALUES (10, 'OP', '运维工程师', 7, '0', '吴同', '2026-06-03 11:14:39', '', NULL, NULL);

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', '吴同', '2026-04-20 09:32:53', '', NULL, '超级管理员');
INSERT INTO `system_role` VALUES (2, '普通角色', 'common', 3, '2', 1, 1, '0', '0', '吴同', '2026-04-20 09:32:53', '吴同', '2026-06-03 11:15:39', '普通角色');
INSERT INTO `system_role` VALUES (100, '系统管理员', 'system', 2, '1', 1, 1, '0', '0', '吴同', '2026-06-03 11:12:40', '超级管理员', '2026-06-29 16:06:48', NULL);

-- ----------------------------
-- Table structure for system_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_role_dept`;
CREATE TABLE `system_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_role_menu
-- ----------------------------
INSERT INTO `system_role_menu` VALUES (2, 1);
INSERT INTO `system_role_menu` VALUES (2, 2);
INSERT INTO `system_role_menu` VALUES (2, 110);
INSERT INTO `system_role_menu` VALUES (2, 1049);
INSERT INTO `system_role_menu` VALUES (2, 1050);
INSERT INTO `system_role_menu` VALUES (2, 1051);
INSERT INTO `system_role_menu` VALUES (2, 1052);
INSERT INTO `system_role_menu` VALUES (2, 1053);
INSERT INTO `system_role_menu` VALUES (2, 1054);
INSERT INTO `system_role_menu` VALUES (2, 2000);
INSERT INTO `system_role_menu` VALUES (2, 2001);
INSERT INTO `system_role_menu` VALUES (2, 2002);
INSERT INTO `system_role_menu` VALUES (2, 2003);
INSERT INTO `system_role_menu` VALUES (2, 2004);
INSERT INTO `system_role_menu` VALUES (2, 2005);
INSERT INTO `system_role_menu` VALUES (2, 2006);
INSERT INTO `system_role_menu` VALUES (2, 2007);
INSERT INTO `system_role_menu` VALUES (2, 2008);
INSERT INTO `system_role_menu` VALUES (2, 2009);
INSERT INTO `system_role_menu` VALUES (2, 2010);
INSERT INTO `system_role_menu` VALUES (2, 2011);
INSERT INTO `system_role_menu` VALUES (2, 2012);
INSERT INTO `system_role_menu` VALUES (2, 2013);
INSERT INTO `system_role_menu` VALUES (2, 2014);
INSERT INTO `system_role_menu` VALUES (2, 2015);
INSERT INTO `system_role_menu` VALUES (2, 2016);
INSERT INTO `system_role_menu` VALUES (2, 2017);
INSERT INTO `system_role_menu` VALUES (2, 2018);
INSERT INTO `system_role_menu` VALUES (2, 2019);
INSERT INTO `system_role_menu` VALUES (2, 2020);
INSERT INTO `system_role_menu` VALUES (2, 2021);
INSERT INTO `system_role_menu` VALUES (2, 2022);
INSERT INTO `system_role_menu` VALUES (2, 2023);
INSERT INTO `system_role_menu` VALUES (2, 2024);
INSERT INTO `system_role_menu` VALUES (2, 2025);
INSERT INTO `system_role_menu` VALUES (2, 2026);
INSERT INTO `system_role_menu` VALUES (2, 2027);
INSERT INTO `system_role_menu` VALUES (2, 2028);
INSERT INTO `system_role_menu` VALUES (2, 2029);
INSERT INTO `system_role_menu` VALUES (2, 2030);
INSERT INTO `system_role_menu` VALUES (2, 2031);
INSERT INTO `system_role_menu` VALUES (2, 2032);
INSERT INTO `system_role_menu` VALUES (2, 2033);
INSERT INTO `system_role_menu` VALUES (2, 2034);
INSERT INTO `system_role_menu` VALUES (2, 2035);
INSERT INTO `system_role_menu` VALUES (2, 2036);
INSERT INTO `system_role_menu` VALUES (2, 2037);
INSERT INTO `system_role_menu` VALUES (2, 2038);
INSERT INTO `system_role_menu` VALUES (2, 2039);
INSERT INTO `system_role_menu` VALUES (2, 2040);
INSERT INTO `system_role_menu` VALUES (2, 2041);
INSERT INTO `system_role_menu` VALUES (2, 2042);
INSERT INTO `system_role_menu` VALUES (2, 2043);
INSERT INTO `system_role_menu` VALUES (2, 2044);
INSERT INTO `system_role_menu` VALUES (2, 2045);
INSERT INTO `system_role_menu` VALUES (2, 2046);
INSERT INTO `system_role_menu` VALUES (2, 2047);
INSERT INTO `system_role_menu` VALUES (2, 2048);
INSERT INTO `system_role_menu` VALUES (2, 2049);
INSERT INTO `system_role_menu` VALUES (2, 2050);
INSERT INTO `system_role_menu` VALUES (2, 2051);
INSERT INTO `system_role_menu` VALUES (2, 2052);
INSERT INTO `system_role_menu` VALUES (2, 2053);
INSERT INTO `system_role_menu` VALUES (2, 2054);
INSERT INTO `system_role_menu` VALUES (2, 2055);
INSERT INTO `system_role_menu` VALUES (2, 2057);
INSERT INTO `system_role_menu` VALUES (2, 2058);
INSERT INTO `system_role_menu` VALUES (2, 2061);
INSERT INTO `system_role_menu` VALUES (2, 2062);
INSERT INTO `system_role_menu` VALUES (2, 2063);
INSERT INTO `system_role_menu` VALUES (2, 2064);
INSERT INTO `system_role_menu` VALUES (2, 2079);
INSERT INTO `system_role_menu` VALUES (2, 2080);
INSERT INTO `system_role_menu` VALUES (2, 2123);
INSERT INTO `system_role_menu` VALUES (2, 2124);
INSERT INTO `system_role_menu` VALUES (2, 2125);
INSERT INTO `system_role_menu` VALUES (2, 2126);
INSERT INTO `system_role_menu` VALUES (2, 2127);
INSERT INTO `system_role_menu` VALUES (2, 2128);
INSERT INTO `system_role_menu` VALUES (2, 2129);
INSERT INTO `system_role_menu` VALUES (2, 2130);
INSERT INTO `system_role_menu` VALUES (2, 2131);
INSERT INTO `system_role_menu` VALUES (2, 2190);
INSERT INTO `system_role_menu` VALUES (2, 2191);
INSERT INTO `system_role_menu` VALUES (2, 2192);
INSERT INTO `system_role_menu` VALUES (2, 2193);
INSERT INTO `system_role_menu` VALUES (2, 2194);
INSERT INTO `system_role_menu` VALUES (2, 2195);
INSERT INTO `system_role_menu` VALUES (2, 2196);
INSERT INTO `system_role_menu` VALUES (2, 2223);
INSERT INTO `system_role_menu` VALUES (2, 2224);
INSERT INTO `system_role_menu` VALUES (2, 2225);
INSERT INTO `system_role_menu` VALUES (2, 2226);
INSERT INTO `system_role_menu` VALUES (2, 2227);
INSERT INTO `system_role_menu` VALUES (2, 2228);
INSERT INTO `system_role_menu` VALUES (2, 2229);
INSERT INTO `system_role_menu` VALUES (2, 2230);
INSERT INTO `system_role_menu` VALUES (2, 2231);
INSERT INTO `system_role_menu` VALUES (2, 2232);
INSERT INTO `system_role_menu` VALUES (2, 2233);
INSERT INTO `system_role_menu` VALUES (2, 2234);
INSERT INTO `system_role_menu` VALUES (2, 2235);
INSERT INTO `system_role_menu` VALUES (2, 2236);
INSERT INTO `system_role_menu` VALUES (2, 2237);
INSERT INTO `system_role_menu` VALUES (2, 2262);
INSERT INTO `system_role_menu` VALUES (2, 2315);
INSERT INTO `system_role_menu` VALUES (2, 2316);
INSERT INTO `system_role_menu` VALUES (2, 2317);
INSERT INTO `system_role_menu` VALUES (2, 2318);
INSERT INTO `system_role_menu` VALUES (2, 2322);
INSERT INTO `system_role_menu` VALUES (2, 2323);
INSERT INTO `system_role_menu` VALUES (2, 2324);
INSERT INTO `system_role_menu` VALUES (2, 2325);
INSERT INTO `system_role_menu` VALUES (2, 2335);
INSERT INTO `system_role_menu` VALUES (2, 2336);
INSERT INTO `system_role_menu` VALUES (2, 2337);
INSERT INTO `system_role_menu` VALUES (2, 2338);
INSERT INTO `system_role_menu` VALUES (2, 2339);
INSERT INTO `system_role_menu` VALUES (2, 2395);
INSERT INTO `system_role_menu` VALUES (2, 2396);
INSERT INTO `system_role_menu` VALUES (2, 2397);
INSERT INTO `system_role_menu` VALUES (2, 2398);
INSERT INTO `system_role_menu` VALUES (2, 2399);
INSERT INTO `system_role_menu` VALUES (2, 2400);
INSERT INTO `system_role_menu` VALUES (2, 2401);
INSERT INTO `system_role_menu` VALUES (2, 2402);
INSERT INTO `system_role_menu` VALUES (2, 2403);
INSERT INTO `system_role_menu` VALUES (2, 2404);
INSERT INTO `system_role_menu` VALUES (2, 2405);
INSERT INTO `system_role_menu` VALUES (2, 2407);
INSERT INTO `system_role_menu` VALUES (2, 2408);
INSERT INTO `system_role_menu` VALUES (2, 2410);
INSERT INTO `system_role_menu` VALUES (2, 2411);
INSERT INTO `system_role_menu` VALUES (2, 2412);
INSERT INTO `system_role_menu` VALUES (2, 2413);
INSERT INTO `system_role_menu` VALUES (2, 2414);
INSERT INTO `system_role_menu` VALUES (2, 2415);
INSERT INTO `system_role_menu` VALUES (2, 2416);
INSERT INTO `system_role_menu` VALUES (2, 2417);
INSERT INTO `system_role_menu` VALUES (2, 2418);
INSERT INTO `system_role_menu` VALUES (100, 1);
INSERT INTO `system_role_menu` VALUES (100, 101);
INSERT INTO `system_role_menu` VALUES (100, 102);
INSERT INTO `system_role_menu` VALUES (100, 103);
INSERT INTO `system_role_menu` VALUES (100, 104);
INSERT INTO `system_role_menu` VALUES (100, 105);
INSERT INTO `system_role_menu` VALUES (100, 106);
INSERT INTO `system_role_menu` VALUES (100, 107);
INSERT INTO `system_role_menu` VALUES (100, 108);
INSERT INTO `system_role_menu` VALUES (100, 500);
INSERT INTO `system_role_menu` VALUES (100, 501);
INSERT INTO `system_role_menu` VALUES (100, 1007);
INSERT INTO `system_role_menu` VALUES (100, 1012);
INSERT INTO `system_role_menu` VALUES (100, 1016);
INSERT INTO `system_role_menu` VALUES (100, 1020);
INSERT INTO `system_role_menu` VALUES (100, 1025);
INSERT INTO `system_role_menu` VALUES (100, 1030);
INSERT INTO `system_role_menu` VALUES (100, 1035);
INSERT INTO `system_role_menu` VALUES (100, 1039);
INSERT INTO `system_role_menu` VALUES (100, 1042);
INSERT INTO `system_role_menu` VALUES (100, 2000);
INSERT INTO `system_role_menu` VALUES (100, 2001);
INSERT INTO `system_role_menu` VALUES (100, 2002);
INSERT INTO `system_role_menu` VALUES (100, 2003);
INSERT INTO `system_role_menu` VALUES (100, 2004);
INSERT INTO `system_role_menu` VALUES (100, 2005);
INSERT INTO `system_role_menu` VALUES (100, 2006);
INSERT INTO `system_role_menu` VALUES (100, 2007);
INSERT INTO `system_role_menu` VALUES (100, 2008);
INSERT INTO `system_role_menu` VALUES (100, 2009);
INSERT INTO `system_role_menu` VALUES (100, 2010);
INSERT INTO `system_role_menu` VALUES (100, 2011);
INSERT INTO `system_role_menu` VALUES (100, 2012);
INSERT INTO `system_role_menu` VALUES (100, 2013);
INSERT INTO `system_role_menu` VALUES (100, 2014);
INSERT INTO `system_role_menu` VALUES (100, 2015);
INSERT INTO `system_role_menu` VALUES (100, 2016);
INSERT INTO `system_role_menu` VALUES (100, 2017);
INSERT INTO `system_role_menu` VALUES (100, 2018);
INSERT INTO `system_role_menu` VALUES (100, 2019);
INSERT INTO `system_role_menu` VALUES (100, 2020);
INSERT INTO `system_role_menu` VALUES (100, 2021);
INSERT INTO `system_role_menu` VALUES (100, 2022);
INSERT INTO `system_role_menu` VALUES (100, 2023);
INSERT INTO `system_role_menu` VALUES (100, 2024);
INSERT INTO `system_role_menu` VALUES (100, 2025);
INSERT INTO `system_role_menu` VALUES (100, 2026);
INSERT INTO `system_role_menu` VALUES (100, 2027);
INSERT INTO `system_role_menu` VALUES (100, 2028);
INSERT INTO `system_role_menu` VALUES (100, 2029);
INSERT INTO `system_role_menu` VALUES (100, 2030);
INSERT INTO `system_role_menu` VALUES (100, 2031);
INSERT INTO `system_role_menu` VALUES (100, 2032);
INSERT INTO `system_role_menu` VALUES (100, 2033);
INSERT INTO `system_role_menu` VALUES (100, 2034);
INSERT INTO `system_role_menu` VALUES (100, 2035);
INSERT INTO `system_role_menu` VALUES (100, 2036);
INSERT INTO `system_role_menu` VALUES (100, 2037);
INSERT INTO `system_role_menu` VALUES (100, 2038);
INSERT INTO `system_role_menu` VALUES (100, 2039);
INSERT INTO `system_role_menu` VALUES (100, 2040);
INSERT INTO `system_role_menu` VALUES (100, 2041);
INSERT INTO `system_role_menu` VALUES (100, 2042);
INSERT INTO `system_role_menu` VALUES (100, 2043);
INSERT INTO `system_role_menu` VALUES (100, 2044);
INSERT INTO `system_role_menu` VALUES (100, 2045);
INSERT INTO `system_role_menu` VALUES (100, 2046);
INSERT INTO `system_role_menu` VALUES (100, 2047);
INSERT INTO `system_role_menu` VALUES (100, 2048);
INSERT INTO `system_role_menu` VALUES (100, 2049);
INSERT INTO `system_role_menu` VALUES (100, 2050);
INSERT INTO `system_role_menu` VALUES (100, 2051);
INSERT INTO `system_role_menu` VALUES (100, 2052);
INSERT INTO `system_role_menu` VALUES (100, 2053);
INSERT INTO `system_role_menu` VALUES (100, 2054);
INSERT INTO `system_role_menu` VALUES (100, 2055);
INSERT INTO `system_role_menu` VALUES (100, 2057);
INSERT INTO `system_role_menu` VALUES (100, 2058);
INSERT INTO `system_role_menu` VALUES (100, 2061);
INSERT INTO `system_role_menu` VALUES (100, 2062);
INSERT INTO `system_role_menu` VALUES (100, 2063);
INSERT INTO `system_role_menu` VALUES (100, 2064);
INSERT INTO `system_role_menu` VALUES (100, 2079);
INSERT INTO `system_role_menu` VALUES (100, 2080);
INSERT INTO `system_role_menu` VALUES (100, 2123);
INSERT INTO `system_role_menu` VALUES (100, 2124);
INSERT INTO `system_role_menu` VALUES (100, 2125);
INSERT INTO `system_role_menu` VALUES (100, 2126);
INSERT INTO `system_role_menu` VALUES (100, 2127);
INSERT INTO `system_role_menu` VALUES (100, 2128);
INSERT INTO `system_role_menu` VALUES (100, 2129);
INSERT INTO `system_role_menu` VALUES (100, 2130);
INSERT INTO `system_role_menu` VALUES (100, 2131);
INSERT INTO `system_role_menu` VALUES (100, 2190);
INSERT INTO `system_role_menu` VALUES (100, 2191);
INSERT INTO `system_role_menu` VALUES (100, 2192);
INSERT INTO `system_role_menu` VALUES (100, 2193);
INSERT INTO `system_role_menu` VALUES (100, 2194);
INSERT INTO `system_role_menu` VALUES (100, 2195);
INSERT INTO `system_role_menu` VALUES (100, 2196);
INSERT INTO `system_role_menu` VALUES (100, 2223);
INSERT INTO `system_role_menu` VALUES (100, 2224);
INSERT INTO `system_role_menu` VALUES (100, 2225);
INSERT INTO `system_role_menu` VALUES (100, 2226);
INSERT INTO `system_role_menu` VALUES (100, 2227);
INSERT INTO `system_role_menu` VALUES (100, 2228);
INSERT INTO `system_role_menu` VALUES (100, 2229);
INSERT INTO `system_role_menu` VALUES (100, 2230);
INSERT INTO `system_role_menu` VALUES (100, 2231);
INSERT INTO `system_role_menu` VALUES (100, 2232);
INSERT INTO `system_role_menu` VALUES (100, 2233);
INSERT INTO `system_role_menu` VALUES (100, 2234);
INSERT INTO `system_role_menu` VALUES (100, 2235);
INSERT INTO `system_role_menu` VALUES (100, 2236);
INSERT INTO `system_role_menu` VALUES (100, 2237);
INSERT INTO `system_role_menu` VALUES (100, 2262);
INSERT INTO `system_role_menu` VALUES (100, 2315);
INSERT INTO `system_role_menu` VALUES (100, 2316);
INSERT INTO `system_role_menu` VALUES (100, 2317);
INSERT INTO `system_role_menu` VALUES (100, 2318);
INSERT INTO `system_role_menu` VALUES (100, 2322);
INSERT INTO `system_role_menu` VALUES (100, 2323);
INSERT INTO `system_role_menu` VALUES (100, 2324);
INSERT INTO `system_role_menu` VALUES (100, 2325);
INSERT INTO `system_role_menu` VALUES (100, 2335);
INSERT INTO `system_role_menu` VALUES (100, 2336);
INSERT INTO `system_role_menu` VALUES (100, 2337);
INSERT INTO `system_role_menu` VALUES (100, 2338);
INSERT INTO `system_role_menu` VALUES (100, 2339);
INSERT INTO `system_role_menu` VALUES (100, 2395);
INSERT INTO `system_role_menu` VALUES (100, 2396);
INSERT INTO `system_role_menu` VALUES (100, 2397);
INSERT INTO `system_role_menu` VALUES (100, 2398);
INSERT INTO `system_role_menu` VALUES (100, 2399);
INSERT INTO `system_role_menu` VALUES (100, 2400);
INSERT INTO `system_role_menu` VALUES (100, 2401);
INSERT INTO `system_role_menu` VALUES (100, 2402);
INSERT INTO `system_role_menu` VALUES (100, 2403);
INSERT INTO `system_role_menu` VALUES (100, 2404);
INSERT INTO `system_role_menu` VALUES (100, 2405);
INSERT INTO `system_role_menu` VALUES (100, 2407);
INSERT INTO `system_role_menu` VALUES (100, 2408);
INSERT INTO `system_role_menu` VALUES (100, 2410);
INSERT INTO `system_role_menu` VALUES (100, 2411);
INSERT INTO `system_role_menu` VALUES (100, 2412);
INSERT INTO `system_role_menu` VALUES (100, 2413);
INSERT INTO `system_role_menu` VALUES (100, 2414);
INSERT INTO `system_role_menu` VALUES (100, 2415);
INSERT INTO `system_role_menu` VALUES (100, 2416);
INSERT INTO `system_role_menu` VALUES (100, 2417);
INSERT INTO `system_role_menu` VALUES (100, 2418);
INSERT INTO `system_role_menu` VALUES (100, 2471);
INSERT INTO `system_role_menu` VALUES (100, 2472);

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES (1, 100, 'admin', '超级管理员', '00', 'support@qiantong.tech', '15888888888', '0', '', '$2a$10$QTX16TRIbQ00ssEAZ1RB7Ov7jhhogF3IkBDVQqLSarC0c5e5MU0Wm', '0', '0', '127.0.0.1', '2026-07-08 11:45:08', '吴同', '2026-04-20 09:32:53', '吴同', '2026-07-08 11:45:07', '管理员');
INSERT INTO `system_user` VALUES (2, 100, 'qKnow', '吴同', '00', '', '18888888888', '0', '', '$2a$10$TXvMO3ea1uL69EMAX2WszenDhXlsMkfBkOGyOBvPK8jd3IInCbdLu', '0', '0', '127.0.0.1', '2026-07-08 11:38:01', '吴同', '2026-06-03 14:48:53', '吴同', '2026-07-08 11:38:00', NULL);

-- ----------------------------
-- Table structure for system_user_post
-- ----------------------------
DROP TABLE IF EXISTS `system_user_post`;
CREATE TABLE `system_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_user_post
-- ----------------------------
INSERT INTO `system_user_post` VALUES (1, 1);

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_user_role
-- ----------------------------
INSERT INTO `system_user_role` VALUES (1, 1);
INSERT INTO `system_user_role` VALUES (2, 100);

SET FOREIGN_KEY_CHECKS = 1;
