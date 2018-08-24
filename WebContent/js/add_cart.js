


function addToCart(movieId,title)
{
	
	jQuery.post
	(
			"/Fabflix_default/ShoppingCart",
			{"movieId":movieId,"title":title,"do":"add"},
			(result) => function(result){
				alert("added movie:"+movieId+" with title"+title)
			}
			
	);
	alert("added movie: "+movieId+" with title: "+title)
}

function remove_cart_item(movieId)
{
	jQuery.post
	(
			"/Fabflix_default/ShoppingCart",
			{"movieId":movieId,"do":"remove"},
			(result) => function(result){
				console.log("back from the grave");
				$("."+movieId+"-row").remove();
			}
			
	);
	$("#"+movieId+"-row").remove();
	if($('tr').length==2)
	{
		$("#checkout-btn").remove();
		$('#shopping-cart tbody:last-child').after('<tr><td>No items!</td></tr>');
	}
}
function update_cart_item(movieId)
{
	strQtyVal = $("#"+movieId+"-qty").val();
	
	if(isNaN(strQtyVal))
	{
		alert("Input is not a number!");
		return;
	}
	qtyVal = parseInt($("#"+movieId+"-qty").val());
	
	if(qtyVal<0)
	{
		alert("Invalid quantity value");
	}
	else if(qtyVal==0)
	{
		remove_cart_item(movieId);
	}
	else
	{
		jQuery.post
		(
			"/Fabflix_default/ShoppingCart",
			{"movieId":movieId,"do":"up","qty":$("#"+movieId+"-qty").val()},
			(result) => function(result){
				console.log("back from the grave");
				$("."+movieId+"-row").remove();
			}
			
		);
	}
	
}

function transfer_to_checkout()
{
	window.location.replace("/Fabflix_default/user_info.jsp");
	return;
}