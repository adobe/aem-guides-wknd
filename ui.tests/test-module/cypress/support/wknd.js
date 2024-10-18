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

import { RandomPageTitle } from './aem' // contains some utility functions

// WKNDCreateContentPage creates a random content page, the page details can be accessed with this.wkndPage
// NOTE: Don't use an arrow function as they don't have their own bindings to this, arguments, or super,

Cypress.Commands.add('WKNDCreateContentPage', function (dir, title) {
  if (!title) {
    title = RandomPageTitle()
  }

  // remove  trailing slash from dir
  dir = dir.replace(/\/+$/, '')

  cy.AEMNavigateSites(dir)

  // popup the create overlay
  cy.get('betty-titlebar-secondary button.granite-collection-create').click()
  // click on create page
  cy.get('betty-titlebar-secondary a.cq-siteadmin-admin-createpage').click()
  // on the next screen select the content page template
  cy.get('.foundation-collection-item[data-foundation-collection-item-id="/conf/wknd/settings/wcm/templates/content-page-template"]').click()
  // click on next
  cy.get('coral-panel.is-selected button.foundation-wizard-control[data-foundation-wizard-control-action="next"]').click()

  // add a page title
  cy.get('input[name="./jcr:title"]').type(title)
  cy.get('input[name="pageName"]').type(title)

  // click on create
  cy.get('coral-panel.is-selected button.foundation-wizard-control[data-foundation-wizard-control-action="next"]').click()
  // close the done dialog, timeout increased since page creation might take a bit longer
  cy.get('coral-dialog[aria-hidden="false"] ._coral-Button--primary', { timeout: 10000 }).click()

  cy.wrap(
    { path: dir + '/' + title, title }).as('wkndPage')
})
