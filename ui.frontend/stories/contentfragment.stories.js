import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Content Fragment',
};

export const Default = () => new ContentFragment('default').markup;

class ContentFragment {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      "<div class='" +
      this.styleClass +
      "' >" +
      '<div class="contentfragment aem-GridColumn--default--9 aem-GridColumn">' +
      '<article class="cmp-contentfragment"' +
      'data-cmp-contentfragment-model="/content/dam/wknd/en/los-angeles/ultimate-guide-to-la-skateparks/jcr:content/model"' +
      'data-cmp-contentfragment-path="/content/dam/wknd/en/los-angeles/ultimate-guide-to-la-skateparks">' +
      '<h3 class="cmp-contentfragment__title">Ultimate Guide to LA Skateparks</h3>' +
      '<p class="cmp-contentfragment__description">The article body for the Ultimate Guide to LA Skateparks</p>' +
      '<div class="cmp-contentfragment__elements">' +
      '<div>' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '</div>' +
      '</div>' +
      '<div>' +
      '<p>Steep mountain sides surround us, like wise trolls from a distant timeline, weathered and worn by' +
      'long-gone glaciers, green moss now covering the black rock. White sheep forage on steep grass,' +
      'defying the chilling winds funneled by the deep valley. The subtle hues of the arctic circle are' +
      'welcoming, comfortable on the eyes. When rare sunrays pierce through the low clouds, the scenery' +
      'reveals its vibrancy, as the waves reflect a translucent cyan blue before crashing loudly onto white' +
      'sand. A small but playful groundswell is building, the offshore breeze grooming playful lines down' +
      'the point, making for welcoming conditions for acclimatizing to cold water and thick neoprene.' +
      'Knowing it is our last surf before a few days of hard wind, we take full advantage out of every' +
      'ripple the North Atlantic Ocean sends our way. Tomorrow this place will have taken on a very' +
      'different, much more hostile appearance.</p>' +
      '<div>' +
      '<div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">' +
      '<div class="text cmp-text--quote aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-text">' +
      '<blockquote>"This is a famouse quote that makes this article pop!"</blockquote>' +
      '<p><u>Famous Skatboarder</u></p>' +
      '</div>' +
      '</div>' +
      '<div class="title cmp-title--underline aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-title">' +
      '<h2 class="cmp-title__text">New Paragraph</h2>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '<p>The front is due to arrive any hour now, weather charts calling for anything in between regular bad' +
      'weather and full arctic storm. In our fishing cabin in the quaint fiord, weather and surf forecast' +
      'websites are refreshed hourly, for any update or hint on where to find the most manageable' +
      'conditions. Wetsuits dried and ready, fresh wax coat on the surfboards. Tents are inspected, every' +
      'seam scrutinized. To properly score in this part of the world, equipment means everything, and any' +
      'inconsistency can ruin a session, the whole trip even. As our social media devices are put on sleep' +
      'mode and reading lamps are switched off, the wind grows in intensity, rattling our windows through' +
      'the night.&nbsp;Every surfer knows the feeling of combined anxiety and excitement, the questions' +
      'that linger in our minds while falling asleep before a swell. Tomorrow they will be answered; we' +
      'tell each other before updating the charts just one last time.</p>' +
      '</div>' +
      '</div>' +
      '</article>' +
      '</div>'
    );
  }
}
