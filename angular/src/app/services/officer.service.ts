import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Officer } from '../model/officer';

@Injectable({
  providedIn: 'root',
})
export class OfficerService {
  private apiUrl = 'http://localhost:8080'; // URL ของ API ที่ใช้ในการดึงข้อมูล

  constructor(private http: HttpClient) {}

  getAllOfficers(): Observable<Officer[]> {
    return this.http.get<Officer[]>(this.apiUrl + '/officer');
  }

  getOfficerById(officerId: number): Observable<Officer> {
    return this.http.get<Officer>(this.apiUrl + '/officer/id/' + officerId);
  }
  // ก่อนลบ Officer ต้องลบความสัมพันธ์ในตารางกลาง (home_officer, many-to-many)
  deleteOfficer(officerId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + '/officer/' + officerId);
  }

  updateOfficer(officer: Officer): Observable<Officer> {
    return this.http.put<Officer>(this.apiUrl + '/officer/update/', officer);
  }

  addNewOfficer(officer: Officer): Observable<Officer> {
    return this.http.post<Officer>(
      this.apiUrl + '/officer/addOfficer/',
      officer
    );
  }

  selectHomeToOfficer(
    officerId: number,
    homeId: number[]
  ): Observable<Officer> {
    return this.http.post<Officer>(
      this.apiUrl + `/officer/selectHomeToOfficer/${officerId}`,
      homeId.map((id) => ({ homeId: id })) // แปลงให้อยู่ในรูปแบบที่ backend รับ
    );
  }
}
