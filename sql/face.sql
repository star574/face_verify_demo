/*
 Navicat Premium Data Transfer

 Source Server         : aly
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : codestar.mysql.rds.aliyuncs.com:11222
 Source Schema         : face

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 15/06/2022 00:15:49
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `uid`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uname`       varchar(255) NOT NULL COMMENT '用户名',
    `uface_id`    blob         NOT NULL COMMENT '人脸二进制数据',
    `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
SET
FOREIGN_KEY_CHECKS = 1;
