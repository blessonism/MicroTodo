-- 添加提醒时间字段
ALTER TABLE tb_task
ADD COLUMN reminder_time DATETIME DEFAULT NULL COMMENT '任务提醒时间'; 