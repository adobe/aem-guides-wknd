import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Text',
};

export const Default = () => "<div style='width:50%'>" + new Text('').markup + '</div>';
export const SizeXSmall = () =>
  "<div style='width:50%'>" + new Text('cmp-text--font-xsmall').markup + '</div>';

SizeXSmall.story = {
  name: 'Size X-Small',
};

export const SizeSmall = () =>
  "<div style='width:50%'>" + new Text('cmp-text--font-small').markup + '</div>';
export const SizeDefault = () =>
  "<div style='width:50%'>" + new Text('cmp-text--font-base').markup + '</div>';
export const SizeLarge = () =>
  "<div style='width:50%'>" + new Text('cmp-text--font-large').markup + '</div>';
export const Quote = () => "<div style='width:50%'>" + new Text('').quoteBlock + '</div>';

class Text {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      "<div class='" +
      this.styleClass +
      "'>" +
      '<div class="cmp-text">' +
      '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eu mi bibendum neque egestas congue quisque egestas. Varius morbi enim nunc faucibus a pellentesque. Scelerisque eleifend donec pretium vulputate sapien nec sagittis.</p>' +
      '</div>' +
      '</div>'
    );
  }

  get quoteBlock() {
    return (
      '<div class="text cmp-text--quote">' +
      '<div class="cmp-text">' +
      '<blockquote>"This is a famouse quote that makes this article pop!"</blockquote>' +
      '<p><u>Famous Skatboarder</u></p>' +
      '</div>'
    );
  }
}
