import { JwtInterceptor } from './jwt.interceptor';
import { SessionService } from '../services/session.service';
import { HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { of, Observable } from 'rxjs';
import { expect } from '@jest/globals';

describe('JwtInterceptor', () => {
  let interceptor: JwtInterceptor;
  let sessionService: Partial<SessionService>;
  let next: HttpHandler;
  const mockHttpEvent: HttpEvent<any> = {} as HttpEvent<any>;

  beforeEach(() => {
    sessionService = {
      isLogged: false,
      sessionInformation: { token: 'dummy-token' } as any,
    };
    interceptor = new JwtInterceptor(sessionService as SessionService);


    next = {
    handle: jest.fn((req: HttpRequest<any>): Observable<HttpEvent<any>> => of(mockHttpEvent)),
    };
  });

  it('should not add Authorization header if not logged in', () => {
    sessionService.isLogged = false;

    const request = new HttpRequest('GET', '/test');
    const result = interceptor.intercept(request, next);

    expect(next.handle).toHaveBeenCalledWith(request);
    expect(result).toBeInstanceOf(Observable);
  });

  it('should add Authorization header if logged in', () => {
    sessionService.isLogged = true;

    const request = new HttpRequest('GET', '/test');
    interceptor.intercept(request, next);

    const calledRequest = (next.handle as jest.Mock).mock.calls[0][0];

    expect(calledRequest.headers.has('Authorization')).toBe(true);
    expect(calledRequest.headers.get('Authorization')).toBe('Bearer dummy-token');
  });
});
