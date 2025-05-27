/// <reference types="cypress" />

describe('Logout', () => {
  it('passes', () => {
    cy.visit('http://localhost:4200/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.contains('span', 'Logout').click();
    cy.url().should('eq', 'http://localhost:4200/')
  })
})