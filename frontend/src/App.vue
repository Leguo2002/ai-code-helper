<template>
  <div class="app">
    <header class="app-header">
      <h1>AI 编程小助手</h1>
    </header>
    <main class="chat-container">
      <div class="messages" ref="messagesRef">
        <div
          v-for="msg in messages"
          :key="msg.id"
          class="message"
          :class="msg.role === 'user' ? 'right' : 'left'"
        >
          <div class="bubble">
            <div class="name">{{ msg.role === 'user' ? '我' : 'AI' }}</div>
            <div class="content" v-html="formatMessage(msg.content)"></div>
          </div>
        </div>
      </div>
      <form class="input-bar" @submit.prevent="handleSend">
        <input
          v-model="input"
          class="text-input"
          type="text"
          placeholder="请输入问题，例如：如何学习Java？"
          :disabled="isStreaming"
        />
        <button class="send-btn" type="submit" :disabled="!input.trim() || isStreaming">发送</button>
      </form>
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import { createAxios } from './services/http';
import { openSSE } from './services/sse';

type ChatMessage = {
  id: string;
  role: 'user' | 'assistant';
  content: string;
};

const axios = createAxios();
const input = ref('');
const isStreaming = ref(false);
const messages = reactive<ChatMessage[]>([]);
const messagesRef = ref<HTMLDivElement | null>(null);
const memoryId = ref<number>(0);
let currentEventSource: EventSource | null = null;

function generateMemoryId(): number {
  // Simple random session id; backend expects int
  return Math.floor(Math.random() * 1_000_000_000);
}

function scrollToBottom() {
  requestAnimationFrame(() => {
    messagesRef.value?.scrollTo({ top: messagesRef.value.scrollHeight, behavior: 'smooth' });
  });
}

function formatMessage(text: string): string {
  // Basic newline to <br>
  return text.replace(/\n/g, '<br />');
}

async function handleSend() {
  const content = input.value.trim();
  if (!content || isStreaming.value) return;
  const userMsg: ChatMessage = {
    id: `${Date.now()}-u`,
    role: 'user',
    content
  };
  messages.push(userMsg);
  input.value = '';
  // Append a placeholder for assistant streaming
  const aiMsg: ChatMessage = {
    id: `${Date.now()}-a`,
    role: 'assistant',
    content: ''
  };
  messages.push(aiMsg);
  startStream(aiMsg.id, content);
}

function startStream(targetMessageId: string, userContent: string) {
  isStreaming.value = true;
  // Close previous stream if any
  if (currentEventSource) {
    currentEventSource.close();
    currentEventSource = null;
  }

  const url = `/api/ai/chat?memoryId=${encodeURIComponent(memoryId.value)}&message=${encodeURIComponent(
    userContent
  )}`;

  currentEventSource = openSSE(url, {
    onMessage: (chunk) => {
      const msg = messages.find((m) => m.id === targetMessageId);
      if (msg) {
        msg.content += chunk;
      }
    },
    onError: () => {
      isStreaming.value = false;
      currentEventSource?.close();
      currentEventSource = null;
    },
    onComplete: () => {
      isStreaming.value = false;
      currentEventSource?.close();
      currentEventSource = null;
    }
  });
}

onMounted(() => {
  memoryId.value = generateMemoryId();
});

watch(
  () => messages.map((m) => m.content).join('\u0001'),
  () => scrollToBottom()
);
</script>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  height: 100vh;
}
.app-header {
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
}
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #f7f7f8;
}
.message {
  display: flex;
  margin: 10px 0;
}
.message.left {
  justify-content: flex-start;
}
.message.right {
  justify-content: flex-end;
}
.bubble {
  max-width: 80%;
  padding: 10px 12px;
  border-radius: 10px;
  background: white;
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
}
.message.right .bubble {
  background: #dff4ff;
}
.name {
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
}
.content {
  white-space: pre-wrap;
  line-height: 1.6;
}
.input-bar {
  display: flex;
  gap: 8px;
  padding: 12px;
  border-top: 1px solid #eee;
}
.text-input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  outline: none;
}
.text-input:disabled {
  background: #f3f3f3;
}
.send-btn {
  padding: 10px 16px;
  border: none;
  border-radius: 8px;
  background: #409eff;
  color: white;
  cursor: pointer;
}
.send-btn:disabled {
  background: #9ecbff;
  cursor: not-allowed;
}
</style>


