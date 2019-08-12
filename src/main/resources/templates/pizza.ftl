<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Karte</title>
    <link rel = "stylesheet" type="text/css" href = "css/pizza.css">
  </head>
  <body>
    <div class = "header">
      <a href = "welcome">Home</a>
      <a>|</a>
      <a href = "pizza">Karte</a>
      <a>|</a>
      <a href = "order">Bestellung</a>
    </div>

<table>
      <tr>
        <th> Name </th>
        <th> Preis </th>
      </tr>

      <#list pizzas as pizza>
      <tr>
        <td> ${pizza.name}</td>
        <td> ${pizza.price} CHF</td>
      </tr>
    </#list>

    </table>
  </body>
</html>
