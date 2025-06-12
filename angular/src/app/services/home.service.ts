import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Home } from '../model/home';

@Injectable({
  providedIn: 'root',
})
export class HomeService {
  private apiUrl = 'http://localhost:8080'; // URL ของ API ที่ใช้ในการดึงข้อมูล
  // อย่าลืมใส่ / ก่อนใส่path

  constructor(private http: HttpClient) {}

  getAllHomes(): Observable<Home[]> {
    return this.http.get<Home[]>(this.apiUrl + '/home');
  }

  getHomeById(homeId: number): Observable<Home> {
    return this.http.get<Home>(this.apiUrl + '/home/id/' + homeId);
  }

  deleteHome(homeId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + '/home/' + homeId);
  }

  updateHome(home: Home): Observable<Home> {
    return this.http.put<Home>(this.apiUrl + '/home/update/', home);
  }

  addNewHome(home: Home): Observable<Home> {
    return this.http.post<Home>(
      this.apiUrl + '/home/addHomeWithOfficers/',
      home
    );
  }

  selectOfficerToHome(homeId: number, officerId: number[]): Observable<Home> {
    return this.http.post<Home>(
      this.apiUrl + `/home/selectOfficerToHome/${homeId}`,
      officerId.map((id) => ({ officerId: id })) // แปลงให้อยู่ในรูปแบบที่ backend รับ
    );
  }
}
