# API Response Standardization Library

A lightweight Java library for standardizing REST API responses across projects with comprehensive builder support.

## ✨ Features

- ✅ Standardized success/error response format
- ✅ HTTP status code validation (blocks HTTP 204)
- ✅ Type-safe generic responses
- ✅ Jackson serialization support
- ✅ Fluent builder pattern for complex responses
- ✅ Comprehensive validation to prevent invalid states
- ✅ Exception hierarchy for consistent error handling

## 🚀 Quick Start

### Installation

```gradle
implementation 'com.github.vikunlabs:lib-apicore:x.x.x'
```

Always look for the latest version of this library here - [JitPack](https://jitpack.io/#vikunalabs/lib-apicore)

### Basic Usage

```java
// Success response
return APIResponse.success(user, BaseAPICode.USER_CREATED);

// Error response
APIError error = new APIError("NOT_FOUND", "User not found");
return APIResponse.error(error, BaseAPICode.RESOURCE_NOT_FOUND);
```

## 📚 Documentation

- [**Response Builders Guide**](docs/RESPONSE_BUILDERS.md) - Comprehensive builder patterns
- [**Exceptions Guide**](docs/EXCEPTIONS_GUIDE.md) - Exception handling best practices
- [**Framework Integration**](docs/FRAMEWORK_INTEGRATION.md) - Spring Boot, JAX-RS, Servlet
- [**API Codes Reference**](docs/API_CODES.md) - Status code mappings and custom codes
- [**Best Practices**](docs/BEST_PRACTICES.md) - Implementation guidelines
- [**Advanced Usage**](docs/ADVANCED_USAGE.md) - Customization and extensions
- [**FAQ**](docs/FAQ.md) - Common questions and solutions
- [**Roadmap**](docs/ROADMAP.md) - Future enhancements and planning

## 🏗️ Core Concepts

### APIResponse Structure
All responses follow a standardized format with status, data/error, message, and timestamp.

### APICode System
Pre-defined status codes with proper HTTP status mappings ensure consistency.

### Builder Pattern
Fluent builders for complex responses with validation errors and detailed metadata.

## 🔌 Framework Integration

The library supports multiple frameworks:
- **Spring Boot** - Controller advice and response entities
- **JAX-RS** - Exception mappers and response providers
- **Servlet API** - Filters and exception handling

See [Framework Integration](docs/FRAMEWORK_INTEGRATION.md) for detailed setup guides.

## ⚠️ Important: HTTP 204 Handling

**HTTP 204 No Content responses cannot use this library** as they must not have any response body. Use framework-native methods instead:

```java
// ✅ Correct - Spring Boot
return ResponseEntity.noContent().build();

// ✅ Correct - JAX-RS
return Response.noContent().build();

// ❌ Incorrect - Will throw exception
return APIResponse.success(null, BaseAPICode.NO_CONTENT);
```

## 🔮 Future Enhancements

We're constantly improving the library! Check out our [Roadmap](docs/ROADMAP.md) to see what's coming next, suggest new features, or contribute to the development.

## 📄 License

MIT License - feel free to use in your projects!

---

*Need help? Check the [FAQ](docs/FAQ.md) or create an issue on GitHub!*