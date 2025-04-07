ALTER TABLE `user`
ADD COLUMN `github_id` varchar(255) DEFAULT NULL COMMENT 'GitHub用户ID',
ADD COLUMN `email` varchar(255) DEFAULT NULL COMMENT 'GitHub邮箱',
ADD COLUMN `access_token` varchar(255) DEFAULT NULL COMMENT 'GitHub访问令牌',
ADD UNIQUE INDEX `idx_github_id`(`github_id`);

-- 将avatar字段的注释更新为包含GitHub头像URL的说明
ALTER TABLE `user` MODIFY COLUMN `avatar` varchar(1024) COMMENT '头像URL，可以是本地上传或GitHub头像URL'; 