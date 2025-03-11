# Smart Parking Management System

A comprehensive parking management system built with Spring Boot and Vue.js.

## Features

- User authentication and authorization
- Parking lot management
- Vehicle management
- Parking space allocation
- Automatic license plate recognition
- Financial transaction processing
- Reporting and analytics
- Role-based access control

## Technology Stack

### Backend
- Java 8+
- Spring Boot
- Spring Security with JWT
- Spring Data JPA
- MySQL Database
- Maven

### Frontend (to be implemented)
- Vue.js
- Vuex for state management
- Vue Router
- Axios for API calls
- Element UI for UI components

## Getting Started

### Prerequisites
- JDK 8 or higher
- Maven
- MySQL

### Database Setup
1. Create a MySQL database named `smart_parking`
2. Update the database configuration in `src/main/resources/application.properties` if needed

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` to start the application
5. The API will be available at `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login with username and password
- `POST /api/auth/change-password` - Change user password

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create a new user
- `PUT /api/users/{id}` - Update a user
- `DELETE /api/users/{id}` - Delete a user
- `PUT /api/users/{id}/profile` - Update user profile

### Parking Operations
- `POST /api/parking/entry` - Handle vehicle entry
- `POST /api/parking/exit` - Handle vehicle exit
- `GET /api/parking/active` - Get active parking records
- `GET /api/parking/records/date-range` - Get parking records by date range
- `GET /api/parking/records/vehicle/{vehicleId}` - Get parking records by vehicle
- `GET /api/parking/records/parking-lot/{parkingLotId}` - Get parking records by parking lot and date range
- `POST /api/parking/plate-detection` - Handle license plate detection

### Financial Operations
- `POST /api/financial/payment` - Process payment
- `GET /api/financial/transactions` - Get all transactions
- `GET /api/financial/transactions/date-range` - Get transactions by date range
- `GET /api/financial/transactions/parking-record/{parkingRecordId}` - Get transactions by parking record
- `GET /api/financial/revenue/date-range` - Get total revenue by date range
- `GET /api/financial/revenue/parking-lot/{parkingLotId}` - Get revenue by parking lot and date range
- `GET /api/financial/revenue/summary/payment-method` - Get revenue summary by payment method

## License
This project is licensed under the MIT License. 