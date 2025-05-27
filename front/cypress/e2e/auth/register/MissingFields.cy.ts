/// <reference types="cypress" />

describe('Login spec', () => {
  it('Missing fields', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type(`${"test"}`)
    cy.get('input[formControlName=lastName]').type(`${"test"}`)
    cy.get('input[formControlName=email]').type(`${"test@test.fr"}`)

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=firstName]').type(`${"test"}`)
    cy.get('input[formControlName=lastName]').type(`${"test"}`)
    cy.get('input[formControlName=password]').type(`${"test"}`)
    cy.get('input[formControlName=email]').clear()

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=firstName]').type(`${"test"}`)
    cy.get('input[formControlName=lastName]').clear()
    cy.get('input[formControlName=email]').type(`${"test"}`)
    cy.get('input[formControlName=password]').type(`${"test"}`)

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=firstName]').clear()
    cy.get('input[formControlName=lastName]').type(`${"test"}`)
    cy.get('input[formControlName=email]').type(`${"test@test.fr"}`)
    cy.get('input[formControlName=password]').type(`${"test"}`)

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=firstName]').type(`${"test"}`)

    cy.get('button[type=submit]').should('not.be.disabled');
  })
});