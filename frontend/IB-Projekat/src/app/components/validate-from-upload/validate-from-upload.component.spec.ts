import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidateFromUploadComponent } from './validate-from-upload.component';

describe('ValidateFromUploadComponent', () => {
  let component: ValidateFromUploadComponent;
  let fixture: ComponentFixture<ValidateFromUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ValidateFromUploadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ValidateFromUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
