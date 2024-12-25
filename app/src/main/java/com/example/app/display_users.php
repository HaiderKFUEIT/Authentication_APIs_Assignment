<?php
include 'config.php';

$query = "SELECT * FROM users";
$result = $conn->query($query);

if ($result->num_rows > 0) {
    $users = [];
    while ($row = $result->fetch_assoc()) {
        // Return password along with other user details
        $users[] = [
            'name' => $row['name'],
            'email' => $row['email'],
            'password' => $row['password'], // Add password
        ];
    }
    echo json_encode(["status" => "success", "users" => $users]);
} else {
    echo json_encode(["status" => "error", "message" => "No users found"]);
}
?>
