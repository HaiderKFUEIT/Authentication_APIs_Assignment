<?php
include 'config.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->name, $data->email, $data->password)) {
    $name = $data->name;
    $email = $data->email;
    $password = password_hash($data->password, PASSWORD_DEFAULT);
    $token = bin2hex(random_bytes(16));

    $query = "INSERT INTO users (name, email, password, token) VALUES ('$name', '$email', '$password', '$token')";
    if ($conn->query($query) === TRUE) {
        echo json_encode(["status" => "success", "token" => $token]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error registering user"]);
    }
}
?>
