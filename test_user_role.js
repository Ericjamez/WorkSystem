const axios = require('axios');

async function testLogin() {
  try {
    // 测试学生登录
    console.log('测试学生登录...');
    const studentResponse = await axios.post('http://localhost:8080/api/auth/login', {
      studentId: '20230001',
      password: 'password123'
    });
    
    console.log('学生登录响应:', JSON.stringify(studentResponse.data, null, 2));
    
    // 测试教师登录
    console.log('\n测试教师登录...');
    const teacherResponse = await axios.post('http://localhost:8080/api/auth/login', {
      teacherId: 'T001',
      password: 'password123'
    });
    
    console.log('教师登录响应:', JSON.stringify(teacherResponse.data, null, 2));
    
  } catch (error) {
    console.error('测试失败:', error.response?.data || error.message);
  }
}

testLogin();
