package dev.hasangurbuz.flightsearchapi.service.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.QAirport;
import dev.hasangurbuz.flightsearchapi.repository.AirportRepository;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import dev.hasangurbuz.flightsearchapi.service.PagedResult;
import dev.hasangurbuz.flightsearchapi.service.exception.AirportExistsException;
import dev.hasangurbuz.flightsearchapi.service.exception.AirportNotFoundException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final QAirport qAirport = QAirport.airport;
    private final JPAQueryFactory queryFactory;


    @Override
    public Airport create(AirportRequestDTO createRequest) {
        String cityName = createRequest.getCity();
        Airport airport = queryFactory.selectFrom(qAirport)
                .where(qAirport.city.containsIgnoreCase(cityName))
                .fetchFirst();

        if (airport != null) {
            throw new AirportExistsException(cityName);
        }

        airport = new Airport();
        airport.setCity(createRequest.getCity());
        return airportRepository.save(airport);
    }

    @Override
    public Airport findById(Long id) {
        Optional<Airport> result = airportRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }

        throw new AirportNotFoundException(id);
    }

    @Override
    public PagedResult<Airport> search(String term, PageRequestDTO pageRequest) {
        QueryResults<Airport> airportQueryResults = queryFactory.selectFrom(qAirport)
                .where(
                        qAirport.city.containsIgnoreCase(term)
                )
                .orderBy(getOrder(pageRequest.getOrder()))
                .offset(pageRequest.getStart())
                .limit(pageRequest.getLimit())
                .fetchResults();

        PagedResult<Airport> result = new PagedResult<>();
        result.setItems(airportQueryResults.getResults());
        result.setTotal((int) airportQueryResults.getTotal());

        return result;
    }

    @Override
    public Airport update(Long id, AirportRequestDTO updateRequest) {
        Airport airport = findById(id);
        airport.setCity(updateRequest.getCity());
        airport = airportRepository.save(airport);
        return airport;
    }

    @Override
    public void delete(Airport airport) {
        airportRepository.delete(airport);
    }

    private OrderSpecifier getOrder(OrderDTO order) {
        OrderSortDTO sort = order.getSort();
        OrderDirectionDTO direction = order.getDirection();

        if (direction.equals(OrderDirectionDTO.ASC)) {
            return qAirport.city.asc();
        }
        return qAirport.city.desc();
    }


}
