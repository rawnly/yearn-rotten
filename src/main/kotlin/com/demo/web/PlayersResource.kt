package com.demo.web

import com.demo.dao.player.PlayerEntity
import com.demo.dao.player.PlayerServiceImpl
import com.demo.jobs.sync.SyncPlayersServiceImpl
import com.demo.utils.DateUtils
import io.netty.handler.codec.http.HttpResponseStatus
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper


class RangeNotValidException(message: String) : RuntimeException(message) {}

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PlayersResource {
    @Inject
    private lateinit var playerService: PlayerServiceImpl

    @Inject
    private lateinit var syncPlayersService: SyncPlayersServiceImpl

    @ServerExceptionMapper
    fun mapException(ex: RangeNotValidException) : Response {
        return Response.status(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE.code()).entity(mapOf("error" to ex.message)).build()
    }

    @GET
    fun getClubPlayers(
        @QueryParam("club_id") clubID: String?,
        @QueryParam("position") position: String?,
        @QueryParam("active") active: Boolean?,
        @QueryParam("birth_year_start") birthYearStart: Int?,
        @QueryParam("birth_year_end") birthYearEnd: Int?,
    ): List<PlayerEntity> {
        val startDate = birthYearStart?.let { DateUtils.startOfTheYear(it) }
        val endDate = birthYearEnd?.let { DateUtils.endOfTheYear(it) }

        val birthYearsRange  = when {
           startDate != null && endDate != null -> startDate..endDate
           startDate != null -> startDate..DateUtils.endOfTheYear(startDate.year)
           endDate != null -> throw RangeNotValidException("birth_date_end must be used with birth_date_start")
           else -> null
        }

        return playerService.getPlayers(position, active, clubID, birthYearsRange)
    }

    @POST
    @Path("/start-job")
    @Transactional
    fun startJob(@QueryParam("club_id") clubID: String): Response {
        syncPlayersService.syncClubPlayers(clubID)

        return Response.ok().build()
    }

    @GET
    @Path("/outdated")
    @Transactional
    fun getOutdated(): Response {
       val players = playerService.findOutdated("5", 11)

         return Response.ok(players).build()
    }
}

