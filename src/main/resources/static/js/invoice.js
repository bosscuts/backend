let staffIds = [];
let htmlResult = '';
$(document).ready(function () {
    $(document).on('click', '.card', function () {
        let staff = $(this);
        staff.toggleClass('border');
        staff.toggleClass('border-primary');
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
        console.log(staffIds)
        $.ajax({
            url: "/invoice/" + staffId,
            type: "get",
            success: function (result) {
                result.forEach(concatHtmlResult);
                $('#result').html(htmlResult);
            }
        });
    });
});

function concatHtmlResult(data, index, array) {
    const productServiceName = data.productServiceName
    const totalAmountPayment = data.totalAmountPayment
    const price = data.price
    const quantity = data.quantity
    htmlResult += `<div class='col-lg-4'>
                    <div class='whitebox'>
                        <h4 class='widget-title'>Tóm tắt hóa đơn</h4>
                        <table class='table'>
                            <tbody>
                            <tr>
                                <td><i class='fa fa-refresh' aria-hidden='true'></i>&nbsp;${productServiceName}</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;${quantity}</font></font>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><i class='fa fa-bookmark' aria-hidden='true'></i>&nbsp;${price}</font></font>
                                </td>
                                <td></td>
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