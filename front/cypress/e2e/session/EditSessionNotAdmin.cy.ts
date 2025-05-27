/// <reference types="cypress" />

describe('Login spec', () => {
  it('Should have create button if admin', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("test@test.fr")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.get('body').then(($body) => {
          if ($body.find('button span:contains("Edit")').length) {
            cy.contains('button', 'Edit').should('not.exist');
          }
        });
  })
});