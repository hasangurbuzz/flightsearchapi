package dev.hasangurbuz.flightsearchapi.service.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.domain.QFlight;
import dev.hasangurbuz.flightsearchapi.repository.FlightRepository;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import dev.hasangurbuz.flightsearchapi.service.PagedResult;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.OrderDTO;
import org.openapitools.model.OrderDirectionDTO;
import org.openapitools.model.OrderSortDTO;
import org.openapitools.model.PageRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Flight create(Flight flight) {
        if (flight == null){
            return null;
        }
        return flightRepository.save(flight);
    }

    @Override
    public PagedResult<Flight> search(Airport departureAirport,
                                      Airport arrivalAirport,
                                      LocalDate departureDate,
                                      PageRequestDTO pageRequest) {

        QFlight qFlight = QFlight.flight;

        QueryResults<Flight> flightQueryResults = queryFactory.selectFrom(qFlight)
                .where(
                        qFlight.departureAirport.id.eq(departureAirport.getId())
                                .and(qFlight.arrivalAirport.id.eq(arrivalAirport.getId()))
                                .and(qFlight.departureDate.dayOfMonth().eq(departureDate.getDayOfMonth()))
                                .and(qFlight.departureDate.month().eq(departureDate.getMonthValue()))
                                .and(qFlight.departureDate.year().eq(departureDate.getYear()))

                ).offset(pageRequest.getStart())
                .limit(pageRequest.getLimit())
                .orderBy(getOrder(pageRequest.getOrder()))
                .fetchResults();


        PagedResult<Flight> results = new PagedResult<>();

        results.setTotal((int) (flightQueryResults.getTotal()));
        results.setItems(flightQueryResults.getResults());
        return results;
    }


    private OrderSpecifier getOrder(OrderDTO order){
        QFlight qFlight = QFlight.flight;
        OrderSortDTO sort = order.getSort();
        OrderDirectionDTO direction = order.getDirection();

        if (sort.equals(OrderSortDTO.DATE)){
            if (direction.equals(OrderDirectionDTO.ASC)){
                return qFlight.departureDate.asc();
            }
            return qFlight.departureDate.desc();
        }
        if (direction.equals(OrderDirectionDTO.ASC)){
            return qFlight.price.asc();
        }
        return qFlight.price.desc();
    }
}
