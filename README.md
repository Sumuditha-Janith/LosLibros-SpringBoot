# LosLibros-SpringBoot

LosLibros is a comprehensive, multi-user web application for managing a bookstore. It is built with the Spring Boot framework and provides a platform for administrators, employees, and users to interact with the bookstore's inventory and services.

---

## Features

* **Multi-User Platform:** Separate interfaces and functionalities for Admin, Employee, and regular Users.
* **Secure Registration & Login:** Robust user authentication and authorization.
* **Password Reset:** Functionality for users to securely reset their passwords.
* **Book Management:** Admins and employees can add, edit, and remove books from the inventory.
* **User Management:** Admins can manage user accounts and roles.
* **Special Publisher Sale Events:** Functionality to create and manage special sales events from publishers.
* **Chat Feature:** A real-time chat feature for users to ask questions and get support.
* **Order History:** Users can view their past orders.
* **Order Management:** Admins and employees can manage and process customer orders.

---

# LosLibros: Full-Stack Bookstore Management System

<br>

## About The Project

**LosLibros** is a comprehensive, full-stack web application designed for managing an online bookstore. It features a robust Spring Boot backend that provides RESTful APIs and a separate, dynamic frontend to ensure a seamless and responsive user experience.

The platform supports multiple user roles (**Admin, Employee, and User**), each with tailored permissions and functionalities. Key features include secure user registration, book and user management, special sales event creation, a customer support chat, and complete order management with user order history.

---

## Technology Stack

| Area      | Technology                                                              |
| --------- | ----------------------------------------------------------------------- |
| **Backend** | `Spring Boot`, `Spring Security`, `Spring Data JPA`, `Hibernate`, `MySQL`, `Maven` |
| **Frontend** | `HTML5`, `CSS3`, `Bootstrap 5`, `JavaScript`, `jQuery`                      |

---

## Getting Started

To get a local copy up and running, follow these simple installation steps.

### Prerequisites

Before you begin, ensure you have the following installed on your system:
* Java JDK 17 or newer
* Apache Maven
* MySQL Server
* An IDE (like IntelliJ IDEA or VS Code)
* A modern web browser with a live server extension (for the frontend)

### Installation & Setup

This project consists of two separate parts: the backend and the frontend. You need to set up both.

#### Backend Setup

1.  **Clone the backend repository** (replace the URL with your actual repository link):
    ```sh
    git clone (https://github.com/Sumuditha-Janith/LosLibros-SpringBoot.git)
    ```
2.  **Open the project** in your favorite IDE (e.g., IntelliJ IDEA).
3.  **Configure the database connection**:
    * Navigate to `src/main/resources/application.properties`.
    * Update the following properties with your MySQL database URL, username, password, and your email credentials (if used for features like password reset).
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password

    # Email configuration for password reset, etc.
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your_email@gmail.com
    spring.mail.password=your_app_password
    ```
4.  **Run the application**: The server will start on `http://localhost:8080`. You can run it from your IDE or using the Maven command:
    ```sh
    mvn spring-boot:run
    ```

#### Frontend Setup

1.  **Clone the frontend repository** (if it's in a separate repository):
    ```sh
    git clone https://github.com/Sumuditha-Janith/LosLibros-SpringBoot.git
    ```
    *If the frontend files are in the same repository, you can skip this step.*

2.  **Open the project folder** in your IDE or a code editor like VS Code.
3.  **Run the `index.html` file** using a live server extension to view the website.

---

## Screenshots and Demos
<img width="1920" height="1080" alt="Screenshot (365)" src="https://github.com/user-attachments/assets/01702586-e286-4850-be89-a9880ea9cc6d" />
<img width="1920" height="1080" alt="Screenshot (364)" src="https://github.com/user-attachments/assets/f785fb76-e563-4a1a-95c1-6d57c0bc31a5" />
<img width="1920" height="1080" alt="Screenshot (363)" src="https://github.com/user-attachments/assets/59e14afb-9546-4b9b-9042-215e78405a97" />
<img width="1920" height="1080" alt="Screenshot (362)" src="https://github.com/user-attachments/assets/4aa03c23-1252-466f-b0c8-af9eb1d1f43f" />
<img width="1920" height="1080" alt="Screenshot (361)" src="https://github.com/user-attachments/assets/323816a4-9dd8-480a-8544-8b3296540007" />
<img width="1920" height="1080" alt="Screenshot (360)" src="https://github.com/user-attachments/assets/bbe911ad-9594-4b51-b27c-1f868e94cdaf" />
<img width="1920" height="1080" alt="Screenshot (359)" src="https://github.com/user-attachments/assets/3dd15e64-da0e-487d-9775-3de50d6f827d" />
<img width="1920" height="1080" alt="Screenshot (358)" src="https://github.com/user-attachments/assets/a37657e0-0ddf-4633-bb0c-2ea699bff0c3" />
<img width="1920" height="1080" alt="Screenshot (357)" src="https://github.com/user-attachments/assets/ce01cef8-1125-4bd1-be3c-210e80202804" />
<img width="1920" height="1080" alt="Screenshot (356)" src="https://github.com/user-attachments/assets/fffad93c-0c00-4a62-97b6-24f3ff19f802" />
<img width="1920" height="1080" alt="Screenshot (355)" src="https://github.com/user-attachments/assets/eb5349d5-b160-4244-8a09-7c47d0748f1e" />
<img width="1920" height="1080" alt="Screenshot (354)" src="https://github.com/user-attachments/assets/eeadf950-7154-4ec4-b515-2ee268916539" />
<img width="1920" height="1080" alt="Screenshot (353)" src="https://github.com/user-attachments/assets/231639d3-18c6-4fa3-a214-8c6be0df2abd" />


Demo Video - https://www.youtube.com/watch?v=myFi6ftH0LE



## Contributing

Contributions make the open-source community an amazing place to learn and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/NewFeature`)
3.  Commit your Changes (`git commit -m 'Add some NewFeature'`)
4.  Push to the Branch (`git push origin feature/NewFeature`)
5.  Open a Pull Request

---

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

