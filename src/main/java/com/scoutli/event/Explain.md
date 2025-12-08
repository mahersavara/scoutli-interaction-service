


Event là POJO (Plain Old Java Object)

Phải có **Constructor mặc định** (Jackson cần để deserialize)
- Fields public để Jackson dễ dàng serialize/deserialize


1. Tại sao cần constructor mặc định?
   - *Trả lời: Jackson deserializer cần nó để tạo object từ JSON*


2. Event này chứa thông tin gì?
   - *Trả lời: Tất cả info cần thiết để Consumer xử lý mà không cần query DB*

**Checkpoint:** File compile được và có constructor mặc định? Pass Step 4! ✅