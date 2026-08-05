-- =====================================================
-- 苍穹外卖 数据库初始化脚本
-- 使用方式：在 MySQL 中执行本文件
-- =====================================================

CREATE DATABASE IF NOT EXISTS sky_take_out DEFAULT CHARACTER SET utf8mb4;
USE sky_take_out;

-- 员工表
DROP TABLE IF EXISTS employee;
CREATE TABLE employee (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32) NOT NULL COMMENT '姓名',
    username    VARCHAR(32) NOT NULL COMMENT '用户名',
    password    VARCHAR(64) NOT NULL COMMENT '密码(MD5加密)',
    phone       VARCHAR(11) NOT NULL COMMENT '手机号',
    sex         VARCHAR(2)  NOT NULL COMMENT '性别：1男 0女',
    id_number   VARCHAR(18) NOT NULL COMMENT '身份证号',
    status      INT         NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    create_time DATETIME    NOT NULL COMMENT '创建时间',
    update_time DATETIME    NOT NULL COMMENT '更新时间',
    create_user BIGINT      NOT NULL COMMENT '创建人id',
    update_user BIGINT      NOT NULL COMMENT '修改人id',
    PRIMARY KEY (id)
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '员工信息表';

-- 初始员工：admin / 123456（123456 的 MD5）
INSERT INTO employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)
VALUES ('管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13812345678', '1', '110101199001010047', 1, NOW(), NOW(), 1, 1);
