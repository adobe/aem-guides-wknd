import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Byline',
};

export const Default = () => new Byline('default').markup;

class Byline {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      "<div class='" +
      this.styleClass +
      "'>" +
      "<div class='byline image aem-GridColumn--default--9 aem-GridColumn'>" +
      "<div class='cmp-byline'>" +
      "<div class='cmp-byline__image'>" +
      "<div data-cmp-src='http://fpoimg.com/300x250?text=Byline Author' data-asset='/content/dam/wknd/en/los-angeles/stacey-roswells.jpg' data-asset-id='b84974cf-ba8d-4777-b670-2f9e66d04cdc' class='cmp-image' itemscope='' itemtype='http://schema.org/ImageObject'>" +
      "<img src='http://fpoimg.com/300x250?text=Byline Author' class='cmp-image__image' itemprop='contentUrl' data-cmp-hook-image='image' alt=''>" +
      '</div>' +
      '</div>' +
      "<h2 class='cmp-byline__name'>Stacey Roswells</h2>" +
      "<p class='cmp-byline__occupations'>Blogger, Photographer, Youtuber</p>" +
      '</div>' +
      '</div>'
    );
  }
}
