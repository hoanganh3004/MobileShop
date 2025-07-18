Đây là một sản phẩm viết về web mua bán điện thoại (MobileShop)

Được làm bằng servlet-jsp 

Cần được giải đáp có thể liên hệ qua - gmail : Hoanganhhy3004@gmail.com
									 - Fb: https://www.facebook.com/hoang.anh.373679

*CHỨC NĂNG CHO KHÁCH HÀNG:
- Đăng nhập/ Đăng kí / Quên mật khẩu ( Yêu cầu mật khẩu mã hoá Bcrypt )
  
- Quản lý tài khoản (Cho phép KH có thể quản lý thông tin tài khoản của mình, cập nhật thông tin, đổi mật khẩu)
  
- Xem sản phẩm : + Hiển thị danh sách sản phẩm
                 + Có thể tìm kiếm sản phẩm theo mã, tên, khoảng giá..
                 + Có button cho phép xem chi tiết, thêm vào giỏ hàng.
  
- Giỏ hàng :  + Hiển thị các sản phẩm đã được thêm vào giỏ.
              + Có button cho phép khách hàng đặt hàng. (Lưu ý: cần KH đăng nhập mới được đặt hàng)
              + Phần đặt hàng thì sẽ có mail gửi về cho KH. Khi KH đặt hàng xong thì phải có thông báo cho admin và KH đặt hàng.
  
- Lịch sử :  + Cho phép KH có thể xem lại được lịch sử các đơn hàng của mình.
             + Tìm kiếm lịch sử theo tên sản phẩm, khoảng ngày đặt hàng, trạng thái đơn hàng.

- Thông báo : + Hiển thị danh sách các thông báo của người dùng. (Ví dụ: mỗi khi đặt hàng thành công, khi admin cập nhật trạng thái đơn hàng)


* CHỨC NĂNG CHO QUẢN TRỊ:
- Quản lý tài khoản KH : + Hiển thị danh sách tài khoản có trên hệ thống.
  						 + Cho phép phân quyền cho tài khoản, cập nhật thông tin, đổi mật khẩu, khóa tài khoản.

- Quản lý sản phẩm :  + Hiển thị danh sách sản phẩm (có phân trang)
					  +	Có thể tìm kiếm sản phẩm theo mã, tên, danh mục…
					  +	Thêm mới sản phẩm
					  +	Có button cho phép xem chi tiết.
					  +	Có button xóa sản phẩm.

- Quản lý danh mục sản phẩm : + Thêm, sửa xóa và tìm kiếm danh mục sản phẩm.
- Quản lý đơn hàng :  + Hiển thị danh sách các đơn hàng.
					  +	Có button tìm kiếm (tìm theo KH đặt, ngày đặt…)
					  +	Xem chi tiết đơn hàng.
					  +	Cập nhật trạng thái đơn hàng (với trường hợp hủy đơn hàng thì có input nhập lý do hủy)
					  +	Khi người quản trị cập nhật trạng trạng thái đơn hàng thì cần có thông báo cho user.
					  +	Có chức năng tạo đơn hàng.
- Thông báo : + Hiển thị danh sách các thông báo
			  +	Có chức năng tìm kiếm
			  +	Có chức năng tạo thông báo.

Trang chủ:                                    
<img width="1915" height="1011" alt="image" src="https://github.com/user-attachments/assets/79a3e5ff-b6a3-4051-8cdd-9dbbb0fce7a7" />

Login:
<img width="1919" height="1004" alt="image" src="https://github.com/user-attachments/assets/24030c87-2cc6-4146-988e-95c75023e9f7" />

trang chủ ( sau khi đã đăng nhập ):
<img width="1919" height="960" alt="image" src="https://github.com/user-attachments/assets/3e84a113-892f-44ce-adef-8b88d0486d25" />

chi tiết sản phẩm :
<img width="1919" height="1015" alt="image" src="https://github.com/user-attachments/assets/c5b95b0f-38c4-4cc3-9135-ec14474835f9" />

<img width="1916" height="961" alt="image" src="https://github.com/user-attachments/assets/941eb002-b39a-4a4a-ad5d-e8d952756500" />

sau khi bấm thêm giỏ hàng :
<img width="1919" height="1010" alt="image" src="https://github.com/user-attachments/assets/894cded5-da0d-4426-b0d3-ffc980d50f09" />

Lịch sử đặt hàng :
<img width="1895" height="1003" alt="image" src="https://github.com/user-attachments/assets/04a90cf4-57bd-4a22-9855-cd65c41c2015" />

thông báo :
<img width="1915" height="460" alt="image" src="https://github.com/user-attachments/assets/d9e58db8-cabc-46b8-87b8-8dfcf7f8b9d7" />

thông tin cá nhân :
<img width="1918" height="1022" alt="image" src="https://github.com/user-attachments/assets/2619c01b-1a2e-4709-b046-c2c0b310cbca" />

* ADMIN :
<img width="1919" height="1007" alt="image" src="https://github.com/user-attachments/assets/76cd6421-73b7-4232-8de9-08d0e561a3e2" />

<img width="1898" height="1015" alt="image" src="https://github.com/user-attachments/assets/9f21dd5b-82cf-40b1-bfa5-f2dd86ba75ee" />

<img width="1919" height="1013" alt="image" src="https://github.com/user-attachments/assets/155f5400-74b1-4c8d-9193-820423106024" />

<img width="1917" height="1007" alt="image" src="https://github.com/user-attachments/assets/1791b529-0480-4dbc-8e5d-4b293d4e7870" />

<img width="1917" height="1003" alt="image" src="https://github.com/user-attachments/assets/424f42ad-76bc-417d-afd6-1c93a2691a6d" />

<img width="1901" height="1014" alt="image" src="https://github.com/user-attachments/assets/914c6487-c494-4fcc-8d17-cd54835f214b" />

<img width="1910" height="1016" alt="image" src="https://github.com/user-attachments/assets/c863e46e-490e-40cf-9b45-198457ac2545" />

<img width="1919" height="1018" alt="image" src="https://github.com/user-attachments/assets/ca735d4e-25ed-489c-a6c6-632d5e8a294b" />

<img width="1141" height="656" alt="image" src="https://github.com/user-attachments/assets/ca779951-177c-4b6e-a70e-0d51f31067df" />
