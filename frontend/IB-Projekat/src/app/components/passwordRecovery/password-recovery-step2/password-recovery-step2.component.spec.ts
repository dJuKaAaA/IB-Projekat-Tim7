import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordRecoveryStep2Component } from './password-recovery-step2.component';

describe('PasswordRecoveryStep2Component', () => {
  let component: PasswordRecoveryStep2Component;
  let fixture: ComponentFixture<PasswordRecoveryStep2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordRecoveryStep2Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordRecoveryStep2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
