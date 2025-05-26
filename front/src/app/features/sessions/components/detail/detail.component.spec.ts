import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { of } from 'rxjs';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockActivatedRoute = {
    snapshot: { paramMap: { get: jest.fn(() => '123') } }
  };
  const mockSessionApiService = {
    detail: jest.fn(),
    delete: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn(),
  };
  const mockTeacherService = {
    detail: jest.fn()
  };
  const mockMatSnackBar = {
    open: jest.fn()
  };
  const mockRouter = {
    navigate: jest.fn()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 42
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create and initialize session and teacher', () => {
    const fakeSession = { users: [42], teacher_id: 5 } as any;
    const fakeTeacher = { id: 5, name: 'TeacherName' } as any;

    mockSessionApiService.detail.mockReturnValue(of(fakeSession));
    mockTeacherService.detail.mockReturnValue(of(fakeTeacher));

    component.ngOnInit();
    fixture.detectChanges();

    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
    expect(component.session).toEqual(fakeSession);
    expect(component.isParticipate).toBe(true);
    expect(mockTeacherService.detail).toHaveBeenCalledWith('5');
    expect(component.teacher).toEqual(fakeTeacher);
    expect(component.isAdmin).toBe(true);
  });

  it('should navigate back on back()', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});

    component.back();

    expect(spy).toHaveBeenCalled();

    spy.mockRestore();
  });

  it('should delete session and navigate', () => {
    mockSessionApiService.delete.mockReturnValue(of(null));

    component.delete();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith('123');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate and refetch session', () => {
    mockSessionApiService.participate.mockReturnValue(of(null));
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession').mockImplementation(() => {});

    component.participate();

    expect(mockSessionApiService.participate).toHaveBeenCalledWith('123', '42');
    expect(fetchSessionSpy).toHaveBeenCalled();
  });

  it('should unParticipate and refetch session', () => {
    mockSessionApiService.unParticipate.mockReturnValue(of(null));
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession').mockImplementation(() => {});

    component.unParticipate();

    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('123', '42');
    expect(fetchSessionSpy).toHaveBeenCalled();
  });
});

