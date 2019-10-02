import {storiesOf} from '@storybook/html';
import '../src/site/main.scss';
import '../.storybook/story-styles.css';

storiesOf('Layout Container', module)
    .add('Modal (Sign In)', () => new SignInLayoutContainer("cmp-layout-container--modal").markup)
    .add('Modal (Sign Out)', () => new SignOutLayoutContainer("cmp-layout-container--modal").markup);

class SignInLayoutContainer {
    constructor(styleClass) {
        this.styleClass = styleClass;
    }

    get markup() {
        return `<div class="xf-web-container">                    
                    <div class="container">
                        <div class="root responsivegrid ${this.styleClass}">
                   
                            <div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">
                    
                                <div class="title cmp-title--underline aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-title">
                                        <h1 class="cmp-title__text">Sign in</h1>
                                    </div>
                                </div>
                                <div class="title aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-title">
                                        <h3 class="cmp-title__text">Welcome Back</h3>
                                    </div>
                                </div>
                                <div class="log-in aem-GridColumn aem-GridColumn--default--12">
                                    <div class="wknd-sign-in-form">
                                        <form method="POST" action="/libs/granite/core/content/login.html/j_security_check"
                                              class="wknd-sign-in-form__form">
                                            <input type="hidden" name="charset" value="🐶"/>
                                            <input type="hidden" name="resource" value="./"/>
                                            <input type="hidden" name="j_validate" value="true"/>
                    
                                            <div class="wknd-sign-in-form__field wknd-sign-in-form__field--text-input">
                                                <div class="cmp-form-text" data-cmp-is="formText">
                                                    <input class="cmp-form-text__text" data-cmp-hook-form-text="input" aria-label="EMAIL"
                                                           type="text" id="form-text-1976236455" placeholder="EMAIL" name="j_username"
                                                           required/>
                                                </div>
                                            </div>
                                            <div class="wknd-sign-in-form__field wknd-sign-in-form__field--text-input">
                                                <div class="cmp-form-text" data-cmp-is="formText">
                                                    <input class="cmp-form-text__text" data-cmp-hook-form-text="input" aria-label="PASSWORD"
                                                           type="text" id="form-text-1499668257" placeholder="PASSWORD" name="j_password"
                                                           required/>
                                                </div>
                                            </div>
                                            <div class="wknd-sign-in-form__field wknd-sign-in-form__field--text">
                                                <div class="cmp-text">
                                                    <a href="#">FORGOT YOUR PASSWORD?</a>
                                                </div>
                                            </div>
                                            <div class="wknd-sign-in-form__field wknd-sign-in-form__field--button">
                                                <button type="SUBMIT" class="cmp-form-button">SIGN IN</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="separator aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-separator">
                                        <hr class="cmp-separator__horizontal-rule"/>
                                    </div>
                                </div>
                                <div class="text aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-text">
                                        <p>NEED AN ACCOUNT? <a href="#">SIGN UP</a></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                 </div>`;

    }
}


class SignOutLayoutContainer {
    constructor(styleClass) {
        this.styleClass = styleClass;
    }

    get markup() {
        return `<div class="xf-web-container">                    
                    <div class="container">
                        <div class="root responsivegrid ${this.styleClass}">

                            <div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">
                                
                                <div class="title cmp-title--underline aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-title">
                                        <h1 class="cmp-title__text">Sign Out</h1>
                                    </div>
                                </div>
                                <div class="title aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-title">
                                        <h3 class="cmp-title__text">Goodbye!</h3>
                                    </div>
                                </div>
                                <div class="text aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-text">
                                        <p>Thanks for stopping by, come back soon!</p>                                    
                                    </div>
                                </div>
                                <div class="separator aem-GridColumn aem-GridColumn--default--12">
                                    <div class="cmp-separator">
                                        <hr class="cmp-separator__horizontal-rule"/>
                                    </div>
                                </div>
                                <div class="button aem-GridColumn aem-GridColumn--default--12">
                                    <a class="cmp-button" href="/system/sling/logout" aria-label="Sign Out">
                                        <span class="cmp-button__text">SIGN OUT</span>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                 </div>`;

    }
}


