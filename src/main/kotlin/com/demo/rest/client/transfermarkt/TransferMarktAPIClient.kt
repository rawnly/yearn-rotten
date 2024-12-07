package com.demo.rest.client.transfermarkt

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "transfermarkt-api")
interface TransferMarktAPIClient {
    @GET
    @Path("/clubs/{clubID}/players")
    @Produces("application/json")
    fun getClubPlayers(clubID: String): TransferMarktClubPlayersResponse
}