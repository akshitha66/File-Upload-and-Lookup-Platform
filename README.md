# 📁 File Upload and Lookup Platform

A full-stack application that allows users to upload files, store them in AWS S3, and retrieve metadata. The app is built using **React (frontend)** and **Spring Boot (backend)**, with AWS services for secure, scalable storage and lookup.

---

## 🛠 Tech Stack

### 🌐 Frontend
- React.js
- Axios (for API calls)
- Bootstrap / Tailwind CSS (optional for styling)

### 🔧 Backend
- Spring Boot
- AWS SDK for Java (S3, DynamoDB)
- REST API
- Java 17+

### ☁️ Cloud Services
- Amazon S3 (File Storage)
- DynamoDB (Metadata Storage)
- IAM (Access Control)
- EC2 (Backend deployment)
- CloudFront + S3 (Frontend deployment)
- API Gateway (Optional in extended version)
- Terraform (Infrastructure as Code - optional)

---

## 🧰 Features

- 🔐 Secure file upload using **pre-signed URLs**
- 🗃 Store file metadata (filename, size, timestamp) in **DynamoDB**
- 🔍 Search / lookup uploaded file details
- 📤 Upload files directly from frontend to AWS S3
- 🛡 IAM Roles for secure AWS access
- 🚀 Ready for cloud deployment (free-tier AWS)

---

## 📁 Project Structure

file-upload-platform/
│
├── backend/ # Spring Boot project
│ └── src/
│ └── main/java/com/example/upload
│ ├── controller/
│ ├── service/
│ └── model/
│
├── frontend/ # React project
│ └── src/
│ ├── components/
│ └── App.js
│
├── README.md
└── .gitignore

yaml
Copy
Edit


---

## 🚀 How to Run Locally

### 🖥 Backend (Spring Boot)

1. Navigate to backend folder:
   ```bash
   cd backend

Frontend (React)
Navigate to frontend folder:

bash
Copy
Edit
cd frontend
Install dependencies:

bash
Copy
Edit
npm install
Start development server:

bash
Copy
Edit
npm start
📦 Future Enhancements
✅ File preview support (image/pdf)

📧 Email notifications via AWS SES

📜 API Gateway + Lambda notifications

🧪 Unit and Integration Tests

📊 Admin dashboard with analytics

🙋‍♀️ Author
Akshitha Chimbili
Full Stack Developer | AWS Enthusiast
