import { Home } from './home';

export interface Officer {
  officerId: number;
  officerName: string;
  officerAge: string;
  homes: Home[];
}
