import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandCreationDialogComponent } from './demand-creation-dialog.component';

describe('DemandCreationDialogComponent', () => {
  let component: DemandCreationDialogComponent;
  let fixture: ComponentFixture<DemandCreationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemandCreationDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandCreationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
