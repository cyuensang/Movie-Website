//callback function
$(document).ready(function()
{
	function loginResult(resultString)
	{
		resultData = JSON.parse(resultString);
		
		if(resultData["success"]=="true")
		{
			console.log("success");
			location.replace("_dashboard.jsp");
		}
		else
		{
			
			jQuery("#login-error").text("Invalid login details. Please try again");
		}
	}
	
	function submitLoginForm(formSubmitEvent)
	{
		console.log("Employee Submit Login Form");
		formSubmitEvent.preventDefault();
		jQuery.post(
				"/Fabflix_default/Dashboard",
				jQuery("#emp_login_form").serialize(),
				(resultDataString) => loginResult(resultDataString));
	}
	
	jQuery("#emp_login_form").submit((event)=>submitLoginForm(event));

});