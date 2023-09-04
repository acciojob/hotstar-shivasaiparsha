package com.driver.Transformers;

import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;

public class ProductionHouseTransformer {
    public static ProductionHouse convertDtoIntoEntity(ProductionHouseEntryDto productionHouseEntryDto){
        ProductionHouse productionHouse=new ProductionHouse(productionHouseEntryDto.getName());
        productionHouse.setRatings(0);
        return productionHouse;
    }
}
