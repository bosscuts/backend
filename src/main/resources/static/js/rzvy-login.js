/*
* rzvy
* Online Multi Business Appointment Scheduling & Reservation Booking Calendar
*/
var rzvy_loader = '<div class="rzvy-loader"><div class="spinner-border m-5" role="status"><span class="visually-hidden">Loading...</span></div></div>';
$(document).ready(function(){
	
	/* Language Selector On Change */
	  $(document).on('click','.country-selectd', function(event){
		$('.country-selectd').addClass('open');
	  });   
	  $(document).on('blur','.country-selectd', function(event){
		$('.country-selectd').removeClass('open');
	  });   
	  $('.country-selectd li').click(function(){	  
		var text = $(this).html();
		$(this).parents('.country-selectd').removeClass('open');
		$('.country-selectd a').html(text);
	  });
	  
	/** validate login form **/
	 $('#rzvy_login_form').validate({
        rules: {
            rzvy_login_email: {required: true, email: true},
            rzvy_login_password: {required: true, minlength:8, maxlength:20}
        },
        messages: {
            rzvy_login_email: {required: generalObj.please_enter_email, email: generalObj.please_enter_valid_email},
            rzvy_login_password: {required: generalObj.please_enter_password, minlength: generalObj.please_enter_minimum_8_characters, maxlength: generalObj.please_enter_maximum_20_characters}
        }
    });
	
	/** validate reset password form **/
	$('#rzvy_reset_password_form').validate({
		rules: {
			rzvy_reset_new_password: { required:true, minlength: 8, maxlength: 20 },
			rzvy_reset_retype_new_password: { required:true, equalTo: "#rzvy_reset_new_password", minlength: 8, maxlength: 20 }
		},
		messages: {
			rzvy_reset_new_password: { required: generalObj.please_enter_password, minlength: generalObj.please_enter_minimum_8_characters, maxlength: generalObj.please_enter_maximum_20_characters },
			rzvy_reset_retype_new_password: { required: generalObj.please_enter_retype_new_password, equalTo: generalObj.new_password_and_retype_new_password_mismatch, minlength: generalObj.please_enter_minimum_8_characters, maxlength: generalObj.please_enter_maximum_20_characters }
		}
	});
	
	/** validate forget password form **/
	$('#rzvy_forgot_password_form').validate({
        rules: {
            rzvy_forgot_password_email: {required: true, email: true}
        },
        messages: {
            rzvy_forgot_password_email: {required: generalObj.please_enter_email, email: generalObj.please_enter_valid_email}
        }
    });
});

/** Login process JS **/
$(document).on('click','#rzvy_login_btn',function(e){
	e.preventDefault();
	$('#rzvy-login-error').hide();
	var email = $('#rzvy_login_email').val();
	var password = $('#rzvy_login_password').val();
	var remember_me = $('#rzvy_login_remember_me').prop('checked');
	if(remember_me){
		remember_me = 'Y';
	}else{
		remember_me = 'N';
	}
	var site_url = generalObj.site_url;
	var ajax_url = generalObj.ajax_url;
	if ($('#rzvy_login_form').valid()){
		$(this).append(rzvy_loader);
		$.ajax({
			type: 'post',
			data: {
				'email': email,
				'password': password,
				'remember_me': remember_me,
				'login_process': 1
			},
			url: ajax_url + "rzvy_login_ajax.php",
			success: function (res) {
				$(".rzvy-loader").remove();
				if(res.trim() == "customer"){
                   window.location.replace(site_url+"backend/c-dashboard.php");
				}else if(res.trim() == "staff"){
					window.location.replace(site_url+"backend/s-dashboard.php");
				}else if(res.trim() == "admin"){
					window.location.replace(site_url+"backend/dashboard.php");
				}else{
					$('#rzvy-login-error').show();
				}
			}
		});
	}
});

/** Reset password JS **/
$(document).on('click','#rzvy_reset_password_btn',function(e){
	e.preventDefault();
	$('#rzvy-login-error').hide();
	var password = $('#rzvy_reset_retype_new_password').val();
	var ajax_url = generalObj.ajax_url;
	var site_url = generalObj.site_url;
	if ($('#rzvy_reset_password_form').valid()){
		$(this).append(rzvy_loader);
		$.ajax({
			type: 'post',
			data: {
				'password': password,
				'reset_password': 1
			},
			url: ajax_url + "rzvy_login_ajax.php",
			success: function (res) {
				$(".rzvy-loader").remove();
				if(res.trim() == "reset"){
					swal(generalObj.reset_qm, generalObj.your_password_reset_successfully_try_login_to_enjoy_services, "success");
					window.location.replace(site_url+"backend");
				}else{
					swal(generalObj.opps, generalObj.something_went_wrong_please_try_again, "error");
				}
			}
		});
	}
});

/** Forget password JS **/
$(document).on('click','#rzvy_forgot_password_btn',function(e){
	e.preventDefault();
	$('#rzvy-forgot-password-success').hide();
	$('#rzvy-forgot-password-error').hide();
	var email = $('#rzvy_forgot_password_email').val();
	var site_url = generalObj.site_url;
	var ajax_url = generalObj.ajax_url;
	if ($('#rzvy_forgot_password_form').valid()){
		$(this).append(rzvy_loader);
		$.ajax({
			type: 'post',
			data: {
				'email': email,
				'forgot_password': 1
			},
			url: ajax_url + "rzvy_login_ajax.php",
			success: function (res) {
				$(".rzvy-loader").remove();
				if(res.trim() == "mailsent"){
					$('#rzvy-forgot-password-error').hide();
					$('#rzvy-forgot-password-success').html(generalObj.reset_password_link_sent_successfully_at_your_registered_email_address);
					$('#rzvy-forgot-password-success').show();
				}else if(res.trim() == "tryagain"){
					$('#rzvy-forgot-password-success').hide();
					$('#rzvy-forgot-password-error').html(generalObj.oops_error_occurred_please_try_again);
					$('#rzvy-forgot-password-error').show();
				}else{
					$('#rzvy-forgot-password-success').hide();
					$('#rzvy-forgot-password-error').html(generalObj.invalid_email_address);
					$('#rzvy-forgot-password-error').show();
				}
			}
		});
	}
});

/* Demo Only */
$(document).on('change','#rezervy_user_type',function(e){
	var useremail = $(this).val();
	$('#rzvy_login_email').val(useremail);
});