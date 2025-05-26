import { AuthService } from './auth.service';
import { HttpClient } from '@angular/common/http';
import { first, of } from 'rxjs';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let authService: AuthService;
  let httpClientMock: jest.Mocked<HttpClient>;

  beforeEach(() => {
    httpClientMock = {
      post: jest.fn(),
    } as any;

    authService = new AuthService(httpClientMock);
  });

  it('should call httpClient.post on register with correct url and body', () => {
    const registerRequest = { email: 'test@test.com', password: '123456', firstName: 'test', lastName: 'test' };
    httpClientMock.post.mockReturnValue(of(void 0));

    authService.register(registerRequest).subscribe();

    expect(httpClientMock.post).toHaveBeenCalledWith(
      'api/auth/register',
      registerRequest
    );
  });

  it('should call httpClient.post on login with correct url and body', () => {
    const loginRequest = { email: 'test@test.com', password: '123456' };
    const sessionInformation = { token: 'abc123', id: 1, admin: false };
    httpClientMock.post.mockReturnValue(of(sessionInformation));

    authService.login(loginRequest).subscribe(result => {
      expect(result).toEqual(sessionInformation);
    });

    expect(httpClientMock.post).toHaveBeenCalledWith(
      'api/auth/login',
      loginRequest
    );
  });
});
