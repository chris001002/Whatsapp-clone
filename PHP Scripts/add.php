<?php
require_once "database.php";
$mydatabase = new Database();
if (isset($_POST["img"])) {
    $message = $_POST["message"];
    $img = $_POST["img"];
    $userid = $_POST["userid"];
    $mydatabase->addStatus($message, $img, $userid);
}
