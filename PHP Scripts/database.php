<?php
date_default_timezone_set("Asia/Jakarta");
class Database
{
    private $conn = null;
    function __construct()
    {
        $this->conn = new mysqli("localhost", "root", "", "whatsappstatus", 3306);

        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        }
    }
    function __destruct()
    {
        $this->conn = null;
    }
    public function addStatus($message, $img, $userid)
    {
        $query = "INSERT INTO `whatsappstatus`.`status` (`statusMessage`, `userid`, `image`) VALUES (?,?,?)";
        $stmt = $this->conn->prepare($query);
        $stmt->bind_param("sss", $message, $userid, $img);
        $stmt->execute();
        $stmt->close();
    }
    public function getAllStatus()
    {
        $query = "SELECT id, userId, statusMessage, `image`,`time`  FROM `whatsappstatus`.`status`";
        $result = $this->conn->query($query);
        $rows = mysqli_fetch_all($result, MYSQLI_ASSOC);
        return json_encode($rows);
    }
    public function updateStatusMessage($id, $message)
    {
        $query = "UPDATE `whatsappstatus`.`status` SET `statusMessage` = ? WHERE `id` = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bind_param("ss", $message, $id);
        $stmt->execute();
        $stmt->close();
    }
    public function updateStatusImage($id, $img)
    {
        $query = "UPDATE `whatsappstatus`.`status` SET `image` = ? WHERE `id` = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bind_param("ss", $img, $id);
        $stmt->execute();
        $stmt->close();
    }
    public function deleteStatus($id)
    {
        $query = "DELETE FROM `whatsappstatus`.`status` WHERE `id` = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $stmt->close();
    }
    public function delete24HoursPassed()
    {
        $query = "DELETE FROM `whatsappstatus`.`status` WHERE `time` < ?";
        $stmt = $this->conn->prepare($query);
        $date = date("Y-m-d H:i:s", strtotime("-24 hours"));
        $stmt->bind_param("s", $date);
        $stmt->execute();
        $stmt->close();
    }
}
