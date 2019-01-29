<nav class="navbar navbar-expand-lg navbar-dark py-lg-4" id="mainNav">
      <div class="container" style="margin-left:20px;">
        <img border="0" alt="img" class="img-responsive" src="./img/logo2.png" width="50" height="50"><h4 style="color:#19424A; margin-top: 10px; margin-left:10px; font-style: italic; ">Blood Donation</h4>
        
        <a class="navbar-brand text-uppercase text-expanded font-weight-bold d-lg-none" href="#">Start Bootstrap</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
          <ul class="navbar-nav mx-auto">
            <li class="nav-item px-lg-4">
              <a class="nav-link text-uppercase text-expanded" href="javascript:;" id="status"></a>
            </li>

        

            <li class="nav-item  px-lg-4">
              <a class="nav-link text-uppercase text-expanded" href="dashboard.php">Dashboard
                <span class="sr-only">(current)</span>
              </a>
            </li>
            <li class="nav-item  px-lg-4">
              <a class="nav-link text-uppercase text-expanded" href="profile_update.php">Profile
                <span class="sr-only">(current)</span>
              </a>
            </li>
            <li class="nav-item active px-lg-4">
              <a class="nav-link text-uppercase text-expanded"  href="notification.php" onclick="updatee(1);">Notifications
                <i class="material-icons" style="font-size:20px;color:white">sms</i>
                <i style="font-size:15px;color:white"><span id="notifcaiton_no"></span></i>
               </a>
            </li>
            
            <li class="nav-item px-lg-4">
              <a class="nav-link text-uppercase text-expanded" href="logout.php">Logout</a>
            </li>
           <div>
              <a class="nav-link text-uppercase text-expanded" href="javascript:;" style="color:white;">Welcome: <?php echo $_SESSION["donar_name"];?></a>
          </div>
      
          </ul>
        </div>
      </div>
    </nav>

   