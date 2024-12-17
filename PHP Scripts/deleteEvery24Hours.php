<?php

require_once "database.php";

$database = new Database();

$database->delete24HoursPassed();
