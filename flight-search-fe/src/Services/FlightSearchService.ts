import { QueryParams } from "../Interfaces/QueryParams"
import { Flight } from "../Interfaces/Flight"
import { Api } from "./Api"

export const SearchFlightsService = {
    getFlights: async (params?: QueryParams) => {
        return Api.get(`searchFlights`, params)
    },

    getDetailedFlight: async (flight: Flight) => {
        return Api.get(`searchFlightById/${flight.id}`)
    },

    getAirports: async (query: String) => {
        return Api.get(`searchAirports/${query}`)
    }
}