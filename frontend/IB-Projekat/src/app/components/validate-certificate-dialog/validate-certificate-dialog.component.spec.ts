import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidateCertificateDialogComponent } from './validate-certificate-dialog.component';

describe('ValidateCertificateDialogComponent', () => {
  let component: ValidateCertificateDialogComponent;
  let fixture: ComponentFixture<ValidateCertificateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ValidateCertificateDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ValidateCertificateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
