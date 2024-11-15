const plusButtons = document.querySelectorAll('.js-btn-plus');
const minusButtons = document.querySelectorAll('.js-btn-minus');

plusButtons.forEach(button => {
  button.addEventListener('click', () => {
    updateQuantity(button, 1);
  });
});

minusButtons.forEach(button => {
  button.addEventListener('click', () => {
    updateQuantity(button, -1);
  });
});

addToCart = (shoeId) => {
  var shoeDetailIds = [];
  var checkboxes = document.querySelectorAll('input[type="checkbox"][name="shop-sizes"]:checked');
  for (var i = 0; i < checkboxes.length; i++) {
    var checkbox = checkboxes[i];
    shoeDetailIds.push(parseInt(checkbox.getAttribute('id').replace('option-', '')));
  }

  $.ajax({
    url: '/gio-hang/add',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(shoeDetailIds),
    success: function (response) {
      window.location.href = "/gio-hang"
    },
    error: function (error) {
      console.error('There was an error!', error);
    }
  });
}

updateCart = () => {
  const tableRows = document.querySelectorAll('#cartItems tr');
  const cartItems = [];
  tableRows.forEach(row => {
    const shoeDetailId = row.getAttribute('id');
    const quantity = row.querySelector('input').value;
    cartItems.push({ first: shoeDetailId, second: quantity });
  });

  $.ajax({
    url: '/gio-hang/update',
    type: 'POST',
    data: JSON.stringify(cartItems),
    contentType: 'application/json',
    success: function (response) {
      alert("Cập nhật thành công")
      window.location.reload();
    },
    error: function (error) {
      alert("Có lỗi xảy ra, vui lòng tải lại trang")
    }
  });
}

function updateQuantity(button, diff) {
  const row = button.closest('tr');
  const quantityInput = row.querySelector('input');
  const quantity = parseInt(quantityInput.value);
  const price = row.querySelector('td:nth-of-type(5)');
  let price1 = price.textContent.replaceAll(',', '');
  price1 = price1.replaceAll('VND', '');
  price1 = price1.replaceAll(' ', '');
  price1 = price1.replaceAll('\n', '');
  price1 = price1.replaceAll('.', '');
  const totalElement = row.querySelector('td:nth-of-type(7) span');
  const total = (quantity + diff) > 0 ? (quantity + diff) * price1 : 0;

  totalElement.textContent = formatCurrency(total);
}

function formatCurrency(number) {
  return number.toLocaleString('vi-VN', { minimumFractionDigits: 0, maximumFractionDigits: 3 }) + ' VND';
}