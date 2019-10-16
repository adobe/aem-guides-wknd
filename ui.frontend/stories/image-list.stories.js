



import {storiesOf} from '@storybook/html';
import '../src/site/main.scss';
import '../.storybook/story-styles.css';

storiesOf('Image List', module)
    .add('Default', () => new DefaultImageList("cmp-image-list").markup)

class DefaultImageList {
    constructor(styleClass) {
        this.styleClass = styleClass;
    }

    get markup() {
        return `<div class="cmp cmp-image-list aem-GridColumn aem-GridColumn--default--12">
                        <ul class="cmp-list">
                            <li class="cmp-list__item">
                                <article class="cmp-image-list__item">
                    
                                    <a class="cmp-image-list__item-image-link" href="#">
                                        <div class="cmp-image-list__item-image">
                                            <div data-cmp-lazy=""
                                                 data-cmp-src="/content/wknd/us/en/magazine/alaskan-adventure/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.jpeg/1571247641788/camp-alaska.jpeg"
                                                 data-cmp-widths="300,500,800,1000,1200"
                                                 data-asset="/content/dam/fpo.jpg"
                                                 class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">                    
                                                <img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="http://fpoimg.com/100x100?text=Image">
                                            </div>
                                        </div>
                                    </a>
                    
                                    <a class="cmp-image-list__item-link" href="#">
                                        <span class="cmp-image-list__item-title">Item 1</span>
                                    </a>
                                </article>
                            </li>
                    
                            <li class="cmp-list__item">
                                <article class="cmp-image-list__item">                    
                                    <a class="cmp-image-list__item-image-link" href="#">
                                        <div class="cmp-image-list__item-image">
                                            <div data-cmp-lazy=""
                                                 data-cmp-src="/content/wknd/us/en/magazine/arctic-surfing/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.jpeg/1571247704654/surfer-back-from-the-ocean.jpeg"
                                                 data-cmp-widths="300,500,800,1000,1200"
                                                 data-asset="/content/dam/wknd/en/magazine/arctic-surfing/surfer-back-from-the-ocean.JPG"
                                                 class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">
                    
                                                <img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="http://fpoimg.com/1280x100?text=Image">
                                            </div>
                                        </div>
                                    </a>
                    
                                    <a class="cmp-image-list__item-link" href="#">
                                        <span class="cmp-image-list__item-title">Item 2</span>
                                    </a>                    
                                </article>
                            </li>
                    
                            <li class="cmp-list__item">
                                <article class="cmp-image-list__item">
                                    <a class="cmp-image-list__item-image-link"
                                       href="#">
                                        <div class="cmp-image-list__item-image">
                                            <div data-cmp-lazy=""
                                                 data-cmp-src="/content/wknd/us/en/magazine/fly-fishing-the-amazon/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.jpeg/1571247751435/amazon-river-02.jpeg"
                                                 data-cmp-widths="300,500,800,1000,1200"
                                                 data-asset="/content/dam/wknd/en/magazine/fly-fishing-amazon/amazon-river-02.JPG"
                                                 class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">
                                                <img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="http://fpoimg.com/100x1000?text=Image">
                                            </div>
                                        </div>
                                    </a>
                    
                                    <a class="cmp-image-list__item-link" href="#">
                                        <span class="cmp-image-list__item-title">Item 3</span>
                                    </a>
                                </article>
                            </li>
                    
                            <li class="cmp-list__item">
                                <article class="cmp-image-list__item">
                    
                                    <a class="cmp-image-list__item-image-link" href="#">
                                        <div class="cmp-image-list__item-image">
                                            <div data-cmp-lazy=""
                                                 data-cmp-src="/content/wknd/us/en/magazine/skiitouring/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.jpeg/1571247753674/skitouring1sjoeberg.jpeg"
                                                 data-cmp-widths="300,500,800,1000,1200"
                                                 data-asset="/content/dam/fpo.jpg"
                                                 class="cmp-image" itemscope="" itemtype="http://schema.org/ImageObject">
                                                <img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="http://fpoimg.com/1280x720?text=Image">
                                            </div>
                                        </div>
                                    </a>
                    
                                    <a class="cmp-image-list__item-link" href="#">
                                        <span class="cmp-image-list__item-title">The last item which has a very long title so that it wraps</span>
                                    </a>
                                </article>
                            </li>
                        </ul>
                    </div>`;
    }
}