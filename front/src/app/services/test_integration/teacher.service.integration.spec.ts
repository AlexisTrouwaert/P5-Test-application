import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TeacherService } from '../teacher.service';
import { Teacher } from '../../interfaces/teacher.interface';
import { expect } from '@jest/globals';

describe('TeacherService (Integration)', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });

    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'aucune requête n’est en attente
  });

  it('should fetch all teachers', () => {
    const mockTeachers: Teacher[] = [
        { id: 1, lastName: 'test', firstName:'test', createdAt: new Date(), updatedAt: new Date()},
        { id: 2, lastName: 'test2', firstName:'test2', createdAt: new Date(), updatedAt: new Date()}
    ];

    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
      expect(teachers.length).toBe(2);
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should fetch teacher by id', () => {
    const mockTeacher: Teacher = { id: 3, lastName: 'test3', firstName:'test3', createdAt: new Date(), updatedAt: new Date()};

    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});
