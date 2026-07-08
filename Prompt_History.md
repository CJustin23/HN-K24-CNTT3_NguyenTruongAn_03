# Prompt History — IT212 Final Test

**Dự án:** E-Learning Cart & Voucher  
**Thư mục:** `d:\IT212_FinalTest`  
**Ngày tổng hợp:** 08/07/2026

Tài liệu này ghi lại toàn bộ các Prompt đã sử dụng trong quá trình phát triển dự án, phục vụ quy định nộp bài.

---

## Prompt 1 — Phân tích Entity & Tạo SRS

> Hãy đọc và phân tích các Entity hiện có trong thư mục Base Code. Sau đó, tạo một file SRS.md và lưu ở thư mục gốc của dự án. Trong file SRS.md, hãy trình bày:
>
> **Thiết kế Cấu trúc dữ liệu:** Đề xuất các Entity mới cho 'Giỏ hàng' (Cart, CartItem) và 'Mã giảm giá' (Voucher). Chỉ rõ các thuộc tính cần thiết của Voucher để đáp ứng đủ yêu cầu: phần trăm giảm (vd: 20%), mức giảm tối đa (vd: 500.000 VNĐ), và số lượng khóa học tối thiểu để áp dụng (vd: 2).
>
> **Đặc tả Thuật toán Tính tiền (Pricing Logic):** Viết pseudo-code hoặc luồng logic thể hiện rõ các bước tính toán, từ kiểm tra số lượng khóa học tối thiểu, tính số tiền giảm theo %, chặn trần mức giảm tối đa, cho đến khi ra được số tiền cuối cùng phải trả.

**Kết quả:** Tạo file `SRS.md` tại thư mục gốc dự án.

---

## Prompt 2 — Entity, Repository & DTO

> Dựa vào bản thiết kế dữ liệu trong file SRS.md vừa tạo, hãy viết code Java cho các Entity (sử dụng chuẩn JPA Annotations: @Entity, @Table, @Id, @OneToMany, @ManyToOne...), tạo các interface Repository (kế thừa JpaRepository) và các lớp DTO tương ứng cho Cart, CartItem và Voucher.

**Kết quả:**
- Entity: `Cart`, `CartItem`, `Voucher` (+ bổ sung `price` cho `Course`)
- Repository: `CartRepository`, `CartItemRepository`, `VoucherRepository`
- DTO: `CartDTO`, `CartItemDTO`, `VoucherDTO`, `AddCartItemRequest`, `ApplyVoucherRequest`

---

## Prompt 3 — Tầng Service & Controller

> Tiếp tục xây dựng tầng Service và Controller:
>
> **Tầng Service:** Viết hàm thực hiện chính xác thuật toán tính tiền giỏ hàng có áp dụng mã giảm giá. Đảm bảo xử lý đúng logic kiểm tra điều kiện (tối thiểu 2 khóa học), tính phần trăm giảm và chặn mức giảm tối đa không quá 500.000 VNĐ.
>
> **Tầng Controller:** Cung cấp API tính tiền cho giỏ hàng. API này trả về định dạng JSON rõ ràng gồm 3 trường: `totalOriginalAmount` (tổng tiền ban đầu), `discountAmount` (số tiền được giảm), và `finalAmount` (số tiền cuối cùng phải trả).

**Kết quả:**
- `CartService` — thuật toán tính tiền theo SRS
- `CartController` — API `GET /api/v1/cart/pricing`
- `CartPricingResponse` — DTO response 3 trường

---

## Prompt 4 — BusinessException & GlobalExceptionHandler

> Hãy kiểm tra xem trong hệ thống Base Code đã có lớp BusinessException và GlobalExceptionHandler (sử dụng @RestControllerAdvice) chưa, nếu chưa hãy tạo mới. Cập nhật lại logic để:
>
> **Trong Service:** Ném ra BusinessException kèm thông báo lỗi phù hợp khi người dùng nhập mã voucher sai, không tồn tại hoặc vi phạm điều kiện (VD: 'Phải mua ít nhất 2 khóa học để áp dụng mã này').
>
> **Trong Exception Handler:** Bắt các ngoại lệ này để trả về HTTP Status 400 Bad Request và chuỗi JSON thông báo lỗi chuẩn mực, đảm bảo không bị văng Stack-trace trên console.

**Kết quả:**
- Xác nhận `BusinessException` và `GlobalExceptionHandler` đã có sẵn trong Base Code
- Cập nhật `CartService` — ném `BusinessException(400, ...)` khi voucher không hợp lệ
- Cập nhật `GlobalExceptionHandler` — log message, không in stack-trace
- Bổ sung API `POST /api/v1/cart/voucher` và tham số `voucherCode` cho API pricing

---

## Prompt 5 — Tiếp tục hoàn thiện

> làm tiếp

**Kết quả:**
- Hoàn thiện logic xử lý ngoại lệ
- Thêm unit test `CartServiceTest` (5 test cases)
- Xác minh build và test PASS

---

## Prompt 6 — Tổng hợp Prompt History

> Hãy tạo file Prompt_History.md tại thư mục gốc của dự án. Tổng hợp và ghi lại toàn bộ nội dung các Prompt mà tôi đã sử dụng từ đầu đến giờ vào file này để tôi hoàn thiện quy định nộp bài.

**Kết quả:** Tạo file `Prompt_History.md` (file này).

---

## Tổng kết tiến độ dự án

| STT | Giai đoạn | Trạng thái |
|-----|-----------|------------|
| 1 | Phân tích & thiết kế (SRS.md) | ✅ Hoàn thành |
| 2 | Entity, Repository, DTO | ✅ Hoàn thành |
| 3 | Service & Controller (Pricing API) | ✅ Hoàn thành |
| 4 | Exception Handling | ✅ Hoàn thành |
| 5 | Unit Test | ✅ Hoàn thành |
| 6 | Prompt History | ✅ Hoàn thành |
