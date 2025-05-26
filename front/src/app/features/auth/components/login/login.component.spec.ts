import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockAuthService = {
    login: jest.fn()
  };
  const mockSessionService = {
    logIn: jest.fn()
  };
  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule],
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call login and navigate on successful submit', () => {
    // Arrange
    const fakeResponse = { id: '123', token: 'token', email: 'test@test.com' };
    mockAuthService.login.mockReturnValue(of(fakeResponse));
    component.form.setValue({ email: 'test@test.com', password: '123456' });

    // Act
    component.submit();

    // Assert
    expect(mockAuthService.login).toHaveBeenCalledWith({ email: 'test@test.com', password: '123456' });
    expect(mockSessionService.logIn).toHaveBeenCalledWith(fakeResponse);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true on login error', () => {
    // Arrange
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Invalid login')));
    component.form.setValue({ email: 'fail@test.com', password: 'fail' });

    // Act
    component.submit();

    // Assert
    expect(component.onError).toBe(true);
  });
});
