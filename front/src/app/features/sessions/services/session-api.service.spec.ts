import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu’aucune requête n’est restée en attente
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', date: new Date(), teacher_id: 1, description: 'Desc 1', users: [1, 2] },
      { id: 2, name: 'Session 2', date: new Date(), teacher_id: 2, description: 'Desc 2', users: [1, 2] },
    ];

    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should fetch session detail by id', () => {
    const mockSession: Session = { id: 1, name: 'Session 1', date: new Date(), teacher_id: 1, description: 'Desc 1', users: [1, 2] };

    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session by id', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull(); // ou ce que le backend renvoie
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should create a session', () => {
    const newSession: Session = { id: 1, name: 'Session 1', date: new Date(), teacher_id: 1, description: 'Desc 1', users: [1, 2] };

    service.create(newSession).subscribe(session => {
      expect(session).toEqual(newSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(newSession);
  });

  it('should update a session', () => {
    const updatedSession: Session = { id: 1, name: 'Session 1', date: new Date(), teacher_id: 1, description: 'Desc 1', users: [1, 2] };

    service.update('1', updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should participate user to session', () => {
    service.participate('1', 'user123').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/user123');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should unParticipate user from session', () => {
    service.unParticipate('1', 'user123').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/user123');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
