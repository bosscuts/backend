let staffService = new Map();
let staffServiceStr = '';
$(document).ready(function () {
    $('#tab_link_service').click();
    $(document).on('click', '#reset_btn', function () {
        location.reload();
    });

    $(document).on('click', '.staff', function () {
        let staff = $(this);
        const staffId = staff.attr('staffId');
        const serviceId = staff.attr('serviceId');
        staffService.set(serviceId, staffId);
        staff.parent().parent().siblings().removeClass('staff-active');
        staff.toggleClass('staff-active');

        staff.children('.card').toggleClass('border-div');

    });

    $(document).on('click', '#confirm', function () {
        $(this).toggleClass('hidden_btn');
        $('#preview_invoice').removeClass('hidden_btn');
        $('#info_customer').removeClass('hidden_div');
    });

    $(document).on('click', '#preview_invoice', function () {
        const totalAmount = $('.total_amount').attr('data-amount');
        const customerPhone = $('#customer_phone').val();
        const customerAddress = $('#customer_address').val();
        const customerFirstName = $('#customer_first_name').val();
        const customerLastName = $('#customer_last_name').val();
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
                        'customerAddress': customerAddress,
                        'customerFirstName': customerFirstName,
                        'customerLastName': customerLastName,
                        'totalAmountPayment': totalAmount
                    }),
                    contentType: 'application/json; charset=utf-8',
                    url: "/invoice/preview",
                    success: function (data, textStatus, xhr) {
                        staffService.clear();
                        if (xhr.status === 200) {
                            $('#invoice_detail').removeClass('hidden_div');
                            $('#create_invoice_btn').removeClass('hidden_btn');
                            $('#preview_invoice').addClass('hidden_btn');
                            let userServiceHtml = '';
                            jQuery.each(data.servicePreviews, function (i, val) {
                                let serviceName = val.serviceProductName;
                                let servicePrice = val.totalPrice;
                                userServiceHtml += `<tr>
                                        <td>
                                            <i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;${serviceName}
                                        </td>
                                        <td>${servicePrice}</td>
                                    </tr>`
                            });
                            let tBody = `<tbody>
                                <tr>
                                    <td><i class='fa fa-refresh' aria-hidden='true'></i>&nbsp;MỘT LẦN</td>
                                    <td></td>
                                </tr>
                                ${userServiceHtml}
                                <tr class='rzvy_subtotal_exit'>
                                    <th>
                                        <i class='fa fa-tags' aria-hidden='true'></i>
                                        &nbsp;Tổng tiền thanh toán:
                                    </th>
                                    <th class="total_amount">${data.totalAmount}</th>
                                </tr>
                            </tbody>`
                            $("#user-service").html(tBody);
                            console.log('staffServiceStr => ' + staffServiceStr);
                        } else {
                            console.log(data);
                            console.log(textStatus);
                            console.log(xhr);
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

    $(document).on('click', '#create_invoice_btn', function () {
        // const token = $("meta[name='_csrf']").attr("content");
        // const header = $("meta[name='_csrf_header']").attr("content");

        const totalAmount = $('.total_amount').attr('data-amount');
        const customerPhone = $('#customer_phone').val();
        const customerAddress = $('#customer_address').val();
        const customerFirstName = $('#customer_first_name').val();
        const customerLastName = $('#customer_last_name').val();
        if (staffServiceStr) {
            if (customerPhone) {
                $.ajax({
                    type: 'post',
                    data: JSON.stringify({
                        'strServiceStaff': staffServiceStr,
                        'customerPhone': customerPhone,
                        'customerAddress': customerAddress,
                        'customerFirstName': customerFirstName,
                        'customerLastName': customerLastName,
                        'totalAmountPayment': totalAmount
                    }),
                    contentType: 'application/json; charset=utf-8',
                    url: "/invoice",
                    success: function (data, textStatus, xhr) {
                        staffService.clear();
                        if (xhr.status === 200) {
                            swal('Tạo hóa đơn thành công.', "", "success");
                            staffServiceStr = '';
                        } else {
                            swal('Tạo hóa đơn thất bại!', "", "error");
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