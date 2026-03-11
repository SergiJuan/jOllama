<div align="center">
  <img src="https://raw.githubusercontent.com/SergiJuan/ollama-java/main/jollama-logo.png" width="200" alt="Ollama Java Client">

### Jollama - Ollama Java Client

</div>

<div align="center">
A simple Java library for interacting with the Ollama API.

![GitHub stars](https://img.shields.io/github/stars/SergiJuan/ollama-java)
![GitHub forks](https://img.shields.io/github/forks/SergiJuan/ollama-java)
![GitHub License](https://img.shields.io/github/license/SergiJuan/ollama-java)
</div>

## Table of Contents

- [Capabilities](#capabilities)
- [Requirements](#requirements)
- [Usage](#usage)
    - [Maven](#maven)
    - [Gradle](#gradle)
- [Examples](#examples)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

---

## Capabilities

- Multi-turn chat with conversation history
- Text generation (`generate`)
- Async requests
- Custom roles for chat
- Model management (list, pull, create, delete)
- Simple HTTP client wrapper
- Exception handling for Ollama API errors

---

## Requirements

- Java 11+
- Ollama Server >= 0.11.10
- Maven or Gradle

---

## Usage

### Maven

```xml
<dependency>
    <groupId>jua.sergi</groupId>
    <artifactId>ollama-java-client</artifactId>
    <version>1.0.0</version>
</dependency>