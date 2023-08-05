<h1 align="center">проект java-filmorate</h1>
<h3 align="center">ТЕСТОВАЯ схема базы данных</h3>
<img src="src/main/resources/templates/схема базы данных.png" alt="схема базы данных" align="center">
<h3>==========================================</h3>
<h3>примеры запросов для приложения:</h3>

<h5>взаимодействие с User: /users</h5>
<li>создать: POST</li>
<li>обновить: PUT</li>
<li>выдать всех пользователей: GET</li>
<li>выдать пользователя по id: GET /:id</li>
<p>------------------------------------------------------------</p>
<li>добавить дргуа в друзья по id друга1 и друга2: PUT /:id/friends/:friendId</li>
<li>выдать друзей пользователя по его id: GET /:id/friends</li>
<li>выдать общих друзей пользователя1 и пользователя2 по их id: GET /:id/friends/common/:otherId</li>
<li>удалить друга2 у пользователя1 по их id: DELETE /:id/friends/:friendId</li>

<p>======================================================</p>

<h5>взаимодействие с Film: /films</h5>
<li>создать: POST</li>
<li>обновить: PUT</li>
<li>выдать все фильмы: GET</li>
<li>выдать популярные фильмы: GET /popular или /popular?count=1</li>
<li>выдать фильм по id: GET /:id</li>
<p>------------------------------------------------------------</p>
<li>поставить лайк от пользователя по id фильма и id пользователя: PUT /:id/like/:userId</li>
<li>удалить лайк от пользователя по id фильма и id пользователя: DELETE /:id/like/:userId</li>
