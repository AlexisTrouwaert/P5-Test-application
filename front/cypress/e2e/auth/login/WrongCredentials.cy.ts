/// <reference types="cypress" />

describe('Login spec', () => {
  it('Wrong credentials', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yogazdaa11@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)
    cy.get('button[type=submit]').click();

    cy.contains('An error occurred').should('be.visible')

    cy.url().should('include', '/login')
  })
});