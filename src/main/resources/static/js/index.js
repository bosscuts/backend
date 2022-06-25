let staffService = new Map();
$(document).ready(function () {
    $(document).on('click', '.staff', function () {
        let staff = $(this);
        const staffId = staff.attr('staffId');
        const serviceId = staff.attr('serviceId');
        staffService.set(serviceId, staffId);

        console.log('staffService ===> :' + JSON.stringify(Object.fromEntries(staffService)));
        staff.parent().parent().siblings().removeClass('staff-active');
        staff.toggleClass('staff-active');
    });

    $(document).on('click', '#confirm', function () {
        $(this).toggleClass('hidden_btn');
        $('#create_invoice_btn').removeClass('hidden_btn');
        $('#customer_phone').removeClass('hidden_input');
    });
    $(document).on('click', '#create_invoice_btn', function () {
        // const token = $("meta[name='_csrf']").attr("content");
        // const header = $("meta[name='_csrf_header']").attr("content");

        const totalAmount = $('#total_amount').attr('data-amount');
        const customerPhone = $('#customer_phone').val();
        let staffServiceStr = '';
        staffService.forEach((value, key) => {
            staffServiceStr += key + '-' + value + ',';
        })
        if (staffServiceStr) {
            if (customerPhone) {
                $.ajax({
                    type: 'post',
                    data: JSON.stringify({
                        'strServiceStaff': staffServiceStr,
                        'customerPhone': customerPhone,
                        'totalAmountPayment': totalAmount
                    }),
                    contentType: 'application/json; charset=utf-8',
                    url: "/invoice",
                    success: function (data, textStatus, xhr) {
                        staffService.clear();
                        if (xhr.status === 200) {
                            swal('Tạo hóa đơn thành công.', "", "success");
                        } else {
                            swal('Tạo hóa đơn thất bại!', "", "error");
                            // $.toaster({ priority : 'danger', message : 'Tạo hóa đơn thất bại!' });
                        }
                    }
                });
            } else {
                swal('Bạn chưa nhập thông tin khách hàng!', "", "error");
            }
        } else {
            swal('Bạn chưa chọn dịch vụ hoặc sản phẩm nào!', "", "error");
        }
    });
});