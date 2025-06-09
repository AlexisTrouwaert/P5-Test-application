import { UnauthGuard } from './unauth.guard';
import { SessionService } from '../services/session.service';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';

describe('UnauthGuard (integration)', () => {
  let guard: UnauthGuard;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(() => {
    sessionServiceMock = { isLogged: false } as any;
    routerMock = { navigate: jest.fn() } as any;

    guard = new UnauthGuard(routerMock, sessionServiceMock);
  });

  it('should allow access when user is not logged in', () => {
    sessionServiceMock.isLogged = false;

    const result = guard.canActivate();

    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('should block access and redirect if user is logged in', () => {
    sessionServiceMock.isLogged = true;

    const result = guard.canActivate();

    expect(result).toBe(false);
    expect(routerMock.navigate).toHaveBeenCalledWith(['rentals']);
  });
});
