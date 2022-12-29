<?php
$servername = "localhost";
$username = "api_gzbbs";
$password = "api_gzbbs";
$dbname = "api_gzbbs";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
mysqli_query($conn , "set names utf8"); //编码设置
$username = $_GET['username'];
$password = $_GET['password'];
$sql = "SELECT * FROM userTable where username = '$username' and password = '$password'";
$result = $conn->query($sql);
$arr = array();
// 输出每行数据
while($row = $result->fetch_assoc()) {
    $count=count($row);//不能在循环语句中，由于每次删除row数组长度都减小
    for($i=0;$i<$count;$i++){
    unset($row[$i]);//删除冗余数据
    }
    array_push($arr,$row);
}
// 非空判断
if (count($arr) == 0) {
    $arr = array(
        'code' => 0,
        'msg' => '用户名或密码错误'
    );
} else {
    $arr = array(
        'code' => 1,
        'msg' => '登录成功'
    );
}
echo json_encode($arr,JSON_UNESCAPED_UNICODE);//json编码