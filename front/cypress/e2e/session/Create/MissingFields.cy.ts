/// <reference types="cypress" />

describe('Login spec', () => {
  it('Missing fields', () => {
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
    cy.get('textarea[formcontrolname="description"]').type('test description');

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=name]').clear();
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').first().click();

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=name]').type(`${"test"}`)
    cy.get('input[formcontrolname="date"]').clear();

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formcontrolname="date"]').type('2025-06-10');
    cy.get('textarea[formcontrolname="description"]').clear();

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=name]').type(`${"test"}`)
    cy.get('input[formcontrolname="date"]').type('2025-06-10');
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').first().click();
    cy.get('textarea[formcontrolname="description"]').type('test description');

    cy.get('button[type=submit]').should('not.be.disabled');
  })
});