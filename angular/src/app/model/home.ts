import { Officer } from './officer';

export interface Home {
  homeId: number;
  homeName: string;
  officers: Officer[];
}
