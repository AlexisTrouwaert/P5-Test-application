import { TestBed } from '@angular/core/testing';
import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';
import { expect } from '@jest/globals';

describe('SessionApiService Integration', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const fakeSession: Session = {
    id: 1,
    name : 'test',
    description: 'test',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2],
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });

    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu’aucune requête HTTP inattendue n’a été faite
  });

  it('should retrieve all sessions (GET)', () => {
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual([fakeSession]);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush([fakeSession]);
  });

  it('should retrieve session detail (GET)', () => {
    service.detail('1').subscribe((session) => {
      expect(session).toEqual(fakeSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(fakeSession);
  });

  it('should create a session (POST)', () => {
    service.create(fakeSession).subscribe((session) => {
      expect(session).toEqual(fakeSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(fakeSession);
    req.flush(fakeSession);
  });

  it('should delete a session (DELETE)', () => {
    service.delete('1').subscribe((res) => {
      expect(res).toBeNull(); // si le backend renvoie 204 No Content
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  // Tu peux faire pareil pour update, participate, unParticipate…
});
