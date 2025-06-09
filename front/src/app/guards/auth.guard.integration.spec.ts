import { AuthGuard } from './auth.guard';
import { SessionService } from '../services/session.service';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';

describe('AuthGuard (integration)', () => {
  let guard: AuthGuard;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(() => {
    sessionServiceMock = { isLogged: false } as any;
    routerMock = { navigate: jest.fn() } as any;

    guard = new AuthGuard(routerMock, sessionServiceMock);
  });

  it('should allow activation if user is logged in', () => {
    sessionServiceMock.isLogged = true;

    const result = guard.canActivate();

    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('should prevent activation and redirect if user is not logged in', () => {
    sessionServiceMock.isLogged = false;

    const result = guard.canActivate();

    expect(result).toBe(false);
    expect(routerMock.navigate).toHaveBeenCalledWith(['login']);
  });
});
