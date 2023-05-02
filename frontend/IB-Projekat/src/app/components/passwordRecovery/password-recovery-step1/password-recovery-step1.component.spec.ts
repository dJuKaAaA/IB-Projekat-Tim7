import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordRecoveryStep1Component } from './password-recovery-step1.component';

describe('PasswordRecoveryStep1Component', () => {
  let component: PasswordRecoveryStep1Component;
  let fixture: ComponentFixture<PasswordRecoveryStep1Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordRecoveryStep1Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordRecoveryStep1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
