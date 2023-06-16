import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RetractCertificateDialogComponent } from './retract-certificate-dialog.component';

describe('PullCertificateDialogComponent', () => {
  let component: RetractCertificateDialogComponent;
  let fixture: ComponentFixture<RetractCertificateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RetractCertificateDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RetractCertificateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
