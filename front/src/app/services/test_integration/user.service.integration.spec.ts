import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from '../user.service';
import { User } from '../../interfaces/user.interface';
import { expect } from '@jest/globals';

describe('UserService Integration', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],  // Module Angular complet pour HTTP
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();  // Vérifie qu’il n’y a pas de requêtes HTTP non consommées
  });

  it('should fetch user by id and handle observable properly', (done) => {
    const mockUser: User = {
      id: 1,
      email: 'test@test.fr',
      firstName: 'Test',
      lastName: 'User',
      password: 'test123456',
      createdAt: new Date(),
      updatedAt: new Date(),
      admin: false
    };

    service.getById('abc123').subscribe(user => {
      expect(user).toEqual(mockUser);
      done();  // Important : signale que le test asynchrone est terminé
    });

    const req = httpMock.expectOne('api/user/abc123');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should delete user and complete observable', (done) => {
    service.delete('abc123').subscribe(response => {
      expect(response).toBeTruthy();
      done();
    });

    const req = httpMock.expectOne('api/user/abc123');
    expect(req.request.method).toBe('DELETE');
    req.flush({ success: true });
  });
});
