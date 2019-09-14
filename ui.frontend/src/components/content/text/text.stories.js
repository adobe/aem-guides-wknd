import { document, console } from 'global';
import { storiesOf } from '@storybook/html';
import '../../../site/main.scss';
import '../../../../.storybook/story-styles.css';




storiesOf('Text', module)
    .add('Default', () => '<div style=\'width:50%\'>' + new Text("").markup + '</div>')
    .add('Quote', () => '<div style=\'width:50%\'>' + new Text("").quoteBlock + '</div>')


class Text {
    constructor(styleClass) {
        this.styleClass = styleClass;
    }

    get markup() {
        return '<div class=\'' + this.styleClass + '\'>' +
                '<div class="cmp-text">'+
                '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eu mi bibendum neque egestas congue quisque egestas. Varius morbi enim nunc faucibus a pellentesque. Scelerisque eleifend donec pretium vulputate sapien nec sagittis.</p>'+
                '</div>'+
            '</div>';
    }

    get quoteBlock() {
        return '<div class="text cmp-text--quote">'+
                '<div class="cmp-text">'+
                '<blockquote>"This is a famouse quote that makes this article pop!"</blockquote>'+
                '<p><u>Famous Skatboarder</u></p>'+
                '</div>'
    }
}



