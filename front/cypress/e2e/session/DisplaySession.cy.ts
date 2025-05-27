/// <reference types="cypress" />

describe('Login spec', () => {
  it('Display sessions', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.get('.list .item').should('have.length.at.least', 1);
    cy.get('mat-card-title').should('exist');
    cy.get('mat-card-subtitle').should('contain.text', 'Session on');
    cy.get('img.picture').should('have.attr', 'src', 'assets/sessions.png');
    cy.get('mat-card-content p').should('exist');
    cy.contains('button', 'Detail').should('exist');
  })
});