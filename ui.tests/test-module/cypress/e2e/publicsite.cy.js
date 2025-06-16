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

describe('validate the Wknd public site', () => {
  it('front page should load', () => {
    // note: cy.origin creates a new context, hence we need to pass the test page as a new argument.
    // also exception handling is isolated to the new context
    cy.origin(Cypress.env('AEM_PUBLISH_URL'), () => {
      cy.on('uncaught:exception', (err) => {
        if (err.message.includes('Blocked a frame with origin')) {
          // handle a specific case where contexthub unload makes the test fail when navigating away from the page in the publish instance
          return false
        }
      })

      cy.visit('/')
      cy.get('header').should('exist')
      cy.get('footer').should('exist')
      // hero image on front page should be visible
      cy.get('.cmp-carousel__item--active  .teaser.cmp-teaser--hero img').should('be.visible')
    })
  })

  it('search should have search results', () => {
    // note: cy.origin creates a new context, hence we need to pass the test page as a new argument.
    // also exception handling is isolated to the new context
    cy.origin(Cypress.env('AEM_PUBLISH_URL'), () => {
      cy.on('uncaught:exception', (err) => {
        if (err.message.includes('Blocked a frame with origin')) {
          // handle a specific case where contexthub unload makes the test fail when navigating away from the page in the publish instance
          return false
        }
      })
      cy.visit('/')
      cy.get('.cmp-search__field').type('Climbing')
      cy.get('.cmp-search__results a').should('have.length.least', 1)
    })
  })
})