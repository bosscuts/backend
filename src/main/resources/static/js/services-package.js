/*
** Rezervy Services Packages Main JS
*/
(function($) {
	"use strict";
	
	/* On Choose service Rest Package Usage */
	$(document).on("click", "#rzvy_logout_btn", function(){
		$('#rzvy_package_reset').val('N');
		$(".rzvy-package-credits-control-input").trigger('change');
	});

	$(document).ajaxComplete(function(event,xhr,options){
		if(options.url!==undefined || options.url!==undefined!==null){
			var rzvy_currajax_xhr = options.url;
			if(rzvy_currajax_xhr.indexOf('rzvy_front_ajax.php')>=0 || rzvy_currajax_xhr.indexOf('rzvy_front_cart_ajax.php')>=0){
				if(options.data!==undefined || options.data!==undefined!==null){
					var rzvy_currajax_xhrdata = options.data;
					if(rzvy_currajax_xhrdata.indexOf('get_multi_and_single_qty_addons_content')>=0){
						$('#rzvy_package_reset').val('Y');
						var rzvy_packages  = $("#rzvy_services_package_content").html();
						if(($('input[name="rzvy-user-selection"]:checked').val() == "nc" || $('input[name="rzvy-user-selection"]:checked').val() == "ec") && rzvy_packages==''){
							rzvy_list_packages_func();
						}
					}	
					if(rzvy_currajax_xhrdata.indexOf('add_to_cart_item')>=0){
						$('#rzvy_package_reset').val('N');
						rzvy_package_cart_func();
					}		
					if(rzvy_currajax_xhrdata.indexOf('front_login')>=0 || rzvy_currajax_xhrdata.indexOf('front_logout')>=0){
						$('#rzvy_package_reset').val('Y');
						rzvy_list_packages_func();
						rzvy_package_cart_func();
					}		
				}				
			}	
		}
	});
	
	/* Function Get Package Listing */
	function rzvy_list_packages_func(){
		var ajaxurl = generalObj.site_url;
		$.ajax({
			type: 'post',
			data: {
				'get_servicespackage_listing': 1
			},
			url: ajaxurl+"addons/services-package/lib/rzvy_services_package_front_ajax.php",
			success: function (res) {
				$(".rzvy_main_loader").addClass("rzvy_hide_loader");
				if(res!=""){	
					$(".rzvy_show_hide_services_package").show();
					$("#rzvy_services_package_content").show();
					$("#rzvy_services_package_content").html(res);
					$(".rzvy-package-credits-control-input").trigger('change');
					
				}
			}
		});
	}
	/* Function Reset Cart Packages System */
	function rzvy_package_cart_func(){
		if($(".rzvy-package-credits-control-input").prop("checked")){
			var package_credit_use = 'Y';
		}else{
			var package_credit_use = 'N';
		}
		
		var rzvy_package_reset = $('#rzvy_package_reset').val();
		var ajax_url = generalObj.ajax_url;
			$.ajax({
			type: 'post',
			data: {
				'package_credit_use': package_credit_use,
				'rzvy_package_reset': rzvy_package_reset,
			},
			url: ajax_url + "rzvy_front_cart_ajax.php",
			success: function (res) {
					$.ajax({
						type: 'post',
						async:true,
						data: {
							'user': $(".rzvy-user-selection:checked").val(),
							'use_lpoint': $(".rzvy-lpoint-control-input").prop("checked"),
							'payment_method': $(".rzvy-payment-method-check:checked").val(),
							'is_partial': $(".rzvy-partial-deposit-control-input").prop("checked"),
							'refresh_cart_sidebar': 1
						},
						url: ajax_url + "rzvy_front_cart_ajax.php",
						success: function (response) {
							$("#rzvy_refresh_cart").html(response);
							if($(".rzvy_cart_items_list li").length>0){
								$(".rzvy-frequently-discount-change:checked").trigger("click");
							}
							if($(".rzvy_cart_items_list_li").hasClass("rzvy_subtotal_exit")){
								$(".remove_payment_according_services_showhide").removeClass("rzvy_hide_show_payment_according_services");
							}else{
								$(".remove_payment_according_services_showhide").addClass("rzvy_hide_show_payment_according_services");
							}
							if($(".rzvy-partial-deposit-control-input").prop("checked")){
								if(Number($(".rzvy_cart_pd_amount").val())>0){
									$(".rzvy_update_partial_amount").html($(".rzvy_cart_pd_amount").val());
								}else{
									$(".rzvy_update_partial_amount").html("0");
								}
								$(".rzvy-cart-partial-deposite-main").show();
							}else{
								$(".rzvy_update_partial_amount").html("0");
								$(".rzvy-cart-partial-deposite-main").hide();
							}
							
							if($(".rzvy-lpoint-control-input").prop("checked")){
								$(".rzvy_update_lpoint_count").html($(".rzvy_cart_lp_amount").data("lpointtotal"));
								if(Number($(".rzvy_cart_lp_amount").val())>0){
									$(".rzvy_update_lpoint_amount").html($(".rzvy_cart_lp_amount").val());
								}else{
									$(".rzvy_update_lpoint_amount").html("0");
								}
								$(".rzvy-cart-lpoint-main").show();
							}else{
								$(".rzvy_update_lpoint_count").html("0");
								$(".rzvy_update_lpoint_amount").html("0");
								$(".rzvy-cart-lpoint-main").hide();
							}
							rzvy_payment_method_refresh_func();
						}
					});
				}
			});
	}
	/* Function Payment Methods Refresh */
	function rzvy_payment_method_refresh_func(){
		var ajax_url = generalObj.ajax_url;
		$(".rzvy-card-detail-box").slideUp(1000);
		$(".rzvy-bank-transfer-detail-box").slideUp(1000);
		$.ajax({
			type: 'post',
			async:true,
			data: {
				'get_payment_methods': 1
			},
			url: ajax_url + "rzvy_front_cart_ajax.php",
			success: function (response) {
				$(".rzvy_payment_methods_container").html(response);
			}
		});
	}	
	/** List Plans If Registered User Type **/
	$(document).on("change", ".rzvy-user-selection", function(){
		$(".rzvy_show_hide_services_package").hide();
		var rzvy_packages  = $("#rzvy_services_package_content").html();
		$('#rzvy_package_reset').val('N');
		if(($(this).attr("id") == "rzvy-new-user" || $(this).attr("id") == "rzvy-existing-user") && rzvy_packages==''){
			rzvy_list_packages_func();
		}else{
			if(rzvy_packages!='' && ($(this).attr("id") == "rzvy-guest-user" || $(this).attr("id") == "rzvy-user-forget-password")){
				$('#rzvy_package_reset').val('Y');
				$(".rzvy-package-credits-control-input").trigger('change');
				$(".rzvy-servicespackage-card-singleqty-unit-selection").each(function(){
					$(this).removeClass('list_active');
				});
				$(".rzvy_show_hide_services_package").hide();
				$("#rzvy_services_package_content").html('');
			}
		}
	});
	
	/** JS to add package into cart **/
	$(document).on("click", ".rzvy-servicespackage-card-singleqty-unit-selection", function(){
		$(".rzvy-package-credits-control-input").trigger('change');
		var id = $(this).data("id");
		var check = $("#rzvy-servicespackage-card-singleqty-unit-"+id).hasClass("list_active");
		var ajax_url = generalObj.ajax_url;
		if(!check){
			$("#rzvy-servicespackage-card-singleqty-unit-"+id).addClass("list_active");
			var qty = 1;
		}else{
			$("#rzvy-servicespackage-card-singleqty-unit-"+id).removeClass("list_active");
			var qty = 0;
		}
		$.ajax({
			type: 'post',
			data: {
				'id': id,
				'qty': qty,
				'add_to_cart_package': 1
			},
			url: ajax_url + "rzvy_front_cart_ajax.php",
			success: function (res) {
				if(qty==0){
					$("#rzvy-servicespackage-card-singleqty-unit-"+id).removeClass("list_active");
				}
				rzvy_package_cart_func();
			}
		});
	});
	
	/** JS to remove package item from cart **/
	$(document).on("click", ".rzvy_remove_servicespackage_from_cart", function(){
		var id = $(this).data("id");
		var ajax_url = generalObj.ajax_url;
		var qty = 0;
		$.ajax({
			type: 'post',
			data: {
				'id': id,
				'qty': qty,
				'add_to_cart_package': 1
			},
			url: ajax_url + "rzvy_front_cart_ajax.php",
			success: function (res) {
			
				$(".rzvy-package-credits-control-input").trigger('change');
				$.ajax({
					type: 'post',
					async:true,
					data: {
						'user': $(".rzvy-user-selection:checked").val(),
						'use_lpoint': $(".rzvy-lpoint-control-input").prop("checked"),
						'payment_method': $(".rzvy-payment-method-check:checked").val(),
						'is_partial': $(".rzvy-partial-deposit-control-input").prop("checked"),
						'refresh_cart_sidebar': 1
					},
					url: ajax_url + "rzvy_front_cart_ajax.php",
					success: function (response) {
						$("#rzvy_refresh_cart").html(response);
						$("#rzvy-servicespackage-card-singleqty-unit-"+id).removeClass("list_active");
						$('.rzvy-servicespackage-card-multipleqty-unit-'+id).val(qty);
						$('.rzvy-servicespackage-card-multipleqty-unit-selection-'+id).removeClass("list_active");
						if($(".rzvy_cart_items_list_li").hasClass("rzvy_subtotal_exit")){
							$(".remove_payment_according_services_showhide").removeClass("rzvy_hide_show_payment_according_services");
						}else{
							$(".remove_payment_according_services_showhide").addClass("rzvy_hide_show_payment_according_services");
						}
						if($(".rzvy-partial-deposit-control-input").prop("checked")){
							if(Number($(".rzvy_cart_pd_amount").val())>0){
								$(".rzvy_update_partial_amount").html($(".rzvy_cart_pd_amount").val());
							}else{
								$(".rzvy_update_partial_amount").html("0");
							}
							$(".rzvy-cart-partial-deposite-main").show();
						}else{
							$(".rzvy_update_partial_amount").html("0");
							$(".rzvy-cart-partial-deposite-main").hide();
						}
						
						if($(".rzvy-lpoint-control-input").prop("checked")){
							$(".rzvy_update_lpoint_count").html($(".rzvy_cart_lp_amount").data("lpointtotal"));
							if(Number($(".rzvy_cart_lp_amount").val())>0){
								$(".rzvy_update_lpoint_amount").html($(".rzvy_cart_lp_amount").val());
							}else{
								$(".rzvy_update_lpoint_amount").html("0");
							}
							$(".rzvy-cart-lpoint-main").show();
						}else{
							$(".rzvy_update_lpoint_count").html("0");
							$(".rzvy_update_lpoint_amount").html("0");
							$(".rzvy-cart-lpoint-main").hide();
						}
						rzvy_payment_method_refresh_func();
					}
				});
			}
		});
	});
	/** Package Credit Use Check **/
	$(document).on("change",".rzvy-package-credits-control-input", function(){
		rzvy_package_cart_func();
	});
		
})(jQuery);