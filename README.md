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
 Для импорта рейтинга нужно будет зайти на страницу списка просмотренных фильмов (если такого списка нет, то нужно добавить и импортировать в него фильмы с рейтингом) и запустить из адресной строки [этот скрипт](javascript:class Kinopoisk2IMDb{constructor(){this.timeout=1e3,this.headers={accept:"*/*","accept-language":"en-US,en;q=0.9,ru;q=0.8,uk;q=0.7","cache-control":"no-cache","content-type":"application/x-www-form-urlencoded",pragma:"no-cache","x-requested-with":"XMLHttpRequest"},this.inputId="K2IMDbRatings",this.ratingSelector=".ipl-rating-selector__fieldset",this.success=0,this.skipped=0,this.error=0,this.progress=0,this.listId=document.querySelector('meta[property="pageId"]').getAttribute("content");const t=document.querySelector(".lister-total-num-results");this.total=+t.textContent.match(/\d(,\d+)?/)[0].replace(",",""),this.pagesCount=Math.ceil(this.total/100),this.createButton()}createButton(){const t=document.createElement("div");t.setAttribute("class","aux-content-widget-2"),t.innerHTML="<h3>K2IMDb ratings file</h3> \n"+`<input type='file' id='${this.inputId}' accept='application/json' />\n`+`<progress value="0" max="${this.total}" style='width: 100%'>0 %</progress>`,this.input=t.querySelector("#"+this.inputId),this.progressEl=t.querySelector("progress"),this.input.addEventListener("change",this.onFileSet.bind(this)),document.querySelector("#sidebar").insertBefore(t,document.querySelector(".list-create-widget"))}onFileSet(){const t=this.input.files[0];if(t){const e=new FileReader;e.readAsText(t,"UTF-8"),e.onload=(t=>{try{this.db=JSON.parse(t.target.result)}catch(t){this.showErrorMessage("File content is corrupted or not in JSON format")}if(confirm("Start import process?")){const t=document.querySelectorAll(".lister-list > .lister-item");this.setRatings(t),this.pagesCount>1&&this.getPages()}}),e.onerror=(t=>{this.showErrorMessage("Error reading file")})}else this.showErrorMessage("File is absent")}getPages(){this.callInALoop(2,this.pagesCount,t=>(this.getPage(t++).then(t=>t.text()).then(t=>{document.createElement("div").innerHTML=t;try{const t=parent.querySelectorAll(".lister-list > .lister-item");this.setRatings(t)}catch(t){console.error(t)}}).catch(e=>{let s=100;t===this.pagesCount&&this.total%100&&(s=this.total%100),this.error+=s,this.addError(t,"was not able to load page"),this.updateProgress()}),t<=this.pagesCount))}getPage(t){return fetch(`https://www.imdb.com/list/${this.listId}/_ajax?st_dt=&mode=detail&page=${t}&sort=list_order,asc`,{credentials:"include",headers:this.headers,referrer:`https://www.imdb.com/list/${this.listId}/?st_dt=&mode=detail&page=${t-1||1}&sort=list_order,asc`,referrerPolicy:"no-referrer-when-downgrade",body:null,method:"GET",mode:"cors"})}setRatings(t){this.callInALoop(0,t.length,e=>{const s=t[e],r=s.querySelector(".lister-item-image").getAttribute("data-tconst");if(!s.querySelector(this.ratingSelector))return this.error++,this.addError(r,"hasn't rating control"),this.updateProgress(),!0;if("0"===this.getValue(s,"rating")){const t=this.getValue(s,"auth");this.db[r]?this.setRating(r,t,this.db[r]):(this.error++,this.addError(r,"missed in our DB"),this.updateProgress())}else this.skipped++,this.updateProgress();return e<t.length-1})}setRating(t,e,s){return this.setRatingAJAX(t,e,s).then(t=>200===t.status?t.headers.get("content-type").includes("application/json")?t.json():{status:200}:{status:t.status}).then(e=>{200===e.status?(this.success++,this.updateProgress()):(this.error++,this.addError(t,"Failed to update rating with "+res.status),this.updateProgress())}).catch(e=>{this.error++,this.addError(t,"Failed to update rating with ",e.message),this.updateProgress()})}setRatingAJAX(t,e,s){return fetch("https://www.imdb.com/ratings/_ajax/title",{credentials:"include",headers:this.headers,referrer:`https://www.imdb.com/list/${this.listId}/`,referrerPolicy:"no-referrer-when-downgrade",body:`tconst=${t}&rating=${s}&auth=${e}&pageType=[object HTMLInputElement]&subpageType=[object HTMLInputElement]&tracking_tag=ur_rt`,method:"POST",mode:"cors"})}updateProgress(){const t=this.success+this.skipped+this.error;this.progressEl.setAttribute("value",t),this.progressEl.innerText=(this.total/t).toFixed(3)+" %",t>=this.total&&this.showSuccessMessage("Import is completed. Results are: \nsuccess: "+this.success+"\nskipped: "+this.skipped+"\nerror: "+this.error+"\n\nCheck the console for details.")}getValue(t,e){return t.querySelector(`[name="${e}"]`).getAttribute("value")}addError(t,e){console.log(`%c ${t}: ${e}`,"color: #d00")}showErrorMessage(t){alert(t)}showSuccessMessage(t){alert(t)}callInALoop(t,e,s){setTimeout(()=>{s(t++)&&this.callInALoop(t,e,s)},this.timeout)}}/^\/list.*$/.test(document.location.pathname)?window.k2imdb=new Kinopoisk2IMDb:alert("This script is supposed to be run on a list page");)
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
