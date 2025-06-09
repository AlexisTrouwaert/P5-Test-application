import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

describe('LoginComponent (integration)', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let mockAuthService: jest.Mocked<AuthService>;
  let mockRouter: jest.Mocked<Router>;
  let mockSessionService: jest.Mocked<SessionService>;

  beforeEach(async () => {
    mockAuthService = { login: jest.fn() } as any;
    mockRouter = { navigate: jest.fn() } as any;
    mockSessionService = { logIn: jest.fn() } as any;

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should log in and redirect on success', () => {
    const fakeSession: SessionInformation = { id: 1, token: 'abc' } as any;
    mockAuthService.login.mockReturnValue(of(fakeSession));

    component.form.setValue({ email: 'test@mail.com', password: '1234' });
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({ email: 'test@mail.com', password: '1234' });
    expect(mockSessionService.logIn).toHaveBeenCalledWith(fakeSession);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('should show error on failed login', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('401')));

    component.form.setValue({ email: 'test@mail.com', password: 'wrong' });
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });
});
