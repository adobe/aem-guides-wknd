import '../src/main/webpack/site/main.scss';
import '../.storybook/story-styles.css';

export default {
  title: 'Layout Container',
};

export const ModalSignIn = () => new SignInLayoutContainer('cmp-layout-container--modal').markup;

ModalSignIn.story = {
  name: 'Modal (Sign In)',
};

export const ModalSignOut = () => new SignOutLayoutContainer('cmp-layout-container--modal').markup;

ModalSignOut.story = {
  name: 'Modal (Sign Out)',
};

class SignInLayoutContainer {
  constructor(styleClass) {
    this.styleClass = styleClass;
  }

  get markup() {
    return `<div class="xf-web-container">                    
                    <div class="container">
                        <div class="root responsivegrid">
                            <div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">
                                <div class="responsivegrid cmp-layout-container--modal aem-GridColumn aem-GridColumn--default--12">
                                    <div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">
                                        <div class="title cmp-title--underline aem-GridColumn aem-GridColumn--default--12">
                                            <div class="cmp-title">
                                                <h1 class="cmp-title__text">Sign In</h1>
                                            </div>
                                        </div>
                                        <div class="title aem-GridColumn aem-GridColumn--default--12">
                                            <div class="cmp-title">
                                                <h3 class="cmp-title__text">Welcome Back</h3>
                                            </div>
                                        </div>
                                        <div class="sign-in-form aem-GridColumn aem-GridColumn--default--12">
                                            <div class="wknd-sign-in-form">
                                                <form method="POST" action="/system/sling/login/j_security_check" id="wknd-sign-in-form"
                                                      class="wknd-sign-in-form__form">
                                                    <input type="hidden" name="charset" value="🐶"/>
                                                    <input type="hidden" name="j_validate" value="true"/>
                                                    <input type="hidden" name="sling.auth.redirect"
                                                           value="/content/wknd/language-masters/en.html"/>
                                                    <div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">
                                                        <div class="text aem-GridColumn aem-GridColumn--default--12">
                                                            <div class="cmp-form-text" data-cmp-is="formText">
                
                
                                                                <input class="cmp-form-text__text" data-cmp-hook-form-text="input"
                                                                       aria-label="EMAIL" type="text" id="form-text-1660183001"
                                                                       placeholder="EMAIL" name="j_username"/>
                                                            </div>
                                                        </div>
                                                        <div class="text aem-GridColumn aem-GridColumn--default--12">
                                                            <div class="cmp-form-text" data-cmp-is="formText">
                
                
                                                                <input class="cmp-form-text__text" data-cmp-hook-form-text="input"
                                                                       aria-label="PASSWORD" type="password" id="form-text-1010823116"
                                                                       placeholder="PASSWORD" name="j_password"/>
                                                            </div>
                                                        </div>
                                                        <div class="text aem-GridColumn aem-GridColumn--default--12">
                                                            <div class="cmp-text">
                                                                <p><a href="#">FORGOT YOUR PASSWORD?</a></p>
                
                                                            </div>
                                                        </div>
                                                        <div class="button aem-GridColumn aem-GridColumn--default--12">
                                                            <button type="SUBMIT" class="cmp-form-button">SIGN IN</button>
                                                        </div>
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
                                                <p>NEED AN ACCOUNT? <a href="#sign-up">SIGN UP</a></p>
                                            </div>
                                        </div>
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
                        <div class="root responsivegrid">
                            <div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">
                                <div class="responsivegrid cmp-layout-container--modal aem-GridColumn aem-GridColumn--default--12">
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
                                        <div class="sign-in-form aem-GridColumn aem-GridColumn--default--12">
                                            <div class="wknd-sign-in-form">
                                                <form method="POST" action="/system/sling/login/j_security_check" id="wknd-sign-in-form" class="wknd-sign-in-form__form">
                                                    <input type="hidden" name="charset" value="🐶"/>
                                                    <input type="hidden" name="j_validate" value="true"/>
                                                    <input type="hidden" name="sling.auth.redirect"/>
                                                    <div class="aem-Grid aem-Grid--12 aem-Grid--default--12 ">
                                                        <div class="button aem-GridColumn aem-GridColumn--default--12">
                                                            <button type="SUBMIT" class="cmp-form-button" name="resource" value="/content/wknd/us/en.html">SIGN OUT</button>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                        <div class="separator aem-GridColumn aem-GridColumn--default--12">
                                            <div class="cmp-separator">
                                                <hr class="cmp-separator__horizontal-rule"/>
                                            </div></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                 </div>`;
  }
}
