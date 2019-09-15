import { document, console } from 'global';
import { storiesOf } from '@storybook/html';
import '../../../site/main.scss';
import '../../../../.storybook/story-styles.css';




storiesOf('Image', module)
  .add('Default', () => new Image("default").markup)
  .add('Caption', () => new Image("default").caption);


  class Image {
      constructor(styleClass) {
          this.styleClass = styleClass;
      }

      get markup() {
          return '<div class=\'' + this.styleClass + '\'>' +
                    '<div data-title="Lava flowing into the ocean" class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">'+
                    '<img src="http://fpoimg.com/1280x720?text=Image" class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="Lava flowing into the ocean" title="Lava flowing into the ocean">'+
                    '<meta itemprop="caption" content="Lava flowing into the ocean">'+
                    '</div>'+
                 '</div>';
      }

      get caption() {
          return '<div class=\'' + this.styleClass + '\'>' +
                    '<div data-title="Lava flowing into the ocean" class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">'+
                    '<img src="http://fpoimg.com/1280x720?text=Image" class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="Lava flowing into the ocean" title="Lava flowing into the ocean">'+
                    '<span class="cmp-image__title" itemprop="caption">Lava flowing into the ocean</span>'+
                    '</div>'+
                '</div>';
      }
  }