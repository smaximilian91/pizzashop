<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Bestellung</title>
    <link rel="stylesheet" type="text/css" href="css/order.css">
</head>

<body>
<div class="header">
    <a href="welcome">Home</a>
    <a>|</a>
    <a href="pizza">Karte</a>
    <a>|</a>
    <a href="order">Bestellung</a>
</div>
<br>
<div class="bestellung">
    <br>
    <div class="text"> Bestellen Sie!</div>
    <div class="pizzen">
        <form action="order" method="post">
            <#list pizzas as pizza>
                <select id="anzahl-${pizza.id}" name="anzahl-${pizza.id}">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
                ${pizza.name} ${pizza.price}
                <br>
            </#list>
            <div class="formular">
                <label>
                    Adresse
                    <input type="text" name="address" required/>
                </label>
                <br>
                <label>
                    Tel.
                    <input type="tel" name="phone" pattern="[0-9]{3} [0-9]{3} [0-9]{2} [0-9]{2}" required/><br>
                    <span>Format: 123 456 78 90</span>
                </label>
                <br>
                <div class="senden">
                    <input type="submit" name="send" value="Absenden">
                </div>
            </div>
        </form>
    </div>
</div>
</div>
</body>
</html>
