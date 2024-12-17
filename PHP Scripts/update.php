<?php
require_once "database.php";
$database = new Database();
if (isset($_POST["statusMessage"])) {
    $database->updateStatusMessage($_POST["id"], $_POST["statusMessage"]);
    echo "Status message successfully updated";
} else if (isset($_POST["img"])) {
    $database->updateStatusImage($_POST["id"], $_POST["img"]);
    echo "Status image successfully updated";
}
