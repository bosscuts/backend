$(document).ready(function () {
    $('#staff_select2').select2();

    $(document).on('click', '#invoice_cash_btn', function () {
        const staffId = $("#staff_select2").val();
        const cashAmount = $('#cash_amount').val();
        const cashDescription = $('#cash_description').val();
        if (cashAmount) {
            $.ajax({
                type: 'post',
                data: JSON.stringify({
                    'staffId': staffId,
                    'cashAmount': cashAmount,
                    'cashDescription': cashDescription
                }),
                contentType: 'application/json; charset=utf-8',
                url: "/users/cash-advance",
                success: function (data, textStatus, xhr) {
                    if (xhr.status === 200) {
                        swal('Tạo hóa đơn thành công.', "", "success");
                    } else {
                        swal('Bạn chưa nhập số tiền!', "", "error");
                    }
                }
            });
        } else {
            swal('Bạn chưa nhập số tiền!', "", "error");
        }

    });
});