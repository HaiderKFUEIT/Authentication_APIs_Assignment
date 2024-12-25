<?php
include 'config.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->email, $data->new_password)) {
    $email = $data->email;
    $new_password = password_hash($data->new_password, PASSWORD_DEFAULT);

    $query = "UPDATE users SET password = '$new_password' WHERE email = '$email'";

    if ($conn->query($query) === TRUE) {
        echo json_encode(["status" => "success"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error changing password"]);
    }
}
?>
