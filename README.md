<h1 align="center">java-filmorate 🎥</h1>
<h3 align="center">приложение для оценки фильмов</h3>
<hr>

[//]: # (--------------------------------------------)
<h2>🟥 функционал: </h2>

<h3>👤 пользователи</h3>

<ul>
    <li>создание/обновление/удаление</li>
    <li>выдача одного/всех пользовател(я/ей)</li>
    <p></p>
    <details>
      <summary>друзья пользователя</summary>
        <ul>
        <li>добавление/удаление</li>
        <li>выдача всех друзей</li>
        <li>выдача общих друзей с другим пользователем</li>
        </ul>
    </details>
</ul>
<hr>

[//]: # (--------------------------------------------)
<h3>🎞️ фильмы</h3>
<li>создание/обновление/удаление</li>
<li>выдача одного/всех фильм(а/ов)</li>
<p></p>

<ul>
    <details>
      <summary>лайки фильма</summary>
        <ul>
        <li>добавление/удаление</li>
        </ul>
    </details>
    <details>
      <summary>популярность фильма</summary>
        <ul>
        <li>выдача популярн(ого/ых) фильм(а/ов)</li>
        </ul>
    </details>
    <details>
      <summary>рейтинги фильма</summary>
        <ul>
        <li>выдача одного/всех рейтинг(а/ов)</li>
        </ul>
    </details>
    <details>
      <summary>жанры фильма</summary>
        <ul>
        <li>выдача одного/всех жанр(а/ов)</li>
        </ul>
    </details>
</ul>
<hr>

[//]: # (---------------------------------------------------------------------------)
<h2>🟥 взаимодействие: </h2>

<h3>🟣 API-запросы</h3>
<h4>👤 пользователи</h4>
<details>
    <summary>создание/обновление/удаление</summary>
    <ul>
        <li style="list-style-position: inside;">в теле запроса передается готовый json</li>
        <ul>
            <li style="list-style-position: inside;">создание:</li>
            <code>POST/users</code>
            <li style="list-style-position: inside;">обновление:</li>
            <code>PUT/users</code>
        </ul>
        <hr>
        <li style="list-style-position: inside;">в строке запроса передается переменная</li>
        <ul>
            <li style="list-style-position: inside;">удаление:</li>
            <code>DELETE/users/:id</code>
        </ul>
    </ul>
</details>

<details>
    <summary>выдача одного/всех пользовател(я/ей)</summary>
    <ul>
        <li style="list-style-position: inside;">только строка запроса</li>
        <ul>
            <li style="list-style-position: inside;">выдача всех:</li>
            <code>GET/users</code>
        </ul>
        <hr>
        <li style="list-style-position: inside;">в строке запроса передается переменная</li>
        <ul>
            <li style="list-style-position: inside;">выдача одного:</li>
            <code>GET/users/:id</code>
        </ul>
    </ul>
</details>

<ul>
    <h5>друзья пользователя</h5>
    <details>
        <summary>добавление/удаление друга</summary>
        <ul>
            <li style="list-style-position: inside;">в строке запроса передаются переменные</li>
            <ul>
                <li style="list-style-position: inside;">добавление:</li>
                <code>POST/users/:id/friends/:friendId</code>
                <li style="list-style-position: inside;">удаление:</li>
                <code>DELETE/users/:id/friends/:friendId</code>
            </ul>
        </ul>
    </details>
    <details>
        <summary>выдача всех друзей пользователя</summary>
        <ul>
            <li style="list-style-position: inside;">в строке запроса передается переменная</li>
            <ul>
                <li style="list-style-position: inside;"></li>
                <code>GET/users/:id/friends</code>
            </ul>
        </ul>
    </details>
    <details>
        <summary>выдача общих друзей с другим пользователем</summary>
        <ul>
            <li style="list-style-position: inside;">в строке запроса передаются переменные</li>
            <ul>
                <li style="list-style-position: inside;"></li>
                <code>GET/users/:id/friends/common/:otherId</code>
            </ul>
        </ul>
    </details>
</ul>
<hr>

<h4>🎞️ фильмы</h4>
<details>
    <summary>создание/обновление/удаление</summary>
    <ul>
        <li style="list-style-position: inside;">в теле запроса передается готовый json</li>
        <ul>
            <li style="list-style-position: inside;">создание:</li>
            <code>POST/films</code>
            <li style="list-style-position: inside;">обновление:</li>
            <code>PUT/films</code>
        </ul>
        <hr>
        <li style="list-style-position: inside;">в строке запроса передается переменная</li>
        <ul>
            <li style="list-style-position: inside;">удаление:</li>
            <code>DELETE/films/:id</code>
        </ul>
    </ul>
</details>

<details>
    <summary>выдача одного/всех фильм(а/ов)</summary>
    <ul>
        <li style="list-style-position: inside;">только строка запроса</li>
        <ul>
            <li style="list-style-position: inside;">выдача всех:</li>
            <code>GET/films</code>
        </ul>
        <hr>
        <li style="list-style-position: inside;">в строке запроса передается переменная</li>
        <ul>
            <li style="list-style-position: inside;">выдача одного:</li>
            <code>GET/films/:id</code>
        </ul>
    </ul>
</details>

<ul>
    <h5>лайки фильма</h5>
    <details>
        <summary>добавление/удаление</summary>
        <ul>
            <li style="list-style-position: inside;">в строке запроса передаются переменные</li>
            <ul>
                <li style="list-style-position: inside;">добавление:</li>
                <code>PUT/films/:id/like/:userId</code>
                <li style="list-style-position: inside;">удаление:</li>
                <code>DELETE/films/:id/like/:userId</code>
            </ul>
        </ul>
    </details>
    <h5>популярность фильма</h5>
    <details>
        <summary>выдача популярн(ого/ых) фильм(а/ов)</summary>
        <ul>
            <li style="list-style-position: inside;">варианты запроса</li>
            <ul>
                <li style="list-style-position: inside;">только строка запроса</li>
                <code>GET/films/popular</code>
                <li style="list-style-position: inside;">в строке запроса передается параметр</li>
                <code>GET/films/popular?count=1</code>
            </ul>
        </ul>
    </details>
    <h5>рейтинги фильма</h5>
    <details>
        <summary>выдача одного/всех рейтинг(а/ов)</summary>
        <ul>
            <li style="list-style-position: inside;">только строка запроса</li>
            <ul>
                <li style="list-style-position: inside;">выдача всех:</li>
                <code>GET/mpa</code>
            </ul>
            <hr>
            <li style="list-style-position: inside;">в строке запроса передается переменная</li>
            <ul>
                <li style="list-style-position: inside;">выдача одного:</li>
                <code>GET/mpa/:id</code>
            </ul>
        </ul>
    </details>
    <h5>жанры фильма</h5>
    <details>
        <summary>выдача одного/всех жанр(а/ов)</summary>
        <ul>
            <li style="list-style-position: inside;">только строка запроса</li>
            <ul>
                <li style="list-style-position: inside;">выдача всех:</li>
                <code>GET/genres</code>
            </ul>
            <hr>
            <li style="list-style-position: inside;">в строке запроса передается переменная</li>
            <ul>
                <li style="list-style-position: inside;">выдача одного:</li>
                <code>GET/genres/:id</code>
            </ul>
        </ul>
    </details>

</ul>
<hr>

[//]: # (--------------------------------------------)
<h3>==========================================</h3>
<h3 align="center">схема базы данных</h3>
<img src="src/main/resources/templates/схема базы данных.png" alt="схема базы данных" align="center">
<h3>==========================================</h3>