<?php

class connect {

    public $con;
    function __construct() {
        $this->con=new mysqli(DB_HOST,DB_USERNAME, DB_PASS,DB_NAME);
        return $this->con;
    }

    function insert($table,$column,$column_value) {
        $link= $this->con;
        $q="INSERT INTO `$table` ($column) VALUES ($column_value)";     
        //echo $q;
        return $link->query($q);        
    }
    
 function update_msg($table,$column_value) {
        $link= $this->con;
        $q="UPDATE `$table` SET $column_value";
        //echo $q;
        return $link->query($q);
    }
    function update($table,$column_value,$where) {
        $link= $this->con;
        $q="UPDATE `$table` SET $column_value WHERE $where";
        //echo $q;
        return $link->query($q);
    }

    function select($table,$column,$where="1=1"){  
        $link= $this->con;
        $q="SELECT $column FROM `$table` WHERE $where";
        //echo $q;
        $result= $link->query($q);
    //    $row=$result->fetch_assoc();
       // echo $row["id"];
        return $result;
    }
    
  
    function select_custom($q){  
        $link= $this->con;
        //echo $q;
        $result= $link->query($q);
    //    $row=$result->fetch_assoc();
       // echo $row["id"];
        return $result;
    }
	
	function select_rec($q){  
        $link= $this->con;
        //echo $q;
        $result= $link->query($q);
    //    $row=$result->fetch_assoc();
       // echo $row["id"];
        return $result;
    }    
   
    function delete($table,$where) {
        $link= $this->con;
        $q="DELETE FROM `$table` WHERE $where";
        //echo $q;
        return $link->query($q);
    }
  
    function get_all_table($pass) {
        $link= $this->con;
        //$link->tablename();
    }    
}
