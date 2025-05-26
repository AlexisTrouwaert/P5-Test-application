import { TestBed } from '@angular/core/testing';
import { SessionService } from '../session.service';
import { SessionInformation } from '../../interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('SessionService (Integration)', () => {
  let service: SessionService;

  const user: SessionInformation = {
    id: 1,
    username: 'test',
    token: 'test-token',
    type: 'user',
    firstName: 'test',
    lastName: 'test',
    admin: true,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService]
    });

    service = TestBed.inject(SessionService);
  });

  it('should be injected properly', () => {
    expect(service).toBeDefined();
  });

  it('should persist state in service across components/services (integration test)', () => {
    service.logIn(user);

    const anotherInstance = TestBed.inject(SessionService);
    expect(anotherInstance.isLogged).toBeTruthy();
    expect(anotherInstance.sessionInformation).toEqual(user);
  });

  it('should emit values via observable', (done) => {
    service.logIn(user);

    service.$isLogged().subscribe(value => {
      expect(value).toBeTruthy();
      done();
    });
  });
});
