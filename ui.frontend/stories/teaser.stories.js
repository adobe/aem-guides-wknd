import { document, console } from 'global';
import { storiesOf } from '@storybook/html';
import '../src/site/main.scss';
import '../.storybook/story-styles.css';




storiesOf('Teaser', module)
    .add('Default', () => '<div style=\'width:40%\'>' + new Teaser("", '').markup + '</div>')
    .add('Card', () => '<div style=\'width:40%\'>' + new Teaser("cmp-teaser--card", '').markup + '</div>')
    .add('Slide', () => '<div>' + new Teaser("cmp-teaser--slide", 'https://www.fillmurray.com/1280/800').markup + '</div>');


class Teaser {
    constructor(styleClass, imgSrc) {
        this.styleClass = styleClass;
        this.imgSrc = imgSrc !== '' ? imgSrc : 'https://www.fillmurray.com/460/300';
    }

    get markup() {
        return '<div class=\'' + this.styleClass + '\'>' +
                    '<div class="cmp-teaser">'+
                    '<div class="cmp-teaser__image">'+
                    '<div data-title="Aerial photo of mountain range" class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">'+
                    '<img src="' + this.imgSrc +'" class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="Aerial photo of mountain range" title="Aerial photo of mountain range">'+
                    '<meta itemprop="caption" content="Aerial photo of mountain range">'+
                    '</div>'+
                    '</div>'+
                    '<div class="cmp-teaser__content">'+
                    '<h2 class="cmp-teaser__title">'+
                    '<a class="cmp-teaser__title-link" href="#">Teaser Title</a>'+
                    '</h2>'+
                    '<div class="cmp-teaser__description">'+
                    '<p>Teaser Description</p>'+
                    '</div>'+
                    '<div class="cmp-teaser__action-container">'+
                    '<a class="cmp-teaser__action-link" href="#">Read More</a>'+
                    '</div>'+
                    '</div>'+
                    '</div>'+
            '</div>';
    }
}


