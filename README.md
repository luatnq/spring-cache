# Các Chiến Lược Cache

## 1. Cache Aside

- **Cách hoạt động:** Cache Aside là chiến lược mà ứng dụng hoặc developer tự quyết định khi nào để load hoặc write dữ liệu vào cache. Khi một yêu cầu truy cập dữ liệu được thực hiện, hệ thống sẽ kiểm tra cache trước; nếu không tìm thấy dữ liệu trong cache, dữ liệu sẽ được đọc từ nguồn dữ liệu chính, sau đó được thêm vào cache để sử dụng lần sau.

- **Nhược điểm:**
  - Phức tạp trong quản lý: Cần phải tự xử lý việc load và evict dữ liệu từ cache.
  - Có thể gặp lỗi nhất quán dữ liệu: Nếu dữ liệu được cập nhật trong database mà không cập nhật đồng thời trong cache.
  - Với trường hợp cache miss thì thời gian xử lí sẽ bị lâu, ảnh hưởng tới trải nghiệm người dùng cho tới khi hết cache miss. Từ đây, sẽ phát sinh thêm vấn đề Cache stampede.

- **Cách khắc phục:**
  - Tự động hóa quá trình evict và invalidate dữ liệu trong cache thông qua các hàm hoặc middleware.
  - Sử dụng transaction hoặc lock để đảm bảo tính nhất quán khi cập nhật dữ liệu.

## 2. Read Through Caching

- **Cách hoạt động:** Read Through Caching là chiến lược mà cache tự động xử lý việc đọc dữ liệu từ nguồn chính và lưu trữ dữ liệu đó khi dữ liệu không có trong cache. Khi một request yêu cầu dữ liệu, cache sẽ kiểm tra xem dữ liệu có tồn tại hay không; nếu không, cache sẽ đọc dữ liệu từ nguồn dữ liệu chính, lưu trữ nó và sau đó trả lại cho người dùng.

- **Nhược điểm:**
  - Độ trễ khi truy cập lần đầu: Dữ liệu cần được load vào cache từ database, gây ra độ trễ.
  - Cần hệ thống cache hỗ trợ: Không phải mọi hệ thống cache đều có khả năng tự động cập nhật dữ liệu từ database.

- **Cách khắc phục:**
  - Sử dụng pre-loading hoặc warm-up cache trước khi ứng dụng đi vào hoạt động.
  - Chọn lựa công nghệ cache phù hợp hỗ trợ tính năng read through.

## 3. Write Through Caching

- **Cách hoạt động:** Write Through Caching là chiến lược đảm bảo rằng mỗi lần dữ liệu được cập nhật hoặc thêm mới, nó sẽ được viết vào cả cache và nguồn dữ liệu chính ngay lập tức. Điều này giúp đảm bảo tính nhất quán giữa cache và nguồn dữ liệu chính.

- **Nhược điểm:**
  - Độ trễ khi ghi: Việc ghi đồng thời vào cả cache và database có thể làm chậm quá trình.
  - Tốn tài nguyên khi ghi: Ghi vào cả hai hệ thống có thể tốn nhiều tài nguyên hơn.

- **Cách khắc phục:**
  - Sử dụng hàng đợi hoặc buffer để tối ưu hóa việc ghi dữ liệu.
  - Đánh giá và điều chỉnh kích thước cache phù hợp để giảm overhead.

## 4. Write Behind Caching

- **Cách hoạt động:** Write Behind Caching là chiến lược mà dữ liệu mới hoặc dữ liệu đã được cập nhật sẽ được viết vào cache trước và sau đó được đồng bộ hóa với nguồn dữ liệu chính sau một khoảng thời gian nhất định hoặc dựa trên một điều kiện nhất định. Điều này giúp tăng tốc độ ghi dữ liệu vì việc ghi vào cache thường nhanh hơn nhiều so với ghi vào nguồn dữ liệu chính.

- **Nhược điểm:**
  - Rủi ro mất dữ liệu: Nếu cache gặp sự cố trước khi đồng bộ, dữ liệu có thể bị mất.
  - Dữ liệu không nhất quán: Có thể xảy ra trễ trong việc đồng bộ dữ liệu với database.

- **Cách khắc phục:**
  - Sử dụng cơ chế bảo vệ dữ liệu như RAID cho cache.
  - Thiết lập kỳ vọng đồng bộ hóa định kỳ để giảm rủi ro mất dữ liệu.

## Tóm tắt

- **Write through cache:** Lưu dữ liệu từ cache xuống database một cách đồng bộ.
- **Write back:** Lưu dữ liệu từ cache xuống database bất đồng bộ.
