import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginVerificationCodeComponent } from './login-verification-code.component';

describe('LoginVerificationCodeComponent', () => {
  let component: LoginVerificationCodeComponent;
  let fixture: ComponentFixture<LoginVerificationCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginVerificationCodeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginVerificationCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
