import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Language Navigation',
};

export const Default = () => new LanguageNavigation('cmp-languagenavigation--default').markup;
export const Dark = () =>
  new LanguageNavigation('cmp-languagenavigation--default cmp-languagenavigation--dark').markup;

class LanguageNavigation {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      '<div class="' +
      this.styleClass +
      '"">' +
      '<nav class="cmp-languagenavigation">' +
      '<ul class="cmp-languagenavigation__group">' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-US cmp-languagenavigation__item--langcode-en-US cmp-languagenavigation__item--level-0">' +
      '<span class="cmp-languagenavigation__item-title" lang="en-US">United States</span>' +
      '<ul class="cmp-languagenavigation__group">' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-US cmp-languagenavigation__item--langcode-en-US cmp-languagenavigation__item--level-1">' +
      '<a class="cmp-languagenavigation__item-link" href="/content/wknd/us/en.html" hreflang="en-US" lang="en-US" rel="alternate" title="English">English</a>' +
      '</li>' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-US cmp-languagenavigation__item--langcode-en-US cmp-languagenavigation__item--level-1">' +
      '<a class="cmp-languagenavigation__item-link" href="/content/wknd/us/es.html" hreflang="en-US" lang="en-US" rel="alternate" title="Spanish">Spanish</a>' +
      '</li>' +
      '</ul>' +
      '</li>' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-CA cmp-languagenavigation__item--langcode-en-CA cmp-languagenavigation__item--level-0 cmp-languagenavigation__item--active">' +
      '<span class="cmp-languagenavigation__item-title" lang="en-CA">Canada</span>' +
      '<ul class="cmp-languagenavigation__group">' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-CA cmp-languagenavigation__item--langcode-en-CA cmp-languagenavigation__item--level-1 cmp-languagenavigation__item--active">' +
      '<a class="cmp-languagenavigation__item-link" href="/content/wknd/ca/en.html" hreflang="en-CA" lang="en-CA" rel="alternate" title="English">English</a>' +
      '</li>' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-CA cmp-languagenavigation__item--langcode-en-CA cmp-languagenavigation__item--level-1">' +
      '<a class="cmp-languagenavigation__item-link" href="/content/wknd/ca/fr.html" hreflang="en-CA" lang="en-CA" rel="alternate" title="French">French</a>' +
      '</li>' +
      '</ul>' +
      '</li>' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-CH cmp-languagenavigation__item--langcode-de-CH cmp-languagenavigation__item--level-0">' +
      '<span class="cmp-languagenavigation__item-title" lang="de-CH">Switzerland</span>' +
      '<ul class="cmp-languagenavigation__group">' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-CH cmp-languagenavigation__item--langcode-de-CH cmp-languagenavigation__item--level-1">' +
      '<a class="cmp-languagenavigation__item-link" href="/content/wknd/ch/de.html" hreflang="de-CH" lang="de-CH" rel="alternate" title="Deutsch">Deutsch</a>' +
      '</li>' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-CH cmp-languagenavigation__item--langcode-de-CH cmp-languagenavigation__item--level-1">' +
      '<a class="cmp-languagenavigation__item-link" href="/content/wknd/ch/fr.html" hreflang="de-CH" lang="de-CH" rel="alternate" title="Français">Français</a>' +
      '</li>' +
      '<li class="cmp-languagenavigation__item cmp-languagenavigation__item--countrycode-CH cmp-languagenavigation__item--langcode-de-CH cmp-languagenavigation__item--level-1">' +
      '<a class="cmp-languagenavigation__item-link" href="/content/wknd/ch/it.html" hreflang="de-CH" lang="de-CH" rel="alternate" title="Italiano">Italiano</a>' +
      '</li>' +
      '</ul>' +
      '</li>' +
      '</ul>' +
      '</nav>' +
      '</div>'
    );
  }
}
