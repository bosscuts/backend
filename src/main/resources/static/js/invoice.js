let staffIds = [];
let htmlResult = '';
$(document).ready(function () {
    $(document).on('change', '#request_type', function () {
        const requestType = $('#request_type').val();
        if (requestType === 'PAY_FINES') {
            $('#from_date_sec').addClass('hidden_input');
            $('#to_date_sec').addClass('hidden_input');
            $('#cash_amount_sec').removeClass('hidden_input');
        } else if (requestType === 'CASH') {
            $('#from_date_sec').addClass('hidden_input');
            $('#to_date_sec').addClass('hidden_input');
            $('#cash_amount_sec').removeClass('hidden_input');
        } else if (requestType === 'HOLIDAY') {
            $('#cash_amount_sec').addClass('hidden_input');
            $('#from_date_sec').removeClass('hidden_input');
            $('#to_date_sec').removeClass('hidden_input');
        }
    });

    $(document).on('click', '#invoice_export_btn', function () {
        let userId = $('#staff_input').val();
        let invoiceAmount = $('#invoice_amount').val();
        let invoiceDescription = $('#invoice_description').val();

        $.ajax({
            url: "/invoice/internal",
            type: 'post',
            data: JSON.stringify({
                'userId': userId,
                'amount': invoiceAmount,
                'description': invoiceDescription,
            }),
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                swal('Tạo hóa đơn thành công.', "", "success");
            }
        });
    });

    $(document).on('click', '.staff', function () {
        let staff = $(this);
        staff.children('.card').toggleClass('border');
        staff.children('.card').toggleClass('border-primary');
    });

    $(document).on('click', '#in-day', function () {
        $('#result').html('');
        htmlResult = '';
        console.log(staffIds.toString())
        console.log('staffIds.length ' + staffIds.toString())
        const staffIdsStr = staffIds.toString();
        if (staffIdsStr) {
            $.ajax({
                url: "/invoice/" + staffIds,
                type: "get",
                success: function (result) {
                    result.forEach(concatHtmlResult);
                    $('#result').html(htmlResult);
                }
            });
        }
    });

    $(document).on('click', '#in-month', function () {
        $('#result').html('');
        htmlResult = '';
        console.log(staffIds.toString())
        console.log('staffIds.length ' + staffIds.toString())
        const staffIdsStr = staffIds.toString();
        if (staffIdsStr) {
            $.ajax({
                url: "/invoice/" + staffIds + '?isMonth=true',
                type: "get",
                success: function (result) {
                    result.forEach(concatHtmlResult);
                    $('#result').html(htmlResult);
                }
            });
        }

    });

    $(document).on('click', '.staff', function () {
        $('#result').html('');
        htmlResult = '';
        let staff = $(this);
        const staffId = staff.attr('staffId');
        const index = staffIds.indexOf(staffId);
        if (index !== -1) {
            staffIds.splice(index, 1);
        } else {
            staffIds.push(staffId);
        }
        console.log(staffIds.toString());
        console.log('staffIds.length ' + staffIds.toString());
        const staffIdsStr = staffIds.toString();
        if (staffIdsStr) {
            $.ajax({
                url: "/invoice/" + staffIds,
                type: "get",
                success: function (result) {
                    result.forEach(concatHtmlResult);
                    $('#result').html(htmlResult);
                }
            });
        }
    });
});

function concatHtmlResult(data, index, array) {
    const productServiceName = data.productServiceName
    const totalAmountPayment = data.totalAmountPayment
    const staffName = data.staffName
    const price = data.price
    const quantity = data.quantity
    const createdDate = data.createdDate
    console.log(data)

    htmlResult += `<div class='col-lg-4'>
                    <div class='whitebox'>
                        <h4 class='widget-title'>Tóm tắt hóa đơn</h4>
                        <table class='table'>
                            <tbody>
                            <tr>
                                <td><i class='fa fa-user' aria-hidden='true'></i>&nbsp;Người thực hiện</td>
                                <td>${staffName}</td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-refresh' aria-hidden='true'></i>&nbsp;Tên dịch vụ</td>
                                <td>${productServiceName}</td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;Số lượng
                                </td>
                                <td>${quantity}</td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;Giá tiền</td>
                                <td>${price}</td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;Ngày thực hiện</td>
                                <td>${createdDate}</td>
                            </tr>
                            <tr class='rzvy_subtotal_exit'>
                                <th>
                                    <i class='fa fa-tags' aria-hidden='true'></i>
                                    &nbsp;Tổng số:
                                </th>
                                <th class="total_amount">
                                    ${totalAmountPayment}
                                </th>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>`;
}