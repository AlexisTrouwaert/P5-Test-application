import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockAuthService = {
    register: jest.fn()
  };
  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call register and navigate on successful submit', () => {
    mockAuthService.register.mockReturnValue(of(void 0));
    component.form.setValue({
      email: 'test@test.com',
      firstName: 'test',
      lastName: 'test',
      password: '123456'
    });

    // Act
    component.submit();

    // Assert
    expect(mockAuthService.register).toHaveBeenCalledWith({
      email: 'test@test.com',
      firstName: 'test',
      lastName: 'test',
      password: '123456'
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true on register error', () => {
    // Arrange
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Registration failed')));
    component.form.setValue({
      email: 'test2test.com',
      firstName: 'test2',
      lastName: 'test2',
      password: '1234567'
    });

    component.submit();

    expect(component.onError).toBe(true);
  });
});
