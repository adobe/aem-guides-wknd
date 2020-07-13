import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Breadcrumb',
};

export const Default = () => new Breadcrumb('default').markup;

class Breadcrumb {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      "<div class='" +
      this.styleClass +
      "'>" +
      "<nav class='cmp-breadcrumb' aria-label='Breadcrumb'>" +
      "<ol class='cmp-breadcrumb__list' itemscope='' itemtype='http://schema.org/BreadcrumbList'>" +
      this._linkedListItem(1) +
      this._linkedListItem(2) +
      this._linkedListItem(3) +
      this._listItem(4) +
      '</ol>' +
      '</nav>'
    );
  }

  _linkedListItem(position) {
    return (
      "<li class='cmp-breadcrumb__item' itemprop='itemListElement' itemscope='' itemtype='http://schema.org/ListItem'>" +
      "<a href='#" +
      position +
      "' class='cmp-breadcrumb__item-link' itemprop='item'>" +
      "<span itemprop='name'>Level " +
      position +
      '</span>' +
      '</a>' +
      "<meta itemprop='position' content='" +
      position +
      "'>" +
      '</li>'
    );
  }

  _listItem(position) {
    return (
      "<li class='cmp-breadcrumb__item' itemprop='itemListElement' itemscope='' itemtype='http://schema.org/ListItem'>" +
      "<span itemprop='name'>Level " +
      position +
      '</span>' +
      "<meta itemprop='position' content='" +
      position +
      "'>" +
      '</li>'
    );
  }
}
