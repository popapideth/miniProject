import { Component, OnInit } from '@angular/core';
import {
  Form,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Officer } from '../../model/officer';
import { OfficerService } from '../../services/officer.service';
import { Home } from '../../model/home';
import { HomeService } from '../../services/home.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit.component.html',
  styleUrl: './edit.component.css',
})
export class EditComponent implements OnInit {
  dataofficers: Officer[] = [];
  currentOfficer: Officer[] = []; // ประกาศตัวแปร currentOfficer เป็น array ของ Officer เพื่อเก็บข้อมูลเจ้าหน้าที่ที่เกี่ยวข้องกับบ้าน
  currentHome: Home[] = [];
  Id: any;
  Form!: FormGroup;
  mode!: 'edithome' | 'editofficer';

  constructor(
    private fb: FormBuilder, // ใช้ FormBuilder เพื่อสร้างฟอร์มแบบ Reactive Forms
    private router: Router,
    private route: ActivatedRoute,
    private officerservice: OfficerService,
    private homeservice: HomeService
  ) {
    // เรียกใช้การสร้างForm
    this.initForm();
  }

  ngOnInit(): void {
    // ดึงข้อมูลจาก query parameters ของ URL
    this.route.queryParams.subscribe((params) => {
      this.Id = parseInt(params['Id']);
      this.mode = params['mode'] === 'edithome' ? 'edithome' : 'editofficer';
      this.initForm();
    });

    if (this.mode === 'editofficer') {
      this.getOfficerById(this.Id);
      // เรียกใช้ฟังก์ชันเพื่อดึงข้อมูล officer ตาม Id ที่ได้จาก query parameters
    } else if (this.mode === 'edithome') {
      this.getHomeById(this.Id);
    }
  }

  private initForm() {
    if (this.mode === 'editofficer') {
      // สร้าง FormGroup สำหรับ officer
      this.Form = this.fb.group({
        officerId: [{ value: '', disabled: true }],
        officerName: ['', Validators.required],
        officerAge: [
          '',
          [Validators.required, Validators.min(0), Validators.max(60)],
        ],
      });
    } else if (this.mode === 'edithome') {
      // 1. สร้าง FormGroup สำหรับ home
      this.Form = this.fb.group({
        homeId: [{ value: '', disabled: true }],
        homeName: ['', Validators.required],
      });
    }
  }

  // ฟังก์ชันสำหรับดึงข้อมูล officer ตาม Id ที่ได้จาก query parameters
  private getOfficerById(Id: any): void {
    this.officerservice.getOfficerById(Id).subscribe((data) => {
      console.log('ข้อมูล officer ที่ดึงมา:', data);
      // กำหนดค่าให้กับ officerForm ด้วยข้อมูลที่ได้จาก API
      // ใส่ข้อมูลลงในฟอร์ม
      this.Form.patchValue({
        officerId: data.officerId,
        officerName: data.officerName,
        officerAge: data.officerAge,
      });
      this.currentHome = data.homes;
    });
  }
  // ฟังก์ชันสำหรับดึงข้อมูล Home ตาม Id ที่ได้จาก query parameters
  private getHomeById(Id: any): void {
    this.homeservice.getHomeById(Id).subscribe((data) => {
      console.log('ข้อมูล home ที่ดึงมา:', data);
      // กำหนดค่าให้กับ homeForm ด้วยข้อมูลที่ได้จาก API
      // ใส่ข้อมูลลงในฟอร์ม
      this.Form.patchValue({
        homeId: data.homeId,
        homeName: data.homeName,
      });
      // ดึงข้อมูล officer ที่เกี่ยวข้องกับ home โดยใช้ homeId Many-to-Many (บ้านมีหลาย officer)
      this.currentOfficer = data.officers; // เก็บข้อมูลเจ้าหน้าที่ที่เกี่ยวข้องกับบ้าน
      //  โดย dataที่ได้มานั้นจะมี officers เป็น array ของ officer ที่เกี่ยวข้องกับ home ด้วย
    });
  }

  // ฟังก์ชันสำหรับบันทึกข้อมูล officer
  onSubmit() {
    if (this.Form.invalid) {
      // ถ้าฟอร์มไม่ถูกต้อง ให้แสดงข้อความเตือน
      console.log('Form is invalid');

      // loop ดูว่า field ไหน error บ้าง
      Object.keys(this.Form.controls).forEach((field) => {
        const control = this.Form.get(field);
        control?.markAllAsTouched(); // ทำให้ทุกฟิลด์ที่มี error แสดงข้อความเตือน
        console.log(`${field} is invalid:`, control?.errors);
      });
      alert('กรุณากรอกข้อมูลให้ครบถ้วน');
      return;
    }
    if (this.mode === 'editofficer') {
      // ถ้าฟอร์มถูกต้อง ให้เรียกใช้ฟังก์ชัน updateOfficer
      // สร้าง object officer ที่จะอัพเดต
      const editdata: Officer = {
        officerId: this.Id, // ใช้ ID เดิม
        officerName: this.Form.get('officerName')?.value,
        officerAge: this.Form.get('officerAge')?.value,
        homes: this.currentHome,
      };
      // เรียกใช้ฟังก์ชัน updateOfficer เพื่ออัพเดตข้อมูล
      this.editOfficer(editdata);
    } else if (this.mode === 'edithome') {
      const editdata: Home = {
        homeId: this.Id, // ใช้ ID เดิม
        homeName: this.Form.get('homeName')?.value,
        officers: this.currentOfficer, // ✅ ส่ง officer กลับไปด้วย เพื่อไม่ให้ข้อมูลหาย
      };
      this.editHome(editdata); // เรียกใช้ฟังก์ชัน updateHome เพื่อบันทึกข้อมูล
    }
  }

  // ฟังก์ชันสำหรับอัพเดตข้อมูล officer
  private editOfficer(data: Officer) {
    console.log('ข้อมูลที่ส่งไปอัพเดต:', data);
    this.officerservice.updateOfficer(data).subscribe(() => {
      alert('อัพเดตข้อมูลเรียบร้อยแล้ว');
      // หลังจากอัพเดตข้อมูลแล้ว ให้เปลี่ยนเส้นทางไปยัง officer-list
      this.router.navigate(['officer']);
    });
  }
  // ฟังก์ชันสำหรับอัพเดตข้อมูล Home
  private editHome(data: Home) {
    console.log('ข้อมูลที่ส่งไปอัพเดท', data);
    this.homeservice.updateHome(data).subscribe(() => {
      alert('อัพเดตข้อมูลบ้านเรียบร้อยแล้ว');
      // หลังจากอัพเดตข้อมูลแล้ว ให้เปลี่ยนเส้นทางไปยัง home-list
      this.router.navigate(['homes']);
    });
  }
}
