import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

import { expect } from '@jest/globals';

describe('AuthService Integration', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should register a user (POST /register)', () => {
    const mockRegisterRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'test',
      lastName: 'test',
      password: '123456',
    };

    service.register(mockRegisterRequest).subscribe(response => {
      expect(response).toBeUndefined(); // Le backend renvoie void donc undefined
    });

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRegisterRequest);

    req.flush(null); // Simule une réponse vide
  });

  it('should login a user (POST /login)', () => {
    const mockLoginRequest: LoginRequest = {
      email: 'test@test.com',
      password: '123456'
    };

    const mockSessionInfo: SessionInformation = {
        id: 1,
        token: 'token123456',
        admin: false,
        firstName: 'test',
        lastName: 'test',
        username: 'testUser',
        type: 'user'
    };

    service.login(mockLoginRequest).subscribe(sessionInfo => {
      expect(sessionInfo).toEqual(mockSessionInfo);
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockLoginRequest);

    req.flush(mockSessionInfo); // Simule la réponse du backend
  });
});
