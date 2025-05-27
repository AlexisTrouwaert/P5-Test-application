/// <reference types="cypress" />

describe('Login spec', () => {
  it('Login successfull with bdd', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')

    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me')

    cy.get('mat-card-title h1').should('have.text', 'User information');

    cy.get('mat-card-content p').eq(0).should('have.text', 'Name: Admin ADMIN');

    cy.get('mat-card-content p').eq(1).should('have.text', 'Email: yoga@studio.com');

    cy.get('mat-card-content p.my2').should('have.text', 'You are admin');

    cy.get('mat-card-content div.p2 p').eq(0).should('contain.text', 'Create at:  April 29, 2025');

    cy.get('mat-card-content div.p2 p').eq(1).should('contain.text', 'Last update:  April 29, 2025');

  })
});