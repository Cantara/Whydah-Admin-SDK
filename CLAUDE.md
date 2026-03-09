# Whydah-Admin-SDK

## Purpose
Client library for Whydah administrative operations. Provides XML/JSON parsing of Whydah data structures, session management for application and user sessions, and client logic for the UserAdminService (UAS) administrative API.

## Tech Stack
- Language: Java 21
- Framework: None (pure library)
- Build: Maven
- Key dependencies: Whydah-Java-SDK, Hystrix, SLF4J

## Architecture
SDK library extending Whydah-Java-SDK with administrative capabilities. Provides session handlers for both application and user sessions, utility methods for common admin API calls, and data structure serialization. Used by applications with admin privileges to manage users, roles, and applications through the UAS API.

## Key Entry Points
- Session handlers: `ApplicationSessionHandler`, `UserSessionHandler`
- Admin API client utilities
- `pom.xml` - Maven coordinates: `net.whydah.sso:Whydah-Admin-SDK`

## Development
```bash
# Build
mvn clean install

# Test
mvn test
```

## Domain Context
Whydah IAM administration. Provides the client-side SDK for applications that need to perform user management, role assignment, and other administrative operations through the Whydah system.
