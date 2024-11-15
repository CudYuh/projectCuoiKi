function addShoe(url) {
    // lấy giá trị từ các trường input trong form
    var title = document.getElementById("inputTitle").value;
    var categoryId = document.getElementById("inputCategory").value;
    var hotOrder = document.getElementById("inputHotOrder").value;
    var price = document.getElementById("inputPrice").value;
    var promotePrice = document.getElementById("inputPromotePrice").value;
    var description = document.getElementById("inputDescription").value;

    // lấy giá trị từ bảng ShoeDetails
    var shoeDetails = [];
    var table = document.getElementById("shoeDetails");
    for (var i = 0; i < table.rows.length; i++) {
        var row = table.rows[i];
        var id = row.attributes['data-id'].value
        var color = row.cells[0].innerText;
        var size = row.cells[1].innerText;
        shoeDetails.push({
            id: id,
            color: color,
            size: size,
        });
    }

    function send(img) {
        var shoeFullDetail = {
            shoe: {
                title: title,
                categoryId: categoryId,
                hotOrder: hotOrder,
                price: price,
                promotePrice: promotePrice,
                description: description,
                image: img
            },
            shoeDetails: shoeDetails
        };

        fetch(url, {
            method: 'POST',
            body: JSON.stringify(shoeFullDetail),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 401) {
                        window.location.href = '/admin/login';
                    } else {
                        alert("Có lỗi xảy ra, nhấn để tải lại trang")
                        window.location.href = '/admin/shoe/add';
                    }
                }
                else {
                    alert("Thành công")
                    window.location.href = '/admin/shoe';
                }
            })
            .catch(error => {
                alert("Có lỗi xảy ra, nhấn để tải lại trang")
                window.location.href = '/admin/shoe/add';
            });
    }

    const inputImage = document.getElementById('inputImage');
    if (inputImage.files.length == 0) send(null)
    else {
        const file = inputImage.files[0];

        const reader = new FileReader();
        reader.readAsArrayBuffer(file);
        reader.onload = function () {
            const imageData = new Uint8Array(reader.result);

            byte_array = Array.from(imageData)
            send(byte_array)
        }
    }
}

function editShoeDetail(button) {
    var id = button.getAttribute("data-id");
    var color = button.getAttribute("data-color");
    var size = button.getAttribute("data-size");

    var row = button.parentNode.parentNode;
    var cells = row.getElementsByTagName("td");
    cells[0].innerHTML = '<input type="text" value="' + color + '">';
    cells[1].innerHTML = '<input type="text" value="' + size + '">';
    cells[2].innerHTML = '<button onclick="saveEditedShoeDetail(this)" class="btn btn-success" type="button" data-id="' + id + '"><i class="fa-solid fa-check"></i>&nbsp;Lưu</button><button type="button" class="btn btn-danger" onclick="cancelEditShoeDetail(this)" data-color="' + color + '" data-size="' + size + '">Hủy</button>';
}
function saveEditedShoeDetail(button) {
    var id = button.getAttribute("data-id");
    var color = button.parentNode.parentNode.getElementsByTagName("input")[0].value;
    var size = button.parentNode.parentNode.getElementsByTagName("input")[1].value;
    var row = button.parentNode.parentNode;
    var cells = row.getElementsByTagName("td");
    cells[0].innerHTML = color;
    cells[1].innerHTML = size;
    cells[2].innerHTML = '<button type="button" onclick="editShoeDetail(this)" data-id="new" data-color="' + color + '" data-size="' + size + '" class="btn btn-warning">Sửa</button><button type="button" onclick="deleteShoeDetail(this)" data-id="new" class="btn btn-danger">Xóa</button>';

    // gửi giá trị mới lên server để lưu lại thông tin của ShoeDetail
    // sử dụng AJAX hoặc form submit để thực hiện việc này
}
function cancelEditShoeDetail(button) {
    var color = button.getAttribute("data-color");
    var size = button.getAttribute("data-size");

    var row = button.parentNode.parentNode;
    var cells = row.getElementsByTagName("td");
    cells[0].innerHTML = color;
    cells[1].innerHTML = size;
    cells[2].innerHTML = '<button type="button"  onclick="editShoeDetail(this)" data-id="' + button.getAttribute("data-id") + '" data-color="' + color + '" data-size="' + size + '" class="btn btn-warning">Sửa</button><button type="button" onclick="deleteShoeDetail(this)" data-id="' + button.getAttribute("data-id") + '" class="btn btn-danger">Xóa</button>';
}
function showAddShoeDetailRow() {
    document.getElementById("addShoeDetailRow").style.display = "";
}

function cancelAddShoeDetail() {
    document.getElementById("addShoeDetailRow").style.display = "none";
}

function saveShoeDetail() {
    var color = document.getElementById("color").value;
    var size = document.getElementById("size").value;
    var table_body = document.getElementById("shoeDetails");
    var row = table_body.insertRow(-1);
    row.setAttribute("data-id", 0);
    row.innerHTML = '<td>' + color + '</td><td>' + size + '</td><td><button type="button" onclick="editShoeDetail(this)" data-id="new" data-color="' + color + '" data-size="' + size + '" class="btn btn-warning">Sửa</button><button type="button" onclick="deleteShoeDetail(this)" data-id="new" class="btn btn-danger">Xóa</button></td>';
    resetNewShoeDetail();
    cancelAddShoeDetail();
}

function deleteShoeDetail(button) {
    var id = button.getAttribute("data-id");


    var row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
}
function resetNewShoeDetail() {
    color = document.getElementById("color").value = '';
    size = document.getElementById("size").value = '';
}