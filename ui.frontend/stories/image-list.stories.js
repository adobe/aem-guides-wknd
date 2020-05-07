import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Image List',
};

export const Default = () => new DefaultImageList('cmp-image-list').markup;

class DefaultImageList {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return `<div class="image-list list aem-GridColumn aem-GridColumn--default--12">
                    <ul class="cmp-image-list">
                        <li class="cmp-image-list__item">
                            <article class="cmp-image-list__item-content">
                                <a class="cmp-image-list__item-image-link" href="#">
                                    <div class="cmp-image-list__item-image">
                                        <div data-cmp-lazy="" 
                                             data-cmp-src="/content/page/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.png/1571324559221/fpo.png" 
                                             data-cmp-widths="300,500,800,1000,1200" 
                                             data-asset="/content/dam/fpo.png" 
                                             data-title="FPO" 
                                             class="cmp-image" 
                                             itemscope="" itemtype="http://schema.org/ImageObject">
                                            <img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="hero image for Skate park" src="http://fpoimg.com/100x100?text=Image">
                                        </div>
                                    </div>
                                </a>
                                <a class="cmp-image-list__item-title-link" href="#">
                                    <span class="cmp-image-list__item-title">Short</span>
                                </a>
                                
                                <span class="cmp-image-list__item-description">Short description</span>
                            </article>
                        </li>
                
                        <li class="cmp-image-list__item">
                            <article class="cmp-image-list__item-content">
                                <a class="cmp-image-list__item-image-link" href="#">
                                    <div class="cmp-image-list__item-image">
                                        <div data-cmp-lazy="" 
                                             data-cmp-src="/content/page/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.png/1571324559221/fpo.png" 
                                             data-cmp-widths="300,500,800,1000,1200" 
                                             data-asset="/content/dam/fpo.png" 
                                             data-title="FPO" 
                                             class="cmp-image" 
                                             itemscope="" itemtype="http://schema.org/ImageObject">
                                            <img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="http://fpoimg.com/1280x100?text=Image">
                                        </div>
                                    </div>
                                </a>
                                <a class="cmp-image-list__item-title-link" href="#">
                                    <span class="cmp-image-list__item-title">A Medium Length Title</span>
                                </a>
                                
                                <span class="cmp-image-list__item-description">A medium length description</span>
                            </article>
                        </li>
                
                        <li class="cmp-image-list__item">
                            <article class="cmp-image-list__item-content">
                                <a class="cmp-image-list__item-image-link" href="#">
                                    <div class="cmp-image-list__item-image">
                                        <div data-cmp-lazy="" 
                                             data-cmp-src="/content/page/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.png/1571324559221/fpo.png" 
                                             data-cmp-widths="300,500,800,1000,1200" 
                                             data-asset="/content/dam/fpo.png" 
                                             data-title="FPO" 
                                             class="cmp-image" 
                                             itemscope="" itemtype="http://schema.org/ImageObject">
                                             <img class="cmp-image__image" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="http://fpoimg.com/100x1000?text=Image">
                                        </div>
                                    </div>
                                </a>
                                <a class="cmp-image-list__item-title-link" href="#">
                                    <span class="cmp-image-list__item-title">A LONG TITLE THAT ILLUSTRATES HOW WRAPPING LOOKS</span>
                                </a>
                                <span class="cmp-image-list__item-description">A long title that should end up being truncated</span>                                
                            </article>
                        </li>
                
                        <li class="cmp-image-list__item">
                            <article class="cmp-image-list__item-content">
                                <a class="cmp-image-list__item-image-link" href="#">
                                    <div class="cmp-image-list__item-image">
                                        <div data-cmp-lazy="" 
                                             data-cmp-src="/content/page/_jcr_content/root/responsivegrid/image.coreimg.100{.width}.png/1571324559221/fpo.png" 
                                             data-cmp-widths="300,500,800,1000,1200" 
                                             data-asset="/content/dam/fpo.png" 
                                             data-title="FPO" 
                                             class="cmp-image" 
                                             itemscope="" itemtype="http://schema.org/ImageObject">
                                             <img class="cmp-image__image cmp-image__image--is-loading" itemprop="contentUrl" data-cmp-hook-image="image" alt="" src="http://fpoimg.com/1280x720?text=Image">
                                        </div>
                                    </div>
                                </a>
                                <a class="cmp-image-list__item-title-link" href="/#">
                                    <span class="cmp-image-list__item-title">A VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY LONG TITLE TO SEE WHAT AN EXTREME CASE OF WRAPPING LOOKS LIKE</span>
                                </a>
                                <span class="cmp-image-list__item-description">A very very very very very very very very very very very very very very very very long title that will most certainly be truncated</span>                                                                
                            </article>
                        </li>
                    </ul>
                </div>`;
  }
}
