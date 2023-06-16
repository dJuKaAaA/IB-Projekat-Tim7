import { TestBed } from '@angular/core/testing';

import { SessionExpiredCheckInterceptor } from './session-expired-check.interceptor';

describe('SessionExpiredCheckInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      SessionExpiredCheckInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: SessionExpiredCheckInterceptor = TestBed.inject(SessionExpiredCheckInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
