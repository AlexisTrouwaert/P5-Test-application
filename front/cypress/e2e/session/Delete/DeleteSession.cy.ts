/// <reference types="cypress" />

describe('Login spec', () => {
  it('Wrong description lenght', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.get('button')
      .contains('Create')
      .click();
    cy.url().should('include', '/sessions/create')

    cy.get('input[formControlName=name]').type(`${"test"}`)
    cy.get('input[formcontrolname="date"]').type('2025-06-10');
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').first().click();
    cy.get('textarea[formcontrolname="description"]').type('test description');

    cy.get('button[type=submit]').click();
    cy.url().should('include', '/sessions')

    cy.get('mat-card.item').last().within(() => {
        cy.contains('button', 'Detail').click();
    });
    cy.contains('button span', 'Delete').click();

    cy.url().should('include', '/sessions')

    cy.get('.list .item').should('have.length', 1);
  })
});