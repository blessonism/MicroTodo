-- 修改start_time字段类型为DATETIME
ALTER TABLE tb_task MODIFY COLUMN start_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

-- 更新现有记录的start_time
UPDATE tb_task SET start_time = CURRENT_TIMESTAMP WHERE start_time IS NULL; 