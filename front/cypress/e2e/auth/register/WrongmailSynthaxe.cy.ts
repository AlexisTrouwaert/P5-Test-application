/// <reference types="cypress" />

describe('Login spec', () => {
  it('Missing fields', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type(`${"test"}`)
    cy.get('input[formControlName=lastName]').type(`${"test"}`)
    cy.get('input[formControlName=email]').type(`${"test"}`)
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').should('be.disabled');
  })
});