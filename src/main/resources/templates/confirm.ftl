<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
        <link rel = "stylesheet" type="text/css" href = "css/confirm.css">
    <title>Confirm!</title>
  </head>


  <body>

  <div class = "header">
    <h1> Ihre Bestellung </h1>
  </div>
  <div class = "confirmation">
  <table>
       <tr>
         <th> Name </th>
         <th> Amount </th>
         <th> Preis </th>
       </tr>
       <script>
       </script>

      <#list pizzaOrders as pizzaOrder>
      <script>
      i += ${pizzaOrder.amount};
      </script>
        <tr>
          <td> ${pizzaOrder.pizza.name} </td>
          <td> ${pizzaOrder.amount} </td>
          <td> ${(pizzaOrder.pizza.price * pizzaOrder.amount)} CHF </td>
        </tr>
      </#list>

      <tr>
         <td> Total </td>
         <td id = "i"> <script> document.getElementById("i").innerHTML = i; </script> </td>
         <td><span> ${order.total} CHF </span> </td>
      </tr>
      </table>
      <br>
      <p><a href = "order">Mehr Pizzas bestellen!</a></p>
      <div class = "addresAndPhone">
      ${order.address},
      ${order.phone}

      </div>
   </div>
</div>


  </body>
</html>
