class Kinopoisk2IMDb {
  constructor() {
    this.timeout = 1000;
    this.headers = {
      "accept": "*/*",
      "accept-language": "en-US,en;q=0.9,ru;q=0.8,uk;q=0.7",
      "cache-control": "no-cache",
      "content-type": "application/x-www-form-urlencoded",
      "pragma": "no-cache",
      "x-requested-with": "XMLHttpRequest"
    };

    this.inputId = "K2IMDbRatings";
    this.ratingSelector = '.ipl-rating-selector__fieldset'
    this.success = 0;
    this.skipped = 0;
    this.error = 0;
    this.progress = 0;
    this.listId = document.querySelector('meta[property="pageId"]').getAttribute('content');

    const listCountEl = document.querySelector('.lister-total-num-results')
    this.total = +listCountEl.textContent.match(/\d(,\d+)?/)[0].replace(',', '')
    this.pagesCount = Math.ceil(this.total / 100);

    this.createButton()
  }

  createButton() {
    const button = document.createElement('div')
    button.setAttribute('class', 'aux-content-widget-2')
    button.innerHTML = "" +
      "<h3>K2IMDb ratings file</h3> \n" +
      `<input type='file' id='${this.inputId}' accept='application/json' />\n` +
      `<progress value="0" max="${this.total}" style='width: 100%'>0 %</progress>`

    this.input = button.querySelector('#' + this.inputId)
    this.progressEl = button.querySelector('progress')
    this.input.addEventListener('change', this.onFileSet.bind(this))

    document.querySelector('#sidebar').insertBefore(button, document.querySelector('.list-create-widget'))
  }

  onFileSet() {
    const file = this.input.files[0];
    if (file) {
      const reader = new FileReader();
      reader.readAsText(file, "UTF-8");
      reader.onload = e => {
        try {
          this.db = JSON.parse(e.target.result);
        } catch (e) {
          this.showErrorMessage('File content is corrupted or not in JSON format')
        }
        if (confirm('Start import process?')) {
          const films = document.querySelectorAll('.lister-list > .lister-item')
          this.setRatings(films)
          if (this.pagesCount > 1) {
            this.getPages()
          }
        }
      }
      reader.onerror = e => {
        this.showErrorMessage("Error reading file");
      }
    } else {
      this.showErrorMessage('File is absent');
    }
  }

  getPages() {
    this.callInALoop(2, this.pagesCount, (i) => {
      this.getPage(i++).then(res => res.text()).then(html => {
        const div = document.createElement('div')
        div.innerHTML = html;
        try {
          const films = div.querySelectorAll('.lister-list > .lister-item')
          this.setRatings(films)
        } catch (e) {
          console.error(e)
        }
      }).catch(e => {
        let failedCount = 100;
        if (i === this.pagesCount && this.total % 100) {
          failedCount = this.total % 100;
        }
        this.error += failedCount;
        this.addError(i, 'was not able to load page');
        this.updateProgress()
      })

      return i <= this.pagesCount;
    })
  }

  getPage(number) {
    return fetch(`https://www.imdb.com/list/${this.listId}/_ajax?st_dt=&mode=detail&page=${number}&sort=list_order,asc`, {
      "credentials": "include",
      "headers": this.headers,
      "referrer": `https://www.imdb.com/list/${this.listId}/?st_dt=&mode=detail&page=${(number - 1) || 1}&sort=list_order,asc`,
      "referrerPolicy": "no-referrer-when-downgrade",
      "body": null,
      "method": "GET",
      "mode": "cors"
    });
  }

  setRatings(films) {
    this.callInALoop(0, films.length, (i) => {
      const film = films[i];
      const filmId = film.querySelector('.lister-item-image').getAttribute('data-tconst')
      if (!film.querySelector(this.ratingSelector)) {
        this.error++;
        this.addError(filmId, "hasn't rating control")
        this.updateProgress()
        return true;
      }

      if (this.getValue(film, 'rating') === '0') {
        const authString = this.getValue(film, 'auth');
        if (this.db[filmId]) {
          this.setRating(filmId, authString, this.db[filmId])
        } else {
          this.error++;
          this.addError(filmId, 'missed in our DB')
          this.updateProgress()
        }
      } else {
        this.skipped++;
        this.updateProgress()
      }

      return i < films.length - 1;
    })
  }

  setRating(filmId, authString, rating) {
    return this.setRatingAJAX(filmId, authString, rating)
      .then(res => {
        if (res.status === 200) {
          if (res.headers.get('content-type').includes('application/json')) {
            return res.json()
          } else {
            return {status: 200} // OK?
          }
        } else {
          return {status: res.status}
        }
      })
      .then(json => {
        if (json.status === 200) {
          this.success++;
          this.updateProgress()

        } else {
          this.error++;
          this.addError(filmId, 'Failed to update rating with ' + res.status)
          this.updateProgress()
        }
      })
      .catch(e => {
        this.error++;
        this.addError(filmId, 'Failed to update rating with ', e.message)
        this.updateProgress()
      })
  }

  setRatingAJAX(filmId, authString, rating) {
    return fetch("https://www.imdb.com/ratings/_ajax/title", {
      "credentials": "include",
      "headers": this.headers,
      "referrer": `https://www.imdb.com/list/${this.listId}/`,
      "referrerPolicy": "no-referrer-when-downgrade",
      "body": `tconst=${filmId}&rating=${rating}&auth=${authString}&pageType=[object HTMLInputElement]&subpageType=[object HTMLInputElement]&tracking_tag=ur_rt`,
      "method": "POST",
      "mode": "cors"
    })
  }

  updateProgress() {
    const progress = this.success + this.skipped + this.error;
    this.progressEl.setAttribute('value', progress)
    this.progressEl.innerText = (this.total / progress).toFixed(3) + ' %';
    if (progress >= this.total) {
      this.showSuccessMessage('Import is completed. Results are: \n' +
        'success: ' + this.success + '\n' +
        'skipped: ' + this.skipped + '\n' +
        'error: ' + this.error + '\n\n' +
        'Check the console for details.')
    }
  }

  getValue(parent, name) {
    return parent.querySelector(`[name="${name}"]`).getAttribute('value')
  }

  addError(id, reason) {
    console.log(`%c ${id}: ${reason}`, 'color: #d00')
  }

  showErrorMessage(message) {
    alert(message)
  }

  showSuccessMessage(message) {
    alert(message)
  }

  callInALoop(i, count, method) {
    setTimeout(() => {
      if (method(i++)) {
        this.callInALoop(i, count, method)
      }
    }, this.timeout)
  }
}

if (/^\/list.*$/.test(document.location.pathname)) {
  window.k2imdb = new Kinopoisk2IMDb;
} else {
  alert('This script is supposed to be run on a list page')
}
