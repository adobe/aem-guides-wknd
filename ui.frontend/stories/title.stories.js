import { document, console } from 'global';
import { storiesOf } from '@storybook/html';
import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';




storiesOf('Title', module)
    .add('Default', () => '<div style=\'width:50%\'>' + new Title("").markup + '</div>')
    .add('Underline', () => '<div style=\'width:50%\'>' + new Title("cmp-title--underline").markup + '</div>')


class Title {
    constructor(styleClass) {
        this.styleClass = styleClass;
    }

    get markup() {
        return this.createTitle('H1') + this.createTitle('H2') + this.createTitle('H3') + this.createTitle('H4') + this.createTitle('H5') + this.createTitle('H6');
    }

    createTitle(headingType) {
        return '<div class=\'' + this.styleClass + '\'>' +
                '<div class="cmp-title">'+
                    '<' + headingType + ' class="cmp-title__text">Lorem Ipsum ' + headingType + '</' + headingType + '>'+
                '</div>'+
            '</div>';
    }
}



