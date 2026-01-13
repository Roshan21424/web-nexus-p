# Nexus — Unified Academic Workspace

Nexus is a **full-stack academic collaboration platform** designed to streamline classroom operations, task tracking, and peer interaction within educational institutions. Built for colleges and universities, Nexus provides a comprehensive suite of tools for managing academic workflows, attendance, assignments, and social engagement — all within a secure, role-based environment.

The platform enables students and faculty to:
- Manage semester-based academic workflows and attendance
- Track assignments and classroom tasks
- Share updates through a college-exclusive social feed
- Access secure class repositories with automatic semester cleanup
- Collaborate with peers in a controlled academic environment

Nexus reflects **real-world academic management systems** with proper authentication, authorization, and data lifecycle management.

## Key Features

- 📚 **Academic Workflow Management** — Semester-based course and task organization  
- ✅ **Attendance Tracking** — Automated attendance management for classes  
- 📝 **Assignment & Task Management** — Create, assign, and track academic tasks  
- 📱 **College Social Feed** — Photo uploads and peer interaction within the institution  
- 📂 **Secure Class Repository** — File storage with automatic cleanup after semester completion  
- 🔐 **Role-Based Access Control** — Implemented using Spring Security and OAuth 2.0  
- 👥 **Multi-Role Support** — Separate interfaces for students, faculty, and administrators  
- ☁️ **Cloud Storage Integration** — AWS S3 for scalable file management  

## System Architecture

Nexus uses a **modern full-stack architecture** with clear separation of concerns between frontend, backend, and data storage layers.

### 🔹 Spring Boot (Main Application Server)
Responsible for:
- User authentication and authorization (OAuth 2.0)
- Role-based access control (Spring Security)
- Academic workflow orchestration
- Attendance management
- Assignment and task tracking
- Social feed management
- File repository management with AWS S3 integration
- Persistent data storage using MySQL and MongoDB

### 🔹 React.js (Frontend Application)
Responsible for:
- Responsive user interface
- Role-specific dashboards
- Real-time updates and notifications
- File upload and management
- Social feed interactions

### 🔹 Database Architecture
- **MySQL** — Relational data (users, courses, attendance, assignments)
- **MongoDB** — Document storage (social feed posts, comments, media metadata)
- **AWS S3** — File storage for class repositories and social media uploads

## Communication Flow

1. User authenticates via OAuth 2.0  
2. Role-based dashboard is displayed (Student/Faculty/Admin)  
3. Faculty creates courses and assignments  
4. Students access course materials and submit tasks  
5. Attendance is tracked and recorded automatically  
6. Social feed allows photo uploads and peer interaction  
7. Semester completion triggers automatic repository cleanup  

## Tech Stack

<p align="left">
  <img src="https://skillicons.dev/icons?i=java,spring,react,javascript,tailwind,mysql,mongodb,aws" />
</p>

### Frontend
- React.js
- Tailwind CSS

### Backend
- Spring Boot
- Spring Security (OAuth 2.0 Authentication)
- RESTful API Architecture

### Databases & Storage
- MySQL (Relational data)
- MongoDB (Document storage)
- AWS S3 (File storage)

### Security & Authentication
- OAuth 2.0
- JWT-based authorization
- Role-Based Access Control (RBAC)

## Authentication & Authorization

- **OAuth 2.0** authentication for secure login
- **JWT tokens** for session management
- **Role-based access control** with three primary roles:
  - **Student** — Access courses, submit assignments, view grades
  - **Faculty** — Manage courses, track attendance, grade assignments
  - **Admin** — System-wide management and configuration

## Local Setup Guide

### Prerequisites

Ensure the following are installed on your system:

- **Java 17+**
- **Node.js 18+**
- **MySQL 8+**
- **MongoDB 6+**
- **AWS Account** (for S3 bucket)
- **Git**

### 1. Clone the Repository

```bash
git clone https://github.com/Roshan21424/web-nexus-p.git
cd web-nexus-p
```

### 2. Set Up AWS S3 Bucket

Nexus uses AWS S3 for storing class repository files and social media uploads.

#### Create an S3 Bucket

