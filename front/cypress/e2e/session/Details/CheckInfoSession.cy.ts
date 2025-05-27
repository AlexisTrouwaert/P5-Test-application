/// <reference types="cypress" />

describe('Login spec', () => {
  it('Check display info session', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.contains('button', 'Detail').click();
    
    cy.get('mat-card-content')
        .contains('calendar_month')
        .parents('[fxlayoutalign="space-between center"]')
        .first()
        .within(() => {
            cy.get('span.ml1').should('not.be.empty');
        });

    cy.get('.description')
        .should('contain.text', 'Description:')
        .and('not.be.empty');

    cy.get('.created')
        .should('contain.text', 'Create at:')
        .and('not.be.empty');

    cy.get('.updated')
        .should('contain.text', 'Last update:')
        .and('not.be.empty');

        cy.get('mat-card-title h1').should('not.be.empty');

    cy.get('mat-card-subtitle span')
        .should('exist')
        .and('not.be.empty');

    cy.get('img.picture')
        .should('have.attr', 'src')
        .and('include', 'sessions.png');

    cy.contains('span', 'attendees')
        .should('exist')
        .invoke('text')
        .should('match', /\d+/);
  })
});