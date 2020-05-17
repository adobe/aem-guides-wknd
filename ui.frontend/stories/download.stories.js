import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Download',
};

export const Default = () => new Download('default').markup;

class Download {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return (
      '<div class="' +
      this.styleClass +
      '">' +
      '<div class="cmp-download">' +
      '<h3 class="cmp-download__title">' +
      '<a class="cmp-download__title-link" href="/content/dam/core-components-examples/library/sample-assets/lava-into-ocean.jpg.coredownload.jpeg">Custom Title</a>' +
      '</h3>' +
      '<div class="cmp-download__description">' +
      '<p>Custom description</p>' +
      '</div>' +
      '<dl class="cmp-download__properties">' +
      '<div class="cmp-download__property cmp-download__property--filename">' +
      '<dt class="cmp-download__property-label">Filename</dt>' +
      '<dd class="cmp-download__property-content">lava-into-ocean.jpg</dd>' +
      '</div>' +
      '<div class="cmp-download__property cmp-download__property--size">' +
      '<dt class="cmp-download__property-label">Size</dt>' +
      '<dd class="cmp-download__property-content">81 KB</dd>' +
      '</div>' +
      '<div class="cmp-download__property cmp-download__property--format">' +
      '<dt class="cmp-download__property-label">Format</dt>' +
      '<dd class="cmp-download__property-content">image/jpeg</dd>' +
      '</div>' +
      '</dl>' +
      '<a class="cmp-download__action" href="/content/dam/core-components-examples/library/sample-assets/lava-into-ocean.jpg.coredownload.jpeg">' +
      '<span class="cmp-download__action-text">Download PDF</span>' +
      '</a>' +
      '</div>' +
      '</div>'
    );
  }
}
