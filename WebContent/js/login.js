//callback function
$(document).ready(function()
{
function loginResult(resultString)
{
	resultData = JSON.parse(resultString);
	
	if(resultData["success"]=="true")
	{
		console.log("success");
		location.replace("index.jsp");
	}
	else
	{
		
		jQuery("#login-error").text("Invalid login details. Please try again");
	}
}
function submitLoginForm(formSubmitEvent)
{
	console.log("Im here");
	formSubmitEvent.preventDefault();
	jQuery.post(
			"/Fabflix_default/Login",
			jQuery("#login_form").serialize(),
			(resultDataString) => loginResult(resultDataString));
			
	
	
}

jQuery("#login_form").submit((event)=>submitLoginForm(event));

});