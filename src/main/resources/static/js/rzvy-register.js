/*
* rzvy
* Online Multi Business Appointment Scheduling & Reservation Booking Calendar
*/
var rzvy_loader = '<div class="rzvy-loader"><div class="spinner-border m-5" role="status"><span class="visually-hidden">Loading...</span></div></div>';

$(document).ready(function(){
	var ajaxurl = generalObj.ajax_url;
	var site_url = generalObj.site_url;
	
	/** JS to add intltel input to phone number **/
	$("#rzvy_register_customer_phone").intlTelInput({
		initialCountry: generalObj.defaultCountryCode,
		separateDialCode: true,
		utilsScript: site_url+"includes/vendor/intl-tel-input/js/utils.js",
    });
	
	
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
	
	/** Validation patterns **/
	$.validator.addMethod("pattern_name", function(value, element) {
		return this.optional(element) || /^[a-zA-Z '.']+$/.test(value);
	}, generalObj.please_enter_only_alphabets);
	$.validator.addMethod("pattern_phone", function(value, element) {
		return this.optional(element) || /\d+(?:[ -]*\d+)*$/.test(value);
	}, generalObj.please_enter_valid_phone_number_without_country_code);
	$.validator.addMethod("pattern_zip", function(value, element) {
		return this.optional(element) || /^[a-zA-Z 0-9\-]*$/.test(value);
	}, generalObj.please_enter_valid_zip);
	
	/** Validate register as customer form **/
	$('#rzvy_customer_register_form').validate({
		rules: {
			rzvy_register_customer_firstname:{ required: true, maxlength: 50 },
			rzvy_register_customer_lastname: { required:true, maxlength: 50 },
			rzvy_register_customer_email:{ required: true, email: true, remote: { 
				url:ajaxurl+"rzvy_check_email_ajax.php",
				type:"POST",
				async:false,
				data: {
					email: function(){ return $("#rzvy_register_customer_email").val(); },
					check_email_exist: 1
				}
			} },
			rzvy_register_customer_password:{ required: true, minlength: 8, maxlength: 20 },
			rzvy_register_customer_phone: { required:true, minlength: 10, maxlength: 15, pattern_phone:true },
			rzvy_register_customer_address: { required:true },
			rzvy_register_customer_city: { required:true },
			rzvy_register_customer_state: { required:true },
			rzvy_register_customer_zip: { required:true, pattern_zip:true, minlength: 5, maxlength: 10 },
			rzvy_register_customer_country: { required:true}
		},
		messages: {
			rzvy_register_customer_firstname:{ required: generalObj.please_enter_first_name, maxlength: generalObj.please_enter_maximum_50_characters },
			rzvy_register_customer_lastname: { required: generalObj.please_enter_last_name, maxlength: generalObj.please_enter_maximum_50_characters },
			rzvy_register_customer_email:{ required: generalObj.please_enter_email, email: generalObj.please_enter_valid_email, remote: generalObj.email_already_exist },
			rzvy_register_customer_password: { required: generalObj.please_enter_password, minlength: generalObj.please_enter_minimum_8_characters, maxlength: generalObj.please_enter_maximum_20_characters },
			rzvy_register_customer_phone: { required: generalObj.please_enter_phone_number, minlength: generalObj.please_enter_minimum_10_digits, maxlength: generalObj.please_enter_maximum_15_digits },
			rzvy_register_customer_address: { required: generalObj.please_enter_address },
			rzvy_register_customer_city: { required: generalObj.please_enter_city },
			rzvy_register_customer_state: { required: generalObj.please_enter_state },
			rzvy_register_customer_zip: { required: generalObj.please_enter_zip, minlength: generalObj.please_enter_minimum_5_characters, maxlength: generalObj.please_enter_maximum_10_characters },
			rzvy_register_customer_country: { required: generalObj.please_enter_country }
		}
	});
});

/** Prevent enter key stroke on form inputs **/
$(document).on("keydown", '.rzvy form input', function (e) {
	if (e.keyCode == 13) {
		e.preventDefault();
		return false;
	}
});

/** Register as Customer JS **/
$(document).on('click', '#rzvy_customer_register_btn', function(e){
	e.preventDefault();
	var site_url = generalObj.site_url;
	var ajaxurl = generalObj.ajax_url;
	if($('#rzvy_customer_register_form').valid()){
		$(this).append(rzvy_loader);
		$(".rzvy_main_loader").removeClass("rzvy_hide_loader");
		var firstname = $("#rzvy_register_customer_firstname").val();
		var lastname = $("#rzvy_register_customer_lastname").val();
		var email = $("#rzvy_register_customer_email").val();
		var password = $("#rzvy_register_customer_password").val();
		var phone = $("#rzvy_register_customer_phone").intlTelInput("getNumber");
		var address = $("#rzvy_register_customer_address").val();
		var city = $("#rzvy_register_customer_city").val();
		var state = $("#rzvy_register_customer_state").val();
		var zip = $("#rzvy_register_customer_zip").val();
		var country = $("#rzvy_register_customer_country").val();
		
		$.ajax({
			type: 'post',
			data: {
				'email': email,
				'password': password,
				'firstname': firstname,
				'lastname': lastname,
				'phone': phone,
				'address': address,
				'city': city,
				'state': state,
				'zip': zip,
				'country': country,
				'customer_register': 1
			},
			url: ajaxurl + "rzvy_register_as_customer_ajax.php",
			success: function (res) {
				$(".rzvy-loader").remove();
				if(res=="registered"){
					swal(generalObj.registered, generalObj.you_are_registered_successfully_please_login, "success");
					window.location.replace(site_url+"backend/refer.php");
				}else{
					swal(generalObj.opps, generalObj.something_went_wrong_please_try_again+" "+response, "error");
				}
			}
		});
	}
});