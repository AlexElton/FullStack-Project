# 🌟 FullStack-Project 🌟

Welcome to the **FullStack-Project**! This is an **AWESOME** full-stack application built with **Vue 3** on the frontend and **Spring Boot** on the backend. 🚀 It's designed to be a marketplace where users can buy and sell products seamlessly. 🛒💻

---

## 🏗️ Project Structure

The project is divided into two main parts:

### 1. **Frontend** (📦 `/Frontend/market`)
- Built with **Vue 3** and **Vite** for a blazing-fast development experience. ⚡
- Features:
  - **Dynamic Product Listings**: Browse and filter products by categories, price, and more.
  - **Shopping Cart**: Add items to your cart and view a detailed summary before checkout.
  - **Sell Items**: List your own products for sale with images, descriptions, and pricing.
  - **Responsive Design**: Works beautifully on all devices. 📱💻
  - **End-to-End Testing**: Powered by **Cypress** for robust testing. 🧪
  - **Unit Testing**: Ensured with **Vitest** for component-level reliability. ✅

### 2. **Backend** (📦 `/Backend`)
- Powered by **Spring Boot** for a robust and scalable backend. 🛠️
- Features:
  - **RESTful API**: Handles all the data operations for the marketplace.
  - **H2 Database**: Lightweight and easy-to-use in-memory database for development. 🗄️
  - **Spring Data JPA**: Simplifies database interactions.
  - **Testing**: Includes unit tests to ensure backend reliability. 🧪

---

## 🚀 How to Get Started

### Prerequisites
- **Node.js** (for the frontend)
- **Java 17+** (for the backend)
- **Maven** (for managing backend dependencies)

### Setup Instructions

#### 1. Clone the Repository
```bash
git clone https://github.com/your-repo/fullstack-project.git
cd fullstack-project
```

#### 2. Clone the Repository
```bash
cd Backend
./mvnw spring-boot:run
```
The backend will start on http://localhost:8080.

#### 3. Start the Frontend
```bash
cd ../Frontend/market
npm install
npm run dev
```

The frontend will start on http://localhost:5173.