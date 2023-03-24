
https://api.openai.com/v1/chat/completions

请求数据：
```json
{
  "model": "gpt-3.5-turbo",
  "messages": [
      {"role": "user", "content": "我说A,请重复"},
      {"role": "system", "content": "A"},
      {"role": "user", "content": "我刚才让你重复什么？"}

```
响应数据
{
"id": "chatcmpl-6xV9UO1OYynBEBBxEAViX6ZcKrhcu",
"object": "chat.completion",
"created": 1679639120,
"model": "gpt-3.5-turbo-0301",
"usage": {
"prompt_tokens": 40,
"completion_tokens": 14,
"total_tokens": 54
},
"choices": [
{
"message": {
"role": "assistant",
"content": "您让我重复\"A\"这个单词。"
},
"finish_reason": "stop",
"index": 0
}
]
}