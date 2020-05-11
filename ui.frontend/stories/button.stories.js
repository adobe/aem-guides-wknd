import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Button',
};

export const DefaultButton = () => new Button('', '').markup;
export const PrimaryButton = () => new Button('cmp-button--primary').markup;
export const SecondaryButton = () => new Button('cmp-button--secondary').markup;
export const IconFacebook = () =>
  new Button('', 'cmp-button__icon cmp-button__icon--facebook').iconMarkup;
export const IconTwitter = () =>
  new Button('', 'cmp-button__icon cmp-button__icon--twitter').iconMarkup;
export const IconInstagram = () =>
  new Button('', 'cmp-button__icon cmp-button__icon--instagram').iconMarkup;

class Button {
  constructor(styleClass, iconClass) {
    this.styleClass = styleClass;
    this.iconClass = iconClass;
  }

  get markup() {
    return (
      '<div class="button ' +
      this.styleClass +
      '">' +
      '<a class="cmp-button" href="#modal" aria-label="select-dates">' +
      '<span class="cmp-button__text">Read More</span>' +
      '</a>' +
      '</div>'
    );
  }

  get iconMarkup() {
    return (
      '<div class="button ' +
      this.styleClass +
      '" style="width:100%; float:left; margin-bottom: 1em">' +
      '<a class="cmp-button" href="#modal" aria-label="select-dates">' +
      '<span class="' +
      this.iconClass +
      '"></span>' +
      '<span class="cmp-button__text">Read More</span>' +
      '</a>' +
      '</div>' +
      '<div class="button ' +
      this.styleClass +
      ' cmp-button--primary" style="width:100%; float:left; margin-bottom: 1em">' +
      '<a class="cmp-button" href="#modal" aria-label="select-dates">' +
      '<span class="' +
      this.iconClass +
      '"></span>' +
      '<span class="cmp-button__text">Read More</span>' +
      '</a>' +
      '</div>' +
      '<div class="button ' +
      this.styleClass +
      ' cmp-button--secondary" style="width:100%; float:left; margin-bottom: 1em">' +
      '<a class="cmp-button" href="#modal" aria-label="select-dates">' +
      '<span class="' +
      this.iconClass +
      '"></span>' +
      '<span class="cmp-button__text">Read More</span>' +
      '</a>' +
      '</div>' +
      '<div class="button ' +
      this.styleClass +
      ' cmp-button--icononly" style="width:100%; float:left; margin-bottom: 1em">' +
      '<a class="cmp-button" href="#modal" aria-label="select-dates">' +
      '<span class="' +
      this.iconClass +
      '"></span>' +
      '<span class="cmp-button__text">Read More</span>' +
      '</a>' +
      '</div>' +
      '<div class="button ' +
      this.styleClass +
      ' cmp-button--icononly cmp-button--primary" style="width:100%; float:left; margin-bottom: 1em">' +
      '<a class="cmp-button" href="#modal" aria-label="select-dates">' +
      '<span class="' +
      this.iconClass +
      '"></span>' +
      '<span class="cmp-button__text">Read More</span>' +
      '</a>' +
      '</div>' +
      '<div class="button ' +
      this.styleClass +
      ' cmp-button--icononly cmp-button--secondary" style="width:100%; float:left; margin-bottom: 1em">' +
      '<a class="cmp-button" href="#modal" aria-label="select-dates">' +
      '<span class="' +
      this.iconClass +
      '"></span>' +
      '<span class="cmp-button__text">Read More</span>' +
      '</a>' +
      '</div>'
    );
  }
}
