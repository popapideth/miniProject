import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditofficerComponent } from './edit.component';

describe('EditofficerComponent', () => {
  let component: EditofficerComponent;
  let fixture: ComponentFixture<EditofficerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditofficerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EditofficerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
