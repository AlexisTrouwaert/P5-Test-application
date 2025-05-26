import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { UnauthGuard } from './unauth.guard';
import { SessionService } from '../services/session.service';
import { expect } from '@jest/globals';

describe('UnauthGuard', () => {
  let guard: UnauthGuard;
  let mockRouter: { navigate: jest.Mock };
  let mockSessionService: Partial<SessionService>;

  beforeEach(() => {
    mockRouter = { navigate: jest.fn() };
    mockSessionService = { isLogged: false };

    TestBed.configureTestingModule({
      providers: [
        UnauthGuard,
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
      ],
    });

    guard = TestBed.inject(UnauthGuard);
  });

  it('should allow activation if user is NOT logged in', () => {
    mockSessionService.isLogged = false;

    expect(guard.canActivate()).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should deny activation and redirect if user IS logged in', () => {
    mockSessionService.isLogged = true;

    expect(guard.canActivate()).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['rentals']);
  });
});
