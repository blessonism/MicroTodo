ALTER TABLE tb_task
ADD COLUMN completion_time datetime DEFAULT NULL COMMENT '任务完成时间',
ADD COLUMN previous_sequence int DEFAULT NULL COMMENT '任务完成前的序号'; 