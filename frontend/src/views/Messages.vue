<template>
  <div class="messages-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>私信管理</span>
          <el-button type="primary" @click="showNewMessageDialog = true">
            发送新消息
          </el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <!-- 消息列表 -->
        <el-col :span="8">
          <el-card class="message-list">
            <template #header>
              <span>消息列表</span>
            </template>
            <div class="message-items">
              <div
                v-for="message in messages"
                :key="message.id"
                class="message-item"
                :class="{ active: selectedConversation && 
                  ((selectedConversation.userId1 === message.senderId && selectedConversation.userId2 === message.receiverId) ||
                   (selectedConversation.userId1 === message.receiverId && selectedConversation.userId2 === message.senderId)) }"
                @click="selectConversation(message)"
              >
                <div class="message-header">
                  <span class="sender-name">
                    {{ message.senderId === user.id ? message.receiverName : message.senderName }}
                  </span>
                  <el-tag
                    :type="message.senderRole === 'TEACHER' ? 'warning' : 'success'"
                    size="small"
                  >
                    {{ message.senderRole === 'TEACHER' ? '教师' : '学生' }}
                  </el-tag>
                </div>
                <div class="message-preview">
                  {{ message.messageContent }}
                </div>
                <div class="message-time">
                  {{ formatTime(message.createTime) }}
                </div>
                <el-badge
                  v-if="!message.isRead && message.receiverId === user.id"
                  value="未读"
                  class="unread-badge"
                />
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 对话内容 -->
        <el-col :span="16">
          <el-card class="conversation" v-if="selectedConversation">
            <template #header>
              <div class="conversation-header">
                <span>与 {{ selectedConversation.otherUserName }} 的对话</span>
                <el-tag :type="selectedConversation.otherUserRole === 'TEACHER' ? 'warning' : 'success'">
                  {{ selectedConversation.otherUserRole === 'TEACHER' ? '教师' : '学生' }}
                </el-tag>
              </div>
            </template>

            <div class="conversation-messages">
              <div
                v-for="msg in conversationMessages"
                :key="msg.id"
                class="message-bubble"
                :class="{ 'own-message': msg.senderId === user.id }"
              >
                <div class="message-content">
                  {{ msg.messageContent }}
                </div>
                <div class="message-meta">
                  <span class="sender">{{ msg.senderName }}</span>
                  <span class="time">{{ formatTime(msg.createTime) }}</span>
                </div>
              </div>
            </div>

            <div class="message-input">
              <el-input
                v-model="newMessage"
                type="textarea"
                :rows="3"
                placeholder="输入消息内容..."
                @keydown.enter.prevent="sendMessage"
              />
              <div class="input-actions">
                <el-button type="primary" @click="sendMessage" :loading="sending">
                  发送
                </el-button>
              </div>
            </div>
          </el-card>

          <el-empty v-else description="请选择对话或发送新消息" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 发送新消息对话框 -->
    <el-dialog
      title="发送新消息"
      v-model="showNewMessageDialog"
      width="500px"
    >
      <el-form :model="newMessageForm" label-width="80px">
        <el-form-item label="收件人">
          <el-select
            v-model="newMessageForm.receiverId"
            placeholder="选择收件人"
            style="width: 100%"
          >
            <el-option-group
              v-for="group in userGroups"
              :key="group.label"
              :label="group.label"
            >
              <el-option
                v-for="user in group.users"
                :key="user.id"
                :label="`${user.name} (${user.role === 'TEACHER' ? '教师' : '学生'})`"
                :value="user.id"
              >
                <span>{{ user.name }}</span>
                <el-tag
                  :type="user.role === 'TEACHER' ? 'warning' : 'success'"
                  size="small"
                  style="margin-left: 10px"
                >
                  {{ user.role === 'TEACHER' ? '教师' : '学生' }}
                </el-tag>
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="消息内容">
          <el-input
            v-model="newMessageForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入消息内容..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showNewMessageDialog = false">取消</el-button>
        <el-button type="primary" @click="sendNewMessage" :loading="sending">
          发送
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { messageApi, userApi } from '../services/api.js'

