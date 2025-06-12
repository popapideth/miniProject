import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { OfficerService } from '../../services/officer.service';
import { Officer } from '../../model/officer';

@Component({
  selector: 'app-officer-list',
  imports: [CommonModule],
  templateUrl: './officer-list.component.html',
  styleUrl: './officer-list.component.css',
})
export class OfficerListComponent implements OnInit {
  dataofficers: Officer[] = [];
  Id: any;
  constructor(
    private route: ActivatedRoute,
    private officerService: OfficerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const searchId = params['searchId'];
      // มี searchId และเป็นตัวเลข - เอาเลขมาหาข้อมูลตาม ID
      if (searchId && !isNaN(searchId)) {
        this.Id = parseInt(searchId, 10);
        this.getOfficerById(this.Id);
      } else {
        this.getOfficers();
      }
    });
  }

  private getOfficers(): void {
    this.officerService.getAllOfficers().subscribe((data: Officer[]) => {
      this.dataofficers = data;
      console.log(data);
    });
  }

  private getOfficerById(officerId: number): void {
    this.officerService.getOfficerById(officerId).subscribe((data: Officer) => {
      this.dataofficers = [data]; // แสดงผลลัพธ์เป็นอาร์เรย์เดียว
      // ห่อด้วย [] เพื่อทำให้เป็น array เนื่องจาก type Home (object เดียว)
      console.log(data);
    });
  }

  pageEditOfficer(officerId: number, mode: 'editofficer'): void {
    this.router.navigate(['edit'], {
      queryParams: { Id: officerId, mode: mode },
    });
  }

  pageAddOfficer(Type: 'officer') {
    this.router.navigate(['add'], {
      queryParams: { type: Type },
    });
  }

  viewHomeOfficer(officerId: number, mode: 'viewhome') {
    this.router.navigate(['home-officer'], {
      queryParams: { Id: officerId, mode: mode },
    });
  }

  deleteOfficer(officerId: number) {
    if (confirm('คุณแน่ใจว่าต้องการลบข้อมูลบ้านนี้หรือไม่?'))
      this.officerService.deleteOfficer(officerId).subscribe(
        () => {
          console.log(`Officer ${officerId} deleted successFully`);
          alert('ลบข้อมูลบ้านเรียบร้อยแล้ว');
          this.getOfficers();
        },
        (error) => {
          console.error('Error deleteing Officer', error);
        }
      );
  }
}
