import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateModelComponent } from './certificate-model.component';

describe('CertificateModelComponent', () => {
  let component: CertificateModelComponent;
  let fixture: ComponentFixture<CertificateModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CertificateModelComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