export default {
  name: 'Messages',
  data() {
    return {
      user: {},
      messages: [],
      conversationMessages: [],
      selectedConversation: null,
      newMessage: '',
      sending: false,
      showNewMessageDialog: false,
      newMessageForm: {
        receiverId: '',
        content: ''
      },
      userGroups: []
    }
  },
  async mounted() {
    this.loadUserInfo()
    await this.loadMessages()
    await this.loadUserGroups()
  },
  methods: {
    loadUserInfo() {
      const userData = localStorage.getItem('user')
      if (userData) {
        this.user = JSON.parse(userData)
      }
    },

    async loadMessages() {
      try {
        const response = await messageApi.getUserMessages(this.user.id)
        if (response.success) {
          this.messages = response.messages
        }
      } catch (error) {
        console.error('获取消息失败:', error)
        this.$message.error('获取消息失败')
      }
    },

    async loadUserGroups() {
      try {
        const [studentsRes, teachersRes] = await Promise.all([
          userApi.getAllStudents(),
          userApi.getAllTeachers()
        ])
        
        this.userGroups = [
          {
            label: '学生',
            users: studentsRes.success ? studentsRes.data : []
          },
          {
            label: '教师',
            users: teachersRes.success ? teachersRes.data : []
          }
        ]
      } catch (error) {
        console.error('获取用户列表失败:', error)
      }
    },

    async selectConversation(message) {
      const otherUserId = message.senderId === this.user.id ? message.receiverId : message.senderId
      const otherUserName = message.senderId === this.user.id ? message.receiverName : message.senderName
      const otherUserRole = message.senderId === this.user.id ? message.receiverRole : message.senderRole
      
      this.selectedConversation = {
        userId1: this.user.id,
        userId2: otherUserId,
        otherUserName,
        otherUserRole
      }

      await this.loadConversation()
    },

    async loadConversation() {
      if (!this.selectedConversation) return

      try {
        const response = await messageApi.getConversation(
          this.selectedConversation.userId1,
          this.selectedConversation.userId2
        )
        if (response.success) {
          this.conversationMessages = response.messages
          
          // 标记消息为已读
          for (const message of this.conversationMessages) {
            if (!message.isRead && message.receiverId === this.user.id) {
              await messageApi.markAsRead(message.id)
            }
          }
        }
      } catch (error) {
        console.error('获取对话失败:', error)
        this.$message.error('获取对话失败')
      }
    },

    async sendMessage() {
      if (!this.newMessage.trim() || !this.selectedConversation) return

      this.sending = true
      try {
        const message = {
          senderId: this.user.id,
          receiverId: this.selectedConversation.userId2,
          messageContent: this.newMessage.trim()
        }

        const response = await messageApi.sendMessage(message)
        if (response.success) {
          this.$message.success('消息发送成功')
          this.newMessage = ''
          await this.loadConversation()
          await this.loadMessages()
        } else {
          this.$message.error('消息发送失败')
        }
      } catch (error) {
        console.error('发送消息失败:', error)
        this.$message.error('消息发送失败')
      } finally {
        this.sending = false
      }
    },

    async sendNewMessage() {
      if (!this.newMessageForm.receiverId || !this.newMessageForm.content.trim()) {
        this.$message.warning('请填写完整信息')
        return
      }

      this.sending = true
      try {
        const message = {
          senderId: this.user.id,
          receiverId: this.newMessageForm.receiverId,
          messageContent: this.newMessageForm.content.trim()
        }

        const response = await messageApi.sendMessage(message)
        if (response.success) {
          this.$message.success('消息发送成功')
          this.showNewMessageDialog = false
          this.newMessageForm = {
            receiverId: '',
            content: ''
          }
          await this.loadMessages()
        } else {
          this.$message.error('消息发送失败')
        }
      } catch (error) {
        console.error('发送消息失败:', error)
        this.$message.error('消息发送失败')
      } finally {
        this.sending = false
      }
    },

    formatTime(timeString) {
      if (!timeString) return ''
      const date = new Date(timeString)
      return date.toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.messages-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-list {
  height: 600px;
}

.message-items {
  max-height: 540px;
  overflow-y: auto;
}

.message-item {
  padding: 12px;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  position: relative;
  transition: background-color 0.3s;
}

.message-item:hover {
  background-color: #f5f7fa;
}

.message-item.active {
  background-color: #ecf5ff;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.sender-name {
  font-weight: bold;
}

.message-preview {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.message-time {
  color: #999;
  font-size: 12px;
}

.unread-badge {
  position: absolute;
  top: 8px;
  right: 8px;
}

.conversation {
  height: 600px;
}

.conversation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.conversation-messages {
  height: 400px;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.message-bubble {
  margin-bottom: 16px;
  max-width: 70%;
}

.message-bubble.own-message {
  margin-left: auto;
}

.message-content {
  padding: 12px;
  background-color: #f0f2f5;
  border-radius: 8px;
  word-wrap: break-word;
}

.own-message .message-content {
  background-color: #409EFF;
  color: white;
}

.message-meta {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  display: flex;
  justify-content: space-between;
}

.message-input {
  margin-top: 20px;
}

.input-actions {
  margin-top: 10px;
  text-align: right;
}
</style>
