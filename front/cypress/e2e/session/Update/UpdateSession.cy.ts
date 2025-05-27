/// <reference types="cypress" />

describe('Login spec', () => {
  it('Should have edit button if admin', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    
    cy.get('.list .item').should('have.length.at.least', 1);
    cy.get('body').then(($body) => {
          if ($body.find('button span:contains("Edit")').length) {
            cy.contains('button', 'Edit').click();
          }
    });

    cy.get('textarea[formcontrolname="description"]').clear();
    cy.get('textarea[formcontrolname="description"]').type('test description 2');

    cy.get('button[type=submit]').should('not.be.disabled');
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions')

    cy.contains('button', 'Detail').click();

    cy.get('.description')
        .should('contain.text', 'Description: test description 2');
    
    cy.get('button mat-icon')
        .contains('arrow_back')
        .click();
    
    cy.get('.list .item').should('have.length.at.least', 1);
    cy.get('body').then(($body) => {
          if ($body.find('button span:contains("Edit")').length) {
            cy.contains('button', 'Edit').click();
          }
    });

    cy.get('textarea[formcontrolname="description"]').clear();
    cy.get('textarea[formcontrolname="description"]').type('test description');

    cy.get('button[type=submit]').should('not.be.disabled');
    cy.get('button[type=submit]').click();
  })
});