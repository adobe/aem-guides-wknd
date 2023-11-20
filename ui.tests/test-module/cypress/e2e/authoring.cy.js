/*
 *  Copyright 2023 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

describe('validate the Wknd authoring flow', () => {
  before(() => {
    cy.AEMLogin()
    cy.AEMDisableWelcomeWizards()
  })

  let testPage
  beforeEach(() => {
    cy.AEMLogin()
    cy.WKNDCreateContentPage('/content/wknd').then(($el) => {
      // this is needed to expose the page details to all tests
      // see https://docs.cypress.io/guides/core-concepts/variables-and-aliases#Aliases-are-reset-before-each-test
      testPage = $el
    })
  })

  afterEach(() => {
    cy.AEMDeletePage(testPage.path)
  })

  after(() => {
    cy.AEMDeleteTestPages('/content/wknd')
  })

  it('should be able to see a page and change page title', function () {
    cy.AEMNavigateSites(testPage.path)

    cy.AEMNavigatePageProperties(testPage.path)
    cy.get('input[name="./jcr:title"]').clear()
    cy.get('input[name="./jcr:title"]').type(testPage.title + '-edited')
    cy.get('button[type="submit"]#shell-propertiespage-doneactivator').click()
    cy.visit('/')
    // navigate back and assess the value is stored
    cy.AEMNavigatePageProperties(testPage.path)
    cy.get('coral-panel.is-selected input[name="./jcr:title"]').should('have.value', testPage.title + '-edited')
  })

  it('should be able to author and publish a page', () => {
    // ===> add a component to the test page
    // open the page in page editor
    cy.AEMNavigatePageEditor(testPage.path)
    // open the dialog
    cy.get('div[data-type="Editable"][data-path="' + testPage.path + '/jcr:content/root/container/*"]').click()
    // clik on add
    cy.get('button[data-action="INSERT"][data-path="' + testPage.path + '/jcr:content/root/container/*"]').click()
    // select the hello world component
    cy.get('coral-list-item[value="/apps/wknd/components/helloworld"]').click()
    // verify component is in page
    cy.get('div[data-path="' + testPage.path + '/jcr:content/root/container/helloworld"]').should('exist')
    // verify component content in page
    cy.get('div[data-path="' + testPage.path + '/jcr:content/root/container/helloworld"] >span').should('contain', 'Hello World Component')

    // publish the page
    cy.AEMQuickPublish(testPage.path)

    // ===> verify content on the published page
    // note: cy.origin creates a new context, hence we need to pass the test page as a new argument.
    // also exception handling is isolated to the new context
    cy.origin(Cypress.env('AEM_PUBLISH_URL'), { args: { testPage } }, ({ testPage }) => {
      cy.on('uncaught:exception', (err) => {
        if (err.message.includes('Blocked a frame with origin')) {
          // handle a specific case where contexthub unload makes the test fail when navigating away from the page in the publish instance
          return false
        }
      })

      // check that the front pages is loadable
      cy.visit('/', { timeout: 30000 })
      // load the test page
      cy.visit('/' + testPage.title + '.html', {
        retryOnStatusCodeFailure: true
      })
      // verify the component
      cy.get('div.cmp-helloworld h2').should('contain', 'Hello World Component')
    })

    // unpublish
    cy.AEMUnpublish(testPage.path)
  })
})
