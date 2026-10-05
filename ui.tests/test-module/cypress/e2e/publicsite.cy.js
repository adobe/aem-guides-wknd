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

// This spec only visits publish; avoid cross-origin serialization of JSON-valued AEM attributes.
Cypress.config('baseUrl', Cypress.env('AEM_PUBLISH_URL'))

describe('validate the Wknd public site', () => {
  beforeEach(() => {
    cy.on('uncaught:exception', (err) => {
      if (err.message.includes('Blocked a frame with origin')) {
        // ContextHub can raise this exception when unloading a publish page.
        return false
      }
    })
  })

  it('front page should load', () => {
    cy.visit('/')
    cy.get('header').should('exist')
    cy.get('footer').should('exist')
    cy.get('.cmp-carousel__item--active  .teaser.cmp-teaser--hero img').should('be.visible')
  })

  const viewports = [[1025, 800], [1200, 800], [1280, 800], [1728, 996], [390, 844]]
  viewports.forEach(([width, height]) => {
    it(`search icon should open the central V1 page at ${width}px`, () => {
      cy.viewport(width, height)
      cy.visit('/')
      cy.get('header .cmp-search__field').should('not.exist')
      cy.get('header .cmp-navigation__item-link').each(($link) => {
        expect($link.attr('href')).not.to.match(/\/us\/en\/ai-powered-search\.html$/)
      })
      cy.get('header .cmp-button--header-search a[aria-label="Search"]')
        .should('have.length', 1)
        .and('be.visible')
        .should('have.attr', 'href')
        .and('match', /^\/(?:content\/wknd\/)?us\/en\/ai-powered-search\.html$/)
      cy.get('header a[aria-label="Search"] .cmp-button__text').should('not.be.visible')
      cy.get('header a[aria-label="Search"] .cmp-button__icon--search').should(($icon) => {
        const icon = $icon[0]
        const style = icon.ownerDocument.defaultView.getComputedStyle(icon, '::before')
        expect(style.content).to.contain('\ue913')
        const bounds = icon.closest('a').getBoundingClientRect()
        expect(bounds.width).to.be.at.least(44)
        expect(bounds.height).to.be.at.least(44)
        expect(bounds.left).to.be.at.least(0)
        expect(bounds.right).to.be.at.most(width)
        if (width > 1024) {
          const links = [...icon.ownerDocument.querySelectorAll('header .cmp-navigation__item--level-1 > .cmp-navigation__item-link')]
            .filter(link => link.getBoundingClientRect().width > 0)
          expect(links).not.to.be.empty
          const lastLink = links[links.length - 1].getBoundingClientRect()
          expect(bounds.left - lastLink.right, 'gap between navigation and search hit areas').to.be.within(0, 8)
          expect(Math.abs((bounds.top + bounds.height / 2) - (lastLink.top + lastLink.height / 2)), 'vertical alignment').to.be.at.most(2)
        }
      })
      cy.get('header a[aria-label="Search"]').click()
      cy.location('pathname').should('match', /^\/(?:content\/wknd\/)?us\/en\/ai-powered-search\.html$/)
      cy.get('main .cmp-contentaisearch').should('be.visible')
      cy.get('main .cmp-contentaisearch__ai-toggle-input').should('be.checked').uncheck()
      cy.get('main .cmp-contentaisearch__ai-toggle-input').should('not.be.checked').check()
      cy.get('main .cmp-contentaisearch__ai-toggle-input').should('be.checked')
    })
  })

  it('central V1 search should return and display search results', () => {
    cy.intercept('GET', '**/contentaisearch.search.json*').as('searchResults')
    cy.intercept('GET', '**/contentaisearch.gensearch.json*').as('aiSummary')
    cy.visit('/us/en/ai-powered-search.html')
    cy.get('main .cmp-contentaisearch__ai-toggle-input').should('be.checked').uncheck()
    cy.get('main .cmp-contentaisearch__input').type('Climbing{enter}')
    cy.wait('@searchResults', { responseTimeout: 60000 }).then(({ response }) => {
      expect(response.statusCode).to.equal(200)
      expect(response.body.results).to.be.an('array').and.have.length.greaterThan(0)
      cy.get('main [data-cmp-hook-contentaisearch="results"] [data-cmp-hook-contentaisearch="item"]')
        .should('have.length', response.body.results.length)
        .first().should('be.visible')
      cy.get('main .cmp-contentaisearch__ai-toggle-input').check()
    })
    cy.wait('@aiSummary', { responseTimeout: 60000 }).then(({ response }) => {
      expect(response.statusCode).to.equal(200)
      expect(response.body.result).to.be.a('string').and.not.be.empty
      expect(response.body.hits).to.be.an('array').and.have.length.greaterThan(0)
    })
    cy.get('main [data-cmp-hook-contentaisearch="summary"]').should('be.visible')
    cy.get('main [data-cmp-hook-contentaisearch="summaryText"]').should('be.visible').and('not.be.empty')
  })
})
