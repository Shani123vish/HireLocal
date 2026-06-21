# HireLocal — Hyperlocal Gig Worker Platform

> Connecting customers with trusted local service professionals in Tier-2 cities like Bhopal, Indore, and Jabalpur.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java, Servlet API, JDBC |
| Frontend | JSP, HTML5, Tailwind CSS |
| Database | MySQL 8+ |
| Server | Apache Tomcat 10+ |
| IDE | Eclipse (Enterprise Java) |

---

## Features

### Customer
- Register and login with role-based access
- Post service jobs with category, description, and address
- View real-time job status — Pending, Assigned, Completed, Cancelled
- Mark job complete and release escrow payment
- Rate and review workers after job completion
- Cancellation penalty system — 1st free, 2nd ₹10, 3rd ₹25+

### Worker / Gig Provider
- Register and set up service profile
- Set base price, service area, experience, and category
- View and accept available jobs matching their category
- Track earnings via escrow-based payment system
- Admin verification badge system

### Admin
- View platform stats — total jobs and total revenue
- Verify workers and approve their profiles
- Ban abusive users

---

## Service Categories

| Home Services | Cleaning | Personal | Other |
|--------------|----------|----------|-------|
| Electrician | House Cleaning | Salon at Home | Packers & Movers |
| Plumber | Sofa/Carpet Clean | Massage Therapy | Driver (Daily) |
| Carpenter | Car Washing | Yoga Trainer | Delivery Boy |
| AC/Fridge Repair | Kitchen Deep Clean | Baby Sitter | Pet Care |
| Painter | Water Tank Clean | Old Age Care | Event Helper |

---

## Database Schema

| Table | Purpose |
|-------|---------|
| users | All roles — customer, worker, admin, super_admin |
| worker_profiles | Worker skills, base price, service area, category |
| service_categories | 20 service categories across 4 types |
| jobs | Job postings by customers with status tracking |
| job_assignments | Worker job acceptance and completion |
| escrow | Payment hold and release system |
| transactions | Complete payment history |
| wallets | Customer and worker balances |
| cancellations | Cancel count and penalty tracking |
| ratings | Customer reviews and star ratings |
| notifications | In-app alerts for users |
| cities | City expansion management |

---

## Project Structure
HireLocal/

├── src/main/java/com/hirelocal/

│   ├── db/

│   │   └── DBConnection.java

│   ├── model/

│   │   ├── User.java

│   │   ├── WorkerProfile.java

│   │   ├── Job.java

│   │   └── JobAssignment.java

│   ├── dao/

│   │   ├── UserDAO.java

│   │   ├── WorkerDAO.java

│   │   ├── JobDAO.java

│   │   ├── JobAssignmentDAO.java

│   │   ├── EscrowDAO.java

│   │   ├── RatingDAO.java

│   │   ├── CancellationDAO.java

│   │   └── AdminDAO.java

│   └── servlet/

│       ├── RegisterServlet.java

│       ├── LoginServlet.java

│       ├── LogoutServlet.java

│       ├── WorkerProfileServlet.java

│       ├── JobServlet.java

│       ├── AcceptJobServlet.java

│       ├── CompleteJobServlet.java

│       ├── CancelJobServlet.java

│       ├── RatingServlet.java

│       ├── VerifyWorkerServlet.java

│       └── BanUserServlet.java

└── src/main/webapp/

├── navbar.jsp

├── login.jsp

├── register.jsp

├── worker-profile.jsp

├── post-job.jsp

├── customer/

│   └── dashboard.jsp

├── worker/

│   └── dashboard.jsp

└── admin/

└── dashboard.jsp
---

## Setup Instructions

### Prerequisites
- JDK 17+
- Apache Tomcat 10+
- MySQL 8+
- Eclipse IDE (Enterprise Java)
- mysql-connector-j JAR

### Step 1 — Clone Repository
```bash
git clone https://github.com/yourusername/HireLocal.git
```

### Step 2 — Database Setup
```sql
CREATE DATABASE hirelocal;
USE hirelocal;
```
Run all table creation queries from the schema file.

### Step 3 — Configure Database Connection
Open `src/main/java/com/hirelocal/db/DBConnection.java` and update:
```java
private static final String URL = "jdbc:mysql://localhost:3306/hirelocal?useSSL=false&allowPublicKeyRetrieval=true";
private static final String USER = "root";
private static final String PASSWORD = "your_password_here";
```

### Step 4 — Add MySQL Connector JAR
Place `mysql-connector-j-x.x.x.jar` inside:
WebContent/WEB-INF/lib/
### Step 5 — Deploy on Tomcat
- Import project in Eclipse as Dynamic Web Project
- Add Apache Tomcat 10 server
- Right click project → Run As → Run on Server

### Step 6 — Create Admin User
```sql
INSERT INTO users(name, email, number, password, role, city)
VALUES('Admin', 'admin@hirelocal.com', '9999999999', 'admin123', 'admin', 'Bhopal');
```

### Step 7 — Access Application
http://localhost:8080/HireLocal/login.jsp
---

## User Flow
Customer → Register → Login → Post Job → View Status → Mark Complete → Rate Worker

Worker   → Register → Login → Create Profile → Accept Job → Complete Job

Admin    → Login → Verify Workers → View Revenue Dashboard
---

## Payment Flow
Customer Pays → Amount held in Escrow → Job Completed → Payment Released to Worker

Platform automatically deducts commission based on job value slab
---

## Commission Slabs

| Job Value | Commission | Example |
|-----------|-----------|---------|
| ₹0 – ₹500 | 10% | Bulb/switch fix |
| ₹501 – ₹1000 | 12% | Plumbing repair |
| ₹1001 – ₹2000 | 15% | AC service |
| ₹2000+ | 18% | Full house paint |

---

## Cancellation Policy

| Cancel Count | Penalty |
|-------------|---------|
| 1st Cancel | No penalty — warning shown |
| 2nd Cancel | ₹10 deducted from wallet |
| 3rd Cancel | ₹25 deducted + 1 hour booking ban |
| Repeated | Account temporarily suspended |

---

## Business Model

| Revenue Stream | Details | Monthly Estimate |
|---------------|---------|-----------------|
| Commission (10-18%) | Per booking cut | ₹15,000 – ₹40,000 |
| Worker Subscription | ₹199/month verified badge | ₹10,000 – ₹50,000 |
| Featured Listing | Top placement fee | ₹5,000 – ₹15,000 |

---

## Market Opportunity

- Urban Company operates only in metros — zero competition in Tier-2 cities
- Bhopal, Indore, Jabalpur — untapped market of millions
- Expansion roadmap: Bhopal → MP → Pan India

---

## Author

**Shani Vishwakarma**
Final Year Project — Startup Ready
Domain: HRTech / GigEconomy / Local Services
Target Cities: Bhopal → Indore → Jabalpur
