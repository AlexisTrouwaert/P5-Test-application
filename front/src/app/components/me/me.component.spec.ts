import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUser = { 
    id: 1, 
    username: 'test', 
    email: 'test@test.com',
    firstName: 'test',
    lastName: 'test',
    admin: true,
    createdAt: new Date(),
    updatedAt: new Date()
  };
  
  const mockSessionService = {
    sessionInformation: { id: 1, admin: true },
    logOut: jest.fn()
  };

  const mockUserService = {
    getById: jest.fn(),
    delete: jest.fn()
  };

  const mockSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user on ngOnInit', () => {
    mockUserService.getById.mockReturnValue(of(mockUser));
    fixture.detectChanges();
    component.ngOnInit();

    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should call window.history.back on back()', () => {
    // Mock window.history.back()
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    fixture.detectChanges();
    component.back();

    expect(backSpy).toHaveBeenCalled();

    backSpy.mockRestore();
  });

  it('should delete user and navigate on delete()', fakeAsync(() => {
    mockUserService.delete.mockReturnValue(of({}));
    fixture.detectChanges();
    component.delete();
    tick(); // simule le passage du temps pour l'observable

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  }));

});
