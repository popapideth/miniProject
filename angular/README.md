# Angular Project

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.2.11.

## Prerequisites

### Node.js Version Management

ตรวจสอบเวอร์ชัน Node.js ที่ติดตั้งอยู่:

```bash
nvm ls
```

เปลี่ยนไปใช้เวอร์ชันที่ต้องการ:

```bash
nvm use <version-number>
nvm use 22.13.1
```

## Installation

### 1. Install Dependencies

```bash
npm install
```

### 2. Install Bootstrap & Font Awesome

```bash
npm install bootstrap
npm install font-awesome
```

### 3. Configure Angular.json

เพิ่ม CSS และ JavaScript files ใน `angular.json` (หลังบรรทัดที่ 26):

```json
"styles": [
  "src/styles.css",
  "node_modules/bootstrap/dist/css/bootstrap.min.css",
  "node_modules/font-awesome/css/font-awesome.css"
],
"scripts": [
  "node_modules/@popperjs/core/dist/umd/popper.min.js",
  "node_modules/bootstrap/dist/js/bootstrap.min.js"
]
```

## Development Server

เริ่มต้น development server:

```bash
npm start
```

หรือ

```bash
ng serve
```

เปิดเบราว์เซอร์และไปที่ `http://localhost:4200/` แอปพลิเคชันจะรีโหลดอัตโนมัติเมื่อมีการแก้ไขไฟล์

## Code Scaffolding

Angular CLI มีเครื่องมือสร้างโค้ดที่ทรงพลัง สร้าง component ใหม่:

สร้าง Component

```bash
ng g component component-name
ng g c component-name
```

Service คืออะไร?
->Service เป็นคลาสที่ใช้สำหรับ:

จัดการข้อมูล และการเชื่อมต่อ API,
แชร์ข้อมูล ระหว่าง Components,
Business Logic ที่ไม่เกี่ยวกับ UI,
Dependency Injection สำหรับการแชร์ฟังก์ชัน

## Angular CLI Generate Commands

สร้าง Component

```bash
ng g component component-name
ng g c component-name
```

สร้าง Interface ใช้ทำงานแทนข้อมูลเหมือน model

```bash
ng g interface interface-name
```

or
สร้าง Models

```bash
ng g interface models/user
ng g interface models/product
```

สร้าง Service

```bash
ng g service service-name
ng g s service-name
```

สร้าง Module

```bash
ng g module module-name
ng g m module-name
```

สร้าง Directive

```bash
ng g directive directive-name
ng g d directive-name
```

สร้าง Pipe

```bash
ng g pipe pipe-name
ng g p pipe-name
```

ดูรายการ schematics ทั้งหมด (เช่น `components`, `directives`, `pipes`):

```bash
ng generate --help
```

## Building

สร้าง build สำหรับ production:

```bash
ng build
```

Build artifacts จะถูกเก็บไว้ในโฟลเดอร์ `dist/`

## Running Tests

### Unit Tests

```bash
ng test
```

### End-to-End Tests

```bash
ng e2e
```

## Notes

- หลังจากติดตั้ง Bootstrap และ Font Awesome แล้ว ให้รีสตาร์ท development server
- ตรวจสอบให้แน่ใจว่าเส้นทางในไฟล์ `angular.json` ถูกต้อง
- Font Awesome v4.7.0 ใช้ class prefix `fa` (เช่น `fa fa-home`)
