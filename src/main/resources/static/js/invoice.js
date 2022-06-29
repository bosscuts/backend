let staffIds = [];
let htmlResult = '';
$(document).ready(function () {
    $(document).on('click', '.staff', function () {
        let staff = $(this);
        staff.children('.card').toggleClass('border');
        staff.children('.card').toggleClass('border-primary');
    });
    // $(document).on('click', '.card', function () {
    //     let staff = $(this);
    //     staff.toggleClass('border');
    //     staff.toggleClass('border-primary');
    // });

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