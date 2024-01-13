package dev.hasangurbuz.flightsearchapi.mapper;

import dev.hasangurbuz.flightsearchapi.domain.Currency;
import dev.hasangurbuz.flightsearchapi.domain.Price;
import org.openapitools.model.PriceDTO;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

    public PriceDTO toDto(Price price) {
        PriceDTO dto = new PriceDTO();
        dto.setAmount(price.getAmount());
        dto.setCurrency(price.getCurrency().name());
        return dto;
    }

    public Price toEntity(PriceDTO dto) {
        Price price = new Price();
        price.setAmount(dto.getAmount());
        price.setCurrency(Currency.valueOf(dto.getCurrency()));
        return price;
    }

}
