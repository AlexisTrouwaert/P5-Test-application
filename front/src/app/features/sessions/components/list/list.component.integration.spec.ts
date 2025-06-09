import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

describe('ListComponent (integration)', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionServiceMock: Partial<SessionService>;
  let sessionApiServiceMock: Partial<SessionApiService>;

  beforeEach(async () => {
    sessionServiceMock = {
      sessionInformation: { id: 1, token: 'fake-token', username: 'test', firstName: 'test', lastName: 'test', admin: true, type: 'user' } // selon interface
    };

    sessionApiServiceMock = {
  all: jest.fn(() => of([
    {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date('2025-06-10T12:00:00Z'),
      teacher_id: 101,
      users: []
    },
    {
      id: 2,
      name: 'Session 2',
      description: 'Description 2',
      date: new Date('2025-06-10T12:00:00Z'),
      teacher_id: 102,
      users: []
    }
  ]))
};

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have sessions$', (done) => {
    component.sessions$.subscribe(sessions => {
      expect(sessions.length).toBe(2);
      expect(sessions[0].name).toBe('Session 1');
      done();
    });
  });

  it('should get user from sessionService', () => {
    expect(component.user).toEqual(sessionServiceMock.sessionInformation);
  });
});
