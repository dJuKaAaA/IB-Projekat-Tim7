import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OAuthLoginComponent } from './oauth-login.component';

describe('OAuthLoginComponent', () => {
  let component: OAuthLoginComponent;
  let fixture: ComponentFixture<OAuthLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OAuthLoginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OAuthLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
