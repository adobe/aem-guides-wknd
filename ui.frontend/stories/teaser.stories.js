import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Teaser',
};

export const Default = () => "<div style='width:60%'>" + new Teaser('').markup + '</div>';
export const DefaultCta = () => "<div style='width:60%'>" + new Teaser('').ctaMarkup + '</div>';

DefaultCta.story = {
  name: 'Default CTA',
};

export const FeaturedCta = () =>
  '<div style="width:60%">' + new Teaser('cmp-teaser--featured').ctaMarkup + '</div>';

FeaturedCta.story = {
  name: 'Featured CTA',
};

export const List = () =>
  '<div style="width:24%; float:left;">' +
  new Teaser('cmp-teaser--list').markup +
  '</div>' +
  '<div style="width:24%; float:left;">' +
  new Teaser('cmp-teaser--list').markup +
  '</div>' +
  '<div style="width:24%; float:left;">' +
  new Teaser('cmp-teaser--list').markup +
  '</div>';
export const Hero = () =>
  '<div style="width:100%">' + new Teaser('cmp-teaser--hero').ctaMarkup + '</div>';
/*
.add('Card', () => '<div style=\'width:40%\'>' + new Teaser("cmp-teaser--card", '').markup + '</div>')
.add('Slide', () => '<div>' + new Teaser("cmp-teaser--slide", 'https://www.fillmurray.com/1280/853').markup + '</div>');*/

class Teaser {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get ctaMarkup() {
    return (
      '<div class="teaser ' +
      this.styleClass +
      ' aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-teaser">' +
      '<div class="cmp-teaser__image">' +
      '<div data-cmp-lazy="" data-cmp-src="/content/wknd/language-masters/en/_jcr_content/root/responsivegrid/responsivegrid/teaser.coreimg.100{.width}.jpeg/1571186006061/adobe-waadobe-wa-b6a6978.jpeg" data-cmp-widths="300,400,500,600,700,800,900,1000,1280,1600" data-asset="/content/dam/wknd/en/magazine/western-australia/Adobe_WAAdobe_WA_B6A6978.JPG" class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">' +
      '<img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="https://www.fillmurray.com/1280/850" />' +
      '</div>' +
      '</div>' +
      '<div class="cmp-teaser__content">' +
      '<h3 class="cmp-teaser__title">' +
      '<a class="cmp-teaser__title-link" href="/content/wknd/language-masters/en/magazine/western-australia.html">Western Australia by Camper Van</a>' +
      '</h3>' +
      '<div class="cmp-teaser__description"><p>A vibrant red, dusty highway stretches out before us into what seems like infinity, at the far reach of our sights it distorts and shimmers from the afternoon heat, fading into the blue of the horizon.</p>' +
      '</div>' +
      '<div class="cmp-teaser__action-container">' +
      '<a class="cmp-teaser__action-link" href="/content/wknd/language-masters/en/magazine/western-australia.html">Read More</a>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>'
    );
  }

  get markup() {
    return (
      '<div class="teaser ' +
      this.styleClass +
      ' aem-GridColumn aem-GridColumn--default--12">' +
      '<div class="cmp-teaser">' +
      '<div class="cmp-teaser__image">' +
      '<div data-cmp-lazy="" data-cmp-src="/content/wknd/language-masters/en/_jcr_content/root/responsivegrid/responsivegrid/teaser.coreimg.100{.width}.jpeg/1571186440698/adobe-waadobe-wa-b6a6978.jpeg" data-cmp-widths="300,400,500,600,700,800,900,1000,1280,1600" data-asset="/content/dam/wknd/en/magazine/western-australia/Adobe_WAAdobe_WA_B6A6978.JPG" class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">' +
      '<a class="cmp-image__link" href="/content/wknd/language-masters/en/magazine/western-australia.html" data-cmp-hook-image="link">' +
      '<img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="https://www.fillmurray.com/1280/850"/>' +
      '</a>' +
      '</div>' +
      '</div>' +
      '<div class="cmp-teaser__content">' +
      '<h3 class="cmp-teaser__title">' +
      '<a class="cmp-teaser__title-link" href="/content/wknd/language-masters/en/magazine/western-australia.html">Western Australia by Camper Van</a>' +
      '</h3>' +
      '<div class="cmp-teaser__description"><p>Details of camping in western Australia.</p>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>'
    );
  }
}
