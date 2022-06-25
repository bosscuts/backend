$(document).ready(function () {
    $(document).on('click', '#customer_register_btn', function () {
        const customerFirstname = $('#customer_firstname').val();
        const customerLastname = $('#customer_lastname').val();
        const customerEmail = $('#customer_email').val();
        const customerPhone = $('#customer_phone').val();
        const customerAddress = $('#customer_address').val();

        if (customerPhone) {
            $.ajax({
                type: 'post',
                data: JSON.stringify({
                    'firstName': customerFirstname,
                    'lastName': customerLastname,
                    'email': customerEmail,
                    'phone': customerPhone,
                    'address': customerAddress
                }),
                contentType: 'application/json; charset=utf-8',
                url: "/customer",
                success: function (data, textStatus, xhr) {
                    if (xhr.status === 200) {
                        swal('Tạo khách hàng thành công.', "", "success");
                    } else {
                        swal('Tạo khách hàng thất bại!', "", "error");
                    }
                }
            });
        } else {
            swal('Bạn chưa nhập số điện thoại khách hàng!', "", "error");
        }
    });
});