function checkoutResult(resultString)
{
	resultData = JSON.parse(resultString);
	
	if(resultData["success"]=="true")
	{
		alert("Success!")
		window.location.replace("/Fabflix_default/success_buy.jsp");
	}
	else
	{
		
		jQuery("#info-error").text("Invalid info. Please try again");
	}
}

function submitCheckoutForm(formSubmitEvent)
{
	console.log("checking out");
	formSubmitEvent.preventDefault();
	jQuery.post(
			"/Fabflix_default/Checkout",
			jQuery("#info_form").serialize(),
			(resultDataString) => checkoutResult(resultDataString));
			
	
	
}

jQuery("#info_form").submit((event)=>submitCheckoutForm(event));