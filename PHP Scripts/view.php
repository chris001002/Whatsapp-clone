<?php
require_once "database.php";
$myDatabase = new Database();
echo $myDatabase->getAllStatus();
