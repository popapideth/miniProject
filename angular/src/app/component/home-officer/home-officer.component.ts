import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { HomeService } from '../../services/home.service';
import { OfficerService } from '../../services/officer.service';
import { Home } from '../../model/home';
import { Officer } from '../../model/officer';
import { of } from 'rxjs';
import {
  FormGroup,
  FormBuilder,
  ReactiveFormsModule,
  Validator,
  Validators,
} from '@angular/forms';
declare var bootstrap: any; // สำหรับใช้งาน Bootstrap JS
@Component({
  selector: 'app-home-officer',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './home-officer.component.html',
  styleUrl: './home-officer.component.css',
})
export class HomeOfficerComponent implements OnInit {
  datahomes: Home[] = []; //เก็บข้อมูลที่มีอยู่ในhomeอยู่แล้ว
  dataofficers: Officer[] = []; //เก็บข้อมูลที่มีอยู่ในofficerอยู่แล้ว

  allOfficer: Officer[] = []; //เก็บข้อมูลทั้งหมด
  allHome: Home[] = []; //เก็บข้อมูลทั้งหมด

  canChooseDataHome: Home[] = []; //ข้อมูลofficerที่สามารถเลือกได้ ในการเพิ่มofficer ลงhome
  canChooseDataOfficer: Officer[] = []; //ข้อมูลhomeที่สามารถเลือกได้ ในการเพิ่มhome ลงofficer
  chooseOfficerId: number[] = []; //เก็บofficerIdที่เลือกมาจากlist
  chooseHomeId: number[] = []; //เก็บhomeIdที่เลือกมาจากlist

  homeForm!: FormGroup;
  mode!: 'edithome' | 'viewhome' | 'viewofficer'; // ประกาศว่า mode จะเป็น 'edit' หรือ 'view' เท่านั้น
  // สัญลักษณ์ ! หลังชื่อตัวแปร (mode!) ใช้เพื่อบอกว่า ตัวแปรนี้จะถูกกำหนดค่าในภายหลัง และไม่ใช่ null หรือ undefined ในขณะที่ใช้งาน
  Id: any; // ที่จะเก็บ ID ของบ้าน/officer

  constructor(
    private router: Router,
    // ใช้ Router เพื่อเปลี่ยนเส้นทางไปยัง URL อื่น
    private route: ActivatedRoute,
    // ใช้ ActivatedRoute เพื่อดึงข้อมูลจาก URL
    private homeservice: HomeService,
    private officerservice: OfficerService,
    private fb: FormBuilder // ใช้ FormBuilder เพื่อสร้างฟอร์มแบบ Reactive Forms
  ) {}

  ngOnInit(): void {
    // this.router.routerState.root.queryParams ใช้เพื่อดึง query parameters จาก URL (Type sting หมด)
    this.route.queryParams.subscribe((params) => {
      this.Id = parseInt(params['Id']); // แปลงค่า Id จาก string เป็น number ด้วยparseInt
      this.mode = params['mode'] === 'viewhome' ? 'viewhome' : 'viewofficer';
    });

    if (this.mode === 'viewofficer') {
      this.getAllOfficer(); //ดึงข้อมูลofficerทั้งหมด
      this.getOfficerByhomeId(this.Id); // ดึงข้อมูล officer เมื่ออยู่ในโหมดviewofficer
    } else if (this.mode === 'viewhome') {
      this.getAllHome();
      this.getHomeByOfficerId(this.Id); // ดึงข้อมูล home เมื่ออยู่ในโหมดviewhome
    }
  }

