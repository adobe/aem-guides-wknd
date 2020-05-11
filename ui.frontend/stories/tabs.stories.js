import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Tabs',
};

export const Default = () => new Tabs('').markup;

class Tabs {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      '<div class="tabs aem-GridColumn aem-GridColumn--default--8">' +
      '<div class="cmp-tabs">' +
      '<ol role="tablist" class="cmp-tabs__tablist" aria-label="Tabs" aria-multiselectable="false">' +
      '<li role="tab" class="cmp-tabs__tab cmp-tabs__tab--active" tabindex="0" data-cmp-hook-tabs="tab" aria-selected="true">Tab 1</li>' +
      '<li role="tab" class="cmp-tabs__tab" tabindex="-1" data-cmp-hook-tabs="tab" aria-selected="false">Tab 2</li>' +
      '<li role="tab" class="cmp-tabs__tab" tabindex="-1" data-cmp-hook-tabs="tab" aria-selected="false">Tab 3</li>' +
      '<li role="tab" class="cmp-tabs__tab" tabindex="-1" data-cmp-hook-tabs="tab" aria-selected="false">Tab 4</li>' +
      '</ol>' +
      '<div role="tabpanel" tabindex="0" class="cmp-tabs__tabpanel cmp-tabs__tabpanel--active" data-cmp-hook-tabs="tabpanel"><div class="responsivegrid">' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '<div class="text aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-text">' +
      '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer euismod odio non leo pretium pellentesque. Curabitur blandit urna cursus, malesuada erat ut, egestas odio. Quisque suscipit, urna ac vulputate sollicitudin, mi urna elementum augue, id tristique arcu erat non enim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer euismod odio non leo pretium pellentesque. Curabitur blandit urna cursus, malesuada erat ut, egestas odio. Quisque suscipit, urna ac vulputate sollicitudin, mi urna elementum augue, id tristique arcu erat non enim.</p>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '<div role="tabpanel" tabindex="0" class="cmp-tabs__tabpanel" data-cmp-hook-tabs="tabpanel" aria-hidden="true"><div class="responsivegrid">' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '<div class="text aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-text">' +
      '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer euismod odio non leo pretium pellentesque. Curabitur blandit urna cursus, malesuada erat ut, egestas odio. Quisque suscipit, urna ac vulputate sollicitudin, mi urna elementum augue, id tristique arcu erat non enim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer euismod odio non leo pretium pellentesque. Curabitur blandit urna cursus, malesuada erat ut, egestas odio. Quisque suscipit, urna ac vulputate sollicitudin, mi urna elementum augue, id tristique arcu erat non enim.</p>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '<div role="tabpanel" tabindex="0" class="cmp-tabs__tabpanel" data-cmp-hook-tabs="tabpanel" aria-hidden="true"><div class="responsivegrid">' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '<div class="text aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-text">' +
      '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer euismod odio non leo pretium pellentesque. Curabitur blandit urna cursus, malesuada erat ut, egestas odio. Quisque suscipit, urna ac vulputate sollicitudin, mi urna elementum augue, id tristique arcu erat non enim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer euismod odio non leo pretium pellentesque. Curabitur blandit urna cursus, malesuada erat ut, egestas odio. Quisque suscipit, urna ac vulputate sollicitudin, mi urna elementum augue, id tristique arcu erat non enim.</p>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '<div role="tabpanel" tabindex="0" class="cmp-tabs__tabpanel" data-cmp-hook-tabs="tabpanel" aria-hidden="true"><div class="contentfragment">' +
      '<article class="cmp-contentfragment" data-cmp-contentfragment-model="wknd/models/adventure" data-cmp-contentfragment-path="/content/dam/wknd/en/adventures/surf-nicaragua/nicaragua-surf-trip">' +
      '<h3 class="cmp-contentfragment__title">Nicaragua Surf Trip</h3>' +
      '<div class="cmp-contentfragment__elements">' +
      '<div>' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '</div>' +
      '</div>' +
      '<div>' +
      '<h2>3-day surf camp in San Juan del Sur<br>' +
      '</h2>' +
      '<div>' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '</div>' +
      '</div>' +
      '<p>We believe that you should experience an authentic surf journey. During this week, you will get the chance to dive deep into the world of surfing, like no other place on earth.</p>' +
      '<ul><li>Exclusive villa for our groups of 12 surfers, with panoramic views on the Pacific</li><li>Some of the world’s most consistent quality waves for beginners &amp; intermediates</li><li>Walk to cafes, restaurants and beach bars</li><li>330+ days of “offshore” wind, creating extremely fun and clean waves</li><li>Learn to surf with members of the National Surf Team</li></ul>' +
      '<div>' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</article>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>'
    );
  }
}
