<?php
session_start();
//date_default_timezone_set("Asia/Dhaka");
define("DB_HOST", "localhost");
define("DB_USERNAME", "root");
define("DB_PASS", "");
define("DB_NAME", "no_more");
define("BASE_URL", "http://localhost/no_more/");
//define("BASE_PATH","C://wamp/www/tims/");
define("BASE_PATH",__DIR__."/");
function goURL($url) {
    return BASE_URL.$url;
}
function getPATH($path) {
    return BASE_PATH.$path;
}
if(!defined("THIS_PAGE_TITLE")){
define("THIS_PAGE_TITLE", "Training Information Management System");
}






if(!defined("LOGED_USER_PRIVILEGE")){
define("LOGED_USER_PRIVILEGE", 0);
}
define("EMPLOYEE", 1);
define("COORDINATOR", 0);
