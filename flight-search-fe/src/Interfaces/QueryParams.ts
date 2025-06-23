import { Sorter } from "./Sorter";

export interface QueryParams {
  page: number;
  departureCode: string;
  arrivalCode: string;
  departureDate?: string;
  returnDate?: string;
  noAdults?: number;
  currency: string;
  nonStops: boolean;
  sortBy?: Sorter;
}
