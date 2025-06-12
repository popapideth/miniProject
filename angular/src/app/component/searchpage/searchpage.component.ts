import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HomeService } from '../../services/home.service';
import { OfficerService } from '../../services/officer.service';
import { Home } from '../../model/home';
import { Officer } from '../../model/officer';
import { delay } from 'rxjs';

declare var bootstrap: any; // สำหรับใช้งาน Bootstrap JS

@Component({
  selector: 'app-searchpage',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './searchpage.component.html',
  styleUrl: './searchpage.component.css',
})
export class SearchpageComponent implements OnInit {
  formSearch!: FormGroup;
  dataHomes: Home[] = [];
  dataOfficer: Officer[] = [];

  // Toast
  toastMessage: string = '';
  toastType: 'success' | 'error' = 'success';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder, // ใช้ FormBuilder เพื่อสร้างฟอร์มแบบ Reactive Forms
    private homeservice: HomeService,
    private officerservice: OfficerService
  ) {}
  ngOnInit(): void {
    // สร้างฟอร์มสำหรับการค้นหา
    this.formSearch = this.fb.group({
      searchType: ['', Validators.required], // ประเภทการค้นหา (hoeme หรือ officer)
      searchId: ['', Validators.required], // ID ที่จะค้นหา
      searchName: [''], // ชื่อที่ใช้ในการค้นหา
    });
  }

  onSubmit(): void {
    if (this.formSearch.invalid) {
      // ถ้าฟอร์มไม่ถูกต้อง ให้แสดงข้อความเตือน
      console.log('Form is invalid');

      //loop ดูว่าฟิลด์ไหนที่ error
      // loop ดูว่า field ไหน error บ้าง
      Object.keys(this.formSearch.controls).forEach((field) => {
        const control = this.formSearch.get(field);
        control?.markAllAsTouched(); // ทำให้ทุกฟิลด์ที่มี error แสดงข้อความเตือน
        console.log(`${field} is invalid:`, control?.errors);
      });
      alert('กรุณากรอกข้อมูลให้ครบถ้วน');
      return;
    }
    // ถ้าฟอร์มถูกต้อง ให้ดำเนินการค้นหา
    const searchData = this.formSearch.value;
    console.log('ข้อมูลที่ค้นหา:', searchData);
    this.searchandValidateData(searchData);
  }

  async searchandValidateData(searchData: any) {
    const datafound = await this.checkData(
      searchData.searchType,
      searchData.searchId
    );

    if (datafound) {
      // นำทางไปหน้าที่ต้องการ
      if (searchData.searchType === 'home') {
        // ถ้าเลือกประเภทการค้นหาเป็นบ้าน
        this.router.navigate(['homes'], {
          queryParams: {
            searchId: searchData.searchId,
          },
        });
      } else if (searchData.searchType === 'officer') {
        // ถ้าเลือกประเภทการค้นหาเป็น Officer
        this.router.navigate(['officer'], {
          queryParams: {
            searchId: searchData.searchId,
          },
        });
      } else {
        // ไม่พบข้อมูล → แสดง Toast
        this.showToast('ไม่พบข้อมูลที่ค้นหา', 'error');
      }
    }
  }
  // เช็คว่า เจอข้อมูล ที่ต้องการไหม ก่อนที่จะไปยังหน้าที่เก็บข้อมูล ถ้าเจอค่อยไปทำตรงส่วนเปลี่ยนหน้า
  private checkData(type: string, Id: number): Promise<boolean> {
    return new Promise((resolve) => {
      //สิ่งที่รอผลลัพธ์แบบ async ใช้กับasync/await
      if (type === 'home') {
        this.homeservice.getHomeById(Id).subscribe({
          next: (data) => {
            // next คือ callback ที่ทำงานเมื่อได้ข้อมูลสำเร็จจาก API (เช่น HTTP 200)
            this.dataHomes = [data];
            resolve(true);
            // บอกว่า "พบข้อมูลแล้ว" ให้ Promise สำเร็จแบบ true
            // พบข้อมูล : next(data) ทำงาน → บันทึกข้อมูล → resolve(true)
          },
          error: (err) => {
            // error คือ callback ที่ทำงานเมื่อเกิดข้อผิดพลาด HTTP 404, 500, หรืออื่น ๆ
            console.log('ไม่พบhome', err);
            resolve(false);
            this.showToast('ไม่พบข้อมูลที่ค้นหา', 'error');
            //  resolve(false) เพื่อแจ้งว่า "ไม่พบข้อมูล"
            // ไม่พบข้อมูล : error(err) ทำงาน → แสดง error → resolve(false)
          },
        });
      } else if (type === 'officer') {
        this.officerservice.getOfficerById(Id).subscribe({
          next: (data) => {
            this.dataOfficer = [data];
            resolve(true);
          },
          error: (err) => {
            console.log('ไม่พบofficer', err);
            resolve(false);
            this.showToast('ไม่พบข้อมูลที่ค้นหา', 'error');
          },
        });
      } else {
        resolve(false); // กรณี type ไม่ถูกต้อง
      }
    });
  }

  showToast(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;

    // แสดง Toast โดยใช้ Bootstrap
    const toastelement = document.getElementById('searchToast');
    if (toastelement) {
      const toast = new bootstrap.Toast(toastelement, {
        autohide: true,
        delay: 4000, // 4 วิ
      });
      toast.show();
    }
  }

  // onSearchTypeChange(value: string): void {
  //   // เมื่อเปลี่ยนประเภทการค้นหา จะรีเซ็ตค่าฟอร์ม
  //   this.formSearch.reset();
  //   if (value === 'home') {
  //     // ถ้าเลือกประเภทการค้นหาเป็นบ้าน
  //     console.log('ค้นหาHome');
  //   } else if (value === 'officer') {
  //     // ถ้าเลือกประเภทการค้นหาเป็นOfficer
  //     console.log('ค้นหาOfficer');
  //   }
  // }
}
