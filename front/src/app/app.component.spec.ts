import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';


describe('AppComponent', () => {
  let mockSessionService: any;
  let mockRouter: any;

  beforeEach(async () => {

    mockSessionService = {
      $isLogged: jest.fn()
    };
    mockRouter = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should return observable from $isLogged()', () => {
    mockSessionService.$isLogged.mockReturnValue(of(true));
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    const result = app.$isLogged();

    expect(mockSessionService.$isLogged).toHaveBeenCalled();
    expect(result).toBeInstanceOf(Object);
  });
  
  it('should call logOut and navigate on logout()', () => {
    mockSessionService.logOut = jest.fn();

    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.logout();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
