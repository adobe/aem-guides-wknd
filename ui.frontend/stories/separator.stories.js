import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Separator',
};

export const Default = () => new Separator('').markup;
export const Light = () => new Separator('cmp-separator--light').markup;
export const Dark = () => new Separator('cmp-separator--dark').markup;
export const Hidden = () => new Separator('cmp-separator--hidden').markup;
export const SpacingSmall = () => new Separator('cmp-separator--space-small').markup;
export const SpacingMedium = () => new Separator('cmp-separator--space-medium').markup;
export const SpacingLarge = () => new Separator('cmp-separator--space-large').markup;

class Separator {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      '<div style="width: 60%">' +
      '<div class="title cmp-title--underline aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-title">' +
      '<h1 class="cmp-title__text">Separator</h1>' +
      '</div>' +
      '</div>' +
      '<div class="separator ' +
      this.styleClass +
      ' ">' +
      '<div class="cmp-separator">' +
      '<hr class="cmp-separator__horizontal-rule"/>' +
      '</div></div>' +
      '<div class="text aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-text">' +
      '<p>Thank goodness. My content is separated!</p>' +
      '</div>' +
      '</div>' +
      '</div>'
    );
  }
}
