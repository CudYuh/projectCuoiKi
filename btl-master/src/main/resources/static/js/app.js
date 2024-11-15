var input1=document.getElementById('signin').value;
var input2=document.getElementById('password').value;

var btn1=document.getElementById('btn1');
var btn2=document.getElementById('btn2');

function login(){
    if(document.getElementById('signin').value=='kin1'&&document.getElementById('password').value=='123'){
        alert('da dang nhap')
    }
    else{
        alert('saimk')
    }
}
btn1.addEventListener('click',login);