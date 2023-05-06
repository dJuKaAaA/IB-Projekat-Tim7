import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateDemandHistoryComponent } from './certificate-demand-history.component';

describe('CertificateDemandHistoryComponent', () => {
  let component: CertificateDemandHistoryComponent;
  let fixture: ComponentFixture<CertificateDemandHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CertificateDemandHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateDemandHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
