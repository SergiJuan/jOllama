<div align="center">
  <img src="https://raw.githubusercontent.com/SergiJuan/jOllama/main/jollama-logo.png" width="200" alt="jOllama logo">

# jOllama — Ollama Java Client

A simple Java client for interacting with the Ollama API and local LLMs.

![GitHub stars](https://img.shields.io/github/stars/SergiJuan/jOllama)
![GitHub forks](https://img.shields.io/github/forks/SergiJuan/jOllama)
![Contributors](https://img.shields.io/github/contributors/SergiJuan/jOllama?style=social)

![Java](https://img.shields.io/badge/Java-11+-orange)
![GitHub last commit](https://img.shields.io/github/last-commit/SergiJuan/jOllama?color=green)
![JitPack](https://jitpack.io/v/SergiJuan/jOllama.svg)
![GitHub License](https://img.shields.io/github/license/SergiJuan/jOllama)

</div>

---

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Examples](#examples)
- [Client Configuration](#client-configuration)
- [API Overview](#api-overview)
- [Project Structure](#project-structure)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- Multi-turn chat with conversation history
- Text generation via `/api/generate`
- Streaming responses for both generate and chat
- Embeddings with built-in `cosineSimilarity` helper
- Full model management: list, pull, create, copy, push, delete, show, ps
- `Options` support for fine-grained generation control (temperature, seed, topP, and more)
- System prompt support in both generate and chat
- Thinking support via `thinking` field in `Message`
- Builder pattern for clean client and manager configuration
- Custom HTTP client support
- Custom chat roles (`user`, `assistant`, `system`)
- Exception handling for Ollama API errors
- Clean, minimal model classes with Jackson support

---

## Requirements

- Java 11+
- Ollama Server >= 0.11.10
- Maven or Gradle

---

## Installation

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.SergiJuan</groupId>
    <artifactId>jOllama</artifactId>
    <version>0.0.4</version>
</dependency>
```

### Gradle

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.SergiJuan:jOllama:0.0.4'
}
```

---

## Quick Start

```java
import jua.sergi.OllamaClient;
import jua.sergi.model.response.GenerateResponse;

public class Main {

    public static void main(String[] args) {

        OllamaClient client = OllamaClient.builder().build();

        GenerateResponse response =
                client.generate("llama3", "Explain what Java is in one sentence.");

        System.out.println(response.getResponse());
    }
}
```

> By default, the client connects to `http://localhost:11434`.

---

## Examples

### Generate

```java
OllamaClient client = OllamaClient.builder().build();

GenerateResponse response = client.generate("llama3", "What is the capital of France?");

System.out.println(response.getResponse());
```

### Generate with options

```java
Options opts = Options.builder()
        .temperature(0.7)
        .seed(42)
        .numPredict(100)
        .build();

GenerateResponse response = client.generate("llama3", "Tell me a joke.", opts);

System.out.println(response.getResponse());
```

### Generate with system prompt

```java
GenerateResponse response = client.generate(
        "llama3",
        "Who are you?",
        "You are a pirate. Always respond in pirate speak.",
        null
);

System.out.println(response.getResponse());
```

### Streaming generate

```java
client.generateStreaming("llama3", "Tell me a story", chunk -> {
    System.out.print(chunk.getResponse());
});
```

### Streaming generate with options

```java
Options opts = Options.builder()
        .temperature(0.5)
        .numPredict(25)
        .build();

client.generateStreaming("llama3", "Count from 1 to 5", opts, chunk -> {
    System.out.print(chunk.getResponse());
});
```

### Chat (multi-turn)

```java
OllamaClient client = OllamaClient.builder().build();

ChatRequest request = new ChatRequest("llama3");
request.addMessage("system", "You are a helpful assistant.");
request.addMessage("user", "Hello! Explain Java in one sentence.");

ChatResponse response = client.chat(request);

System.out.println(response.getMessage().getContent());
```

You can build full conversation history by chaining messages:

```java
request.addMessage("assistant", "Java is a statically-typed, object-oriented language...");
request.addMessage("user", "What about Python?");

ChatResponse followUp = client.chat(request);
System.out.println(followUp.getMessage().getContent());
```

### Streaming chat

```java
ChatRequest request = new ChatRequest("llama3");
request.addMessage("user", "Write me a haiku about Java.");

client.chatStreaming(request, chunk -> {
    System.out.print(chunk.getMessage().getContent());
});
```

### Embeddings

```java
EmbeddingResponse r1 = client.embed("llama3", "The cat sat on the mat");
EmbeddingResponse r2 = client.embed("llama3", "A cat was resting on the rug");

System.out.println(r1.cosineSimilarity(r2)); // ~0.97
```

---

## Model Management

All model management operations are available through `ModelManager`:

```java
ModelManager manager = ModelManager.builder().build();
```

### List installed models

```java
manager.list().forEach(System.out::println);
// llama3 (4.1 GB)
// mistral (3.8 GB)
```

### Pull a model

```java
PullResponse response = manager.pull("mistral");
System.out.println(response.isSuccess()); // true
```

### Show model details

```java
ModelDetails details = manager.show("llama3");
System.out.println(details.getModelInfo().getArchitecture()); // llama
System.out.println(details.getModelInfo().getParameterCount()); // 8030261248
```

### Create a model from a Modelfile

```java
CreateResponse response = manager.create(
        "my-model",
        "FROM llama3\nSYSTEM You are a helpful pirate."
);
System.out.println(response.isSuccess()); // true
```

### Copy a model

```java
manager.copy("llama3", "llama3-backup");
```

### Push a model to the registry

```java
PushResponse response = manager.push("my-namespace/my-model");
System.out.println(response.isSuccess()); // true
```

### List models loaded in memory

```java
manager.ps().forEach(System.out::println);
// llama3 (4.1 GB) — expires: 2026-04-19T18:00:00Z
```

### Delete a model

```java
manager.delete("llama3");
```

---

## Client Configuration

### Custom host

```java
OllamaClient client = OllamaClient.builder()
        .host("http://my-ollama-server:11434")
        .build();
```

### Custom HTTP client

You can inject your own `HttpClient` implementation, useful for testing or custom networking:

```java
HttpClient myClient = new MyCustomHttpClient();

OllamaClient client = OllamaClient.builder()
        .httpClient(myClient)
        .build();
```

The same options are available on `ModelManager`:

```java
ModelManager manager = ModelManager.builder()
        .host("http://my-ollama-server:11434")
        .httpClient(myClient)
        .build();
```

---

## Options Reference

All fields are optional. Unset fields are excluded from the request so Ollama uses its own defaults.

| Option | Type | Description |
|---|---|---|
| `temperature` | `double` | Randomness (0.0 = deterministic, 2.0 = very random). Default: 0.8 |
| `topP` | `double` | Nucleus sampling threshold. Default: 0.9 |
| `topK` | `double` | Limit sampling to top-K tokens. Default: 40 |
| `seed` | `int` | Fixed seed for reproducibility |
| `numPredict` | `int` | Max tokens to generate. -1 = unlimited |
| `repeatPenalty` | `double` | Penalise repeated tokens. Default: 1.1 |
| `presencePenalty` | `double` | Penalise tokens already in the text |
| `frequencyPenalty` | `double` | Penalise tokens by how often they appeared |
| `numCtx` | `int` | Context window size in tokens. Default: 2048 |
| `mirostat` | `double` | Mirostat mode: 0 = off, 1 = v1, 2 = v2 |
| `mirostatEta` | `double` | Mirostat learning rate. Default: 0.1 |
| `mirostatTau` | `double` | Mirostat target entropy. Default: 5.0 |
| `numGpu` | `int` | Number of GPU layers to use |
| `stop` | `String` | Stop sequence — generation halts at this string |

---

## API Overview

### OllamaClient

| Method | Endpoint | Description |
|---|---|---|
| `client.generate(model, prompt)` | `/api/generate` | Blocking text generation |
| `client.generate(model, prompt, options)` | `/api/generate` | Blocking generation with options |
| `client.generate(model, prompt, system, options)` | `/api/generate` | Blocking generation with system prompt |
| `client.generateStreaming(model, prompt, onChunk)` | `/api/generate` | Streaming text generation |
| `client.generateStreaming(model, prompt, options, onChunk)` | `/api/generate` | Streaming with options |
| `client.chat(request)` | `/api/chat` | Blocking multi-turn chat |
| `client.chatStreaming(request, onChunk)` | `/api/chat` | Streaming multi-turn chat |
| `client.embed(model, prompt)` | `/api/embeddings` | Generate embedding vector |

### ModelManager

| Method | Endpoint | Description |
|---|---|---|
| `manager.list()` | `/api/tags` | List installed models |
| `manager.pull(name)` | `/api/pull` | Download a model |
| `manager.create(name, modelfile)` | `/api/create` | Create a model from a Modelfile |
| `manager.copy(source, destination)` | `/api/copy` | Copy a model to a new name |
| `manager.push(name)` | `/api/push` | Push a model to the registry |
| `manager.ps()` | `/api/ps` | List models loaded in memory |
| `manager.show(name)` | `/api/show` | Get detailed model info |
| `manager.delete(name)` | `/api/delete` | Delete an installed model |

Full API documentation is available through Javadoc comments in the source code.

---

## Project Structure

```
jua.sergi
 ├── OllamaClient                   # Main client entry point (includes Builder as inner class)
 ├── exception
 │    └── OllamaException           # Runtime exception for API errors
 ├── http
 │    ├── HttpClient                 # HTTP client interface (post, get, delete, stream)
 │    └── JavaHttpClient             # Default implementation (Java 11 HttpClient)
 ├── manager
 │    └── ModelManager               # Model management (list, pull, create, copy, push, delete, show, ps)
 └── model
      ├── Options                    # Generation options (temperature, seed, topP, etc.)
      ├── ModelDetails               # Detailed model info from /api/show
      ├── entity
      │    ├── Message               # Chat message (role + content + thinking)
      │    ├── ModelInfo             # Installed model metadata
      │    └── RunningModel          # Model loaded in memory (from /api/ps)
      ├── request
      │    ├── GenerateRequest       # Request model for /api/generate
      │    ├── ChatRequest           # Request model for /api/chat
      │    ├── EmbeddingRequest      # Request model for /api/embeddings
      │    ├── PullRequest           # Request model for /api/pull
      │    ├── CreateRequest         # Request model for /api/create
      │    ├── CopyRequest           # Request model for /api/copy
      │    └── PushRequest           # Request model for /api/push
      └── response
           ├── GenerateResponse      # Response model for /api/generate
           ├── ChatResponse          # Response model for /api/chat
           ├── EmbeddingResponse     # Response model for /api/embeddings (includes cosineSimilarity)
           ├── ModelListResponse     # Wrapper for /api/tags response
           ├── PullResponse          # Response model for /api/pull
           ├── CreateResponse        # Response model for /api/create
           ├── PushResponse          # Response model for /api/push
           └── PsResponse            # Wrapper for /api/ps response
```

---

## Roadmap

- [x] Generate API (`/api/generate`)
- [x] Chat API (`/api/chat`)
- [x] Streaming responses (generate + chat)
- [x] Builder pattern configuration
- [x] Custom HTTP client support
- [x] Options support (temperature, seed, topP, and more)
- [x] System prompt support
- [x] Thinking support in `Message`
- [x] Embeddings API (`/api/embeddings`)
- [x] Model management — list, pull, delete, show (`/api/tags`, `/api/pull`, `/api/delete`, `/api/show`)
- [x] Model management — create, copy, push, ps (`/api/create`, `/api/copy`, `/api/push`, `/api/ps`)
- [ ] Async API with `CompletableFuture`
- [ ] Spring Boot integration

---

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.