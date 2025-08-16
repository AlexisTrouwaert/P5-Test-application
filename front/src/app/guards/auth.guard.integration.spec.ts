import { AuthGuard } from './auth.guard';
import { SessionService } from '../services/session.service';
import { Router, Routes } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionsModule } from '../features/sessions/sessions.module';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';

describe('AuthGuard (integration)', () => {

  let router: Router;
  let sessionService: SessionService;

  @Component({ template: 'Page de session' })
class MockSessionComponent {}

@Component({ template: 'Page de login' })
class MockLoginComponent {}

  const routes: Routes = [
    { path: 'sessions', component: MockSessionComponent, canActivate: [AuthGuard] },
    { path: 'login', component: MockLoginComponent }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        MockSessionComponent, 
        MockLoginComponent
      ],
      imports: [RouterTestingModule.withRoutes(routes)],
      providers: [AuthGuard, SessionService]
    });
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  it('should allow activation if user is logged in', async () => {
    sessionService.isLogged = true;

    await router.navigate(['/sessions']);

    expect(router.url).toBe('/sessions')
  });

  it('should prevent activation and redirect if user is not logged in', async () => {
    sessionService.isLogged = false;
    
    await router.navigate(['/sessions']);

    expect(router.url).toBe('/login');
  });
});
