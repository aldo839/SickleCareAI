# SickleCareAI

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%2Boot-4.0-brightgreen)
![PostgreSQL](https://img.shields.io/badge/Postgresql)
![Licence](https://img.shields.io/badge/licence-MIT-blue)

> Application of heath tracking for sickle cell person in Cameroon.
> This can be useful for sickle cell person of others country in the word, 
> we are focus on Cameroon because we want to 
> the performance of the app and optimize it for starting with others country.

## Description
**Sickle cell disease** is the most common hereditary disease in the world, with a 
very high prevalence in sub-Saharan Africa.

After meeting with patients, doctors and researchers, a specific problem emerged 
for each category.
- **Patients** face a lack of real-time follow-up, with crises that can occur at 
unexpected moments.
- **Doctors** face challenges in the continuous monitoring of a patient and also 
the impact of limited research.
- **Researchers** face a lack of long-term clinical data from patients.

>How can we solve these distinct problems in a joint manner ?

This is where we propose SickleCareAI, a follow-up application for sickle cell
patients that includes both patients and their doctors, based on data security.

This anonymized data can then be used by medical researchers and also help train
artificial intelligence models for the prediction of the vaso-occlusive crises

## Demo

## Features

### Patient
- Create an account
- Activate account with activation code
- Authenticate
- See all doctor
- Choose a doctor after admin validation

### Doctor
- Create an account
- Activate account with activation code
- Authenticate
- See his patients after admin validation

### Administrator
- Activate account with activation code 
- Authenticate
- Manage patients and doctors accounts
- Manage user statistics

### Root
- Authenticate
- Create admin account
- Manage admin account
- Execute all tasks that administrator can do

## Technical Stack

| Category             | Technology            |
|----------------------|-----------------------|
| Programming language | Java 21               |
| Framework            | Spring Boot 4         |
| Security             | Spring Security + JWT |
| Persistance          | Spring Data JPA       |
| Database             | PostgreSQL            |
| Test                 | JUnit 5 + JaCoCo      |
| API Documentation    | OpenAPI / Swagger |
| Build                | Maven                 |

## Installation Guide

### Project Clone
```bash
git clone https://github.com/aldo839/sicklecareai.git
cd sicklecareai
```

### Configuration of the database
```
spring.datasource.url=jdbc:postgresql://localhost:5432/sicklecareai
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password

jwt.secret=strong_and_long_secret_for_jwt
```

### Application running
```bash
mvn spring-boot:run
```

## Main Endpoint

### Patient
| Method | Route              | Description                                                      | Auth require                                    |
|--------|--------------------|------------------------------------------------------------------|-------------------------------------------------|
| POST   | /api/patients/register-patient | Patient registration route                                       | Not require                          |
| GET    | /api/patients/get-all | Route to fetch all patients                                      | DOCTOR, ADMIN, ROOT                             |
| GET    | /api/patients/{id} | Route to get information's on patient account                    | PATIENT (only his account), DOCTOR, ADMIN, ROOT |
| PUT    | /api/patients/{id} | Route to update patient account                                  | PATIENT (only his account)                      |
| PUT | /api/patients/validate-patient/{id} | Route to validate patient account after information verification | ADMIN, ROOT                        |
| POST   | /api/patients/select-doctor/{id} | Route to select doctor using doctorID                            | PATIENT                            |
| DELETE | /api/patients/{id} | Route to delete a patient account                                | ROOT, ADMIN                                     |
| GET | /api/patients/patients-by-doctor/{id}/ | Route to fetch all patients for a specific doctor | DOCTOR (only is patients), ROOT, ADMIN          |

### Doctor
| Method | Route                               | Description                                                     | Auth require                           |
|--------|-------------------------------------|-----------------------------------------------------------------|----------------------------------------|
| POST   | /api/doctors/register-doctor        | Doctor registration route                                       | Not require                            |
| GET    | /api/doctors/get-all                | Route to fetch all doctors                                      | PATIENT, ADMIN, ROOT                   |
| GET    | /api/doctors/{id}                   | Route to get information's on doctor account                    | DOCTOR (only his account), ADMIN, ROOT |
| PUT    | /api/doctors/{id}                   | Route to update doctor account                                  | PATIENT (only his account)             |
| PUT | /api/doctors/validate-doctor/{id} | Route to validate doctor account after information verification | ADMIN, ROOT                            |
| DELETE | /api/doctors/{id}                  | Route to delete a doctor account                                | ROOT, ADMIN                            |

### Admin
| Method | Route                     | Description                                 | Auth require                   |
|--------|---------------------------|---------------------------------------------|--------------------------------|
| POST   | /api/admin/register-admin | Admin registration route                    | ROOT                           |
| GET    | /api/admin/get-all        | Route to fetch all admins                   | ROOT                           |
| GET    | /api/admin/{id}           | Route to get information's on admin account | ADMIN (only his account), ROOT |
| PUT    | /api/admin/{id}           | Route to update admin account               | ADMIN (only his account)       |
| DELETE | /api/admin/{id}           | Route to delete a admin account             | ROOT                   |
