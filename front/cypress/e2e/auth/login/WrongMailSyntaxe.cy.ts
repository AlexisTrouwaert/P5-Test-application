/// <reference types="cypress" />

describe('Login spec', () => {
  it('Wrong mail syntaxe', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.get('input[formControlName=email]').type(`${"test"}`)
    cy.get('input[formControlName=password]').type(`${"test"}`)

    cy.get('button[type=submit]').should('be.disabled');
  })
});