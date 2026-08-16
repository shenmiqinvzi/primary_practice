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


DROP TABLE IF EXISTS category;
CREATE TABLE category (
                          id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT
  '主键',
                          type        INT         DEFAULT NULL COMMENT '类型：1菜品分类
  2套餐分类',
                          name        VARCHAR(32) NOT NULL COMMENT '分类名称',
                          sort        INT         NOT NULL DEFAULT 0 COMMENT
  '排序，越小越靠前',
                          status      INT         DEFAULT 1 COMMENT '状态：1启用 0禁用',
                          create_time DATETIME    DEFAULT NULL COMMENT '创建时间',
                          update_time DATETIME    DEFAULT NULL COMMENT '更新时间',
                          create_user BIGINT      DEFAULT NULL COMMENT '创建人',
                          update_user BIGINT      DEFAULT NULL COMMENT '更新人',
                          PRIMARY KEY (id),
                          UNIQUE KEY idx_category_name (name)
) ENGINE=InnoDB COMMENT='菜品及套餐分类';



INSERT INTO category VALUES (1, 1, '热菜', 1, 1, NOW(), NOW(), 1,
                             1);
INSERT INTO category VALUES (2, 1, '凉菜', 2, 1, NOW(), NOW(), 1,
                             1);
INSERT INTO category VALUES (3, 1, '主食', 3, 1, NOW(), NOW(), 1,
                             1);
INSERT INTO category VALUES (4, 2, '人气套餐', 1, 1, NOW(), NOW(),
                             1, 1);
INSERT INTO category VALUES (5, 2, '商务套餐', 2, 1, NOW(), NOW(),
                             1, 1);

DROP TABLE IF EXISTS dish;
CREATE TABLE dish (
                      id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT
  '主键',
                      name        VARCHAR(32) NOT NULL COMMENT '菜品名称',
                      category_id BIGINT      NOT NULL COMMENT '分类id',
                      price       DECIMAL(10,2) NOT NULL COMMENT '价格',
                      image       VARCHAR(255) DEFAULT NULL COMMENT '图片',
                      description VARCHAR(255) DEFAULT NULL COMMENT '描述',
                      status      INT         DEFAULT 1 COMMENT '状态：1起售 0停售',
                      create_time DATETIME    DEFAULT NULL,
                      update_time DATETIME    DEFAULT NULL,
                      create_user BIGINT      DEFAULT NULL,
                      update_user BIGINT      DEFAULT NULL,
                      PRIMARY KEY (id)
) ENGINE=InnoDB COMMENT='菜品表';



DROP TABLE IF EXISTS dish_flavor;
CREATE TABLE dish_flavor (
                             id       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
                             dish_id  BIGINT       NOT NULL COMMENT '菜品id',
                             name     VARCHAR(32)  DEFAULT NULL COMMENT '口味名称',
                             value    VARCHAR(255) DEFAULT NULL COMMENT '口味值',
                             PRIMARY KEY (id)
) ENGINE=InnoDB COMMENT='菜品口味表';

CREATE TABLE setmeal (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(32) NOT NULL COMMENT '套餐名称',
    price DECIMAL(10,2) NOT NULL COMMENT '套餐价格',
    image VARCHAR(255) DEFAULT NULL COMMENT '图片路径',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述',
    status INT DEFAULT 1 COMMENT '状态：1起售 0停售',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人',
    update_user BIGINT DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (id),
    UNIQUE KEY idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐表';


CREATE TABLE setmeal_dish (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    setmeal_id BIGINT NOT NULL COMMENT '套餐ID',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    name VARCHAR(32) NOT NULL COMMENT '菜品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '菜品单价',
    copies INT NOT NULL DEFAULT 1 COMMENT '份数',
    PRIMARY KEY (id),
    UNIQUE KEY idx_setmeal_dish (setmeal_id, dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐菜品关联表';


DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    openid       VARCHAR(255) NOT NULL UNIQUE COMMENT '微信用户唯一标识（登录凭证）',
    name         VARCHAR(64)  DEFAULT NULL COMMENT '用户姓名',
    phone        VARCHAR(11)  DEFAULT NULL COMMENT '手机号',
    sex          CHAR(1)      DEFAULT NULL COMMENT '性别：0-女 1-男',
    id_number    VARCHAR(18)  DEFAULT NULL COMMENT '身份证号',
    avatar       VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
    status       INT          DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
    create_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_time  DATETIME     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

DROP TABLE IF EXISTS shopping_cart;
CREATE TABLE shopping_cart (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(32) NOT NULL COMMENT '商品名称',
    image VARCHAR(255) DEFAULT NULL COMMENT '图片路径',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    dish_id BIGINT DEFAULT NULL COMMENT '菜品ID',
    setmeal_id BIGINT DEFAULT NULL COMMENT '套餐ID',
    dish_flavor VARCHAR(32) DEFAULT NULL COMMENT '菜品口味',
    number INT NOT NULL DEFAULT 1 COMMENT '数量',
    amount DECIMAL(10,2) NOT NULL COMMENT '单价',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';