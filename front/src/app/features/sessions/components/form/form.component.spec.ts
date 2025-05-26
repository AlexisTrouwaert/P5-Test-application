import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockRouter = {
    url: '/sessions/update/123',
    navigate: jest.fn(),
  };

  const mockActivatedRoute = {
    snapshot: { paramMap: { get: jest.fn(() => '123') } }
  };

  const mockSessionApiService = {
    detail: jest.fn(),
    create: jest.fn(),
    update: jest.fn()
  };

  const mockTeacherService = {
    all: jest.fn(() => of([{ id: 1, name: 'Teacher 1' }]))
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect non-admin users', () => {
    mockSessionService.sessionInformation.admin = false;

    mockSessionApiService.detail.mockReturnValue(of({
    name: 'Fake session',
    date: new Date().toISOString(),
    teacher_id: 1,
    description: 'Fake description'
  }));
  
    component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    // Reset admin to true for other tests
    mockSessionService.sessionInformation.admin = true;
  });

  it('should initialize form in update mode', fakeAsync(() => {
    const fakeSession = {
      name: 'Test Session',
      date: new Date('2024-05-25').toISOString(),
      teacher_id: 1,
      description: 'Description'
    };
    mockRouter.url = '/sessions/update/123';
    mockSessionApiService.detail.mockReturnValue(of(fakeSession));

    component.ngOnInit();
    tick();

    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.value.name).toBe(fakeSession.name);
    expect(component.sessionForm?.value.teacher_id).toBe(fakeSession.teacher_id);
  }));

  it('should initialize form in create mode', () => {
    mockRouter.url = '/sessions/create';

    component.ngOnInit();

    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.value.name).toBe('');
  });

  it('should call create on submit when onUpdate is false', () => {
    mockRouter.url = '/sessions/create';
    component.ngOnInit();
    component.sessionForm?.setValue({
      name: 'New Session',
      date: '2024-05-25',
      teacher_id: 1,
      description: 'Desc'
    });
    mockSessionApiService.create.mockReturnValue(of({}));
    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith(component.sessionForm?.value);
  });

  it('should call update on submit when onUpdate is true', fakeAsync(() => {
    mockRouter.url = '/sessions/update/123';
    const fakeSession = {
      name: 'Test Session',
      date: new Date('2024-05-25').toISOString(),
      teacher_id: 1,
      description: 'Description'
    };
    mockSessionApiService.detail.mockReturnValue(of(fakeSession));
    component.ngOnInit();
    tick();

    component.sessionForm?.setValue({
      name: 'Updated Session',
      date: '2024-05-26',
      teacher_id: 2,
      description: 'New description'
    });
    mockSessionApiService.update.mockReturnValue(of({}));

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('123', component.sessionForm?.value);
  }));

  it('should show snackbar and navigate on exitPage', () => {
    component['exitPage']('Test message');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Test message', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
