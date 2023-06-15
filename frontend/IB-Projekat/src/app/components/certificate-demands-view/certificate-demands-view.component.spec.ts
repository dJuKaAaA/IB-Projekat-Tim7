import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateDemandsViewComponent } from './certificate-demands-view.component';

describe('CertificateDemandsViewComponent', () => {
  let component: CertificateDemandsViewComponent;
  let fixture: ComponentFixture<CertificateDemandsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CertificateDemandsViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateDemandsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
