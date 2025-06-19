export interface QueryParams {
  page?: number;
  departureCode: string;
  arrivalCode: string;
  departureDate?: string;
  noAdults: number;
  currency: string;
  nonStops?: boolean;
}
