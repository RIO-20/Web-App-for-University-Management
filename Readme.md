# University Management Web App (univ_management)

## 📖 Overview
The **University Management** project is a robust MVC web application designed to help universities manage operations efficiently. It tracks and administers key organizational aspects including faculties, rooms, equipment, sponsorships, and associated financial transactions. 

It provides analysis capabilities such as tracking budget stats, calculating room maintenance costs, and assessing overall financial impact via a user-friendly frontend.

## 🛠️ Technology Stack
- **Backend Framework**: Java 17 & Spring Boot 4.0.1
- **Web Layer**: Spring WebMVC + Spring Validation
- **Frontend / View**: Thymeleaf (Server-Side rendering)
- **Database Architecture**: Microsoft SQL Server (`mssql-jdbc`)
- **Data Access Layer**: Spring JDBC (No-ORM approach using `JdbcTemplate`)
- **Utility**: Lombok (reduces boilerplate code for POJOs and DTOs)

## 🏗️ Architecture Design
The application adheres to a clean, standard **Layered Architecture**. The lack of an ORM (like Hibernate) implies a performant, fine-tuned approach utilizing raw SQL queries and explicitly mapped results:

*   **Controllers** (`controller/`): Handle HTTP requests, manage user sessions, and render Thymeleaf templates (e.g., `PageController`, `EquipmentController`).
*   **Services** (`service/`): Encapsulate the application's core business logic and orchestrate data flow between DAOs and Controllers.
*   **DAOs (Data Access Objects)** (`dao/`): Directly communicate with the SQL Server via `JdbcTemplate` to execute native SQL queries.
*   **Mappers** (`mapper/`): Implement Spring's `RowMapper` to explicitly transform database result sets (`ResultSet`) into domain models or DTOs.
*   **Models** (`model/`): Domain entities that mirror the core database tables representations.
*   **DTOs (Data Transfer Objects)** (`dto/`): Specialized data structures designed specifically to serve views, calculate statistics, or join multiple entities.

## 🧱 Core Modules & Entities
1. **Faculties & Rooms** (`Facultate`, `Sala`):
   - Manage university faculties.
   - Track room definitions, calculating data such as room efficiency (`RoomEfficiencyDto`) and general room statistics (`RoomStatsDto`).
2. **Equipment & Maintenance** (`Dotare`, `Dotare_Tranzactie`):
   - Inventory tracking for university assets.
   - Generates views for maintenance analysis (`MaintenanceCostDto`) and historical tracking (`EquipmentWithHistoryDto`).
3. **Finances & Transactions** (`Tranzactie`, `Dotare_Tranzactie`):
   - Handling university budget spending and transactional history.
   - Calculates financial impact statements (`FinancialImpactDto`, `BudgetStatsDto`).
4. **Sponsors** (`Sponsor`, `Sponsor_Facultate`):
   - Manages external sponsors, linking them financially to specific faculties.
5. **Administration & Internal Logic** (`Administrator`, `Message`, `Action`):
   - Restricts application capabilities to authorized administrators.
   - Contains notification handlers and operational logic refinements.

## 🚀 Running the Application

### Prerequisites
* JDK 17+ installed.
* Available Microsoft SQL Server instance running.
* Maven build tool (or use the provided [mvnw](cci:7://file:///e:/Facultate/Anul%203/SEM%201/BD/Proiect_Final/univ_management/mvnw:0:0-0:0) wrapper).

### Execute Locally 
1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code).
2. Ensure your `application.properties` or `application.yml` correctly targets your local MS SQL Server instance with the proper credentials.
3. Use the integrated Maven wrapper to run the application:
   ```bash
   ./mvnw spring-boot:run
