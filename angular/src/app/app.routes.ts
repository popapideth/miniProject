// ที่เก็บเส้นทาง (Routes) ทั้งหมด ในระบบ Standalone Angular Application(Component ที่ทำงานได้เอง โดยไม่ต้องถูกประกาศอยู่ใน Module( ไม่ต้องพึ่งพา NgModule เลย))
import { Routes } from '@angular/router';
import { HomeListComponent } from './component/home-list/home-list.component';
import { OfficerListComponent } from './component/officer-list/officer-list.component';
import { HomeOfficerComponent } from './component/home-officer/home-officer.component';
import { EditComponent } from './component/edit/edit.component';
import { AppComponent } from './app.component';
import { SearchpageComponent } from './component/searchpage/searchpage.component';
import { AdddataComponent } from './component/adddata/adddata.component';

export const routes: Routes = [
  { path: 'homes', component: HomeListComponent },
  { path: 'officer', component: OfficerListComponent },
  { path: 'home-officer', component: HomeOfficerComponent },
  { path: 'edit', component: EditComponent },
  { path: 'search', component: SearchpageComponent }, // Search route
  { path: 'add', component: AdddataComponent },
  { path: '', component: HomeListComponent }, // Default route
];
