import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  const mockSessionInformation: SessionInformation = {
    id: 1,
    username: 'test',
    token: 'test-token',
    type: 'user',
    firstName: 'test',
    lastName: 'test',
    admin: true,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initially be logged out', () => {
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should update state on logIn', () => {
    service.logIn(mockSessionInformation);

    expect(service.isLogged).toBeTruthy();
    expect(service.sessionInformation).toEqual(mockSessionInformation);
  });

  it('should emit true after logIn', (done) => {
    service.$isLogged().subscribe((isLogged) => {
      if (isLogged) {
        expect(isLogged).toBeTruthy();
        done();
      }
    });

    service.logIn(mockSessionInformation);
  });

  it('should update state on logOut', () => {
    service.logIn(mockSessionInformation);
    service.logOut();

    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit false after logOut', (done) => {
    service.logIn(mockSessionInformation);

    const emitted: boolean[] = [];

    service.$isLogged().subscribe((isLogged) => {
      emitted.push(isLogged);
      if (emitted.length === 2) {
        expect(emitted[0]).toBeTruthy(); // après login
        expect(emitted[1]).toBeFalsy();  // après logout
        done();
      }
    });

    service.logOut();
  });
});
