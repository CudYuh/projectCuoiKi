datHang = () => {
    let c_fname = document.getElementById('c_fname').value;
    let c_lname = document.getElementById('c_lname').value;
    let c_address = document.getElementById('c_address').value;
    let c_option_address = document.getElementById('c_option_address').value;
    let c_email_address = document.getElementById('c_email_address').value;
    let c_phone = document.getElementById('c_phone').value;
    let c_order_notes = document.getElementById('c_order_notes').value;

    let data = {
        name: c_fname + " " + c_lname,
        address: c_address + " " + c_option_address,
        email: c_email_address,
        phone: c_phone,
        note: c_order_notes,
        cartItems: null
    }

    $.ajax({
        url: '/dat-hang',
        type: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (response) {
            console.log("Bạn đã đặt hàng thành công");
            window.location.href = "/tra-cuu-don-hang/" + response;
        },
        error: function (error) {
            alert(error)
        }
    });
}