1. Log in to your [AWS Console](https://console.aws.amazon.com/)
2. Navigate to **S3** service
3. Click **Create bucket**
4. Configure your bucket:
   - **Bucket name**: Choose a unique name (e.g., `nexus-classroom-files`)
   - **Region**: Select your preferred region
   - **Block Public Access**: Keep public access blocked for security
   - **Versioning**: Enable if you want file version history
5. Click **Create bucket**

#### Create IAM User with S3 Access

1. Navigate to **IAM** → **Users** → **Add users**
2. Create a user (e.g., `nexus-s3-user`)
3. Attach the policy: **AmazonS3FullAccess** (or create a custom policy with limited access)
4. Generate **Access Key ID** and **Secret Access Key**
5. **Save these credentials securely** — you'll need them for configuration

### 3. Configure Databases

#### Start MySQL

Start your MySQL server and create the database:

```sql
CREATE DATABASE nexus;
```

Create a user with appropriate permissions:

```sql
CREATE USER 'nexus_user'@'localhost' IDENTIFIED BY 'YOUR_DB_PASSWORD';
GRANT ALL PRIVILEGES ON nexus.* TO 'nexus_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Start MongoDB

```bash
mongod --dbpath /path/to/your/data/directory
```

Or if using a system service:

```bash
# Linux/macOS
sudo systemctl start mongod

# Windows
net start MongoDB
```

### 4. Configure Spring Boot Backend

Edit: `backend/src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8080

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/nexus
spring.datasource.username=nexus_user
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/nexus

# AWS S3 Configuration
aws.s3.bucket.name=nexus-classroom-files
aws.s3.region=us-east-1
aws.access.key.id=YOUR_AWS_ACCESS_KEY
aws.secret.access.key=YOUR_AWS_SECRET_KEY

# File Upload Configuration
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# OAuth 2.0 Configuration (Google Example)
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email
```

Replace the placeholders:
- `YOUR_DB_PASSWORD` — Your MySQL password
- `YOUR_AWS_ACCESS_KEY` — Your AWS Access Key ID
- `YOUR_AWS_SECRET_KEY` — Your AWS Secret Access Key
- `YOUR_GOOGLE_CLIENT_ID` — Your Google OAuth Client ID
- `YOUR_GOOGLE_CLIENT_SECRET` — Your Google OAuth Client Secret
- `nexus-classroom-files` — Your S3 bucket name
- `us-east-1` — Your AWS region

#### Setting Up OAuth 2.0 (Google)

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Navigate to **APIs & Services** → **Credentials**
4. Click **Create Credentials** → **OAuth 2.0 Client ID**
5. Configure the OAuth consent screen
6. Set **Authorized redirect URIs** to: `http://localhost:8080/login/oauth2/code/google`
7. Copy the **Client ID** and **Client Secret**

### 5. Run Spring Boot Backend

Navigate to the backend directory and start the application:

```bash
cd backend
./mvnw spring-boot:run
```

**Windows users:**

```bash
mvnw.cmd spring-boot:run
```

The backend will run at: **http://localhost:8080**

### 6. Configure React Frontend

Create a `.env` file in `frontend/`:

```bash
cd frontend
```

Create `.env` file with the following content:

```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_OAUTH_ENABLED=true
```

### 7. Run React Frontend

Install dependencies and start the frontend:

```bash
npm install
npm start
```

The frontend will run at: **http://localhost:3000**

### 8. Access the Application

Open your browser and navigate to:

**http://localhost:3000**

#### Default Test Accounts

After the application starts, you may need to create initial accounts through the OAuth flow or seed the database with test users.

## 🔧 Troubleshooting

### Database Connection Errors

**MySQL:**
- Verify MySQL is running: `mysql -u nexus_user -p`
- Ensure the `nexus` database exists
- Check credentials in `application.properties`

**MongoDB:**
- Verify MongoDB is running: `mongosh` or `mongo`
- Check connection string in `application.properties`

### AWS S3 Issues

- Verify your AWS credentials are correct
- Ensure the S3 bucket exists and the region matches
- Check IAM user permissions for S3 access
- Verify bucket name is globally unique

### OAuth 2.0 Issues

- Confirm redirect URI matches exactly: `http://localhost:8080/login/oauth2/code/google`
- Verify OAuth consent screen is configured
- Check client ID and secret are correct
- Ensure your Google Cloud project has OAuth enabled

### Port Already in Use

If any port (3000, 8080) is already in use:
- Kill the process using that port, or
- Change the port in the respective configuration files


## Security Best Practices

- **Never commit** `.env` files, AWS credentials, or OAuth secrets to version control
- Add to `.gitignore`:
  ```
  .env
  application.properties
  *.pem
  *.p12
  ```
- Use environment variables for all sensitive data
- Rotate AWS credentials regularly
- Implement proper CORS configuration for production
- Use HTTPS in production environments
- Enable S3 bucket encryption
- Implement rate limiting on API endpoints

## Automatic Data Cleanup

Nexus automatically manages class repository data lifecycle:

- **Semester Completion Detection** — System tracks semester end dates
- **Automatic Cleanup** — Files and data are archived/deleted after semester completion
- **Configurable Retention** — Administrators can configure retention policies
- **Audit Trail** — All cleanup operations are logged for compliance

## Future Improvements

- Mobile application (React Native)
- Real-time notifications using WebSockets
- Advanced analytics dashboard
- Integration with LMS platforms (Canvas, Moodle)
- Video conferencing integration
- AI-powered assignment grading
- Plagiarism detection
- Multi-language support
- Calendar integration
- Email notifications

## Contributions

Contributions, suggestions, and improvements are welcome.  
Feel free to open an issue or submit a pull request.

---

**Built with ❤️ for academic excellence**
