import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { OfficerService } from '../../services/officer.service';
import { Officer } from '../../model/officer';
import { HomeService } from '../../services/home.service';
import { Home } from '../../model/home';

@Component({
  selector: 'app-adddata',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './adddata.component.html',
  styleUrl: './adddata.component.css',
})
export class AdddataComponent implements OnInit {
  formAddData!: FormGroup;
  type!: 'home' | 'officer';

  constructor(
    private fb: FormBuilder, // ใช้ FormBuilder เพื่อสร้างฟอร์มแบบ Reactive Forms
    private router: Router,
    private route: ActivatedRoute,
    private officerservice: OfficerService,
    private homeservice: HomeService
  ) {
    this.initForm();
    // ใช้แบบนี้เมื่อมีการกำหนดรูปแบบForm หลายแบบ
  }
  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.type = params['type'] === 'home' ? 'home' : 'officer';
      this.initForm();
    });
  }

  private initForm() {
    if (this.type === 'home') {
      this.formAddData = this.fb.group({
        homeName: ['', Validators.required],
      });
    } else if (this.type === 'officer') {
      this.formAddData = this.fb.group({
        officerName: ['', Validators.required],
        officerAge: ['', Validators.required],
      });
    }
  }
  onSubmit() {
    if (this.formAddData.invalid) {
      console.log('form is invalid');
      Object.keys(this.formAddData.controls).forEach((field) => {
        const controls = this.formAddData.get(field);
        controls?.markAllAsTouched();
        console.log(`${field} is invalid `, controls?.errors);
      });
      return;
    }

    if (this.type === 'officer') {
      const addData: Officer = {
        officerId: 0,
        officerName: this.formAddData.get('officerName')?.value,
        officerAge: this.formAddData.get('officerAge')?.value,
        // แต่คุณต้องการค่าเป็น string หรือ number  ต้องมี ?.value\
        homes: [],
      };
      this.AddNewOfficer(addData);
    } else if (this.type === 'home') {
      const addData: Home = {
        homeId: 0,
        homeName: this.formAddData.get('homeName')?.value,
        // แต่คุณต้องการค่าเป็น string หรือ number  ต้องมี ?.value
        officers: [], // ตั้งค่าเริ่มต้น เป็นไม่มีความเกี่ยวข้องไรเลย ค่อยโยงทีหลัง
      };
      this.AddNewHome(addData);
    }
  }

  private AddNewOfficer(newData: Officer) {
    console.log('ข้อมูลOfficerที่ต้องการเพิ่ม', newData);
    this.officerservice.addNewOfficer(newData).subscribe(() => {
      alert('เพิ่มข้อมูลOfficerใหม่เรียบร้อยแล้ว');
      this.router.navigate(['officer']);
    });
  }

  private AddNewHome(newData: Home) {
    console.log('ข้อมูลHomeที่ต้องการเพิ่ม', newData);
    this.homeservice.addNewHome(newData).subscribe(() => {
      alert('เพิ่มข้อมูลHomeใหม่เรียบร้อยแล้ว');
      this.router.navigate(['homes']);
    });
  }
}
