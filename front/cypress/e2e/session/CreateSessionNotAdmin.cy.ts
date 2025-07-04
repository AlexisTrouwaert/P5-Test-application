/// <reference types="cypress" />

describe('Login spec', () => {
  it('Should have create button if admin', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("test@test.fr")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.get('button')
      .contains('Create')
      .should('not.exist');
  })
});