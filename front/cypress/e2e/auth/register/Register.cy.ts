/// <reference types="cypress" />

describe('Login spec', () => {
  it('Register succesfull', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {
        id: 1,
        username: 'test',
        firstName: 'test',
        lastName: 'test',
        email: 'test@test.fr',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=firstName]').type(`${"test"}`)
    cy.get('input[formControlName=lastName]').type(`${"test"}`)
    cy.get('input[formControlName=email]').type(`${"test@test.fr"}`)
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click();
    cy.url().should('include', '/login')
  })
});