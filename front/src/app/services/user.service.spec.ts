import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch a user by id', () => {
    const mockUser: User = {
      id: 1,
      email: 'test@test.fr',
      firstName: 'Test',
      lastName: 'User',
      password: 'test123456',
      createdAt: new Date(),
      updatedAt: new Date(),
      admin: false
    };

    service.getById('123').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne('api/user/123');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should delete a user by id', () => {
    service.delete('123').subscribe(response => {
      expect(response).toBeTruthy(); // Ou selon ce que ton API retourne
    });

    const req = httpMock.expectOne('api/user/123');
    expect(req.request.method).toBe('DELETE');
    req.flush({ success: true });
  });
});
