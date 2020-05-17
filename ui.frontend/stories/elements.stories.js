import { document, console } from 'global';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Elements',
};

export const Standard = () => new Elements().markup;

class Elements {
  get markup() {
    return (
      '<div style="width:60%">' +
      '<div class="title cmp-title--underline aem-GridColumn aem-GridColumn--default--8">' +
      '<div class="cmp-title">' +
      '<h2 class="cmp-title__text">Headings</h2>' +
      '</div>' +
      '</div>' +
      '<div class="text aem-GridColumn aem-GridColumn--default--8">' +
      '<div class="cmp-text">' +
      '<h1>Heading H1</h1>' +
      '<h2>Heading H2</h2>' +
      '<h3>Heading H3</h3>' +
      '<h4>Heading H4</h4>' +
      '<h5>Heading H5</h5>' +
      '<h6>Heading H6</h6>' +
      '<h1><a href="#">Heading H1</a></h1>' +
      '<h2><a href="#">Heading H2</a></h2>' +
      '<h3><a href="#">Heading H3</a></h3>' +
      '<h4><a href="#">Heading H4</a></h4>' +
      '<h5><a href="#">Heading H5</a></h5>' +
      '<h6><a href="#">Heading H6</a></h6>' +
      '</div>' +
      '</div>' +
      '<div class="title cmp-title--underline aem-GridColumn aem-GridColumn--default--8">' +
      '<div class="cmp-title">' +
      '<h2 class="cmp-title__text">Text</h2>' +
      '</div>' +
      '</div>' +
      '<div class="text aem-GridColumn aem-GridColumn--default--8">' +
      '<div class="cmp-text">' +
      '<h5>Standard</h5>' +
      '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer euismod odio non leo pretium pellentesque. <a title="Sample Link" href="#">Curabitur blandit urna</a> cursus, malesuada erat ut, egestas odio. Quisque suscipit, urna ac vulputate sollicitudin, mi urna elementum augue, id tristique arcu erat non enim. </p>' +
      '<h5>Type Formatting</h5>' +
      '<p><b>Bold</b> can be used to emphasize a word or phrase, as can <u>underline</u> and <i>italics</i>. <sup>Superscript</sup> and <sub>subscript</sub> are useful for mathematical (E = mc<sup>2</sup>) or scientific (h<sub>2</sub>O) expressions. Paragraph styles can provide alternative renderings, such as quote sections:</p>' +
      '</div>' +
      '</div>' +
      '<div class="text aem-GridColumn aem-GridColumn--default--8">' +
      '<div class="cmp-text">' +
      '<div> </div>' +
      '<ul>' +
      '<li>List Item</li>' +
      '<li>List Item</li>' +
      '<li>List Item</li>' +
      '</ul>' +
      '<ol>' +
      '<li>List Item</li>' +
      '<li>List Item</li>' +
      '<li>List Item</li>' +
      '<li>List Item</li>' +
      '</ol>' +
      '<p> </p>' +
      '</div>' +
      '</div>' +
      '</div>'
    );
  }
}
