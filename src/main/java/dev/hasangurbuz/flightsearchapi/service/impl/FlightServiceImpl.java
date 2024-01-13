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
import dev.hasangurbuz.flightsearchapi.mapper.PriceMapper;
import dev.hasangurbuz.flightsearchapi.repository.FlightRepository;
import dev.hasangurbuz.flightsearchapi.repository.PriceRepository;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import dev.hasangurbuz.flightsearchapi.service.PagedResult;
import dev.hasangurbuz.flightsearchapi.service.exception.DateException;
import dev.hasangurbuz.flightsearchapi.service.exception.FlightNotFoundException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final PriceRepository priceRepository;
    private final JPAQueryFactory queryFactory;
    private final QFlight qFlight = QFlight.flight;
    private final PriceMapper priceMapper;
    private final AirportService airportService;

    @Override
    public Flight create(FlightRequestDTO flightRequest) {
        Airport originAirport = airportService.findById(flightRequest.getOriginAirportId());
        Airport destinationAirport = airportService.findById(flightRequest.getDestinationAirportId());

        Price price = priceMapper.toEntity(flightRequest.getPrice());
        price = priceRepository.save(price);

        Flight flight = new Flight();
        flight.setPrice(price);
        flight.setOrigin(originAirport);
        flight.setDestination(destinationAirport);
        flight.setDepartureDate(DateHelper.toUTC(flightRequest.getDepartureDate()));
        flight.setReturnDate(DateHelper.toUTC(flightRequest.getReturnDate()));

        return flightRepository.save(flight);
    }

    @Override
    public PagedResult<Flight> search(FlightSearchRequestDTO searchRequest) {
        boolean isOneway = searchRequest.getReturnDate() == null;

        Airport originAirport = airportService.findById(searchRequest.getOriginAirportId());
        Airport destinationAirport = airportService.findById(searchRequest.getDestinationAirportId());
        LocalDate departureDate = searchRequest.getDepartureDate();
        LocalDate returnDate = searchRequest.getReturnDate();
        PageRequestDTO pageRequest = searchRequest.getPageRequest();


        QueryResults<Flight> flightQueryResults = queryFactory.selectFrom(qFlight)
                .where(
                        qFlight.origin.id.eq(originAirport.getId())
                                .and(qFlight.destination.id.eq(destinationAirport.getId()))
                                .and(dateEq(qFlight.departureDate, DateHelper.toUTC(departureDate)))
                                .and(dateEq(qFlight.returnDate, DateHelper.toUTC(returnDate)))


                ).offset(pageRequest.getStart())
                .limit(pageRequest.getLimit())
                .orderBy(getOrder(pageRequest.getOrder()))
                .fetchResults();

        PagedResult<Flight> flightPagedResult = new PagedResult<>();
        flightPagedResult.setItems(flightQueryResults.getResults());
        flightPagedResult.setTotal((int) flightQueryResults.getTotal());

        return flightPagedResult;
    }

    @Override
    public Flight findById(Long id) {
        Optional<Flight> queryResult = flightRepository.findById(id);
        if (queryResult.isPresent()) {
            return queryResult.get();
        }
        throw new FlightNotFoundException(id);
    }

    @Override
    public boolean existingFlightByAirportId(Long originId) {
        Flight flight = queryFactory.selectFrom(qFlight)
                .where(qFlight.origin.id.eq(originId))
                .fetchFirst();
        if (flight != null) return true;

        flight = queryFactory.selectFrom(qFlight)
                .where(qFlight.destination.id.eq(originId))
                .fetchFirst();

        if (flight != null) return true;

        return false;
    }

    @Override
    public Flight update(Long id, FlightRequestDTO flightRequest) {
        OffsetDateTime departureDate = flightRequest.getDepartureDate();
        OffsetDateTime returnDate = flightRequest.getReturnDate();
        if (!DateHelper.isDateAfterThanCurrent(departureDate)) {
            throw new DateException("Departure date cannot be earlier than current date");
        }

        if (returnDate != null) {
            if (returnDate.isBefore(departureDate)) {
                throw new DateException("Return date cannot be earlier than departure date");
            }
        }
        Flight flight = findById(id);
        Price oldPrice = flight.getPrice();
        Price newPrice = priceMapper.toEntity(flightRequest.getPrice());
        newPrice.setId(oldPrice.getId());
        newPrice = priceRepository.save(newPrice);
        flight.setPrice(newPrice);

        Airport originAirport = airportService.findById(flightRequest.getOriginAirportId());
        Airport destinationAirport = airportService.findById(flightRequest.getDestinationAirportId());

        flight.setOrigin(originAirport);
        flight.setDestination(destinationAirport);
        flight.setDepartureDate(departureDate);
        flight.setReturnDate(returnDate);

        return flightRepository.save(flight);
    }

    @Override
    public void delete(Flight flight) {
        priceRepository.deleteById(flight.getPrice().getId());
        flightRepository.delete(flight);
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

    private BooleanExpression dateEq(DateTimePath<OffsetDateTime> column, Date value) {
        if (value == null) {
            return column.isNull();
        }
        return Expressions.dateTimeOperation(
                        Date.class,
                        Ops.DateTimeOps.DATE,
                        column)
                .eq(value);
    }
}
