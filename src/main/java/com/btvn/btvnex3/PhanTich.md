1. Vai trò của HTTP Status Codes trong Robust API
   HTTP Status Codes đóng vai trò như một ngôn ngữ chung toàn cầu giữa Client và Server. Thay vì Client phải đọc nội dung (Body) để đoán xem request có chạy đúng không, họ chỉ cần nhìn vào mã trạng thái ở Header:

2xx: Thành công (Success).

4xx: Lỗi từ phía Client (Ví dụ: Sai URL, sai dữ liệu, sai định danh).

5xx: Lỗi từ phía Server (Lỗi logic code, sập DB).

2. Tác hại của việc trả về null hoặc {} thay vì 404 Not Found
   Khi Server trả về null hoặc chuỗi rỗng {} nhưng đi kèm HTTP Status 200 OK, Client sẽ bị đánh lừa:

Xử lý phức tạp: Code ở ứng dụng Client (như máy quét mã vạch) luôn phải có thêm một bước kiểm tra: if (data == null || data.id == null). Nếu lập trình viên frontend quên kiểm tra, ứng dụng sẽ lập tức bị crash lỗi NullPointerException hoặc hiển thị thông tin trống rỗng lên màn hình người dùng.

Mơ hồ về ngữ nghĩa: 200 OK có nghĩa là "Yêu cầu thành công và có dữ liệu". Trả về 200 OK cho một mặt hàng không tồn tại là sai lệch hoàn toàn về bản chất giao thức HTTP. Với 404 Not Found, các thư viện gọi API phía Client (như Axios, Retrofit) sẽ tự động kích hoạt block catch/error, giúp việc gom lỗi và hiển thị thông báo "Mặt hàng không tồn tại" trở nên tập trung và gọn gàng hơn.

3. Tại sao Jackson Dataformat XML là cần thiết cho Content Negotiation?
   Mặc định, Spring Boot sử dụng thư viện Jackson và được cấu hình tối ưu để chuyển đổi Object Java sang chuỗi JSON (application/json).

Khi Client gửi request kèm Header Accept: application/xml (yêu cầu dữ liệu trả về là XML), Spring Boot sẽ kiểm tra xem trong hệ thống có bộ chuyển đổi (HttpMediaTypeNotAcceptableException) nào xử lý được XML hay không. Nếu không có dependency jackson-dataformat-xml, Spring Boot sẽ không biết cách "dịch" Object Java sang cấu trúc thẻ XML và sẽ trả về lỗi 464 Not Acceptable. Thư viện này cung cấp các cơ chế định dạng (Serializer/Deserializer) dành riêng cho cấu trúc XML.