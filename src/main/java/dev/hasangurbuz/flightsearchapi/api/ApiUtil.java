package dev.hasangurbuz.flightsearchapi.api;

import org.openapitools.model.OrderDTO;
import org.openapitools.model.PageRequestDTO;


public class ApiUtil {
    public static PageRequestDTO normalizePageRequest(PageRequestDTO pageRequestDTO) {
        if (pageRequestDTO == null) {
            pageRequestDTO = new PageRequestDTO();
        }
        if (pageRequestDTO.getOrder() == null) {
            pageRequestDTO.setOrder(new OrderDTO());
        }
        return pageRequestDTO;
    }
}
