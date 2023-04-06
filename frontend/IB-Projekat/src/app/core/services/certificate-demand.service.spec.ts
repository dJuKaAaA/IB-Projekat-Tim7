import { TestBed } from '@angular/core/testing';

import { CertificateDemandService } from './certificate-demand.service';

describe('CertificateDemandService', () => {
  let service: CertificateDemandService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CertificateDemandService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
