import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'List',
};

export const Default = () => new List('default').markup;
export const UpNext = () => new List('cmp-list--upnext').markup;

class List {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      "<div class='" +
      this.styleClass +
      "'>" +
      '<ul class="cmp-list">' +
      '<li class="cmp-list__item">' +
      '<article>' +
      '<a class="cmp-list__item-link" href="/content/wknd/en/sports/five-gyms-la.html">' +
      '<span class="cmp-list__item-title">Flow with the Go. 5 Gyms in LA.</span>' +
      '<span class="cmp-list__item-date">Tuesday, 10 Sep 2019</span>' +
      '</a>' +
      '' +
      '</article>' +
      '</li>' +
      '' +
      '<li class="cmp-list__item">' +
      '<article>' +
      '<a class="cmp-list__item-link" href="/content/wknd/en/sports/la-skateparks.html">' +
      '<span class="cmp-list__item-title">Ultimate guide to LA Skateparks.</span>' +
      '<span class="cmp-list__item-date">Wednesday, 17 Apr 2019</span>' +
      '</a>' +
      '' +
      '</article>' +
      '</li>' +
      '' +
      '<li class="cmp-list__item">' +
      '<article>' +
      '<a class="cmp-list__item-link" href="/content/wknd/en/sports/mountain-bike-routes.html">' +
      '<span class="cmp-list__item-title">Mountain Bike Routes for the weekend.</span>' +
      '<span class="cmp-list__item-date">Wednesday, 17 Apr 2019</span>' +
      '</a>' +
      '' +
      '</article>' +
      '</li>' +
      '</ul>' +
      '</div>'
    );
  }
}
