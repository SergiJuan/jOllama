<div align="center">
  <img src="https://raw.githubusercontent.com/SergiJuan/jOllama/main/jollama-logo.png" width="200" alt="jOllama logo">

# jOllama — Ollama Java Client

A simple Java client for interacting with the Ollama API and local LLMs.

![GitHub stars](https://img.shields.io/github/stars/SergiJuan/jOllama)
![GitHub forks](https://img.shields.io/github/forks/SergiJuan/jOllama)
![Contributors](https://img.shields.io/github/contributors/SergiJuan/jOllama?style=social)

![Java](https://img.shields.io/badge/Java-11+-orange)
![GitHub last commit](https://img.shields.io/github/last-commit/SergiJuan/jOllama?color=green)
![JitPack](https://img.shields.io/jitpack/version/com.github.SergiJuan/jOllama)
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
- Builder pattern for clean client configuration
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
    <version>0.0.1</version>
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
    implementation 'com.github.SergiJuan:jOllama:0.0.1'
}
```

---

## Quick Start

```java
import jua.sergi.OllamaClient;
import jua.sergi.model.GenerateResponse;

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

### Chat (multi-turn)

```java
import jua.sergi.OllamaClient;
import jua.sergi.model.ChatRequest;
import jua.sergi.model.ChatResponse;

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

---

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| `client.generate(model, prompt)` | `/api/generate` | Generate text from a prompt |
| `client.chat(request)` | `/api/chat` | Multi-turn chat with message history |

Full API documentation is available through Javadoc comments in the source code.

---

## Project Structure

```
jua.sergi
 ├── OllamaClient          # Main client entry point
 ├── OllamaClientBuilder   # Builder for client configuration
 ├── http
 │    ├── HttpClient        # HTTP client interface
 │    └── JavaHttpClient    # Default implementation (Java 11 HttpClient)
 └── model
      ├── GenerateRequest   # Request model for /api/generate
      ├── GenerateResponse  # Response model for /api/generate
      ├── ChatRequest       # Request model for /api/chat
      ├── ChatResponse      # Response model for /api/chat
      └── Message           # Chat message (role + content)
```

---

## Roadmap

- [x] Generate API (`/api/generate`)
- [x] Chat API (`/api/chat`)
- [x] Builder pattern configuration
- [x] Custom HTTP client support
- [x] Streaming responses
- [ ] Async API with `CompletableFuture`
- [ ] Embeddings API (`/api/embeddings`)
- [ ] Model management (list, pull, create, delete)
- [ ] Spring Boot integration

---

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.