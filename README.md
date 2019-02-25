# Kinopoisk2IMDB

* Программа для импорта оценок и списков просмотренных фильмов с Кинопоиска на IMDB.
* Для работы необходима Java 8, [Скачать можно тут](https://www.java.com/ru/download/)
* Подробные инструкции по работе читайте в пункте [Рекомендации по настройке](#Рекомендации-по-настройке).
* [Ссылка на скачивание последней версии](https://github.com/REDNBLACK/J-Kinopoisk2IMDB/releases/latest).

![Скриншот](https://cloud.githubusercontent.com/assets/1498939/14435615/74ab56f8-0021-11e6-9645-6d44a53ff63e.png "Screenshot")

## Рекомендации по настройке

### Основные (Здесь указаны самые необходимые опции для работы, остальное можно не читать)

#### Файл с кинопоиска:
 * [Ссылка на инструкцию с картинками](docs/ExportKinopoiskList.pdf)

#### Режим работы:
 * Хотите добавить фильмы в конкретный список на IMDB? `Добавить в список`
 * Нужно лишь импортировать оценки фильмов? Выбирайте `Выставить рейтинг`
 * Нужно выполнить все вышеуказанное? `Добавить в список и выставить рейтинг`

##### Импорт рейтинга:
 Импорт рейтинга доступен в полуавтоматическом режиме. Приложение после окончания работы сгенерирует `ratings.json` файл в папке приложения.
 Для импорта рейтинга нужно будет зайти на страницу списка просмотренных фильмов (если такого списка нет, то нужно добавить и импортировать в него фильмы с рейтингом) и запустить из адресной строки [этот скрипт](javascript:class%20Kinopoisk2IMDb%7Bconstructor()%7Bthis.timeout%3D1e3%2Cthis.headers%3D%7Baccept%3A%22*%2F*%22%2C%22accept-language%22%3A%22en-US%2Cen%3Bq%3D0.9%2Cru%3Bq%3D0.8%2Cuk%3Bq%3D0.7%22%2C%22cache-control%22%3A%22no-cache%22%2C%22content-type%22%3A%22application%2Fx-www-form-urlencoded%22%2Cpragma%3A%22no-cache%22%2C%22x-requested-with%22%3A%22XMLHttpRequest%22%7D%2Cthis.inputId%3D%22K2IMDbRatings%22%2Cthis.ratingSelector%3D%22.ipl-rating-selector__fieldset%22%2Cthis.success%3D0%2Cthis.skipped%3D0%2Cthis.error%3D0%2Cthis.progress%3D0%2Cthis.listId%3Ddocument.querySelector(%27meta%5Bproperty%3D%22pageId%22%5D%27).getAttribute(%22content%22)%3Bconst%20t%3Ddocument.querySelector(%22.lister-total-num-results%22)%3Bthis.total%3D%2Bt.textContent.match(%2F%5Cd(%2C%5Cd%2B)%3F%2F)%5B0%5D.replace(%22%2C%22%2C%22%22)%2Cthis.pagesCount%3DMath.ceil(this.total%2F100)%2Cthis.createButton()%7DcreateButton()%7Bconst%20t%3Ddocument.createElement(%22div%22)%3Bt.setAttribute(%22class%22%2C%22aux-content-widget-2%22)%2Ct.innerHTML%3D%22%3Ch3%3EK2IMDb%20ratings%20file%3C%2Fh3%3E%20%5Cn%22%2B%60%3Cinput%20type%3D%27file%27%20id%3D%27%24%7Bthis.inputId%7D%27%20accept%3D%27application%2Fjson%27%20%2F%3E%5Cn%60%2B%60%3Cprogress%20value%3D%220%22%20max%3D%22%24%7Bthis.total%7D%22%20style%3D%27width%3A%20100%25%27%3E0%20%25%3C%2Fprogress%3E%60%2Cthis.input%3Dt.querySelector(%22%23%22%2Bthis.inputId)%2Cthis.progressEl%3Dt.querySelector(%22progress%22)%2Cthis.input.addEventListener(%22change%22%2Cthis.onFileSet.bind(this))%2Cdocument.querySelector(%22%23sidebar%22).insertBefore(t%2Cdocument.querySelector(%22.list-create-widget%22))%7DonFileSet()%7Bconst%20t%3Dthis.input.files%5B0%5D%3Bif(t)%7Bconst%20e%3Dnew%20FileReader%3Be.readAsText(t%2C%22UTF-8%22)%2Ce.onload%3D(t%3D%3E%7Btry%7Bthis.db%3DJSON.parse(t.target.result)%7Dcatch(t)%7Bthis.showErrorMessage(%22File%20content%20is%20corrupted%20or%20not%20in%20JSON%20format%22)%7Dif(confirm(%22Start%20import%20process%3F%22))%7Bconst%20t%3Ddocument.querySelectorAll(%22.lister-list%20%3E%20.lister-item%22)%3Bthis.setRatings(t)%2Cthis.pagesCount%3E1%26%26this.getPages()%7D%7D)%2Ce.onerror%3D(t%3D%3E%7Bthis.showErrorMessage(%22Error%20reading%20file%22)%7D)%7Delse%20this.showErrorMessage(%22File%20is%20absent%22)%7DgetPages()%7Bthis.callInALoop(2%2Cthis.pagesCount%2Ct%3D%3E(this.getPage(t%2B%2B).then(t%3D%3Et.text()).then(t%3D%3E%7Bdocument.createElement(%22div%22).innerHTML%3Dt%3Btry%7Bconst%20t%3Dparent.querySelectorAll(%22.lister-list%20%3E%20.lister-item%22)%3Bthis.setRatings(t)%7Dcatch(t)%7Bconsole.error(t)%7D%7D).catch(e%3D%3E%7Blet%20s%3D100%3Bt%3D%3D%3Dthis.pagesCount%26%26this.total%25100%26%26(s%3Dthis.total%25100)%2Cthis.error%2B%3Ds%2Cthis.addError(t%2C%22was%20not%20able%20to%20load%20page%22)%2Cthis.updateProgress()%7D)%2Ct%3C%3Dthis.pagesCount))%7DgetPage(t)%7Breturn%20fetch(%60https%3A%2F%2Fwww.imdb.com%2Flist%2F%24%7Bthis.listId%7D%2F_ajax%3Fst_dt%3D%26mode%3Ddetail%26page%3D%24%7Bt%7D%26sort%3Dlist_order%2Casc%60%2C%7Bcredentials%3A%22include%22%2Cheaders%3Athis.headers%2Creferrer%3A%60https%3A%2F%2Fwww.imdb.com%2Flist%2F%24%7Bthis.listId%7D%2F%3Fst_dt%3D%26mode%3Ddetail%26page%3D%24%7Bt-1%7C%7C1%7D%26sort%3Dlist_order%2Casc%60%2CreferrerPolicy%3A%22no-referrer-when-downgrade%22%2Cbody%3Anull%2Cmethod%3A%22GET%22%2Cmode%3A%22cors%22%7D)%7DsetRatings(t)%7Bthis.callInALoop(0%2Ct.length%2Ce%3D%3E%7Bconst%20s%3Dt%5Be%5D%2Cr%3Ds.querySelector(%22.lister-item-image%22).getAttribute(%22data-tconst%22)%3Bif(!s.querySelector(this.ratingSelector))return%20this.error%2B%2B%2Cthis.addError(r%2C%22hasn%27t%20rating%20control%22)%2Cthis.updateProgress()%2C!0%3Bif(%220%22%3D%3D%3Dthis.getValue(s%2C%22rating%22))%7Bconst%20t%3Dthis.getValue(s%2C%22auth%22)%3Bthis.db%5Br%5D%3Fthis.setRating(r%2Ct%2Cthis.db%5Br%5D)%3A(this.error%2B%2B%2Cthis.addError(r%2C%22missed%20in%20our%20DB%22)%2Cthis.updateProgress())%7Delse%20this.skipped%2B%2B%2Cthis.updateProgress()%3Breturn%20e%3Ct.length-1%7D)%7DsetRating(t%2Ce%2Cs)%7Breturn%20this.setRatingAJAX(t%2Ce%2Cs).then(t%3D%3E200%3D%3D%3Dt.status%3Ft.headers.get(%22content-type%22).includes(%22application%2Fjson%22)%3Ft.json()%3A%7Bstatus%3A200%7D%3A%7Bstatus%3At.status%7D).then(e%3D%3E%7B200%3D%3D%3De.status%3F(this.success%2B%2B%2Cthis.updateProgress())%3A(this.error%2B%2B%2Cthis.addError(t%2C%22Failed%20to%20update%20rating%20with%20%22%2Bres.status)%2Cthis.updateProgress())%7D).catch(e%3D%3E%7Bthis.error%2B%2B%2Cthis.addError(t%2C%22Failed%20to%20update%20rating%20with%20%22%2Ce.message)%2Cthis.updateProgress()%7D)%7DsetRatingAJAX(t%2Ce%2Cs)%7Breturn%20fetch(%22https%3A%2F%2Fwww.imdb.com%2Fratings%2F_ajax%2Ftitle%22%2C%7Bcredentials%3A%22include%22%2Cheaders%3Athis.headers%2Creferrer%3A%60https%3A%2F%2Fwww.imdb.com%2Flist%2F%24%7Bthis.listId%7D%2F%60%2CreferrerPolicy%3A%22no-referrer-when-downgrade%22%2Cbody%3A%60tconst%3D%24%7Bt%7D%26rating%3D%24%7Bs%7D%26auth%3D%24%7Be%7D%26pageType%3D%5Bobject%20HTMLInputElement%5D%26subpageType%3D%5Bobject%20HTMLInputElement%5D%26tracking_tag%3Dur_rt%60%2Cmethod%3A%22POST%22%2Cmode%3A%22cors%22%7D)%7DupdateProgress()%7Bconst%20t%3Dthis.success%2Bthis.skipped%2Bthis.error%3Bthis.progressEl.setAttribute(%22value%22%2Ct)%2Cthis.progressEl.innerText%3D(this.total%2Ft).toFixed(3)%2B%22%20%25%22%2Ct%3E%3Dthis.total%26%26this.showSuccessMessage(%22Import%20is%20completed.%20Results%20are%3A%20%5Cnsuccess%3A%20%22%2Bthis.success%2B%22%5Cnskipped%3A%20%22%2Bthis.skipped%2B%22%5Cnerror%3A%20%22%2Bthis.error%2B%22%5Cn%5CnCheck%20the%20console%20for%20details.%22)%7DgetValue(t%2Ce)%7Breturn%20t.querySelector(%60%5Bname%3D%22%24%7Be%7D%22%5D%60).getAttribute(%22value%22)%7DaddError(t%2Ce)%7Bconsole.log(%60%25c%20%24%7Bt%7D%3A%20%24%7Be%7D%60%2C%22color%3A%20%23d00%22)%7DshowErrorMessage(t)%7Balert(t)%7DshowSuccessMessage(t)%7Balert(t)%7DcallInALoop(t%2Ce%2Cs)%7BsetTimeout(()%3D%3E%7Bs(t%2B%2B)%26%26this.callInALoop(t%2Ce%2Cs)%7D%2Cthis.timeout)%7D%7D%2F%5E%5C%2Flist.*%24%2F.test(document.location.pathname)%3Fwindow.k2imdb%3Dnew%20Kinopoisk2IMDb%3Aalert(%22This%20script%20is%20supposed%20to%20be%20run%20on%20a%20list%20page%22)%3B)
 Для этого нужно скопировать ссылку, вставить её в адресную строку и нажать `Enter`. Также можно создать новую закладку и запустить её по клику.
 В правой части экрана будет создан блок с возможностью загрузки файла - туда и нужно загрузить полученный `ratings.json`.
 Если по окончанию работы в консоли остались строки с ошибкой `hasn't rating control`, нужно изменить список [таким образом](docs/Titles-I-Didn't-Rate.gif), чтоб отображались только фильмы без рейтинга.
 После этого перезагрузить страницу и ещё раз запустить скрипт.

#### Настройки авторизации IMDB:
 * [Ссылка на инструкцию с картинками](docs/ObtainIMDBAuthString.pdf)
 * То же самое нужно проделать для полей `sid` и `session-id`

##### Пара ключ-значение в тестовом запросе:
 * Нужно открыть любой фильм на IMDb и добавить его вручную в любой список (кроме watchlist)
 * После этого открыть консоль разроботчика и найти там контрольную пару, как указано в [инструкции](docs/ObtainIMDBControlPair.png)
 
#### ID списка IMDB:
 * [Ссылка на инструкцию с картинками](docs/CreateListOrObtainListID.pdf)
 * Если вы хотите добавить фильмы в коллекцию `Watchlist`, то используйте имя `watchlist` в этом поле.


### Дополнительные

#### Тип запроса:
 * Большая часть фильмов у вас - англоязычные? Выбирайте `IMDB XML` или `OMDB API`.
 * Преимущественно аниме и иностранные? `IMDB JSON`.
 * В основном российские? `IMDB HTML` - ваш выбор.
 * Если всего поровну или вам лень что-либо настраивать, то оставьте как есть, то есть - `IMDB XML` + `IMDB JSON` + `IMDB HTML`
 * Если на выходе вы получаете низкий уровень успешно импортированных фильмов и много ошибок `IMDB ID is not set` стоит добавить `OMDB`.

#### OMDB API key:
 * Для использования базы OMDB вам понадобится ключ. Получить его можно тут: http://www.omdbapi.com/apikey.aspx (бесплатно до 1000 запросов в день)

#### Сравнение:
 * Если не хотите разбираться в тонкостях настройки или это ваш первый запуск, то оставьте как есть, а именно: `Год с отклонением`, `Тип фильма с полным совпадением` и `Интеллектуальное сравнение названий`.

#### Чистый запуск:
 * Включайте только если случайно удалили оценки/список на сайте IMDB, и хотите импортировать список с Кинопоиска заново.


## Подробное описание доступных опций

- `Режим работы` - Режим работы программы
    - (По умолчанию) `Добавить в список и выставить рейтинг` - выставить рейтинг фильмам и добавить их в список.
    - `Добавить в список` -только добавить фильмы в список.
    - `Выставить рейтинг` - только выставить рейтинг фильмам.

- `Сравнение` - каким способом сравнивать названия фильмов из таблицы Кинопоиска с найденными в IMDB.
    - (По умолчанию) `Год с отклонением` - сравнить год, используя отклонение в +/-1 год
    - `Год с полным совпадением` - проверить идентичность по годам.
    - (По умолчанию) `Тип фильма с полным совпадением` - индентичное сравнение типов фильмов (обычный/сериал/документальный/короткометражка)
    - `Любой тип фильма` - не сравнивать типы фильмов
    - (По умолчанию) `Интеллектуальное сравнение названий` - сравнить названия, используя уникальный алгоритм.
    - `Полное совпадение названий` - проверить на идентичность названий.
    - `Одно название начинается с другого` - сравнить находится ли название фильма из таблицы Кинопоиска в начале названия с IMDB.
    - `Одно название оканчивается другим` - сравнить находится ли название фильма из таблицы Кинопоиска в конце названия с IMDB.
    - `Одно название содержит другое` - сравнить находится ли название фильма из таблицы Кинопоиска в любой части названия с IMDB.
 
- `Тип запроса` - какой тип запроса использовать при поиске фильма в IMDB
    - (По умолчанию) `IMDB XML` - Наиболее точный тип - работает с 80% точностью, т.к. возвращает оригинальные названия фильмов, но не находит российские фильмы и плохо находит иностранные.
    - (По умолчанию) `IMDB JSON` - Запрос обрабатывается быстрее, но работает с точностью >60%, т.к. возвращает лишь локализованные (на английском) названия фильмов - идеально подходит для поиска аниме, не находит российские фильмы.
    - (По умолчанию) `IMDB HTML` - Низкая точность (~30%) - рекомендуется использовать только для импорта российских фильмов.
    - `OMDB API` - API сервиса, точность чуть ниже чем у `IMDB XML`, самая высокая скорость работы, но не ищет российские и иностранные фильмы.

## Расширенные настройки

- `user_agent` - User Agent браузера, который будет подставляться при каждом запросе импорта
    - (По умолчанию) `"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36"`.

- `year_deviation` - Отклонение года для опции Сравнения `Год с отклонением`
    - (По умолчанию) `1`    

- `timeout` - Таймаут попытки соединения для каждого запроса; можете увеличить, если у вас, к примеру, плоблемы с интернетом.
    - (По умолчанию) `3000`

- `log_level` - Уровень логирования приложения; измените на `debug`, если хотите отослать мне информацию об ошибках.
    - (По умолчанию) `info`
