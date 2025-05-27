/// <reference types="cypress" />

describe('Login spec', () => {
  it('Should have delete button if admin', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("test@test.fr")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.contains('button', 'Detail').click();
    cy.url().should('match', /\/detail\/\d+$/);
    cy.contains('button span', 'Delete').should('not.exist');
  })
});