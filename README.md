# 🏸 Badminton Shop - Project Overview

Dự án xây dựng hệ thống quản lý và kinh doanh dụng cụ cầu lông, tập trung vào trải nghiệm mua sắm tối ưu và quản lý kho hàng chặt chẽ.

## 📌 Giới thiệu dự án
Hệ thống hỗ trợ quản lý toàn diện từ danh mục sản phẩm, thương hiệu cho đến quy trình xử lý đơn hàng của người dùng. Dự án được thiết kế với cấu trúc database chuẩn hóa để đảm bảo hiệu năng và tính toàn vẹn dữ liệu.

### Tính năng chính:
- **Quản lý sản phẩm:** Phân loại theo thương hiệu (Yonex, Lining...) và danh mục (Vợt, Giày...).
- **Hệ thống người dùng:** Phân quyền Admin/User, lưu trữ thông tin giao hàng.
- **Quy trình đơn hàng:** Theo dõi trạng thái đơn hàng (Pending, Processing, Completed, Cancelled).
- **Quản lý tồn kho:** Cập nhật số lượng sản phẩm tự động.

---

## 🗄️ Thiết kế Cơ sở dữ liệu (Database Schema)

Dưới đây là cấu trúc các bảng được thiết kế để vận hành hệ thống:

```sql
-- 1. Bảng thương hiệu
CREATE TABLE brands (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    logo_url NVARCHAR(255)
);

-- 2. Bảng danh mục sản phẩm
CREATE TABLE categories (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE
);

-- 3. Bảng sản phẩm
CREATE TABLE products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NTEXT,
    price DECIMAL(18, 2) NOT NULL,
    image_url NVARCHAR(255),
    stock INT NOT NULL DEFAULT 0,
    category_id INT,
    brand_id INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (brand_id) REFERENCES brands(id)
);

-- 4. Bảng người dùng
CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL, -- Sẽ được mã hóa
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name NVARCHAR(100),
    address NVARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'USER', -- Ví dụ: USER, ADMIN
    created_at DATETIME2 DEFAULT GETDATE()
);

-- 5. Bảng đơn hàng
CREATE TABLE orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT,
    order_date DATETIME2 DEFAULT GETDATE(),
    status NVARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, PROCESSING, COMPLETED, CANCELLED
    total_amount DECIMAL(18, 2) NOT NULL,
    shipping_address NVARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 6. Bảng chi tiết đơn hàng
CREATE TABLE order_details (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(18, 2) NOT NULL, -- Giá tại thời điểm mua
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Dữ liệu mẫu khởi tạo
INSERT INTO brands (name) VALUES ('Yonex'), ('Lining'), ('Victor'), ('Mizuno');
INSERT INTO categories (name) VALUES (N'
