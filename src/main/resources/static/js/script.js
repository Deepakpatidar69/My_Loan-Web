

    function  toggleButton(){
        
        document.getElementById("signup-btns").style.display = "block";
        
    }
    
    function sendOTP() {
        var email = document.getElementById("email").value;
 
        alert("OTP sent to " + email);
           var xhr = new XMLHttpRequest();
        xhr.open("GET", "send-Otp/" + email);
        xhr.send();
        document.getElementById("otpGroup").style.display = "block";
    }


const toggleallRes = () =>{
    
  
            $(".nav-link").removeClass("active");
            $("#all-res").addClass("active");
};

const toggleSidebar = () => {

    if ($('.sidebar').is(":visible")) {

        // Display Sidebar
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");

    } else {
        // Show Sidebar
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");

    }

};

const toggleyRes = () =>{
    
    console.log("clicked all ");
            $(".nav-link").removeClass("active");
            $("#your-res").addClass("active");
};

const toggleappRes = () =>{
    
    console.log("clicked all ");
            $(".nav-link").removeClass("active");
            $("#app-res").addClass("active");
};