  private getAllOfficer() {
    this.officerservice.getAllOfficers().subscribe((data) => {
      this.allOfficer = data;
    });
  }
  private getAllHome() {
    this.homeservice.getAllHomes().subscribe((data) => {
      this.allHome = data;
    });
  }
  // --------------------------------------------------------------------------------
  //Mode 'viewofficer' ผ่านhome List
  private getOfficerByhomeId(Id: any): void {
    this.homeservice.getHomeById(Id).subscribe((data) => {
      console.log('ข้อมูลบ้านที่ดึงมา:', data);
      // data ที่มามีofficers ซึ่งเป็น array ของofficer ที่เกี่ยวข้องกับ home นั้นมาด้วย จึงเรียกเอาเฉพาะ officers มาเก็บไว้ในตัวแปร dataofficers
      this.dataofficers = data.officers;
      // ดึงข้อมูล officer ที่เกี่ยวข้องกับ home โดยใช้ homeId Many-to-Many (บ้านมีหลาย officer)
      console.log('ข้อมูล officer ที่เกี่ยวข้องกับ home ', this.dataofficers);
      // เก็บข้อมูลOfficerที่มีอยู่แล้ว ในบ้านนี้
    });
  }
  pageEditOfficer(officerId: number, mode: 'editofficer'): void {
    this.router.navigate(['edit'], {
      queryParams: { Id: officerId, mode: mode },
    });
  }
  deleteOfficer(officerId: number): void {
    if (confirm('คุณแน่ใจว่าต้องการลบข้อมูลOfficerนี้หรือไม่?')) {
      this.officerservice.deleteOfficer(officerId).subscribe(
        () => {
          console.log(`Officer ${officerId} deleted successfully`);
          alert('ลบข้อมูลOfficerเรียบร้อยแล้ว');
          this.getOfficerByhomeId(this.Id); // Refresh the list after deletion
        },
        (error) => {
          console.error('Error deleting officer:', error);
        }
      );
    }
  }
  chooseDataOfficer() {
    const haveId = this.dataofficers.map((o) => o.officerId); //ดึงเฉพาะofficerIdที่มีอยู่แล้วในบ้านนี้
    // map() คือ method ของ Array ที่จะวนลูปผ่านทุก element
    // ในแต่ละรอบมันจะรับ object (ในที่นี้คือ o) แล้วส่งค่ากลับตามที่เรากำหนด (คือ o.officerId)
    this.canChooseDataOfficer = this.allOfficer.filter(
      (o) => !haveId.includes(o.officerId) //เลือกเฉพาะofficerIdที่ยังไม่มีในhomeนี้
    );
    console.log('Officerที่สามารเลือกได้', this.canChooseDataOfficer);
  }
  confirmSelectOfficer() {
    const selectDataId = this.chooseOfficerId; //เก็บofficerIdที่เลือกมาจากlist

    this.homeservice.selectOfficerToHome(this.Id, selectDataId).subscribe({
      next: (updateHome) => {
        //กรณีสำเร็จ
        alert('เพิ่มOfficerสำเร็จ');
        this.getOfficerByhomeId(this.Id); //reload
        console.log('ข้อมูลที่อัพเดทใหม่', updateHome);
        // ✅ ปิด modal อย่างถูกต้อง
        const modalElement = document.getElementById('addOfficerToHome');
        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        modalInstance?.hide(); // ปิด modal
      },
      error: (err) => {
        //กรณีerror
        console.error('เพิ่มOfficerไม่สำเร็จ', err);
        alert('errorจ้า');
      },
    });
  }
  eventChooseOfficer(event: any) {
    const officerId = +event.target.value; //+ เพื่อแปลงจาก string เป็น number
    // ดึงค่า value ของ checkbox ซึ่งมักจะเป็น id ของ officer จาก event.target
    if (event.target.checked) {
      //ถ้า checkbox ถูกติ๊ก (checked = true) → เพิ่ม id ไปใน array
      this.chooseOfficerId.push(officerId); //เพิ่ม officerId ลงใน array
    } //ถ้าไม่ถูกติ๊ก → ลบออกจาก array
    else {
      this.chooseOfficerId = this.chooseOfficerId.filter(
        (id) => id !== officerId
      );
      // ใช้ .filter() เพื่อสร้าง array ใหม่ ที่ไม่มี officerId นั้น
    }
    console.log('Id ที่เลือก:', this.chooseOfficerId);
  }
  // --------------------------------------------------------------------------------
  // Mode 'viewhome' ผ่านofficer List
  private getHomeByOfficerId(Id: any) {
    this.officerservice.getOfficerById(Id).subscribe((data) => {
      console.log('ข้อมูลOfficerที่ดึงมา:', data);
      // data ที่มามี home ซึ่งเป็นarray ของhome ที่เกี่ยวข้องกับ officer นั้นมาด้วย จึงเรียกเอาเฉพาะ home มาเก็บไว้ใน datahomes
      this.datahomes = data.homes;
      console.log('ข้อมูล home ที่เกี่ยวข้องกับ officer ', this.datahomes);
      // เก็บข้อมูลบ้านที่มีอยู่แล้ว ในofficerนี้
    });
  }
  pageEditHome(homeId: number, mode: 'edithome') {
    console.log('test');
    this.router.navigate(['edit'], {
      queryParams: { Id: homeId, mode: mode },
    });
  }
  deleteHome(homeId: number) {
    if (confirm('คุณแน่ใจว่าต้องการลบข้อมูลOfficerนี้หรือไม่?')) {
      this.homeservice.deleteHome(homeId).subscribe(
        () => {
          console.log(`Home ${homeId}deleted successfully `);
          alert('ลบข้อมูลHomeเรียบร้อยแล้ว');
          this.getHomeByOfficerId(this.Id);
        },
        (error) => {
          console.error('Error deleting home:', error);
        }
      );
    }
  }
  chooseDataHome() {
    const haveId = this.datahomes.map((o) => o.homeId); //ดึงเฉพาะhomeIdที่มีอยู่แล้วในofficerนี้
    this.canChooseDataHome = this.allHome.filter(
      (o) => !haveId.includes(o.homeId) //เลือกเฉพาะhomeIdที่ยังไม่มีในofficerนี้
    );
    console.log('Homeที่สามารเลือกได้', this.canChooseDataHome);
  }
  confirmSelectHome() {
    const selectDataId = this.chooseHomeId; //เก็บhomeIdที่เลือกมาจากlist

    this.officerservice.selectHomeToOfficer(this.Id, selectDataId).subscribe({
      next: (updateOfficer) => {
        alert('เพิ่มHomeสำเร็จ');
        this.getHomeByOfficerId(this.Id);
        console.log('ข้อมูลที่อัพเดทใหม่', updateOfficer);
        // ✅ ปิด modal อย่างถูกต้อง
        const modalElement = document.getElementById('addHomeToOfficer');
        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        modalInstance?.hide(); // ปิด modal
      },
      error: (err) => {
        console.error('เพิ่มHomeสำเร็จ', err);
        alert('error จ้า');
      },
    });
  }
  eventChooseHome(event: any) {
    const homeId = +event.target.value;
    if (event.target.checked) {
      this.chooseHomeId.push(homeId);
    } else {
      this.chooseHomeId = this.chooseHomeId.filter((id) => id !== homeId);
    }
    console.log('Id ที่เลือก:', this.chooseHomeId);
  }
}
