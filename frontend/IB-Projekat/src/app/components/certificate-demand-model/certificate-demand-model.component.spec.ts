import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateDemandModelComponent } from './certificate-demand-model.component';

describe('CertificateDemandModelComponent', () => {
  let component: CertificateDemandModelComponent;
  let fixture: ComponentFixture<CertificateDemandModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CertificateDemandModelComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateDemandModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
