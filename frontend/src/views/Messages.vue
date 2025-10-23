<template>
  <div class="messages-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>私信管理</span>
        </div>
      </template>

      <el-row :gutter="20">
        <!-- 联系人列表 -->
        <el-col :span="6">
          <el-card class="contact-list">
            <template #header>
              <span>联系人</span>
            </template>
            <div class="contact-items">
              <div
                v-for="contact in contacts"
                :key="contact.id"
                class="contact-item"
                :class="{ active: selectedContact && selectedContact.id === contact.id }"
                @click="selectContact(contact)"
              >
                <div class="contact-header">
                  <span class="contact-name">{{ contact.name }}</span>
                  <el-tag
                    :type="contact.role === 'TEACHER' ? 'warning' : 'success'"
                    size="small"
                  >
                    {{ contact.role === 'TEACHER' ? '教师' : '学生' }}
                  </el-tag>
                </div>
                <div class="last-message">
                  {{ contact.lastMessage || '暂无消息' }}
                </div>
                <div class="contact-time">
                  {{ contact.lastTime ? formatTime(contact.lastTime) : '' }}
                </div>
                <el-badge
                  v-if="contact.unreadCount > 0"
                  :value="contact.unreadCount"
                  class="unread-badge"
                />
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 对话内容 -->
        <el-col :span="18">
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

          <el-card class="welcome-card" v-else>
            <el-empty description="请选择联系人开始对话" />
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { messageApi } from '../services/api.js'

export default {
  name: 'Messages',
  data() {
    return {
      user: {},
      messages: [],
      contacts: [],
      conversationMessages: [],
      selectedConversation: null,
      selectedContact: null,
      newMessage: '',
      sending: false
    }
  },
  async mounted() {
    this.loadUserInfo()
    await this.loadMessages()
    await this.loadContacts()
  },
  computed: {
    filteredMessages() {
      if (!this.selectedContact) {
        return this.messages
      }
      return this.messages.filter(message => 
        message.senderId === this.selectedContact.id || 
        message.receiverId === this.selectedContact.id
      )
    }
  },
  methods: {
    loadUserInfo() {
      const userData = localStorage.getItem('user')
      if (userData) {
        this.user = JSON.parse(userData)
      }
    },

    async loadContacts() {
      try {
        // 从消息中提取联系人
        const contactMap = new Map()
        
        this.messages.forEach(message => {
          const otherUserId = message.senderId === this.user.id ? message.receiverId : message.senderId
          const otherUserName = message.senderId === this.user.id ? message.receiverName : message.senderName
          const otherUserRole = message.senderId === this.user.id ? message.receiverRole : message.senderRole
          
          if (!contactMap.has(otherUserId)) {
            contactMap.set(otherUserId, {
              id: otherUserId,
              name: otherUserName,
              role: otherUserRole,
              lastMessage: message.messageContent,
              lastTime: message.createTime,
              unreadCount: 0
            })
          } else {
            const contact = contactMap.get(otherUserId)
            if (new Date(message.createTime) > new Date(contact.lastTime)) {
              contact.lastMessage = message.messageContent
              contact.lastTime = message.createTime
            }
          }
          
          // 计算未读消息数
          if (!message.isRead && message.receiverId === this.user.id) {
            const contact = contactMap.get(otherUserId)
            contact.unreadCount++
          }
        })
        
        // 转换为数组并按最后时间排序
        this.contacts = Array.from(contactMap.values())
          .sort((a, b) => new Date(b.lastTime) - new Date(a.lastTime))
      } catch (error) {
        console.error('加载联系人失败:', error)
      }
    },

    selectContact(contact) {
      this.selectedContact = contact
      this.selectedConversation = {
        userId1: this.user.id,
        userId2: contact.id,
        otherUserName: contact.name,
        otherUserRole: contact.role
      }
      this.loadConversation()
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

.contact-list {
  height: 600px;
}

.contact-items {
  max-height: 540px;
  overflow-y: auto;
}

.contact-item {
  padding: 12px;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  position: relative;
  transition: background-color 0.3s;
}

.contact-item:hover {
  background-color: #f5f7fa;
}

.contact-item.active {
  background-color: #ecf5ff;
}

.contact-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.contact-name {
  font-weight: bold;
}

.last-message {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.contact-time {
  color: #999;
  font-size: 12px;
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
