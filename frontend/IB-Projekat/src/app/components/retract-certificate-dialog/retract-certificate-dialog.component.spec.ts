import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PullCertificateDialogComponent } from './retract-certificate-dialog.component';

describe('PullCertificateDialogComponent', () => {
  let component: PullCertificateDialogComponent;
  let fixture: ComponentFixture<PullCertificateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PullCertificateDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PullCertificateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
