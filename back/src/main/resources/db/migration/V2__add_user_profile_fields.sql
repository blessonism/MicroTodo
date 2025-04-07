-- 添加用户显示名称和头像字段
ALTER TABLE user 
ADD COLUMN display_name VARCHAR(50) DEFAULT NULL COMMENT '显示名称',
ADD COLUMN avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL';

-- 更新现有用户的显示名称为其用户名
UPDATE user SET display_name = uname WHERE display_name IS NULL;

-- 设置默认头像URL（你可以替换为你想要的默认头像URL）
UPDATE user SET avatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png' WHERE avatar IS NULL;