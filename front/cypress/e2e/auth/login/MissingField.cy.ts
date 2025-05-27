/// <reference types="cypress" />

describe('Login spec', () => {
  it('Missing field', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').should('be.disabled');
  })
});