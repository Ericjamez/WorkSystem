-- Create database (run this manually in MySQL)
-- CREATE DATABASE IF NOT EXISTS student_homework_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE student_homework_db;

-- Users table (for teachers, students and administrators)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('TEACHER', 'STUDENT', 'ADMIN') NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_role ON users(role);

-- Students table (extends users)
CREATE TABLE students (
    id BIGINT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    class_name VARCHAR(50),
    major VARCHAR(100),
    grade VARCHAR(10),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_student_id ON students(student_id);
CREATE INDEX idx_class_name ON students(class_name);

-- Teachers table (extends users)
CREATE TABLE teachers (
    id BIGINT PRIMARY KEY,
    teacher_id VARCHAR(20) NOT NULL UNIQUE,
    department VARCHAR(100),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_teacher_id ON teachers(teacher_id);

-- College management tables
-- Colleges table
CREATE TABLE colleges (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Majors table
CREATE TABLE majors (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        college_id BIGINT NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        description TEXT,
                        create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (college_id) REFERENCES colleges(id) ON DELETE CASCADE,
                        UNIQUE KEY unique_college_major (college_id, name)
);

-- Classes table
CREATE TABLE classes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         major_id BIGINT NOT NULL,
                         name VARCHAR(50) NOT NULL,
                         description TEXT,
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE,
                         UNIQUE KEY unique_major_class (major_id, name)
);

-- Update students table to reference classes
ALTER TABLE students
    ADD COLUMN class_id BIGINT,
    ADD FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE SET NULL;


-- Homework assignments table
CREATE TABLE homework (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    course_name VARCHAR(100) NOT NULL,
    class_id BIGINT,
    attachment_path VARCHAR(500),
    deadline TIMESTAMP NOT NULL,
    total_score INT DEFAULT 100,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE SET NULL
);

CREATE INDEX idx_course_name ON homework(course_name);
CREATE INDEX idx_deadline ON homework(deadline);
CREATE INDEX idx_teacher_id ON homework(teacher_id);

-- Homework submissions table
CREATE TABLE homework_submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    homework_id BIGINT NOT NULL,
    content TEXT,
    attachment_path VARCHAR(500),
    score INT,
    teacher_comment TEXT,
    status ENUM('SUBMITTED', 'GRADED', 'RETURNED', 'RESUBMITTED') DEFAULT 'SUBMITTED',
    return_reason TEXT,
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

-- Private messages table
CREATE TABLE private_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    message_content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_sender_id ON private_messages(sender_id);
CREATE INDEX idx_receiver_id ON private_messages(receiver_id);
CREATE INDEX idx_create_time ON private_messages(create_time);


-- Insert sample admin user
INSERT INTO users (username, password, role, name, email, phone) VALUES
('admin', 'admin123', 'ADMIN', '系统管理员', 'admin@example.com', '13800138005');

-- Insert sample college data
INSERT INTO colleges (name, description) VALUES
('计算机学院', '计算机科学与技术相关专业'),
('软件学院', '软件工程相关专业'),
('电子信息学院', '电子信息工程相关专业');

-- Insert sample major data
INSERT INTO majors (college_id, name, description) VALUES
(1, '计算机科学与技术', '计算机科学与技术专业'),
(1, '人工智能', '人工智能专业'),
(2, '软件工程', '软件工程专业'),
(3, '电子信息工程', '电子信息工程专业');

-- Insert sample class data
INSERT INTO classes (major_id, name, description) VALUES
(1, '计算机科学与技术1班', '计算机科学与技术专业1班'),
(1, '计算机科学与技术2班', '计算机科学与技术专业2班'),
(2, '人工智能1班', '人工智能专业1班'),
(3, '软件工程1班', '软件工程专业1班'),
(4, '电子信息工程1班', '电子信息工程专业1班');

-- Insert sample data
-- Insert users (both teachers and students)
INSERT INTO users (username, password, role, name, email, phone) VALUES
-- Teachers
('teacher1', 'password123', 'TEACHER', '张老师', 'teacher1@example.com', '13800138000'),
('teacher2', 'password123', 'TEACHER', '李老师', 'teacher2@example.com', '13800138001'),
-- Students
('student1', 'password123', 'STUDENT', '张三', 'zhangsan@example.com', '13800138002'),
('student2', 'password123', 'STUDENT', '李四', 'lisi@example.com', '13800138003'),
('student3', 'password123', 'STUDENT', '王五', 'wangwu@example.com', '13800138004');

-- Insert teachers
INSERT INTO teachers (id, teacher_id, department) VALUES
(2, 'T001', '计算机科学与技术系'),
(3, 'T002', '软件工程系');

-- Insert students
INSERT INTO students (id, student_id, class_name, major) VALUES
(4, '20230001', '计算机科学与技术1班', '计算机科学与技术'),
(5, '20230002', '计算机科学与技术1班', '计算机科学与技术'),
(6, '20230003', '计算机科学与技术2班', '计算机科学与技术');

-- Update existing students with class_id
UPDATE students SET class_id = 1 WHERE id = 4;
UPDATE students SET class_id = 1 WHERE id = 5;
UPDATE students SET class_id = 2 WHERE id = 6;

-- Insert homework
INSERT INTO homework (teacher_id, title, description, course_name, deadline, total_score) VALUES
(2, 'Java基础编程作业', '完成Java基础语法练习，包括变量、循环、条件语句等', 'Java程序设计', '2024-12-31 23:59:59', 100),
(3, 'Spring Boot项目实践', '使用Spring Boot框架开发一个简单的Web应用', 'Web开发技术', '2024-12-25 23:59:59', 100),
(2, '数据库设计作业', '设计一个学生信息管理系统的数据库', '数据库原理', '2024-12-20 23:59:59', 100);

-- Insert homework submissions
INSERT INTO homework_submissions (student_id, homework_id, content, score, teacher_comment) VALUES
(4, 1, '已完成所有Java基础练习，代码已提交到GitHub', 95, '代码结构清晰，完成度很高'),
(5, 1, '完成了大部分练习，部分题目需要改进', 85, '需要加强异常处理'),
(4, 2, 'Spring Boot项目已部署，包含用户管理功能', 90, '功能完整，界面需要优化');

-- Insert sample private messages
INSERT INTO private_messages (sender_id, receiver_id, message_content) VALUES
(4, 2, '张老师，关于Java作业的问题想请教您'),
(2, 4, '好的，有什么问题尽管问'),
(5, 3, '李老师，Spring Boot项目的部署有问题');
