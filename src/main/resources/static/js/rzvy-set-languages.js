/** set selected language for superadmin JS **/
$(document).on('change', '.sarzy_set_language', function(){
	$(".rzvy_main_loader").removeClass("rzvy_hide_loader");
	var ajaxurl = generalObj.ajax_url;
	var lang = $(this).val();
	$.ajax({
		type: 'post',
		data: {
			'lang': lang,
			'set_sarzy_selected_lang': 1
		},
		url: ajaxurl + "rzvy_set_language_ajax.php",
		success: function (res) {
			location.reload();
		}
	});
});

/** set selected language for admin JS **/
$(document).on('click', '.rzvy_set_language', function(){
	$(".rzvy_main_loader").removeClass("rzvy_hide_loader");
	var ajaxurl = generalObj.ajax_url;
	var lang = $(this).attr('value');
	$.ajax({
		type: 'post',
		data: {
			'lang': lang,
			'set_rzvy_selected_lang': 1
		},
		url: ajaxurl + "rzvy_set_language_ajax.php",
		success: function (res) {
			location.reload();
		}
	});
});

/** set selected language for admin JS **/
$(document).on('click', '.rzvy_set_language_atag', function(){
	$(".rzvy_main_loader").removeClass("rzvy_hide_loader");
	var ajaxurl = generalObj.ajax_url;
	var lang = $(this).data("lng");
	$.ajax({
		type: 'post',
		data: {
			'lang': lang,
			'set_rzvy_selected_lang': 1
		},
		url: ajaxurl + "rzvy_set_language_ajax.php",
		success: function (res) {
			location.reload();
		}
	});
});

/** Toggle comman header JS **/
$(document).on('click', '#rzvy-sasa-navbarresponsive-toggler-icon', function(){
	$("#rzvy-sasa-navbarresponsive").slideToggle();
});

/** Hide all notification detail JS **/
$(document).on('click', '.rzvy_menubar_langmenu_link', function(){ 
	/* hide notification dropdown start */
	$("#rzvy-mainnav .navbar-collapse .navbar-nav > .nav-item.dropdown.rzvy-notification-dd").removeClass("show");
	$('#rzvy-notification-dropdown-content').html("");
	$('#rzvy-notification-dropdown-content').slideUp();
	/* hide notification dropdown start */

	/* hide refund dropdown start */
	$("#rzvy-mainnav .navbar-collapse .navbar-nav > .nav-item.dropdown.rzvy-refundrequest-dd").removeClass("show");
	$('#rzvy-refund-dropdown-content').html("");
	$('#rzvy-refund-dropdown-content').slideUp();
	/* hide refund dropdown end */
	
	/* hide supportticket dropdown start */
	$("#rzvy-mainnav .navbar-collapse .navbar-nav > .nav-item.dropdown.rzvy-supportticket-dd").removeClass("show");
	$('#rzvy-supportticket-dropdown-content').html("");
	$('#rzvy-supportticket-dropdown-content').slideUp();
	/* hide supportticket dropdown end */
});
$(document).on('click', '.rzvy_menubar_usermenu_link', function(){
	/* hide notification dropdown start */
	$("#rzvy-mainnav .navbar-collapse .navbar-nav > .nav-item.dropdown.rzvy-notification-dd").removeClass("show");
	$('#rzvy-notification-dropdown-content').html("");
	$('#rzvy-notification-dropdown-content').slideUp();
	/* hide notification dropdown start */

	/* hide refund dropdown start */
	$("#rzvy-mainnav .navbar-collapse .navbar-nav > .nav-item.dropdown.rzvy-refundrequest-dd").removeClass("show");
	$('#rzvy-refund-dropdown-content').html("");
	$('#rzvy-refund-dropdown-content').slideUp();
	/* hide refund dropdown end */
	
	/* hide supportticket dropdown start */
	$("#rzvy-mainnav .navbar-collapse .navbar-nav > .nav-item.dropdown.rzvy-supportticket-dd").removeClass("show");
	$('#rzvy-supportticket-dropdown-content').html("");
	$('#rzvy-supportticket-dropdown-content').slideUp();
	/* hide supportticket dropdown end */
});