import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HomeService } from '../../services/home.service';
import { Home } from '../../model/home';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home-list',
  imports: [CommonModule],
  templateUrl: './home-list.component.html',
  styleUrl: './home-list.component.css',
})
export class HomeListComponent implements OnInit {
  datahomes: Home[] = [];
  Id: any;
  constructor(
    private homeService: HomeService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const searchId = params['searchId'];
      // มี searchId และเป็นตัวเลข - เอาเลขมาหาข้อมูลตาม ID
      if (searchId && !isNaN(searchId)) {
        this.Id = parseInt(searchId, 10);
        this.getHomesById(this.Id);
      } else {
        this.getHomes(); // ถ้าไม่มี searchId หรือไม่ใช่ตัวเลข ให้ดึงข้อมูลบ้านทั้งหมด
      }
    });
  }

  private getHomes(): void {
    this.homeService.getAllHomes().subscribe((data: Home[]) => {
      //.subscribe :เมธอดสำหรับรับข้อมูลจาก Observable
      // Arrow function ที่รับค่าพารามิเตอร์ dataจากObservable ซึ่งมี type เป็น array ของ Home interface/class
      console.log(data);
      this.datahomes = data;
    });
  }

  private getHomesById(homeId: number): void {
    this.homeService.getHomeById(homeId).subscribe((data: Home) => {
      //  return ข้อมูลบ้านเพียงหลังเดียว (Home)
      this.datahomes = [data]; // แสดงผลบ้านที่มี Id ตรงกับ homeId เท่านั้น
      // ห่อด้วย [] เพื่อทำให้เป็น array เนื่องจาก type Home (object เดียว)
      console.log(data);
    });
  }

  pageEditHome(homeId: number, mode: 'edithome'): void {
    this.router.navigate(['edit'], {
      //'home-officer' คือ path หลัก , homeId คือพารามิเตอร์ที่ถูกต่อท้าย path (เช่น /home-officer/5 ถ้า homeId เป็น 5)
      queryParams: { Id: homeId, mode: mode }, // queryParams: { mode: mode } คือการส่งข้อมูลเพิ่มผ่าน URL
      // โครงสร้าง: { key: value } โดยที่ key ชื่อ 'mode' และ value มาจากพารามิเตอร์ mode ผลลัพธ์ URL จะเป็นแบบนี้: /home-officer/5?mode=edit
    });
  }

  pageAddHome(Type: 'home') {
    this.router.navigate(['add'], {
      queryParams: { type: Type },
    });
  }
  viewHomeOfficer(homeId: number, mode: 'viewofficer'): void {
    this.router.navigate(['home-officer'], {
      queryParams: { Id: homeId, mode: mode },
    });
  }

  deleteHome(homeId: number): void {
    if (confirm('คุณแน่ใจว่าต้องการลบข้อมูลบ้านนี้หรือไม่?')) {
      this.homeService.deleteHome(homeId).subscribe(
        () => {
          console.log(`Home ${homeId} deleted successfully `);
          alert('ลบข้อมูลบ้านเรียบร้อยแล้ว');
          this.getHomes(); // Refresh the list after deletion
        },
        (error) => {
          console.error('Error deleting home:', error);
        }
      );
    }
  }
}
