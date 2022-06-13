$(document).ready(function() {
  var rzvy_user_type =  $('input[name="rzvy-user-selection"]:checked').val();
  if(rzvy_user_type!=undefined || rzvy_user_type!=null || rzvy_user_type!=''){
	var dataToggle = 'rzvy-existing-user';
	$('.rzvy_referral_code_divf').slideDown(1000);
	if(rzvy_user_type=='gc'){
		$('.rzvy_referral_code_divf').slideUp(1000);
		dataToggle = 'rzvy-guest-user';
	}else if(rzvy_user_type=='nc'){
		dataToggle = 'rzvy-new-user';
	}else if(rzvy_user_type=='fp'){
		dataToggle = 'rzvy-user-forget-password';
	}else if($('.rzvy_loggedin_name').text()!=undefined && $('.rzvy_loggedin_name').text()!=null && $('.rzvy_loggedin_name').text()!=''){
		dataToggle = 'rzvy-new-user';
	}
    var $dataForm = $('[data-form="'+dataToggle+'"]');
    $dataForm.slideDown();
    $('[data-form]').not($dataForm).slideUp();  
  }
  if($('input[name="rzvy-user-selection"]').length<=2){
	  $('.rzvy-users-selection-div').hide();
  }
  
  $('input[name="rzvy-user-selection"]:checked').trigger('change');

  /* Parent category onchange */
  $(document).on('change','.rzvy-pcategories-selection',function () {
    $('.rzvy-pcategory-container .owl-stage-outer .owl-item').each(function(){
		$(this).removeClass('selected');
	}); 
    $(this).parent().parent().parent().parent().toggleClass('selected');
  });
  /* Categories onchange */
  $(document).on('change','.rzvy-categories-radio-change',function () {
    $('#rzvy_categories_html_content .owl-stage-outer .owl-item').each(function(){
		$(this).removeClass('selected');
	}); 
    $(this).parent().parent().parent().parent().toggleClass('selected');
  });
  /* Service onchange */
  $(document).on('change','.rzvy-services-radio-change',function () {
	$('#rzvy_services_html_content .owl-stage-outer .owl-item').each(function(){
		$(this).removeClass('selected');
	});      
    $(this).parent().parent().parent().parent().toggleClass('selected');
  });
  /* Addon Multi Qty onchange */
  $(document).on('change','.rzvy_addons_mltqty_cb',function (){
	var rzvy_addonid = $(this).data('aid');
    $(this).parent().parent().parent().parent().toggleClass('selected');
	if($(this).parent().parent().parent().parent().hasClass('selected')){
		$('#rzvy-addon-card-multipleqty-plus-js-counter-btn-'+rzvy_addonid).trigger('click');
	}else{
		$('#rzvy-addon-card-multipleqty-minus-js-counter-btn-'+rzvy_addonid).trigger('click');
	}
  });
  /* Packages onchange */
  $(document).on('change','.rzvy-servicespackage-card-singleqty-unit-selection',function () {
	$('#rzvy_services_html_content .owl-stage-outer .owl-item').each(function(){
		$(this).removeClass('selected');
	});      
    $(this).parent().parent().parent().parent().toggleClass('selected');
  });
  /* Addon Single Qty onchange */
  $(document).on('change','.rzvy-addon-card-singleqty-unit-selection',function (){
    $(this).parent().parent().parent().parent().toggleClass('selected');
  });
  /* Frequently Discount On Change */
  $(document).on('change','.form-check.custom input',function (){
    $(this).parent().parent().siblings().removeClass('selected');
    $(this).parent().parent().toggleClass('selected');
  });
  
  /* User types form change */
  $(document).on('click','[data-toggle]',function () {
    var dataToggle = $('[data-toggle]:checked').prop('id') || '';
    var $dataForm = $('[data-form="'+dataToggle+'"]');
    $dataForm.slideDown();
    $('[data-form]').not($dataForm).slideUp();
	
	$('.rzvy_referral_code_divf').slideDown(1000);
	if(dataToggle=='rzvy-guest-user'){
		$('.rzvy_referral_code_divf').slideUp(1000);
	}	
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
	
	
  $('.addi-services figure input').on('click', function() {
    $(this).parents('.owl-item').toggleClass('selected');
  });
  $('.rzvy-review').on('click', function() {
    $(this).toggleClass('open');
  });
  $('.service-types figure').on('click', function() {
    $('.services-item').slideDown(1000);
  });
  $('.services figure').on('click', function() {
    $('.addi-services-item').slideDown(1000);
  });
  $('.services.team figure').on('click', function() {
    $('.xdsoft_datetimepicker.xdsoft_inline').slideDown(1000);
  });
  $('.hamburger').on('click', function() {
    $(this).toggleClass('active');
  });

  //Rzvy Onclick Stop Propagation Script
  $('.review-modal').on('click', function(e) {
    event.stopPropagation();
  });

  

  //Rzvy Number Input Modify Script
  $('.quantity').each(function() {
    var spinner = jQuery(this),
    input = spinner.find('input[type="number"]'),
    btnUp = spinner.find('.up'),
    btnDown = spinner.find('.down'),
    min = input.attr('min'),
    max = input.attr('max');
    btnUp.click(function() {
      var oldValue = parseFloat(input.val());
      if (oldValue >= max) {
        var newVal = oldValue;
      }
      else {
        var newVal = oldValue + 1;
      }
      spinner.find('input').val(newVal);
      spinner.find('input').trigger('change');
    });
    btnDown.click(function() {
      var oldValue = parseFloat(input.val());
      if (oldValue <= min) {
        var newVal = oldValue;
      }
      else {
        var newVal = oldValue - 1;
      }
      spinner.find('input').val(newVal);
      spinner.find('input').trigger('change');
    });
  }); 

  /* //Rzvy Theia Sticky Sidebar Script*/
  var formOfset = $('.form-style-two .rzvy-rightbar-heightcal');
  var bottom = formOfset.position().top + formOfset.outerHeight()-10;
  $(window).scroll(function(event) {
    var scroll = $(this).scrollTop();
    if (scroll >= bottom) {
      $('.rzvy-sticky').addClass('sticky');
    }
    else {
      $('.rzvy-sticky').removeClass('sticky');
    }
  }); 
});

//Rzvy Get Inline CSS By Element Height Script
$(window).on('resize', function () {
  $('.review-modal form').css('max-height', $(window).height()-230);
});
$(window).trigger('resize');



$(document).ajaxComplete(function(){
	//Rzvy Owl Carousel Slider Script
  $('.owl-carousel').each(function() {
    var $carousel = $(this);
    var $items = ($carousel.data('items') !== undefined) ? $carousel.data('items') : 1;
    var $items_lg = ($carousel.data('items-lg') !== undefined) ? $carousel.data('items-lg') : 1;
    var $items_md = ($carousel.data('items-md') !== undefined) ? $carousel.data('items-md') : 1;
    var $items_sm = ($carousel.data('items-sm') !== undefined) ? $carousel.data('items-sm') : 1;
    var $items_ssm = ($carousel.data('items-ssm') !== undefined) ? $carousel.data('items-ssm') : 1;
    $carousel.owlCarousel ({
      loop : ($carousel.data('loop') !== undefined) ? $carousel.data('loop') : true,
      items : $carousel.data('items'),
      margin : ($carousel.data('margin') !== undefined) ? $carousel.data('margin') : 0,
      dots : ($carousel.data('dots') !== undefined) ? $carousel.data('dots') : true,
      nav : ($carousel.data('nav') !== undefined) ? $carousel.data('nav') : false,
      navText : ["<div class='slider-no-current'><span class='current-no'></span><span class='total-no'></span></div><span class='current-monials'></span>", "<div class='slider-no-next'></div><span class='next-monials'></span>"],
      autoplay : ($carousel.data('autoplay') !== undefined) ? $carousel.data('autoplay') : false,
      autoplayTimeout : ($carousel.data('autoplay-timeout') !== undefined) ? $carousel.data('autoplay-timeout') : 5000000,
      animateIn : ($carousel.data('animatein') !== undefined) ? $carousel.data('animatein') : false,
      animateOut : ($carousel.data('animateout') !== undefined) ? $carousel.data('animateout') : false,
      mouseDrag : ($carousel.data('mouse-drag') !== undefined) ? $carousel.data('mouse-drag') : true,
      autoWidth : ($carousel.data('auto-width') !== undefined) ? $carousel.data('auto-width') : false,
      autoHeight : ($carousel.data('auto-height') !== undefined) ? $carousel.data('auto-height') : false,
      center : ($carousel.data('center') !== undefined) ? $carousel.data('center') : false,
      responsiveClass: true,
      dotsEachNumber: true,
      smartSpeed: 600,
      autoplayHoverPause: true,
      responsive : {
        0 : {
          items : $items_ssm,
        },
        480 : {
          items : $items_sm,
        },
        768 : {
          items : $items_md,
        },
        992 : {
          items : $items_lg,
        },
        1200 : {
          items : $items,
        }
      }
    });
    var totLength = $('.owl-dot', $carousel).length;
    $('.total-no', $carousel).html(totLength);
    $('.current-no', $carousel).html(totLength);
    $carousel.owlCarousel();
    $('.current-no', $carousel).html(1);
    $carousel.on('changed.owl.carousel', function(event) {
      var total_items = event.page.count;
      var currentNum = event.page.index + 1;
      $('.total-no', $carousel ).html(total_items);
      $('.current-no', $carousel).html(currentNum);
    });
  });
});