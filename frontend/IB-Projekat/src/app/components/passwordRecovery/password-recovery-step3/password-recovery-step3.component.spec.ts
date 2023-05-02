import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordRecoveryStep3Component } from './password-recovery-step3.component';

describe('PasswordRecoveryStep3Component', () => {
  let component: PasswordRecoveryStep3Component;
  let fixture: ComponentFixture<PasswordRecoveryStep3Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordRecoveryStep3Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordRecoveryStep3Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
