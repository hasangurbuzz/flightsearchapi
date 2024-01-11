package dev.hasangurbuz.flightsearchapi.service.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.domain.Price;
import dev.hasangurbuz.flightsearchapi.domain.QFlight;
import dev.hasangurbuz.flightsearchapi.helper.DateHelper;
import dev.hasangurbuz.flightsearchapi.repository.FlightRepository;
import dev.hasangurbuz.flightsearchapi.repository.PriceRepository;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import dev.hasangurbuz.flightsearchapi.service.PagedResult;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.OrderDTO;
import org.openapitools.model.OrderDirectionDTO;
import org.openapitools.model.OrderSortDTO;
import org.openapitools.model.PageRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final PriceRepository priceRepository;
    private final JPAQueryFactory queryFactory;
    private final QFlight qFlight = QFlight.flight;

    @Override
    public Flight create(Flight flight) {
        if (flight == null) {
            return null;
        }

        Price price = priceRepository.save(flight.getPrice());
        flight.setPrice(price);

        return flightRepository.save(flight);
    }

    @Override
    public PagedResult<Flight> search(Airport departureAirport,
                                      Airport arrivalAirport,
                                      LocalDate departureDate,
                                      LocalDate returnDate,
                                      PageRequestDTO pageRequest) {

        QueryResults<Flight> flightQueryResults = queryFactory.selectFrom(qFlight)
                .where(
                        qFlight.origin.id.eq(departureAirport.getId())
                                .and(qFlight.destination.id.eq(arrivalAirport.getId()))
                                .and(dateEq(qFlight.departureDate, DateHelper.toUTC(departureDate)))
                                .and(dateEq(qFlight.returnDate, DateHelper.toUTC(returnDate)))


                ).offset(pageRequest.getStart())
                .limit(pageRequest.getLimit())
                .orderBy(getOrder(pageRequest.getOrder()))
                .fetchResults();


        PagedResult<Flight> results = new PagedResult<>();

        results.setTotal((int) (flightQueryResults.getTotal()));
        results.setItems(flightQueryResults.getResults());
        return results;
    }


    private OrderSpecifier getOrder(OrderDTO order) {
        OrderSortDTO sort = order.getSort();
        OrderDirectionDTO direction = order.getDirection();

        if (sort.equals(OrderSortDTO.DATE)) {
            if (direction.equals(OrderDirectionDTO.ASC)) {
                return qFlight.departureDate.asc();
            }
            return qFlight.departureDate.desc();
        }
        if (direction.equals(OrderDirectionDTO.ASC)) {
            return qFlight.price.amount.asc();
        }
        return qFlight.price.amount.desc();
    }

    private BooleanExpression dateEq(DateTimePath<OffsetDateTime> column, Date value){
        return Expressions.dateTimeOperation(
                Date.class,
                Ops.DateTimeOps.DATE,
                column)
                .eq(value);
    }
}
