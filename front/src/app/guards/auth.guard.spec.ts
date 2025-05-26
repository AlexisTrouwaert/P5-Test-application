import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from '../services/session.service';
import { AuthGuard } from './auth.guard';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(() => {
    const routerSpy = { navigate: jest.fn() };
    const sessionServiceSpy = { isLogged: false };

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: Router, useValue: routerSpy },
        { provide: SessionService, useValue: sessionServiceSpy }
      ]
    });

    guard = TestBed.inject(AuthGuard);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  it('should redirect to login if not logged', () => {
    sessionService.isLogged = false;

    const canActivate = guard.canActivate();

    expect(canActivate).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['login']);
  });

  it('should allow activation if logged', () => {
    sessionService.isLogged = true;

    const canActivate = guard.canActivate();

    expect(canActivate).toBe(true);
    expect(router.navigate).not.toHaveBeenCalled();
  });
});