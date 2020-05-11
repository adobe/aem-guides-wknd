import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Search',
};

export const SearchHeader = () => new Search('cmp-search--header').markup;

SearchHeader.story = {
  name: 'Search - Header',
};

class Search {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      "<div class='" +
      this.styleClass +
      "'>" +
      '<section class="cmp-search" role="search" data-cmp-min-length="3" data-cmp-results-size="5">' +
      '<form class="cmp-search__form" data-cmp-hook-search="form" method="get" action="/content/wknd/en/sports/la-skateparks.searchresults.json/_jcr_content/root/header/search" autocomplete="off">' +
      '<div class="cmp-search__field">' +
      '<i class="cmp-search__icon" data-cmp-hook-search="icon"></i>' +
      '<span class="cmp-search__loading-indicator" data-cmp-hook-search="loadingIndicator"></span>' +
      '<input class="cmp-search__input" data-cmp-hook-search="input" type="text" name="fulltext" placeholder="Search" role="combobox" aria-autocomplete="list" aria-haspopup="true" aria-invalid="false" aria-owns="cmp-search-results-0">' +
      '<button class="cmp-search__clear" data-cmp-hook-search="clear">' +
      '<i class="cmp-search__clear-icon"></i>' +
      '</button>' +
      '</div>' +
      '</form>' +
      '<div class="cmp-search__results" data-cmp-hook-search="results" role="listbox" aria-multiselectable="false" id="cmp-search-results-0" aria-hidden="true" style="display: none;"></div>' +
      '' +
      '<script data-cmp-hook-search="itemTemplate" type="x-template">' +
      '<a class="cmp-search__item" data-cmp-hook-search="item">' +
      '<span class="cmp-search__item-title" data-cmp-hook-search="itemTitle"></span>' +
      '</a>' +
      '</script>' +
      '</section>' +
      '</div>'
    );
  }
}
