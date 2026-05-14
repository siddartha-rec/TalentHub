# HR Candidate Management & Workflow Platform

Enterprise-grade recruitment management system with configurable workflows, dynamic roles & permissions, and duplicate candidate detection.

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- MySQL 8.x
- Liquibase
- MapStruct
- Redis (optional caching)

### Frontend
- React.js 18+
- TypeScript
- Vite
- Tailwind CSS / Ant Design
- Zustand / Redux Toolkit
- TanStack Query

## Project Structure

```
/workspace
├── backend/                 # Spring Boot Backend
│   ├── src/main/java/com/company/hrms/
│   │   ├── auth/           # Authentication & JWT
│   │   ├── user/           # User management
│   │   ├── role/           # Role management
│   │   ├── permission/     # Permission management
│   │   ├── workflow/       # Workflow engine
│   │   ├── candidate/      # Candidate management
│   │   ├── interview/      # Interview management
│   │   ├── audit/          # Audit logging
│   │   ├── notification/   # Notifications
│   │   ├── analytics/      # Reports & analytics
│   │   └── common/         # Common utilities
│   └── src/main/resources/
│       ├── application.yml
│       └── db/changelog/   # Liquibase migrations
│
└── frontend/               # React Frontend (to be implemented)
```

## Key Features

### Phase 1 (MVP)
- ✅ Dynamic Roles & Permissions (database-driven)
- ✅ Configurable Workflow Engine
- ✅ Candidate Lifecycle Management
- ✅ Duplicate Candidate Detection
- ✅ Candidate Transition History
- ✅ Audit Logging

### Phase 2
- Interview Management
- Notifications (Email & In-app)
- Reports & Analytics

### Phase 3
- AI Features (Resume parsing, ranking)
- External Integrations (LinkedIn, Calendar)
- Advanced Workflow Features

## Database Schema

Core Tables:
- `users` - User accounts
- `roles` - Dynamic roles
- `permissions` - Permission definitions
- `role_permissions` - Role-permission mapping
- `user_roles` - User-role mapping
- `workflow_master` - Workflow definitions
- `workflow_stage` - Workflow stages
- `workflow_transition` - Stage transitions with rules
- `candidates` - Candidate profiles
- `candidate_documents` - Resumes & documents
- `candidate_comments` - Internal notes
- `candidate_transition_history` - Stage change audit
- `interviews` - Interview scheduling
- `interview_feedback` - Interview evaluations
- `audit_logs` - System audit trail

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- MySQL 8.x
- Node.js 18+ (for frontend)
- Docker (optional)

### Backend Setup

1. Configure database in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hrms_db
    username: root
    password: your_password
```

2. Build and run:
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

3. Access Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

### API Endpoints

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh token
- `POST /api/auth/logout` - Logout

#### Candidates
- `POST /api/candidates` - Create candidate
- `GET /api/candidates` - List candidates
- `GET /api/candidates/{id}` - Get candidate details
- `PUT /api/candidates/{id}` - Update candidate
- `DELETE /api/candidates/{id}` - Archive candidate
- `POST /api/candidates/{id}/transition` - Change stage

#### Workflows
- `POST /api/workflows` - Create workflow
- `GET /api/workflows` - List workflows
- `GET /api/workflows/{id}` - Get workflow details
- `POST /api/workflows/{id}/stages` - Add stage
- `POST /api/workflows/{id}/transitions` - Add transition

#### Roles & Permissions
- `POST /api/roles` - Create role
- `GET /api/roles` - List roles
- `POST /api/roles/{id}/permissions` - Assign permissions
- `GET /api/permissions` - List all permissions

#### Interviews
- `POST /api/interviews` - Schedule interview
- `POST /api/interviews/feedback` - Submit feedback

## Security

- JWT-based authentication
- Role-Based Access Control (RBAC)
- Permission-Based Access Control (PBAC)
- Password encryption (BCrypt)
- Audit logging for all critical operations

## Configuration Principles

**DO NOT HARDCODE:**
- ❌ Roles
- ❌ Permissions
- ❌ Workflow stages
- ❌ Workflow transitions
- ❌ Approval flows

**Everything is:**
- ✅ Database-driven
- ✅ Configurable via UI/API
- ✅ Permission-controlled
- ✅ Extensible

## License

Proprietary - All rights reserved
