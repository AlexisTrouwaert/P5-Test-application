import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { of } from 'rxjs';
import { User } from '../../interfaces/user.interface';
import { expect } from '@jest/globals';

describe('MeComponent (integration)', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  // Mocks
  let mockRouter: jest.Mocked<Router>;
  let mockSnackBar: jest.Mocked<MatSnackBar>;
  let mockSessionService: Partial<SessionService>;
  let mockUserService: jest.Mocked<UserService>;

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn(),
    } as any;

    mockSnackBar = {
      open: jest.fn(),
    } as any;

    mockSessionService = {
      sessionInformation: { id: 42, token: 'fake-token', type: 'user', username: 'test', firstName: 'test', admin: true, lastName: 'test' },
      logOut: jest.fn(),
    };

    mockUserService = {
      getById: jest.fn(),
      delete: jest.fn(),
    } as any;

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should fetch user on init', () => {
    const fakeUser: User = { id: 42, token: 'fake-token', type: 'user', username: 'test', firstName: 'test', admin: true, lastName: 'test' } as unknown as User;
    mockUserService.getById.mockReturnValue(of(fakeUser));

    fixture.detectChanges();

    expect(mockUserService.getById).toHaveBeenCalledWith('42');
    expect(component.user).toEqual(fakeUser);
  });

  it('should delete user and logout + redirect + show snackbar', () => {
    mockUserService.delete.mockReturnValue(of(undefined));

    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('42');
    expect(mockSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
