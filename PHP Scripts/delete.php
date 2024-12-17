<?php
include_once "database.php";
$database = new Database();
if (isset($_POST["id"])) {
    $database->deleteStatus($_POST["id"]);
    echo "Status successfully deleted";
}
