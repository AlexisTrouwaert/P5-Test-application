/// <reference types="cypress" />

describe('Login spec', () => {
  it('Wrong description lenght', () => {
    cy.request({
        method: 'POST',
        url: '/api/session',
        failOnStatusCode: false
    })

    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.get('button')
      .contains('Create')
      .click();
    cy.url().should('include', '/sessions/create')

    const longDescription = 'a'.repeat(2501);

    cy.get('input[formControlName=name]').type(`${"test"}`)
    cy.get('input[formcontrolname="date"]').type('2025-06-10');
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').first().click();
    cy.get('textarea[formcontrolname="description"]').invoke('val', longDescription)
    .trigger('input');

    cy.get('button[type=submit]').click();
  })
});