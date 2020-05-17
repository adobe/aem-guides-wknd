import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Navigation',
};

export const HeaderDesktop = () => new Navigation('cmp-navigation--header').markup;

HeaderDesktop.story = {
  name: 'Header - Desktop',
};

export const Footer = () =>
  "<div class='cmp-layout-container--footer'>" +
  new Navigation('cmp-navigation--footer').markup +
  '</div>';

class Navigation {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      "<div class='" +
      this.styleClass +
      "'>" +
      '<nav class="cmp-navigation" role="navigation" itemscope="" itemtype="http://schema.org/SiteNavigationElement">' +
      '<ul class="cmp-navigation__group">' +
      '<li class="cmp-navigation__item cmp-navigation__item--level-0">' +
      '<a href="/content/wknd/en/restaurants.html" title="Restaurants" class="cmp-navigation__item-link">Restaurants</a>' +
      '</li>' +
      '<li class="cmp-navigation__item cmp-navigation__item--level-0">' +
      '<a href="/content/wknd/en/bars.html" title="Bars" class="cmp-navigation__item-link">Bars</a>' +
      '</li>' +
      '<li class="cmp-navigation__item cmp-navigation__item--level-0 cmp-navigation__item--active">' +
      '<a href="/content/wknd/en/sports.html" title="Sports" aria-current="page" class="cmp-navigation__item-link">Sports</a>' +
      '</li>' +
      '<li class="cmp-navigation__item cmp-navigation__item--level-0">' +
      '<a href="/content/wknd/en/art.html" title="Art" class="cmp-navigation__item-link">Art</a>' +
      '</li>' +
      '<li class="cmp-navigation__item cmp-navigation__item--level-0">' +
      '<a href="/content/wknd/en/music.html" title="Music" class="cmp-navigation__item-link">Music</a>' +
      '</li>' +
      '<li class="cmp-navigation__item cmp-navigation__item--level-0">' +
      '<a href="/content/wknd/en/shopping.html" title="Shopping" class="cmp-navigation__item-link">Shopping</a>' +
      '</li>' +
      '</ul>' +
      '</nav>' +
      '</div>'
    );
  }
}
