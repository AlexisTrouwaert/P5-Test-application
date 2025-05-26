import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  const mockTeachers: Teacher[] = [
    { id: 1, lastName: 'test', firstName:'test', createdAt: new Date(), updatedAt: new Date()},
    { id: 2, lastName: 'test2', firstName:'test2', createdAt: new Date(), updatedAt: new Date()}
  ];

  const mockTeacher: Teacher = { id: 3, lastName: 'test3', firstName:'test3', createdAt: new Date(), updatedAt: new Date()};

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all teachers', () => {
    service.all().subscribe((teachers) => {
      expect(teachers.length).toBe(2);
      expect(teachers).toEqual(mockTeachers);
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should fetch a teacher by id', () => {
    const teacherId = '1';

    service.detail(teacherId).subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne(`api/teacher/${teacherId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});
