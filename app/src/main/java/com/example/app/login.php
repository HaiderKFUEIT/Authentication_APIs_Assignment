<?php
include 'config.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->email, $data->password)) {
    $email = $data->email;
    $password = $data->password;

    $query = "SELECT * FROM users WHERE email = '$email'";
    $result = $conn->query($query);

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc();

        if (password_verify($password, $user['password'])) {
            $token = bin2hex(random_bytes(16));
            $update = "UPDATE users SET token = '$token' WHERE email = '$email'";
            $conn->query($update);

            echo json_encode(["status" => "success", "token" => $token]);
        } else {
            echo json_encode(["status" => "error", "message" => "Invalid credentials"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "User not found"]);
    }
}
?>
