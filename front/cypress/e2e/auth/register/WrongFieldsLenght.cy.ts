/// <reference types="cypress" />

describe('Login spec', () => {
  it('Missing fields', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type(`${"te"}`)
    cy.get('input[formControlName=lastName]').type(`${"test"}`)
    cy.get('input[formControlName=email]').type(`${"test@test.fr"}`)
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.get('button[type=submit]').click();
    cy.contains('An error occurred').should('be.visible')

    cy.get('input[formControlName=firstName]').clear().type(`${"test"}`)
    cy.get('input[formControlName=lastName]').clear().type(`${"te"}`)

    cy.get('button[type=submit]').click();
    cy.contains('An error occurred').should('be.visible')

    cy.get('input[formControlName=firstName]').type(`${"test"}`)
    cy.get('input[formControlName=lastName]').clear().type(`${"test"}`)
    cy.get('input[formControlName=password]').clear().type(`${"ts"}`)

    cy.get('button[type=submit]').click();
    cy.contains('An error occurred').should('be.visible')

    cy.get('input[formControlName=firstName]').type(`${"testazertyuiopqsdfghj"}`)
    cy.get('input[formControlName=password]').clear().type(`${"test!1234"}`)

    cy.get('button[type=submit]').click();
    cy.contains('An error occurred').should('be.visible')

    cy.get('input[formControlName=firstName]').clear().type(`${"test"}`)
    cy.get('input[formControlName=lastName]').clear().type(`${"testazertyuiopqsdfghj"}`)

    cy.get('button[type=submit]').click();
    cy.contains('An error occurred').should('be.visible')

    cy.get('input[formControlName=lastName]').clear().type(`${"test"}`)
    cy.get('input[formControlName=password]').clear().type(`${"testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest"}`)

    cy.get('button[type=submit]').click();
    cy.contains('An error occurred').should('be.visible')
  })
});