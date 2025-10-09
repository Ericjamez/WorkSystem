-- Create database (run this manually in MySQL)
-- CREATE DATABASE IF NOT EXISTS student_homework_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE student_homework_db;

-- Students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    class_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_student_id ON students(student_id);
CREATE INDEX idx_class_name ON students(class_name);

-- Homework assignments table
CREATE TABLE homework (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    course_name VARCHAR(100) NOT NULL,
    deadline TIMESTAMP NOT NULL,
    total_score INT DEFAULT 100,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_course_name ON homework(course_name);
CREATE INDEX idx_deadline ON homework(deadline);

-- Homework submissions table
CREATE TABLE homework_submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    homework_id BIGINT NOT NULL,
    content TEXT,
    attachment_path VARCHAR(500),
    score INT,
    teacher_comment TEXT,
    submit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (homework_id) REFERENCES homework(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_homework (student_id, homework_id)
);

CREATE INDEX idx_student_id_sub ON homework_submissions(student_id);
CREATE INDEX idx_homework_id_sub ON homework_submissions(homework_id);
CREATE INDEX idx_submit_time ON homework_submissions(submit_time);

-- Insert sample data
INSERT INTO students (student_id, name, class_name, email, phone) VALUES
('20230001', '张三', '计算机科学与技术1班', 'zhangsan@example.com', '13800138001'),
('20230002', '李四', '计算机科学与技术1班', 'lisi@example.com', '13800138002'),
('20230003', '王五', '计算机科学与技术2班', 'wangwu@example.com', '13800138003');

INSERT INTO homework (title, description, course_name, deadline, total_score) VALUES
('Java基础编程作业', '完成Java基础语法练习，包括变量、循环、条件语句等', 'Java程序设计', '2024-12-31 23:59:59', 100),
('Spring Boot项目实践', '使用Spring Boot框架开发一个简单的Web应用', 'Web开发技术', '2024-12-25 23:59:59', 100),
('数据库设计作业', '设计一个学生信息管理系统的数据库', '数据库原理', '2024-12-20 23:59:59', 100);

INSERT INTO homework_submissions (student_id, homework_id, content, score, teacher_comment) VALUES
(1, 1, '已完成所有Java基础练习，代码已提交到GitHub', 95, '代码结构清晰，完成度很高'),
(2, 1, '完成了大部分练习，部分题目需要改进', 85, '需要加强异常处理'),
(1, 2, 'Spring Boot项目已部署，包含用户管理功能', 90, '功能完整，界面需要优化');
