let staffIds = [];
let incomeStaffIds = [];
let htmlResult = '';
let htmlIncomeResult = '';
$(document).ready(function () {
    // $('#from_date_sec').datetimepicker({
    //     timepicker:false
    // });
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
        const userId = $('#staff_input').val();
        const invoiceAmount = $('#invoice_amount').val();
        const invoiceDescription = $('#invoice_description').val();
        const requestType = $('#request_type').val();
        const fromDate = $('#from_date').val();
        const toDate = $('#to_date').val();
        console.log(`userId ${userId} - invoiceAmount ${invoiceAmount} - invoiceDescription ${invoiceDescription} - requestType ${requestType}`);
        let valid = true;
        if (!userId || !requestType) {
            swal('Bạn chưa chọn nhân viên hoặc loại yêu cầu!', "", "error");
            valid = false;
        } else if (requestType && userId) {
            if (requestType === 'HOLIDAY' && (!fromDate || !toDate)) {
                swal('Bạn chưa chọn ngày nghỉ!', "", "error");
                valid = false;
            } else if ((requestType === 'CASH' || requestType === 'PAY_FINES')) {
                if (invoiceAmount === '0') {
                    swal('Bạn chưa nhập số tiền!', "", "error");
                    valid = false;
                }
            }
        }
        if (valid) {
            $.ajax({
                url: "/invoice/internal",
                type: 'post',
                data: JSON.stringify({
                    'userId': userId,
                    'amount': invoiceAmount,
                    'requestType': requestType,
                    'fromDate': fromDate,
                    'toDate': toDate,
                    'description': invoiceDescription,
                }),
                contentType: 'application/json; charset=utf-8',
                success: function (data, textStatus, xhr) {
                    console.log(data);
                    console.log(textStatus);
                    console.log(xhr.status);
                    if (textStatus === 'success') {
                        if (requestType === 'PAY_FINES') {
                            swal('Tạo hóa đơn nộp phạt thành công.', "", "success");
                        } else if (requestType === 'CASH') {
                            swal('Ứng tiền thành công.', "", "success");
                        } else if (requestType === 'HOLIDAY') {
                            swal('Tạo đơn xin nghỉ phép thành công.', "", "success");
                        }
                    } else {
                        swal('Tạo yêu cầu thất bại!', "", "error");
                    }
                }
            });
        }
    });

    $(document).on('click', '.staff-income', function () {
        htmlIncomeResult = '';
        $('#result-staff-income').html('');
        let staff = $(this);
        staff.children('.card').toggleClass('border-div');
        const staffId = staff.attr('staffId');
        const index = incomeStaffIds.indexOf(staffId);
        if (index !== -1) {
            incomeStaffIds.splice(index, 1);
        } else {
            incomeStaffIds.push(staffId);
        }

        const staffIdsStr = incomeStaffIds.toString();
        console.log("staffIdsStr " + staffIdsStr)
        if (staffIdsStr) {
            $.ajax({
                url: "/users/income/" + staffIdsStr,
                type: "get",
                success: function (result) {
                    console.log(result)

                    result.forEach(concatIncome);
                    $('#result-staff-income').html(htmlIncomeResult);
                    console.log(htmlIncomeResult)
                }
            });
        }
    });


    $(document).on('click', '.staff-revenue', function () {
        let staff = $(this);
        staff.children('.card').toggleClass('border-div');
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
        staff.children('.card').toggleClass('border-div');

        const staffId = staff.attr('staffId');
        const index = staffIds.indexOf(staffId);
        if (index !== -1) {
            staffIds.splice(index, 1);
        } else {
            staffIds.push(staffId);
        }
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

function concatIncome(data, index, array) {
    const staffName = data.staffName
    const salary = data.salary
    const cash = data.cash
    const totalCompensation = data.totalCompensation
    const totalCommission = data.totalCommission
    htmlIncomeResult += `<div class='col-lg-4'>
                    <div class='whitebox'>
                        <h4 class='widget-title'>Thống kê thu nhập</h4>
                        <table class='table'>
                            <tbody>
                            <tr>
                                <td><i class='fa fa-user' aria-hidden='true'></i>&nbsp;Nhân viên</td>
                                <td>${staffName}</td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-tags' aria-hidden='true'></i>&nbsp;Tiền lương</td>
                                <td>${salary}</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-money" aria-hidden="true"></i>&nbsp;Tạm ứng</td>
                                <td>${cash}</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-money" aria-hidden="true"></i>&nbsp;Nộp phạt</td>
                                <td>${totalCompensation}</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-money" aria-hidden="true"></i>&nbsp;Hoa hồng</td>
                                <td>${totalCommission}</td>
                            </tr>
                            <tr class='rzvy_subtotal_exit'>
                                <th>
                                    <i class="fa fa-money" aria-hidden="true"></i>
                                    &nbsp;Thực nhận:
                                </th>
                                <th class="total_amount">
                                    ${salary}
                                </th>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>`;
}

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
                                <td><i class='fa fa-tags' aria-hidden='true'></i>&nbsp;Tên dịch vụ</td>
                                <td>${productServiceName}</td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;Số lượng</td>
                                <td>${quantity}</td>
                            </tr>
                            <tr>
                                <td><i class="fa fa-money" aria-hidden="true"></i>&nbsp;Giá tiền</td>
                                <td>${price}</td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;Ngày thực hiện</td>
                                <td>${createdDate}</td>
                            </tr>
                            <tr class='rzvy_subtotal_exit'>
                                <th>
                                    <i class="fa fa-money" aria-hidden="true"></i>
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